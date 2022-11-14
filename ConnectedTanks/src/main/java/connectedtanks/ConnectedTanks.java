package connectedtanks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import sysmlinjava.analysis.common.Axis;
import sysmlinjava.analysis.common.AxisFixedRange;
import sysmlinjava.analysis.linecharts.LineChartData;
import sysmlinjava.analysis.linecharts.LineChartDefinition;
import sysmlinjava.analysis.linecharts.LineChartsDisplay;
import sysmlinjava.analysis.linecharts.LineChartsTransmitter;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.parametrics.BindingConnector;
import sysmlinjava.annotations.parametrics.BindingConnectorFunction;
import sysmlinjava.annotations.parametrics.ConstraintBlock;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLBindingConnectorFunction;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.Point2D;

/**
 * {@code ConnectedTanks} is a SysMLinJava model of a simple system of two tanks
 * of fluid connected by a pipe. The model is a SysMLinJava implementation of
 * the model by the same name described in "SysML Extension for Physical
 * Interaction and Signal Flow Simulation", Object Management Group, Inc., 2018.
 * The concept of the connected tanks system is the use of the SysML proxy port
 * to model the interface between tank and pipe, and the use of the SysML
 * constraint block to perform parametric specification and analysis of the
 * system model.
 * <p>
 * {@code ConnectedTanks} consists of three parts, the two tanks,
 * {@code fluidReservoir1} and {@code fluidReservoir2}, and the {@code pipe}
 * that connects them. The two tanks have different levels of fluid which
 * equalize via the fluid flow through the pipe. It also consists of three
 * constraint blocks, one for each tank and the pipe. The properties of the
 * tanks and pipe (fluid flows, pressures, etc.) are obtained from the
 * constraints in the constraint blocks associated with each. These constraints
 * are specified in the {@code tank1ConstraintBlock},
 * {@code tank2ConstraintBlock}, and {@code pipeConstraintBlock}. In essence,
 * the SysMLinJava model of the connected tanks is an executable model of a
 * system defined by its constraints where the model is verified/validated by
 * its conformance to the system's constraints.
 * <p>
 * The tanks and pipe each have a proxy port that represents its interface with
 * the pipe or tanks, respectively. These proxy ports include values for the
 * fluid flows and pressures at the interface at any instant in the model
 * execution. As the fluid flows between the tanks via the pipe, the pressures
 * at the interface change, thereby changing the fluid flow, which results in a
 * slower rate of change for (time-based derivative of) the tank fluid levels.
 * It is these tank fluid levels over time that are displayed on a line chart
 * generated by the {@code ConnectedTanks} block during model execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 */
public class ConnectedTanks extends SysMLBlock
{
	/**
	 * Tank which acts as a first fluid reservoir
	 */
	@Part
	Tank fluidReservoir1;
	/**
	 * Tank which acts as a second fluid reservoir
	 */
	@Part
	Tank fluidReservoir2;
	/**
	 * Pipe that connects the two tanks allowing fluid to move (equalize) between
	 * them
	 */
	@Part
	Pipe pipe;

	/**
	 * Constraint block that constrains the first tank to specified parameters
	 */
	@ConstraintBlock
	TankConstraint tank1ConstraintBlock;
	/**
	 * Constraint block that constrains the second tank to specified parameters
	 */
	@ConstraintBlock
	TankConstraint tank2ConstraintBlock;
	/**
	 * Constraint block that constrains the pipe to specified parameters
	 */
	@ConstraintBlock
	PipeConstraint pipeConstraintBlock;

	/**
	 * Function that makes the connection between the tanks and pipe
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction tanksToPipeConnectorFunction;

	/**
	 * Connector that performs the functions that make the connections between the
	 * tanks and pipe
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector tanksToPipeConnector;

	/**
	 * Function that binds the constraint parameters in the constraint blocks to the
	 * parameteric values of the tanks and pipe
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction constraintParametersConnectorFunction;

	/**
	 * Connector that performs the functions that bind the constraint parameters in
	 * the constraint blocks to the parameteric values of the tanks and pipe
	 */
	@BindingConnector
	public SysMLBindingConnector constraintParametersConnector;

	/**
	 * Transmitter of constraint parameter values to a line chart display
	 */
	public LineChartsTransmitter lineChartsTransmitter;

	/**
	 * Constructor
	 */
	public ConnectedTanks()
	{
		super();
		lineChartsTransmitter = new LineChartsTransmitter(LineChartsDisplay.udpPort, true);
		transmitLineChartDefinition();
	}

	/**
	 * Increments the model execution to the next increment of time
	 * 
	 * @param i index of the next increment of time
	 */
	public void increment(int i)
	{
		pipe.increment();
		transmitLineChartData(DurationSeconds.of(i));
	}

