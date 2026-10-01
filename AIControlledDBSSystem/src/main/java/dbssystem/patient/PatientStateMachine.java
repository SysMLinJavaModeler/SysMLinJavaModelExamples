package dbssystem.patient;

import java.util.Optional;

import dbssystem.common.DBSSignalSignal;
import sysmlinjava.attributetypes.DurationMilliseconds;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.events.SysMLTimeEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.EntryActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
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
 * State machine for the Patient. The state machine consists of only two states
 * - one to initialize the patient and one to repeatedly handle receipts of
 * {@code DBSSignal}s from the {@code DBSActuator} and occurance of motion
 * transmission times (simulating repeated tremor motion).
 * 
 * @author ModelerOne
 */
public class PatientStateMachine extends SysMLStateMachine
{
	@EntryActionFunction
	protected SysMLEntryActionFunction initializeActivity;

	@State
	protected SysMLState initializingState;
	@State
	protected SysMLState operationalState;

	@Transition
	protected InitialTransition initialToInitializingTransition;
	@Transition
	protected SysMLTransition initializingToOperationalTransition;
	@Transition
	protected SysMLTransition operationalOnDBSSignalTransition;
	@Transition
	protected SysMLTransition operationalOnMotionTimeTransition;
	@Transition
	protected SysMLTransition operationalToFinalTransition;

	@GuardCondition
	private SysMLGuardCondition isDBSSignalGuardCondition;

	@Guard
	private SysMLGuard isDBSSignalGuard;

	@EffectActionFunction
	protected SysMLEffectActionFunction onDBSSignalEffectActivity;
	@EffectActionFunction
	protected SysMLEffectActionFunction onMotionTimeEffectActivity;

	@Effect
	protected SysMLEffect onDBSSignalEffect;
	@Effect
	protected SysMLEffect onMotionTimeEffect;

	static final String timerID = "motionTimer";

	/**
	 * Constructor
	 * 
	 * @param patient part whose state behavior this state machine represents
	 */
	public PatientStateMachine(Patient patient)
	{
		super(Optional.of(patient), true, "PatientStateMachine");
	}

	@Override
	protected void createStateEntryActionFunctions()
	{
		initializeActivity = (contextBlock) ->
		{
			contextBlock.get().getStateMachine().get().startTimer(new SysMLTimeEvent(timerID, DurationMilliseconds.ofSeconds(5), Optional.of(DurationMilliseconds.ofSeconds(5))));
		};
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, Optional.of(initializeActivity), Optional.empty(), Optional.empty(), "Initializing");
		operationalState = new SysMLState(context, "Operational");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createGuardConditions()
	{
		isDBSSignalGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() && event.get() instanceof SysMLSignalEvent signalEvent && signalEvent.signal instanceof DBSSignalSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isDBSSignalGuard = new SysMLGuard(context, this.isDBSSignalGuardCondition, "isDBSSignal");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createEffectActionFunctions()
	{
		onDBSSignalEffectActivity = (event, contextBlock) ->
		{
			SysMLSignalEvent signalEvent = (SysMLSignalEvent) event.get();
			DBSSignalSignal dbsSignalSignal = (DBSSignalSignal) signalEvent.signal;
			Patient patient = (Patient) contextBlock.get();
			patient.onDBSSignal(dbsSignalSignal.value);
		};
		onMotionTimeEffectActivity = (event, contextBlock) ->
		{
			Patient patient = (Patient) contextBlock.get();
			patient.onMotionTime();
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onDBSSignalEffect = new SysMLEffect(context, onDBSSignalEffectActivity, "onDBSSignal");
		onMotionTimeEffect = new SysMLEffect(context, onMotionTimeEffectActivity, "onMotionTime");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnDBSSignalTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isDBSSignalGuard),
		Optional.of(onDBSSignalEffect), "OperationalOnDBSSignal", SysMLTransitionKind.internal);
		operationalOnMotionTimeTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLTimeEvent.class), Optional.empty(),
		Optional.of(onMotionTimeEffect), "OperationalOnMotionTime", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}
}
