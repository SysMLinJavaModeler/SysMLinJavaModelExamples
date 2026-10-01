package dbssystem.controller;

import java.util.List;
import java.util.Optional;

import dbssystem.actuators.DBSActuator;
import dbssystem.sensors.PulseSensor;
import dbssystem.sensors.TremorSensor;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.neuralnetdisplay.NeuralNetDisplay;
import sysmlinjava.views.neuralnetdisplay.NeuralNetDisplayDefinition;
import sysmlinjava.views.neuralnetdisplay.NeuralNetDisplayDefinition.NeuronDisplayDefinition;
import sysmlinjava.views.neuralnetdisplay.NeuralNetInputDataSet;
import sysmlinjava.views.neuralnetdisplay.NeuralNetOutputDataSet;
import sysmlinjava.views.neuralnetdisplay.NeuralNetworkAnalysisCase;

/**
 * Anaysis case for the Deep-brain stimulation (DBS) artificial neural network
 * (ANN) of the DBSController
 */
public class DBSANNAnalysisCase extends NeuralNetworkAnalysisCase
{
	/**
	 * Unique ID for the display
	 */
	public static final String displayID = "NeuralNetDisplay";
	/**
	 * Format string for values
	 */
	public static final String format = "%5.2f";
	/**
	 * Definition of the input neurons display
	 */
	public static final List<NeuronDisplayDefinition> inputNeuronDefs = List.of(new NeuronDisplayDefinition("Frequency High", format), new NeuronDisplayDefinition("Frequency Med ",
	format), new NeuronDisplayDefinition("Frequency Low ", format), new NeuronDisplayDefinition("Frequency Off ", format), new NeuronDisplayDefinition("Amplitude High",
	format), new NeuronDisplayDefinition("Amplitude Low ", format), new NeuronDisplayDefinition("Pulse     High", format), new NeuronDisplayDefinition("Pulse     Low ", format));
	/**
	 * Definition of the output neurons display
	 */
	public static final List<NeuronDisplayDefinition> outputNeuronDefs = List.of(new NeuronDisplayDefinition("Frequency  High ", format), new NeuronDisplayDefinition(
	"Frequency  Med  ", format), new NeuronDisplayDefinition("Frequency  Low  ", format), new NeuronDisplayDefinition("Frequency  Off  ", format), new NeuronDisplayDefinition(
	"PhaseShift Large", format), new NeuronDisplayDefinition("PhaseShift Small", format));

	/**
	 * Whether the input data has been updated
	 */
	@Attribute
	boolean inputUpdated;
	
	/**
	 * Whether the output data has been updated
	 */
	@Attribute
	boolean outputUpdated;

	/**
	 * Name ID for the neural net input data
	 */
	public static final String inputData = "inputs";
	/**
	 * Name ID for the neural net output data
	 */
	public static final String outputData = "outputs";

	/**
	 * Constructor
	 * 
	 * @param controller DBSController that is the subject of this analysis
	 */
	public DBSANNAnalysisCase(DBSController controller)
	{
		super(Optional.empty(), true);
	}
		
	@Override
	public void perform()
	{
		if(inputUpdated && outputUpdated)
		{
			super.perform();
			inputUpdated = outputUpdated = false;
		}
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		switch (paramID)
		{
		case inputData:
			if (paramValue instanceof NeuralNetInputDataSet inputDataSet)
			{
				neuralNetInputParam.setValue(inputDataSet);
				inputUpdated = true;
			}
			else
				logger.warning("unexpected type of bound parameter for id: " + paramID + ", i.e. not a " + NeuralNetInputDataSet.class.getSimpleName());
			break;
		case outputData:
			if (paramValue instanceof NeuralNetOutputDataSet outputDataSet)
			{
				neuralNetOutputParam.setValue(outputDataSet);
				outputUpdated = true;
			}
			else
				logger.warning("unexpected type of bound parameter for id: " + paramID + ", i.e. not a " + NeuralNetOutputDataSet.class.getSimpleName());
			break;
		default:
			logger.warning("unexpected name ID of bound parameter: " + paramID);
		}
	}

	@Override
	protected void createAttributes()
	{
		numberInputNeurons = new IInteger(inputNeuronDefs.size());
		numberOutputNeurons = new IInteger(outputNeuronDefs.size());
		displayDefinition = new NeuralNetDisplayDefinition(displayID, inputNeuronDefs, outputNeuronDefs, NeuralNetDisplay.udpPort);
		inputUpdated = false;
		outputUpdated = false;
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(DBSController.class);
	}

	@Override
	protected void createObjective()
	{
		objective = DBSControllerAnalysisRequirements.dbsControllerAnalysisObjective;
	}

	@Override
	protected void createActors()
	{
		actors = List.of(PulseSensor.class, TremorSensor.class, DBSActuator.class);
	}
}
