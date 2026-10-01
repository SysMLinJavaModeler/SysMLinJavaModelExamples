
package hflink.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hflink.common.items.ApplicationUIControlString;
import hflink.common.items.ApplicationUIView;
import hflink.common.ports.ApplicationUIClientProtocol;
import hflink.common.ports.DesktopUIClientProtocol;
import hflink.common.ports.PCUIClientProtocol;
import hflink.domain.HFLinkDomain;
import hflink.requirements.HFDataLinkedSystemRequirements;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.actions.VerificationCaseAction;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.occurrences.Subject;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.javaannotations.requirements.RequirementFunction;
import sysmlinjava.states.FinalEvent;
import sysmlinjava.verifications.SysMLVerdictKind;
import sysmlinjava.verifications.SysMLVerificationCase;
import sysmlinjava.views.htmldisplay.HTMLDisplay;
import sysmlinjava.views.htmldisplay.HTMLString;
import sysmlinjava.views.htmldisplay.HTMLStringTransmitter;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageSequenceDisplay;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitter;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageTransmitters;

/**
 * The {@code HFDataLinkDomainVerificationCase} is a SysMLinJava model of the
 * test for the SysMLinJava model of the {@code HFLinkDomain}. The
 * {@code HFDataLinkDomainVerificationCase} consists of a single test case which
 * simulates an operator using the {@code CommandControlSystem}'s web
 * browser-based application to repeatedly poll each of the
 * {@code DeployedSystem}'s for their status and then commanding each of the
 * {@code DeployedSystem}s to shutdown. The test exercises the full protocol
 * stacks on all devices of all the subsystems to validate the system
 * architecture and design.
 * 
 * @author ModelerOne
 */
public class HFDataLinkDomainVerificationCase extends SysMLVerificationCase
{
	/**
	 * Port for the C2 application user-interface, i.e. the browser-based monitor
	 * and control views protocol
	 */
	@Port
	public ApplicationUIClientProtocol application;
	/**
	 * Port for the PC's desktop user-interface, i.e. the PC's window interaction
	 * protocol
	 */
	@Port
	public DesktopUIClientProtocol desktop;
	/**
	 * Port for the PC's physical user-interface, i.e. monitor, keyboard, mouse,
	 * audio, etc. protocols
	 */
	@Port
	public PCUIClientProtocol pc;

	/**
	 * Value of all the application UI views received from deployed systems
	 */
	@Attribute
	public List<ApplicationUIView> applicationUIViews;

	/**
	 * Subject of the test case, i.e. the domain of the HF Data-Linked System
	 */
	@Subject
	public HFLinkDomain domain;

	/**
	 * Connector between application and desktop
	 */
	@FlowConnector
	public SysMLFlowConnector applicationToDesktop;
	/**
	 * Connector between desktop and PC
	 */
	@FlowConnector
	public SysMLFlowConnector desktopToPC;
	/**
	 * Connector between PC and C2 Computer
	 */
	@FlowConnector
	public SysMLFlowConnector pcToC2Computer;

	/**
	 * Transmitter of HTML received from deployed systems to the HTML display, if
	 * opened
	 */
	@Part
	public HTMLStringTransmitter htmlTransmitter;

	/**
	 * Number cycles of sending controls/receiving status in test so far
	 */
	@Attribute
	IInteger cycles;
	/**
	 * Max number cycles of sending controls/receiving status in test
	 */
	@Attribute
	IInteger maxCycles;

	/**
	 * Value of the URLs for the send of "send status" controls to the deployed
	 * systems
	 */
	@Attribute
	List<String> controlURLs;

	int controlURLIndex = 0;

	/**
	 * Value of the URLs for the send of "shutdown" controls to the deployed systems
	 */
	@Attribute
	List<String> shutdownURLs;

	int shutdownURLIndex = 0;

	/**
	 * Duration of (random) delay time until next transmission of control URL
	 * (simulating operator's somewhat random intervals between sending controls to
	 * remote systems)
	 */
	@Attribute
	DurationSeconds delaySecs = new DurationSecondsRandomUniform(new RReal(0));

	/**
	 * Constructor
	 */
	public HFDataLinkDomainVerificationCase()
	{
		super("HF-Linked Domain Test", (long) 0, Optional.empty());
		htmlTransmitter = new HTMLStringTransmitter(HTMLDisplay.udpPort, false);
		createAttributes();
		createPorts();
		createStateMachine();
		createFlowConnectors();
	}

	@Override
	public void start()
	{
		super.start();
		domain.start();
		delay(5.000);
	}

	@Override
	public void stop()
	{
		acceptEvent(new FinalEvent());
		domain.stop();
		delay(5.000);
		super.stop();
	}

	@VerificationCaseAction
	@Override
	public void perform()
	{
		verdict = initializeTest();
		if (verdict == SysMLVerdictKind.pass)
			verdict = executeTest();
		finalizeTest();
	}

