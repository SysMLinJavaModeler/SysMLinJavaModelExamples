package motorinwheel.systems.atmosphere;

import java.util.Optional;
import motorinwheel.common.signals.FrontalArealSpeedSignal;
import motorinwheel.common.stateMachine.SingleStateStateMachine;
import sysmlinjava.attributetypes.FrontalArealSpeed;
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

	@GuardCondition
	private SysMLGuardCondition isFrontalAreaSpeedGuardCondition;
	
	@Guard
	private SysMLGuard isFrontalArealSpeedGuard;
	
	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnAreaFrontalSpeedTransitionEffectActivity;

	@Effect
	public SysMLEffect operationalOnAreaFrontalSpeedTransitionEffect;

	public AtmosphereStateMachine(Atmosphere contextPart)
	{
		super(contextPart, false, "AtmosphereStateMachine");
	}
	
	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isFrontalAreaSpeedGuardCondition = (event, contextPart) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof FrontalArealSpeedSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isFrontalArealSpeedGuard = new SysMLGuard(context, isFrontalAreaSpeedGuardCondition, "isFrontalAreaSpeed");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnAreaFrontalSpeedTransitionEffectActivity = (event, contextPart) ->
		{
			Atmosphere atmosphere = (Atmosphere)contextPart.get();
			FrontalArealSpeed vehicleForces = ((FrontalArealSpeedSignal)((SysMLSignalEvent)event.get()).signal).frontalArealSpeed;
			atmosphere.onFrontalArea(vehicleForces);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnAreaFrontalSpeedTransitionEffect = new SysMLEffect(context, operationalOnAreaFrontalSpeedTransitionEffectActivity, "OperationalOnAreaFrontalSpeedTransition");
	}

	@Override
	protected void createTransitions()
	{
		super.createTransitions();
		operationalOnAreaFrontalSpeedTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isFrontalArealSpeedGuard), Optional.of(operationalOnAreaFrontalSpeedTransitionEffect),
			"OperationalOnAreaFrontalSpeed", SysMLTransitionKind.internal);
	}
}
