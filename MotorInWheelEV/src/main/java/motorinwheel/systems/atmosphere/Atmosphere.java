package motorinwheel.systems.atmosphere;

import java.util.Optional;

import motorinwheel.common.ports.matter.AirResistanceTransmitPort;
import motorinwheel.common.ports.matter.FrontalArealSpeedReceivePort;
import motorinwheel.domain.MotorInWheelEVDomain;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.attributetypes.AreaMetersSquare;
import sysmlinjava.attributetypes.DensityKilogramsPerMeterCubic;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.attributetypes.FrontalArealSpeed;
import sysmlinjava.attributetypes.SpeedKilometersPerHour;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * The atmosphere through which the vehicle moves. Function is to impose forces
 * on the vehicle to simulate the air resistance encountered. Primarily consists
 * of a port to receive the moving frontal surface of the vehicle and a port to
 * transmit forces of air resistance.
 * 
 * @author ModelerOne
 */
public class Atmosphere extends SysMLPart
{
	/**
	 * Port to receive the frontal areal speed of the moving vehicle
	 */
	@Port
	public FrontalArealSpeedReceivePort frontalArea;
	/**
	 * Port to transmit air resisance force to the moving vehicle
	 */
	@Port
	public AirResistanceTransmitPort air;

	/**
	 * Flow of the frontal area of the vehicle into the atmosphere
	 */
	@Attribute
	public FrontalArealSpeed frontalArealSpeedIn;
	/**
	 * Flow of the force of air resistance out of the atmosphere into the vehicle
	 */
	@Attribute
	public ForceNewtons airResistanceOut;

	/**
	 * Value of the atmosphere's air density
	 */
	@Attribute
	public DensityKilogramsPerMeterCubic airDensity;
	/**
	 * Value of the last flow of the force of air resistance
	 */
	@Attribute
	public ForceNewtons lastAirResistanceOut;

	/**
	 * Constructor
	 * 
	 * @param domain the domain of which the atmosphere is a part
	 * @param name   unique name
	 * @param id     unique ID
	 */
	public Atmosphere(MotorInWheelEVDomain domain, String name, long id)
	{
		super(Optional.of(domain), name, id);
	}

	/**
	 * Constant value for minimum speed changes used for simulation
	 */
	public static final double minSpeedDeltaKilometersPerHour = 15.0;
	/**
	 * Constant value for minimum difference in air resistance used for simulation
	 */
	public static final double minDifferenceAirResistanceValues = 3;

	/**
	 * Reception that reacts to the receipt of a new frontal areal speed
	 * 
	 * @param frontalArealSpeed frontal areal speed of the moving vehicle
	 */
	@Action
	public void onFrontalArea(FrontalArealSpeed frontalArealSpeed)
	{
		frontalArealSpeedIn.value = frontalArealSpeed.value;
		frontalArealSpeedIn.speed.value = frontalArealSpeed.speed.value;
		calculateAirResistance();
		if (Math.abs(lastAirResistanceOut.value - airResistanceOut.value) > minDifferenceAirResistanceValues)
		{
			lastAirResistanceOut.value = airResistanceOut.value;
			air.transmit(airResistanceOut);
		}
	}

	/**
	 * Calculation of the air resistance as function of the moving vehicle's frontal
	 * area
	 */
	@Calculation
	private void calculateAirResistance()
	{
		airResistanceOut.value = Vehicle.dragCoefficient.value * (airDensity.value * Math.pow(frontalArealSpeedIn.speed.value, 2) / 2) * frontalArealSpeedIn.value;
		if (frontalArealSpeedIn.speed.value >= 0)
			airResistanceOut.direction.value = Math.toRadians(270);
		else
		{
			airResistanceOut.value = -airResistanceOut.value;
			airResistanceOut.direction.value = Math.toRadians(90);
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new AtmosphereStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		airDensity = new DensityKilogramsPerMeterCubic(1.1);
		lastAirResistanceOut = new ForceNewtons(0, Math.toRadians(270));
		frontalArealSpeedIn = new FrontalArealSpeed(new AreaMetersSquare(0), new SpeedKilometersPerHour(0));
		airResistanceOut = new ForceNewtons(0, Math.toRadians(270));
	}

	@Override
	protected void createPorts()
	{
		air = new AirResistanceTransmitPort(this, 0L);
		frontalArea = new FrontalArealSpeedReceivePort(this, this, 0L);
	}
}
