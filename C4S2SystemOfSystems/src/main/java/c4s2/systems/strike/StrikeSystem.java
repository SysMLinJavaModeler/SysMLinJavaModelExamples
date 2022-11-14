package c4s2.systems.strike;

import java.util.Optional;
import c4s2.common.events.TargetEngagedEvent;
import c4s2.common.messages.StrikeSystemMonitorMessage;
import c4s2.common.objects.information.StrikeOrdnance;
import c4s2.common.objects.information.StrikeSystemControl;
import c4s2.common.objects.information.StrikeSystemMonitor;
import c4s2.common.objects.information.TargetMonitor;
import c4s2.common.ports.information.C4S2MessagingProtocol;
import c4s2.common.ports.matter.StrikeOrdnanceTransmitProtocol;
import c4s2.common.valueTypes.OrdnanceStatesEnum;
import c4s2.common.valueTypes.OrdnanceTypeEnum;
import c4s2.common.valueTypes.StrikeSystemStatesEnum;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.valuetypes.BBoolean;
import sysmlinjava.valuetypes.DirectionRadians;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.DistanceMiles;
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.LatitudeDegrees;
import sysmlinjava.valuetypes.LongitudeDegrees;
import sysmlinjava.valuetypes.PointGeospatial;
import sysmlinjava.valuetypes.SpeedMetersPerSecond;
import sysmlinjava.valuetypes.VelocityMetersPerSecondRadians;

/**
 * The {@code StrikeSystem} is a SysMLinJava model of a system that performs
 * target strike operations, e.g. an armed drone, fighter, or similar system.
 * The {@code StrikeSystem} is an extension of the standard {@code SysMLBlock}.
 * It has a port for sending/receiving messages to/from the C4S2System and
 * another port for "transmitting" (firing) ordnance to the strike target.
 * <p>
 * The {@code StrikeSystem} is based near the {@code RadarSystem} and, as
 * controlled by the {@code C4S2Operator} via the {@code C4S2System}, its life
 * cycle consists of standing by for strike operation, conducting the strike
 * operation, returning to base, and then detaching from the operation. In
 * addition to receiving {@code StrikeSystemControl}s, the {@code StrikeSystem}
 * receives periodic {@code TargetMonitor} messages from the {@code C4S2System}
 * that it uses to locate the target during its strike operation. The
 * {@code StrikeSystem} travels at a relatively high speed and, when within a
 * specified range of the target, fires at the target. After firing, the system
 * awaits command to return to base and waits there until it receives the
 * command that it is detached from the operation.
 * <p>
 * Note that this strike system is modeled with minimal capability here.
 * Obvously, one could add far greater and more precisely specified features and
 * capabilities to this system for more advanced MBSE.
 * 
 * @author ModelerOne
 *
 */
public class StrikeSystem extends SysMLBlock
{
	/**
	 * Port representing the messaging protocol for messaging with other systems
	 * such as the C4S2System
	 */
	@FullPort
	public C4S2MessagingProtocol messaging;
	/**
	 * Port representing the transmitter (firing) of ordnance at the strike target
	 */
	@FullPort
	public StrikeOrdnanceTransmitProtocol ordnanceTransmitter;

	/**
	 * Value for the latitude of center of the radar scan in which the target will
	 * be found, fixed, and tracked
	 */
	@Value
	public PointGeospatial scanCenterPosition;
	/**
	 * Value for the distance from the system base to center of the radar scan in
	 * which the target will be found, fixed, and tracked
	 */
	@Value
	public DistanceMiles baseToScanCenterMiles;
	/**
	 * Value for the geospatial position of base
	 */
	@Value
	public PointGeospatial basePosition;
	/**
	 * Value for the standby position of the strike system
	 */
	@Value
	public PointGeospatial standbyPosition;
	/**
	 * Value for the current position of the strike system
	 */
	@Value
	public PointGeospatial currentPosition;
	/**
	 * Value for the current velocity (speed and heading) of the strike system
	 */
	@Value
	public VelocityMetersPerSecondRadians currentVelocity;
	/**
	 * Value for the ordnance that is loaded on the strike system
	 */
	@Value
	public StrikeOrdnance strikeOrdnance;
	/**
	 * Value for the current position of the strike target
	 */
	@Value
	public PointGeospatial targetPosition;
	/**
	 * Value for the distance to target for the target to be "in range"
	 */
	@Value
	public DistanceMeters targetInRangeDistance;
	/**
	 * Value for the current strike system monitoring information
	 */
	@Value
	public StrikeSystemMonitor currentMonitor;
	/**
	 * Value for the current strike system controling information
	 */
	@Value
	private StrikeSystemControl currentControl;
	/**
	 * Value for the time of strike system's last position update
	 */
	@Value
	public InstantMilliseconds lastPositionUpdateTime;

	public StrikeSystem()
	{
		super();
	}

	/**
	 * Reacts to the receipt of a strike system control message that commands
	 * transition from initializing to standby mode.
	 * 
	 * @param control information to control the strike system
	 */
	@Reception
	public void onInitializingToStandingByControl(StrikeSystemControl control)
	{
		currentControl = control;
	}

