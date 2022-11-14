package motorinwheel.components.brake;

import java.util.Optional;
import motorinwheel.common.events.HydraulicPressureEvent;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.ForceNewtonsPerMeterSquare;

/**
 * State machine for the {@code MechanicalBrake} model/simulation. The state
 * machine is a specialization of the {@code SingleStateMachine} consisting
 * mainly of a single internal state transition for the event of an update to
 * the value of the force being exerted on the braking mechanism. See the model
 * of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class MechanicalBrakeStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnEventTransition;

	@EffectActivity
	public SysMLEffectActivity operationalOnEventTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnEventTransitionEffect;

	public MechanicalBrakeStateMachine(MechanicalBrake contextBlock)
	{
		super(contextBlock, false, "MechanicalBrakeStateMachine");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		this.operationalOnEventTransitionEffectActivity = (event, contextBlock) ->
		{
			MechanicalBrake brake = (MechanicalBrake)contextBlock.get();
			ForceNewtonsPerMeterSquare pressure = ((HydraulicPressureEvent)event.get()).getForce();
			brake.onHydraulicPressureChange(pressure);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		this.operationalOnEventTransitionEffect = new SysMLEffect(contextBlock, operationalOnEventTransitionEffectActivity, "operationalOnHydraulicsTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnEventTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(HydraulicPressureEvent.class), Optional.empty(), Optional.of(operationalOnEventTransitionEffect),
			"OperationalOnHydraulics", SysMLTransitionKind.internal);
	}
}
