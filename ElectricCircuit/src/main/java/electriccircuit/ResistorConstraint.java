package electriccircuit;

import java.util.Optional;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.ResistanceOhms;

/**
 * {@code ResistorConstraint} is the SysMLinJava model of a constraint block for
 * the {@code Resistor} component of the {@code ElectricCircuit} system. The
 * model is a SysMLinJava implementation of the constraint block described in
 * "SysML Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018. The {@code ResistorConstraint} is an extension
 * of the {@code BinaryElectricalComponentConstraint} with a specialized
 * constraint parameter for the resistance value of the resistor.
 * <p>
 * The {@code ResistorConstraint} model includes a constraint for the voltage
 * across the resistor which is the standard equation for the resistor, i.e.
 * {@code R * i = v}. This constraint of the {@code ResistorConstraint} block is
 * used to validate/verify the voltage source model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.ResistorConstraint
 * 
 * @author ModelerOne
 *
 */
public class ResistorConstraint extends BinaryElectricalComponentConstraint
{
	/**
	 * Constraint parameter for the resistance of the resistor
	 */
	@ConstraintParameter
	ResistanceOhms r;

	/**
	 * Constraint for the resistor
	 */
	@Constraint
	SysMLConstraint rConstraint;

	/**
	 * Function that "binds" the resistance value for the resistor to the constraint
	 * parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction rPortFunction;

	/**
	 * Port that executes the function that "binds" the resistance value for the
	 * resistor to the constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort rPort;

	/**
	 * Constructor
	 * 
	 * @param name unique name for the resistor
	 */
	public ResistorConstraint(String name)
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
			case rc, rl:
				r.value = ((ResistanceOhms)constraintParams.get(paramID)).value;
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
		if (currentParamID.isPresent() && currentParamID.get().equals(ParamsEnum.time.toString()))
		{
			super.performConstraints();
			rConstraint.apply();
		}
	}

	@Override
	protected void createConstraints()
	{
		super.createConstraints();
		rConstraint = () ->
		{
			assert r.value * i.value == v.value : "r * i != v";
		};
	}

	@Override
	protected void createConstraintParameters()
	{
		super.createConstraintParameters();
		r = new ResistanceOhms(0);
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		super.createConstraintParameterPortFunctions();
		rPortFunction = (constraintParameterPort, contextBlock) ->
		{
			ResistanceOhms parameter = ((Resistor)contextBlock).resistance;
			constraintParameterPort.updateParameterValue(new ResistanceOhms(parameter));
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		super.createConstraintParameterPorts();
		ParamsEnum resistorEnum = name.get().contains("RC") ? ParamsEnum.rc : ParamsEnum.rl;
		rPort = new SysMLConstraintParameterPort(this, rPortFunction, resistorEnum.toString());

		paramPorts.put(resistorEnum.toString(), rPort);
	}
}