	@Override
	protected void createParts()
	{
		fluidReservoir1 = new Tank("Tank1", 1L);
		fluidReservoir2 = new Tank("Tank2", 2L);
		pipe = new Pipe("Pipe", 0L);
	}

	@Override
	protected void createConstraintBlocks()
	{
		tank1ConstraintBlock = new TankConstraint();
		tank1ConstraintBlock.setName("tank1ConstraintBlock");
		tank2ConstraintBlock = new TankConstraint();
		tank2ConstraintBlock.setName("tank2ConstraintBlock");
		pipeConstraintBlock = new PipeConstraint();
	}

	@Override
	protected void createConnectorFunctions()
	{
		tanksToPipeConnectorFunction = () ->
		{
			pipe.pipeOpening1.addConnectedPortPeer(fluidReservoir1.tankOpening);
			pipe.pipeOpening2.addConnectedPortPeer(fluidReservoir2.tankOpening);
		};
	}

	@Override
	protected void createConnectors()
	{
		tanksToPipeConnector = new SysMLAssociationBlockConnector(List.of(fluidReservoir1, fluidReservoir2), List.of(pipe), tanksToPipeConnectorFunction);
	}

	@Override
	protected void createConstraintParameterConnectorFunctions()
	{
		constraintParametersConnectorFunction = () ->
		{
			{
				SysMLConstraintParameterPort surfaceAreaPort = tank1ConstraintBlock.surfaceAreaPort;
				surfaceAreaPort.parameterContextBlock = fluidReservoir1;
				fluidReservoir1.area.bindTo(surfaceAreaPort);

				SysMLConstraintParameterPort fluidDensityPort = tank1ConstraintBlock.fluidDensityPort;
				fluidDensityPort.parameterContextBlock = fluidReservoir1;
				fluidReservoir1.fluidDensity.bindTo(fluidDensityPort);

				SysMLConstraintParameterPort fluidLevelPort = tank1ConstraintBlock.fluidLevelPort;
				fluidLevelPort.parameterContextBlock = fluidReservoir1;
				fluidReservoir1.fluidLevel.bindTo(fluidLevelPort);

				SysMLConstraintParameterPort fluidFlowPort = tank1ConstraintBlock.fluidFlowPort;
				fluidFlowPort.parameterContextBlock = fluidReservoir1;
				fluidReservoir1.fluidFlow.bindTo(fluidFlowPort);

				SysMLConstraintParameterPort pressurePort = tank1ConstraintBlock.pressurePort;
				pressurePort.parameterContextBlock = fluidReservoir1;
				fluidReservoir1.pressure.bindTo(pressurePort);
			}
			{
				SysMLConstraintParameterPort surfaceAreaPort = tank2ConstraintBlock.surfaceAreaPort;
				surfaceAreaPort.parameterContextBlock = fluidReservoir2;
				fluidReservoir2.area.bindTo(surfaceAreaPort);

				SysMLConstraintParameterPort fluidDensityPort = tank2ConstraintBlock.fluidDensityPort;
				fluidDensityPort.parameterContextBlock = fluidReservoir2;
				fluidReservoir2.fluidDensity.bindTo(fluidDensityPort);

				SysMLConstraintParameterPort fluidLevelPort = tank2ConstraintBlock.fluidLevelPort;
				fluidLevelPort.parameterContextBlock = fluidReservoir2;
				fluidReservoir2.fluidLevel.bindTo(fluidLevelPort);

				SysMLConstraintParameterPort fluidFlowPort = tank2ConstraintBlock.fluidFlowPort;
				fluidFlowPort.parameterContextBlock = fluidReservoir2;
				fluidReservoir2.fluidFlow.bindTo(fluidFlowPort);

				SysMLConstraintParameterPort pressurePort = tank2ConstraintBlock.pressurePort;
				pressurePort.parameterContextBlock = fluidReservoir2;
				fluidReservoir2.pressure.bindTo(pressurePort);
			}
			{
				SysMLConstraintParameterPort pipeRadiusPort = pipeConstraintBlock.radiusPort;
				pipeRadiusPort.parameterContextBlock = pipe;
				pipe.pipeRadius.bindTo(pipeRadiusPort);

				SysMLConstraintParameterPort pipeLengthPort = pipeConstraintBlock.lengthPort;
				pipeLengthPort.parameterContextBlock = pipe;
				pipe.pipeLength.bindTo(pipeLengthPort);

				SysMLConstraintParameterPort dynamicViscosityPort = pipeConstraintBlock.viscosityPort;
				dynamicViscosityPort.parameterContextBlock = pipe;
				pipe.dynamicViscosity.bindTo(dynamicViscosityPort);

				SysMLConstraintParameterPort fluidFlowPort = pipeConstraintBlock.fluidFlowPort;
				fluidFlowPort.parameterContextBlock = pipe;
				pipe.fluidFlow.bindTo(fluidFlowPort);

				SysMLConstraintParameterPort pressureDiffPort = pipeConstraintBlock.pressureDiffPort;
				pressureDiffPort.parameterContextBlock = pipe;
				pipe.fluidPressureDiff.bindTo(pressureDiffPort);

				SysMLConstraintParameterPort resistancePort = pipeConstraintBlock.resistancePort;
				resistancePort.parameterContextBlock = pipe;
				pipe.resistance.bindTo(resistancePort);

				SysMLConstraintParameterPort opening1PressurePort = pipeConstraintBlock.opening1PressurePort;
				opening1PressurePort.parameterContextBlock = pipe;
				pipe.pipeOpening1.vf.p.bindTo(opening1PressurePort);

				SysMLConstraintParameterPort opening1FluidFlowPort = pipeConstraintBlock.opening1FluidFlowPort;
				opening1FluidFlowPort.parameterContextBlock = pipe;
				pipe.pipeOpening1.vf.q.bindTo(opening1FluidFlowPort);

				SysMLConstraintParameterPort opening2PressurePort = pipeConstraintBlock.opening2PressurePort;
				opening2PressurePort.parameterContextBlock = pipe;
				pipe.pipeOpening2.vf.p.bindTo(opening2PressurePort);

				SysMLConstraintParameterPort opening2FluidFlowPort = pipeConstraintBlock.opening2FluidFlowPort;
				opening2FluidFlowPort.parameterContextBlock = pipe;
				pipe.pipeOpening2.vf.q.bindTo(opening2FluidFlowPort);
			}
		};
	}

