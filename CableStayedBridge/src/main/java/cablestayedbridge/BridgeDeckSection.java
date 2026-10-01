package cablestayedbridge;

import java.util.StringJoiner;

import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.DistanceFeet;
import sysmlinjava.attributetypes.KeyValueMap;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.WeightPounds;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.parts.SysMLPart;

/**
 * SysMLinJava block-based representation of a section of the deck of a
 * cable-stayed bridge. The {@code BridgeDeckSection} is a section of the deck
 * bounded by suspension/load bearing points at the four corners of the section.
 * Vehicles on the bridge transmit their weights/loads to points on the section.
 * The section concurrently transmits those loads to the cables at the
 * suspension points at the corners of the section. The
 * {@code BridgeDeckSection} receives thse vehicle loads and transfers them to
 * the cables.
 * <p>
 * The {@code BridgeDeckSection} is characterized by its values for the vehicle
 * loads on the section, locations on the deck of the corners (suspension
 * points) of the section, and the section's weight, which is also transfered to
 * the suspension points and cables.
 * 
 * @author ModelerOne
 *
 */
public class BridgeDeckSection extends SysMLPart
{
	/**
	 * Value for the collection of vehicle loads currently on the section
	 */
	@Attribute
	KeyValueMap<Integer, Load> sectionLoads;
	/**
	 * Value for the location on the bridge deck of the section's northeast corner
	 */
	@Attribute
	Point2D northEastCorner;
	/**
	 * Value for the location on the bridge deck of the section's southwest corner
	 */
	@Attribute
	Point2D southWestCorner;
	/**
	 * Value for the sections length (east-west)
	 */
	@Attribute
	DistanceFeet sectionLength;
	/**
	 * Value for the sections width (north-south)
	 */
	@Attribute
	DistanceFeet sectionWidth;
	/**
	 * Value for the bridge section's weight
	 */
	@Attribute
	WeightPounds weight;

	/**
	 * Hyperlink to deck section specification document
	 */
	@Hyperlink
	SysMLHyperlink deckSectionSpecification;

	/**
	 * Constructor
	 * 
	 * @param index           index of this section into a list/array of sections
	 * 
	 * @param northEastCorner location on the bridge deck of the section's northeast
	 *                        corner
	 * @param southWestCorner location on the bridge deck of the section's southwest
	 *                        corner
	 */
	public BridgeDeckSection(Integer index, Point2D northEastCorner, Point2D southWestCorner)
	{
		super(String.format("BridgeSection%d", index), (long)index);
		this.northEastCorner.setValue(northEastCorner);
		this.southWestCorner.setValue(southWestCorner);
	}

	/**
	 * Attempts to determine if (accept) the specified load as being within this
	 * section's bounds and return whether or not it is, adding the load to the
	 * section's collection of loads if it is.
	 * 
	 * @param load load to be accepted or not
	 * @return true if load is accepted by the section, false otherwise
	 */
	@Calculation
	public boolean acceptLoad(Load load)
	{
		boolean accepted = false;
		if (load.location.isWithinRectangle(northEastCorner, southWestCorner, true, false))
		{
			Load currentLoad = sectionLoads.get(load.id.intValue());
			if (currentLoad != null)
				currentLoad.weight.setValue(load.weight);
			else
				sectionLoads.put(load.id.intValue(), load);
			accepted = true;
		}
		else
			sectionLoads.remove(load.id.intValue());
		return accepted;
	}

	/**
	 * Adds the sections load to the specified corner suspension points' (cables')
	 * loads.
	 * 
	 * @param nwLoad load to which the sections load at its northwest corner is to
	 *               be added
	 * @param neLoad load to which the sections load at its northeast corner is to
	 *               be added
	 * @param swLoad load to which the sections load at its southwest corner is to
	 *               be added
	 * @param seLoad load to which the sections load at its southeast corner is to
	 *               be added
	 */
	@Calculation
	public void addLoads(WeightPounds nwLoad, WeightPounds neLoad, WeightPounds swLoad, WeightPounds seLoad)
	{
		double bareDeckCornerLoad = weight.value * 0.25;
		StringJoiner joiner = new StringJoiner("\n");
		sectionLoads.map.forEach((i, load) -> joiner.add(String.format("%d) %,4.2f", i, load.weight.value)));

		nwLoad.add(bareDeckCornerLoad);
		neLoad.add(bareDeckCornerLoad);
		swLoad.add(bareDeckCornerLoad);
		seLoad.add(bareDeckCornerLoad);

		for (Load vehicleLoad : sectionLoads.values())
		{
			double xPropEast = (vehicleLoad.location.xValue - southWestCorner.xValue) / sectionLength.value;
			double xPropWest = 1.0 - xPropEast;
			double yPropNorth = (vehicleLoad.location.yValue - southWestCorner.yValue) / sectionWidth.value;
			double yPropSouth = 1.0 - yPropNorth;

			nwLoad.add(vehicleLoad.weight.value * yPropNorth * xPropWest);
			neLoad.add(vehicleLoad.weight.value * yPropNorth * xPropEast);
			swLoad.add(vehicleLoad.weight.value * yPropSouth * xPropWest);
			seLoad.add(vehicleLoad.weight.value * yPropSouth * xPropEast);
		}
		sectionLoads.clear();
	}

	@Override
	protected void createAttributes()
	{
		northEastCorner = new Point2D(0, 0);
		southWestCorner = new Point2D(0, 0);
		sectionLength = new DistanceFeet(50);
		sectionWidth = new DistanceFeet(90);
		weight = new WeightPounds(800_000); // 150 lb/ft3 concrete * 50 * 90 * 0.9 ft3 thick + 490 lb/ft3 steel * 50 * 90 *
		// 15 * 0.01 ft3
		sectionLoads = new KeyValueMap<>();
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		deckSectionSpecification = new SysMLHyperlink("Cable-Stayed Bridge Deck Sectionr Specification", "http://SuspensionBridgeBuilders.com/Specs/CableStayedBridgeDeckSectionSpecification.pdf");
	}
}
