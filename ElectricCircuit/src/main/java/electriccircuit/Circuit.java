package electriccircuit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import sysmlinjava.attributetypes.CurrentAmps;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.BindingConnector;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.bom.annotations.BOMLineItem;
import sysmlinjava.views.common.Axis;
import sysmlinjava.views.common.AxisFixedRange;
import sysmlinjava.views.linechart.LineChartData;
import sysmlinjava.views.linechart.LineChartDefinition;
import sysmlinjava.views.linechart.LineChartsDisplay;
import sysmlinjava.views.linechart.LineChartsTransmitter;

/**
 * SysMLinJava model of a simple electrical circuit of an AC voltage source in
 * parallel with an RC and a RL circuit. The model is a SysMLinJava
 * implementation of the model by the same name described in "SysML Extension
 * for Physical Interaction and Signal Flow Simulation", Object Management
 * Group, Inc., 2018. The concept of the electric circuit is the use of the
 * SysML proxy port to model the interface between electrical components, and
 * the use of the SysML Parametric Analysis Case to perform parametric specification and
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
 * The circuit model also consists of six Parametric Analysis Cases, one for each
 * component in the circuit. The Parametric Analysis Cases also inherit from a common
 * Parametric Analysis Case, the {@code BinaryElectricalComponentConstraint}. The
 * properties of the components are obtained from the constraints in the
 * Parametric Analysis Cases associated with each. Constraints common to all such
 * components are specified in the inherited Parametric Analysis Case while unique
 * constraints such as the calculation of voltage and CurrentAmps across the
 * component at any time, are specified in the specialized Parametric Analysis Case. In
 * essence, the SysMLinJava model of the electrical circuit is an executable
 * model of a system of electrical components defined by its constraints where
 * the model is verified/validated by its conformance to the system's
 * constraints.
 * <p>
 * The two-pin components each have two proxy ports that represent its interface
 * with other connected components. These proxy ports include values for the
 * voltage and CurrentAmps flows at the pin interface to the component at any
 * instant in the model execution. As the voltage at the AC source changes, the
 * values of voltage and CurrentAmps across the components and at their pin port
 * interfaces is calculated in accordance with the constraints imposed by the
 * Parametric Analysis Cases. These values are "bound" to constraint parameters in the
 * Parametric Analysis Cases where their values are used in constraint assertions for
 * parametric analysis of the modeled circuit.
 * <p>
 * The executing model also generates and transmits circuit component values
 * (voltage and current) for display in a set of line charts, as available in
 * the SysMLinJava TaskMaster&trade;. These line chart displays show the
 * sinusoidal graph of the component values over time providing a useful view of
 * the executing model of the circuit. These charts are usefull for model
 * validation and other parameteric analysis as needed.
 * 
 * @implNote The {@code Circuit} is annotated for use in generating a
 *           bill-of-materials.
 * @see <a href="https://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * @author ModelerOne
 */
