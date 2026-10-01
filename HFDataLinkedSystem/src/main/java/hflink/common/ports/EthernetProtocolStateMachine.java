package hflink.common.ports;

import java.util.Optional;

import hflink.common.signals.EthernetPacketSignal;
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
 * State machine for the ethernet port enabling the protocol to operate
 * asynchronously from the rest of the protocol stack. States include an
 * initialization and operations with the most significant transition being the
 * one that occurs upon arrival of a signal event for a received ethernet
 * packet. See the state machine declaration below for details.
 * 
 * @author ModelerOne
 */
public class EthernetProtocolStateMachine extends SysMLStateMachine
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
	private SysMLTransition operationalOnEthernetPacketTransition;
	@Transition
	private FinalTransition operationalToFinalTransition;

	@GuardCondition
	private SysMLGuardCondition isEthernetPacketGuardCondition;

	@Guard
	private SysMLGuard isEthernetPacketGuard;

	@EffectActionFunction
	private SysMLEffectActionFunction onEthernetPacketEvent;

	@Effect
	private SysMLEffect onEthernetPacketEventEffect;

	/**
	 * Constructor
	 * 
	 * @param ethernetPort port/protocol operating IAW this state machine
	 */
	public EthernetProtocolStateMachine(EthernetProtocol ethernetPort)
	{
		super(Optional.of(ethernetPort), true, "EthernetPortStateMachine");
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
		isEthernetPacketGuardCondition = (event, context) ->
		{
			return event.isPresent() &&
			event.get() instanceof SysMLSignalEvent signalEvent &&
			signalEvent.signal instanceof EthernetPacketSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isEthernetPacketGuard = new SysMLGuard(context, isEthernetPacketGuardCondition, "isEthernetPacketGuard");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		onEthernetPacketEvent = (event, context) ->
		{
			if (event.get() instanceof SysMLSignalEvent signalEvent &&
				signalEvent.signal instanceof EthernetPacketSignal packetSignal &&
				context.get() instanceof EthernetProtocol ethernetPort)
				ethernetPort.onEthernetPacketReceived(packetSignal.packet);
			else
				logger.warning("unexpected event type: " + event.get().getClass().getSimpleName());
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onEthernetPacketEventEffect = new SysMLEffect(context, onEthernetPacketEvent, "onEthernetPacket");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnEthernetPacketTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class),
		Optional.of(isEthernetPacketGuard), Optional.of(onEthernetPacketEventEffect), "OperationalOnEthernetPacket", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}

}