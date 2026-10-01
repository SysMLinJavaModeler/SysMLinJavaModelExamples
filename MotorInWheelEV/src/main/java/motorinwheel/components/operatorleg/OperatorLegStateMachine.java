package motorinwheel.components.operatorleg;

import java.util.Optional;

import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

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

	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnTimerTransitionEffectActivity;

	/**
	 * 
	 */
	@Effect
	public SysMLEffect operationalOnTimerTransitionEffect;

	/**
	 * @param operator
	 */
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
	protected void createEffectActionFunctions()
	{
		operationalOnTimerTransitionEffectActivity = (event, contextPart) ->
		{
			SysMLTimeEvent timeEvent = (SysMLTimeEvent)event.get();
			if (timeEvent.timerID.equals(OperatorLeg.timerID))
			{
				OperatorLeg operatorLeg = (OperatorLeg)contextPart.get();
				operatorLeg.onSpeedUpdateTime();
			}
			else
				logger.warning("unexpected timerID: " + timeEvent.timerID);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnTimerTransitionEffect = new SysMLEffect(context, operationalOnTimerTransitionEffectActivity, "OperationalOnTimerTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnTimerTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLTimeEvent.class), Optional.empty(), Optional.of(operationalOnTimerTransitionEffect), "OperationalOnTimer",
			SysMLTransitionKind.internal);
	}
}
