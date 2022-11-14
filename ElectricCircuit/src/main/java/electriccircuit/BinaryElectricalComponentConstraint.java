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
 * {@code BinaryElectricalComponentConstraint} is the SysMLinJava model of a
 * constraint block common to the constraint blocks for the components of the
 * {@code ElectricCircuit} system. The model is a SysMLinJava implementation of
 * the constraint block described in "SysML Extension for Physical Interaction
 * and Signal Flow Simulation", Object Management Group, Inc., 2018. The
 * {@code BinaryElectricalComponentConstraint} is a {@code SysMLConstraintBlock}
 * characterized by its constraint parameters, which are the values of the
 * {@code TwoPinElectricalComponent}.
 * <p>
 * The {@code BinaryElectricalComponentConstraint} model iincludes a series of
 * constraints which are declared in the constraint block. The
 * {@code BinaryElectricalComponentConstraint} block is used to validate/verify
 * the capacitor model's execution.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @see electriccircuit.BinaryElectricalComponentConstraint
 * 
 * @author ModelerOne
 *
 */
public class BinaryElectricalComponentConstraint extends SysMLConstraintBlock
{
	/**
	 * Constraint parameter for the current through the component
	 */
	@ConstraintParameter
	public Current i;
	/**
	 * Constraint parameter for the current through the negative pin of the component
	 */
	@ConstraintParameter
	public Current negI;
	/**
	 * Constraint parameter for the current through the positive pin of the component
	 */
	@ConstraintParameter
	public Current posI;
	/**
	 * Constraint parameter for the voltage across the component
	 */
	@ConstraintParameter
	public Voltage v;
	/**
	 * Constraint parameter for the voltage at the negative pin of the component
	 */
	@ConstraintParameter
	public Voltage negV;
	/**
	 * Constraint parameter for the voltage at the posative pin of the component
	 */
	@ConstraintParameter
	public Voltage posV;
	/**
	 * Constraint parameter for the time of the current values of the other constraint parameters
	 */
	@ConstraintParameter
	public DurationSeconds time;

	/**
	 * Function that 'binds" the i constraint parameter to its component value
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction iPortFunction;
	/**
	 * Function that 'binds" the negI constraint parameter to its component value
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction negIPortFunction;
	/**
	 * Function that 'binds" the posI constraint parameter to its component value
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction posIPortFunction;
	/**
	 * Function that 'binds" the v constraint parameter to its component value
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction vPortFunction;
	/**
	 * Function that 'binds" the negV constraint parameter to its component value
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction negVPortFunction;
	/**
	 * Function that 'binds" the posV constraint parameter to its component value
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction posVPortFunction;
	/**
	 * Function that 'binds" the time constraint parameter to its component value
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction timePortFunction;

	/**
	 * Port that executes the function that "binds" the {@code i}constraint parameter to its component value
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort iPort;
	/**
	 * Port that executes the function that "binds" the {@code negI}constraint parameter to its component value
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort negIPort;
	/**
	 * Port that executes the function that "binds" the {@code posI}constraint parameter to its component value
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort posIPort;
	/**
	 * Port that executes the function that "binds" the {@code v}constraint parameter to its component value
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort vPort;
	/**
	 * Port that executes the function that "binds" the {@code negV}constraint parameter to its component value
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort negVPort;
	/**
	 * Port that executes the function that "binds" the {@code posV}constraint parameter to its component value
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort posVPort;
	/**
	 * Port that executes the function that "binds" the {@code time}constraint parameter to its component value
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort timePort;

	/**
	 * Constructor
	 * @param parent parent constraint block, if any, to this constraint block
	 * @param name unique name of the constraint block
	 */
	public BinaryElectricalComponentConstraint(Optional<? extends SysMLConstraintBlock> parent, String name)
	{
		super(parent, name);
	}

	@Override
	protected void performConstraints()
	{
		constraint.apply();
	}

	/**
	 * State machine not used for this constraint block as this is a fully
	 * synchronous model, i.e. all electronic components operate sequentially in
	 * same execution thread. Default for {@code SysMLConstraintBlock} is
	 * asynchronous execution in multi-threaded model, so need to override this
	 * operation to NOT create state machine.
	 */
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
			assert posI.value + negI.value == 0 : "posI + negI != 0";
			assert posV.value - negV.value == v.value : "posV - negV != 0";
			assert i == posI : "i != posI";
		};
	}

	@Override
	protected void createConstraintParameters()
	{
		i = new Current(0);
		negI = new Current(0);
		posI = new Current(0);
		v = new Voltage(0);
		negV = new Voltage(0);
		posV = new Voltage(0);
		time = new DurationSeconds(0);
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		iPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Current parameter = ((TwoPinElectricalComponent)contextBlock).iThru;
			constraintParameterPort.updateParameterValue(new Current(parameter));
		};
		negIPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Current parameter = ((TwoPinElectricalComponent)contextBlock).n.cF.i;
			constraintParameterPort.updateParameterValue(new Current(parameter));
		};
		posIPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Current parameter = ((TwoPinElectricalComponent)contextBlock).p.cF.i;
			constraintParameterPort.updateParameterValue(new Current(parameter));
		};
		vPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Voltage parameter = ((TwoPinElectricalComponent)contextBlock).vDrop;
			constraintParameterPort.updateParameterValue(new Voltage(parameter));
		};
		negVPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Voltage parameter = ((TwoPinElectricalComponent)contextBlock).n.cF.v;
			constraintParameterPort.updateParameterValue(new Voltage(parameter));
		};
		posVPortFunction = (constraintParameterPort, contextBlock) ->
		{
			Voltage parameter = ((TwoPinElectricalComponent)contextBlock).p.cF.v;
			constraintParameterPort.updateParameterValue(new Voltage(parameter));
		};
		timePortFunction = (constraintParameterPort, contextBlock) ->
		{
			DurationSeconds parameter = ((TwoPinElectricalComponent)contextBlock).time;
			constraintParameterPort.updateParameterValue(new DurationSeconds(parameter));
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		iPort = new SysMLConstraintParameterPort(this, iPortFunction, ParamsEnum.i.toString());
		negIPort = new SysMLConstraintParameterPort(this, negIPortFunction, ParamsEnum.negI.toString());
		posIPort = new SysMLConstraintParameterPort(this, posIPortFunction, ParamsEnum.posI.toString());
		vPort = new SysMLConstraintParameterPort(this, vPortFunction, ParamsEnum.v.toString());
		negVPort = new SysMLConstraintParameterPort(this, negVPortFunction, ParamsEnum.negV.toString());
		posVPort = new SysMLConstraintParameterPort(this, posVPortFunction, ParamsEnum.posV.toString());
		timePort = new SysMLConstraintParameterPort(this, timePortFunction, ParamsEnum.time.toString());

		paramPorts.put(ParamsEnum.i.toString(), iPort);
		paramPorts.put(ParamsEnum.negI.toString(), negIPort);
		paramPorts.put(ParamsEnum.posI.toString(), posIPort);
		paramPorts.put(ParamsEnum.v.toString(), vPort);
		paramPorts.put(ParamsEnum.negV.toString(), negVPort);
		paramPorts.put(ParamsEnum.posV.toString(), posVPort);
		paramPorts.put(ParamsEnum.time.toString(), timePort);
	}
}
