package electriccircuit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import sysmlinjava.analysis.bom.annotations.BOMLineItem;
import sysmlinjava.analysis.common.Axis;
import sysmlinjava.analysis.common.AxisFixedRange;
import sysmlinjava.analysis.linecharts.LineChartData;
import sysmlinjava.analysis.linecharts.LineChartDefinition;
import sysmlinjava.analysis.linecharts.LineChartsDisplay;
import sysmlinjava.analysis.linecharts.LineChartsTransmitter;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Value;
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
 * SysMLinJava model of a simple electrical circuit of an AC voltage source in
 * parallel with an RC and a RL circuit. The model is a SysMLinJava
 * implementation of the model by the same name described in "SysML Extension
 * for Physical Interaction and Signal Flow Simulation", Object Management
 * Group, Inc., 2018. The concept of the electric circuit is the use of the
 * SysML proxy port to model the interface between electrical components, and
 * the use of the SysML constraint block to perform parametric specification and
 * analysis of the circuit model.
 * <p>
 * The {@code Circuit} consists of five components, i.e. the AC voltage source,
 * {@code s}, a resistor {@code rc} and capacitor {@code c} in the RC circuit,
 * and a resistor {@code rl} and inductor {@code l} in the RL circuit. A ground
 * {@code g} is also in the circuit model. The five components all inherit a
 * basic {@code TwoPinElectricalComponent} block that contains properties common
 * to all such components, i.e. ports for the two pins, and voltage and current
 * across the component. The blocks that represent the specific compoents add
 * properties unique to the component type, e.g. resistance, capacitance,
 * inductance, etc.
 * <p>
 * The circuit model also consists of six constraint blocks, one for each
 * component in the circuit. The constraint blocks also inherit from a common
 * constraint block, the {@code BinaryElectricalComponentConstraint}. The
 * properties of the components are obtained from the constraints in the
 * constraint blocks associated with each. Constraints common to all such
 * components are specified in the inherited constraint block while unique
 * constraints such as the calculation of voltage and current across the
 * component at any time, are specified in the specialized constraint block. In
 * essence, the SysMLinJava model of the electrical circuit is an executable
 * model of a system of electrical components defined by its constraints where
 * the model is verified/validated by its conformance to the system's
 * constraints.
 * <p>
 * The two-pin components each have two proxy ports that represent its interface
 * with other connected components. These proxy ports include values for the
 * voltage and current flows at the pin interface to the component at any
 * instant in the model execution. As the voltage at the AC source changes, the
 * values of voltage and current across the components and at their pin port
 * interfaces is calculated in accordance with the constraints imposed by the
 * constraint blocks. These values are "bound" to constraint parameters in the
 * constraint blocks where their values are used in constraint assertions for
 * parametric analysis of the modeled circuit.
 * <p>
 * The executing model also generates and transmits circuit component values
 * (voltage and current) for display in a set of line charts, as available in
 * the SysMLinJava TaskMaster&trade;. These line chart displays show the
 * sinusoidal graph of the component values over time providing a useful view of
 * the executing model of the circuit. These charts are usefull for model
 * validation and other parameteric analysis as needed.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 */
public class Circuit extends SysMLBlock
{
	/**
	 * Voltage source (AC) for the circuit
	 */
	@BOMLineItem
	@Part
	VoltageSource voltageSource;
	/**
	 * Ground for the circuit
	 */
	@BOMLineItem
	@Part
	Ground ground;
	/**
	 * Resistor in the RC branch of the circuit
	 */
	@BOMLineItem
	@Part
	ResistorRC resistorRC;
	/**
	 * Resistor in the RL branch of the circuit
	 */
	@BOMLineItem
	@Part
	ResistorRL resistorRL;
	/**
	 * Capacitor in the RC branch of the circuit
	 */
	@BOMLineItem
	@Part
	CapacitorRC capacitorRC;
	/**
	 * Inductor in the RL branch of the circuit
	 */
	@BOMLineItem
	@Part
	InductorRL inductorRL;

