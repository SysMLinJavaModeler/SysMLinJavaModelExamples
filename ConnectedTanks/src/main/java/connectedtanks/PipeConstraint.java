package connectedtanks;

import java.util.Optional;
import static java.lang.Math.PI;
import static java.lang.Math.pow;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.constraintblocks.SysMLConstraintBlock;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.DistanceMeters;

/**
 * Constraint block for the parameteric analysis of the {@code Pipe} of the
 * {@code ConnectedTanks} system. {@code PipeConstraint} includes parameters for
 * dynamic and static values of the pipe and specifies a set of constraints on
 * the parameters as specified in the OMG document from which this example
 * SysMLinJava model was derived, i.e. "SysML Extension for Physical Interaction
 * and Signal Flow Simulation", Object Management Group, Inc., 2018.
 * 
 * @author ModelerOne
 *
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 */
public class PipeConstraint extends SysMLConstraintBlock
{
	/**
	 * Pressure at the opening to tank 1
	 */
	@ConstraintParameter
	Pressure opening1Pressure;
	/**
	 * Pressure at the opening to tank 2
	 */
	@ConstraintParameter
	Pressure opening2Pressure;
	/**
	 * Rate of flow of fluid through opening to tank 1
	 */
	@ConstraintParameter
	VolumeFlowRate opening1FluidFlow;
	/**
	 * Rate of flow of fluid through opening to tank 1
	 */
	@ConstraintParameter
	VolumeFlowRate opening2FluidFlow;
	/**
	 * Radius of pipe
	 */
	@ConstraintParameter
	DistanceMeters radius;
	/**
	 * Length of pipe
	 */
	@ConstraintParameter
	DistanceMeters length;
	/**
	 * Rate of flow of fluid through pipe
	 */
	@ConstraintParameter
	VolumeFlowRate fluidFlow;
	/**
	 * Pressure differential across pipe
	 */
	@ConstraintParameter
	Pressure pressureDiff;
	/**
	 * Viscosity of fluid in the pipe
	 */
	@ConstraintParameter
	Viscosity viscosity;
	/**
	 * Resistance of pipe to fluid
	 */
	@ConstraintParameter
	ViscousResistance resistance;

	/**
	 * Port that binds the pipes pressure at opening to tank 1 to constraint
	 * parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort opening1PressurePort;
	/**
	 * Port that binds the pipes pressure at opening to tank 2 to constraint
	 * parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort opening2PressurePort;
	/**
	 * Port that binds the pipes fluid flow at opening to tank 1 to constraint
	 * parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort opening1FluidFlowPort;
	/**
	 * Port that binds the pipes fluid flow at opening to tank 2 to constraint
	 * parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort opening2FluidFlowPort;
	/**
	 * Port that binds the pipes radius to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort radiusPort;
	/**
	 * Port that binds the pipes length to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort lengthPort;
	/**
	 * Port that binds the pipes fluid flow to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort fluidFlowPort;
	/**
	 * Port that binds the pipes pressure differential to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort pressureDiffPort;
	/**
	 * Port that binds the pipes viscosity to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort viscosityPort;
	/**
	 * Port that binds the pipes resistance to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort resistancePort;

	/**
	 * Function that performs the binding of the pipe's pressure at opening to tank
	 * 1 to constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction opening1PressurePortFunction;
	/**
	 * Function that performs the binding of the pipe's pressure at opening to tank
	 * 2 to constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction opening2PressurePortFunction;
	/**
	 * Function that performs the binding of the pipe's fluid flow at opening to
	 * tank 1 to constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction opening1FluidFlowPortFunction;
	/**
	 * Function that performs the binding of the pipe's fluid flow at opening to
	 * tank 2 to constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction opening2FluidFlowPortFunction;
	/**
	 * Function that performs the binding of the pipe's radius to constraint
	 * parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction radiusPortFunction;
	/**
	 * Function that performs the binding of the pipe's length to constraint
	 * parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction lengthPortFunction;
	/**
	 * Function that performs the binding of the pipe's fluid flow to constraint
	 * parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction fluidFlowPortFunction;
	/**
	 * Function that performs the binding of the pipe's pressure differential to
	 * constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction pressureDiffPortFunction;
	/**
	 * Function that performs the binding of the pipe's viscosity to constraint
	 * parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction viscosityPortFunction;
	/**
	 * Function that performs the binding of the pipe's resistance to constraint
	 * parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction resistancePortFunction;

	/**
	 * Enumeration of the constraint parameters
	 * 
	 * @author ModelerOne
	 *
	 */
	enum PipeParams
	{
		opening1Pressure, opening2Pressure, opening1FluidFlow, opening2FluidFlow, radius, length, fluidFlow, pressureDiff, viscosity, resistance
	};

