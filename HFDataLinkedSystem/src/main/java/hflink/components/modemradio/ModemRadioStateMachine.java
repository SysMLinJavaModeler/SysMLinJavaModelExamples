package hflink.components.modemradio;

import java.util.Optional;
import hflink.common.events.DataLinkFrameEvent;
import hflink.common.events.GPSMessageEvent;
import hflink.common.events.IPPacketEvent;
import hflink.common.objects.DataLinkFrame;
import hflink.common.objects.GPSMessage;
import hflink.common.objects.IPPacket;
import sysmlinjava.analysis.statetransitions.StateTransitionTablesTransmitter;
import sysmlinjava.analysis.statetransitions.StateTransitionsDisplay;
import sysmlinjava.analysis.statetransitionstransmitters.StateTransitionsTransmitters;
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
 * State machine for the Modem-Radio model/simulation. The state machine
 * consists of states to initialize and operate, with key transitions being
 * those that occur upon receipt of IP Packets from either the ethernet or HF
 * data-link interfaces, or upon receipt of the GPS time message from the GPS
 * interface. See the model of the state machine below for more detail.
 * 
 * @author ModelerOne
 *
 */
public class ModemRadioStateMachine extends SysMLStateMachine
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
	private SysMLTransition operationalOnEthernetIPPacketTransition;
	@Transition
	private SysMLTransition operationalOnDataLinkFrameTransition;
	@Transition
	private SysMLTransition operationalOnGPSMessageTransition;
	@Transition
	private FinalTransition operationalToFinalTransition;

	@EffectActivity
	private SysMLEffectActivity onEthernetIPPacketEventEffectActivity;
	@EffectActivity
	private SysMLEffectActivity onDataLinkFrameEventEffectActivity;
	@EffectActivity
	private SysMLEffectActivity onGPSMessageEventEffectActivity;

	@Effect
	private SysMLEffect onEthernetIPPacketEventEffect;
	@Effect
	private SysMLEffect onDataLinkFrameEventEffect;
	@Effect
	private SysMLEffect onGPSMessageEventEffect;

	public ModemRadioStateMachine(ModemRadio modemRadio)
	{
		super(Optional.of(modemRadio), true, "ModemRadioStateMachine");
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
		onEthernetIPPacketEventEffectActivity = (event, contextBlock) ->
		{
			IPPacket ipPacket = ((IPPacketEvent)event.get()).getPacket();
			ModemRadio modemRadio = (ModemRadio)contextBlock.get();
			modemRadio.processIPPacketFromEthernet(ipPacket);
		};
		onDataLinkFrameEventEffectActivity = (event, contextBlock) ->
		{
			DataLinkFrame datalinkFrame = ((DataLinkFrameEvent)event.get()).getFrame();
			ModemRadio modemRadio = (ModemRadio)contextBlock.get();
			modemRadio.processIPPacketFromDataLink(datalinkFrame);
		};

		onGPSMessageEventEffectActivity = (event, contextBlock) ->
		{
			GPSMessage gpsMessage = ((GPSMessageEvent)event.get()).getMessage();
			ModemRadio modemRadio = (ModemRadio)contextBlock.get();
			modemRadio.processTDMASlotTimeFromGPS(gpsMessage);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onGPSMessageEventEffect = new SysMLEffect(contextBlock, onGPSMessageEventEffectActivity, "onGPSMessage");
		onEthernetIPPacketEventEffect = new SysMLEffect(contextBlock, onEthernetIPPacketEventEffectActivity, "onEthernetIPPacket");
		onDataLinkFrameEventEffect = new SysMLEffect(contextBlock, onDataLinkFrameEventEffectActivity, "onDataLinkFrame");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(contextBlock, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(contextBlock, initializingState, operationalState, "InitializingToOperational");
		operationalOnEthernetIPPacketTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(IPPacketEvent.class), Optional.empty(), Optional.of(onEthernetIPPacketEventEffect),
			"OperationalOnEthernetIPPacket", SysMLTransitionKind.internal);
		operationalOnGPSMessageTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(GPSMessageEvent.class), Optional.empty(), Optional.of(onGPSMessageEventEffect), "OperationalOnGPSMessage",
			SysMLTransitionKind.internal);
		operationalOnDataLinkFrameTransition = new SysMLTransition(contextBlock, operationalState, operationalState, Optional.of(DataLinkFrameEvent.class), Optional.empty(), Optional.of(onDataLinkFrameEventEffect),
			"OperationalOnDataLinkFrame", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(contextBlock, operationalState, finalState, "OperationalToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.empty()));
	}
}
