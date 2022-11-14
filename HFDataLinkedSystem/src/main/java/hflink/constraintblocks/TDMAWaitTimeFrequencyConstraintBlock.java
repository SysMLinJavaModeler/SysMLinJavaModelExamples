package hflink.constraintblocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import hflink.components.modemradio.ModemRadio;
import sysmlinjava.analysis.barcharts.BarChartConstraintBlock;
import sysmlinjava.analysis.barcharts.BarChartDefinition;
import sysmlinjava.analysis.barcharts.BarChartsDisplay;
import sysmlinjava.analysis.common.Axis;
import sysmlinjava.analysis.common.CategoriesAxis;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.RReal;

/**
 * Constraint block for the calculation (constraint) of the wait times for the
 * modem radios to obtain a transmission slot time in the TDMA protocol used by
 * all systems. It maintains binding connectors (via its parameter ports) with
 * the input wait times for each of the systems' modem-radios, and it outputs a
 * type of histogram of the wait time frequencies to a bar graph display.
 * 
 * @author ModelerOne
 *
 */
public class TDMAWaitTimeFrequencyConstraintBlock extends BarChartConstraintBlock
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
	public TDMAWaitTimeFrequencyConstraintBlock()
	{
		super(new BarChartDefinition("TDMA Wait Time Frequency", catAxis, yAxis, layerNames), BarChartsDisplay.udpPort, true);
	}

	/**
	 * Creates the constraint parameters by putting an initial (zero) value and key
	 * (name) for each into the parameters map. Note use of the base class
	 * constraintParams map for wait times and this class's categoryParams map for
	 * the categories.
	 */
	@Override
	protected void createConstraintParameters()
	{
		for (ParamContextBlocksEnum paramBlock : ParamContextBlocksEnum.values())
			constraintParams.put(paramBlock.toString(), new RReal(0));

		Map<String, RReal> map = new HashMap<>();
		for (CategoriesEnum category : CategoriesEnum.values())
			map.put(category.catName, new RReal(0));
		categoryParams.add(map);

	}

	/**
	 * Creates the constraint that calculates the histogram data from the input wait
	 * times, i.e.
	 * 
	 * <pre>
	 * If the parameter is ID'ed<br>
	 *   Get the wait time parameter<br>
	 *   If the wait time parameter is available, then<br>
	 *     Find the wait time's category, i.e. the interval on the histogram's x-axis<br>
	 *     Increment the frequency of the category, i.e. set the new height of the bar on the histogram's y-axis<br>
	 * </pre>
	 */
	@Override
	protected void createConstraints()
	{
		constraint = () ->
		{
			if (currentParamID.isPresent())
			{
				RReal currentParam = (RReal)constraintParams.get(currentParamID.get());
				if (currentParam != null)
				{
					DurationSeconds waitTime = DurationSeconds.of(currentParam);
					ListIterator<TimeInterval> intervals = timeIntervals.listIterator();
					boolean found = false;
					while (!found && intervals.hasNext())
					{
						TimeInterval interval = intervals.next();
						if (interval.includes(waitTime))
						{
							RReal category = categoryParams.get(0).get(interval.category);
							category.value += 1;
							found = true;
						}
					}
					if (!found)
						logger.severe("time interval for wait time " + waitTime.value + " not found");
				}
				else
					logger.severe("currentParam for currentParamID " + currentParamID.get() + " not found");
			}
			else
				logger.severe("currentParamID not present");
		};
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		SysMLConstraintParameterPortFunction c2Function = (constraintParameterPort, contextBlock) ->
		{
			DurationSeconds waitTime = ((ModemRadio)contextBlock).tdmaTransmit.timeWaited;
			constraintParameterPort.updateParameterValue(new DurationSeconds(waitTime));
		};
		SysMLConstraintParameterPortFunction alphaFunction = (constraintParameterPort, contextBlock) ->
		{
			DurationSeconds waitTime = ((ModemRadio)contextBlock).tdmaTransmit.timeWaited;
			constraintParameterPort.updateParameterValue(new DurationSeconds(waitTime));
		};
		SysMLConstraintParameterPortFunction bravoFunction = (constraintParameterPort, contextBlock) ->
		{
			DurationSeconds waitTime = ((ModemRadio)contextBlock).tdmaTransmit.timeWaited;
			constraintParameterPort.updateParameterValue(new DurationSeconds(waitTime));
		};
		SysMLConstraintParameterPortFunction charlieFunction = (constraintParameterPort, contextBlock) ->
		{
			DurationSeconds waitTime = ((ModemRadio)contextBlock).tdmaTransmit.timeWaited;
			constraintParameterPort.updateParameterValue(new DurationSeconds(waitTime));
		};
		paramPortFunctions.put(ParamContextBlocksEnum.c2.toString(), c2Function);
		paramPortFunctions.put(ParamContextBlocksEnum.alpha.toString(), alphaFunction);
		paramPortFunctions.put(ParamContextBlocksEnum.bravo.toString(), bravoFunction);
		paramPortFunctions.put(ParamContextBlocksEnum.charlie.toString(), charlieFunction);
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		for (ParamContextBlocksEnum blockEnum : ParamContextBlocksEnum.values())
		{
			SysMLConstraintParameterPortFunction portFunction = paramPortFunctions.get(blockEnum.toString());
			SysMLConstraintParameterPort paramPort = new SysMLConstraintParameterPort(this, portFunction, blockEnum.toString());
			paramPorts.put(blockEnum.toString(), paramPort);
		}
	}

	/**
	 * Enum for the parameter context blocks, i.e. the 4 systems in the domain
	 * 
	 * @author ModelerOne
	 *
	 */
	public static enum ParamContextBlocksEnum
	{
		c2, alpha, bravo, charlie
	}

	/**
	 * Enum for the categories of wait time intervals
	 * 
	 * @author ModelerOne
	 *
	 */
	public static enum CategoriesEnum
	{
		Cat0to4(0, 5, "0-5"), Cat5to9(5, 10, "5-10"), Cat10to14(10, 15, "10-15"), Cat15to19(15, 20, "15-20"), Cat20to24(20, 25, "20-25"), Cat25to29(25, 30, "25-30"), Cat30to34(30, 35, "30-35"), Cat35to39(35, 40, "35-40"), Cat40to44(40, 45,
			"40-45"), Cat45to49(45, 50, "45-50"), Cat50to54(50, 55, "50-55"), Cat55to59(55, 60, "55-60"), Cat60toN(60, Double.MAX_VALUE, "60+");

		/**
		 * Category's min wait time
		 */
		double minWaitTimeSeconds;
		/**
		 * Category's max wait time
		 */
		double maxWaitTimeSeconds;
		/**
		 * Name of category
		 */
		String catName;

		/**
		 * Private constructor
		 * 
		 * @param minWaitTimeSeconds min wait time in category
		 * @param maxWaitTimeSeconds max wait time in category
		 * @param catName            category name
		 */
		private CategoriesEnum(double minWaitTimeSeconds, double maxWaitTimeSeconds, String catName)
		{
			this.minWaitTimeSeconds = minWaitTimeSeconds;
			this.maxWaitTimeSeconds = maxWaitTimeSeconds;
			this.catName = catName;
		}

		/**
		 * Returns list of category names
		 * 
		 * @return list of names
		 */
		public static List<String> namesList()
		{
			List<String> result = new ArrayList<>();
			for (CategoriesEnum cat : values())
				result.add(cat.catName);
			return result;
		}
	}

	/**
	 * Represents a time interval that corresponds to a category of wait times
	 * 
	 * @author ModelerOne Kunce;
	 *
	 */
	public static class TimeInterval
	{
		/**
		 * Name of category
		 */
		public String category;
		/**
		 * Intervals start time
		 */
		public DurationSeconds start;
		/**
		 * Interval's end time
		 */
		public DurationSeconds end;

		/**
		 * Constructor
		 * 
		 * @param category name of category
		 * @param start    interval start seconds
		 * @param end      interval end seconds
		 */
		public TimeInterval(String category, DurationSeconds start, DurationSeconds end)
		{
			super();
			this.category = category;
			this.start = start;
			this.end = end;
		}

		/**
		 * Return whether this time interval includes the specified seconds of wait time
		 * 
		 * @param waitTimeSeconds wait time to be checked
		 * @return True if waitTimeSeconds is in the interval, false othewise
		 */
		public boolean includes(RReal waitTimeSeconds)
		{
			return waitTimeSeconds.value >= start.value && waitTimeSeconds.value < end.value;
		}
	}
}