	@Action
	private SysMLVerdictKind initializeTest()
	{
		controlURLIndex = 0;
		shutdownURLIndex = 0;
		enableInteractionMessageTransmissions();
		start();
		return SysMLVerdictKind.pass;
	}

	@Action
	private SysMLVerdictKind executeTest()
	{
		SysMLVerdictKind result = SysMLVerdictKind.fail;

		// Send the first control and then wait for last shutdown to be sent (subsequent
		// controls are sent in response to receiving a monitor in onApplicationUIView()
		// above
		ApplicationUIControlString controlString = new ApplicationUIControlString(controlURLs.get(controlURLIndex));
		logger.info(controlString.toString());
		application.transmit(controlString);
		controlURLIndex++;

		// Sample the "send control/receive monitor cycles until completed
		while (cycles.lessThan(maxCycles))
			delay(5.0);
		// Then sample the send shutdown/receive monitor cycles until completed
		while (shutdownURLIndex < shutdownURLs.size())
			delay(5.0);

		// All done, so set the verdict
		if (applicationUIViewsOK())
			result = SysMLVerdictKind.pass;
		logger.info("--- verdict = " + result.toString());
		return result;
	}

	@Action
	private void finalizeTest()
	{
		stop();
	}

	/**
	 * Action that is event handler for all ApplicationUIView events, i.e. all HTML
	 * pages received for display by the web browser. Each HTML page represents the
	 * HTTP response received from a deployed web server and is validated correct
	 * and saved before submitting the next HTTP request as a URL.
	 * 
	 * @param view next received view, i.e. the next HTML to be displayed in the web
	 *             browser
	 */
	@RequirementFunction
	@Action
	public void onApplicationUIView(ApplicationUIView view)
	{
		logger.info(view.toString());
		applicationUIViews.add(view);
		htmlTransmitter.transmit(new HTMLString(view.view.text));

		if (cycles.lessThan(maxCycles))
		{
			if (controlURLIndex < controlURLs.size())
			{
				ApplicationUIControlString controlString = new ApplicationUIControlString(controlURLs.get(controlURLIndex));
				logger.info(String.format("--cycle %d, ctrl %d: %s", cycles.value, controlURLIndex, controlString.toString()));
				double seconds = delaySecs.getValue();
				delay(seconds);
				application.transmit(controlString);
				controlURLIndex++;
			}
			else
			{
				cycles.increment();
				if (cycles.lessThan(maxCycles))
				{
					controlURLIndex = 0;
					ApplicationUIControlString controlString = new ApplicationUIControlString(controlURLs.get(controlURLIndex));
					logger.info(String.format("--cycle %d, ctrl %d: %s", cycles.value, controlURLIndex, controlString.toString()));
					double seconds = delaySecs.getValue();
					delay(seconds);
					application.transmit(controlString);
					controlURLIndex++;
				}
				else
				{
					shutdownURLIndex = 0;
					ApplicationUIControlString shutdownString = new ApplicationUIControlString(shutdownURLs.get(shutdownURLIndex));
					logger.info(String.format("--shtdwn, ctrl %d: %s", cycles.value, shutdownURLIndex, shutdownString.toString()));
					delay(2);
					application.transmit(shutdownString);
					shutdownURLIndex++;
				}
			}
		}
		else if (shutdownURLIndex < shutdownURLs.size())
		{
			ApplicationUIControlString shutdownString = new ApplicationUIControlString(shutdownURLs.get(shutdownURLIndex));
			logger.info(String.format("--shtdwn, ctrl %d: %s", cycles.value, shutdownURLIndex, shutdownString.toString()));
			delay(2);
			application.transmit(shutdownString);
			shutdownURLIndex++;
		}
		else if (shutdownURLIndex >= shutdownURLs.size())
			logger.warning("unexpected ApplicationUIView: " + view.toString());
	}

	/**
	 * Calculation for the application UI views that all of them (the HTTP
	 * responses) are received from the site systems as expected.
	 * 
	 * @return whether the receive views are OK
	 */
	@Calculation
	private boolean applicationUIViewsOK()
	{
		boolean result = true;
		if (applicationUIViews.size() != maxCycles.value * 3 + 4)
			result = false;
		else
		{
			int i = 0;
			while (result && i < maxCycles.value)
			{
				if (!applicationUIViews.get(i * 3 + 0).view.text.contains("System www.hflinkeddep1.com is A-OK!") ||
					!applicationUIViews.get(i * 3 + 1).view.text.contains("System www.hflinkeddep2.com is A-OK!") ||
					!applicationUIViews.get(i * 3 + 2).view.text.contains("System www.hflinkeddep3.com is A-OK!"))
					result = false;
				i++;
			}
			if (result)
			{
				i = (int) (maxCycles.value * 3);
				if (!applicationUIViews.get(i + 0).view.text.contains("System www.hflinkeddep1.com is shutting down") ||
					!applicationUIViews.get(i + 1).view.text.contains("System www.hflinkeddep2.com is shutting down") ||
					!applicationUIViews.get(i + 2).view.text.contains("System www.hflinkeddep3.com is shutting down") ||
					!applicationUIViews.get(i + 3).view.text.contains("System www.hflinkedc2.com is shutting down"))
					result = false;
			}
		}
		return result;
	}

