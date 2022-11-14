
package hflink.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import hflink.common.objects.ApplicationUIView;
import hflink.common.ports.ApplicationUIClientProtocol;
import hflink.common.ports.DesktopUIClientProtocol;
import hflink.common.ports.PCUIClientProtocol;
import hflink.domain.HFLinkDomain;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.analysis.htmldisplay.HTMLDisplay;
import sysmlinjava.analysis.htmldisplay.HTMLString;
import sysmlinjava.analysis.htmldisplay.HTMLStringTransmitter;
import sysmlinjava.analysis.interactionmessagetransmitter.InteractionMessageTransmitters;
import sysmlinjava.analysis.interactionsequence.InteractionMessageSequenceDisplay;
import sysmlinjava.analysis.interactionsequence.InteractionMessageTransmitter;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.tests.SystemUnderTest;
import sysmlinjava.annotations.tests.TestCase;
import sysmlinjava.tests.SysMLTest;

/**
 * The {@code HFDataLinkDomainTest} is a SysMLinJava model of the test for the
 * SysMLinJava model of the {@code HFLinkDomain}. The
 * {@code HFDataLinkDomainTest} consists of a single test case which simulates
 * an operator using the {@code CommandControlSystem}'s web browser-based
 * application to repeatedly poll each of the {@code DeployedSystem}'s for their
 * status and then commanding each of the {@code DeployedSystem}s to shutdown.
 * The test exercises the full protocol stacks on all devices of all the
 * subsystems to validate the system architecture and design.
 * 
 * @author ModelerOne
 *
 */
public class HFDataLinkDomainTest extends SysMLTest
{
	/**
	 * Port for the C2 application user-interface, i.e. the browser-based monitor
	 * and control views protocol
	 */
	@FullPort
	public ApplicationUIClientProtocol application;
	/**
	 * Port for the PC's desktop user-interface, i.e. the PC's window interaction
	 * protocol
	 */
	@FullPort
	public DesktopUIClientProtocol desktop;
	/**
	 * Port for the PC's physical user-interface, i.e. monitor, keyboard, mouse,
	 * audio, etc. protocols
	 */
	@FullPort
	public PCUIClientProtocol pc;

	/**
	 * Value of all the application UI views received from deployed
	 * systems
	 */
	@Value
	public List<ApplicationUIView> applicationUIViews;

	/**
	 * System under test, i.e. the domain of the HF Data-Linked System
	 */
	@SystemUnderTest
	public HFLinkDomain domain;

	/**
	 * Connector function that makes the connections between the ports in this test
	 * and the C2 Computer
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction connectorFunction;

	/**
	 * Connector that invokes the connector function after construction of the
	 * domain.
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector connector;

	/**
	 * Transmitter of HTML received from deployed systems to the HTML display, if
	 * opened
	 */
	public HTMLStringTransmitter htmlTransmitter;

	/**
	 * Test case (only one) for the test
	 */
	@TestCase
	public HFDataLinkDomainTestCase testCase;

	/**
	 * Number cycles of sending controls/receiving status in test so far
	 */
	int cycles = 0;
	/**
	 * Max number cycles of sending controls/receiving status in test
	 */
	final int maxCycles = 10;

//	/**
//	 * Value of the URLs for the send of "send status" controls to the deployed systems
//	 */
//	@Value
//	List<String> controlURLs;
//
//	int controlURLIndex = 0;
//
//	/**
//	 * Value of the URLs for the send of "shutdown" controls to the deployed systems
//	 */
//	@Value
//	List<String> shutdownURLs;
//
//	int shutdownURLIndex = 0;

	public HFDataLinkDomainTest()
	{
		super(Optional.empty(), "HF-Linked Domain Test", (long)0);

		htmlTransmitter = new HTMLStringTransmitter(HTMLDisplay.udpPort, false);
	}

	/**
	 * Event handler for all ApplicationUIView events, i.e. all HTML pages displayed
	 * by the web browser. Each HTML page represents the HTTP response received from
	 * a deployed web server and is validated correct and saved before submitting
	 * the next HTTP request as a URL.
	 * 
	 * @param view New view of the web page, i.e. the new HTML displayed in the web
	 *             browser
	 */
	@Operation
	public void onApplicationUIView(ApplicationUIView view)
	{
		applicationUIViews.add(view);
		htmlTransmitter.transmit(new HTMLString(view.view.text));
		((HFDataLinkDomainTestCase)testCases.get(0)).onApplicationUIView(view);
	}

	@Operation
	@Override
	public void initialize()
	{
		super.initialize();
		this.start();
		domain.start();
		delay(5.000);
	}

	@Operation
	@Override
	public void finalize()
	{
		super.finalize();
		acceptEvent(new FinalEvent());
		domain.stop();
		delay(5.000);
		this.stop();
	}

	@Operation
	@Override
	public void enableInteractionMessageTransmissions()
	{
		InteractionMessageTransmitter transmitter = new InteractionMessageTransmitter(InteractionMessageSequenceDisplay.udpPort, false);
		InteractionMessageTransmitters interactionMessageTransmitters = new InteractionMessageTransmitters(transmitter);

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

	@Override
	protected void createValues()
	{
		applicationUIViews = new ArrayList<>();
//		controlURLs = new ArrayList<>(List.of(
//			"http://www.hflinkeddep1.com/control=sendstatus",
//			"http://www.hflinkeddep2.com/control=sendstatus",
//			"http://www.hflinkeddep3.com/control=sendstatus"));
//		shutdownURLs = new ArrayList<>(List.of(
//			"http://www.hflinkeddep1.com/control=shutdown",
//			"http://www.hflinkeddep2.com/control=shutdown",
//			"http://www.hflinkeddep3.com/control=shutdown",
//			"http://www.hflinkedc2.com/control=shutdown"));
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new HFDataLinkDomainTestStateMachine(this));
	}

	@Override
	protected void createSystemUnderTest()
	{
		systemUnderTest = new HFLinkDomain();
		domain = (HFLinkDomain)systemUnderTest;
	}

	@Override
	protected void createFullPorts()
	{
		application = new ApplicationUIClientProtocol(this, this, 0L);
		desktop = new DesktopUIClientProtocol(this, 0L);
		pc = new PCUIClientProtocol(this, 0L);
	}

	@Override
	protected void createConnectorFunctions()
	{
		connectorFunction = () ->
		{
			application.addConnectedPortServer(desktop);
			desktop.addConnectedPortClient(application);
			desktop.addConnectedPortServer(pc);
			pc.addConnectedPortClient(desktop);
			pc.addConnectedPortPeer(domain.C2System.C2Computer.pc);
			domain.C2System.C2Computer.pc.addConnectedPortPeer(pc);
		};
	}

	@Override
	protected void createConnectors()
	{
		connector = new SysMLAssociationBlockConnector(this, domain, connectorFunction);
	}

	@Override
	protected void createTestCases()
	{
		testCase = new HFDataLinkDomainTestCase(this);
		testCases.add(testCase);
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
		HFDataLinkDomainTest test = new HFDataLinkDomainTest();
		test.initialize();
		test.execute();
		test.finalize();
		System.exit(0);
	}
}
