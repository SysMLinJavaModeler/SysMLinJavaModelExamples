package roboticmower.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import roboticmower.RoboticMowerDomain;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.KeyValueMap;
import sysmlinjava.attributetypes.ListOrdered;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.Waypoint;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.javaannotations.actions.AnalysisCaseAction;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.barcharts.BarChartAnalysisCase;
import sysmlinjava.views.barcharts.BarChartDefinition;
import sysmlinjava.views.common.Axis;
import sysmlinjava.views.common.CategoriesAxis;

/**
 * Analysis case for the analysis of the error in planned versus actual
 * waypoints of the Robotic Mower. It utilizes binding connectors to the current
 * planned waypoint for the mower as calculated by the PositionController and
 * the current actual waypoint of the mower as sensed by the PositionController.
 * The analysis case produces data for a histogram of the frequency distribution
 * of the error values. The histogram is in the form the SysMLinJava bar graph
 * display.
 * 
 * @author ModelerOne
 */
public class RoboticMowerWaypointErrorFrequencyAnalysisCase extends BarChartAnalysisCase
{
	/**
	 * Categories of the output histogram, i.e. names of error frequency ranges
	 */
	static final public CategoriesAxis catAxis = new CategoriesAxis("Waypoint Error (centimeters)", CategoriesEnum.namesList());
	/**
	 * Axis (Y) for the frequency values of the histogram
	 */
	static final public Axis yAxis = new Axis("Frequency", "occurances", Optional.empty(), Optional.empty(), 5, 5);
	/**
	 * ID for the current waypoint the mower actually navigated to
	 */
	public static final String uidWaypointActual = "waypointActual";
	/**
	 * ID for the current waypoint the mower is planned to navigate to
	 */
	public static final String uidWaypointPlanned = "waypointPlanned";
	/**
	 * ID for the current 2D waypoint error, , i.e. X and Y differences between the
	 * mower's planned and actual waypoints
	 */
	public static final String uidWaypointError2D = "waypointError2D";

	/**
	 * Parameter for the current waypoint the mower actually navigated to
	 */
	@Parameter
	public Waypoint currWaypointActual;
	/**
	 * Parameter for the current waypoint the mower is planned to navigated to
	 */
	@Parameter
	public Waypoint currWaypointPlanned;
	/**
	 * Parameter for the 2D waypoint error, i.e. X and Y differences between the
	 * mower's planned and actual waypoints
	 */
	@Parameter
	public Point2D waypointError2D;

	/**
	 * Initial actual waypoint
	 */
	@Attribute
	private Waypoint initialCurrWaypointActual;

	/**
	 * Initial planned waypoint
	 */
	@Attribute
	private Waypoint initialCurrWaypointPlanned;

	/**
	 * Initial waypoint error - zero
	 */
	@Attribute
	private Point2D initialWaypointError2D;

	/**
	 * List of the name values of the bars (only one layer, so no "stacked" bars)
	 */
	final static ArrayList<String> layerNames = new ArrayList<>(List.of("Frequency"));

	/**
	 * Constructor
	 * 
	 * @param udpPort UDP port at which the BarChartsDisplay operates to receive the
	 *                frequency distribution data
	 */
	public RoboticMowerWaypointErrorFrequencyAnalysisCase(int udpPort)
	{
		super(new BarChartDefinition("Waypoint Error Frequency", catAxis, yAxis, layerNames), udpPort, true);
	}

	/**
	 * Performs the analysis that calculates the histogram data from the planned and
	 * actual waypoint values.
	 * 
	 * <pre>
	 *	If the parameter is ID'ed<br>
	 *		Switch on the current parameter updated ...
	 *			If the actual waypoint is updated, then<br>
	 *				Calculate the difference between the planned and actual X and Y positions<br>
	 *				Calculate the distance between the two positions As the hypotenuse of the X and Y differences<br>
	 *				Determine which category of error range this distance falls into
	 *				Increment the value for the category for the frequency distribution
	 *			Else if the planned waypoint is updated, then<br>
	 *				Do nothing as the error is calculated after the actual waypoint is updated<br>
	 *		Transmit the updated chart data to the bar chart display
	 * </pre>
	 */
	@AnalysisCaseAction
	@Override
	public void perform()
	{
		if (currentParamID.isPresent())
		{
			switch (currentParamID.get())
			{
			case uidWaypointActual:
			{
				DistanceMeters xDiff = new DistanceMeters(currWaypointActual.xValue - currWaypointPlanned.xValue);
				DistanceMeters yDiff = new DistanceMeters(currWaypointActual.yValue - currWaypointPlanned.yValue);
				DistanceMeters errorDistance = new DistanceMeters(DistanceMeters.hypotenuseOf(xDiff, yDiff));

				Optional<CategoriesEnum> errorCategory = CategoriesEnum.of(errorDistance);
				if (errorCategory.isPresent())
					categoryParams.get(0).get(errorCategory.get().catName).value += 1;

				waypointError2D.setValue(xDiff.value, yDiff.value);
				break;
			}
			case uidWaypointPlanned:
				// do nothing, planned waypoint is used to calculate error only after actual
				// waypoint is updated
				break;
			default:
				logger.severe("unrecognized currentParamID " + currentParamID.get());
			}
		}
		else
			logger.severe("currentParamID not present");
		super.perform();
	}

