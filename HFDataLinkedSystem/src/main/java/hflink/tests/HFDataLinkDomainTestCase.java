package hflink.tests;

import java.util.List;
import java.util.Optional;
import hflink.common.objects.ApplicationUIControlString;
import hflink.common.objects.ApplicationUIView;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.probability.SysMLUniformProbabilityDistribution;
import sysmlinjava.tests.SysMLTestCase;
import sysmlinjava.tests.SysMLVerdictKind;
import sysmlinjava.valuetypes.DurationSeconds;

/**
 * The {@code HFLinkSysMLTest} is a SysMLinJava model of the test for the
 * SysMLinJava model of the {@code HFLinkDomain}. The {@code HFLinkSysMLTest}
 * consists of a single test case which simulates an operator using the
 * {@code CommandControlSystem}'s web browser-based application to repeatedly
 * poll each of the {@code DeployedSystem}'s for their status and then
 * commanding each of the {@code DeployedSystem}s to shutdown. The test
 * exercises the full protocol stacks on all devices of all the subsystems to
 * validate the system architecture and design.
 * 
 * @author ModelerOne
 *
 */
public class HFDataLinkDomainTestCase extends SysMLTestCase
{
	/**
	 * Test of which this test case is a part of
	 */
	private final HFDataLinkDomainTest hfDataLinkDomainTest;

	/**
	 * Number cycles of sending controls/receiving status in test so far
	 */
	int cycles = 0;

	/**
	 * Max number cycles of sending controls/receiving status in test
	 */
	final int maxCycles = 10;

	/**
	 * URLs for the send of "send status" controls to the deployed systems
	 */
	@Value
	List<String> controlURLs;

	/**
	 * Index into the controlURLs
	 */
	int controlURLIndex;

	/**
	 * URLs for the send of "shutdown" controls to the deployed systems
	 */
	@Value
	List<String> shutdownURLs;

	/**
	 * Index into the shutdownURLs
	 */
	int shutdownURLIndex;

	/**
	 * Duration of (random) delay time until next transmission of control URL
	 * (simulating operator's somewhat random intervals between sending controls to
	 * remote systems)
	 */
	DurationSeconds delaySecs = new DurationSeconds(0, new SysMLUniformProbabilityDistribution(1.0, 5.0));

	/**
	 * Constructor
	 * 
	 * @param test test of which this test case is a part of
	 */
	public HFDataLinkDomainTestCase(HFDataLinkDomainTest test)
	{
		super(test);
		this.hfDataLinkDomainTest = test;
	}

	/**
	 * Event handler for all ApplicationUIView events, i.e. all HTML pages displayed
	 * by the web browser. Each HTML page represents the HTTP response received from
	 * a deployed web server and is validated correct and saved before submitting
	 * the next HTTP request as a URL.
	 * 
	 * @param view next view of the web page, i.e. the new HTML to be displayed in
	 *             the web browser
	 */
	@Reception
	public void onApplicationUIView(ApplicationUIView view)
	{
		logger.info(view.toString());
		if (cycles < maxCycles)
		{
			if (controlURLIndex < controlURLs.size())
			{
				ApplicationUIControlString controlString = new ApplicationUIControlString(controlURLs.get(controlURLIndex));
				logger.info(String.format("--cycle %d, ctrl %d: %s", cycles, controlURLIndex, controlString.toString()));
				double seconds = delaySecs.getValue();
				delay(seconds);
				hfDataLinkDomainTest.application.transmit(controlString);
				controlURLIndex++;
			}
			else
			{
				cycles++;
				if (cycles < maxCycles)
				{
					controlURLIndex = 0;
					ApplicationUIControlString controlString = new ApplicationUIControlString(controlURLs.get(controlURLIndex));
					logger.info(String.format("--cycle %d, ctrl %d: %s", cycles, controlURLIndex, controlString.toString()));
					double seconds = delaySecs.getValue();
					delay(seconds);
					hfDataLinkDomainTest.application.transmit(controlString);
					controlURLIndex++;
				}
				else
				{
					shutdownURLIndex = 0;
					ApplicationUIControlString shutdownString = new ApplicationUIControlString(shutdownURLs.get(shutdownURLIndex));
					logger.info(String.format("--shtdwn, ctrl %d: %s", cycles, shutdownURLIndex, shutdownString.toString()));
					delay(2);
					hfDataLinkDomainTest.application.transmit(shutdownString);
					shutdownURLIndex++;
				}
			}
		}
		else if (shutdownURLIndex < shutdownURLs.size())
		{
			ApplicationUIControlString shutdownString = new ApplicationUIControlString(shutdownURLs.get(shutdownURLIndex));
			logger.info(String.format("--shtdwn, ctrl %d: %s", cycles, shutdownURLIndex, shutdownString.toString()));
			delay(2);
			hfDataLinkDomainTest.application.transmit(shutdownString);
			shutdownURLIndex++;
		}
		else if (shutdownURLIndex >= shutdownURLs.size())
			logger.warning("unexpected ApplicationUIView: " + view.toString());
	}

