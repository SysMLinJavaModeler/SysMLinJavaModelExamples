package hflink.tests;

import java.util.Optional;

import hflink.common.items.ApplicationUIView;
import hflink.common.signals.ApplicationUIViewSignal;
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

	@GuardCondition
	private SysMLGuardCondition isApplicationUIViewGuardCondition;
	
	@Guard
	private SysMLGuard isApplicationUIViewGuard;

	@EffectActionFunction
	private SysMLEffectActionFunction onApplicationUIViewEventEffectAction;
	
	@Effect
	private SysMLEffect onApplicationUIViewEventEffect;

	@SuppressWarnings("javadoc")
	public HFDataLinkDomainTestStateMachine(HFDataLinkDomainVerificationCase hfDataLinkDomainTest)
	{
		super(Optional.of(hfDataLinkDomainTest), true, "HFDataLinkDomainTestStateMachine");
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
		isApplicationUIViewGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent signalEvent &&
				signalEvent.signal instanceof ApplicationUIViewSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isApplicationUIViewGuard = new SysMLGuard(context, isApplicationUIViewGuardCondition, "isIPPacket");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		onApplicationUIViewEventEffectAction = (event, contextBlock) ->
		{
			if (event.get() instanceof SysMLSignalEvent signalEvent)
			{
				ApplicationUIView view = ((ApplicationUIViewSignal)signalEvent.signal).view;
				HFDataLinkDomainVerificationCase test = (HFDataLinkDomainVerificationCase)contextBlock.get();
				test.onApplicationUIView(view);
			}
			else
				logger.warning("unexpected event type: " + event.get().getClass().getSimpleName());
		};
	}

	@Override
	protected void createEffects()
	{
		onApplicationUIViewEventEffect = new SysMLEffect(context, onApplicationUIViewEventEffectAction, "onApplicationUIView");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnApplicationUIViewTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isApplicationUIViewGuard), Optional.of(onApplicationUIViewEventEffect),
			"OperationalOnApplicationUIView", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}
}