public class Circuit extends SysMLPart
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
	 * CurrentAmps flowing through the circuit
	 */
	@Attribute
	CurrentAmps i;

	/**
	 * Parametric Analysis Case for the voltage source
	 */
	@ParametricAnalysis
	VoltageSourceConstraint voltageSourceConstraint;
	/**
	 * Parametric Analysis Case for the ground
	 */
	@ParametricAnalysis
	GroundConstraint groundConstraint;
	/**
	 * Parametric Analysis Case for the resistor in the RC branch of the circuit
	 */
	@ParametricAnalysis
	ResistorConstraint resistorRCConstraint;
	/**
	 * Parametric Analysis Case for the resistor in the RL branch of the circuit
	 */
	@ParametricAnalysis
	ResistorConstraint resistorRLConstraint;
	/**
	 * Parametric Analysis Case for the capacitor in the RC branch of the circuit
	 */
	@ParametricAnalysis
	CapacitorConstraint capacitorRCConstraint;
	/**
	 * Parametric Analysis Case for the inductor in the RL branch of the circuit
	 */
	@ParametricAnalysis
	InductorConstraint inductorRLConstraint;

	/**
	 * Connector for voltage source to the RC resistor
	 */
	@FlowConnector
	private SysMLFlowConnector voltageSourceToResistorRC;
	/**
	 * Connector for voltage source to the RL resistor
	 */
	@FlowConnector
	private SysMLFlowConnector voltageSourceToResistorRL;
	/**
	 * Connector for the RC resistor to the RC capactor
	 */
	@FlowConnector
	private SysMLFlowConnector resisterRCToCapacitorRC;
	/**
	 * Connector for the RL resistor to the RL inductor
	 */
	@FlowConnector
	private SysMLFlowConnector resisterRLToInductorRL;
	/**
	 * Connector for the inductor to ground
	 */
	@FlowConnector
	private SysMLFlowConnector inductorRLToGround;
	/**
	 * Connector for the capacitor to ground
	 */
	@FlowConnector
	private SysMLFlowConnector capacitorRCToGround;
	/**
	 * Connector for voltage source to ground
	 */
	@FlowConnector
	private SysMLFlowConnector voltageSourceToGround;

	/**
	 * Binding of voltage source amplitude to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector sAmpBindingConnector;
	/**
	 * Binding of voltage source CurrentAmps to parameter in parametric analysis
	 * case
	 */
	@BindingConnector
	public SysMLBindingConnector sIBindingConnector;
	/**
	 * Binding of voltage source negative terminal CurrentAmps to parameter in
	 * parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector sNegIBindingConnector;
	/**
	 * Binding of voltage source positive terminal CurrentAmps to parameter in
	 * parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector sPosIBindingConnector;
	/**
	 * Binding of voltage source voltage to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector sVBindingConnector;
	/**
	 * Binding of voltage source negtive terminal voltage to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector sNegVBindingConnector;
	/**
	 * Binding of voltage source negative terminal voltage to parameter in
	 * parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector sPosVBindingConnector;
	/**
	 * Binding of voltage source time to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector sTimeBindingConnector;
	/**
	 * Binding of resistor in RC circuit resistane to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rcRBindingConnector;
	/**
	 * Binding of resistor in RC circuit resistane to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rcIBindingConnector;
	/**
	 * Binding of resistor in RC circuit resistane to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rcNegIBindingConnector;
	/**
	 * Binding of resistor in RC circuit resistane to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rcPosIBindingConnector;
	/**
	 * Binding of resistor in RC circuit resistane to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rcVBindingConnector;
	/**
	 * Binding of resistor in RC circuit resistane to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rcNegVBindingConnector;
	/**
	 * Binding of resistor in RC circuit resistane to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rcPosVBindingConnector;
	/**
	 * Binding of resistor in RC circuit resistane to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rcTimeBindingConnector;
	/**
	 * Binding of resistor in RL circuit to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rlRBindingConnector;
	/**
	 * Binding of inductance in RL circuit to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rlIBindingConnector;
	/**
	 * Binding of negative current thru RL circuit to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rlNegIBindingConnector;
	/**
	 * Binding of positive current thru RL circuit to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rlPosIBindingConnector;
	/**
	 * Binding of voltage across RL circuit to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rlVBindingConnector;
	/**
	 * Binding of negative voltage across RL circuit to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rlNegVBindingConnector;
	/**
	 * Binding of positive voltage across RL circuit to parameter in parametric
	 * analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rlPosVBindingConnector;
	/**
	 * Binding of time in RL circuit to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector rlTimeBindingConnector;
	/**
	 * Binding of capaitor in RC circuit to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector cCBindingConnector;
	@BindingConnector
	public SysMLBindingConnector cIBindingConnector;
	@BindingConnector
	public SysMLBindingConnector cNegIBindingConnector;
	@BindingConnector
	public SysMLBindingConnector cPosIBindingConnector;
	@BindingConnector
	public SysMLBindingConnector cVBindingConnector;
	@BindingConnector
	public SysMLBindingConnector cNegVBindingConnector;
	@BindingConnector
	public SysMLBindingConnector cPosVBindingConnector;
	@BindingConnector
	public SysMLBindingConnector cTimeBindingConnector;
	/**
	 * Binding of inductor in RL circuit to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector iLBindingConnector;
	@BindingConnector
	public SysMLBindingConnector iIBindingConnector;
	@BindingConnector
	public SysMLBindingConnector iPosIBindingConnector;
	@BindingConnector
	public SysMLBindingConnector iPosVBindingConnector;
	@BindingConnector
	public SysMLBindingConnector iNegIBindingConnector;
	@BindingConnector
	public SysMLBindingConnector iVBindingConnector;
	@BindingConnector
	public SysMLBindingConnector iNegVBindingConnector;
	@BindingConnector
	public SysMLBindingConnector iTimeBindingConnector;
	/**
	 * Binding of ground to parameter in parametric analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector gPosVBindingConnector;

	/**
	 * Transmitter of parametric analysis values for rc component to a line chart
	 * display
	 */
	public LineChartsTransmitter rRCLineChartsTransmitter;
	/**
	 * Transmitter of parametric analysis values for c component to a line chart
	 * display
	 */
	public LineChartsTransmitter cRCLineChartsTransmitter;
	/**
	 * Transmitter of parametric analysis values for rl component to a line chart
	 * display
	 */
	public LineChartsTransmitter rRLLineChartsTransmitter;
	/**
	 * Transmitter of parametric analysis values for l component to a line chart
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
	@Action
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
		}
		logger.info("done");
	}

	@Override
	protected void createAttributes()
	{
		i = new CurrentAmps(0);
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
	protected void createAnalysisCases()
	{
		voltageSourceConstraint = new VoltageSourceConstraint("VoltageSourceConstraint");
		groundConstraint = new GroundConstraint("GroundConstraint");
		resistorRCConstraint = new ResistorConstraint("ResistorRCConstraint");
		resistorRLConstraint = new ResistorConstraint("ResistorRLConstraint");
		inductorRLConstraint = new InductorConstraint("InductorConstraint");
		capacitorRCConstraint = new CapacitorConstraint("CapacitorConstraint");
	}

	@Override
	protected void createFlowConnectors()
	{
		voltageSourceToResistorRC = new SysMLFlowConnector(voltageSource.p, resistorRC.p, "VoltageSourceToResistorRC", 0L);
		voltageSourceToResistorRL = new SysMLFlowConnector(voltageSource.p, resistorRL.p, "VoltageSourceToResistorRL", 0L);
		resisterRCToCapacitorRC = new SysMLFlowConnector(resistorRC.n, capacitorRC.p, "ResistorRCToCapacitorRC", 0L);
		resisterRLToInductorRL = new SysMLFlowConnector(resistorRL.n, inductorRL.p, "ResistorRLToInductorRL", 0L);
		inductorRLToGround = new SysMLFlowConnector(inductorRL.n, ground.p, "InductorRLToGround", 0L);
		capacitorRCToGround = new SysMLFlowConnector(capacitorRC.n, ground.p, "CapacitorRCToGround", 0L);
		voltageSourceToGround = new SysMLFlowConnector(voltageSource.n, ground.p, "VoltageSourceToGround", 0L);
	}

	@Override
	protected void createBindingConnectors()
	{
		// Create binding connectors for voltage source
		sAmpBindingConnector = new SysMLBindingConnector(voltageSource.amplitude, voltageSourceConstraint, ParamsEnum.amp.toString());
		sIBindingConnector = new SysMLBindingConnector(voltageSource.iThru, voltageSourceConstraint, ParamsEnum.i.toString());
		sPosIBindingConnector = new SysMLBindingConnector(voltageSource.p.cF.i, voltageSourceConstraint, ParamsEnum.posI.toString());
		sNegIBindingConnector = new SysMLBindingConnector(voltageSource.n.cF.i, voltageSourceConstraint, ParamsEnum.negI.toString());
		sVBindingConnector = new SysMLBindingConnector(voltageSource.vDrop, voltageSourceConstraint, ParamsEnum.v.toString());
		sPosVBindingConnector = new SysMLBindingConnector(voltageSource.p.cF.v, voltageSourceConstraint, ParamsEnum.posV.toString());
		sNegVBindingConnector = new SysMLBindingConnector(voltageSource.n.cF.v, voltageSourceConstraint, ParamsEnum.negV.toString());
		sTimeBindingConnector = new SysMLBindingConnector(voltageSource.time, voltageSourceConstraint, ParamsEnum.time.toString());

		// Create binding connectors for resistor in inductance circuit
		rlRBindingConnector = new SysMLBindingConnector(resistorRL.resistance, resistorRLConstraint, ParamsEnum.rl.toString());
		rlIBindingConnector = new SysMLBindingConnector(resistorRL.iThru, resistorRLConstraint, ParamsEnum.i.toString());
		rlPosIBindingConnector = new SysMLBindingConnector(resistorRL.p.cF.i, resistorRLConstraint, ParamsEnum.posI.toString());
		rlNegIBindingConnector = new SysMLBindingConnector(resistorRL.n.cF.i, resistorRLConstraint, ParamsEnum.negI.toString());
		rlVBindingConnector = new SysMLBindingConnector(resistorRL.vDrop, resistorRLConstraint, ParamsEnum.v.toString());
		rlPosVBindingConnector = new SysMLBindingConnector(resistorRL.p.cF.v, resistorRLConstraint, ParamsEnum.posV.toString());
		rlNegVBindingConnector = new SysMLBindingConnector(resistorRL.n.cF.v, resistorRLConstraint, ParamsEnum.negV.toString());
		rlTimeBindingConnector = new SysMLBindingConnector(resistorRL.time, resistorRLConstraint, ParamsEnum.time.toString());

		// Create binding connectors for resistor in capacitance circuit
		rcRBindingConnector = new SysMLBindingConnector(resistorRC.resistance, resistorRCConstraint, ParamsEnum.rc.toString());
		rcIBindingConnector = new SysMLBindingConnector(resistorRC.iThru, resistorRCConstraint, ParamsEnum.i.toString());
		rcPosIBindingConnector = new SysMLBindingConnector(resistorRC.p.cF.i, resistorRCConstraint, ParamsEnum.posI.toString());
		rcNegIBindingConnector = new SysMLBindingConnector(resistorRC.n.cF.i, resistorRCConstraint, ParamsEnum.negI.toString());
		rcVBindingConnector = new SysMLBindingConnector(resistorRC.vDrop, resistorRCConstraint, ParamsEnum.v.toString());
		rcPosVBindingConnector = new SysMLBindingConnector(resistorRC.p.cF.v, resistorRCConstraint, ParamsEnum.posV.toString());
		rcNegVBindingConnector = new SysMLBindingConnector(resistorRC.n.cF.v, resistorRCConstraint, ParamsEnum.negV.toString());
		rcTimeBindingConnector = new SysMLBindingConnector(resistorRC.time, resistorRCConstraint, ParamsEnum.time.toString());

		// Create binding connectors for capacitor in capacitance circuit
		cCBindingConnector = new SysMLBindingConnector(capacitorRC.capacitance, capacitorRCConstraint, ParamsEnum.c.toString());
		cIBindingConnector = new SysMLBindingConnector(capacitorRC.iThru, capacitorRCConstraint, ParamsEnum.i.toString());
		cPosIBindingConnector = new SysMLBindingConnector(capacitorRC.p.cF.i, capacitorRCConstraint, ParamsEnum.posI.toString());
		cNegIBindingConnector = new SysMLBindingConnector(capacitorRC.n.cF.i, capacitorRCConstraint, ParamsEnum.negI.toString());
		cVBindingConnector = new SysMLBindingConnector(capacitorRC.vDrop, capacitorRCConstraint, ParamsEnum.v.toString());
		cPosVBindingConnector = new SysMLBindingConnector(capacitorRC.p.cF.v, capacitorRCConstraint, ParamsEnum.posV.toString());
		cNegVBindingConnector = new SysMLBindingConnector(capacitorRC.n.cF.v, capacitorRCConstraint, ParamsEnum.negV.toString());
		cTimeBindingConnector = new SysMLBindingConnector(capacitorRC.time, capacitorRCConstraint, ParamsEnum.time.toString());

		// Create binding connectors for inductor in inductance circuit
		iLBindingConnector = new SysMLBindingConnector(inductorRL.inductance, inductorRLConstraint, ParamsEnum.l.toString());
		iIBindingConnector = new SysMLBindingConnector(inductorRL.iThru, inductorRLConstraint, ParamsEnum.i.toString());
		iPosIBindingConnector = new SysMLBindingConnector(inductorRL.p.cF.i, inductorRLConstraint, ParamsEnum.posI.toString());
		iNegIBindingConnector = new SysMLBindingConnector(inductorRL.n.cF.i, inductorRLConstraint, ParamsEnum.negI.toString());
		iVBindingConnector = new SysMLBindingConnector(inductorRL.vDrop, inductorRLConstraint, ParamsEnum.v.toString());
		iPosVBindingConnector = new SysMLBindingConnector(inductorRL.p.cF.v, inductorRLConstraint, ParamsEnum.posV.toString());
		iNegVBindingConnector = new SysMLBindingConnector(inductorRL.n.cF.v, inductorRLConstraint, ParamsEnum.negV.toString());
		iTimeBindingConnector = new SysMLBindingConnector(inductorRL.time, inductorRLConstraint, ParamsEnum.time.toString());

		// Create binding connector for ground
		gPosVBindingConnector = new SysMLBindingConnector(ground.p.cF.v, groundConstraint, ParamsEnum.posV.toString());

		// Add the connectors to the binding connectors map (used by
		// ParametricAnalysisCase to update parameter values)
		voltageSourceConstraint.paramConnectors.put(ParamsEnum.amp.toString(), sAmpBindingConnector);
		voltageSourceConstraint.paramConnectors.put(ParamsEnum.i.toString(), sIBindingConnector);
		voltageSourceConstraint.paramConnectors.put(ParamsEnum.negI.toString(), sNegIBindingConnector);
		voltageSourceConstraint.paramConnectors.put(ParamsEnum.posI.toString(), sPosIBindingConnector);
		voltageSourceConstraint.paramConnectors.put(ParamsEnum.v.toString(), sVBindingConnector);
		voltageSourceConstraint.paramConnectors.put(ParamsEnum.negV.toString(), sNegVBindingConnector);
		voltageSourceConstraint.paramConnectors.put(ParamsEnum.posV.toString(), sPosVBindingConnector);
		voltageSourceConstraint.paramConnectors.put(ParamsEnum.time.toString(), sTimeBindingConnector);
		resistorRLConstraint.paramConnectors.put(ParamsEnum.rl.toString(), rlRBindingConnector);
		resistorRLConstraint.paramConnectors.put(ParamsEnum.i.toString(), rlIBindingConnector);
		resistorRLConstraint.paramConnectors.put(ParamsEnum.negI.toString(), rlNegIBindingConnector);
		resistorRLConstraint.paramConnectors.put(ParamsEnum.posI.toString(), rlPosIBindingConnector);
		resistorRLConstraint.paramConnectors.put(ParamsEnum.v.toString(), rlVBindingConnector);
		resistorRLConstraint.paramConnectors.put(ParamsEnum.negV.toString(), rlNegVBindingConnector);
		resistorRLConstraint.paramConnectors.put(ParamsEnum.posV.toString(), rlPosVBindingConnector);
		resistorRLConstraint.paramConnectors.put(ParamsEnum.time.toString(), rlTimeBindingConnector);
		resistorRCConstraint.paramConnectors.put(ParamsEnum.rc.toString(), rcRBindingConnector);
		resistorRCConstraint.paramConnectors.put(ParamsEnum.i.toString(), rcIBindingConnector);
		resistorRCConstraint.paramConnectors.put(ParamsEnum.negI.toString(), rcNegIBindingConnector);
		resistorRCConstraint.paramConnectors.put(ParamsEnum.posI.toString(), rcPosIBindingConnector);
		resistorRCConstraint.paramConnectors.put(ParamsEnum.v.toString(), rcVBindingConnector);
		resistorRCConstraint.paramConnectors.put(ParamsEnum.negV.toString(), rcNegVBindingConnector);
		resistorRCConstraint.paramConnectors.put(ParamsEnum.posV.toString(), rcPosVBindingConnector);
		resistorRCConstraint.paramConnectors.put(ParamsEnum.time.toString(), rcTimeBindingConnector);
		capacitorRCConstraint.paramConnectors.put(ParamsEnum.c.toString(), cCBindingConnector);
		capacitorRCConstraint.paramConnectors.put(ParamsEnum.i.toString(), cIBindingConnector);
		capacitorRCConstraint.paramConnectors.put(ParamsEnum.negI.toString(), cNegIBindingConnector);
		capacitorRCConstraint.paramConnectors.put(ParamsEnum.posI.toString(), cPosIBindingConnector);
		capacitorRCConstraint.paramConnectors.put(ParamsEnum.v.toString(), cVBindingConnector);
		capacitorRCConstraint.paramConnectors.put(ParamsEnum.negV.toString(), cNegVBindingConnector);
		capacitorRCConstraint.paramConnectors.put(ParamsEnum.posV.toString(), cPosVBindingConnector);
		capacitorRCConstraint.paramConnectors.put(ParamsEnum.time.toString(), cTimeBindingConnector);
		inductorRLConstraint.paramConnectors.put(ParamsEnum.l.toString(), iLBindingConnector);
		inductorRLConstraint.paramConnectors.put(ParamsEnum.i.toString(), iIBindingConnector);
		inductorRLConstraint.paramConnectors.put(ParamsEnum.negI.toString(), iNegIBindingConnector);
		inductorRLConstraint.paramConnectors.put(ParamsEnum.posI.toString(), iPosIBindingConnector);
		inductorRLConstraint.paramConnectors.put(ParamsEnum.v.toString(), iVBindingConnector);
		inductorRLConstraint.paramConnectors.put(ParamsEnum.negV.toString(), iNegVBindingConnector);
		inductorRLConstraint.paramConnectors.put(ParamsEnum.posV.toString(), iPosVBindingConnector);
		inductorRLConstraint.paramConnectors.put(ParamsEnum.time.toString(), iTimeBindingConnector);
		groundConstraint.paramConnectors.put(ParamsEnum.g.toString(), gPosVBindingConnector);
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
	 * @see sysmlinjava.views.common.Axis
	 * @see sysmlinjava.views.linechart.LineChartDefinition
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
	 * @see sysmlinjava.views.common.Axis
	 * @see sysmlinjava.views.linechart.LineChartDefinition
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
	 * @see sysmlinjava.views.common.Axis
	 * @see sysmlinjava.views.linechart.LineChartDefinition
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
	 * @see sysmlinjava.views.common.Axis
	 * @see sysmlinjava.views.linechart.LineChartDefinition
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
	 * @see sysmlinjava.attributetypes.Point2D
	 * @see sysmlinjava.views.linechart.LineChartData
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
	 * @see sysmlinjava.attributetypes.Point2D
	 * @see sysmlinjava.views.linechart.LineChartData
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
	 * @see sysmlinjava.attributetypes.Point2D
	 * @see sysmlinjava.views.linechart.LineChartData
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
	 * @see sysmlinjava.attributetypes.Point2D
	 * @see sysmlinjava.views.linechart.LineChartData
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
