package c4s2.components.services.radar;

import static c4s2.common.valueTypes.ServiceStatesEnum.*;
import java.util.Optional;
import c4s2.common.messages.RadarMonitorMessage;
import c4s2.common.messages.RadarServiceMonitorMessage;
import c4s2.common.messages.RadarSystemControlMessage;
import c4s2.common.objects.information.RadarControl;
import c4s2.common.objects.information.RadarMonitor;
import c4s2.common.objects.information.RadarServiceControl;
import c4s2.common.objects.information.RadarServiceMonitor;
import c4s2.common.objects.information.RadarSystemControl;
import c4s2.common.objects.information.RadarSystemMonitor;
import c4s2.common.ports.information.C4S2MessagingProtocol;
import c4s2.common.valueTypes.ServiceStatesEnum;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.InstantMilliseconds;

/**
 * The {@code RadarServices} is the SysMLinJava model of the radar services
 * component of the {@code C4S2System}. It is an extension of the
 * {@code SysMLBlock} that provides services to monitor and control the
 * {@code RadarSystem}. The current version of the {@code RadarServices}
 * performs only simple monitor and control translation between the monitor and
 * control data used by the operator and the monitor and control data used by
 * the radar.
 * <p>
 * The {@code RadarServices} includes a port for the messaging protocol used to
 * communicate via messages with other systems and services. It also has a part
 * for a database of radar monitor and control data and values for the current
 * (latest) monitor and control data is has received or transmitted.
 * <p>
 * Receptions provided by the service include reaction to the receipt of radar
 * monitor data from the {@code RadarSystem}, reaction to the receipt of radar
 * control data from the {@code C4S2Operator}, and service control data from the
 * {@code SystemServices}.
 * 
 * @author ModelerOne
 *
 */
public class RadarServices extends SysMLBlock
{
	/**
	 * Port for sending and receiving messages
	 */
	@FullPort
	public C4S2MessagingProtocol messaging;

	/**
	 * Part for database of radar monitors/controls
	 */
	@Part
	public RadarMonitorAndControlDatabase radarMonitorAndControlDatabase;

	/**
	 * Value for the current radar monitor
	 */
	@Value
	public RadarServiceMonitor currentMonitor;
	/**
	 * Value for the current radar control
	 */
	@Value
	public RadarServiceControl currentControl;

	/**
	 * Constructor
	 */
	public RadarServices()
	{
		super("RadarServices", 0L);
	}

	/**
	 * Reception to handle receipt of new radar system monitor
	 * 
	 * @param monitor new radar system monitor
	 */
	@Reception
	public void onRadarSystemMonitor(RadarSystemMonitor monitor)
	{
		logger.info(monitor.toString());
		radarMonitorAndControlDatabase.add(monitor);
		RadarMonitor radarMonitor = new RadarMonitor(monitor.currentState, monitor.radarSignalTransmission, monitor.radarSignalReturn, monitor.confidence, monitor.time);
		messaging.transmit(new RadarMonitorMessage(radarMonitor));
	}

	/**
	 * Reception to handle receipt of new radar system control
	 * 
	 * @param control new radar system monitor
	 */
	@Reception
	public void onRadarControl(RadarControl control)
	{
		logger.info(control.toString());
		RadarSystemControl systemControl = new RadarSystemControl(control.state, control.transmission, control.time);
		messaging.transmit(new RadarSystemControlMessage(systemControl));
	}

	/**
	 * Reception to handle receipt of new service control
	 * 
	 * @param control new service control
	 */
	@Reception
	public void onRadarServiceControl(RadarServiceControl control)
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
			messaging.transmit(new RadarServiceMonitorMessage(currentMonitor));
			break;
		case Final:
			currentMonitor.state = currentControl.state;
			currentMonitor.time = InstantMilliseconds.now();
			messaging.transmit(new RadarServiceMonitorMessage(currentMonitor));
			acceptEvent(new FinalEvent());
			break;
		default:
			logger.severe("unrecognized or invalid state in control");
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new RadarServicesStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		currentControl = new RadarServiceControl(ServiceStatesEnum.initial, InstantMilliseconds.now());
		currentMonitor = new RadarServiceMonitor(ServiceStatesEnum.initial, InstantMilliseconds.now());
	}

	@Override
	protected void createParts()
	{
		super.createParts();
		radarMonitorAndControlDatabase = new RadarMonitorAndControlDatabase();
	}

	@Override
	protected void createFullPorts()
	{
		messaging = new C4S2MessagingProtocol(this, 0L, "RadarServicesMessaging");
	}
}
