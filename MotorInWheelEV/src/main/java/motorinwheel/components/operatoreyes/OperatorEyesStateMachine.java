package motorinwheel.components.operatoreyes;

import java.util.Optional;
import motorinwheel.common.signals.SpeedValueDisplaySignal;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.attributetypes.SpeedKilometersPerHour;
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

	@GuardCondition
	private SysMLGuardCondition isSpeedValueDisplayGuardCondition;
	
	@Guard
	private SysMLGuard isSpeedValueDisplayGuard;
	
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnSpeedometerUpdateTransitionEffectActivity;

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
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isSpeedValueDisplayGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof SpeedValueDisplaySignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isSpeedValueDisplayGuard = new SysMLGuard(context, isSpeedValueDisplayGuardCondition, "isSpeedValueDisplay");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnSpeedometerUpdateTransitionEffectActivity = (event, contextPart) ->
		{
			OperatorEyes operator = (OperatorEyes)contextPart.get();
			SpeedKilometersPerHour speed = ((SpeedValueDisplaySignal)((SysMLSignalEvent)event.get()).signal).speed;
			operator.onSpeedometerView(speed);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnSpeedometerUpdateTransitionEffect = new SysMLEffect(context, operationalOnSpeedometerUpdateTransitionEffectActivity, "OperationalOnSpeedometerUpdateTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnSpeedometerUpdateTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isSpeedValueDisplayGuard),
			Optional.of(operationalOnSpeedometerUpdateTransitionEffect), "OperationalOnSpeedometerUpdate", SysMLTransitionKind.internal);
	}
}
