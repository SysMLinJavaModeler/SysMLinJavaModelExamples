package motorinwheel.components.deceleration;

import java.util.Optional;
import motorinwheel.common.events.BrakePedalForceEvent;
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

	@EffectActivity
	public SysMLEffectActivity operationalOnBrakePedalForceTransitionEffectActivity;

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
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		operationalOnBrakePedalForceTransitionEffectActivity = (event, contextBlock) ->
		{
			Decelerator decelerator = (Decelerator)contextBlock.get();
			ForceNewtons force = ((BrakePedalForceEvent)event.get()).getForce();
			decelerator.onBrakePedal(force);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnBrakePedalForceTransitionEffect = new SysMLEffect(contextBlock, operationalOnBrakePedalForceTransitionEffectActivity, "OperationalOnBrakePedalForceTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnBrakePedalForceTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(BrakePedalForceEvent.class), Optional.empty(), Optional.of(operationalOnBrakePedalForceTransitionEffect),
			"OperationalOnBrakePedalForce", SysMLTransitionKind.internal);
	}
}
