package motorinwheel.components.operatordisplays;

import java.util.Optional;
import motorinwheel.common.events.ElectronicPulseFrequencyEvent;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.FrequencyHertz;

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

	@EffectActivity
	public SysMLEffectActivity operationalOnWheelPulseFrequencyTransitionEffectActivity;

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
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		operationalOnWheelPulseFrequencyTransitionEffectActivity = (event, contextBlock) ->
		{
			OperatorDisplays operatorInterface = (OperatorDisplays)contextBlock.get();
			FrequencyHertz wheelPulseFrequency = ((ElectronicPulseFrequencyEvent)event.get()).getFrequency();
			WheelLocationEnum wheelLocation = WheelLocationEnum.valueOf(((ElectronicPulseFrequencyEvent)event.get()).getID().intValue());
			operatorInterface.onWheelPulseFrequency(wheelPulseFrequency, wheelLocation);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnWheelPulseFrequencyTransitionEffect = new SysMLEffect(contextBlock, operationalOnWheelPulseFrequencyTransitionEffectActivity, "OperationalOnWheelPulseFrequencyTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnWheelPulseFrequencyTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(ElectronicPulseFrequencyEvent.class), Optional.empty(),
			Optional.of(operationalOnWheelPulseFrequencyTransitionEffect), "OperationalOnWheelPulseFrequency", SysMLTransitionKind.internal);
	}
}
