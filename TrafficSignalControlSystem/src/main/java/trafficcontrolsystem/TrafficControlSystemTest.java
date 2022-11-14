package trafficcontrolsystem;

import java.util.Optional;
import sysmlinjava.annotations.tests.SystemUnderTest;
import sysmlinjava.annotations.tests.TestCase;
import sysmlinjava.tests.SysMLTest;

/**
 * Test of the {@code TrafficControlSystem} model. The test simply injects
 * events into the system for an emergency vehicle approaching and departing the
 * system from different directions and at different times.
 * 
 * @author ModelerOne
 *
 */
public class TrafficControlSystemTest extends SysMLTest
{
	/**
	 * System that will undergo the test
	 */
	@SystemUnderTest
	TrafficControlSystem tcs;

	/**
	 * One and only test case in the test
	 */
	@TestCase
	TrafficControlSystemTestCase testCase;
	
	/**
	 * Constructor
	 */
	public TrafficControlSystemTest()
	{
		super(Optional.empty(), "TrafficControlSystemTest", 0L);
	}

	@Override
	protected void createSystemUnderTest()
	{
		systemUnderTest = new TrafficControlSystem();
		tcs = (TrafficControlSystem)systemUnderTest;
	}

	@Override
	protected void createTestCases()
	{
		testCase = new TrafficControlSystemTestCase(this);
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
		TrafficControlSystemTest test = new TrafficControlSystemTest();
		test.initialize();
		test.execute();
		test.finalize();
		System.exit(0);
	}

}
