package hflink.components.switchrouter;

import java.util.Optional;

import hflink.common.items.IPPacket;
import hflink.common.signals.IPPacketSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.statemachines.Effect;
import sysmlinjava.javaannotations.statemachines.EffectActionFunction;
import sysmlinjava.javaannotations.statemachines.Guard;
import sysmlinjava.javaannotations.statemachines.GuardCondition;
import sysmlinjava.javaannotations.statemachines.State;
import sysmlinjava.javaannotations.statemachines.Transition;
import sysmlinjava.states.FinalEvent;
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
 * State machine for the Ethernet Switch/IP Router model/simulation. The state
 * machine consists of states to initialize and operate, with the key transition
 * being the one that occurs upon receipt of an IP Packet to be routed to the
 * applicable ethernet port. See the model of the state machine below for more
 * detail.
 * 
 * @author ModelerOne
 *
 */
@SuppressWarnings({"javadoc", "unused"})
public class EthernetSwitchIPRouterStateMachine extends SysMLStateMachine
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
	private SysMLTransition operationalOnIPPacketTransition;
	@Transition
	private SysMLTransition operationalToFinalTransition;

	@GuardCondition
	private SysMLGuardCondition isIPPacketGuardCondition;

	@Guard
	private SysMLGuard isIPPacketGuard;

	@EffectActionFunction
	private SysMLEffectActionFunction onIPPacketEventEffectAction;

	@Effect
	private SysMLEffect onIPPacketEventEffect;

	public EthernetSwitchIPRouterStateMachine(EthernetSwitchIPRouter ethernetSwitch)
	{
		super(Optional.of(ethernetSwitch), true, "EthernetSwitchIPRouterStateMachine");
	}

	@Override
	protected void createStates()
	{
		super.createStates();
		initializingState = new SysMLState(context, "Initializing");
		operationalState = new SysMLState(context, "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		isIPPacketGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent signalEvent &&
				signalEvent.signal instanceof IPPacketSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isIPPacketGuard = new SysMLGuard(context, isIPPacketGuardCondition, "isIPPacket");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		onIPPacketEventEffectAction = (event, contextBlock) ->
		{
			if (event.get() instanceof SysMLSignalEvent signalEvent)
			{
				IPPacket packet = ((IPPacketSignal)signalEvent.signal).packet;
				EthernetSwitchIPRouter switchRouter = (EthernetSwitchIPRouter)contextBlock.get();
				switchRouter.routeIPPacket(packet);
			}
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		this.onIPPacketEventEffect = new SysMLEffect(context, onIPPacketEventEffectAction, "onIPPacket");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "InitialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnIPPacketTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isIPPacketGuard), Optional.of(onIPPacketEventEffect), "OperationalOnIPPacket",
			SysMLTransitionKind.internal);
		operationalToFinalTransition = new SysMLTransition(context, operationalState, finalState, Optional.of(FinalEvent.class), Optional.empty(), Optional.empty(), "OperationalToFinal", SysMLTransitionKind.external);
	}
}
