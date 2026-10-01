package connectedtanks;

import sysmlinjava.attributetypes.AreaMetersSquare;
import sysmlinjava.attributetypes.DensityKilogramsPerMeterCubic;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.PressureNewtonsPerMeterSquare;
import sysmlinjava.attributetypes.VolumeFlowMetersCubicPerSecond;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.ProxyPort;
import sysmlinjava.parts.SysMLPart;
import static sysmlinjava.attributetypes.AccelerationMetersPerSecondPerSecond.gravity;
import java.util.Optional;

/**
 * {@code Tank} is the SysMLinJava model of a tank of fluid as part of the
 * {@code ConnectedTanks} system. The {@code Tank} is characterized by its
 * surface area, diameter, fluid Level, fluid flow, and pressure. It has a
 * single opening to the system {@code Pipe} through which the fluid flows, in
 * or out of the tank depending on the pressure at the opening.
 * <p>
 * The {@code Tank} model is as specified by its constraints which are declared
 * in the {@code TankConstraint} block. The {@code TankConstraint} block is used
 * to validate/verify the tank model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see connectedtanks.TankAnalysis
 * 
 * @author ModelerOne
 *
 */
public class Tank extends SysMLPart
{
	/**
	 * Proxy port by which fluid flows and pressure is transmitted into/out of the
	 * tank
	 */
	@ProxyPort
	public VolumeFlowElement tankOpening;

	/**
	 * Pressure exerted by the fluid on the tank's bottom
	 */
	@Attribute
	public PressureNewtonsPerMeterSquare pressure;
	/**
	 * Rate of fluid flow into/out of the tank
	 */
	@Attribute
	public VolumeFlowMetersCubicPerSecond fluidFlow;
	/**
	 * Height (level) of the fluid in the tank
	 */
	@Attribute
	public DistanceMeters fluidLevel;
	/**
	 * Desity of the tank fluid
	 */
	@Attribute
	public DensityKilogramsPerMeterCubic fluidDensity;
	/**
	 * Surface area of the fluid in the tank
	 */
	@Attribute
	public AreaMetersSquare area;

	/**
	 * Constructor
	 * 
	 * @param name unique name for the tank
	 * @param id   unique ID for the tank
	 */
	public Tank(String name, Long id)
	{
		super(name, id);
	}

	/**
	 * Gets the volume flow for the tank in terms of the tanks' current rate of
	 * fluid flow and pressure
	 * 
	 * @return the current volume flow of the tank
	 */
	public FlowingVolume getVolumeFlow()
	{
		return new FlowingVolume(fluidFlow, pressure);
	}

	/**
	 * Sets the volume flow of the tank at the time (duration) into the model
	 * execution. The values are calculated in accordance with the constraint model
	 * for the tank as specified in the {@code TankConstraint} block.
	 * 
	 * @param flowingVolume the volume flow for the current time duration
	 * @param duration      the time duration into the model execution for the
	 *                      volume flow
	 */
	public void setVolumeFlow(FlowingVolume flowingVolume, DurationSeconds duration)
	{
		fluidFlow.setValue(flowingVolume.q.value);
		fluidLevel.setValue(fluidLevel.value + ((fluidFlow.value * duration.value) / area.value));
		pressure.setValue(gravity.multipliedBy(fluidLevel).multipliedBy(fluidDensity));
	}

	@Override
	protected void createAttributes()
	{
		area = new AreaMetersSquare(4.0);
		fluidLevel = new DistanceMeters(id == 1 ? 40.0 : 15.0);
		fluidDensity = new DensityKilogramsPerMeterCubic(10.0);
		pressure = new PressureNewtonsPerMeterSquare(gravity.value * fluidLevel.value * fluidDensity.value);
		fluidFlow = new VolumeFlowMetersCubicPerSecond(0);
	}

	@Override
	protected void createPorts()
	{
		super.createPorts();
		tankOpening = new VolumeFlowElement(this, Optional.of(this), 0L);
	}
}