	@Override
	protected void createAttributes()
	{
		maxCycles = new IInteger(10);
		cycles = new IInteger(0);

		applicationUIViews = new ArrayList<>();

		controlURLs = new ArrayList<>(
		List.of("http://www.hflinkeddep1.com/control=sendstatus", "http://www.hflinkeddep2.com/control=sendstatus", "http://www.hflinkeddep3.com/control=sendstatus"));

		shutdownURLs = new ArrayList<>(
		List.of("http://www.hflinkeddep1.com/control=shutdown", "http://www.hflinkeddep2.com/control=shutdown", "http://www.hflinkeddep3.com/control=shutdown", "http://www.hflinkedc2.com/control=shutdown"));
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new HFDataLinkDomainTestStateMachine(this));
	}

	@Override
	protected void createObjective()
	{
		objective = HFDataLinkedSystemRequirements.rid4_5_3_2_4;
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(HFLinkDomain.class);
		domain = new HFLinkDomain();
	}

	@Override
	protected void createActors()
	{
		actors = List.of();
	}

	protected void createPorts()
	{
		application = new ApplicationUIClientProtocol(this, this, 0L);
		desktop = new DesktopUIClientProtocol(this, 0L);
		pc = new PCUIClientProtocol(this, 0L);
	}

	protected void createFlowConnectors()
	{
		applicationToDesktop = new SysMLFlowConnector(TypesEnum.clienttoserver, true, application, desktop, "", 0L);
		desktopToPC = new SysMLFlowConnector(TypesEnum.clienttoserver, true, desktop, pc, "", 0L);
		pcToC2Computer = new SysMLFlowConnector(TypesEnum.peertopeer, true, pc, domain.C2System.C2Computer.pc, "", 0L);
	}

	@Override
	public void enableInteractionMessageTransmissions()
	{
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, false);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter, false);

		domain.C2System.ModemRadio.hfTransmit.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.C2System.ModemRadio.ethernet.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.C2System.ModemRadio.hfIPPacketProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.C2System.ModemRadio.ethernetIPPacketProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.C2System.ModemRadio.gpsMessageProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.C2System.C2Computer.Ethernet.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.C2System.C2Computer.pc.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.C2System.SwitchRouter.ethernet0.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.C2System.SwitchRouter.ethernet1.messageUtility = Optional.of(interactionMessageTransmitters);

		domain.SiteAlphaDeployedSystem.DeployedModemRadio.hfTransmit.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteAlphaDeployedSystem.DeployedModemRadio.ethernet.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteAlphaDeployedSystem.DeployedModemRadio.hfIPPacketProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteAlphaDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteAlphaDeployedSystem.DeployedModemRadio.gpsMessageProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteAlphaDeployedSystem.DeployedComputer.Ethernet.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteAlphaDeployedSystem.DeployedSwitchRouter.ethernet0.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteAlphaDeployedSystem.DeployedSwitchRouter.ethernet1.messageUtility = Optional.of(interactionMessageTransmitters);

		domain.SiteBravoDeployedSystem.DeployedModemRadio.hfTransmit.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteBravoDeployedSystem.DeployedModemRadio.ethernet.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteBravoDeployedSystem.DeployedModemRadio.hfIPPacketProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteBravoDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteBravoDeployedSystem.DeployedModemRadio.gpsMessageProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteBravoDeployedSystem.DeployedComputer.Ethernet.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteBravoDeployedSystem.DeployedSwitchRouter.ethernet0.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteBravoDeployedSystem.DeployedSwitchRouter.ethernet1.messageUtility = Optional.of(interactionMessageTransmitters);

		domain.SiteCharlieDeployedSystem.DeployedModemRadio.hfTransmit.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteCharlieDeployedSystem.DeployedModemRadio.ethernet.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteCharlieDeployedSystem.DeployedModemRadio.hfIPPacketProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteCharlieDeployedSystem.DeployedModemRadio.ethernetIPPacketProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteCharlieDeployedSystem.DeployedModemRadio.gpsMessageProcessorProxy.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteCharlieDeployedSystem.DeployedComputer.Ethernet.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteCharlieDeployedSystem.DeployedSwitchRouter.ethernet0.messageUtility = Optional.of(interactionMessageTransmitters);
		domain.SiteCharlieDeployedSystem.DeployedSwitchRouter.ethernet1.messageUtility = Optional.of(interactionMessageTransmitters);

		domain.GPS.GPSMessaging.messageUtility = Optional.of(interactionMessageTransmitters);
	}

	/**
	 * Console-based process that creates, initializes, executes, and finalizes the
	 * test.
	 * 
	 * @param args null arguments list
	 */
	public static void main(String[] args)
	{
		try
		{
			Thread.sleep(3000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		HFDataLinkDomainVerificationCase test = new HFDataLinkDomainVerificationCase();
		test.perform();
		System.exit(0);
	}
}