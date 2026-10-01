package c4s2.components.services.operator;

import static c4s2.common.attributetypes.ServiceStatesEnum.Final;
import static c4s2.common.attributetypes.ServiceStatesEnum.Initial;
import static c4s2.common.attributetypes.ServiceStatesEnum.Initializing;
import static c4s2.common.attributetypes.ServiceStatesEnum.Operational;

import java.util.Optional;

import c4s2.common.attributetypes.ServiceStatesEnum;
import c4s2.common.items.information.OperatorRadarControlView;
import c4s2.common.items.information.OperatorRadarMonitorView;
import c4s2.common.items.information.OperatorServiceControl;
import c4s2.common.items.information.OperatorServiceMonitor;
import c4s2.common.items.information.OperatorStrikeControlView;
import c4s2.common.items.information.OperatorStrikeMonitorView;
import c4s2.common.items.information.OperatorSystemControlView;
import c4s2.common.items.information.OperatorSystemMonitorView;
import c4s2.common.items.information.OperatorTargetControlView;
import c4s2.common.items.information.OperatorTargetMonitorView;
import c4s2.common.items.information.RadarMonitor;
import c4s2.common.items.information.StrikeMonitor;
import c4s2.common.items.information.SystemMonitor;
import c4s2.common.items.information.TargetMonitor;
import c4s2.common.messages.OperatorServiceMonitorMessage;
import c4s2.common.messages.RadarControlMessage;
import c4s2.common.messages.StrikeControlMessage;
import c4s2.common.messages.SystemControlMessage;
import c4s2.common.messages.TargetControlMessage;
import c4s2.common.ports.information.C4S2MessagingProtocol;
import c4s2.common.ports.information.OperatorControlViewReceiveProtocol;
import c4s2.common.ports.information.OperatorMonitorViewTransmitProtocol;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.items.ItemIn;
import sysmlinjava.javaannotations.items.ItemOut;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalEvent;

/**
 * The {@code OperatorServices} is the SysMLinJava model of the operator
 * services component of the {@code C4S2System}. It is an extension of the
 * {@code SysMLPart} that provides displays and services to the
 * {@code C4S2Operator}. The current version of the {@code OperatorServices}
 * performs only simple translation of the operator displays to/from the monitor
 * and control data received from/transmitted to the radar, strike, target, and
 * system services.
 * <p>
 * The {@code OperatorServices} includes a port for the messaging protocol used
 * to communicate via messages with other services. It also has ports for the
 * display (views) of the monitor and control data received from and transmitted
 * to the other services. The operator uses views of monitor data to determine
 * the state of the C4S2 domain, and uses the views of the control data to set
 * control values and thereby control the states of the systems and services he
 * uses to perform the F2T2EA operation.
 * <p>
 * Receptions provided by the service include reactions to the receipt of radar,
 * strike, targeting, and systems monitor data from the other {@code C4S2System}
 * services, and to the receipt of radar, strike, targeting, and systems control
 * data from the {@code C4S2Operator}.
 * 
 * @author ModelerOne
 *
 */
public class OperatorServices extends SysMLPart
{
	/**
	 * Port representing the messaging protocol used to communicate with other
	 * services
	 */
	@Port
	public C4S2MessagingProtocol messaging;
	/**
	 * Port representing the operator's display of monitor data received from other
	 * services
	 */
	@Port
	public OperatorMonitorViewTransmitProtocol monitorView;
	/**
	 * Port representing the operator's display of control data to be transmitted to
	 * other services
	 */
	@Port
	public OperatorControlViewReceiveProtocol controlView;

	/**
	 * Item for the current control data for this service
	 */
	@ItemIn
	private OperatorServiceControl currentControl;
	/**
	 * Item for the current monitor data of this service
	 */
	@ItemOut
	private OperatorServiceMonitor currentMonitor;

	/**
	 * Constructor
	 */
	public OperatorServices()
	{
		super("OperatorServices", 0L);
	}

	/**
	 * Reaction to the receipt of system control data from the operator display
	 * 
	 * @param controlView the view containing the control data
	 */
	@Action
	public void onSystemControlView(OperatorSystemControlView controlView)
	{
		logger.info(controlView.toString());
		SystemControlMessage message = new SystemControlMessage(controlView.control);
		messaging.transmit(message);
	}

