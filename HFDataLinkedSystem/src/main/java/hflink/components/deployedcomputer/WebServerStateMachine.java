package hflink.components.deployedcomputer;

import java.util.Optional;
import hflink.common.events.HTTPRequestEvent;
import hflink.common.objects.HTTPRequest;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;

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

	@EffectActivity
	private SysMLEffectActivity onHTTPRequestEventEffectActivity;
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
		initializingState = new SysMLState(contextBlock, Optional.empty(), Optional.empty(), Optional.empty(), "Initializing");
		operationalState = new SysMLState(contextBlock, Optional.empty(), Optional.empty(), Optional.empty(), "Operational");
	}

	@Override
	protected void createEffectActivities()
	{
		onHTTPRequestEventEffectActivity = (event, contextBlock) ->
		{
			if (event.get() instanceof HTTPRequestEvent)
			{
				HTTPRequest request = ((HTTPRequestEvent)event.get()).getRequest();
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
		onHTTPRequestEventEffect = new SysMLEffect(contextBlock, onHTTPRequestEventEffectActivity, "onHTTPRequest");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, "InitializingToOperational");
		operationalOnHTTPRequestTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(HTTPRequestEvent.class), Optional.empty(), Optional.of(onHTTPRequestEventEffect), "OperationalOnHttpRequest",
			SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(contextBlock, operationalState, finalState, "OperationalToFinal");
	}
}