	/**
	 * Current flowing through the circuit
	 */
	@Value
	Current i;

	/**
	 * Constraint block for the voltage source
	 */
	@ConstraintBlock
	VoltageSourceConstraint voltageSourceConstraint;
	/**
	 * Constraint block for the ground
	 */
	@ConstraintBlock
	GroundConstraint groundConstraint;
	/**
	 * Constraint block for the resistor in the RC branch of the circuit
	 */
	@ConstraintBlock
	ResistorConstraint resistorRCConstraint;
	/**
	 * Constraint block for the resistor in the RL branch of the circuit
	 */
	@ConstraintBlock
	ResistorConstraint resistorRLConstraint;
	/**
	 * Constraint block for the capacitor in the RC branch of the circuit
	 */
	@ConstraintBlock
	CapacitorConstraint capacitorRCConstraint;
	/**
	 * Constraint block for the inductor in the RL branch of the circuit
	 */
	@ConstraintBlock
	InductorConstraint inductorRLConstraint;

	/**
	 * Function that connects the components in the circuit
	 */
	@AssociationConnectorFunction
	SysMLAssociationBlockConnectorFunction connectorFunction;

	/**
	 * Connector that executes the functions that connect the componens of the
	 * circuit
	 */
	@AssociationConnector
	SysMLAssociationBlockConnector connector;

	/**
	 * Function that binds the constraint parameters in the constraint blocks to the
	 * parameteric values of the voltage source s
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction sParametersConnectorFunction;
	/**
	 * Function that binds the constraint parameters in the constraint blocks to the
	 * parameteric values of the ground g
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction gParametersConnectorFunction;
	/**
	 * Function that binds the constraint parameters in the constraint blocks to the
	 * parameteric values of the resistor rc in the capacitance branch
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction rcParametersConnectorFunction;
	/**
	 * Function that binds the constraint parameters in the constraint blocks to the
	 * parameteric values of the resistor rl in the inductance branch
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction rlParametersConnectorFunction;
	/**
	 * Function that binds the constraint parameters in the constraint blocks to the
	 * parameteric values of the capacitor c
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction cParametersConnectorFunction;
	/**
	 * Function that binds the constraint parameters in the constraint blocks to the
	 * parameteric values of the inductor l
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction lParametersConnectorFunction;

	/**
	 * Connector that invokes the function that binds the constraint parameters in
	 * the constraint blocks to the parameteric values of the voltage source s
	 */
	@BindingConnector
	public SysMLBindingConnector sParametersConnector;
	/**
	 * Connector that invokes the function that binds the constraint parameters in
	 * the constraint blocks to the parameteric values of the ground g
	 */
	@BindingConnector
	public SysMLBindingConnector gParametersConnector;
	/**
	 * Connector that invokes the function that binds the constraint parameters in
	 * the constraint blocks to the parameteric values of the resistor rc in the
	 * capacitance branch
	 */
	@BindingConnector
	public SysMLBindingConnector rcParametersConnector;
	/**
	 * Connector that invokes the function that binds the constraint parameters in
	 * the constraint blocks to the parameteric values of the resistor rl in the
	 * inductance branch
	 */
	@BindingConnector
	public SysMLBindingConnector rlParametersConnector;
	/**
	 * Connector that invokes the function that binds the constraint parameters in
	 * the constraint blocks to the parameteric values of the capacitor c
	 */
	@BindingConnector
	public SysMLBindingConnector cParametersConnector;
	/**
	 * Connector that invokes the function that binds the constraint parameters in
	 * the constraint blocks to the parameteric values of the inductor l
	 */
	@BindingConnector
	public SysMLBindingConnector lParametersConnector;

