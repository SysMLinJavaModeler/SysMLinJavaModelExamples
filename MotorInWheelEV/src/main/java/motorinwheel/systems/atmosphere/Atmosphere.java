package motorinwheel.systems.atmosphere;

import java.util.Optional;
import motorinwheel.common.ports.matter.AirResistanceTransmitPort;
import motorinwheel.common.ports.matter.FrontalArealSpeedReceivePort;
import motorinwheel.domain.MotorInWheelEVDomain;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.valuetypes.FrontalArealSpeed;
import sysmlinjava.valuetypes.AreaMetersSquare;
import sysmlinjava.valuetypes.DensityKilogramsPerMeterCubic;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;

/**
 * The atmosphere through which the vehicle moves. Function is to impose forces
 * on the vehicle to simulate the air resistance encountered. Primarily consists
 * of a port to receive the moving frontal surface of the vehicle and a port to
 * transmit forces of air resistance.
 * 
 * @author ModelerOne
 *
 */
public class Atmosphere extends SysMLBlock
{
	/**
	 * Port to receive the frontal areal speed of the moving vehicle
	 */
	@FullPort
	public FrontalArealSpeedReceivePort frontalArea;
	/**
	 * Port to transmit air resisance force to the moving vehicle
	 */
	@FullPort
	public AirResistanceTransmitPort air;

	/**
	 * Flow of the frontal area of the vehicle into the atmosphere
	 */
	@Flow
	public FrontalArealSpeed frontalArealSpeedIn;
	/**
	 * Flow of the force of air resistance out of the atmosphere into the vehicle
	 */
	@Flow
	public ForceNewtons airResistanceOut;

	/**
	 * Value of the atmosphere's air density
	 */
	@Value
	public DensityKilogramsPerMeterCubic airDensity;
	/**
	 * Value of the last flow of the force of air resistance
	 */
	@Value
	public ForceNewtons lastAirResistanceOut;

	/**
	 * Constraint that caculates the air resistance as function of the moving
	 * vehicle's frontal area
	 */
	@Constraint
	public SysMLConstraint airResistanceCalculation;

	/**
	 * Constructor
	 * @param domain the domain of which the atmosphere is a part
	 * @param name unique name
	 * @param id unique ID
	 */
	public Atmosphere(MotorInWheelEVDomain domain, String name, long id)
	{
		super(domain, name, id);
	}

	public static final double minSpeedDeltaKilometersPerHour = 15.0;
	public static final double minDifferenceAirResistanceValues = 3;

	/**
	 * Reception that reacts to the receipt of a new frontal areal speed
	 * @param frontalArealSpeed frontal areal speed of the moving vehicle
	 */
	@Reception
	public void onFrontalArea(FrontalArealSpeed frontalArealSpeed)
	{
		logger.info(frontalArealSpeed.toString());
		frontalArealSpeedIn.value = frontalArealSpeed.value;
		frontalArealSpeedIn.speed.value = frontalArealSpeed.speed.value;
		airResistanceCalculation.apply();
		if (Math.abs(lastAirResistanceOut.value - airResistanceOut.value) > minDifferenceAirResistanceValues)
		{
			lastAirResistanceOut.value = airResistanceOut.value;
			air.transmit(airResistanceOut);
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new AtmosphereStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		airDensity = new DensityKilogramsPerMeterCubic(1.1);
		lastAirResistanceOut = new ForceNewtons(0, Math.toRadians(270));
	}

	@Override
	protected void createFlows()
	{
		frontalArealSpeedIn = new FrontalArealSpeed(new AreaMetersSquare(0), new SpeedKilometersPerHour(0));
		airResistanceOut = new ForceNewtons(0, Math.toRadians(270));
	}

	@Override
	protected void createFullPorts()
	{
		air = new AirResistanceTransmitPort(this, 0L);
		frontalArea = new FrontalArealSpeedReceivePort(this, this, 0L);
	}

	@Override
	protected void createConstraints()
	{
		airResistanceCalculation = () ->
		{
			airResistanceOut.value = Vehicle.dragCoefficient.value * (airDensity.value * Math.pow(frontalArealSpeedIn.speed.value, 2) / 2) * frontalArealSpeedIn.value;
			if (frontalArealSpeedIn.speed.value >= 0)
				airResistanceOut.direction.value = Math.toRadians(270);
			else
			{
				airResistanceOut.value = -airResistanceOut.value;
				airResistanceOut.direction.value = Math.toRadians(90);
			}
		};
	}
}
