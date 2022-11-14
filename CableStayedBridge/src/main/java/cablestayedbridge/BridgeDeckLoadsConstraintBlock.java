package cablestayedbridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import sysmlinjava.analysis.barcharts.BarChartConstraintBlock;
import sysmlinjava.analysis.barcharts.BarChartDefinition;
import sysmlinjava.analysis.barcharts.BarChartsDisplay;
import sysmlinjava.analysis.common.Axis;
import sysmlinjava.analysis.common.CategoriesAxis;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.ListOrdered;
import sysmlinjava.valuetypes.RReal;
import sysmlinjava.valuetypes.WeightPounds;

/**
 * Constraint block for the calculation (constraint) of the live loads on the
 * bridge deck of the {@code CableStayedBridge}. It maintains binding connectors
 * (via its parameter ports) with the input loads and it outputs data for a bar
 * chart display of the load value at each of the load points on the bridge. The
 * bar chart is updated for each time incremental change of the loads on the
 * bridge.
 * 
 * @author ModelerOne
 *
 */
public class BridgeDeckLoadsConstraintBlock extends BarChartConstraintBlock
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
	public BridgeDeckLoadsConstraintBlock()
	{
		super(new BarChartDefinition("Cable Stayed Bridge Loads", catAxis, yAxis, layerNames), BarChartsDisplay.udpPort, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onParameterChange(String paramID)
	{
		currentParamID = Optional.empty();
		if (!paramID.isEmpty())
			if (paramID.equals(cableLoadsKey))
			{
				ListOrdered<WeightPounds> cableLoads = ((ListOrdered<WeightPounds>)paramPorts.get(paramID).getValue());
				if (!cableLoads.isEmpty())
				{
					for (CablesEnum cableEnum: CablesEnum.values())
						categoryParams.get(0).get(cableEnum.categoryName).value = cableLoads.get(cableEnum.ordinal()).value;
					currentParamID = Optional.of(cableLoadsKey);
				}
				else
					logger.severe("empty cable -oads list from parameter port " + paramPorts.get(paramID).name.get());
			}
			else if (paramID.equals(cableAvailablesKey))
			{
				ListOrdered<WeightPounds> cableAvailables = ((ListOrdered<WeightPounds>)paramPorts.get(paramID).getValue());

				if (!cableAvailables.isEmpty())
				{
					for (CablesEnum cableEnum: CablesEnum.values())
						categoryParams.get(1).get(cableEnum.categoryName).value = cableAvailables.get(cableEnum.ordinal()).value;
					currentParamID = Optional.of(cableAvailablesKey);
				}
				else
					logger.severe("empty cable-availables list from parameter port " + paramPorts.get(paramID).name.get());
			}
			else
				logger.severe("unrecognized paramter ID " + paramID);
		else
			logger.severe("missing/blank parameter ID");
	}

	@Override
	protected void createConstraintParameters()
	{
		Map<String, RReal> cableLoadsMap = new HashMap<>();
		for (CablesEnum category : CablesEnum.values())
			cableLoadsMap.put(category.categoryName, new WeightPounds(0));
		categoryParams.add(cableLoadsMap);

		Map<String, RReal> cableAvailablesMap = new HashMap<>();
		for (CablesEnum category : CablesEnum.values())
			cableAvailablesMap.put(category.categoryName, new WeightPounds(0));
		categoryParams.add(cableAvailablesMap);
	}

	@Override
	protected void createConstraints()
	{
		constraint = () ->
		{
			// No constraint here. The bar chart is the constraint output
		};
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		SysMLConstraintParameterPortFunction cableLoadsPortFunction = (constraintParameterPort, contextBlock) ->
		{
			ListOrdered<WeightPounds> loads = new ListOrdered<>();
			((CableStayedBridge)contextBlock).cableLoads.forEach(load -> loads.add(new WeightPounds(load)));
			constraintParameterPort.updateParameterValue(loads);
		};
		paramPortFunctions.put(cableLoadsKey, cableLoadsPortFunction);

		SysMLConstraintParameterPortFunction cableAvailablesPortfunction = (constraintParameterPort, contextBlock) ->
		{
			ListOrdered<WeightPounds> availables = new ListOrdered<>();
			((CableStayedBridge)contextBlock).cableAvailables.forEach(available -> availables.add(new WeightPounds(available)));
			constraintParameterPort.updateParameterValue(availables);
		};
		paramPortFunctions.put(cableAvailablesKey, cableAvailablesPortfunction);
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		SysMLConstraintParameterPortFunction cableLoadsPortFunction = paramPortFunctions.get(cableLoadsKey);
		SysMLConstraintParameterPort cableLoadsPort = new SysMLConstraintParameterPort(this, cableLoadsPortFunction, cableLoadsKey);
		paramPorts.put(cableLoadsKey, cableLoadsPort);

		SysMLConstraintParameterPortFunction cableAvailablesPortFunction = paramPortFunctions.get(cableAvailablesKey);
		SysMLConstraintParameterPort cableAvailablesPort = new SysMLConstraintParameterPort(this, cableAvailablesPortFunction, cableAvailablesKey);
		paramPorts.put(cableAvailablesKey, cableAvailablesPort);
	}

	/**
	 * Enum for the categories of suspension points
	 * 
	 * @author ModelerOne
	 *
	 */
	public static enum CablesEnum
	{
		NW0toSE9("NW[0]-SE[9]"),
		NW1toSE8("NW[1]-SE[8]"),
		NW2toSE7("NW[2]-SE[7]"),
		NW3toSE6("NW[3]-SE[6]"),
		NW4toSE5("NW[4]-SE[5]"),
		SW4toNE5("SW[4]-NE[5]"),
		SW3toNE6("SW[3]-NE[6]"),
		SW2toNE7("SW[2]-NE[7]"),
		SW1toNE8("SW[1]-NE[8]"),
		SW0toNE9("SW[0]-NE[9]");

		/**
		 * Name of category
		 */
		String categoryName;

		/**
		 * Private constructor
		 * 
		 * @param categoryName name of category (cable name based on bridge deck connection points)
		 */
		private CablesEnum(String categoryName)
		{
			this.categoryName = categoryName;
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
