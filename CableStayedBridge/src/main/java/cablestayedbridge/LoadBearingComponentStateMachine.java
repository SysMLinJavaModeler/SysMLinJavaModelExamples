package cablestayedbridge;

import java.util.Optional;
import cablestayedbridge.ports.LoadSignal;
import cablestayedbridge.ports.LoadSignalEvent;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.Guard;
import sysmlinjava.annotations.statemachines.GuardCondition;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLGuard;
import sysmlinjava.statemachine.SysMLGuardCondition;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;

/**
 * SysMLinJava state machine=base representation of the behavior of the load
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
	 * Effect activity to be performed during a transition for a (not last) load
	 * received
	 */
	@EffectActivity
	protected SysMLEffectActivity loadingEffectActivity;
	/**
	 * Effect activity to be performed during a transition for a last-in-a-series
	 * load received
	 */
	@EffectActivity
	protected SysMLEffectActivity loadedEffectActivity;
	/**
	 * Effect activity to be performed during a transition for a component failure
	 */
	@EffectActivity
	protected SysMLEffectActivity failedEffectActivity;

	/**
	 * Effect that invokes the activity to be performed during a transition for a
	 * (not last) load received
	 */
	@Effect
	protected SysMLEffect loadingEffect;
	/**
	 * Effect that invokes the activity to be performed during a transition for a
	 * last-in-a-series load received
	 */
	@Effect
	protected SysMLEffect loadedEffect;
	/**
	 * Effect that invokes the activity to be performed during a transition for a
	 * component failure
	 */
	@Effect
	protected SysMLEffect failedEffect;

	/**
	 * Constructor
	 * 
	 * @param contextBlock   block in whose context this state machine is to execute
	 * @param isAsynchronous whether this state machine is to execute asychronously,
	 *                       i.e. in its own thread of execution
	 * @param name           unique name
	 */
	public LoadBearingComponentStateMachine(SysMLBlock contextBlock, boolean isAsynchronous, String name)
	{
		super(Optional.of(contextBlock), isAsynchronous, name);
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(contextBlock, "Initializing");
		loadingState = new SysMLState(contextBlock, "Loading");
		loadedState = new SysMLState(contextBlock, "Loaded");
		failedState = new SysMLState(contextBlock, "Failed");
	}

	@Override
	protected void createGuardConditions()
	{
		toLoadingGuardCondition = (event, contextBlock) ->
		{
			LoadSignalEvent signalEvent = (LoadSignalEvent)event.get();
			LoadSignal signal = (LoadSignal)signalEvent.signal;
			Load load = signal.load;
			return !load.lastLoad;
		};
		toLoadedGuardCondition = (event, contextBlock) ->
		{
			LoadSignalEvent signalEvent = (LoadSignalEvent)event.get();
			LoadSignal signal = (LoadSignal)signalEvent.signal;
			Load load = signal.load;
			return load.lastLoad;
		};
	}

	@Override
	protected void createGuards()
	{
		toLoadedGuard = new SysMLGuard(contextBlock, toLoadedGuardCondition, "ToLoadedGuard");
		toLoadingGuard = new SysMLGuard(contextBlock, toLoadingGuardCondition, "ToLoadingGuard");
	}

	protected void createEffectActivities()
	{
		loadingEffectActivity = (event, contextBlock) ->
		{
			LoadSignalEvent signalEvent = (LoadSignalEvent)event.get();
			LoadSignal signal = (LoadSignal)signalEvent.signal;
			Load load = signal.load;
			((LoadBearingComponent)contextBlock.get()).onLoad(load);
		};
		loadedEffectActivity = (event, contextBlock) ->
		{
			LoadSignalEvent signalEvent = (LoadSignalEvent)event.get();
			LoadSignal signal = (LoadSignal)signalEvent.signal;
			Load load = signal.load;
			((LoadBearingComponent)contextBlock.get()).onLoaded(load);
		};
		failedEffectActivity = (event, contextBlock) ->
		{
			FailureEvent failureEvent = (FailureEvent)event.get();
			((LoadBearingComponent)contextBlock.get()).onFailed(failureEvent);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		loadingEffect = new SysMLEffect(contextBlock, loadingEffectActivity, "toLoadingEffect");
		loadedEffect = new SysMLEffect(contextBlock, loadedEffectActivity, "toLoadedEffect");
		failedEffect = new SysMLEffect(contextBlock, failedEffectActivity, "toFailedEffect");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "InitialToInitializing");
		initializingToLoadingTransition = new SysMLTransition(contextBlock, initializingState, loadingState, Optional.of(LoadSignalEvent.class), Optional.of(toLoadingGuard), Optional.of(loadingEffect), "InitializingToLoading",
			SysMLTransitionKind.external);
		initializingToLoadedTransition = new SysMLTransition(contextBlock, initializingState, loadedState, Optional.of(LoadSignalEvent.class), Optional.of(toLoadedGuard), Optional.of(loadedEffect), "InitializingToLoading",
			SysMLTransitionKind.external);
		loadingOnLoadingTransition = new SysMLTransition(contextBlock, loadingState, loadingState, Optional.of(LoadSignalEvent.class), Optional.of(toLoadingGuard), Optional.of(loadingEffect), "LoadingOnLoading",
			SysMLTransitionKind.internal);
		loadingToLoadedTransition = new SysMLTransition(contextBlock, loadingState, loadedState, Optional.of(LoadSignalEvent.class), Optional.of(toLoadedGuard), Optional.of(loadedEffect), "LoadingToLoaded", SysMLTransitionKind.external);
		loadedOnLoadedTransition = new SysMLTransition(contextBlock, loadedState, loadedState, Optional.of(LoadSignalEvent.class), Optional.of(toLoadedGuard), Optional.of(loadedEffect), "LoadedOnLoaded",
			SysMLTransitionKind.internal);
		loadedToLoadingTransition = new SysMLTransition(contextBlock, loadedState, loadingState, Optional.of(LoadSignalEvent.class), Optional.of(toLoadingGuard), Optional.of(loadingEffect), "LoadedToLoading", SysMLTransitionKind.external);
		loadedToFailedTransition = new SysMLTransition(contextBlock, loadedState, failedState, Optional.of(FailureEvent.class), Optional.empty(), Optional.of(failedEffect), "LoadedToFailed", SysMLTransitionKind.external);
		failedToFinalTransition = new FinalTransition(contextBlock, failedState, finalState, "FailedToFinal");
	}
}
