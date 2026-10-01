package c4s2.components.services.strike;

import static c4s2.common.attributetypes.ServiceStatesEnum.Final;
import static c4s2.common.attributetypes.ServiceStatesEnum.Initial;
import static c4s2.common.attributetypes.ServiceStatesEnum.Initializing;
import static c4s2.common.attributetypes.ServiceStatesEnum.Operational;

import java.util.Optional;

import c4s2.common.attributetypes.ServiceStatesEnum;
import c4s2.common.items.information.StrikeControl;
import c4s2.common.items.information.StrikeMonitor;
import c4s2.common.items.information.StrikeServiceControl;
import c4s2.common.items.information.StrikeServiceMonitor;
import c4s2.common.items.information.StrikeSystemControl;
import c4s2.common.items.information.StrikeSystemMonitor;
import c4s2.common.messages.StrikeMonitorMessage;
import c4s2.common.messages.StrikeServiceMonitorMessage;
import c4s2.common.messages.StrikeSystemControlMessage;
import c4s2.common.ports.information.C4S2MessagingProtocol;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.items.ItemIn;
import sysmlinjava.javaannotations.items.ItemOut;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalEvent;

/**
 * The {@code StrikeServices} is the SysMLinJava model of the strike services
 * component of the {@code C4S2System}. It is an extension of the
 * {@code SysMLPart} that provides services to monitor and control the
 * {@code StrikeSystem}. The current version of the {@code StrikeServices}
 * performs only simple monitor and control translation between the monitor and
 * control data used by the operator and the monitor and control data used by
 * the strike (drone, aircraft, other) system.
 * <p>
 * The {@code StrikeServices} includes a port for the messaging protocol used to
 * communicate via messages with other systems and services. It also has a part
 * for a database of strike monitor and control data and values for the current
 * (latest) monitor and control data it has received or transmitted.
 * <p>
 * Receptions provided by the service include reaction to the receipt of strike
 * monitor data from the {@code StrikeSystem}, reaction to the receipt of strike
 * control data from the {@code C4S2Operator}, and service control data from the
 * {@code SystemServices}.
 * 
 * @author ModelerOne
 *
 */
@SuppressWarnings("javadoc")
public class StrikeServices extends SysMLPart
{
	@Port
	public C4S2MessagingProtocol messaging;

	@Part
	public StrikeMonitorAndControlDatabase strikeMonitorAndControlDatabase;

	@ItemIn
	private StrikeServiceControl currentControl;
	@ItemOut
	private StrikeServiceMonitor currentMonitor;

	public StrikeServices()
	{
		super("StrikeServices", 0L);
	}

	@Action
	public void onStrikeSystemMonitor(StrikeSystemMonitor systemMonitor)
	{
		logger.info(systemMonitor.toString());
		StrikeMonitor monitor = new StrikeMonitor(systemMonitor);
		strikeMonitorAndControlDatabase.monitors.add(monitor);
		messaging.transmit(new StrikeMonitorMessage(monitor));
	}

	@Action
	public void onStrikeControl(StrikeControl control)
	{
		logger.info(control.toString());
		strikeMonitorAndControlDatabase.controls.add(control);
		StrikeSystemControl systemControl = new StrikeSystemControl(control.state, control.strikeLocation, control.strikeOrdnance);
		messaging.transmit(new StrikeSystemControlMessage(systemControl));
	}

	@Action
	public void onStrikeServiceControl(StrikeServiceControl control)
	{
		logger.info(control.toString());
		currentControl = control;
		switch (currentControl.state.ordinal())
		{
		case Initial:
			logger.severe("unable to transition to Initial state from current state");
			break;
		case Initializing:
		case Operational:
			currentMonitor.state = currentControl.state;
			currentMonitor.time = InstantMilliseconds.now();
			messaging.transmit(new StrikeServiceMonitorMessage(new StrikeServiceMonitor(currentMonitor)));
			break;
		case Final:
			currentMonitor.state = currentControl.state;
			currentMonitor.time = InstantMilliseconds.now();
			messaging.transmit(new StrikeServiceMonitorMessage(new StrikeServiceMonitor(currentMonitor)));
			acceptEvent(new FinalEvent());
			break;
		default:
			break;
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new StrikeServicesStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		super.createAttributes();
		currentControl = new StrikeServiceControl(ServiceStatesEnum.initial, InstantMilliseconds.now());
		currentMonitor = new StrikeServiceMonitor(ServiceStatesEnum.initial, InstantMilliseconds.now());
	}

	@Override
	protected void createParts()
	{
		super.createParts();
		strikeMonitorAndControlDatabase = new StrikeMonitorAndControlDatabase();
	}

	@Override
	protected void createPorts()
	{
		messaging = new C4S2MessagingProtocol(this, 0L, "StrikeServicesMessaging");
	}
}
