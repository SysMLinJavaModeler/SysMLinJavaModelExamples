package dbssystem.controller;

import java.util.Optional;

import dbssystem.common.PulseValueSignal;
import dbssystem.common.TremorLevelSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalTransition;
import sysmlinjava.states.InitialTransition;
import sysmlinjava.states.SysMLEffect;
import sysmlinjava.states.SysMLEffectActionFunction;
import sysmlinjava.states.SysMLGuard;
import sysmlinjava.states.SysMLGuardCondition;
import sysmlinjava.states.SysMLState;
import sysmlinjava.states.SysMLStateMachine;
import sysmlinjava.states.SysMLTransition;
import sysmlinjava.states.SysMLTransitionKind;

/**
 * State machine for the DBSController. The state machine consists of only two
 * states - one to initialize the controller and one to repeatedly handle
 * receipts of tremor and pulse values from the patient.
 * 
 * @author ModelerOne
 */
public class DBSControllerStateMachine extends SysMLStateMachine
{
	@State
	protected SysMLState initializingState;
	@State
	protected SysMLState operationalState;

	@Transition
	protected InitialTransition initialToInitializingTransition;
	@Transition
	protected SysMLTransition initializingToOperationalTransition;
	@Transition
	protected SysMLTransition operationalOnTremorLevelTransition;
	@Transition
	protected SysMLTransition operationalOnPulseValueTransition;
	@Transition
	protected SysMLTransition operationalToFinalTransition;

	@GuardCondition
	protected SysMLGuardCondition isPulseGuardCondition;
	@GuardCondition
	protected SysMLGuardCondition isTremorGuardCondition;

	@Guard
	protected SysMLGuard isPulseGuard;
	@Guard
	protected SysMLGuard isTremorGuard;

	@EffectActionFunction
	protected SysMLEffectActionFunction onPulseValueEffectAction;
	@EffectActionFunction
	protected SysMLEffectActionFunction onTremorLevelEffectAction;

	@Effect
	protected SysMLEffect onPulseValueEffect;
	@Effect
	protected SysMLEffect onTremorLevelEffect;

	/**
	 * Constructor
	 * 
	 * @param contextPart part ({@code DBSController}) in whose context this state
	 *                    machine executes
	 */
	public DBSControllerStateMachine(DBSController contextPart)
	{
		super(Optional.of(contextPart), true, "DBSControllerStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, "Initializing");
		operationalState = new SysMLState(context, "Operational");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createGuardConditions()
	{
		isPulseGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent signalEvent && signalEvent.signal instanceof PulseValueSignal;
		};
		isTremorGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent signalEvent && signalEvent.signal instanceof TremorLevelSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isPulseGuard = new SysMLGuard(context, isPulseGuardCondition, "isPulse");
		isTremorGuard = new SysMLGuard(context, isTremorGuardCondition, "isTremor");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		onPulseValueEffectAction = (event, contextBlock) ->
		{
			DBSController controller = (DBSController) contextBlock.get();
			SysMLSignalEvent pulseEvent = (SysMLSignalEvent) event.get();
			PulseValueSignal pulseValueSignal = (PulseValueSignal) pulseEvent.signal;
			controller.onPulseValue(pulseValueSignal.value);
		};
		onTremorLevelEffectAction = (event, contextBlock) ->
		{
			DBSController controller = (DBSController) contextBlock.get();
			SysMLSignalEvent tremorEvent = (SysMLSignalEvent) event.get();
			TremorLevelSignal tremorLevelSignal = (TremorLevelSignal) tremorEvent.signal;
			controller.onTremorLevel(tremorLevelSignal.value);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onPulseValueEffect = new SysMLEffect(context, onPulseValueEffectAction, "onPulseValue");
		onTremorLevelEffect = new SysMLEffect(context, onTremorLevelEffectAction, "onTremorLevel");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnPulseValueTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isPulseGuard),
		Optional.of(onPulseValueEffect), "OperationalOnPulseValue", SysMLTransitionKind.internal);
		operationalOnTremorLevelTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isTremorGuard),
		Optional.of(onTremorLevelEffect), "OperationalOnTremorLevel", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}
}
