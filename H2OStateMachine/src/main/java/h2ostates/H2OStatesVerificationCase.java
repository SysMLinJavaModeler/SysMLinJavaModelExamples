package h2ostates;

import java.util.List;
import java.util.Optional;

import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.events.SysMLChangeEvent;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.verifications.SysMLVerdictKind;
import sysmlinjava.verifications.SysMLVerificationCase;

/**
 * Verification (test) case for the {@code H2O} model. It demonstrates how the
 * SysML verification case is implemented in SysMLinJava. The test includes a
 * single case which executes the {@code H2O} part and its
 * {@code H2OStateMachine}.
 * <p>
 * The tested {@code H2OStateMachine} is a SysMLinJava implementation of the
 * SysML state for {@code H2O} as described in the book "A Practical Guide to
 * SysML - The Systems Modeling Language 3rd edition" by Sanford Friedenthal, et
 * al; Object Management Group; Morgan Kaufman publisher; copyright 2015.
 * 
 * @author ModelerOne
 *
 */
public class H2OStatesVerificationCase extends SysMLVerificationCase
{
	private H2O h2O;

	/**
	 * Link to Friedenthal, et al book
	 */
	@Hyperlink
	public SysMLHyperlink friedenthalBook;

	/**
	 * Constructor of the test case
	 */
	public H2OStatesVerificationCase()
	{
		super("H2O States Test", 0L, Optional.empty());
	}

	@Action
	@Override
	public void perform()
	{
		boolean initialized = intializeTest();
		if (initialized)
			verdict = executeTest();
		finalizeTest();
	}

	/**
	 * Initializes the test case, i.e. starts the H2O subject of test.
	 * 
	 * @return verdict of the intialization, i.e. it succeeded
	 */
	@Action
	private boolean intializeTest()
	{
		logger.info("initializing");
		boolean result = false;
		verdict = SysMLVerdictKind.inconclusive;
		h2O = new H2O();
		h2O.start();
		result = true;
		return result;
	}

	/**
	 * Executes the test. Execution changes the temperature from below freezing to
	 * boiling at increments of 10C, then to decomposed at increments of 200C. Test
	 * is validated by checking that the H2O moved throught the state machine
	 * completely and correctly, i.e. ends in the final state.
	 * 
	 * @return verdict of test
	 */
	@Action
	private SysMLVerdictKind executeTest()
	{
		SysMLVerdictKind result = SysMLVerdictKind.inconclusive;
		logger.info("executing");
		logger.info("raising to 100C");
		for (int i = -50; i <= 100; i += 10)
			changeTemp(h2O, i);
		logger.info("raising to 2200C");
		for (int i = 200; i <= 2200; i += 200)
			changeTemp(h2O, i);
		if (h2O.temp.greaterThanOrEqualTo(h2O.decomposedTemp) && h2O.stateMachine.get().currentState.isPresent() && h2O.stateMachine.get().currentState.get() == h2O.stateMachine.get().finalState)
			result = SysMLVerdictKind.pass;
		return result;
	}

	/**
	 * Finalizes the test case, i.e. stops the H2O subject of test
	 */
	@Action
	private void finalizeTest()
	{
		logger.info("finalizing");
		h2O.stop();
	}

	/**
	 * Action to change the temperature of the H2O during model execution/test.
	 * Changes are invoked by submitting a temp change event to the H2O and waiting
	 * a second before the next change, thereby performing the simulation at a
	 * accelerated rate that is still comprehendable.
	 * 
	 * @param h2O      the H2O subject of the test
	 * @param degreesC temperature to be used in degrees C
	 */
	@Action
	private void changeTemp(H2O h2O, int degreesC)
	{
		logger.info(String.format("temperature=%dC", degreesC));
		h2O.temp.value = degreesC;
		h2O.acceptEvent(new SysMLChangeEvent("TemperatureChange", "TemperatureChangeEvent", 0L));
		h2O.delay(1.0);
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(H2O.class);
	}

	@Override
	protected void createActors()
	{
		actors = List.of(); // this verification case is the actor
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		friedenthalBook = H2OStatesVerificationRequirements.friedenthalBook;
	}

	@Override
	protected void createObjective()
	{
		objective = H2OStatesVerificationRequirements.objective;
	}

	/**
	 * Console-based process that creates, initializes, executes, and finalizes the
	 * test.
	 * 
	 * @param args null arguments list
	 */
	public static void main(String[] args)
	{
		H2OStatesVerificationCase test = new H2OStatesVerificationCase();
		test.perform();
		System.exit(0);
	}
}
