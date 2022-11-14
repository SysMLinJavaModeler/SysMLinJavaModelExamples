package motorinwheel.systems.atmosphere;

import java.util.Optional;
import motorinwheel.common.events.FrontalAreaSpeedEvent;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.FrontalArealSpeed;

/**
 * State machine for the Atmosphere model/simulation. The state machine is a
 * specialization of the {@code SingleStateMachine} consisting mainly of
 * internal state transitions for events on the atmosphere's interface, i.e.
 * changes to the frontal areal speed moving through the atmosphere. See the
 * model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class AtmosphereStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnAreaFrontalSpeedTransition;

	@EffectActivity
	public SysMLEffectActivity operationalOnAreaFrontalSpeedTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnAreaFrontalSpeedTransitionEffect;

	public AtmosphereStateMachine(Atmosphere contextBlock)
	{
		super(contextBlock, false, "AtmosphereStateMachine");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		operationalOnAreaFrontalSpeedTransitionEffectActivity = (event, contextBlock) ->
		{
			Atmosphere atmosphere = (Atmosphere)contextBlock.get();
			FrontalArealSpeed vehicleForces = ((FrontalAreaSpeedEvent)event.get()).getAreaSpeed();
			atmosphere.onFrontalArea(vehicleForces);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnAreaFrontalSpeedTransitionEffect = new SysMLEffect(contextBlock, operationalOnAreaFrontalSpeedTransitionEffectActivity, "OperationalOnAreaFrontalSpeedTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnAreaFrontalSpeedTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(FrontalAreaSpeedEvent.class), Optional.empty(), Optional.of(operationalOnAreaFrontalSpeedTransitionEffect),
			"OperationalOnAreaFrontalSpeed", SysMLTransitionKind.internal);
	}
}
