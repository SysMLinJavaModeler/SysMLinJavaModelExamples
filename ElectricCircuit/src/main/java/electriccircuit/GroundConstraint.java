package electriccircuit;

import java.util.Optional;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.constraintblocks.SysMLConstraintBlock;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.DurationSeconds;

/**
 * {@code GroundConstraint} is the SysMLinJava model of a constraint block for
 * the {@code Ground} component of the {@code ElectricCircuit} system. The model
 * is a SysMLinJava implementation of the constraint block described in "SysML
 * Extension for Physical Interaction and Signal Flow Simulation", Object
 * Management Group, Inc., 2018. The {@code GroundConstraint} is an extension of
 * the {@code BinaryElectricalComponentConstraint} with a specialized constraint
 * parameter for the voltage on the positive pin for the ground.
 * <p>
 * The {@code GroundConstraint} model includes a constraint for the voltage of
 * the ground, i.e. {@code v = 0}. The {@code GroundConstraint} block is used to
 * validate/verify the voltage source model's execution.
 * <p>
 * Unlike the other components in the {@code ElectricCircuit} model, the
 * {@code Gound} component is not a "two pin" element, i.e. ground has only a
 * single interface/port for the zero voltage "sink". Consequently, the
 * {@code GroundConstraint} is unable to inherit the corresponding constraint
 * block for two pin compoents and must implement propeties that are unique to
 * the {@code Ground} component.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.GroundConstraint
 * 
 * @author ModelerOne
 *
 */
public class GroundConstraint extends SysMLConstraintBlock
{
	/**
	 * Constraint parameter for the ground voltage, the constraint for which asserts
	 * is always zero
	 */
	@ConstraintParameter
	public Voltage posV;
	/**
	 * Constraint parameter for the time of the current voltage
	 */
	@ConstraintParameter
	public DurationSeconds time;

	/**
	 * Function that "binds" the ground's voltage value to the constraint parameter
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction posVPortFunction;

	/**
	 * Port that executes the function that "binds" the ground's voltage value to
	 * the constraint parameter
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort posVPort;

	/**
	 * Constructor
	 * 
	 * @param name unique name of the constraint block
	 */
	public GroundConstraint(String name)
	{
		super(Optional.empty(), name);
	}

	@Override
	protected void performConstraints()
	{
		constraint.apply();
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.empty();
	}

	@Override
	protected void createConstraints()
	{
		constraint = () ->
		{
			assert posV.value == 0 : "posV != 0";
		};
	}

	@Override
	protected void createConstraintParameters()
	{
		posV = new Voltage(0);
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		posVPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Voltage parameter = ((Ground)contextBlock).p.cF.v;
			constraintParameterPort.updateParameterValue(new Voltage(parameter));
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		posVPort = new SysMLConstraintParameterPort(this, posVPortFunction, ParamsEnum.posV.toString());

		paramPorts.put(ParamsEnum.posV.toString(), posVPort);
	}
}
