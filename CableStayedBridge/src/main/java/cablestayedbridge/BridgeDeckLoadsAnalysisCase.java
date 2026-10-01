package cablestayedbridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sysmlinjava.attributetypes.KeyValueMap;
import sysmlinjava.attributetypes.ListOrdered;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.WeightPounds;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.views.barcharts.BarChartAnalysisCase;
import sysmlinjava.views.barcharts.BarChartDefinition;
import sysmlinjava.views.barcharts.BarChartsDisplay;
import sysmlinjava.views.common.Axis;
import sysmlinjava.views.common.CategoriesAxis;

/**
 * Constraint block for the calculation (constraint) of the live loads on the
 * bridge deck of the {@code CableStayedBridge}. It maintains binding connectors
 * (via its parameter ports) with the input loads and it outputs data for a bar
 * chart display of the load value at each of the load points on the bridge. The
 * bar chart is updated for each time incremental change of the loads on the
 * bridge.
 * 
 * @author ModelerOne
 */
public class BridgeDeckLoadsAnalysisCase extends BarChartAnalysisCase
{
	/**
	 * Categories of the output bar chart, i.e. cables at the distances along the
	 * bridge deck of the load points
	 */
	static final public CategoriesAxis catAxis = new CategoriesAxis("Cable (bridge deck connection points)", CablesEnum.namesList());
	/**
	 * Axis (Y) for the value of the cable loads and available capacities
	 */
	static final public Axis yAxis = new Axis("Load and Available Capacity", "pounds", Optional.of(800_000.0), Optional.of(1_400_000.0), 100_000, 10);

	/**
	 * String value for name/key to bound value for constraint parameter that
	 * represents the loads on the cables
	 */
	final static String cableLoadsKey = "cableLoads";
	/**
	 * String value for name/key to bound value for constraint parameter that
	 * represents the available load capacities of the cables
	 */
	final static String cableAvailablesKey = "cableAvailables";
	/**
	 * List of the name values of the bar layers
	 */
	final static ArrayList<String> layerNames = new ArrayList<>(List.of("Load", "Available"));

	/**
	 * Constructor
	 */
	public BridgeDeckLoadsAnalysisCase()
	{
		super(new BarChartDefinition("Cable Stayed Bridge Loads", catAxis, yAxis, layerNames), BarChartsDisplay.udpPort, true);
	}

	@Override
	// Performs the action to analyze the cable loads. Whereas the analysis is the
	// bar chart display of the loads, nothing need be performed here beyond
	// super transmitting current bar chart data to the display.
	public void perform()
	{
		super.perform();
	}

	/**
	 * Override of {@code BarChartAnalysisCase}'s {@code onParameterChange()}. This
	 * method is identical to the {@code ParametricAnalysisCase}'s
	 * {@code onParameterChange()} which is overridden by the
	 * {@code BarChartAnlsysisCase}. Whereas the latter case assumes all bound
	 * parameters are of type {@code RReal}, the model analysis uses lists of
	 * weights for bound parameters, thereby requiring the former's more general
	 * method.
	 */
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
					onParameterChange(currentParamID.get(), currentParam);
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
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		if (paramID.equals(cableLoadsKey) && paramValue instanceof ListOrdered cableLoads)
		{
			if (!cableLoads.isEmpty())
			{
				for (CablesEnum cableEnum : CablesEnum.values())
					categoryParams.get(0).get(cableEnum.categoryName).value = ((WeightPounds) cableLoads.get(cableEnum.ordinal())).value;
				currentParamID = Optional.of(cableLoadsKey);
			}
			else
				logger.severe("empty cable loads list from parameter port " + paramConnectors.get(paramID).name.get());
		}
		else if (paramID.equals(cableAvailablesKey) && paramValue instanceof ListOrdered cableAvailables)
		{
			if (!cableAvailables.isEmpty())
			{
				for (CablesEnum cableEnum : CablesEnum.values())
					categoryParams.get(1).get(cableEnum.categoryName).value = ((WeightPounds) cableAvailables.get(cableEnum.ordinal())).value;
				currentParamID = Optional.of(cableAvailablesKey);
			}
			else
				logger.severe("empty cable-availables list from parameter port " + paramConnectors.get(paramID).name.get());
		}
		else
			logger.severe("unrecognized paramter ID " + paramID);
	}

	@Override
	protected void createParameters()
	{
		categoryParams = ListOrdered.of(KeyValueMap.of(CablesEnum.namesList(), List.of(new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(
		0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(
		0))), KeyValueMap.of(CablesEnum.namesList(), List.of(new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(
		0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0))));
	}

	@Override
	protected void createObjective()
	{
		objective = CableStayedBridgeAnalysisRequirements.bridgeDeckLoadsAnalysisObjective;
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(BridgeDeck.class);
	}

	@Override
	protected void createActors()
	{
		actors = List.of();
	}

	@Override
	protected void createResult()
	{
		categoryParams = new ListOrdered<KeyValueMap<String, RReal>>();
	}

	/**
	 * Enum for the categories of suspension points
	 * 
	 * @author ModelerOne
	 */
	public static enum CablesEnum
	{
		/**
		 * Cable from northwest deck point 0 to southeast deck point 9
		 */
		NW0toSE9("NW[0]-SE[9]"),
		/**
		 * Cable from northwest deck point 1 to southeast deck point 8
		 */
		NW1toSE8("NW[1]-SE[8]"),
		/**
		 * Cable from northwest deck point 2 to southeast deck point 7
		 */
		NW2toSE7("NW[2]-SE[7]"),
		/**
		 * Cable from northwest deck point 3 to southeast deck point 6
		 */
		NW3toSE6("NW[3]-SE[6]"),
		/**
		 * Cable from northwest deck point 4 to southeast deck point 5
		 */
		NW4toSE5("NW[4]-SE[5]"),
		/**
		 * Cable from northwest deck point 4 to southeast deck point 5
		 */
		SW4toNE5("SW[4]-NE[5]"),
		/**
		 * Cable from southwest deck point 3 to northeast deck point 6
		 */
		SW3toNE6("SW[3]-NE[6]"),
		/**
		 * Cable from southwest deck point 2 to northeast deck point 7
		 */
		SW2toNE7("SW[2]-NE[7]"),
		/**
		 * Cable from southwest deck point 1 to northeast deck point 8
		 */
		SW1toNE8("SW[1]-NE[8]"),
		/**
		 * Cable from southwest deck point 0 to northeast deck point 9
		 */
		SW0toNE9("SW[0]-NE[9]");

		/**
		 * Name of category
		 */
		String categoryName;

		/**
		 * Private constructor
		 * 
		 * @param categoryName name of category (cable name based on bridge deck
		 *                     connection points)
		 */
		private CablesEnum(String categoryName)
		{
			this.categoryName = categoryName;
		}

		static List<Integer> ordinals()
		{
			return List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		}

		/**
		 * Returns list of category names
		 * 
		 * @return list of names
		 */
		public static List<String> namesList()
		{
			List<String> result = new ArrayList<>();
			for (CablesEnum cableEnum : values())
				result.add(cableEnum.categoryName);
			return result;
		}
	}
}
