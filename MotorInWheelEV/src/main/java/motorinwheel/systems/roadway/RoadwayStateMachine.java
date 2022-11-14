package motorinwheel.systems.roadway;

import java.util.Optional;
import motorinwheel.common.events.MechanicalForceEvent;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.ForceNewtons;

/**
 * State machine for the Roadway model/simulation. The state machine is a
 * specialization of the {@code SingleStateMachine} consisting mainly of
 * internal state transitions for events on the roadway's interfaces, i.e.
 * changes to the mechanical forces of the vehicle's wheels on the roadway. See
 * the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class RoadwayStateMachine extends SingleStateStateMachine
{
	@Transition
	public SysMLTransition operationalOnVehicleForcesTransition;
	@EffectActivity
	public SysMLEffectActivity operationalOnVehicleForcesTransitionEffectActivity;
	@Effect
	public SysMLEffect operationalOnVehicleForcesTransitionEffect;

	public RoadwayStateMachine(Roadway contextBlock)
	{
		super(contextBlock, true, "RoadwayStateMachine");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		this.operationalOnVehicleForcesTransitionEffectActivity = (event, contextBlock) ->
		{
			Roadway roadway = (Roadway)contextBlock.get();
			ForceNewtons weight = ((MechanicalForceEvent)event.get()).getForce();
			WheelLocationEnum wheelLocation = WheelLocationEnum.valueOf(((MechanicalForceEvent)event.get()).getID().intValue());
			roadway.onVehicleForces(weight, wheelLocation);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		this.operationalOnVehicleForcesTransitionEffect = new SysMLEffect(contextBlock, operationalOnVehicleForcesTransitionEffectActivity, "OperationalOnRoadwayForceTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnVehicleForcesTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(MechanicalForceEvent.class), Optional.empty(), Optional.of(operationalOnVehicleForcesTransitionEffect),
			"OperationalOnRoadwayForce", SysMLTransitionKind.internal);
	}
}
