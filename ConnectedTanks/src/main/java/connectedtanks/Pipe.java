package connectedtanks;

import java.util.Optional;
import static java.lang.Math.PI;
import static java.lang.Math.pow;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.PressureNewtonsPerMeterSquare;
import sysmlinjava.attributetypes.ViscosityPascalSecond;
import sysmlinjava.attributetypes.ViscousResistanceNewtons;
import sysmlinjava.attributetypes.VolumeFlowMetersCubicPerSecond;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.ProxyPort;
import sysmlinjava.parts.SysMLPart;

/**
 * {@code Pipe} is the SysMLinJava model of a fluid pipe that connects two tanks
 * of fluid as part of the {@code ConnectedTanks} system. The {@code Pipe} is
 * characterized by its length, radius, viscosity, fluid flow, and pressure
 * differential. It has two openings to the two tanks through which the fluid
 * flows into or out of the tank depending on the pressure at the opening.
 * <p>
 * The {@code Pipe} model is as specified by its constraints which are declared
 * in the {@code PipeConstraint} block. The {@code PipeConstraint} block is used
 * to validate/verify the pipe model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see connectedtanks.PipeAnalysis
 * 
 * @author ModelerOne
 *
 */
public class Pipe extends SysMLPart
{
	/**
	 * Port representing the pipe's opening to tank 1
	 */
	@ProxyPort
	public VolumeFlowElement pipeOpening1;
	/**
	 * Port representing the pipe's opening to tank 2
	 */
	@ProxyPort
	public VolumeFlowElement pipeOpening2;

	/**
	 * Rate of fluid flow through the pipe
	 */
	@Attribute
	public VolumeFlowMetersCubicPerSecond fluidFlow;
	/**
	 * Pressure differential across the pipe
	 */
	@Attribute
	public PressureNewtonsPerMeterSquare fluidPressureDiff;
	/**
	 * Length of the pipe
	 */
	@Attribute
	public DistanceMeters pipeLength;
	/**
	 * Radius of the pipe
	 */
	@Attribute
	public DistanceMeters pipeRadius;
	/**
	 * Viscosity (dynamic) of the pipe fluid
	 */
	@Attribute
	public ViscosityPascalSecond dynamicViscosity;
	/**
	 * Viscous resistance of the pipe
	 */
	@Attribute
	public ViscousResistanceNewtons resistance;

	/**
	 * Constructor
	 * 
	 * @param name unique name for the pipe
	 * @param id   unique ID for the pipe
	 */
	public Pipe(String name, Long id)
	{
		super(name, id);
	}

	/**
	 * Time duration between model execution/simulation steps
	 */
	private static final DurationSeconds incrementTime = DurationSeconds.of(1.0);

	/**
	 * Increments the model exection, i.e. re-calculates state of pipe for next time
	 * increment
	 */
	public void increment()
	{
		pipeOpening1.getVolumeFlow();
		pipeOpening2.getVolumeFlow();

		fluidPressureDiff.setValue(pipeOpening2.vf.p.value - pipeOpening1.vf.p.value);
		VolumeFlowMetersCubicPerSecond fluidFlow1 = new VolumeFlowMetersCubicPerSecond(fluidPressureDiff.value / resistance.value);
		VolumeFlowMetersCubicPerSecond fluidFlow2 = new VolumeFlowMetersCubicPerSecond(-fluidFlow1.value);
		fluidFlow.setValue(fluidFlow1);

		pipeOpening1.setVolumeFlow(new FlowingVolume(fluidFlow1, pipeOpening1.vf.p), incrementTime);
		pipeOpening2.setVolumeFlow(new FlowingVolume(fluidFlow2, pipeOpening2.vf.p), incrementTime);
	}

	@Override
	protected void createAttributes()
	{
		fluidFlow = new VolumeFlowMetersCubicPerSecond(0.0);
		fluidPressureDiff = new PressureNewtonsPerMeterSquare(0.0);
		pipeLength = new DistanceMeters(10.0);
		pipeRadius = new DistanceMeters(0.5);
		dynamicViscosity = new ViscosityPascalSecond(2.0);
		resistance = new ViscousResistanceNewtons((8.0 * dynamicViscosity.value * pipeLength.value) / (PI * pow(pipeRadius.value, 4.0)));
	}

	@Override
	protected void createPorts()
	{
		pipeOpening1 = new VolumeFlowElement(this, Optional.empty(), 1L);
		pipeOpening2 = new VolumeFlowElement(this, Optional.empty(), 2L);
	}
}
