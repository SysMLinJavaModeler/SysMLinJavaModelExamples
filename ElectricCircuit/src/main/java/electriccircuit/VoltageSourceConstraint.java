package electriccircuit;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import java.util.Optional;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.DurationSeconds;

/**
 * {@code VoltageSourceConstraint} is the SysMLinJava model of a constraint
 * block for the {@code VoltageSource} component of the {@code ElectricCircuit}
 * system. The model is a SysMLinJava implementation of the constraint block
 * described in "SysML Extension for Physical Interaction and Signal Flow
 * Simulation", Object Management Group, Inc., 2018. The
 * {@code VoltageSourceConstraint} is an extension of the
 * {@code BinaryElectricalComponentConstraint} with a specialized constraint
 * parameter for the voltage source value of it input voltage to the circuit.
 * <p>
 * The {@code VoltageSourceConstraint} model includes a constraint for the
 * voltage input to the circuit which a simple sinusoidal wave form, i.e.
 * {@code vin = amp * sin(2 * PI * time)}. The {@code VoltageSourceConstraint}
 * block is used to validate/verify the voltage source model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.VoltageSourceConstraint
 * 
 * @author ModelerOne
 *
 */
public class VoltageSourceConstraint extends BinaryElectricalComponentConstraint
{
	/**
	 * Constraint parameter for the amplitude voltage of the voltage source
	 */
	@ConstraintParameter
	Voltage amp;

	/**
	 * Constraint for the amplitude voltage of the voltage source
	 */
	@Constraint
	SysMLConstraint ampConstraint;

	/**
	 * Function that "binds" the value of the amplitude voltage of the voltage
	 * source to the constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction ampPortFunction;

	/**
	 * Port that executes the function that "binds" the value of the amplitude
	 * voltage of the voltage source to the constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort ampPort;

	/**
	 * Constructor
	 * @param name unique name for the voltage source
	 */
	public VoltageSourceConstraint(String name)
	{
		super(Optional.empty(), name);
	}

	@Override
	protected void onParameterChange(String paramID)
	{
		super.onParameterChange(paramID);
		ParamsEnum paramEnum = ParamsEnum.valueOf(paramID);
		if (paramEnum != null)
			switch (paramEnum)
			{
			case amp:
				amp.value = ((Voltage)constraintParams.get(paramID)).value;
				break;
			case i:
				i.value = ((Current)constraintParams.get(paramID)).value;
				break;
			case negI:
				negI.value = ((Current)constraintParams.get(paramID)).value;
				break;
			case negV:
				negV.value = ((Voltage)constraintParams.get(paramID)).value;
				break;
			case posI:
				posI.value = ((Current)constraintParams.get(paramID)).value;
				break;
			case posV:
				posV.value = ((Voltage)constraintParams.get(paramID)).value;
				break;
			case time:
				time.value = ((DurationSeconds)constraintParams.get(paramID)).value;
				break;
			case v:
				v.value = ((Voltage)constraintParams.get(paramID)).value;
				break;
			default:
				logger.severe("unrecognized parameter enum for constraint parameters: " + paramEnum);
				break;
			}
		else
			logger.severe("paramID has no mapping to any constraint parameter: " + paramID);
	}

	@Override
	protected void performConstraints()
	{
		// Constraints are applied only if/when the "last" parameter (time)
		// is received which is when all parameters have been updated to their
		// next/updated value.
		if (currentParamID.isPresent() && currentParamID.get().equals(ParamsEnum.time.toString()))
		{
			super.performConstraints();
			ampConstraint.apply();
		}
	}

	@Override
	protected void createConstraints()
	{
		super.createConstraints();
		ampConstraint = () ->
		{
			assert v.value == amp.value * sin(2.0 * PI * time.value) : "v != amp * sin(2 * PI * time";
		};
	}

	@Override
	protected void createConstraintParameters()
	{
		super.createConstraintParameters();
		amp = new Voltage(0);
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		super.createConstraintParameterPortFunctions();
		ampPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Voltage parameter = ((VoltageSource)contextBlock).amplitude;
			constraintParameterPort.updateParameterValue(new Voltage(parameter));
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		super.createConstraintParameterPorts();
		ampPort = new SysMLConstraintParameterPort(this, ampPortFunction, ParamsEnum.amp.toString());

		paramPorts.put(ParamsEnum.amp.toString(), ampPort);
	}
}
