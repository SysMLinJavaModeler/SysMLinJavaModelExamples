package motorinwheel.systems.roadway;

import java.util.Optional;
import motorinwheel.common.signals.MechanicalForceSignal;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.attributetypes.ForceNewtons;
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

	@GuardCondition
	private SysMLGuardCondition isVehicleForceGuardCondition;
	
	@Guard
	private SysMLGuard isVehicleForceGuard;
	
	@Transition
	public SysMLTransition operationalOnEventTransition;

	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnVehicleForcesTransitionEffectActivity;
	
	@Effect
	public SysMLEffect operationalOnVehicleForcesTransitionEffect;

	public RoadwayStateMachine(Roadway contextPart)
	{
		super(contextPart, true, "RoadwayStateMachine");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isVehicleForceGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof MechanicalForceSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isVehicleForceGuard = new SysMLGuard(context, isVehicleForceGuardCondition, "isVehicleForce");
	}
	
	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		this.operationalOnVehicleForcesTransitionEffectActivity = (event, contextPart) ->
		{
			Roadway roadway = (Roadway)contextPart.get();
			ForceNewtons weight = ((MechanicalForceSignal)((SysMLSignalEvent)event.get()).signal).force;
			WheelLocationEnum wheelLocation = WheelLocationEnum.valueOf(((MechanicalForceSignal)((SysMLSignalEvent)event.get()).signal).id.intValue());
			roadway.onVehicleForces(weight, wheelLocation);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		this.operationalOnVehicleForcesTransitionEffect = new SysMLEffect(context, operationalOnVehicleForcesTransitionEffectActivity, "OperationalOnRoadwayForceTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnVehicleForcesTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isVehicleForceGuard), Optional.of(operationalOnVehicleForcesTransitionEffect),
			"OperationalOnRoadwayForce", SysMLTransitionKind.internal);
	}
}
