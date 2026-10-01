package motorinwheel.components.operatordisplays;

import java.util.Optional;
import motorinwheel.common.signals.ElectronicPulseFrequencySignal;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.attributetypes.FrequencyHertz;
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
 * State machine for the {@code OperatorDisplays} model/simulation. The state
 * machine is a specialization of the {@code SingleStateMachine} consisting
 * mainly of a single internal state transition for the event of the receipt of
 * new value of the wheel pulse frequency, i.e. a new speed reading for a wheel.
 * See the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class OperatorDisplaysStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnWheelPulseFrequencyTransition;

	@GuardCondition
	private SysMLGuardCondition isElectronicPulseFrequencyGuardCondition;
	
	@Guard
	private SysMLGuard isElectronicPulseFrequencyGuard;
	
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnWheelPulseFrequencyTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnWheelPulseFrequencyTransitionEffect;

	public OperatorDisplaysStateMachine(OperatorDisplays operatorDisplays)
	{
		super(operatorDisplays, true, "OperatorDisplaysStateMachine");
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
		isElectronicPulseFrequencyGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof ElectronicPulseFrequencySignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isElectronicPulseFrequencyGuard = new SysMLGuard(context, isElectronicPulseFrequencyGuardCondition, "isBrakeTorque");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnWheelPulseFrequencyTransitionEffectActivity = (event, contextPart) ->
		{
			OperatorDisplays operatorInterface = (OperatorDisplays)contextPart.get();
			FrequencyHertz wheelPulseFrequency = ((ElectronicPulseFrequencySignal)((SysMLSignalEvent)event.get()).signal).frequency;
			WheelLocationEnum wheelLocation = WheelLocationEnum.valueOf(((ElectronicPulseFrequencySignal)((SysMLSignalEvent)event.get()).signal).id.intValue());
			operatorInterface.onWheelPulseFrequency(wheelPulseFrequency, wheelLocation);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnWheelPulseFrequencyTransitionEffect = new SysMLEffect(context, operationalOnWheelPulseFrequencyTransitionEffectActivity, "OperationalOnWheelPulseFrequencyTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnWheelPulseFrequencyTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isElectronicPulseFrequencyGuard),
			Optional.of(operationalOnWheelPulseFrequencyTransitionEffect), "OperationalOnWheelPulseFrequency", SysMLTransitionKind.internal);
	}
}
