package motorinwheel.components.acceleration;

import java.util.Optional;
import motorinwheel.common.events.AcceleratorPedalForceEvent;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.ForceNewtons;

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

	@EffectActivity
	public SysMLEffectActivity operationalOnAcceleratorPedalForceTransitionEffectActivity;

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
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		operationalOnAcceleratorPedalForceTransitionEffectActivity = (event, contextBlock) ->
		{
			Accelerator accelerator = (Accelerator)contextBlock.get();
			ForceNewtons force = ((AcceleratorPedalForceEvent)event.get()).getForce();
			accelerator.onAcceleratorPedal(force);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnAcceleratorPedalForceTransitionEffect = new SysMLEffect(contextBlock, operationalOnAcceleratorPedalForceTransitionEffectActivity, "OperationalOnAcceleratorPedalForceTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnAcceleratorPedalForceTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(AcceleratorPedalForceEvent.class), Optional.empty(),
			Optional.of(operationalOnAcceleratorPedalForceTransitionEffect), "OperationalOnAcceleratorPedalForce", SysMLTransitionKind.internal);
	}
}