	/**
	 * Transmitter of constraint parameter values for rc component to a line chart
	 * display
	 */
	public LineChartsTransmitter rRCLineChartsTransmitter;
	/**
	 * Transmitter of constraint parameter values for c component to a line chart
	 * display
	 */
	public LineChartsTransmitter cRCLineChartsTransmitter;
	/**
	 * Transmitter of constraint parameter values for rl component to a line chart
	 * display
	 */
	public LineChartsTransmitter rRLLineChartsTransmitter;
	/**
	 * Transmitter of constraint parameter values for l component to a line chart
	 * display
	 */
	public LineChartsTransmitter iRLLineChartsTransmitter;

	/**
	 * Constructor that creates the line chart data transmitters and transmits the
	 * definitions of the line charts that will be produced by the executing model
	 */
	public Circuit()
	{
		super();
		rRCLineChartsTransmitter = new LineChartsTransmitter(LineChartsDisplay.udpPort, false);
		cRCLineChartsTransmitter = new LineChartsTransmitter(LineChartsDisplay.udpPort + 1, false);
		rRLLineChartsTransmitter = new LineChartsTransmitter(LineChartsDisplay.udpPort + 2, false);
		iRLLineChartsTransmitter = new LineChartsTransmitter(LineChartsDisplay.udpPort + 3, false);
		transmitResistorRCLineChartDefinition();
		transmitCapacitorRCLineChartDefinition();
		transmitResistorRLLineChartDefinition();
		transmitInductorRLLineChartDefinition();
	}

	/**
	 * Operation to execute the model and thereby simulate the circuit operation
	 * 
	 * @param deltaDuration incrments of time for the simulation
	 * @param maxDuration   max time to execute the model and run the simulation
	 */
	@Operation
	public void operate(DurationSeconds deltaDuration, DurationSeconds maxDuration)
	{
		logger.info("begins...");
		DurationSeconds time;
		for (time = new DurationSeconds(0); time.lessThan(maxDuration); time.add(deltaDuration))
		{
			voltageSource.updateVin(time);
			capacitorRC.updateVDrop(resistorRC, voltageSource.input, voltageSource.dvdt, time);
			resistorRC.updateVDrop(voltageSource.input, capacitorRC.vDrop);
			inductorRL.updateVDrop(resistorRL, voltageSource.input, time);
			resistorRL.updateVDrop(inductorRL, voltageSource.input, time);
			resistorRC.updateIThru(voltageSource.input);
			resistorRL.updateIThru(voltageSource.input);
			capacitorRC.updateIthru(resistorRC, voltageSource.input);
			inductorRL.updateIthru(resistorRL, voltageSource.input);
			i.setValue(resistorRC.iThru.added(resistorRL.iThru));
			voltageSource.updateIThru(i);

			transmitRCLineChartData();
			transmitCLineChartData();
			transmitRLLineChartData();
			transmitLLineChartData();
			System.out.println(String.format("%2.3f) %+8.3f [%+8.3f %+8.3f %+8.3f] [%+8.3f %+8.3f %+8.3f]", time.value, voltageSource.input.value, capacitorRC.vDrop.value, resistorRC.vDrop.value, capacitorRC.iThru.value,
				inductorRL.vDrop.value, resistorRL.vDrop.value, inductorRL.iThru.value));
		}
		logger.info("done");
	}

	@Override
	protected void createValues()
	{
		i = new Current(0);
	}

	@Override
	protected void createParts()
	{
		voltageSource = new VoltageSource("VoltageSource");
		ground = new Ground("Ground");
		resistorRL = new ResistorRL("ResistorRL");
		resistorRC = new ResistorRC("ResistorRC");
		inductorRL = new InductorRL("InductorRL");
		capacitorRC = new CapacitorRC("CapacitorRC");
	}

	@Override
	protected void createConstraintBlocks()
	{
		voltageSourceConstraint = new VoltageSourceConstraint("VoltageSourceConstraint");
		groundConstraint = new GroundConstraint("GroundConstraint");
		resistorRCConstraint = new ResistorConstraint("ResistorRCConstraint");
		resistorRLConstraint = new ResistorConstraint("ResistorRLConstraint");
		inductorRLConstraint = new InductorConstraint("InductorConstraint");
		capacitorRCConstraint = new CapacitorConstraint("CapacitorConstraint");
	}

