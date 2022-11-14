package connectedtanks;

import java.util.Optional;
import static java.lang.Math.PI;
import static java.lang.Math.pow;
import sysmlinjava.annotations.ProxyPort;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.DurationSeconds;

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
 * @see connectedtanks.PipeConstraint
 * 
 * @author ModelerOne
 *
 */
public class Pipe extends SysMLBlock
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
	@Value
	public VolumeFlowRate fluidFlow;
	/**
	 * Pressure differential across the pipe
	 */
	@Value
	public Pressure fluidPressureDiff;
	/**
	 * Length of the pipe
	 */
	@Value
	public DistanceMeters pipeLength;
	/**
	 * Radius of the pipe
	 */
	@Value
	public DistanceMeters pipeRadius;
	/**
	 * Viscosity (dynamic) of the pipe fluid
	 */
	@Value
	public Viscosity dynamicViscosity;
	/**
	 * Viscous resistance of the pipe
	 */
	@Value
	public ViscousResistance resistance;

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
		VolumeFlowRate fluidFlow1 = new VolumeFlowRate(fluidPressureDiff.value / resistance.value);
		VolumeFlowRate fluidFlow2 = new VolumeFlowRate(-fluidFlow1.value);
		fluidFlow.setValue(fluidFlow1);

		pipeOpening1.setVolumeFlow(new FlowingVolume(fluidFlow1, pipeOpening1.vf.p), incrementTime);
		pipeOpening2.setVolumeFlow(new FlowingVolume(fluidFlow2, pipeOpening2.vf.p), incrementTime);
	}

	@Override
	protected void createValues()
	{
		fluidFlow = new VolumeFlowRate(0.0);
		fluidPressureDiff = new Pressure(0.0);
		pipeLength = new DistanceMeters(10.0);
		pipeRadius = new DistanceMeters(0.5);
		dynamicViscosity = new Viscosity(2.0);
		resistance = new ViscousResistance((8.0 * dynamicViscosity.value * pipeLength.value) / (PI * pow(pipeRadius.value, 4.0)));
	}

	@Override
	protected void createFullPorts()
	{
		pipeOpening1 = new VolumeFlowElement(this, Optional.empty(), 1L);
		pipeOpening2 = new VolumeFlowElement(this, Optional.empty(), 2L);
	}
}