	/**
	 * Determines whether the HTML for all HTTP responses received from the site
	 * systems is as expected.
	 * 
	 * @return whether the receive views (HTML) are OK
	 */
	@Operation
	private boolean applicationUIViewsOK()
	{
		boolean result = true;
		if (hfDataLinkDomainTest.applicationUIViews.size() != maxCycles * 3 + 4)
			result = false;
		else
		{
			int i = 0;
			while (result && i < maxCycles)
			{
				if (!hfDataLinkDomainTest.applicationUIViews.get(i * 3 + 0).view.text.contains("System www.hflinkeddep1.com is A-OK!")
					|| !hfDataLinkDomainTest.applicationUIViews.get(i * 3 + 1).view.text.contains("System www.hflinkeddep2.com is A-OK!")
					|| !hfDataLinkDomainTest.applicationUIViews.get(i * 3 + 2).view.text.contains("System www.hflinkeddep3.com is A-OK!"))
					result = false;
				i++;
			}
			if (result)
			{
				i = maxCycles * 3;
				if (!hfDataLinkDomainTest.applicationUIViews.get(i + 0).view.text.contains("System www.hflinkeddep1.com is shutting down")
					|| !hfDataLinkDomainTest.applicationUIViews.get(i + 1).view.text.contains("System www.hflinkeddep2.com is shutting down")
					|| !hfDataLinkDomainTest.applicationUIViews.get(i + 2).view.text.contains("System www.hflinkeddep3.com is shutting down")
					|| !hfDataLinkDomainTest.applicationUIViews.get(i + 3).view.text.contains("System www.hflinkedc2.com is shutting down"))
					result = false;
			}
		}
		return result;
	}

	@Override
	protected void createValues()
	{
		controlURLs = List.of("http://www.hflinkeddep1.com/control=sendstatus", "http://www.hflinkeddep2.com/control=sendstatus", "http://www.hflinkeddep3.com/control=sendstatus");
		shutdownURLs = List.of("http://www.hflinkeddep1.com/control=shutdown", "http://www.hflinkeddep2.com/control=shutdown", "http://www.hflinkeddep3.com/control=shutdown", "http://www.hflinkedc2.com/control=shutdown");
	}

	/**
	 * Creates test case activity for test case initialization. Transmits the first
	 * control string (HTML request). Subsequent transmissions will be made by the
	 * {@code onApplicationUIView()} event handler (executing in the state machine
	 * thread) that receives the view strings (HTML responses).<br>
	 */
	@Override
	protected void createInitializeActivity()
	{
		initialize = Optional.of(() ->
		{
			controlURLIndex = 0;
			shutdownURLIndex = 0;
		});
	}

	/**
	 * Creates test case activity for test case execution. Execution waits for all
	 * the controls to be transmitted and responses received. Then calculates the
	 * test verdict.
	 */
	@Override
	protected void createExecuteActivity()
	{
		execute = () ->
		{
			verdict = SysMLVerdictKind.fail;

			// Send the first control and then wait for last shutdown to be sent (subsequent
			// controls are sent in response to receiving a monitor in onApplicationUIView()
			// above
			ApplicationUIControlString controlString = new ApplicationUIControlString(controlURLs.get(controlURLIndex));
			logger.info(controlString.toString());
			hfDataLinkDomainTest.application.transmit(controlString);
			controlURLIndex++;

			//Sample the "send control/receive monitor cycles until completed
			while (cycles < maxCycles)
				delay(5.0);
			//Then sample the send shutdown/receive monitor cylces until completed
			while (shutdownURLIndex < shutdownURLs.size())
				delay(5.0);

			//All done, so set the verdict
			if (applicationUIViewsOK())
				verdict = SysMLVerdictKind.pass;
		};
	}

	/**
	 * Creates test case activity for test case finalization, which does nothing.
	 */
	@Override
	protected void createFinalizeActivity()
	{
		finalize = Optional.of(() ->
		{
			//No op for now.
		});
	}
}