	@Override
	protected void createConnectorFunctions()
	{
		connectorFunction = () ->
		{
			voltageSource.p.addConnectedPortPeer(resistorRC.p);
			voltageSource.p.addConnectedPortPeer(resistorRL.p);
			resistorRC.n.addConnectedPortPeer(capacitorRC.p);
			resistorRL.n.addConnectedPortPeer(inductorRL.p);
			inductorRL.n.addConnectedPortPeer(ground.p);
			capacitorRC.n.addConnectedPortPeer(ground.p);
			voltageSource.n.addConnectedPortPeer(ground.p);
		};
	}

	@Override
	protected void createConnectors()
	{
		connector = new SysMLAssociationBlockConnector(List.of(voltageSource, resistorRC, resistorRL, inductorRL, capacitorRC, ground), List.of(voltageSource, resistorRC, resistorRL, inductorRL, capacitorRC, ground), connectorFunction);
	}

	@Override
	protected void createConstraintParameterConnectorFunctions()
	{
		sParametersConnectorFunction = () ->
		{
			SysMLConstraintParameterPort ampPort = voltageSourceConstraint.ampPort;
			ampPort.parameterContextBlock = voltageSource;
			voltageSource.amplitude.bindTo(ampPort);

			SysMLConstraintParameterPort posIPort = voltageSourceConstraint.posIPort;
			posIPort.parameterContextBlock = voltageSource;
			voltageSource.p.cF.i.bindTo(posIPort);
			SysMLConstraintParameterPort posVPort = voltageSourceConstraint.posVPort;
			posVPort.parameterContextBlock = voltageSource;
			voltageSource.p.cF.v.bindTo(posVPort);

			SysMLConstraintParameterPort negIPort = voltageSourceConstraint.negIPort;
			negIPort.parameterContextBlock = voltageSource;
			voltageSource.n.cF.i.bindTo(negIPort);
			SysMLConstraintParameterPort negVPort = voltageSourceConstraint.negVPort;
			negVPort.parameterContextBlock = voltageSource;
			voltageSource.n.cF.v.bindTo(negVPort);

			SysMLConstraintParameterPort iPort = voltageSourceConstraint.iPort;
			iPort.parameterContextBlock = voltageSource;
			voltageSource.iThru.bindTo(iPort);
			SysMLConstraintParameterPort vPort = voltageSourceConstraint.vPort;
			vPort.parameterContextBlock = voltageSource;
			voltageSource.vDrop.bindTo(vPort);

			SysMLConstraintParameterPort timePort = voltageSourceConstraint.timePort;
			timePort.parameterContextBlock = voltageSource;
			voltageSource.time.bindTo(timePort);
		};
		gParametersConnectorFunction = () ->
		{
			SysMLConstraintParameterPort posVPort = groundConstraint.posVPort;
			posVPort.parameterContextBlock = ground;
			ground.p.cF.v.bindTo(posVPort);
		};
		rcParametersConnectorFunction = () ->
		{
			SysMLConstraintParameterPort rPort = resistorRCConstraint.rPort;
			rPort.parameterContextBlock = resistorRC;
			resistorRC.resistance.bindTo(rPort);

			SysMLConstraintParameterPort posIPort = resistorRCConstraint.posIPort;
			posIPort.parameterContextBlock = resistorRC;
			resistorRC.p.cF.i.bindTo(posIPort);
			SysMLConstraintParameterPort posVPort = resistorRCConstraint.posVPort;
			posVPort.parameterContextBlock = resistorRC;
			resistorRC.p.cF.v.bindTo(posVPort);

			SysMLConstraintParameterPort negIPort = resistorRCConstraint.negIPort;
			negIPort.parameterContextBlock = resistorRC;
			resistorRC.n.cF.i.bindTo(negIPort);
			SysMLConstraintParameterPort negVPort = resistorRCConstraint.negVPort;
			negVPort.parameterContextBlock = resistorRC;
			resistorRC.n.cF.v.bindTo(negVPort);

			SysMLConstraintParameterPort iPort = resistorRCConstraint.iPort;
			iPort.parameterContextBlock = resistorRC;
			resistorRC.iThru.bindTo(iPort);
			SysMLConstraintParameterPort vPort = resistorRCConstraint.vPort;
			vPort.parameterContextBlock = resistorRC;
			resistorRC.vDrop.bindTo(vPort);

			SysMLConstraintParameterPort timePort = resistorRCConstraint.timePort;
			timePort.parameterContextBlock = voltageSource;
			voltageSource.time.bindTo(timePort);
		};
		rlParametersConnectorFunction = () ->
		{
			SysMLConstraintParameterPort rPort = resistorRLConstraint.rPort;
			rPort.parameterContextBlock = resistorRL;
			resistorRL.resistance.bindTo(rPort);

			SysMLConstraintParameterPort posIPort = resistorRLConstraint.posIPort;
			posIPort.parameterContextBlock = resistorRL;
			resistorRL.p.cF.i.bindTo(posIPort);
			SysMLConstraintParameterPort posVPort = resistorRLConstraint.posVPort;
			posVPort.parameterContextBlock = resistorRL;
			resistorRL.p.cF.v.bindTo(posVPort);

			SysMLConstraintParameterPort negIPort = resistorRLConstraint.negIPort;
			negIPort.parameterContextBlock = resistorRL;
			resistorRL.n.cF.i.bindTo(negIPort);
			SysMLConstraintParameterPort negVPort = resistorRLConstraint.negVPort;
			negVPort.parameterContextBlock = resistorRL;
			resistorRL.n.cF.v.bindTo(negVPort);

			SysMLConstraintParameterPort iPort = resistorRLConstraint.iPort;
			iPort.parameterContextBlock = resistorRL;
			resistorRL.iThru.bindTo(iPort);
			SysMLConstraintParameterPort vPort = resistorRLConstraint.vPort;
			vPort.parameterContextBlock = resistorRL;
			resistorRL.vDrop.bindTo(vPort);

			SysMLConstraintParameterPort timePort = resistorRLConstraint.timePort;
			timePort.parameterContextBlock = voltageSource;
			voltageSource.time.bindTo(timePort);
		};
		cParametersConnectorFunction = () ->
		{
			SysMLConstraintParameterPort cPort = capacitorRCConstraint.cPort;
			cPort.parameterContextBlock = capacitorRC;
			capacitorRC.capacitance.bindTo(cPort);

			SysMLConstraintParameterPort posIPort = capacitorRCConstraint.posIPort;
			posIPort.parameterContextBlock = capacitorRC;
			capacitorRC.p.cF.i.bindTo(posIPort);
			SysMLConstraintParameterPort posVPort = capacitorRCConstraint.posVPort;
			posVPort.parameterContextBlock = capacitorRC;
			capacitorRC.p.cF.v.bindTo(posVPort);

			SysMLConstraintParameterPort negIPort = capacitorRCConstraint.negIPort;
			negIPort.parameterContextBlock = capacitorRC;
			capacitorRC.n.cF.i.bindTo(negIPort);
			SysMLConstraintParameterPort negVPort = capacitorRCConstraint.negVPort;
			negVPort.parameterContextBlock = capacitorRC;
			capacitorRC.n.cF.v.bindTo(negVPort);

			SysMLConstraintParameterPort iPort = capacitorRCConstraint.iPort;
			iPort.parameterContextBlock = capacitorRC;
			capacitorRC.iThru.bindTo(iPort);
			SysMLConstraintParameterPort vPort = capacitorRCConstraint.vPort;
			vPort.parameterContextBlock = capacitorRC;
			capacitorRC.vDrop.bindTo(vPort);

			SysMLConstraintParameterPort timePort = capacitorRCConstraint.timePort;
			timePort.parameterContextBlock = voltageSource;
			voltageSource.time.bindTo(timePort);
		};
		lParametersConnectorFunction = () ->
		{
			SysMLConstraintParameterPort lPort = inductorRLConstraint.lPort;
			lPort.parameterContextBlock = inductorRL;
			inductorRL.inductance.bindTo(lPort);

			SysMLConstraintParameterPort posIPort = inductorRLConstraint.posIPort;
			posIPort.parameterContextBlock = inductorRL;
			inductorRL.p.cF.i.bindTo(posIPort);
			SysMLConstraintParameterPort posVPort = inductorRLConstraint.posVPort;
			posVPort.parameterContextBlock = inductorRL;
			inductorRL.p.cF.v.bindTo(posVPort);

			SysMLConstraintParameterPort negIPort = inductorRLConstraint.negIPort;
			negIPort.parameterContextBlock = inductorRL;
			inductorRL.n.cF.i.bindTo(negIPort);
			SysMLConstraintParameterPort negVPort = inductorRLConstraint.negVPort;
			negVPort.parameterContextBlock = inductorRL;
			inductorRL.n.cF.v.bindTo(negVPort);

			SysMLConstraintParameterPort iPort = inductorRLConstraint.iPort;
			iPort.parameterContextBlock = inductorRL;
			inductorRL.iThru.bindTo(iPort);
			SysMLConstraintParameterPort vPort = inductorRLConstraint.vPort;
			vPort.parameterContextBlock = inductorRL;
			inductorRL.vDrop.bindTo(vPort);

			SysMLConstraintParameterPort timePort = inductorRLConstraint.timePort;
			timePort.parameterContextBlock = voltageSource;
			voltageSource.time.bindTo(timePort);
		};
	}

