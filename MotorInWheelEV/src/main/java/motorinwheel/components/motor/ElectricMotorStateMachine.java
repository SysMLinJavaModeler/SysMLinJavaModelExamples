package motorinwheel.components.motor;

import java.util.Optional;
import motorinwheel.common.signals.ElectricalPowerSignal;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

/**
 * State machine for the Electric Motor model/simulation. The state machine is a
 * specialization of the {@code SingleStateMachine} consisting mainly of
 * internal state transitions for events on the power supply's interface, i.e.
 * changes to the electrical power supplied to the motor by the power supply.
 * See the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class ElectricMotorStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnPowerTransition;

	@GuardCondition
	private SysMLGuardCondition isElectricalPowerGuardCondition;
	
	@Guard
	private SysMLGuard isElectricalPowerGuard;
	
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnPowerTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnPowerTransitionEffect;

	public ElectricMotorStateMachine(ElectricMotor motor)
	{
		super(motor, false, "ElectricMotorStateMachine");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isElectricalPowerGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof ElectricalPowerSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isElectricalPowerGuard = new SysMLGuard(context, isElectricalPowerGuardCondition, "isElectricalPower");
	}
	
	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		this.operationalOnPowerTransitionEffectActivity = (event, contextPart) ->
		{
			ElectricMotor motor = (ElectricMotor)contextPart.get();
			PowerWatts power = ((ElectricalPowerSignal)((SysMLSignalEvent)event.get()).signal).power;
			motor.onElectricalPower(power);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnPowerTransitionEffect = new SysMLEffect(context, operationalOnPowerTransitionEffectActivity, "OperationalOnPowerTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnPowerTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isElectricalPowerGuard), Optional.of(operationalOnPowerTransitionEffect), "OperationalOnPower",
			SysMLTransitionKind.internal);
	}
}
