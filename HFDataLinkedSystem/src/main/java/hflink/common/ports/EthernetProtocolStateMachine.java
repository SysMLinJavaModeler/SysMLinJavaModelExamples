package hflink.common.ports;

import java.util.Optional;
import hflink.common.events.EthernetPacketEvent;
import hflink.common.objects.EthernetPacket;
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
 * State machine for the ethernet port enabling the protocol to operate
 * asynchronously from the rest of the protocol stack. States include an
 * initialization and operations with the most significant transition being the
 * one that occurs upon arrival of a signal event for a received ethernet
 * packet. See the state machine declaration below for details.
 * 
 * @author ModelerOne
 *
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

	@EffectActivity
	private SysMLEffectActivity onEthernetPacketEvent;
	@Effect
	private SysMLEffect onEthernetPacketEventEffect;

	public EthernetProtocolStateMachine(EthernetProtocol ethernetPort)
	{
		super(Optional.of(ethernetPort), true, "EthernetPortStateMachine");
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
		super.createEffectActivities();
		onEthernetPacketEvent = (event, contextBlock) ->
		{
			if (event.get() instanceof EthernetPacketEvent)
			{
				EthernetPacket packet = ((EthernetPacketEvent)event.get()).getPacket();
				EthernetProtocol ethernetPort = (EthernetProtocol)contextBlock.get();
				ethernetPort.onEthernetPacketReceived(packet);
			}
			else
				logger.warning("unexpected event type: " + event.get().getClass().getSimpleName());
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onEthernetPacketEventEffect = new SysMLEffect(contextBlock, onEthernetPacketEvent, "onEthernetPacket");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, "InitializingToOperational");
		operationalOnEthernetPacketTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(EthernetPacketEvent.class), Optional.empty(), Optional.of(onEthernetPacketEventEffect),
			"OperationalOnEthernetPacket", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(contextBlock, operationalState, finalState, "OperationalToFinal");
	}

}