	@Override
	protected void createConstraintParameterConnectors()
	{
		sParametersConnector = new SysMLBindingConnector(voltageSource, voltageSourceConstraint, sParametersConnectorFunction);
		gParametersConnector = new SysMLBindingConnector(ground, groundConstraint, gParametersConnectorFunction);
		rlParametersConnector = new SysMLBindingConnector(resistorRL, resistorRLConstraint, rlParametersConnectorFunction);
		rcParametersConnector = new SysMLBindingConnector(resistorRC, resistorRCConstraint, rcParametersConnectorFunction);
		cParametersConnector = new SysMLBindingConnector(capacitorRC, capacitorRCConstraint, cParametersConnectorFunction);
		lParametersConnector = new SysMLBindingConnector(inductorRL, inductorRLConstraint, lParametersConnectorFunction);
	}

	/**
	 * Title of the line chart used to display the changing values of rc component
	 * currents and voltages
	 */
	static final String rcLineChartTitle = "Circuit Component ResistorRC Voltages and Currents";
	/**
	 * Title of the line chart used to display the changing values of c component
	 * currents and voltages
	 */
	static final String cLineChartTitle = "Circuit Component CapacitorRC Voltages and Currents";
	/**
	 * Title of the line chart used to display the changing values of rl component
	 * currents and voltages
	 */
	static final String rlLineChartTitle = "Circuit Component ResistorRL Voltages and Currents";
	/**
	 * Title of the line chart used to display the changing values of l component
	 * currents and voltages
	 */
	static final String lLineChartTitle = "Circuit Component InductorRL Voltages and Currents";