	/**
	 * Constructor
	 */
	public PipeConstraint()
	{
		super(Optional.empty(), "PipeConstraint");
	}

	@Override
	protected void onParameterChange(String paramID)
	{
		if (!paramID.isEmpty())
		{
			previousParamID = currentParamID;
			currentParamID = Optional.of(paramID);
			SysMLConstraintParameterPort paramPort = paramPorts.get(currentParamID.get());
			if (paramPort != null)
			{
				if (currentParam != null)
					previousParam = currentParam;
				currentParam = paramPort.getValue();
				if (currentParam != null)
				{
					PipeParams paramEnum = PipeParams.valueOf(currentParamID.get());
					if (paramEnum != null)
						switch (paramEnum)
						{
						case fluidFlow:
							fluidFlow.value = ((VolumeFlowRate)currentParam).value;
							break;
						case length:
							length.value = ((DistanceMeters)currentParam).value;
							break;
						case opening1FluidFlow:
							opening1FluidFlow.value = ((VolumeFlowRate)currentParam).value;
							break;
						case opening1Pressure:
							opening1Pressure.value = ((Pressure)currentParam).value;
							break;
						case opening2FluidFlow:
							opening2FluidFlow.value = ((VolumeFlowRate)currentParam).value;
							break;
						case opening2Pressure:
							opening2Pressure.value = ((Pressure)currentParam).value;
							break;
						case pressureDiff:
							pressureDiff.value = ((Pressure)currentParam).value;
							break;
						case radius:
							radius.value = ((DistanceMeters)currentParam).value;
							break;
						case resistance:
							resistance.value = ((ViscousResistance)currentParam).value;
							break;
						case viscosity:
							viscosity.value = ((Viscosity)currentParam).value;
							break;
						default:
							break;
						}
					else
						logger.severe("unrecognized parameter ID for parameter to be retrieved: " + paramID);
				}
				else
					logger.severe("bound parameter value not retrieved from its bound parameter port: " + paramID);
			}
			else
				logger.severe("bound parameter port not found for parameter: " + paramID);
		}
		else
			currentParamID = Optional.empty();
	}

