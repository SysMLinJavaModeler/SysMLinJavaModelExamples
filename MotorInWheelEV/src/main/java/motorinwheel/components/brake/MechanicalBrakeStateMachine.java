package motorinwheel.components.brake;

import java.util.Optional;
import motorinwheel.common.signals.HydraulicPressureSignal;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.attributetypes.ForceNewtonsPerMeterSquare;
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
	@GuardCondition
	private SysMLGuardCondition isHydraulicPressureGuardCondition;
	
	@Guard
	private SysMLGuard isHydraulicPressureGuard;
	
	@Transition
	public SysMLTransition operationalOnEventTransition;

	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnEventTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnEventTransitionEffect;

	public MechanicalBrakeStateMachine(MechanicalBrake contextPart)
	{
		super(contextPart, false, "MechanicalBrakeStateMachine");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isHydraulicPressureGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof HydraulicPressureSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isHydraulicPressureGuard = new SysMLGuard(context, isHydraulicPressureGuardCondition, "isHydraulicPressure");
	}
	
	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnEventTransitionEffectActivity = (event, contextPart) ->
		{
			MechanicalBrake brake = (MechanicalBrake)contextPart.get();
			ForceNewtonsPerMeterSquare pressure = ((HydraulicPressureSignal)((SysMLSignalEvent)event.get()).signal).force;
			brake.onHydraulicPressureChange(pressure);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		this.operationalOnEventTransitionEffect = new SysMLEffect(context, operationalOnEventTransitionEffectActivity, "operationalOnHydraulicsTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnEventTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isHydraulicPressureGuard), Optional.of(operationalOnEventTransitionEffect),
			"OperationalOnHydraulics", SysMLTransitionKind.internal);
	}
}