	@Override
	protected void createConstraintParameterConnectors()
	{
		constraintParametersConnector = new SysMLBindingConnector(List.of(fluidReservoir1, fluidReservoir2, pipe), List.of(tank1ConstraintBlock, tank2ConstraintBlock, pipeConstraintBlock), constraintParametersConnectorFunction);
	}

	/**
	 * Title of the line chart used to display the changing tank fluid levels
	 */
	static final String lineChartTitle = "Tank Fluid Levels";

	/**
	 * Transmits the definition of the line chart of the constraint parameters (tank
	 * levels) to the graphical line chart display
	 * 
	 * @see sysmlinjava.analysis.common.Axis
	 * @see sysmlinjava.analysis.linecharts.LineChartDefinition
	 */
	protected void transmitLineChartDefinition()
	{
		Axis xAxis = new Axis("Duration", "seconds", Optional.of(0.0), Optional.of(90.0), 10.0, 5);
		AxisFixedRange yAxisTank1FluidLevel = new AxisFixedRange("Tank1 Fluid Level", "meters", 0.0, 45.0, 5.0, 5);
		AxisFixedRange yAxisTank2FluidLevel = new AxisFixedRange("Tank2 Fluid Level", "meters", 0.0, 45.0, 5.0, 5);
		ArrayList<AxisFixedRange> yAxes = new ArrayList<>(Arrays.asList(yAxisTank1FluidLevel, yAxisTank2FluidLevel));
		LineChartDefinition graph = new LineChartDefinition(lineChartTitle, yAxes, xAxis);
		lineChartsTransmitter.transmitGraph(graph);
	}

	/**
	 * Transmits the values of the constraint parameters (tank levels) to the
	 * graphical line chart display
	 * 
	 * @param duration time since start of connected tanks simulation
	 * 
	 * @see sysmlinjava.valuetypes.Point2D
	 * @see sysmlinjava.analysis.linecharts.LineChartData
	 */
	protected void transmitLineChartData(DurationSeconds duration)
	{
		List<Point2D> xyTank1FluidLevel = new ArrayList<>(Arrays.asList(new Point2D(duration.value, fluidReservoir1.fluidLevel.value)));
		List<Point2D> xyTank2FluidLevel = new ArrayList<>(Arrays.asList(new Point2D(duration.value, fluidReservoir2.fluidLevel.value)));
		List<List<Point2D>> xyListList = new ArrayList<>(Arrays.asList(xyTank1FluidLevel, xyTank2FluidLevel));
		LineChartData graphData = new LineChartData(lineChartTitle, xyListList);
		lineChartsTransmitter.transmitGraphData(graphData);
	}

	/**
	 * Main operation that executes the model. Execution starts with construction of
	 * the connected tanks domain, followed by the repeated incremental update of
	 * the tanks' and pipe's properties until the tank levels are essentially
	 * equalized.
	 * 
	 * @param args arguments (not used)
	 */
	public static void main(String[] args)
	{
		ConnectedTanks domain = new ConnectedTanks();
		int i = 0;
		while (domain.fluidReservoir1.fluidLevel.subtracted(domain.fluidReservoir2.fluidLevel).absoluted().greaterThan(0.1))
			domain.increment(i++);
		System.exit(0);
	}
}
