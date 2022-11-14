package motorinwheel.components.motor;

import java.util.Optional;
import motorinwheel.common.events.ElectricPowerEvent;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.PowerWatts;

/**
 * State machine for the Electric Motor model/simulation. The state machine is a
 * specialization of the {@code SingleStateMachine} consisting mainly of
 * internal state transitions for events on the power supply's interface, i.e.
 * changes to the electrical power supplied to the motor by the power supply.
 * See the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class ElectricMotorStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnPowerTransition;

	@EffectActivity
	public SysMLEffectActivity operationalOnPowerTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnPowerTransitionEffect;

	public ElectricMotorStateMachine(ElectricMotor motor)
	{
		super(motor, false, "ElectricMotorStateMachine");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		this.operationalOnPowerTransitionEffectActivity = (event, contextBlock) ->
		{
			ElectricMotor motor = (ElectricMotor)contextBlock.get();
			PowerWatts power = ((ElectricPowerEvent)event.get()).getPower();
			motor.onElectricalPower(power);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnPowerTransitionEffect = new SysMLEffect(contextBlock, operationalOnPowerTransitionEffectActivity, "OperationalOnPowerTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnPowerTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(ElectricPowerEvent.class), Optional.empty(), Optional.of(operationalOnPowerTransitionEffect), "OperationalOnPower",
			SysMLTransitionKind.internal);
	}
}
