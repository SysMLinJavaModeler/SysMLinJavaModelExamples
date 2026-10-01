
package c4s2.systems.target;

import java.util.Optional;

import c4s2.common.items.information.RadarSignalTransmission;
import c4s2.common.items.information.StrikeOrdnance;
import c4s2.common.signals.RadarSignalTransmissionSignal;
import c4s2.common.signals.StrikeOrdnanceSignal;
import sysmlinjava.attributetypes.DurationMilliseconds;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLEntryActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

/**
 * The VehicleTargetStateMachine is the SysMLinJava model of the state machine
 * for a vehicle (moving) target trackable by radar and destroyable by ordnance.
 * The state machine has 3 states - initializing, operational, and destroyed.
 * See the state machine's states and transitions for definition of this
 * behavior.
 * 
 * @author ModelerOne
 *
 *         Known users:
 * @see VehicleArmoredLargeTarget
 */
public class VehicleTargetStateMachine extends SysMLStateMachine
{
	private static final String explodingTimer = "explodingTimer";
	@State
	public SysMLState initializingState;
	@State
	public SysMLState operationalState;
	@State
	public SysMLState explodingState;
	@State
	public SysMLState destroyedState;

	@Transition
	public InitialTransition initialToInitializingTransition;
	@Transition
	public SysMLTransition initializingToOperationalTransition;
	@Transition
	public SysMLTransition operationalOnRadarSignalTransmissionTransition;
	@Transition
	public SysMLTransition operationalToExplodingTransition;
	@Transition
	public SysMLTransition explodingToDestroyedTransition;
	@Transition
	public SysMLTransition destroyedOnRadarSignalTransmissionTransition;
	@Transition
	public FinalTransition destroyedToFinalTransition;

	@EntryActionFunction
	public SysMLEntryActionFunction initializingStateOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction explodingStateOnEnterAction;
	@EntryActionFunction
	public SysMLEntryActionFunction destroyedStateOnEnterAction;

	@GuardCondition
	public SysMLGuardCondition isDestroyedTimeGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isDestroyedByStrikeOrdnanceGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isRadarSignalTransmissionGuardCondition;

	@Guard
	public SysMLGuard isDestroyedTimeGuard;
	@Guard
	public SysMLGuard isDestroyedByStrikeOrdnanceGuard;
	@Guard
	public SysMLGuard isRadarSignalTransmissionGuard;

	@EffectActionFunction
	public SysMLEffectActionFunction operationalOnRadarSignalTransmissionTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction explodingToDestroyedTransitionEffectAction;
	@EffectActionFunction
	public SysMLEffectActionFunction destroyedOnRadarSignalTransmissionTransitionEffectAction;

	@Effect
	public SysMLEffect operationalOnRadarSignalTransmissionTransitionEffect;
	@Effect
	public SysMLEffect explodingToDestroyedTransitionEffect;
	@Effect
	public SysMLEffect destroyedOnRadarSignalTransmissionTransitionEffect;

	public VehicleTargetStateMachine(VehicleArmoredLargeTarget vehicleTarget)
	{
		super(Optional.of(vehicleTarget), true, "VehicleTargetStateMachine");
	}

