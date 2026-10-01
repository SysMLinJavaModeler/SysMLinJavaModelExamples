package motorinwheel.components.deceleration;

import java.util.Optional;
import motorinwheel.common.signals.BrakePedalForceSignal;
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
 * State machine for the {@code Decelerator} model/simulation. The state machine
 * is a specialization of the {@code SingleStateMachine} consisting mainly of a
 * single internal state transition for the event of an update to the value of
 * the force being exerted on the brake pedal. See the model of the state
 * machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class DeceleratorStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnBrakePedalForceTransition;

	@GuardCondition
	private SysMLGuardCondition isBrakePedalForceGuardCondition;
	
	@Guard
	private SysMLGuard isBrakePedalForceGuard;
	
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnBrakePedalForceTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnBrakePedalForceTransitionEffect;

	public DeceleratorStateMachine(Decelerator decelerator)
	{
		super(decelerator, false, "DeceleratorStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isBrakePedalForceGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof BrakePedalForceSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isBrakePedalForceGuard = new SysMLGuard(context, isBrakePedalForceGuardCondition, "isBrakePedalForce");
	}
	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnBrakePedalForceTransitionEffectActivity = (event, contextPart) ->
		{
			Decelerator decelerator = (Decelerator)contextPart.get();
			ForceNewtons force = ((BrakePedalForceSignal)((SysMLSignalEvent)event.get()).signal).force;
			decelerator.onBrakePedal(force);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnBrakePedalForceTransitionEffect = new SysMLEffect(context, operationalOnBrakePedalForceTransitionEffectActivity, "OperationalOnBrakePedalForceTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnBrakePedalForceTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isBrakePedalForceGuard), Optional.of(operationalOnBrakePedalForceTransitionEffect),
			"OperationalOnBrakePedalForce", SysMLTransitionKind.internal);
	}
}
