package roboticmower.mower;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static sysmlinjava.attributetypes.RReal.hypotenuseOf;

import java.util.Optional;

import roboticmower.info.WheelsControl;
import roboticmower.ports.PositionVectorReflector;
import roboticmower.ports.VelocityControlReceiver;
import roboticmower.ports.WheelsControlTransmitter;
import sysmlinjava.attributetypes.DirectionRadians;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.attributetypes.RevolutionsPerMinute;
import sysmlinjava.attributetypes.Vector2D;
import sysmlinjava.attributetypes.VelocityMetersPerSecondRadians;
import sysmlinjava.attributetypes.Waypoint;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.probability.SysMLNormalProbabilityDistribution;
import sysmlinjava.states.FinalEvent;

/**
 * Controller for the mower's movements. The controller is periodically
 * stimulated for its vector position (direction and distance) by the position
 * controller. It reflects (transmits) this vector back to the position
 * controller. The controller then receives a new controlled velocity (speed and
 * direction) from the position controller, thereby having the mower's movements
 * controlled by the position controller.
 * 
 * @author ModelerOne
 * @see roboticmower.mower.MowerControllerStateMachine
 */
public class MowerController extends SysMLPart
{
	/**
	 * Port to reflect the position vector back to the position controller
	 */
	@Port
	public	PositionVectorReflector reflector;
	/**
	 * Port to receive the mower's next velocity from the position controller
	 */
	@Port
	public	VelocityControlReceiver velocityReceiver;
	/**
	 * Port to transmit controls of the mower's wheels to the drive subsystem
	 */
	@Port
	public	WheelsControlTransmitter wheelsControlTransmitter;

	/**
	 * Value of the mower's last sensed position
	 */
	@Attribute
	public Waypoint lastPosition;
	/**
	 * Value of the mower's current velocity
	 */
	@Attribute
	public VelocityMetersPerSecondRadians velocity;
	/**
	 * Value of the mower's assumed mean error in speed (control vs actual)
	 */
	@Attribute
	RReal speedErrorMean;
	/**
	 * Value of the mower's assumed standard deviation of error in speed (control vs
	 * actual)
	 */
	@Attribute
	RReal speedErrorStdDev;
	/**
	 * Value of the mower's assumed mean error in heading (control vs actual)
	 */
	@Attribute
	RReal headingErrorMean;
	/**
	 * Value of the mower's assumed standard deviation of error in heading (control
	 * vs actual)
	 */
	@Attribute
	RReal headingErrorStdDev;
	/**
	 * Value of normal probability distribution of mower's error in speed (control
	 * vs actual)
	 */
	@Attribute
	SysMLNormalProbabilityDistribution speedErrorDistribution;
	/**
	 * Value of normal probability distribution of mower's error in heading (control
	 * vs actual)
	 */
	@Attribute
	SysMLNormalProbabilityDistribution headingErrorDistribution;

	/**
	 * Constructor
	 * 
	 * @param mower system of which this controller is a subsystem
	 */
	public MowerController(Mower mower)
	{
		super(Optional.of(mower), "MowerController", 0L);
	}

	/**
	 * Initializes the subsystem by initializing the mower's "last" (starting)
	 * position.
	 */
	public void initialize()
	{
		logger.info("initializing");
		lastPosition.time = InstantMilliseconds.now();
	}

	/**
	 * Response to sensing of the mower's position by the position controller.
	 * Receives the position of the position controller (simulating receipt of a
	 * distance/directional signal) and uses the mower's current position to
	 * calculate a vector (distance/direction) from the position controller to this
	 * mower. The vector is then transmitted back to the position controller
	 * (simulating the reflected distance/directional signal) for its use in
	 * calculating the mower's current position.
	 * 
	 * The mower's current position is determined by the position of the mower at
	 * its last sensing time and the velocity of the mower it was controlled to
	 * follow after the last sensing.
	 * 
	 * @param point2D location of the position controller
	 */
	@Action
	public void onPositionSensing(Point2D point2D)
	{
		InstantMilliseconds now = InstantMilliseconds.now();
		DurationSeconds moveDuration = now.subtracted(lastPosition.time);
		velocity.toNextRandomDelta();
		Waypoint currentPosition = lastPosition.moved(velocity, moveDuration);
		DistanceMeters xDelta = new DistanceMeters(abs(point2D.xValue - currentPosition.xValue));
		DistanceMeters yDelta = new DistanceMeters(abs(point2D.yValue - currentPosition.yValue));
		DistanceMeters distance = new DistanceMeters(hypotenuseOf(xDelta, yDelta));
		DirectionRadians direction = DirectionRadians.direction(point2D, currentPosition);
		Vector2D vector = new Vector2D(distance, direction);
		reflector.transmit(vector);
		lastPosition.setValue(currentPosition.xValue, currentPosition.yValue, now);
	}

	/**
	 * Response to receipt of the controlled velocity of the mower. Velocity of the
	 * mower is set to the control value and used to determine controls of the drive
	 * wheels.
	 * 
	 * @param controlVelocity velocity to be used by the mower from current location
	 *                        until new velocity received
	 */
	@Action
	public void onVelocityControl(VelocityMetersPerSecondRadians controlVelocity)
	{
		logger.info(String.format("velocity=%1.4f mps, %1.4f rad from position=[%2.3f,%2.3f] ", controlVelocity.value, controlVelocity.heading.value, lastPosition.xValue, lastPosition.yValue));
		velocity.setValue(controlVelocity);
		RevolutionsPerMinute rpm = RevolutionsPerMinute.valueOf(((Mower)context.get()).drive.wheelDiameter, controlVelocity);
		WheelsControl wheelsControl = new WheelsControl(rpm, controlVelocity.heading.toDegrees());
		wheelsControlTransmitter.transmit(wheelsControl);
	}

	@Override
	public void stop()
	{
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createAttributes()
	{
		lastPosition = new Waypoint(0, 0.5, InstantMilliseconds.MIN);
		speedErrorMean = new RReal(0);
		speedErrorStdDev = new RReal(0.2 * 0.1);
		headingErrorMean = new RReal(0);
		headingErrorStdDev = new RReal(2 * PI / 180);
		speedErrorDistribution = new SysMLNormalProbabilityDistribution(speedErrorMean, speedErrorStdDev);
		headingErrorDistribution = new SysMLNormalProbabilityDistribution(headingErrorMean, headingErrorStdDev);

		velocity = new VelocityMetersPerSecondRadians(0, 0, speedErrorDistribution, headingErrorDistribution);
	}

	@Override
	protected void createPorts()
	{
		reflector = new PositionVectorReflector(this, 0L, "PositionVectorStimulator");
		velocityReceiver = new VelocityControlReceiver(this, 0L, "VelocityReceiver");
		wheelsControlTransmitter = new WheelsControlTransmitter(this, 0L, "WheelsControlTransmitter");
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new MowerControllerStateMachine(this));
	}
}
