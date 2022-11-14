package c4s2.components.services.operator;

import static c4s2.common.valueTypes.ServiceStatesEnum.Final;
import static c4s2.common.valueTypes.ServiceStatesEnum.Initial;
import static c4s2.common.valueTypes.ServiceStatesEnum.Initializing;
import static c4s2.common.valueTypes.ServiceStatesEnum.Operational;
import java.util.Optional;
import c4s2.common.messages.OperatorServiceMonitorMessage;
import c4s2.common.messages.RadarControlMessage;
import c4s2.common.messages.StrikeControlMessage;
import c4s2.common.messages.SystemControlMessage;
import c4s2.common.messages.TargetControlMessage;
import c4s2.common.objects.information.OperatorRadarControlView;
import c4s2.common.objects.information.OperatorRadarMonitorView;
import c4s2.common.objects.information.OperatorServiceControl;
import c4s2.common.objects.information.OperatorServiceMonitor;
import c4s2.common.objects.information.OperatorStrikeControlView;
import c4s2.common.objects.information.OperatorStrikeMonitorView;
import c4s2.common.objects.information.OperatorSystemControlView;
import c4s2.common.objects.information.OperatorSystemMonitorView;
import c4s2.common.objects.information.OperatorTargetControlView;
import c4s2.common.objects.information.OperatorTargetMonitorView;
import c4s2.common.objects.information.RadarMonitor;
import c4s2.common.objects.information.StrikeMonitor;
import c4s2.common.objects.information.SystemMonitor;
import c4s2.common.objects.information.TargetMonitor;
import c4s2.common.ports.information.C4S2MessagingProtocol;
import c4s2.common.ports.information.OperatorControlViewReceiveProtocol;
import c4s2.common.ports.information.OperatorMonitorViewProtocolConjugate;
import c4s2.common.valueTypes.ServiceStatesEnum;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.InstantMilliseconds;

/**
 * The {@code OperatorServices} is the SysMLinJava model of the operator
 * services component of the {@code C4S2System}. It is an extension of the
 * {@code SysMLBlock} that provides displays and services to the
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
public class OperatorServices extends SysMLBlock
{
	/**
	 * Port representing the messaging protocol used to communicate with other
	 * services
	 */
	@FullPort
	public C4S2MessagingProtocol messaging;
	/**
	 * Port representing the operator's display of monitor data received from other
	 * services
	 */
	@FullPort
	public OperatorMonitorViewProtocolConjugate monitorView;
	/**
	 * Port representing the operator's display of control data to be transmitted to
	 * other services
	 */
	@FullPort
	public OperatorControlViewReceiveProtocol controlView;

	/**
	 * Value for the current control data for this service
	 */
	@Value
	private OperatorServiceControl currentControl;
	/**
	 * Value for the current monitor data of this service
	 */
	@Value
	private OperatorServiceMonitor currentMonitor;

	public OperatorServices()
	{
		super("OperatorServices", 0L);
	}

	/**
	 * Reaction to the receipt of system control data from the operator display
	 * 
	 * @param controlView the view containing the control data
	 */
	@Reception
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
	@Reception
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
	@Reception
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
	@Reception
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
	@Reception
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
	@Reception
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
	@Reception
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
	@Reception
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
	@Reception
	public void onOperatorServiceControl(OperatorServiceControl control)
	{
		logger.info(control.toString());
		currentControl = control;
		switch (currentControl.state.ordinal)
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
	protected void createValues()
	{
		super.createValues();
		currentControl = new OperatorServiceControl(ServiceStatesEnum.initial, InstantMilliseconds.now());
		currentMonitor = new OperatorServiceMonitor(ServiceStatesEnum.initial, InstantMilliseconds.now());
	}

	@Override
	protected void createFullPorts()
	{
		super.createFullPorts();
		messaging = new C4S2MessagingProtocol(this, 0L, "OperatorServicesMessaging");
		monitorView = new OperatorMonitorViewProtocolConjugate(this, 0L);
		controlView = new OperatorControlViewReceiveProtocol(this, 0L);
	}
}
