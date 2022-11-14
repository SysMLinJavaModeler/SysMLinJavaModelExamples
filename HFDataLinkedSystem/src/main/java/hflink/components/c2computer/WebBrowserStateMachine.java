package hflink.components.c2computer;

import java.util.Optional;
import hflink.common.events.ApplicationUIControlEvent;
import hflink.common.events.HTTPResponseEvent;
import hflink.common.objects.ApplicationUIControl;
import hflink.common.objects.HTTPResponse;
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

	@EffectActivity
	private SysMLEffectActivity onApplicationUIControlEventEffectActivity;
	@EffectActivity
	private SysMLEffectActivity onHTTPResponseEventEffectActivity;
	@Effect
	private SysMLEffect onHTTPResponseEventEffect;
	@Effect
	private SysMLEffect onApplicationUIControlEventEffect;

	public WebBrowserStateMachine(WebBrowser webBrowser)
	{
		super(Optional.of(webBrowser), true, "WebBrowserStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(contextBlock, "Initializing");
		operationalState = new SysMLState(contextBlock, "Operational");
	}

	@Override
	protected void createEffectActivities()
	{
		onHTTPResponseEventEffectActivity = (event, contextBlock) ->
		{
			HTTPResponse response = ((HTTPResponseEvent)event.get()).getResponse();
			WebBrowser webBrowser = (WebBrowser)contextBlock.get();
			webBrowser.onHTTPResponse(response);
		};

		onApplicationUIControlEventEffectActivity = (event, contextBlock) ->
		{
			ApplicationUIControl control = ((ApplicationUIControlEvent)event.get()).getControl();
			WebBrowser webBrowser = (WebBrowser)contextBlock.get();
			webBrowser.onApplicationUIControl(control);
		};
	}

	@Override
	protected void createEffects()
	{
		onHTTPResponseEventEffect = new SysMLEffect(contextBlock, onHTTPResponseEventEffectActivity, "onHTTPResponse");
		onApplicationUIControlEventEffect = new SysMLEffect(contextBlock, onApplicationUIControlEventEffectActivity, "onApplicationUIControl");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, "InitializingToOperational");
		operationalOnHTTPResponseTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(HTTPResponseEvent.class), Optional.empty(), Optional.of(onHTTPResponseEventEffect), "OperationalOnHTTPResponse",
			SysMLTransitionKind.internal);
		operationalOnApplicationUIControlTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(ApplicationUIControlEvent.class), Optional.empty(), Optional.of(onApplicationUIControlEventEffect),
			"OperationalOnApplicationUIControl", SysMLTransitionKind.external);
		operationalToFinalTransition = new FinalTransition(contextBlock, operationalState, finalState, "OperationalToFinal");
	}
}