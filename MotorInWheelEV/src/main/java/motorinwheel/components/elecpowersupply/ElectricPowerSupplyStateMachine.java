package motorinwheel.components.elecpowersupply;

import java.util.Optional;
import motorinwheel.common.signals.MechanicalForceSignal;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.attributetypes.ForceNewtons;
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
 * State machine for the ElectricPowerSupply model/simulation. The state machine
 * is a specialization of the {@code SingleStateMachine} consisting mainly of
 * internal state transitions for events on the power supply's interface, i.e.
 * changes to the mechanical force on the power supply's switch by the
 * accelerator. See the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class ElectricPowerSupplyStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnEventTransition;

	@GuardCondition
	private SysMLGuardCondition isMechanicalForceGuardCondition;
	
	@Guard
	private SysMLGuard isMechanicalForceGuard;
	
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnEventTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnEventTransitionEffect;

	public ElectricPowerSupplyStateMachine(ElectricPowerSupply contextPart)
	{
		super(contextPart, false, "ElectricPowerGeneratorStateMachine");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isMechanicalForceGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof MechanicalForceSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isMechanicalForceGuard = new SysMLGuard(context, isMechanicalForceGuardCondition, "isMechanicalForce");
	}
	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		this.operationalOnEventTransitionEffectActivity = (event, contextPart) ->
		{
			ElectricPowerSupply powerSupply = (ElectricPowerSupply)contextPart.get();
			ForceNewtons force = ((MechanicalForceSignal)((SysMLSignalEvent)event.get()).signal).force;
			powerSupply.onControl(force);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		this.operationalOnEventTransitionEffect = new SysMLEffect(context, operationalOnEventTransitionEffectActivity, "operationalOnPowerTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnEventTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isMechanicalForceGuard), Optional.of(operationalOnEventTransitionEffect), "OperationalOnPower",
			SysMLTransitionKind.internal);
	}
}