	/**
	 * Reaction to the receipt of radar control data from the operator display
	 * 
	 * @param controlView the view containing the control data
	 */
	@Action
	public void onRadarControlView(OperatorRadarControlView controlView)
	{
		logger.info(controlView.toString());
		RadarControlMessage message = new RadarControlMessage(controlView.control);
		messaging.transmit(message);
	}

	/**
	 * Reaction to the receipt of targeting control data from the operator display
	 * 
	 * @param controlView the view containing the control data
	 */
	@Action
	public void onTargetControlView(OperatorTargetControlView controlView)
	{
		logger.info(controlView.toString());
		TargetControlMessage message = new TargetControlMessage(controlView.control);
		messaging.transmit(message);
	}

	/**
	 * Reaction to the receipt of strike control data from the operator display
	 * 
	 * @param controlView the view containing the control data
	 */
	@Action
	public void onStrikeControlView(OperatorStrikeControlView controlView)
	{
		logger.info(controlView.toString());
		StrikeControlMessage message = new StrikeControlMessage(controlView.control);
		messaging.transmit(message);
	}

	/**
	 * Reaction to the receipt of system monitor data from the
	 * {@code SystemServices}
	 * 
	 * @param monitor the system monitor data
	 */
	@Action
	public void onSystemMonitor(SystemMonitor monitor)
	{
		logger.info(monitor.toString());
		OperatorSystemMonitorView view = new OperatorSystemMonitorView(monitor);
		monitorView.transmit(view);
	}

	/**
	 * Reaction to the receipt of radar monitor data from the {@code RadarServices}
	 * 
	 * @param monitor the radar monitor data
	 */
	@Action
	public void onRadarMonitor(RadarMonitor monitor)
	{
		logger.info(monitor.toString());
		OperatorRadarMonitorView view = new OperatorRadarMonitorView(monitor);
		monitorView.transmit(view);
	}

	/**
	 * Reaction to the receipt of targeting monitor data from the
	 * {@code TargetServices}
	 * 
	 * @param monitor the targeting monitor data
	 */
	@Action
	public void onTargetMonitor(TargetMonitor monitor)
	{
		logger.info(monitor.toString());
		OperatorTargetMonitorView view = new OperatorTargetMonitorView(monitor);
		monitorView.transmit(view);
	}

	/**
	 * Reaction to the receipt of strike monitor data from the
	 * {@code StrikeServices}
	 * 
	 * @param monitor the strike monitor data
	 */
	@Action
	public void onStrikeMonitor(StrikeMonitor monitor)
	{
		logger.info(monitor.toString());
		OperatorStrikeMonitorView view = new OperatorStrikeMonitorView(monitor);
		monitorView.transmit(view);
	}

	/**
	 * Reaction to the receipt of service control data from the
	 * {@code SystemServices}. The reception reacts according to the state of the
	 * service, i.e. transitions to the controlled state and/or confirms the state
	 * transition
	 * 
	 * @param control the operator service control data
	 */
	@Action
	public void onOperatorServiceControl(OperatorServiceControl control)
	{
		logger.info(control.toString());
		currentControl = control;
		switch (currentControl.state.ordinal())
		{
		case Initial:
			logger.severe("unable to transition to Initial state from current state");
			break;
		case Initializing:
			logger.severe("unable to transition to Initialization state from current state");
			break;
		case Operational:
			currentMonitor.state = currentControl.state;
			currentMonitor.time = InstantMilliseconds.now();
			messaging.transmit(new OperatorServiceMonitorMessage(new OperatorServiceMonitor(currentMonitor)));
			break;
		case Final:
			currentMonitor.state = currentControl.state;
			currentMonitor.time = InstantMilliseconds.now();
			messaging.transmit(new OperatorServiceMonitorMessage(new OperatorServiceMonitor(currentMonitor)));
			acceptEvent(new FinalEvent());
			break;
		default:
			logger.severe("unrecognized or invalid state in control");
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new OperatorServicesStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		super.createAttributes();
		currentControl = new OperatorServiceControl(ServiceStatesEnum.initial, InstantMilliseconds.now());
		currentMonitor = new OperatorServiceMonitor(ServiceStatesEnum.initial, InstantMilliseconds.now());
	}

	@Override
	protected void createPorts()
	{
		super.createPorts();
		messaging = new C4S2MessagingProtocol(this, 0L, "OperatorServicesMessaging");
		monitorView = new OperatorMonitorViewTransmitProtocol(this, 0L);
		controlView = new OperatorControlViewReceiveProtocol(this, 0L);
	}
}
