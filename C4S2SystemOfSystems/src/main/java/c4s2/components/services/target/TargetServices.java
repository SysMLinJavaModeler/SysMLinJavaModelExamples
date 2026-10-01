package c4s2.components.services.target;

import static c4s2.common.attributetypes.ServiceStatesEnum.Final;
import static c4s2.common.attributetypes.ServiceStatesEnum.Initial;
import static c4s2.common.attributetypes.ServiceStatesEnum.Initializing;
import static c4s2.common.attributetypes.ServiceStatesEnum.Operational;
import static c4s2.common.attributetypes.TargetDevelopmentAlgorithmsEnum.complex;
import static c4s2.common.attributetypes.TargetDevelopmentAlgorithmsEnum.difficult;
import static c4s2.common.attributetypes.TargetDevelopmentAlgorithmsEnum.simple;

import java.util.Optional;

import c4s2.common.attributetypes.ServiceStatesEnum;
import c4s2.common.attributetypes.TargetDevelopmentAlgorithmsEnum;
import c4s2.common.items.information.RadarMonitor;
import c4s2.common.items.information.TargetControl;
import c4s2.common.items.information.TargetMonitor;
import c4s2.common.items.information.TargetServiceControl;
import c4s2.common.items.information.TargetServiceMonitor;
import c4s2.common.items.information.Waypoint;
import c4s2.common.messages.TargetMonitorMessage;
import c4s2.common.messages.TargetServiceMonitorMessage;
import c4s2.common.ports.information.C4S2MessagingProtocol;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.PointGeospatial;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.items.ItemIn;
import sysmlinjava.javaannotations.items.ItemOut;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalEvent;

/**
 * The {@code TargetServices} is the SysMLinJava model of the tracking/targeting
 * services component of the {@code C4S2System}. It is an extension of the
 * {@code SysMLPart} that provides services to execute, monitor, and control
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
public class TargetServices extends SysMLPart
{
	@Port
	public C4S2MessagingProtocol messaging;

	@Part
	public TargetMonitorDatabase targetMonitorDatabase;

	@ItemIn
	public TargetControl currentTargetControl;
	@ItemOut
	public TargetMonitor currentTargetMonitor;
	@ItemIn
	public TargetServiceControl currentServiceControl;
	@ItemOut
	public TargetServiceMonitor currentServiceMonitor;

	public TargetServices()
	{
		super("TargetServices", 0L);
	}

	@Action
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

	@Action
	public void onTargetControl(TargetControl control)
	{
		logger.info(control.toString());
		currentTargetControl = control;
		currentTargetMonitor.algorithm = currentTargetControl.algorithm;
		currentTargetMonitor.time = InstantMilliseconds.now();
		publishTargetMonitor();
	}

	@Action
	public void onTargetServiceControl(TargetServiceControl serviceControl)
	{
		logger.info(serviceControl.toString());
		currentServiceControl = serviceControl;
		switch (currentServiceControl.state.ordinal())
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

	@Action
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

	@Action
	private void projectFutureTrack()
	{
		switch (currentTargetMonitor.algorithm.ordinal())
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

	@Action
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
	protected void createAttributes()
	{
		super.createAttributes();
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
	protected void createPorts()
	{
		messaging = new C4S2MessagingProtocol(this, 0L, "TargetServicesMessaging");
	}
}
