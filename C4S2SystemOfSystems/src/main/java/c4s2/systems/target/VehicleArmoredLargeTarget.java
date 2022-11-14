package c4s2.systems.target;

import java.util.Optional;
import c4s2.common.objects.information.RadarSignalTransmission;
import c4s2.common.objects.information.StrikeOrdnance;
import c4s2.common.ports.matter.StrikeOrdnanceReceiveProtocol;
import c4s2.common.valueTypes.OrdnanceTypeEnum;
import c4s2.common.valueTypes.RadarReturnSignatureEnum;
import c4s2.common.valueTypes.TargetStatusEnum;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.DirectionRadians;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.DistanceMiles;
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.LatitudeDegrees;
import sysmlinjava.valuetypes.LatitudeRadians;
import sysmlinjava.valuetypes.LongitudeDegrees;
import sysmlinjava.valuetypes.LongitudeRadians;
import sysmlinjava.valuetypes.PointGeospatial;
import sysmlinjava.valuetypes.SpeedMetersPerSecond;
import sysmlinjava.valuetypes.SpeedMilesPerHour;
import sysmlinjava.valuetypes.VelocityMetersPerSecondRadians;

/**
 * The {@code VehicleArmoredLargeTarget} is a SysMLinJava model of a vehicle
 * (moving target) that is armored and large, e.g. a tank. As an extension of
 * the basic {@code RadarTarget}, the {@code VehicleArmoredLargeTarget} has
 * ports to receive a radar scanning signal and to return (reflect) the signal
 * that indicates the type of target and its location. The
 * {@code VehicleArmoredLargeTarget} also has a port to receive strike ordnance
 * to potentially destroy the target.
 * <p>
 * The {@code VehicleArmoredLargeTarget} has two specialized receptions for
 * receiving the radar signal - one for receiving and reflecting the signal
 * while operational (moving) and another for receiving and reflecting the
 * signal when destroyed (stopped). The operational reception simply determine
 * the vehicle's position at the time of the signal reception based on a simple
 * straight line velocity, and reflects the signal with its VehicleArmoredLarge
 * signature and its geoposition embedded in the signal return. The "destroyed"
 * reception simply returns/reflects the signal with a VehicleDestroyed
 * signature and its location at the ordnance impact embedded in the return.
 * <p>
 * <b>Note:</b>This model of the vehicle target has it moving in a simple
 * straight line velocity - currently set to be due west - a heading of 270% -
 * at a mere 5 miles per hour. Its start is from a geoposition that is one mile
 * east of the center of the radar's scanning area. The model of the RadarSystem
 * should be consitent with this velocity and location to perform a realistic
 * simulation of tracking the moving vehicle target.
 * 
 * @author ModelerOne
 *
 */
public class VehicleArmoredLargeTarget extends RadarTarget
{
	/**
	 * Port representing the "receiver" of strike ordnance
	 */
	@FullPort
	public StrikeOrdnanceReceiveProtocol ordnanceReceiver;
	/**
	 * Value of the time the vehicle started from its initial position (used to
	 * compute vehicle position at any time after starting movement)
	 */
	@Value
	public InstantMilliseconds lastPositionTime;

	/**
	 * Value of the vehicle's velocity (derived from speed and heading values)
	 */
	@Value
	public VelocityMetersPerSecondRadians currentVelocity;
	/**
	 * Value of the operational status of the target vehicle
	 */
	@Value
	public TargetStatusEnum operatingStatus;
	/**
	 * Value of the latitude of the radar's scanning area center used to calculate
	 * the vehicle's start position
	 */
	@Value
	public LatitudeRadians scanCenterLatitude;
	/**
	 * Value of the location of the radar's scanning area center used to calculate
	 * the vehicle's start position
	 */
	@Value
	public PointGeospatial scanCenterLocation;
	/**
	 * Value of the due east distance from the radar's scanning area center where
	 * the vehcile starts movement from
	 */
	@Value
	public DistanceMiles vehicleStartDistanceFromScanCenter;

	/**
	 * Constructor
	 */
	public VehicleArmoredLargeTarget()
	{
		super();
	}

	/**
	 * Initializes the initial position time of when the vehicle started movement
	 */
	public void initialize()
	{
		lastPositionTime = InstantMilliseconds.now();
	}

