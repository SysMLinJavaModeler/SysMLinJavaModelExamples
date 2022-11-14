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
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.InductanceHenrys;

/**
 * {@code InductorConstraint} is the SysMLinJava model of a constraint block for
 * the {@code Inductor} component of the {@code ElectricCircuit} system. The
 * model is a SysMLinJava implementation of the constraint block described in
 * "SysML Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018. The {@code InductorConstraint} is an extension
 * of the {@code BinaryElectricalComponentConstraint} with a specialized
 * constraint parameter for the inductance value of the capacitor.
 * <p>
 * The {@code InductorConstraint} model includes a constraint for the voltage
 * across the inductor which is the standard equation for the capacitor, i.e.
 * {@code L * di/dt = v}. This constraint of the {@code InductorConstraint}
 * block is used to validate/verify the voltage source model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.InductorConstraint
 * 
 * @author ModelerOne
 *
 */
public class InductorConstraint extends BinaryElectricalComponentConstraint
{
	/**
	 * Constraint parameter for the inductance of the inductor
	 */
	@ConstraintParameter
	InductanceHenrys l;

	/**
	 * Value for the previous current value used to estimate the derivative of the
	 * current flow.
	 */
	@Value
	Current iPrevious;

	/**
	 * Constraint for the inductor
	 */
	@Constraint
	SysMLConstraint lConstraint;

	/**
	 * Function that "binds" the value of inductance in the inductor to the
	 * constrain parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction lPortFunction;

	/**
	 * Port that executes the function that "binds" the value of inductance in the
	 * inductor to the constrain parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort lPort;

	/**
	 * Constructor
	 * 
	 * @param name unique name for the inductor
	 */
	public InductorConstraint(String name)
	{
		super(Optional.empty(), name);
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		iPrevious = new Current(0);
	}

	@Override
	protected void onParameterChange(String paramID)
	{
		super.onParameterChange(paramID);
		ParamsEnum paramEnum = ParamsEnum.valueOf(paramID);
		if (paramEnum != null)
			switch (paramEnum)
			{
			case l:
				l.value = ((InductanceHenrys)constraintParams.get(paramID)).value;
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
			lConstraint.apply();
			iPrevious.value = i.value;
		}
	}

	@Override
	protected void createConstraints()
	{
		super.createConstraints();
		lConstraint = () ->
		{
			double di = i.value - iPrevious.value;
			assert l.value * di == v.value : "l * di != v";
		};
	}

	@Override
	protected void createConstraintParameters()
	{
		super.createConstraintParameters();
		l = new InductanceHenrys(0);
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		super.createConstraintParameterPortFunctions();
		lPortFunction = (constraintParameterPort, contextBlock) ->
		{
			InductanceHenrys parameter = ((InductorRL)contextBlock).inductance;
			constraintParameterPort.updateParameterValue(new InductanceHenrys(parameter));
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		super.createConstraintParameterPorts();
		lPort = new SysMLConstraintParameterPort(this, lPortFunction, ParamsEnum.l.toString());

		paramPorts.put(ParamsEnum.l.toString(), lPort);
	}

}
