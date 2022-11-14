package motorinwheel.components.operatorleg;

import java.util.Optional;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;

/**
 * State machine for the {@code OperatorLeg} model/simulation. The state machine
 * is a specialization of the {@code SingleStateMachine} consisting mainly of a
 * single internal state transition for the event of a time to change the force
 * on the accelerator. See the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class OperatorLegStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnTimerTransition;

	@EffectActivity
	public SysMLEffectActivity operationalOnTimerTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnTimerTransitionEffect;

	public OperatorLegStateMachine(OperatorLeg operator)
	{
		super(operator, true, "OperatorLegStateMachine");
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
		operationalOnTimerTransitionEffectActivity = (event, contextBlock) ->
		{
			SysMLTimeEvent timeEvent = (SysMLTimeEvent)event.get();
			if (timeEvent.timerID.equals("OperatorSpeedUpdateTimer"))
			{
				OperatorLeg operatorLeg = (OperatorLeg)contextBlock.get();
				operatorLeg.onSpeedUpdateTime();
			}
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnTimerTransitionEffect = new SysMLEffect(contextBlock, operationalOnTimerTransitionEffectActivity, "OperationalOnTimerTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnTimerTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(SysMLTimeEvent.class), Optional.empty(), Optional.of(operationalOnTimerTransitionEffect), "OperationalOnTimer",
			SysMLTransitionKind.internal);
	}
}
