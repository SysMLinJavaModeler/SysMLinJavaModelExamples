package hflink.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import hflink.common.ports.TDMAReceiveProtocol;
import hflink.requirements.HFDataLinkedSystemRequirements;
import hflink.systems.gps.GPS;
import sysmlinjava.actions.SysMLCalculation;
import sysmlinjava.actions.SysMLCalculationFunction;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.KeyValueMap;
import sysmlinjava.attributetypes.ListOrdered;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.actions.CalculationFunction;
import sysmlinjava.views.barcharts.BarChartAnalysisCase;
import sysmlinjava.views.barcharts.BarChartDefinition;
import sysmlinjava.views.barcharts.BarChartsDisplay;
import sysmlinjava.views.common.Axis;
import sysmlinjava.views.common.CategoriesAxis;

/**
 * Parametric analysis case for the calculation (constraint) of the wait times
 * for the modem radios to obtain a transmission slot time in the TDMA protocol
 * used by all systems. It maintains binding connectors with the input wait
 * times for each of the systems' modem-radios, and it outputs a type of
 * histogram of the wait time frequencies to a bar graph display.
 * 
 * @author ModelerOne
 */
public class TDMAWaitTimeFrequencyAnalysisCase extends BarChartAnalysisCase
{
	/**
	 * Categories of the output histogram, i.e. names of frequency ranges
	 */
	static final public CategoriesAxis catAxis = new CategoriesAxis("Delay times (sec)", CategoriesEnum.namesList());
	/**
	 * Axis (Y) for the frequency values of the histogram
	 */
	static final public Axis yAxis = new Axis("Frequency", "occurances", Optional.empty(), Optional.empty(), 5, 5);
	/**
	 * Time intervals associated with the categories
	 */
	static ArrayList<TimeInterval> timeIntervals = new ArrayList<>();
	/**
	 * Set the min and max values of the time intervals associated with the
	 * categories
	 */
	static
	{
		for (CategoriesEnum cat : CategoriesEnum.values())
			timeIntervals.add(new TimeInterval(cat.catName, DurationSeconds.of(cat.minWaitTimeSeconds), DurationSeconds.of(cat.maxWaitTimeSeconds)));
	}
	/**
	 * List of the name values of the bars (only one layer, so no "stacked" bars)
	 */
	final static ArrayList<String> layerNames = new ArrayList<>(List.of("Frequency"));

	/**
	 * Constructor
	 */
	public TDMAWaitTimeFrequencyAnalysisCase()
	{
		super(new BarChartDefinition("TDMA Wait Time Frequency", catAxis, yAxis, layerNames), BarChartsDisplay.udpPort, true);
	}

	@CalculationFunction
	SysMLCalculationFunction nextWaitTimeCalculationFunction;

	@Calculation
	SysMLCalculation nextWaitTimeCalculation;

	@Calculation
	@Override
	public void perform()
	{
		((SysMLCalculationFunction) nextWaitTimeCalculation.function).perform();
		super.perform();
	}

	/**
	 * Creates/initializes the analysis parameters (wait times), inserting them into
	 * a key-value map. Note use of the base class {@code params} map.
	 */
	@Override
	protected void createParameters()
	{
		params = KeyValueMap.of(List.of(ParamContextsEnum.alpha.toString(), ParamContextsEnum.bravo.toString(), ParamContextsEnum.charlie.toString(), ParamContextsEnum.c2.toString()), List.of(new RReal(
		0), new RReal(0), new RReal(0), new RReal(0)));
	}

	/**
	 * Creates the calculation function that calculates the histogram data from the
	 * input wait times, i.e.
	 * 
	 * <pre>
	 * If the next value of the wait time parameter is available, then<br>
	 *   Get the wait time parameter<br>
	 *   If the wait time parameter is present, then<br>
	 *     Get the time interval on the histogram's x-axis in which the wait time is within<br>
	 *     Increment the frequency of the time interval, i.e. set the new height of the bar on the histogram's y-axis<br>
	 * </pre>
	 */
	@Override
	protected void createCalculationFunctions()
	{
		nextWaitTimeCalculationFunction = () ->
		{
			if (nextWaitTimeAvailable())
			{
				Optional<DurationSeconds> waitTime = getWaitTime();
				if (waitTime.isPresent())
				{
					TimeInterval interval = getTimeIntervalFor(waitTime.get());
					incrementCountFor(interval);
				}
			}
			else
				logger.severe("currentParamID not present");
		};
	}

	@Override
	protected void createCalculations()
	{
		nextWaitTimeCalculation = new SysMLCalculation(nextWaitTimeCalculationFunction, "nextWaitTimeCalculation", 0L);
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(TDMAReceiveProtocol.class);
	}

	@Override
	protected void createObjective()
	{
		objective = HFDataLinkedSystemRequirements.rid4_5_3_2_4;

	}

	@Override
	protected void createActors()
	{
		actors = List.of(GPS.class);
	}

	@Override
	protected void createResult()
	{
		categoryParams = ListOrdered.of(KeyValueMap.of(CategoriesEnum.namesList(), List.of(new RReal(0), new RReal(0), new RReal(0), new RReal(0), new RReal(0), new RReal(
		0), new RReal(0), new RReal(0), new RReal(0), new RReal(0), new RReal(0), new RReal(0), new RReal(0))));
	}

	private boolean nextWaitTimeAvailable()
	{
		return currentParamID.isPresent();
	}

	private Optional<DurationSeconds> getWaitTime()
	{
		Optional<DurationSeconds> result = Optional.empty();
		RReal currentParam = (RReal) params.get(currentParamID.get());
		if (currentParam != null)
			result = Optional.of(DurationSeconds.of(currentParam));
		else
			logger.severe("currentParam for currentParamID " + currentParamID.get() + " not found");
		return result;
	}

	private TimeInterval getTimeIntervalFor(DurationSeconds waitTime)
	{
		TimeInterval result = null;
		ListIterator<TimeInterval> intervals = timeIntervals.listIterator();
		boolean found = false;
		while (!found && intervals.hasNext())
		{
			TimeInterval interval = intervals.next();
			if (interval.includes(waitTime))
			{
				result = interval;
				found = true;
			}
		}
		if (!found)
			logger.severe("time interval for wait time " + waitTime.value + " not found");
		return result;
	}

	private void incrementCountFor(TimeInterval interval)
	{
		RReal category = categoryParams.get(0).get(interval.category);
		category.value += 1;
	}
}