	/**
	 * Event handler for receipt of a strike system control message that commands
	 * transition from standby to striking mode.
	 * 
	 * @param control information to control the strike system
	 */
	@Reception
	public void onStandingByToStrikingControl(StrikeSystemControl control)
	{
		currentControl = control;
	}

	/**
	 * Reacts to receipt of a new TargetMonitor by calculating current position of
	 * strike system and comparing it to the target's position. If within range,
	 * shoots the target, and, if not within range, changes velocity direction of
	 * the strike system to get closer to target.
	 * 
	 * @param targetMonitor target's latest waypoint positions
	 */
	@Reception
	public void onTargetMonitorDuringStrike(TargetMonitor targetMonitor)
	{
		logger.info(targetMonitor.toString());
		InstantMilliseconds now = InstantMilliseconds.now();
		DurationSeconds nextPositionTime = DurationSeconds.ofMillis(now.millis() - lastPositionUpdateTime.millis());
		DistanceMeters distance = new DistanceMeters(currentVelocity.value * nextPositionTime.value);
		DirectionRadians direction = new DirectionRadians(currentVelocity.heading.value);
		PointGeospatial nextPosition = currentPosition.movedTo(direction, distance);
		currentPosition.setValue(nextPosition);

		int lastWaypointIndex = targetMonitor.pastWaypoints.size() - 1;
		targetPosition = targetMonitor.pastWaypoints.get(lastWaypointIndex).location;
		if (currentPosition.isWithinCircle(targetPosition, targetInRangeDistance))
		{
			shoot(targetMonitor);
			currentMonitor.confirmTargetEngaged = Optional.of(BBoolean.True);
			acceptEvent(new TargetEngagedEvent());
		}

		currentVelocity.setValue(currentVelocity, currentPosition.headingTo(targetPosition));
		lastPositionUpdateTime = now;
		currentMonitor.position = currentPosition;
		currentMonitor.velocity = currentVelocity;
		currentMonitor.time = now;

		messaging.transmit(new StrikeSystemMonitorMessage(new StrikeSystemMonitor(currentMonitor)));
	}

	/**
	 * Reacts to the receipt of a strike system control message while returning to
	 * base.
	 * 
	 * @param control information to control the strike system
	 */
	@Reception
	public void onStrikingToReturningControl(StrikeSystemControl control)
	{
		logger.info(control.toString());
		currentControl = control;
	}
	public void onReturningMonitorTime()
	{
		logger.info("returning monitor time...");
		InstantMilliseconds now = InstantMilliseconds.now();
		DurationSeconds nextPositionTime = DurationSeconds.ofMillis(now.millis() - lastPositionUpdateTime.millis());
		DistanceMeters distance = new DistanceMeters(currentVelocity.value * nextPositionTime.value);
		DirectionRadians direction = new DirectionRadians(currentVelocity.heading.value);
		PointGeospatial nextPosition = currentPosition.movedTo(direction, distance);

		lastPositionUpdateTime = now;
		currentPosition.setValue(nextPosition);
		currentVelocity.setValue(currentVelocity, currentPosition.headingTo(standbyPosition));
	
		currentMonitor.position = currentPosition;
		currentMonitor.velocity = currentVelocity;
		currentMonitor.time = now;
	
		messaging.transmit(new StrikeSystemMonitorMessage(new StrikeSystemMonitor(currentMonitor)));
	}

	public void onReturnedToBase()
	{
	}

	/**
	 * Initializes the strike system by updating its location time and the state of
	 * its ordnance.
	 */
	@Operation
	public void initialize()
	{
		logger.info("initializing...");
		strikeOrdnance.state = OrdnanceStatesEnum.unarmed;
	}

	/**
	 * Puts the strike system into standby mode by updating its location, and arming
	 * its ordnance. Also updates and sends the system's current monitor.
	 */
	@Operation
	public void standby()
	{
		logger.info("standing by...");
		InstantMilliseconds now = InstantMilliseconds.now();
		currentPosition.setValue(standbyPosition);
		currentVelocity.setValue(0, DirectionRadians.north.value);
		lastPositionUpdateTime = now;

		strikeOrdnance.state = OrdnanceStatesEnum.armed;
		strikeOrdnance.location = currentPosition;
		strikeOrdnance.time = now;
		currentMonitor.loadedOrdnance = Optional.of(strikeOrdnance);

		currentMonitor.state = StrikeSystemStatesEnum.standingby;
		currentMonitor.position = currentPosition;
		currentMonitor.velocity = currentVelocity;
		currentMonitor.time = now;
		currentMonitor.confirmTargetEngaged = Optional.of(BBoolean.False);

		messaging.transmit(new StrikeSystemMonitorMessage(new StrikeSystemMonitor(currentMonitor)));
	}

