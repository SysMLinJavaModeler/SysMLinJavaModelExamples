package hflink.components.modemradio;

import java.util.Optional;

import hflink.common.items.DataLinkFrame;
import hflink.common.items.GPSMessage;
import hflink.common.items.IPPacket;
import hflink.common.signals.DataLinkFrameSignal;
import hflink.common.signals.GPSMessageSignal;
import hflink.common.signals.IPPacketSignal;
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
import sysmlinjava.views.statetransitionstables.StateTransitionTablesTransmitter;
import sysmlinjava.views.statetransitionstables.StateTransitionsDisplay;
import sysmlinjava.views.statetransitionstables.StateTransitionsTransmitters;

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
@SuppressWarnings({"javadoc", "unused"})
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

	@GuardCondition
	private SysMLGuardCondition isEthernetIPPacketGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isDataLinkFrameGuardCondition;
	@GuardCondition
	private SysMLGuardCondition isGPSMessageGuardCondition;

	@Guard
	private SysMLGuard isEthernetIPPacketGuard;
	@Guard
	private SysMLGuard isDataLinkFrameGuard;
	@Guard
	private SysMLGuard isGPSMessageGuard;

	@EffectActionFunction
	private SysMLEffectActionFunction onEthernetIPPacketEventEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction onDataLinkFrameEventEffectAction;
	@EffectActionFunction
	private SysMLEffectActionFunction onGPSMessageEventEffectAction;

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
		initializingState = new SysMLState(context, "Initializing");
		operationalState = new SysMLState(context, "Operational");
	}

	@Override
	protected void createGuardConditions()
	{
		isDataLinkFrameGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent signalEvent &&
				signalEvent.signal instanceof DataLinkFrameSignal;
		};
		isEthernetIPPacketGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent signalEvent &&
				signalEvent.signal instanceof IPPacketSignal;
		};
		isGPSMessageGuardCondition = (event, contextBlock) ->
		{
			return event.isPresent() &&
				event.get() instanceof SysMLSignalEvent signalEvent &&
				signalEvent.signal instanceof GPSMessageSignal;
		};
	}

	@Override
	protected void createGuards()
	{
		isDataLinkFrameGuard = new  SysMLGuard(context, isDataLinkFrameGuardCondition, "isDataLinkFrame");
		isEthernetIPPacketGuard = new SysMLGuard(context, isEthernetIPPacketGuardCondition, "EthernetIPPacket");
		isGPSMessageGuard = new SysMLGuard(context, isGPSMessageGuardCondition, "isGPSMessage");
	}

	@Override
	protected void createEffectActionFunctions()
	{
		super.createEffectActionFunctions();
		onEthernetIPPacketEventEffectAction = (event, contextBlock) ->
		{
			IPPacket ipPacket = ((IPPacketSignal)((SysMLSignalEvent)event.get()).signal).packet;
			ModemRadio modemRadio = (ModemRadio)contextBlock.get();
			modemRadio.processIPPacketFromEthernet(ipPacket);
		};
		onDataLinkFrameEventEffectAction = (event, contextBlock) ->
		{
			DataLinkFrame datalinkFrame = ((DataLinkFrameSignal)((SysMLSignalEvent)event.get()).signal).frame;
			ModemRadio modemRadio = (ModemRadio)contextBlock.get();
			modemRadio.processIPPacketFromDataLink(datalinkFrame);
		};

		onGPSMessageEventEffectAction = (event, contextBlock) ->
		{
			GPSMessage gpsMessage = ((GPSMessageSignal)((SysMLSignalEvent)event.get()).signal).message;
			ModemRadio modemRadio = (ModemRadio)contextBlock.get();
			modemRadio.processTDMASlotTimeFromGPS(gpsMessage);
		};
	}

	@Override
	protected void createEffects()
	{
		super.createEffects();
		onGPSMessageEventEffect = new SysMLEffect(context, onGPSMessageEventEffectAction, "onGPSMessage");
		onEthernetIPPacketEventEffect = new SysMLEffect(context, onEthernetIPPacketEventEffectAction, "onEthernetIPPacket");
		onDataLinkFrameEventEffect = new SysMLEffect(context, onDataLinkFrameEventEffectAction, "onDataLinkFrame");
	}

	@Override
	protected void createTransitions()
	{
		initialToInitializingTransition = new InitialTransition(context, initialState, initializingState, "IntialToInitializing");
		initializingToOperationalTransition = new SysMLTransition(context, initializingState, operationalState, "InitializingToOperational");
		operationalOnEthernetIPPacketTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isEthernetIPPacketGuard), Optional.of(onEthernetIPPacketEventEffect),
			"OperationalOnEthernetIPPacket", SysMLTransitionKind.internal);
		operationalOnGPSMessageTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isGPSMessageGuard), Optional.of(onGPSMessageEventEffect), "OperationalOnGPSMessage",
			SysMLTransitionKind.internal);
		operationalOnDataLinkFrameTransition = new SysMLTransition(context, operationalState, operationalState, Optional.of(SysMLSignalEvent.class), Optional.of(isDataLinkFrameGuard), Optional.of(onDataLinkFrameEventEffect),
			"OperationalOnDataLinkFrame", SysMLTransitionKind.internal);
		operationalToFinalTransition = new FinalTransition(context, operationalState, finalState, "OperationalToFinal");
	}

	@Override
	protected void createTransitionsUtility()
	{
		transitionsUtility = Optional.of(new StateTransitionsTransmitters(Optional.of(new StateTransitionTablesTransmitter(StateTransitionsDisplay.udpPort, false)), Optional.empty(), false));
	}
}
