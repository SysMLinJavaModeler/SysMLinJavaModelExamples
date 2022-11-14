package motorinwheel.components.elecpowersupply;

import java.util.Optional;
import motorinwheel.common.events.MechanicalForceEvent;
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
 * State machine for the ElectricPowerSupply model/simulation. The state machine
 * is a specialization of the {@code SingleStateMachine} consisting mainly of
 * internal state transitions for events on the power supply's interface, i.e.
 * changes to the mechanical force on the power supply's switch by the
 * accelerator. See the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class ElectricPowerSupplyStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnEventTransition;

	@EffectActivity
	public SysMLEffectActivity operationalOnEventTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnEventTransitionEffect;

	public ElectricPowerSupplyStateMachine(ElectricPowerSupply contextBlock)
	{
		super(contextBlock, false, "ElectricPowerGeneratorStateMachine");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		this.operationalOnEventTransitionEffectActivity = (event, contextBlock) ->
		{
			ElectricPowerSupply powerSupply = (ElectricPowerSupply)contextBlock.get();
			ForceNewtons force = ((MechanicalForceEvent)event.get()).getForce();
			powerSupply.onControl(force);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		this.operationalOnEventTransitionEffect = new SysMLEffect(contextBlock, operationalOnEventTransitionEffectActivity, "operationalOnPowerTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnEventTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(MechanicalForceEvent.class), Optional.empty(), Optional.of(operationalOnEventTransitionEffect), "OperationalOnPower",
			SysMLTransitionKind.internal);
	}
}
