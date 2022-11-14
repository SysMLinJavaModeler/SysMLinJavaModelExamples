package motorinwheel.components.operatoreyes;

import java.util.Optional;
import motorinwheel.common.events.SpeedValueDisplayEvent;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;

/**
 * State machine for the {@code OperatorEyes} model/simulation. The state
 * machine is a specialization of the {@code SingleStateMachine} consisting
 * mainly of a single internal state transition for the event of an update to
 * the value of the speed on the speedometer. See the model of the state machine
 * below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class OperatorEyesStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnSpeedometerUpdateTransition;

	@EffectActivity
	public SysMLEffectActivity operationalOnSpeedometerUpdateTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnSpeedometerUpdateTransitionEffect;

	public OperatorEyesStateMachine(OperatorEyes operator)
	{
		super(operator, true, "OperatorEyesStateMachine");
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
		operationalOnSpeedometerUpdateTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorEyes operator = (OperatorEyes)contextBlock.get();
			SpeedKilometersPerHour speed = ((SpeedValueDisplayEvent)event.get()).getSpeed();
			operator.onSpeedometerView(speed);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnSpeedometerUpdateTransitionEffect = new SysMLEffect(contextBlock, operationalOnSpeedometerUpdateTransitionEffectActivity, "OperationalOnSpeedometerUpdateTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnSpeedometerUpdateTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(SpeedValueDisplayEvent.class), Optional.empty(),
			Optional.of(operationalOnSpeedometerUpdateTransitionEffect), "OperationalOnSpeedometerUpdate", SysMLTransitionKind.internal);
	}
}
