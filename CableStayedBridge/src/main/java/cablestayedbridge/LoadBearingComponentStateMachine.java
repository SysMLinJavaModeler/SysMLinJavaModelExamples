package cablestayedbridge;

import java.util.Optional;

import cablestayedbridge.ports.LoadSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.parts.SysMLPart;
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
 * SysMLinJava state machine based representation of the behavior of the load
 * bearing component. The state machine consists primarily of a state whereby
 * the component is receiving increments of load and a state whereby all loads
 * have been received and the component transmits the load to other components
 * of the structure.
 * 
 * @author ModelerOne
 *
 */
public class LoadBearingComponentStateMachine extends SysMLStateMachine
{
	/**
	 * State for initializing the component
	 */
	@State
	protected SysMLState initializingState;
	/**
	 * State for receiving an individual (not last) load on the component
	 */
	@State
	protected SysMLState loadingState;
	/**
	 * State for receiving the last load on the component and transmitting the total
	 * load to the next component(s)
	 */
	@State
	protected SysMLState loadedState;
	/**
	 * State for the component having failed from the load
	 */
	@State
	protected SysMLState failedState;

	/**
	 * Transmition from initial to initializing state
	 */
	@Transition
	protected InitialTransition initialToInitializingTransition;
	/**
	 * Transmition from initializing to loading state
	 */
	@Transition
	protected SysMLTransition initializingToLoadingTransition;
	/**
	 * Transmition from initializing to loaded state
	 */
	@Transition
	protected SysMLTransition initializingToLoadedTransition;
	/**
	 * Transmition from loading to loaded state
	 */
	@Transition
	protected SysMLTransition loadingToLoadedTransition;
	/**
	 * Transmition (internal) for handling of the next (not last) load received
	 */
	@Transition
	protected SysMLTransition loadingOnLoadingTransition;
	/**
	 * Transmition from loaded to loading state
	 */
	@Transition
	protected SysMLTransition loadedToLoadingTransition;
	/**
	 * Transmition (internal) for handling of the last load received
	 */
	@Transition
	protected SysMLTransition loadedOnLoadedTransition;
	/**
	 * Transmition from loaded to failed state
	 */
	@Transition
	protected SysMLTransition loadedToFailedTransition;
	/**
	 * Transmition from failed to final state
	 */
	@Transition
	protected SysMLTransition failedToFinalTransition;

	/**
	 * Guard condition that the received load is the last in a set of loads
	 */
	@GuardCondition
	protected SysMLGuardCondition toLoadedGuardCondition;
	/**
	 * Guard condition that the received load is the not the last in a set of loads
	 */
	@GuardCondition
	protected SysMLGuardCondition toLoadingGuardCondition;

	/**
	 * Guard that invokes the condition that the received load is the last in a set
	 * of loads
	 */
	@Guard
	protected SysMLGuard toLoadedGuard;
	/**
	 * Guard that invokes the condition that the received load is the not the last
	 * in a set of loads
	 */
	@Guard
	protected SysMLGuard toLoadingGuard;

	/**
	 * Effect action to be performed during a transition for a (not last) load
	 * received
	 */
	@EffectActionFunction
	protected SysMLEffectActionFunction loadingEffectAction;
	/**
	 * Effect action to be performed during a transition for a last-in-a-series
	 * load received
	 */
	@EffectActionFunction
	protected SysMLEffectActionFunction loadedEffectAction;
	/**
	 * Effect action to be performed during a transition for a component failure
	 */
	@EffectActionFunction
	protected SysMLEffectActionFunction failedEffectAction;

	/**
	 * Effect that invokes the action to be performed during a transition for a
	 * (not last) load received
	 */
	@Effect
	protected SysMLEffect loadingEffect;
	/**
	 * Effect that invokes the action to be performed during a transition for a
	 * last-in-a-series load received
	 */
	@Effect
	protected SysMLEffect loadedEffect;
	/**
	 * Effect that invokes the action to be performed during a transition for a
	 * component failure
	 */
	@Effect
	protected SysMLEffect failedEffect;