	/**
	 * Reacts to the reception of a radar signal transmission when in operational
	 * state. Reaction is to calculate vehicle's current position and invoke
	 * {@code RadarTarget}'s reception of the transmission to determine if the vehicle's
	 * position is within the radar signal scanning area and, if so, return
	 * (reflect) the signal back to the radar with embedded signature and position
	 * information.
	 * 
	 * @param transmission radar signal transmitted to the vehicle target
	 */
	@Reception
	public void receiveRadarSignalTransmissionWhenOperational(RadarSignalTransmission transmission)
	{
		updatePosition();
		receiveRadarSignalTransmission(transmission);
	}

	/**
	 * Reacts to the reception of a radar signal transmission when destroyed.
	 * Reaction is to check if the transmission's power is greater than zero. If so,
	 * it simply invoke {@code RadarTarget}'s reception of the transmission to
	 * determine if the vehicle's position is within the radar signal scanning area
	 * and, if so, return (reflect) the signal back to the radar with embedded
	 * signature, which now "vehicleDestroyed" and position information. If the
	 * power is zero, then that indicates the end of the simulation and the vehicle
	 * invokes the transition to it "final" state.
	 * 
	 * @param transmission radar signal transmitted to the vehicle target
	 */
	@Reception
	public void receiveRadarSignalTransmissionWhenDestroyed(RadarSignalTransmission transmission)
	{
		if (transmission.scanPower.value > 0)
			receiveRadarSignalTransmission(transmission);
		else
			acceptEvent(new FinalEvent());
	}

	/**
	 * Returns whether or not the vehicle is destroyed by the specified strike
	 * ordnance. The vehicle is determined to be destoryed if a certain size of
	 * orndance was received and it was received within a certain range from the
	 * vehicle.
	 * 
	 * @param ordnance the ordnance that was "received" by the vehicle
	 * @return true if destroyed by the ordnance, false otherwise
	 */
	@Operation
	public boolean isDestroyedBy(StrikeOrdnance ordnance)
	{
		boolean largeOrdnance = ordnance.type.equals(OrdnanceTypeEnum.large);
		boolean inRange = currentPosition.isWithinCircle(ordnance.location, ordnance.type.getRange());
		return largeOrdnance && inRange;
	}

	/**
	 * Explodes the vehicle, i.e. sets its status to "exploding", as preliminary
	 * state to being "destroyed"
	 */
	public void explode()
	{
		operatingStatus.setValue(TargetStatusEnum.exploding);
		currentVelocity.setValue(0, 0);
	}

	/**
	 * Destroys the vehicle, i.e. sets its position to a final non-changing value
	 * and sets its status and radar signature to "vehicleDestroyed"
	 */
	@Operation
	public void destroy()
	{
		updatePosition();
		operatingStatus.setValue(TargetStatusEnum.destroyed);
		signature = RadarReturnSignatureEnum.vehicleDestroyed;
	}

	private void updatePosition()
	{
		InstantMilliseconds now = InstantMilliseconds.now();
		DurationSeconds nextPositionTime = DurationSeconds.ofMillis(now.value - lastPositionTime.value);
		DistanceMeters distance = new DistanceMeters(currentVelocity.value * nextPositionTime.value);
		DirectionRadians direction = new DirectionRadians(currentVelocity.heading.value);
		PointGeospatial nextPosition = currentPosition.movedTo(direction, distance);
		currentPosition.setValue(nextPosition);
		lastPositionTime = now;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new VehicleTargetStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		
		signature = RadarReturnSignatureEnum.vehicleLargeArmored;
		
		scanCenterLocation = new PointGeospatial(new LatitudeRadians(new LatitudeDegrees(45.0)), new LongitudeRadians(new LongitudeDegrees(105.0)));
		currentPosition = scanCenterLocation.movedTo(DirectionRadians.east, new DistanceMeters(800));
		currentVelocity = new VelocityMetersPerSecondRadians(new SpeedMetersPerSecond(new SpeedMilesPerHour(20)), DirectionRadians.west);
		operatingStatus = new TargetStatusEnum(TargetStatusEnum.operating);
		lastPositionTime = InstantMilliseconds.now();
	}

	@Override
	protected void createFullPorts()
	{
		super.createFullPorts();
		ordnanceReceiver = new StrikeOrdnanceReceiveProtocol(this, 0L);
	}
}