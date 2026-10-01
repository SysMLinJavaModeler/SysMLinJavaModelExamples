package dbssystem.controller;

import java.util.List;
import java.util.Optional;

import dbssystem.common.DBSControl;
import dbssystem.common.PulseValue;
import dbssystem.common.TremorLevel;
import dbssystem.controller.DBSANN.FreqAmpPulse;
import dbssystem.controller.DBSANN.FreqPhase;
import dbssystem.controller.DBSANN.FrequencyEnum;
import dbssystem.controller.DBSANN.PulseEnum;
import dbssystem.controller.DBSANN.TremorAmplitudeEnum;
import sysmlinjava.attributetypes.DistanceMillimeters;
import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.InternetAddress;
import sysmlinjava.attributetypes.PhaseShiftRadians;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.BindingConnector;
import sysmlinjava.javaannotations.items.Item;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.neuralnetdisplay.NeuralNetDisplayDefinition.NeuronDisplayDefinition;
import sysmlinjava.views.neuralnetdisplay.NeuralNetInputDataSet;
import sysmlinjava.views.neuralnetdisplay.NeuralNetOutputDataSet;

/**
 * DBSController is a SysMLinJava model of a controller that uses a patient's
 * tremor level and pulse to determine a control value for a deep-brain
 * stimulator signal to be injected into the patient's brain to reduce the
 * tremor. It receives tremor and pulse signals from sensors on the patient and
 * performs an AI-based constraint to determine the signal to be injected into
 * the patient's brain by an embedded DBS actuator.
 * <p>
 * Note the AI used in this model is artificially simple to enable
 * straightforward demonstration of the modeling and simulation of the DBS
 * system using SysMLinJava.
 * 
 * @author ModelerOne
 */
public class DBSController extends SysMLPart
{
	/**
	 * Neural network that generates AI-based controls for patient's DBS
	 */
	@Part
	DBSANN neuralNet;

	/**
	 * Input data to the DBSANN
	 */
	@Item
	NeuralNetInputDataSet neuralNetInputDataSet;

	/**
	 * Output data from the DBSANN
	 */
	@Item
	NeuralNetOutputDataSet neuralNetOutputDataSet;

	/**
	 * Port for the input of tremor level from the patient
	 */
	@Port
	public TremorLevelInPort tremorInPort;
	/**
	 * Port for the input of pulse values from the patient
	 */
	@Port
	public PulseValueInPort pulseInPort;
	/**
	 * Port for the output of DBS actuator controls to the actuator in the patient's
	 * brain
	 */
	@Port
	public DBSControlOutPort controlOutPort;

	/**
	 * Value of the current tremor level of the patient
	 */
	@Attribute
	public TremorLevel currentTremor;
	/**
	 * Value of the current pulse of the patient
	 */
	@Attribute
	public PulseValue currentPulseRate;
	/**
	 * Value of the current frequency of the DBS signal being transmitted to the
	 * patient's brain
	 */
	@Attribute
	public FrequencyHertz currentDBSFrequency;
	/**
	 * Value of the current pahse shift (relative to patient's tremor) of the DBS
	 * signal being transmitted to the patient's brain
	 */
	@Attribute
	public PhaseShiftRadians currentDBSPhaseShift;
	/**
	 * Value of the current frequency of the patient's tremor
	 */
	@Attribute
	public FrequencyHertz currentTremorFrequency;
	/**
	 * Value of the current amplitude of the patient's tremor
	 */
	@Attribute
	public DistanceMillimeters currentTremorAmplitude;
	/**
	 * Value of the current control being sent of the DBS actuator in the patient's
	 * brain.
	 */
	@Attribute
	public DBSControl currentControl;
	/**
	 * Value of the IP address to be used by the ports to receive tremor and pulse
	 * values
	 */
	@Attribute
	public InternetAddress ipAddress;
	/**
	 * Value of the UDP port to be used by the port to receive tremor level values
	 */
	@Attribute
	public IInteger tremorLevelUDPPort;
	/**
	 * Value of the UDP port to be used by the port to receive pulse values
	 */
	@Attribute
	public IInteger pulseValueUDPPort;

	/**
	 * Parametric analysis that determines the DBS signal to be output to the
	 * patient from the tremor and pulse values received from the patient
	 */
	@ParametricAnalysis
	public DBSANNAnalysisCase dbsANNAnalysisCase;

	/**
	 * Connector that binds the neural net input data set to the DBS analysis case's
	 * parameter.
	 */
	@BindingConnector
	public SysMLBindingConnector neuralNetInputDataSetBindingConnector;
	/**
	 * Connector that binds the neural net output data set to the DBS analysis
	 * case's parameter.
	 */
	@BindingConnector
	public SysMLBindingConnector neuralNetOutputDataSetBindingConnector;

	/**
	 * Constructor
	 */
	public DBSController()
	{
		super("DBSController", 0L);
	}

	/**
	 * Starts the controller, i.e. starts the state machine (to start event
	 * handling), the analysis case (to start the neural net display), and the two
	 * input ports to receive the patient's tremor level and pulse rate.
	 */
	@Override
	public void start()
	{
		super.start();
		dbsANNAnalysisCase.start();
		tremorInPort.start();
		pulseInPort.start();
	}

	/**
	 * Stops the controller
	 */
	@Override
	public void stop()
	{
		tremorInPort.stop();
		pulseInPort.stop();
		dbsANNAnalysisCase.stop();
		super.stop();
	}