	@Override
	protected void createStateEntryActionFunctions()
	{
		super.createStateEntryActionFunctions();
		initializingStateOnEnterAction = (context) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)context.get();
			vehicleTarget.initialize();
		};
		explodingStateOnEnterAction = (context) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)context.get();
			vehicleTarget.explode();
			startTimer(new SysMLTimeEvent(explodingTimer, DurationMilliseconds.ofSeconds(2), Optional.empty()));
		};
		destroyedStateOnEnterAction = (context) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)context.get();
			vehicleTarget.destroy();
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, Optional.of(initializingStateOnEnterAction), Optional.empty(), Optional.empty(), "Initializing");
		operationalState = new SysMLState(context, "Operational");
		explodingState = new SysMLState(context, Optional.of(explodingStateOnEnterAction), Optional.empty(), Optional.empty(), "Exploding");
		destroyedState = new SysMLState(context, Optional.of(destroyedStateOnEnterAction), Optional.empty(), Optional.empty(), "Destroyed");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isDestroyedTimeGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLTimeEvent && ((SysMLTimeEvent)event.get()).timerID.equals(explodingTimer);
		};

		isDestroyedByStrikeOrdnanceGuardCondition = (event, context) ->
		{
			boolean result = false;
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)context.get();
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof StrikeOrdnanceSignal)
			{
				StrikeOrdnance strikeOrdnance = ((StrikeOrdnanceSignal)((SysMLSignalEvent)event.get()).signal).ordnance;
				result = vehicleTarget.isDestroyedBy(strikeOrdnance);
			}
			return result;
		};
		isRadarSignalTransmissionGuardCondition = (event, context) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent && ((SysMLSignalEvent)event.get()).signal instanceof RadarSignalTransmissionSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isDestroyedTimeGuard = new SysMLGuard(context, isDestroyedTimeGuardCondition, "isDestroyedTime");
		isDestroyedByStrikeOrdnanceGuard = new SysMLGuard(context, isDestroyedByStrikeOrdnanceGuardCondition, "isDestroyedByStrikeOrdnance");
		isRadarSignalTransmissionGuard = new SysMLGuard(context, isRadarSignalTransmissionGuardCondition, "isRadarSignalTransmission");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		operationalOnRadarSignalTransmissionTransitionEffectAction = (event, context) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)context.get();
			RadarSignalTransmission transmission = ((RadarSignalTransmissionSignal)((SysMLSignalEvent)event.get()).signal).transmission;
			vehicleTarget.receiveRadarSignalTransmissionWhenOperational(transmission);
		};
		explodingToDestroyedTransitionEffectAction = (event, context) ->
		{
		};
		destroyedOnRadarSignalTransmissionTransitionEffectAction = (event, context) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)context.get();
			RadarSignalTransmission transmission = ((RadarSignalTransmissionSignal)((SysMLSignalEvent)event.get()).signal).transmission;
			vehicleTarget.receiveRadarSignalTransmissionWhenDestroyed(transmission);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnRadarSignalTransmissionTransitionEffect = new SysMLEffect(context, operationalOnRadarSignalTransmissionTransitionEffectAction, "OperationalOnRadarSignalTransmissionTransition");
		explodingToDestroyedTransitionEffect = new SysMLEffect(context, explodingToDestroyedTransitionEffectAction, "ExplodingToDestroyedTransition");
		destroyedOnRadarSignalTransmissionTransitionEffect = new SysMLEffect(context, destroyedOnRadarSignalTransmissionTransitionEffectAction, "DestroyedOnRadarSignalTransmissionTransition");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnRadarSignalTransmissionTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isRadarSignalTransmissionGuard),
			Optional.of(operationalOnRadarSignalTransmissionTransitionEffect), "OperationalOnRadarSignalTransmission", SysMLTransitionKind.internal);
		operationalToExplodingTransition = new SysMLTransition(context, operationalState, explodingState, Optional.of(SysMLSignalEvent.class), Optional.of(isDestroyedByStrikeOrdnanceGuard), Optional.empty(), "OperationalToExploding",
			SysMLTransitionKind.external);
		explodingToDestroyedTransition = new SysMLTransition(context, explodingState, destroyedState, Optional.of(SysMLTimeEvent.class), Optional.of(isDestroyedTimeGuard), Optional.of(explodingToDestroyedTransitionEffect),
			"ExplodingToDestroyed", SysMLTransitionKind.external);
		destroyedOnRadarSignalTransmissionTransition = new SysMLTransition(context, destroyedState, destroyedState, Optional.of(SysMLSignalEvent.class), Optional.of(isRadarSignalTransmissionGuard),
			Optional.of(destroyedOnRadarSignalTransmissionTransitionEffect), "DestroyedOnRadarSignalTransmission", SysMLTransitionKind.internal);
		destroyedToFinalTransition = new FinalTransition(context, destroyedState, finalState, "DestroyedToFinal");
	}
}