	/**
	 * Constructor
	 * 
	 * @param context        part in whose context this state machine is to execute
	 * @param isAsynchronous whether this state machine is to execute asychronously,
	 *                       i.e. in its own thread of execution
	 * @param name           unique name
	 */
	public LoadBearingComponentStateMachine(SysMLPart context, boolean isAsynchronous, String name)
	{
		super(Optional.of(context), isAsynchronous, name);
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, "Initializing");
		loadingState = new SysMLState(context, "Loading");
		loadedState = new SysMLState(context, "Loaded");
		failedState = new SysMLState(context, "Failed");
	}

	@Override
	protected void createGuardConditions()
	{
		toLoadingGuardCondition = (event, _) ->
		{
			LoadSignal signal = ((LoadSignal)((SysMLSignalEvent)event.get()).signal);
			Load load = signal.load;
			return !load.lastLoad;
		};
		toLoadedGuardCondition = (event, _) ->
		{
			LoadSignal signal = ((LoadSignal)((SysMLSignalEvent)event.get()).signal);
			Load load = signal.load;
			return load.lastLoad;
		};
	}

	@Override
	protected void createGuards()
	{
		toLoadedGuard = new SysMLGuard(context, toLoadedGuardCondition, "ToLoadedGuard");
		toLoadingGuard = new SysMLGuard(context, toLoadingGuardCondition, "ToLoadingGuard");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		loadingEffectAction = (event, context) ->
		{
			LoadSignal signal = ((LoadSignal)((SysMLSignalEvent)event.get()).signal);
			Load load = signal.load;
			((LoadBearingComponent)context.get()).onLoad(load);
		};
		loadedEffectAction = (event, context) ->
		{
			LoadSignal signal = ((LoadSignal)((SysMLSignalEvent)event.get()).signal);
			Load load = signal.load;
			((LoadBearingComponent)context.get()).onLoaded(load);
		};
		failedEffectAction = (event, context) ->
		{
			FailureEvent failureEvent = (FailureEvent)event.get();
			((LoadBearingComponent)context.get()).onFailed(failureEvent);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		loadingEffect = new SysMLEffect(context, loadingEffectAction, "toLoadingEffect");
		loadedEffect = new SysMLEffect(context, loadedEffectAction, "toLoadedEffect");
		failedEffect = new SysMLEffect(context, failedEffectAction, "toFailedEffect");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToLoadingTransition = new SysMLTransition(context, initializingState, loadingState, Optional.of(SysMLSignalEvent.class), Optional.of(toLoadingGuard), Optional.of(loadingEffect), "InitializingToLoading",
			SysMLTransitionKind.external);
		initializingToLoadedTransition = new SysMLTransition(context, initializingState, loadedState, Optional.of(SysMLSignalEvent.class), Optional.of(toLoadedGuard), Optional.of(loadedEffect), "InitializingToLoading",
			SysMLTransitionKind.external);
		loadingOnLoadingTransition = new SysMLTransition(context, loadingState, loadingState, Optional.of(SysMLSignalEvent.class), Optional.of(toLoadingGuard), Optional.of(loadingEffect), "LoadingOnLoading",
			SysMLTransitionKind.internal);
		loadingToLoadedTransition = new SysMLTransition(context, loadingState, loadedState, Optional.of(SysMLSignalEvent.class), Optional.of(toLoadedGuard), Optional.of(loadedEffect), "LoadingToLoaded", SysMLTransitionKind.external);
		loadedOnLoadedTransition = new SysMLTransition(context, loadedState, loadedState, Optional.of(SysMLSignalEvent.class), Optional.of(toLoadedGuard), Optional.of(loadedEffect), "LoadedOnLoaded",
			SysMLTransitionKind.internal);
		loadedToLoadingTransition = new SysMLTransition(context, loadedState, loadingState, Optional.of(SysMLSignalEvent.class), Optional.of(toLoadingGuard), Optional.of(loadingEffect), "LoadedToLoading", SysMLTransitionKind.external);
		loadedToFailedTransition = new SysMLTransition(context, loadedState, failedState, Optional.of(FailureEvent.class), Optional.empty(), Optional.of(failedEffect), "LoadedToFailed", SysMLTransitionKind.external);
		failedToFinalTransition = new FinalTransition(context, failedState, finalState, "FailedToFinal");
	}
}
