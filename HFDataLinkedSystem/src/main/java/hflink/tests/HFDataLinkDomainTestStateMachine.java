package hflink.tests;

import java.util.Optional;
import hflink.common.events.ApplicationUIViewEvent;
import hflink.common.objects.ApplicationUIView;
import sysmlinjava.annotations.statemachines.Effect;
import sysmlinjava.annotations.statemachines.EffectActivity;
import sysmlinjava.annotations.statemachines.State;
import sysmlinjava.annotations.statemachines.Transition;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.statemachine.FinalTransition;
import sysmlinjava.statemachine.InitialTransition;
import sysmlinjava.statemachine.SysMLEffect;
import sysmlinjava.statemachine.SysMLEffectActivity;
import sysmlinjava.statemachine.SysMLState;
import sysmlinjava.statemachine.SysMLStateMachine;
import sysmlinjava.statemachine.SysMLTransition;
import sysmlinjava.statemachine.SysMLTransitionKind;

/**
 * State machine for the {@code HFDataLinkDomainTest}. The state machine consists of
 * states to initialize, operate, and terminate with the key transition being
 * the one that occurs upon receipt of a new Application user-interface view -
 * the HTML of an HTTP response from the web servers on the
 * {@code DeployedSystem}s. See the model of the state machine below for more
 * detail.
 * 
 * @author ModelerOne
 *
 */
public class HFDataLinkDomainTestStateMachine extends SysMLStateMachine
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
	private SysMLTransition operationalOnApplicationUIViewTransition;
	@Transition
	private SysMLTransition operationalToFinalTransition;

	@EffectActivity
	private SysMLEffectActivity onApplicationUIViewEventEffectActivity;
	@Effect
	private SysMLEffect onApplicationUIViewEventEffect;

	public HFDataLinkDomainTestStateMachine(SysMLBlock contextBlock)
	{
		super(Optional.of(contextBlock), true, "HFDataLinkDomainTestStateMachine");
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
		onApplicationUIViewEventEffectActivity = (event, contextBlock) ->
		{
			if (event.get() instanceof ApplicationUIViewEvent)
			{
				ApplicationUIView view = ((ApplicationUIViewEvent)event.get()).getView();
				HFDataLinkDomainTest test = (HFDataLinkDomainTest)contextBlock.get();
				test.onApplicationUIView(view);
			}
			else
				logger.warning("unexpected event type: " + event.get().getClass().getSimpleName());
		};
	}

	@Override
	protected void createEffects()
	{
		onApplicationUIViewEventEffect = new SysMLEffect(contextBlock, onApplicationUIViewEventEffectActivity, "onApplicationUIView");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, "InitializingToOperational");
		operationalOnApplicationUIViewTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(ApplicationUIViewEvent.class), Optional.empty(), Optional.of(onApplicationUIViewEventEffect),
			"OperationalOnApplicationUIView", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(contextBlock, operationalState, finalState, "OperationalToFinal");
	}
}