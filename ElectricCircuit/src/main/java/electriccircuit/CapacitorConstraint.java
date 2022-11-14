package electriccircuit;

import java.util.Optional;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.CapacitanceFarads;
import sysmlinjava.valuetypes.DurationSeconds;

/**
 * {@code CapacitorConstraint} is the SysMLinJava model of a constraint block
 * for the {@code Capacitor} component of the {@code ElectricCircuit} system.
 * The model is a SysMLinJava implementation of the constraint block described
 * in "SysML Extension for Physical Interaction and Signal Flow Simulation",
 * Object Management Group, Inc., 2018. The {@code CapacitorConstraint} is an
 * extension of the {@code BinaryElectricalComponentConstraint} with a
 * specialized constraint parameter for the capacitance value of the capacitor.
 * <p>
 * The {@code CapacitorConstraint} model includes a constraint for the current
 * flowing thru the capacitor which is the standard equation for the capacitor,
 * i.e. {@code C * dv/dt = i}. This constraint of the
 * {@code CapacitorConstraint} block is used to validate/verify the voltage
 * source model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.CapacitorConstraint
 * 
 * @author ModelerOne
 *
 */
public class CapacitorConstraint extends BinaryElectricalComponentConstraint
{
	/**
	 * Constraint parameter for the capacitance of the capacitor
	 */
	@ConstraintParameter
	CapacitanceFarads c;

	/**
	 * Value for previous voltage used to estimate the derivative of voltage across
	 * the capacitor
	 */
	@Value
	Voltage vPrevious;

	/**
	 * Constraint for the capacitor
	 */
	@Constraint
	SysMLConstraint cConstraint;

	/**
	 * Function that "binds" the value of capacitance in the capacitor to the
	 * constrain parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction cPortFunction;

	/**
	 * Port that executes the function that "binds" the value of capacitance in the
	 * capacitor to the constrain parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort cPort;

	/**
	 * Constructor
	 * 
	 * @param name unique name of the capacitor constraint block
	 */
	public CapacitorConstraint(String name)
	{
		super(Optional.empty(), name);
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		vPrevious = new Voltage(0);
	}

	@Override
	protected void onParameterChange(String paramID)
	{
		super.onParameterChange(paramID);
		ParamsEnum paramEnum = ParamsEnum.valueOf(paramID);
		if (paramEnum != null)
			switch (paramEnum)
			{
			case c:
				c.value = ((CapacitanceFarads)constraintParams.get(paramID)).value;
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
			cConstraint.apply();
			vPrevious.value = v.value;
		}
	}

	@Override
	protected void createConstraints()
	{
		super.createConstraints();
		cConstraint = () ->
		{
			double dv = v.value - vPrevious.value;
			assert c.value * dv == i.value : "c * dv/dt != i";
		};
	}

	@Override
	protected void createConstraintParameters()
	{
		super.createConstraintParameters();
		c = new CapacitanceFarads(0);
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		super.createConstraintParameterPortFunctions();
		cPortFunction = (constraintParameterPort, contextBlock) ->
		{
			CapacitanceFarads parameter = ((CapacitorRC)contextBlock).capacitance;
			constraintParameterPort.updateParameterValue(new CapacitanceFarads(parameter));
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		super.createConstraintParameterPorts();
		cPort = new SysMLConstraintParameterPort(this, cPortFunction, ParamsEnum.c.toString());

		paramPorts.put(ParamsEnum.c.toString(), cPort);
	}
}