	/**
	 * Transmits the definition of the line chart of the constraint parameters (rc
	 * component currents and voltages) to the graphical line chart display
	 * 
	 * @see sysmlinjava.analysis.common.Axis
	 * @see sysmlinjava.analysis.linecharts.LineChartDefinition
	 */
	protected void transmitResistorRCLineChartDefinition()
	{
		Axis xAxis = new Axis("Time", "seconds", Optional.of(0.0), Optional.of(10.0), 1.0, 10);
		AxisFixedRange yAxisVoltages = new AxisFixedRange("Voltage", "volts", -25.0, 25.0, 5.0, 10);
		AxisFixedRange yAxisCurrents = new AxisFixedRange("Current", "amperes", -5.0, 5.0, 1.0, 10);
		ArrayList<AxisFixedRange> yAxes = new ArrayList<>(Arrays.asList(yAxisVoltages, yAxisCurrents));
		LineChartDefinition graph = new LineChartDefinition(rcLineChartTitle, yAxes, xAxis);
		rRCLineChartsTransmitter.transmitGraph(graph);
	}

	/**
	 * Transmits the definition of the line chart of the constraint parameters (c
	 * component currents and voltages) to the graphical line chart display
	 * 
	 * @see sysmlinjava.analysis.common.Axis
	 * @see sysmlinjava.analysis.linecharts.LineChartDefinition
	 */
	protected void transmitCapacitorRCLineChartDefinition()
	{
		Axis xAxis = new Axis("Time", "seconds", Optional.of(0.0), Optional.of(10.0), 1.0, 10);
		AxisFixedRange yAxisVoltages = new AxisFixedRange("Voltage", "volts", -240.0, 240.0, 20.0, 4);
		AxisFixedRange yAxisCurrents = new AxisFixedRange("Current", "amperes", -5.0, 5.0, 1.0, 10);
		ArrayList<AxisFixedRange> yAxes = new ArrayList<>(Arrays.asList(yAxisVoltages, yAxisCurrents));
		LineChartDefinition graph = new LineChartDefinition(cLineChartTitle, yAxes, xAxis);
		cRCLineChartsTransmitter.transmitGraph(graph);
	}

