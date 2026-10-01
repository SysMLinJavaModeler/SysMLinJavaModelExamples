package motorinwheel.components.acceleration;

import java.util.Optional;
import motorinwheel.common.signals.AcceleratorPedalForceSignal;
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
 * State machine for the {@code Accelerator} model/simulation. The state machine
 * is a specialization of the {@code SingleStateMachine} consisting mainly of a
 * single internal state transition for the event of an update to the value of
 * the force being exerted on the accelerator pedal. See the model of the state
 * machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class AcceleratorStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnAcceleratorPedalForceTransition;

	@GuardCondition
	public SysMLGuardCondition isAcceleratorPedalForceGuardCondition;
	
	@Guard
	public SysMLGuard isAcceleratorPedalForceGuard;
	
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnAcceleratorPedalForceTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnAcceleratorPedalForceTransitionEffect;

	public AcceleratorStateMachine(Accelerator operatorInterface)
	{
		super(operatorInterface, false, "AccelerationSystemStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
	}

	@Override
	protected void createGuardConditions()
	{
		isAcceleratorPedalForceGuardCondition = (event, contextPart) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent &&
				((SysMLSignalEvent)event.get()).signal instanceof AcceleratorPedalForceSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isAcceleratorPedalForceGuard = new SysMLGuard(context, isAcceleratorPedalForceGuardCondition, "isAcceleratorPedalForce");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnAcceleratorPedalForceTransitionEffectActivity = (event, contextPart) ->
		{
			Accelerator accelerator = (Accelerator)contextPart.get();
			ForceNewtons force = ((AcceleratorPedalForceSignal)((SysMLSignalEvent)event.get()).signal).force;
			accelerator.onAcceleratorPedal(force);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnAcceleratorPedalForceTransitionEffect = new SysMLEffect(context, operationalOnAcceleratorPedalForceTransitionEffectActivity, "OperationalOnAcceleratorPedalForceTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnAcceleratorPedalForceTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isAcceleratorPedalForceGuard),
			Optional.of(operationalOnAcceleratorPedalForceTransitionEffect), "OperationalOnAcceleratorPedalForce", SysMLTransitionKind.internal);
	}
}
