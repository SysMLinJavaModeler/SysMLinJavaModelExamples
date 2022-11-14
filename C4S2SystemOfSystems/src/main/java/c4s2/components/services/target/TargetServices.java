package c4s2.components.services.target;

import java.util.Optional;
import c4s2.common.messages.TargetMonitorMessage;
import c4s2.common.messages.TargetServiceMonitorMessage;
import c4s2.common.objects.information.RadarMonitor;
import c4s2.common.objects.information.TargetControl;
import c4s2.common.objects.information.TargetMonitor;
import c4s2.common.objects.information.TargetServiceControl;
import c4s2.common.objects.information.TargetServiceMonitor;
import c4s2.common.objects.information.Waypoint;
import c4s2.common.ports.information.C4S2MessagingProtocol;
import c4s2.common.valueTypes.ServiceStatesEnum;
import c4s2.common.valueTypes.TargetDevelopmentAlgorithmsEnum;
import static c4s2.common.valueTypes.ServiceStatesEnum.*;
import static c4s2.common.valueTypes.TargetDevelopmentAlgorithmsEnum.*;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.PointGeospatial;

/**
 * The {@code TargetServices} is the SysMLinJava model of the tracking/targeting
 * services component of the {@code C4S2System}. It is an extension of the
 * {@code SysMLBlock} that provides services to execute, monitor, and control
 * the tracking and targeting algorithms that are a part of the service. The
 * current version of the {@code TargetServices} performs simple tracking and
 * targeting algorithms based on monitoring data received from the radar system
 * via the {@code RadarServices}.
 * <p>
 * The {@code TargetServices} includes a port for the messaging protocol used to
 * communicate via messages with other systems and services. It also has a part
 * for a database of target monitor data, i.e. the target tracks, as well as
 * values for the current (latest) monitor and control data is has received or
 * transmitted.
 * <p>
 * Receptions provided by the service include reaction to the receipt of radar
 * monitor data from the {@code RadarService}, reaction to the receipt of target
 * control data from the {@code C4S2Operator}, and service control data from the
 * {@code SystemServices}.
 * 
 * @author ModelerOne
 *
 */
public class TargetServices extends SysMLBlock
{
	@FullPort
	public C4S2MessagingProtocol messaging;

	@Part
	public TargetMonitorDatabase targetMonitorDatabase;

	@Value
	public TargetControl currentTargetControl;
	@Value
	public TargetMonitor currentTargetMonitor;
	@Value
	public TargetServiceControl currentServiceControl;
	@Value
	public TargetServiceMonitor currentServiceMonitor;

	public TargetServices()
	{
		super("TargetServices", 0L);
	}

	@Reception
	public void onRadarMonitor(RadarMonitor radarMonitor)
	{
		logger.info(radarMonitor.toString());
		if (radarMonitor.radarSignalReturn.isPresent())
		{
			updateTargetMonitors(radarMonitor);
			projectFutureTrack();
			publishTargetMonitor();
		}
	}

	@Reception
	public void onTargetControl(TargetControl control)
	{
		logger.info(control.toString());
		currentTargetControl = control;
		currentTargetMonitor.algorithm = currentTargetControl.algorithm;
		currentTargetMonitor.time = InstantMilliseconds.now();
		publishTargetMonitor();
	}

	@Reception
	public void onTargetServiceControl(TargetServiceControl serviceControl)
	{
		logger.info(serviceControl.toString());
		currentServiceControl = serviceControl;
		switch (currentServiceControl.state.ordinal)
		{
		case Initial:
			logger.severe("unable to transition to Initial state from current state");
			break;
		case Initializing:
			logger.severe("unable to transition to Initialization state from current state");
			break;
		case Operational:
			currentServiceMonitor.state = currentServiceControl.state;
			currentServiceMonitor.time = InstantMilliseconds.now();
			messaging.transmit(new TargetServiceMonitorMessage(new TargetServiceMonitor(currentServiceMonitor)));
			break;
		case Final:
			currentServiceMonitor.state = currentServiceControl.state;
			currentServiceMonitor.time = InstantMilliseconds.now();
			messaging.transmit(new TargetServiceMonitorMessage(new TargetServiceMonitor(currentServiceMonitor)));
			acceptEvent(new FinalEvent());
			break;
		default:
			logger.severe("unrecognized or invalid state in control");
		}
	}

	@Operation
	private void updateTargetMonitors(RadarMonitor radarMonitor)
	{
		if (targetMonitorDatabase.monitors.isEmpty())
			targetMonitorDatabase.monitors.add(currentTargetMonitor);
		else
			currentTargetMonitor = targetMonitorDatabase.monitors.get(0); // Initial capability of only one target
		currentTargetMonitor.signature = radarMonitor.radarSignalReturn.get().signature;
		currentTargetMonitor.time.value = radarMonitor.time.value;
		InstantMilliseconds time = radarMonitor.radarSignalTransmission.get().scanStartTime;
		PointGeospatial location = radarMonitor.radarSignalReturn.get().position;
		Waypoint waypoint = new Waypoint(time, location);
		currentTargetMonitor.pastWaypoints.add(waypoint);
	}

	@Operation
	private void projectFutureTrack()
	{
		switch (currentTargetMonitor.algorithm.ordinal)
		{
		case complex:
			currentTargetMonitor.projectComplexFutureTrack();
			break;
		case difficult:
			currentTargetMonitor.projectDifficultFutureTrack();
			break;
		case simple:
			currentTargetMonitor.projectSimpleFutureTrack();
			break;
		default:
			logger.severe("unrecognized algorithm type " + currentTargetMonitor.algorithm.toString());
			break;
		}
	}

	@Operation
	private void publishTargetMonitor()
	{
		messaging.transmit(new TargetMonitorMessage(new TargetMonitor(currentTargetMonitor)));
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new TargetServicesStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		currentTargetMonitor = new TargetMonitor();
		currentTargetControl = new TargetControl(TargetDevelopmentAlgorithmsEnum.Simple, InstantMilliseconds.now());
		currentServiceControl = new TargetServiceControl(ServiceStatesEnum.initial, InstantMilliseconds.now());
		currentServiceMonitor = new TargetServiceMonitor(ServiceStatesEnum.initial, InstantMilliseconds.now());
	}

	@Override
	protected void createParts()
	{
		super.createParts();
		targetMonitorDatabase = new TargetMonitorDatabase();
	}

	@Override
	protected void createFullPorts()
	{
		messaging = new C4S2MessagingProtocol(this, 0L, "TargetServicesMessaging");
	}
}
