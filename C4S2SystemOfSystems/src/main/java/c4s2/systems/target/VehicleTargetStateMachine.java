
package c4s2.systems.target;

import java.util.Optional;
import c4s2.common.events.RadarSignalTransmissionEvent;
import c4s2.common.events.StrikeOrdnanceEvent;
import c4s2.common.objects.information.RadarSignalTransmission;
import c4s2.common.objects.information.StrikeOrdnance;
import c4s2.common.signals.RadarSignalTransmissionSignal;
import c4s2.common.signals.StrikeOrdnanceSignal;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.OnEnterActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLOnEnterActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;
import sysmlinjava.valuetypes.DurationMilliseconds;

/**
 * The VehicleTargetStateMachine is the SysMLinJava model of the state machine
 * for a vehicle (moving) target trackable by radar and destroyable by ordnance.
 * The state machine has 3 states - initializing, operational, and destroyed.
 * See the state machine's states and transitions for definition of this
 * behavior.
 * 
 * @author ModelerOne
 *
 * Known users:
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

	@OnEnterActivity
	public SysMLOnEnterActivity initializingStateOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity explodingStateOnEnterActivity;
	@OnEnterActivity
	public SysMLOnEnterActivity destroyedStateOnEnterActivity;

	@GuardCondition
	public SysMLGuardCondition isDestroyedTimeGuardCondition;
	@GuardCondition
	public SysMLGuardCondition isDestroyedByStrikeOrdnanceGuardCondition;

	@Guard
	public SysMLGuard isDestroyedTimeGuard;
	@Guard
	public SysMLGuard isDestroyedByStrikeOrdnanceGuard;

	@EffectActivity
	public SysMLEffectActivity operationalOnRadarSignalTransmissionTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity explodingToDestroyedTransitionEffectActivity;
	@EffectActivity
	public SysMLEffectActivity destroyedOnRadarSignalTransmissionTransitionEffectActivity;

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
	protected void createStateOnEnterActivities()
	{
		super.createStateOnEnterActivities();
		initializingStateOnEnterActivity = (contextBlock) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)contextBlock.get();
			vehicleTarget.initialize();
		};
		explodingStateOnEnterActivity = (contextBlock) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)contextBlock.get();
			vehicleTarget.explode();
			startTimer(new SysMLTimeEvent(explodingTimer, DurationMilliseconds.ofSeconds(2), Optional.empty()));
		};
		destroyedStateOnEnterActivity = (contextBlock) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)contextBlock.get();
			vehicleTarget.destroy();
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(contextBlock, Optional.of(initializingStateOnEnterActivity), Optional.empty(), Optional.empty(), "Initializing");
		operationalState = new SysMLState(contextBlock, "Operational");
		explodingState = new SysMLState(contextBlock, Optional.of(explodingStateOnEnterActivity), Optional.empty(), Optional.empty(), "Exploding");
		destroyedState = new SysMLState(contextBlock, Optional.of(destroyedStateOnEnterActivity), Optional.empty(), Optional.empty(), "Destroyed");
	}

	@Override
	protected void createGuardConditions()
	{
		super.createGuardConditions();
		isDestroyedTimeGuardCondition = (event, contextBlock) ->
		{
			SysMLTimeEvent timeEvent = (SysMLTimeEvent)event.get();
			return (timeEvent.timerID.equals(explodingTimer));
		};

		isDestroyedByStrikeOrdnanceGuardCondition = (event, contextBlock) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)contextBlock.get();
			StrikeOrdnance strikeOrdnance = ((StrikeOrdnanceSignal)((StrikeOrdnanceEvent)event.get()).signal).ordnance;
			boolean isDestroyed = vehicleTarget.isDestroyedBy(strikeOrdnance);
			return isDestroyed;
		};
	}

	@Override
	protected void createGuards()
	{
		super.createGuards();
		isDestroyedTimeGuard = new SysMLGuard(contextBlock, isDestroyedTimeGuardCondition, "isDestroyedTime");
		isDestroyedByStrikeOrdnanceGuard = new SysMLGuard(contextBlock, isDestroyedByStrikeOrdnanceGuardCondition, "isDestroyedByStrikeOrdnance");
	}

	@Override
	protected void createEffectActivities()
	{
		super.createEffectActivities();
		operationalOnRadarSignalTransmissionTransitionEffectActivity = (event, contextBlock) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)contextBlock.get();
			RadarSignalTransmission transmission = ((RadarSignalTransmissionSignal)((RadarSignalTransmissionEvent)event.get()).signal).transmission;
			vehicleTarget.receiveRadarSignalTransmissionWhenOperational(transmission);
		};
		explodingToDestroyedTransitionEffectActivity = (event, contextBlock) ->
		{
		};
		destroyedOnRadarSignalTransmissionTransitionEffectActivity = (event, contextBlock) ->
		{
			VehicleArmoredLargeTarget vehicleTarget = (VehicleArmoredLargeTarget)contextBlock.get();
			RadarSignalTransmission transmission = ((RadarSignalTransmissionSignal)((RadarSignalTransmissionEvent)event.get()).signal).transmission;
			vehicleTarget.receiveRadarSignalTransmissionWhenDestroyed(transmission);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		operationalOnRadarSignalTransmissionTransitionEffect = new SysMLEffect(contextBlock, operationalOnRadarSignalTransmissionTransitionEffectActivity, "OperationalOnRadarSignalTransmissionTransition");
		explodingToDestroyedTransitionEffect = new SysMLEffect(contextBlock, explodingToDestroyedTransitionEffectActivity, "ExplodingToDestroyedTransition");
		destroyedOnRadarSignalTransmissionTransitionEffect = new SysMLEffect(contextBlock, destroyedOnRadarSignalTransmissionTransitionEffectActivity, "DestroyedOnRadarSignalTransmissionTransition");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, "InitializingToOperational");
		operationalOnRadarSignalTransmissionTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(RadarSignalTransmissionEvent.class), Optional.empty(),
			Optional.of(operationalOnRadarSignalTransmissionTransitionEffect), "OperationalOnRadarSignalTransmission", SysMLTransitionKind.internal);
		operationalToExplodingTransition = new SysMLTransition(contextBlock, operationalState, explodingState, Optional.of(StrikeOrdnanceEvent.class), Optional.of(isDestroyedByStrikeOrdnanceGuard),
			Optional.empty(), "OperationalToExploding", SysMLTransitionKind.external);
		explodingToDestroyedTransition = new SysMLTransition(contextBlock, explodingState, destroyedState, Optional.of(SysMLTimeEvent.class), Optional.of(isDestroyedTimeGuard),
			Optional.of(explodingToDestroyedTransitionEffect), "ExplodingToDestroyed", SysMLTransitionKind.external);
		destroyedOnRadarSignalTransmissionTransition = new SysMLTransition(contextBlock, destroyedState, destroyedState, Optional.of(RadarSignalTransmissionEvent.class), Optional.empty(),
			Optional.of(destroyedOnRadarSignalTransmissionTransitionEffect), "DestroyedOnRadarSignalTransmission", SysMLTransitionKind.internal);
		destroyedToFinalTransition = new FinalTransition(contextBlock, destroyedState, finalState, "DestroyedToFinal");
	}
}