	@Override
	protected void onParameterChange(String paramID)
	{
		if (!paramID.isBlank())
		{
			previousParamID = currentParamID;
			currentParamID = Optional.of(paramID);
			SysMLBindingConnector paramConnector = paramConnectors.get(currentParamID.get());
			if (paramConnector != null)
			{
				if (currentParam != null)
					previousParam = currentParam;
				SysMLAttributeType boundParam = paramConnector.getAttribute();
				if (boundParam != null)
				{
					currentParam = boundParam;
					switch (currentParamID.get())
					{
					case uidWaypointActual:
						currWaypointActual = (Waypoint) currentParam;
						break;
					case uidWaypointPlanned:
						currWaypointPlanned = (Waypoint) currentParam;
						break;
					default:
						break;
					}
				}
				else
					logger.severe("bound parameter value not retrieved from its binding connector: " + paramID);
			}
			else
				logger.severe("binding connector not found for parameter: " + paramID);
		}
		else
			currentParamID = Optional.empty();

	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(RoboticMowerDomain.class);
	}

	@Override
	protected void createActors()
	{
		actors = List.of();
	}

	@Override
	protected void createObjective()
	{
		objective = RoboticMowerSystemAnalysisRequirements.waypointsAnalysisObjective;
	}

	@Override
	protected void createResult()
	{
		categoryParams = ListOrdered.of(KeyValueMap.of(CategoriesEnum.namesList(), List.of(new RReal(0), new RReal(0), new RReal(0), new RReal(0), new RReal(0), new RReal(
		0), new RReal(0), new RReal(0), new RReal(0), new RReal(0), new RReal(0))));
	}

	/**
	 * Creates the constraint parameters with initial (zero) values. The constraint
	 * parameter instances declared in this class are used by this constraint block
	 * and only references to them are stored in the {@code constraintParams}.
	 */
	@Override
	protected void createParameters()
	{
		params = KeyValueMap.of(List.of(uidWaypointActual, uidWaypointPlanned, uidWaypointError2D), List.of(new Waypoint(0, 0, InstantMilliseconds.now()), new Waypoint(0, 0,
		InstantMilliseconds.now()), new Point2D(0, 0)));
		waypointError2D = new Point2D();
	}

	/**
	 * Enum for the categories of error distance
	 * 
	 * @author ModelerOne
	 */
	public static enum CategoriesEnum
	{
		/**
		 * Instance for error distance 0.00 meters to 0.02 meters (exclusive)
		 */
		Category000(0.00, 0.02, "  0,  2"),
		/**
		 * Instance for error distance 0.02 meters to 0.04 meters (exclusive)
		 */
		Category002(0.02, 0.04, "  2,  4"),
		/**
		 * Instance for error distance 0.04 meters to 0.06 meters (exclusive)
		 */
		Category004(0.04, 0.06, "  4,  6"),
		/**
		 * Instance for error distance 0.06 meters to 0.08 meters (exclusive)
		 */
		Category006(0.06, 0.08, "  6,  8"),
		/**
		 * Instance for error distance 0.08 meters to 0.10 meters (exclusive)
		 */
		Category008(0.08, 0.10, "  8, 10"),
		/**
		 * Instance for error distance 0.10 meters to 0.12 meters (exclusive)
		 */
		Category010(0.10, 0.12, " 10, 12"),
		/**
		 * Instance for error distance 0.12 meters to 0.14 meters (exclusive)
		 */
		Category012(0.12, 0.14, " 12, 14"),
		/**
		 * Instance for error distance 0.14 meters to 0.16 meters (exclusive)
		 */
		Category014(0.14, 0.16, " 14, 16"),
		/**
		 * Instance for error distance 0.16 meters to 0.18 meters (exclusive)
		 */
		Category016(0.16, 0.18, " 16, 18"),
		/**
		 * Instance for error distance 0.18 meters to 0.20 meters (exclusive)
		 */
		Category018(0.18, 0.20, " 18, 20"),
		/**
		 * Instance for error distance 0.20 meters and greater
		 */
		Category020(0.20, 99.99, "  > 20 ");

		/**
		 * Category's min error distance
		 */
		double minErrorMeters;
		/**
		 * Category's max error distance
		 */
		double maxErrorMeters;
		/**
		 * Name of category, represented as min and max error distances in centimeters
		 */
		String catName;

		/**
		 * Private constructor
		 * 
		 * @param minErrorMeters min error in meters for this category
		 * @param maxErrorMeters max error in meters for this category
		 * @param catName        category name
		 */
		private CategoriesEnum(double minErrorMeters, double maxErrorMeters, String catName)
		{
			this.minErrorMeters = minErrorMeters;
			this.maxErrorMeters = maxErrorMeters;
			this.catName = catName;
		}

		/**
		 * Returns optional instance of category enumeration for specified error
		 * distance
		 * 
		 * @param errorDistance distance whose category is to be returned
		 * @return optional instance of category enumeration for specified error
		 *         distance
		 */
		static Optional<CategoriesEnum> of(DistanceMeters errorDistance)
		{
			Optional<CategoriesEnum> result = Optional.empty();
			CategoriesEnum[] values = values();
			int i = 0;
			while (result.isEmpty() && i < values.length)
			{
				if (errorDistance.greaterThanOrEqualTo(values[i].minErrorMeters) && errorDistance.lessThan(values[i].maxErrorMeters))
					result = Optional.of(values[i]);
				i++;
			}
			return result;
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
}