	@Override
	protected void performConstraints()
	{
		// Constraints are applied only if/when the "last" parameter (opening2FluidFlow)
		// is received which is when all parameters have been updated to their
		// next/updated value.
		if (currentParamID.isPresent() && currentParamID.get().equals(PipeParams.opening2FluidFlow.toString()))
		{
			constraint.apply();
			logger.info(String.format("pipe: pressureDiff=%4.4f fluidFlow=%2.5f", pressureDiff.value, fluidFlow.value));
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.empty();
	}

	@Override
	protected void createConstraints()
	{
		// Constraints as specified in "SysML Extension for Physical Interaction and
		// Signal Flow Simulation", Object Management Group, Inc., 2018
		constraint = () ->
		{
			assert resistance.value == (8 * viscosity.value * length.value) / (PI * pow(radius.value, 2.0)) : "resistance != (8 * viscosity * length) / (PI * pow(radius, 4.0))";
			assert pressureDiff.value == opening2Pressure.value - opening1Pressure.value;
			assert fluidFlow.value == pressureDiff.value / resistance.value;
			assert opening1FluidFlow.value == fluidFlow.value : "opening1FluidFlow != fluidFlow";
			assert opening2FluidFlow.value == opening1FluidFlow.value : "opening2FluidFlow != opening1FluidFlow";
		};
	}

	@Override
	protected void createConstraintParameters()
	{
		// All params initialized to zero when first created. Will be set to first
		// values for simulation by port constructors.
		opening1Pressure = new Pressure(0);
		opening2Pressure = new Pressure(0);
		opening1FluidFlow = new VolumeFlowRate(0);
		opening2FluidFlow = new VolumeFlowRate(0);
		radius = new DistanceMeters(0);
		length = new DistanceMeters(0);
		fluidFlow = new VolumeFlowRate(0);
		pressureDiff = new Pressure(0);
		viscosity = new Viscosity(0);
		resistance = new ViscousResistance(0);
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		opening1PressurePortFunction = (constraintParameterPort, contextBlock) ->
		{
			Pressure parameter = ((Pipe)contextBlock).pipeOpening1.vf.p;
			constraintParameterPort.updateParameterValue(new Pressure(parameter));
		};
		opening2PressurePortFunction = (constraintParameterPort, contextBlock) ->
		{
			Pressure parameter = ((Pipe)contextBlock).pipeOpening2.vf.p;
			constraintParameterPort.updateParameterValue(new Pressure(parameter));
		};
		opening1FluidFlowPortFunction = (constraintParameterPort, contextBlock) ->
		{
			VolumeFlowRate parameter = ((Pipe)contextBlock).pipeOpening1.vf.q;
			constraintParameterPort.updateParameterValue(new VolumeFlowRate(parameter));
		};
		opening2FluidFlowPortFunction = (constraintParameterPort, contextBlock) ->
		{
			VolumeFlowRate parameter = ((Pipe)contextBlock).pipeOpening2.vf.q;
			constraintParameterPort.updateParameterValue(new VolumeFlowRate(parameter));
		};
		radiusPortFunction = (constraintParameterPort, contextBlock) ->
		{
			DistanceMeters parameter = ((Pipe)contextBlock).pipeRadius;
			constraintParameterPort.updateParameterValue(new DistanceMeters(parameter));
		};
		lengthPortFunction = (constraintParameterPort, contextBlock) ->
		{
			DistanceMeters parameter = ((Pipe)contextBlock).pipeLength;
			constraintParameterPort.updateParameterValue(new DistanceMeters(parameter));
		};
		fluidFlowPortFunction = (constraintParameterPort, contextBlock) ->
		{
			VolumeFlowRate parameter = ((Pipe)contextBlock).fluidFlow;
			constraintParameterPort.updateParameterValue(new VolumeFlowRate(parameter));
		};
		pressureDiffPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Pressure parameter = ((Pipe)contextBlock).fluidPressureDiff;
			constraintParameterPort.updateParameterValue(new Pressure(parameter));
		};
		viscosityPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Viscosity parameter = ((Pipe)contextBlock).dynamicViscosity;
			constraintParameterPort.updateParameterValue(new Viscosity(parameter));
		};
		resistancePortFunction = (constraintParameterPort, contextBlock) ->
		{
			ViscousResistance parameter = ((Pipe)contextBlock).resistance;
			constraintParameterPort.updateParameterValue(new ViscousResistance(parameter));
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		opening1PressurePort = new SysMLConstraintParameterPort(this, opening1PressurePortFunction, PipeParams.opening1Pressure.toString());
		opening2PressurePort = new SysMLConstraintParameterPort(this, opening2PressurePortFunction, PipeParams.opening2Pressure.toString());
		opening1FluidFlowPort = new SysMLConstraintParameterPort(this, opening1FluidFlowPortFunction, PipeParams.opening1FluidFlow.toString());
		opening2FluidFlowPort = new SysMLConstraintParameterPort(this, opening2FluidFlowPortFunction, PipeParams.opening2FluidFlow.toString());
		radiusPort = new SysMLConstraintParameterPort(this, radiusPortFunction, PipeParams.radius.toString());
		lengthPort = new SysMLConstraintParameterPort(this, lengthPortFunction, PipeParams.length.toString());
		fluidFlowPort = new SysMLConstraintParameterPort(this, fluidFlowPortFunction, PipeParams.fluidFlow.toString());
		pressureDiffPort = new SysMLConstraintParameterPort(this, pressureDiffPortFunction, PipeParams.pressureDiff.toString());
		viscosityPort = new SysMLConstraintParameterPort(this, viscosityPortFunction, PipeParams.viscosity.toString());
		resistancePort = new SysMLConstraintParameterPort(this, resistancePortFunction, PipeParams.resistance.toString());

		//Add each port to the map of ports for use by retrievParameters() operation
		paramPorts.put(PipeParams.opening1Pressure.toString(), opening1PressurePort);
		paramPorts.put(PipeParams.opening2Pressure.toString(), opening2PressurePort);
		paramPorts.put(PipeParams.opening1FluidFlow.toString(), opening1FluidFlowPort);
		paramPorts.put(PipeParams.opening2FluidFlow.toString(), opening2FluidFlowPort);
		paramPorts.put(PipeParams.radius.toString(), radiusPort);
		paramPorts.put(PipeParams.length.toString(), lengthPort);
		paramPorts.put(PipeParams.fluidFlow.toString(), fluidFlowPort);
		paramPorts.put(PipeParams.pressureDiff.toString(), pressureDiffPort);
		paramPorts.put(PipeParams.viscosity.toString(), viscosityPort);
		paramPorts.put(PipeParams.resistance.toString(), resistancePort);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PipeConstraint [name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append(", opening1Pressure=");
		builder.append(opening1Pressure);
		builder.append(", opening2Pressure=");
		builder.append(opening2Pressure);
		builder.append(", opening1FluidFlow=");
		builder.append(opening1FluidFlow);
		builder.append(", opening2FluidFlow=");
		builder.append(opening2FluidFlow);
		builder.append(", radius=");
		builder.append(radius);
		builder.append(", length=");
		builder.append(length);
		builder.append(", fluidFlow=");
		builder.append(fluidFlow);
		builder.append(", pressureDiff=");
		builder.append(pressureDiff);
		builder.append(", viscosity=");
		builder.append(viscosity);
		builder.append(", resistance=");
		builder.append(resistance);
		builder.append("]");
		return builder.toString();
	}
}