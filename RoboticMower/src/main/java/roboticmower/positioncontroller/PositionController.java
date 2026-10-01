package roboticmower.positioncontroller;

import static sysmlinjava.attributetypes.RReal.adjacentOf;
import static sysmlinjava.attributetypes.RReal.hypotenuseOf;
import static sysmlinjava.attributetypes.RReal.oppositeOf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

import roboticmower.ports.PositionVectorReceiver;
import roboticmower.ports.VelocityControlTransmitter;
import sysmlinjava.attributetypes.DirectionRadians;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.DurationMilliseconds;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.ListOrdered;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.SpeedMetersPerSecond;
import sysmlinjava.attributetypes.Vector2D;
import sysmlinjava.attributetypes.VelocityMetersPerSecondRadians;
import sysmlinjava.attributetypes.Waypoint;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.events.TimeEvent;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalEvent;

/**
 * Controller of the mower's position. The controller periodically senses the
 * position of the mower and compares the mower's current position to the next
 * waypoint to which the mower is to move. It uses this comparison to
 * calculate the next velocity of the mower and transmits the velocity to the
 * mower.
 * 
 * @author ModelerOne
 *
 */
public class PositionController extends SysMLPart
{
	/**
	 * Port by which the controller senses the position of the mower.
	 */
	@Port
	public	PositionVectorReceiver vectorReceiver;
	/**
	 * Port by which the controller transmits the next velocity of the mower to the
	 * mower
	 */
	@Port
	public	VelocityControlTransmitter velocityTransmitter;

	/**
	 * Value for this controller's (fixed) position
	 */
	@Attribute
	public Point2D controllerPosition;
	/**
	 * Value for the nominal mower speed
	 */
	@Attribute
	public Vector2D mowerVector;
	/**
	 * Value for the nominal mower speed
	 */
	@Attribute
	SpeedMetersPerSecond mowerSpeed;
	/**
	 * Value for the distance increment at which controls are sent
	 */
	@Attribute
	DistanceMeters controlDistance;
	/**
	 * Value for list of waypoints the mower is to navigate. Waypoints are generated
	 * for a typical mowing pattern of simple back-and-forth straight paths along
	 * the length of the lawn. The waypoints are at 0.5 meter intervals along the
	 * path. Waypoints are calculated for this model execution/simulation in the
	 * {@code createValues()} method below.
	 */
	@Attribute
	ListOrdered<Waypoint> waypoints;
	/**
	 * Value of the next waypoint the mower is planned to navigate to
	 */
	@Attribute
	Waypoint nextWaypointPlanned;
	/**
	 * Value of the current waypoint the mower actually navigated to
	 */
	@Attribute
	public	Waypoint currWaypointActual;
	/**
	 * Value of the current waypoint the mower is planned to navigate to
	 */
	@Attribute
	public	Waypoint currWaypointPlanned;
	/**
	 * Value of the nominal time between waypoints
	 */
	@Attribute
	DurationSeconds interWaypointTime;

	/**
	 * Time event for time to sense the position of the mower (for subsequent
	 * control of its velocity)
	 */
	@TimeEvent
	SysMLTimeEvent positionSensingTime;

	/**
	 * Index into the waypoints to be followed
	 */
	int nextWaypointIndex;

	/**
	 * Constructor which invokes all of the creation/initialization of the
	 * controller's properties
	 */
	public PositionController()
	{
		super("PositionController", 0L);
	}

	/**
	 * Initializes the controller by setting the waypoints and initiating the first
	 * sensing of the mower's position
	 */
	@Action
	public void initialize()
	{
		logger.info("initializing");
		nextWaypointIndex = 0;
		nextWaypointPlanned = waypoints.get(nextWaypointIndex);
		currWaypointPlanned.setValue(nextWaypointPlanned);
		onPositionSensingTime();
	}

	/**
	 * Initiates the next sensing of the mower's position by sending a signal to the
	 * mower that will be reflected and used to determine the mower's distance and
	 * direction from the controller. Operation also sets timer for next sensing.
	 */
	@Action
	public void onPositionSensingTime()
	{
		System.out.println(getClass().getSimpleName() + ".OnPositionSensingTime():");
		vectorReceiver.transmit(controllerPosition);
		stateMachine.get().startTimer(positionSensingTime);
	}