	/**
	 * Transmits the definition of the line chart of the constraint parameters (rl
	 * component currents and voltages) to the graphical line chart display
	 * 
	 * @see sysmlinjava.analysis.common.Axis
	 * @see sysmlinjava.analysis.linecharts.LineChartDefinition
	 */
	protected void transmitResistorRLLineChartDefinition()
	{
		Axis xAxis = new Axis("Time", "seconds", Optional.of(0.0), Optional.of(10.0), 1.0, 10);
		AxisFixedRange yAxisVoltages = new AxisFixedRange("Voltage", "volts", -240.0, 240.0, 20.0, 5);
		AxisFixedRange yAxisCurrents = new AxisFixedRange("Current", "amperes", -20.0, 20.0, 1.0, 5);
		ArrayList<AxisFixedRange> yAxes = new ArrayList<>(Arrays.asList(yAxisVoltages, yAxisCurrents));
		LineChartDefinition graph = new LineChartDefinition(rlLineChartTitle, yAxes, xAxis);
		rRLLineChartsTransmitter.transmitGraph(graph);
	}

	/**
	 * Transmits the definition of the line chart of the constraint parameters (l
	 * component currents and voltages) to the graphical line chart display
	 * 
	 * @see sysmlinjava.analysis.common.Axis
	 * @see sysmlinjava.analysis.linecharts.LineChartDefinition
	 */
	protected void transmitInductorRLLineChartDefinition()
	{
		Axis xAxis = new Axis("Time", "seconds", Optional.of(0.0), Optional.of(10.0), 1.0, 10);
		AxisFixedRange yAxisVoltages = new AxisFixedRange("Voltage", "volts", -2.0, 2.0, 0.1, 5);
		AxisFixedRange yAxisCurrents = new AxisFixedRange("Current", "amperes", -12.0, 12.0, 1.0, 5);
		ArrayList<AxisFixedRange> yAxes = new ArrayList<>(Arrays.asList(yAxisVoltages, yAxisCurrents));
		LineChartDefinition graph = new LineChartDefinition(lLineChartTitle, yAxes, xAxis);
		iRLLineChartsTransmitter.transmitGraph(graph);
	}