	/**
	 * Event handler for the receipt of a new tremor level value
	 * 
	 * @param level new tremor level value
	 */
	@Action
	public void onTremorLevel(TremorLevel level)
	{
		logger.info(level.toString());
		currentTremor.setValue(level);
		FrequencyEnum freq = FrequencyEnum.valueOf(currentTremor.frequency.value);
		TremorAmplitudeEnum amp = TremorAmplitudeEnum.valueOf(currentTremor.amplitude.value);
		PulseEnum pulse = PulseEnum.valueOf(currentPulseRate.value);
		FreqAmpPulse fap = new FreqAmpPulse(freq, amp, pulse);
		
		neuralNetInputDataSet.setValue(fap.toANNInput());
		neuralNetOutputDataSet.setValue(neuralNet.calculate(neuralNetInputDataSet));

		FreqPhase freqPhase = FreqPhase.fromANNOutput(neuralNetOutputDataSet.outputValues);
		FrequencyHertz controlFrequency = new FrequencyHertz(freqPhase.frequency.hertz);
		PhaseShiftRadians controlPhaseShift = new PhaseShiftRadians(freqPhase.phaseShift.radians);
		currentControl.setValue(controlFrequency, controlPhaseShift);
		controlOutPort.transmit(currentControl); // TODO need to transmit new copy?
	}

	/**
	 * Event handler for the receipt of a new pulse value
	 * 
	 * @param value new pulse value
	 */
	@Action
	public void onPulseValue(PulseValue value)
	{
		logger.info(value.toString());
		currentPulseRate.setValue(value);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new DBSControllerStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		currentDBSFrequency = new FrequencyHertz(0);
		currentDBSPhaseShift = new PhaseShiftRadians(0);
		currentTremorFrequency = new FrequencyHertz(0);
		currentTremorAmplitude = new DistanceMillimeters(0);
		currentTremor = new TremorLevel(new FrequencyHertz(5.0), new DistanceMillimeters(12.0));
		currentPulseRate = new PulseValue(IInteger.of(0));
		currentControl = new DBSControl(currentDBSFrequency, currentDBSPhaseShift);

		ipAddress = InternetAddress.ofLocalHost();
		tremorLevelUDPPort = new IInteger(8701);
		pulseValueUDPPort = new IInteger(8702);
	}

	@Override
	protected void createItems()
	{
		neuralNetInputDataSet = new NeuralNetInputDataSet(null);
		neuralNetOutputDataSet = new NeuralNetOutputDataSet(null);
	}

	@Override
	protected void createParts()
	{
		neuralNet = new DBSANN();
	}

	@Override
	protected void createPorts()
	{
		tremorInPort = new TremorLevelInPort(this, ipAddress.toInetAddress(), tremorLevelUDPPort.toInteger());
		pulseInPort = new PulseValueInPort(this, ipAddress.toInetAddress(), pulseValueUDPPort.toInteger());
		controlOutPort = new DBSControlOutPort(this, 0L, "DBSControlOutPort");
	}

	@Override
	protected void createAnalysisCases()
	{
		dbsANNAnalysisCase = new DBSANNAnalysisCase(this);
	}

	@Override
	protected void createBindingConnectors()
	{
		neuralNetInputDataSetBindingConnector = new SysMLBindingConnector(this.neuralNetInputDataSet, dbsANNAnalysisCase, DBSANNAnalysisCase.inputData);
		neuralNetOutputDataSetBindingConnector = new SysMLBindingConnector(this.neuralNetOutputDataSet, dbsANNAnalysisCase, DBSANNAnalysisCase.outputData);

		dbsANNAnalysisCase.paramConnectors.put(DBSANNAnalysisCase.inputData, neuralNetInputDataSetBindingConnector);
		dbsANNAnalysisCase.paramConnectors.put(DBSANNAnalysisCase.outputData, neuralNetOutputDataSetBindingConnector);
	}

	/**
	 * ID of the neural net display definition to be used to display the inputs and
	 * outputs of the controllers AI-base constraint block
	 */
	public static final String displayID = "NeuralNetDisplay";
	/**
	 * String format of the input and output values to be displayed by the neural
	 * net inputs/outputs display
	 */
	public static final String format = "%5.2f";
	/**
	 * List of the neuron definitions for the input values to be displayed by the
	 * neural net inputs/outputs display
	 */
	public static final List<NeuronDisplayDefinition> inputNeuronDefs = List.of(new NeuronDisplayDefinition("Frequency High", format), new NeuronDisplayDefinition("Frequency Med ",
	format), new NeuronDisplayDefinition("Frequency Low ", format), new NeuronDisplayDefinition("Frequency Off ", format), new NeuronDisplayDefinition("Amplitude High",
	format), new NeuronDisplayDefinition("Amplitude Low ", format), new NeuronDisplayDefinition("Pulse     High", format), new NeuronDisplayDefinition("Pulse     Low ", format));
	/**
	 * List of the neuron definitions for the output values to be displayed by the
	 * neural net inputs/outputs display
	 */
	public static final List<NeuronDisplayDefinition> outputNeuronDefs = List.of(new NeuronDisplayDefinition("Frequency  High ", format), new NeuronDisplayDefinition(
	"Frequency  Med  ", format), new NeuronDisplayDefinition("Frequency  Low  ", format), new NeuronDisplayDefinition("Frequency  Off  ", format), new NeuronDisplayDefinition(
	"PhaseShift Large", format), new NeuronDisplayDefinition("PhaseShift Small", format));
}