	/**
	 * Receives the vector value that was sensed for the mower. The vector is used
	 * to calculate the mower's current position which is then compared against the
	 * next waypoint for calculation of the next velocity (speed and direction) the
	 * mower needs to travel to the next waypoint.
	 * 
	 * @param vector  vector (distance and direction) of the mower
	 * @param instant time of the vector
	 */
	@Action
	public void onPositionSensed(Vector2D vector, InstantMilliseconds instant)
	{
		System.out.println(getClass().getSimpleName() + ".OnPositionSensed():");
		logger.info(String.format(vector.toString()));
		mowerVector.setValue(vector);
		DistanceMeters distance = new DistanceMeters(vector.value);
		DistanceMeters xDelta = new DistanceMeters(oppositeOf(distance, vector.direction));
		DistanceMeters yDelta = new DistanceMeters(adjacentOf(distance, vector.direction));
		currWaypointActual.setValue(controllerPosition.xValue + xDelta.value, controllerPosition.yValue + yDelta.value, instant);

		nextWaypointPlanned = waypoints.get(nextWaypointIndex);
		DistanceMeters xDiff = new DistanceMeters(nextWaypointPlanned.xValue - currWaypointActual.xValue);
		DistanceMeters yDiff = new DistanceMeters(nextWaypointPlanned.yValue - currWaypointActual.yValue);
		currWaypointPlanned.setValue(nextWaypointPlanned);
		
		DistanceMeters nextDistance = new DistanceMeters(hypotenuseOf(xDiff, yDiff));
		DirectionRadians nextDirection = DirectionRadians.direction(currWaypointActual, nextWaypointPlanned);
		SpeedMetersPerSecond nextSpeed = new SpeedMetersPerSecond(nextDistance.dividedBy(interWaypointTime));
		VelocityMetersPerSecondRadians nextVelocity = new VelocityMetersPerSecondRadians(nextSpeed, nextDirection);
		velocityTransmitter.transmit(nextVelocity);
		nextWaypointIndex++;
	}

	@Override
	public void stop()
	{
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createAttributes()
	{
		controllerPosition = new Point2D(9.5, 5);
		mowerVector = new Vector2D(0, 0);
		mowerSpeed = new SpeedMetersPerSecond(0.2);
		controlDistance = new DistanceMeters(0.5);
		interWaypointTime = new DurationSeconds(controlDistance.dividedBy(mowerSpeed));
		InstantMilliseconds startTime = InstantMilliseconds.now().add(DurationSeconds.of(3.0));
		waypoints = new ListOrdered<>();
		nextWaypointPlanned = new Waypoint();
		currWaypointActual = new Waypoint();
		currWaypointPlanned = new Waypoint();

		List<String> waypointLines = new ArrayList<>();
		nextWaypointIndex = 0;
		for (int i = 0; i < 9; i++)
		{
			StringJoiner joiner = new StringJoiner("][", "[", "]");
			double x = i + 0.5;
			if ((i % 2) == 0)
			{
				int j = 1;
				while (j <= 18)
				{
					double y = j * 0.5;
					InstantMilliseconds time = startTime.add(DurationSeconds.of(interWaypointTime.multipliedBy(nextWaypointIndex)));
					waypoints.add(new Waypoint(x, y, time));
					joiner.add(String.format("%2.2f,%2.2f", x, y));
					nextWaypointIndex++;
					j++;
				}
				j--;
				x += 0.5;
				double y = j * 0.5;
				InstantMilliseconds time = startTime.add(DurationSeconds.of(interWaypointTime.multipliedBy(nextWaypointIndex)));
				waypoints.add(new Waypoint(x, y, time));
				joiner.add(String.format("%2.2f,%2.2f", x, y));
				nextWaypointIndex++;
			}
			else
			{
				int j = 18;
				while (j >= 1)
				{
					double y = j * 0.5;
					InstantMilliseconds time = startTime.add(DurationSeconds.of(interWaypointTime.multipliedBy(nextWaypointIndex)));
					waypoints.add(new Waypoint(x, y, time));
					joiner.add(String.format("%2.2f,%2.2f", x, y));
					nextWaypointIndex++;
					j--;
				}
				j++;
				x += 0.5;
				double y = j * 0.5;
				InstantMilliseconds time = startTime.add(DurationSeconds.of(interWaypointTime.multipliedBy(nextWaypointIndex)));
				waypoints.add(new Waypoint(x, y, time));
				joiner.add(String.format("%2.2f,%2.2f", x, y));
				nextWaypointIndex++;
			}
			waypointLines.add(joiner.toString());
		}
		StringJoiner allLines = new StringJoiner("\n");
		waypointLines.forEach(waypointLine -> allLines.add(waypointLine));
		logger.info("waypoints plan...\n" + allLines.toString());
	}

	@Override
	protected void createEvents()
	{
		positionSensingTime = new SysMLTimeEvent("PositionSensingTime", DurationMilliseconds.of(interWaypointTime), Optional.empty());
	}

	@Override
	protected void createPorts()
	{
		vectorReceiver = new PositionVectorReceiver(this, 0L, "PositionVectorReceiver");
		velocityTransmitter = new VelocityControlTransmitter(this, 0L, "VelocityControlTransmitter");
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new PositionControllerStateMachine(this));
	}
}