	/**
	 * Transmits the values of the constraint parameters (rc component currents and
	 * voltages) to the graphical line chart display
	 * 
	 * @see sysmlinjava.valuetypes.Point2D
	 * @see sysmlinjava.analysis.linecharts.LineChartData
	 */
	protected void transmitRCLineChartData()
	{
		List<Point2D> xyVoltage = new ArrayList<>(Arrays.asList(new Point2D(voltageSourceConstraint.time.value, resistorRCConstraint.v.value)));
		List<Point2D> xyCurrent = new ArrayList<>(Arrays.asList(new Point2D(voltageSourceConstraint.time.value, resistorRCConstraint.i.value)));
		List<List<Point2D>> xyListList = new ArrayList<>(Arrays.asList(xyVoltage, xyCurrent));
		LineChartData graphData = new LineChartData(rcLineChartTitle, xyListList);
		rRCLineChartsTransmitter.transmitGraphData(graphData);
	}

	/**
	 * Transmits the values of the constraint parameters (c component currents and
	 * voltages) to the graphical line chart display
	 * 
	 * @see sysmlinjava.valuetypes.Point2D
	 * @see sysmlinjava.analysis.linecharts.LineChartData
	 */
	protected void transmitCLineChartData()
	{
		List<Point2D> xyVoltage = new ArrayList<>(Arrays.asList(new Point2D(voltageSourceConstraint.time.value, capacitorRCConstraint.v.value)));
		List<Point2D> xyCurrent = new ArrayList<>(Arrays.asList(new Point2D(voltageSourceConstraint.time.value, capacitorRCConstraint.i.value)));
		List<List<Point2D>> xyListList = new ArrayList<>(Arrays.asList(xyVoltage, xyCurrent));
		LineChartData graphData = new LineChartData(cLineChartTitle, xyListList);
		cRCLineChartsTransmitter.transmitGraphData(graphData);
	}

	/**
	 * Transmits the values of the constraint parameters (rl component currents and
	 * voltages) to the graphical line chart display
	 * 
	 * @see sysmlinjava.valuetypes.Point2D
	 * @see sysmlinjava.analysis.linecharts.LineChartData
	 */
	protected void transmitRLLineChartData()
	{
		List<Point2D> xyVoltage = new ArrayList<>(Arrays.asList(new Point2D(voltageSourceConstraint.time.value, resistorRLConstraint.v.value)));
		List<Point2D> xyCurrent = new ArrayList<>(Arrays.asList(new Point2D(voltageSourceConstraint.time.value, resistorRLConstraint.i.value)));
		List<List<Point2D>> xyListList = new ArrayList<>(Arrays.asList(xyVoltage, xyCurrent));
		LineChartData graphData = new LineChartData(rlLineChartTitle, xyListList);
		rRLLineChartsTransmitter.transmitGraphData(graphData);
	}

	/**
	 * Transmits the values of the constraint parameters (l component currents and
	 * voltages) to the graphical line chart display
	 * 
	 * @see sysmlinjava.valuetypes.Point2D
	 * @see sysmlinjava.analysis.linecharts.LineChartData
	 */
	protected void transmitLLineChartData()
	{
		List<Point2D> xyVoltage = new ArrayList<>(Arrays.asList(new Point2D(voltageSourceConstraint.time.value, inductorRLConstraint.v.value)));
		List<Point2D> xyCurrent = new ArrayList<>(Arrays.asList(new Point2D(voltageSourceConstraint.time.value, inductorRLConstraint.i.value)));
		List<List<Point2D>> xyListList = new ArrayList<>(Arrays.asList(xyVoltage, xyCurrent));
		LineChartData graphData = new LineChartData(lLineChartTitle, xyListList);
		iRLLineChartsTransmitter.transmitGraphData(graphData);
	}

	/**
	 * Main operation/execution of the model
	 * 
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		Circuit circuit = new Circuit();
		circuit.operate(DurationSeconds.of(0.01), DurationSeconds.of(10.0));
		Runtime.getRuntime().exit(0);
	}
}
