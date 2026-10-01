package hflink.components.deployedcomputer;

import java.util.Optional;

import hflink.common.items.HTTPRequest;
import hflink.common.signals.HTTPRequestSignal;
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
 * State machine for the Web Server model/simulation. The state machine consists
 * of states to initialize and operate, with one key transition being the one
 * that occurs upon receipt of an HTTP request from the web browser on the
 * {@code CommandControlSystem}. See the model of the state machine below for
 * more detail.
 * 
 * @author ModelerOne
 *
 */
@SuppressWarnings({"javadoc", "unused"})
public class WebServerStateMachine extends SysMLStateMachine
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
	private SysMLTransition operationalOnHTTPRequestTransition;
	@Transition
	private FinalTransition operationalToFinalTransition;

	@GuardCondition
	private SysMLGuardCondition isHTTPRequestEventGuardCondition;

	@Guard
	private SysMLGuard isHTTPRequestEventGuard;

	@EffectActionFunction
	private SysMLEffectActionFunction onHTTPRequestEventEffectAction;

	@Effect
	private SysMLEffect onHTTPRequestEventEffect;

	public WebServerStateMachine(WebServer webServer)
	{
		super(Optional.of(webServer), true, "WebServerStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, Optional.empty(), Optional.empty(), Optional.empty(), "Initializing");
		operationalState = new SysMLState(context, Optional.empty(), Optional.empty(), Optional.empty(), "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		isHTTPRequestEventGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent signalEvent &&
				signalEvent.signal instanceof HTTPRequestSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isHTTPRequestEventGuard = new SysMLGuard(context, isHTTPRequestEventGuardCondition, "isHTTPRequest");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		onHTTPRequestEventEffectAction = (event, contextBlock) ->
		{
			if (event.isPresent() && event.get() instanceof SysMLSignalEvent signalEvent)
			{
				HTTPRequest request = ((HTTPRequestSignal)signalEvent.signal).request;
				WebServer webServer = (WebServer)contextBlock.get();
				webServer.onHTTPRequest(request);
			}
			else
				logger.warning("unexpected event type: " + event.get().getClass().getSimpleName());
		};
	}

	@Override
	protected void createEffects()
	{
		onHTTPRequestEventEffect = new SysMLEffect(context, onHTTPRequestEventEffectAction, "onHTTPRequest");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnHTTPRequestTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isHTTPRequestEventGuard), Optional.of(onHTTPRequestEventEffect), "OperationalOnHttpRequest",
			SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}
}