	/**
	 * Puts the strike system into strike mode, i.e. depart from base moving to
	 * within target range
	 */
	@Operation
	public void departFromBase()
	{
		logger.info("departing from base...");
		InstantMilliseconds now = InstantMilliseconds.now();
		currentPosition.setValue(standbyPosition);
		currentVelocity.setValue(44.704, DirectionRadians.north.value);
		lastPositionUpdateTime = now;

		currentMonitor.state = StrikeSystemStatesEnum.striking;
		currentMonitor.position = currentPosition;
		currentMonitor.velocity = currentVelocity;
		currentMonitor.time = now;

		messaging.transmit(new StrikeSystemMonitorMessage(new StrikeSystemMonitor(currentMonitor)));
	}

	/**
	 * Shoots the ordnance at the target, i.e. spends the ordnance
	 * 
	 * @param targetMonitor target
	 */
	@Operation
	public void shoot(TargetMonitor targetMonitor)
	{
		logger.info(targetMonitor.toString());
		int lastWaypointIndex = targetMonitor.pastWaypoints.size() - 1;
		strikeOrdnance.location = targetMonitor.pastWaypoints.get(lastWaypointIndex).location;
		strikeOrdnance.state = OrdnanceStatesEnum.spent;
		strikeOrdnance.time = InstantMilliseconds.now();
		ordnanceTransmitter.transmit(strikeOrdnance);
		currentMonitor.loadedOrdnance = Optional.of(strikeOrdnance);
	}

	/**
	 * Puts the strike system into returning mode, i.e. returning to base
	 */
	@Operation
	public void returnToBase()
	{
		logger.info("returning to base...");
		InstantMilliseconds now = InstantMilliseconds.now();
		DurationSeconds nextPositionTime = DurationSeconds.ofMillis(now.millis() - lastPositionUpdateTime.millis());
		DistanceMeters distance = new DistanceMeters(currentVelocity.value * nextPositionTime.value);
		DirectionRadians direction = new DirectionRadians(currentVelocity.heading.value);
		PointGeospatial nextPosition = currentPosition.movedTo(direction, distance);
		
		lastPositionUpdateTime = now;
		currentPosition.setValue(nextPosition);
		currentVelocity.setValue(currentVelocity, currentPosition.headingTo(standbyPosition));

		currentMonitor.state = StrikeSystemStatesEnum.returning;
		currentMonitor.position = currentPosition;
		currentMonitor.velocity = currentVelocity;
		currentMonitor.time = now;

		messaging.transmit(new StrikeSystemMonitorMessage(new StrikeSystemMonitor(currentMonitor)));
	}

	/**
	 * Puts the strike system into returned mode, i.e. returned to base
	 */
	@Operation
	public void arriveAtBase()
	{
		logger.info("arrived at base...");
		InstantMilliseconds now = InstantMilliseconds.now();
		currentPosition.setValue(standbyPosition);
		currentVelocity.setValue(0);		
		lastPositionUpdateTime = now;
		
		currentMonitor.state = StrikeSystemStatesEnum.returned;
		currentMonitor.position = currentPosition;
		currentMonitor.velocity = currentVelocity;
		currentMonitor.time = now;
		
		messaging.transmit(new StrikeSystemMonitorMessage(new StrikeSystemMonitor(currentMonitor)));
	}

	/**
	 * Puts the strike system into detached mode, i.e. no longer available for
	 * strike operations
	 */
	@Operation
	public void detach()
	{
		logger.info("detached...");
		currentMonitor.state = StrikeSystemStatesEnum.detached;
		currentMonitor.time = InstantMilliseconds.now();

		messaging.transmit(new StrikeSystemMonitorMessage(new StrikeSystemMonitor(currentMonitor)));
	}

	public boolean isReturnedToBase()
	{
		boolean isReturned = currentPosition.isWithinCircle(standbyPosition, new DistanceMeters(100));
		return isReturned;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new StrikeSystemStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		scanCenterPosition = new PointGeospatial(new LatitudeDegrees(45.0), new LongitudeDegrees(105.0));
		standbyPosition = new PointGeospatial(scanCenterPosition.movedTo(DirectionRadians.south, new DistanceMeters(1100)));
		currentPosition = new PointGeospatial(standbyPosition);
		currentVelocity = new VelocityMetersPerSecondRadians(new SpeedMetersPerSecond(0), DirectionRadians.north);
		
		strikeOrdnance = new StrikeOrdnance(OrdnanceTypeEnum.large, OrdnanceStatesEnum.unarmed, currentPosition, InstantMilliseconds.now());
		targetInRangeDistance = new DistanceMeters(100);
		currentMonitor = new StrikeSystemMonitor(StrikeSystemStatesEnum.initializing, InstantMilliseconds.now(), currentPosition, currentVelocity, Optional.of(strikeOrdnance), Optional.of(BBoolean.False));
		currentControl = new StrikeSystemControl(StrikeSystemStatesEnum.initializing, Optional.of(currentPosition), Optional.of(strikeOrdnance));
		lastPositionUpdateTime = InstantMilliseconds.now();
	}

	@Override
	protected void createFullPorts()
	{
		super.createFullPorts();
		messaging = new C4S2MessagingProtocol(this, 0L, "StrikeSystemMessaging");
		ordnanceTransmitter = new StrikeOrdnanceTransmitProtocol(this, 0L);
	}
}
