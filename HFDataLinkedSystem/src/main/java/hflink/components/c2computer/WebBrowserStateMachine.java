package hflink.components.c2computer;

import java.util.Optional;

import hflink.common.items.ApplicationUIControl;
import hflink.common.items.HTTPResponse;
import hflink.common.signals.ApplicationUIControlSignal;
import hflink.common.signals.HTTPResponseSignal;
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
 * State machine for the Web Browser model/simulation. The state machine
 * consists of states to initialize and operate, with the key transitions being
 * one that occurs upon receipt of an application user-interface control from
 * the operator, and the other being receipt of an HTTP response from the web
 * servers of the {@code DeployedSystem}s. See the model of the state machine
 * below for more detail.
 * 
 * @author ModelerOne
 *
 */
@SuppressWarnings({"javadoc", "unused"})
public class WebBrowserStateMachine extends SysMLStateMachine
{
	@State
	private SysMLState initializingState;
	@State
	private SysMLState operationalState;

	@Transition
	private InitialTransition initialToInitializingTransition;
	@Transition
	private SysMLTransition initializingToOperationalTransition;
	@Transition
	private SysMLTransition operationalOnApplicationUIControlTransition;
	@Transition
	private SysMLTransition operationalOnHTTPResponseTransition;
	@Transition
	private FinalTransition operationalToFinalTransition;

	@GuardCondition
	private SysMLGuardCondition isApplicationUIControlGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isHTTPResponseGuardCondition;

	@Guard
	private SysMLGuard isHTTPResponseGuard;
	@Guard
	private SysMLGuard isApplicationUIControlGuard;

	@EffectActionFunction
	private SysMLEffectActionFunction onApplicationUIControlEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction onHTTPResponseffectAction;

	@Effect
	private SysMLEffect onHTTPResponseEffect;
	@Effect
	private SysMLEffect onApplicationUIControlEffect;

	public WebBrowserStateMachine(WebBrowser webBrowser)
	{
		super(Optional.of(webBrowser), true, "WebBrowserStateMachine");
	}

	@Override
	protected void createGuardConditions()
	{
		isApplicationUIControlGuardCondition = (event, contextBlock) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof ApplicationUIControlSignal;
		};
		isHTTPResponseGuardCondition = (event, contextBlock) ->
		{
			return ((SysMLSignalEvent)event.get()).signal instanceof HTTPResponseSignal;
		};
	}
	
	@Override
	protected void createGuards()
	{
		isApplicationUIControlGuard = new SysMLGuard(context, isApplicationUIControlGuardCondition, "isApplicationUIControl");
		isHTTPResponseGuard = new SysMLGuard(context, isHTTPResponseGuardCondition, "isHTTPResponse");
	}
	
	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, "Initializing");
		operationalState = new SysMLState(context, "Operational");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		onHTTPResponseffectAction = (event, contextBlock) ->
		{
			HTTPResponse response = ((HTTPResponseSignal)((SysMLSignalEvent)event.get()).signal).response;
			WebBrowser webBrowser = (WebBrowser)contextBlock.get();
			webBrowser.onHTTPResponse(response);
		};

		onApplicationUIControlEffectAction = (event, contextBlock) ->
		{
			ApplicationUIControl control = ((ApplicationUIControlSignal)((SysMLSignalEvent)event.get()).signal).control;
			WebBrowser webBrowser = (WebBrowser)contextBlock.get();
			webBrowser.onApplicationUIControl(control);
		};
	}

	@Override
	protected void createEffects()
	{
		onHTTPResponseEffect = new SysMLEffect(context, onHTTPResponseffectAction, "onHTTPResponse");
		onApplicationUIControlEffect = new SysMLEffect(context, onApplicationUIControlEffectAction, "onApplicationUIControl");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnHTTPResponseTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isHTTPResponseGuard), Optional.of(onHTTPResponseEffect), "OperationalOnHTTPResponse",
			SysMLTransitionKind.internal);
		operationalOnApplicationUIControlTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isApplicationUIControlGuard), Optional.of(onApplicationUIControlEffect),
			"OperationalOnApplicationUIControl", SysMLTransitionKind.external);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}
}