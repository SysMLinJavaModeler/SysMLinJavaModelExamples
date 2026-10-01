package cablestayedbridge;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import cablestayedbridge.ports.DeckToCableLoadTransmitter;
import cablestayedbridge.ports.DeckToGroundLoadTransmitter;
import cablestayedbridge.ports.DeckToPylonLoadTransmitter;
import cablestayedbridge.ports.VehicleToDeckLoadReceiver;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.DirectionDegrees;
import sysmlinjava.attributetypes.DistanceFeet;
import sysmlinjava.attributetypes.ListOrdered;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.WeightPounds;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalEvent;

/**
 * SysMLinJava block-based representation of a cable-stayed bridge deck. The
 * deck supports all vehicles on the bridge and is comprised of eleven sections,
 * each of which is suspended by a cable, pylon point, and/or ground anchor
 * point at each corner of the section.
 * <p>
 * The {@code BridgeDeck} is a load bearing component of the bridge. It has a
 * number of full ports that receive the loads of the vehicles and transmit the
 * loads of the deck sections to the other components of the bridge, i.e. to the
 * cables, pylons, and ground anchors. The {@code BridgeDeck} is characterized
 * by its values for suspension points, length, width, and weight and its flows
 * of weight of the vehicles on the deck and its weight on the cables, pylon,
 * and ground anchors. The {@code BridgeDeck} behaves as a
 * {@code LoadBearingComponentStateMachine} and operates synchronously with the
 * other components of the bridge.
 * 
 * @author ModelerOne
 */
public class BridgeDeck extends SysMLPart implements LoadBearingComponent
{
	/**
	 * Full ports for transmitting loads to the western ground anchor connections
	 * with the deck
	 */
	@Port
	List<DeckToGroundLoadTransmitter> groundConnectionsWest;
	/**
	 * Full ports for transmitting loads to the cables along northwestern edge of
	 * deck
	 */
	@Port
	List<DeckToCableLoadTransmitter> cableConnectionsNorthWest;
	/**
	 * Full ports for transmitting loads to the cables along southwestern edge of
	 * deck
	 */
	@Port
	List<DeckToCableLoadTransmitter> cableConnectionsSouthWest;
	/**
	 * Full ports for transmitting loads to the pylon connections with the deck
	 */
	@Port
	List<DeckToPylonLoadTransmitter> pylonConnections;
	/**
	 * Full ports for transmitting loads to the cables along northeastern edge of
	 * deck
	 */
	@Port
	List<DeckToCableLoadTransmitter> cableConnectionsNorthEast;
	/**
	 * Full ports for transmitting loads to the cables along southeastern edge of
	 * deck
	 */
	@Port
	List<DeckToCableLoadTransmitter> cableConnectionsSouthEast;
	/**
	 * Full port for transmitting loads to the eastern ground anchor connections
	 * with the deck
	 */
	@Port
	List<DeckToGroundLoadTransmitter> groundConnectionsEast;
	/**
	 * Full port for the deck to receive the loads of the eastbound vehicles
	 */
	@Port
	List<VehicleToDeckLoadReceiver> vehiclesEastBound;
	/**
	 * Full port for the deck to receive the loads of the westbound vehicles
	 */
	@Port
	List<VehicleToDeckLoadReceiver> vehiclesWestBound;

	/**
	 * Parts for the sections of the bridge deck, one between each adjacent pair of
	 * load points - cables, pylon, and east and west ground anchors
	 */
	@Part
	List<BridgeDeckSection> sections;

	/**
	 * Flows of the weights of the deck suspended by the cable on the north side of
	 * the deck
	 */
	@Attribute
	ListOrdered<WeightPounds> suspendedWeightsNorth;
	/**
	 * Flows of the weights of the deck suspended by the cable on the south side of
	 * the deck
	 */
	@Attribute
	ListOrdered<WeightPounds> suspendedWeightsSouth;
	/**
	 * Flows of the weights of the deck suspended by the pylon
	 */
	@Attribute
	ListOrdered<WeightPounds> pylonWeights;
	/**
	 * Flows of the weights of the deck suspended by the western ground anchor
	 */
	@Attribute
	ListOrdered<WeightPounds> groundWeightsWest;
	/**
	 * Flows of the weights of the deck suspended by the eastern ground anchor
	 */
	@Attribute
	ListOrdered<WeightPounds> groundWeightsEast;
	/**
	 * Ordered list of the flows of the forces (weights) of the deck on each of the
	 * suspension cables
	 */
	@Attribute
	public ListOrdered<WeightPounds> cableForces;
	/**
	 * Ordered list of the values of the breaking strengths of the suspension cables
	 */
	@Attribute
	public ListOrdered<WeightPounds> cableStrengths;

	/**
	 * Locations of cable suspension points on the north and south sides of the deck
	 */
	@Attribute
	SuspensionPoints suspensionPoints;
	/**
	 * Locations of ground anchor points on the west end of the deck
	 */
	@Attribute
	ListOrdered<Point2D> groundPointsWest;
	/**
	 * Locations of ground anchor points on the east end of the deck
	 */
	@Attribute
	ListOrdered<Point2D> groundPointsEast;
	/**
	 * Locations of the mount points on the pylon
	 */
	@Attribute
	ListOrdered<Point2D> pylonPoints;
	/**
	 * Value for the length of the deck
	 */
	@Attribute
	DistanceFeet length;
	/**
	 * Value for the width of the deck
	 */
	@Attribute
	DistanceFeet width;
	/**
	 * Value for the deck's location at its northeast corner
	 */
	@Attribute
	Point2D neCorner;
	/**
	 * Value for the deck's location at its southwest corner
	 */
	@Attribute
	Point2D swCorner;

	/**
	 * Hyperlink for bridge deck specification document
	 */
	@Hyperlink
	SysMLHyperlink deckSpecification;

	/**
	 * Constructor
	 * 
	 * @param bridge the cable stayed bridge of which this bridge deck is a part
	 */
	public BridgeDeck(CableStayedBridge bridge)
	{
		super(Optional.of(bridge), "BridgeDeck", 0L);
	}

	@Action
	@Override
	public void onLoad(Load load)
	{
		// If the load is on the bridge deck
		if (load.location.isWithinRectangle(neCorner, swCorner, true, false))
		{
			// Get the deck section upon which this load is located to accept/receive the
			// load)
			boolean found = false;
			ListIterator<BridgeDeckSection> deckSections = sections.listIterator();
			while (!found && deckSections.hasNext())
				if (deckSections.next().acceptLoad(load))
					found = true;
		}
	}

	@Action
	@Override
	public void onLoaded(Load load)
	{
		// On last load
		onLoad(load);

		// Zero all of the loads before accumulating weights to them
		logger.info(String.format("load:%s", load.location));
		suspendedWeightsNorth.list.forEach(weight -> weight.zero());
		suspendedWeightsSouth.list.forEach(weight -> weight.zero());
		groundWeightsWest.list.forEach(weight -> weight.zero());
		groundWeightsEast.list.forEach(weight -> weight.zero());
		pylonWeights.list.forEach(weight -> weight.zero());

		// Add the loads presented by each section to each of the load points along each
		// of the two edges of the deck
		sections.get(0).addLoads(groundWeightsWest.get(0), suspendedWeightsNorth.get(0), groundWeightsWest.get(1), suspendedWeightsSouth.get(0));
		sections.get(1).addLoads(suspendedWeightsNorth.get(0), suspendedWeightsNorth.get(1), suspendedWeightsSouth.get(0), suspendedWeightsSouth.get(1));
		sections.get(2).addLoads(suspendedWeightsNorth.get(1), suspendedWeightsNorth.get(2), suspendedWeightsSouth.get(1), suspendedWeightsSouth.get(2));
		sections.get(3).addLoads(suspendedWeightsNorth.get(2), suspendedWeightsNorth.get(3), suspendedWeightsSouth.get(2), suspendedWeightsSouth.get(3));
		sections.get(4).addLoads(suspendedWeightsNorth.get(3), suspendedWeightsNorth.get(4), suspendedWeightsSouth.get(3), suspendedWeightsSouth.get(4));
		sections.get(5).addLoads(suspendedWeightsNorth.get(4), pylonWeights.get(0), suspendedWeightsSouth.get(4), pylonWeights.get(1));
		sections.get(6).addLoads(pylonWeights.get(0), suspendedWeightsNorth.get(5), pylonWeights.get(1), suspendedWeightsSouth.get(5));
		sections.get(7).addLoads(suspendedWeightsNorth.get(5), suspendedWeightsNorth.get(6), suspendedWeightsSouth.get(5), suspendedWeightsSouth.get(6));
		sections.get(8).addLoads(suspendedWeightsNorth.get(6), suspendedWeightsNorth.get(7), suspendedWeightsSouth.get(6), suspendedWeightsSouth.get(7));
		sections.get(9).addLoads(suspendedWeightsNorth.get(7), suspendedWeightsNorth.get(8), suspendedWeightsSouth.get(7), suspendedWeightsSouth.get(8));
		sections.get(10).addLoads(suspendedWeightsNorth.get(8), suspendedWeightsNorth.get(9), suspendedWeightsSouth.get(8), suspendedWeightsSouth.get(9));
		sections.get(11).addLoads(suspendedWeightsNorth.get(9), groundWeightsEast.get(0), suspendedWeightsSouth.get(9), groundWeightsEast.get(1));

		// Transmit the deck loads to the western ground anchor
		groundConnectionsWest.get(0).transmit(new Load(0, groundWeightsWest.get(0), groundPointsWest.get(0), DirectionDegrees.north, false));
		groundConnectionsWest.get(1).transmit(new Load(1, groundWeightsWest.get(1), groundPointsWest.get(1), DirectionDegrees.south, true));

		// Transmit the deck loads to the cables along the northwest edge of the deck
		cableConnectionsNorthWest.get(0).transmit(new Load(0, suspendedWeightsNorth.get(0), suspensionPoints.north.get(0), DirectionDegrees.west, false));
		cableConnectionsNorthWest.get(1).transmit(new Load(1, suspendedWeightsNorth.get(1), suspensionPoints.north.get(1), DirectionDegrees.west, false));
		cableConnectionsNorthWest.get(2).transmit(new Load(2, suspendedWeightsNorth.get(2), suspensionPoints.north.get(2), DirectionDegrees.west, false));
		cableConnectionsNorthWest.get(3).transmit(new Load(3, suspendedWeightsNorth.get(3), suspensionPoints.north.get(3), DirectionDegrees.west, false));
		cableConnectionsNorthWest.get(4).transmit(new Load(4, suspendedWeightsNorth.get(4), suspensionPoints.north.get(4), DirectionDegrees.west, false));

		// Transmit the deck loads to the cables along the southwest edge of the deck
		cableConnectionsSouthWest.get(0).transmit(new Load(0, suspendedWeightsSouth.get(0), suspensionPoints.south.get(0), DirectionDegrees.east, false));
		cableConnectionsSouthWest.get(1).transmit(new Load(1, suspendedWeightsSouth.get(1), suspensionPoints.south.get(1), DirectionDegrees.east, false));
		cableConnectionsSouthWest.get(2).transmit(new Load(2, suspendedWeightsSouth.get(2), suspensionPoints.south.get(2), DirectionDegrees.east, false));
		cableConnectionsSouthWest.get(3).transmit(new Load(3, suspendedWeightsSouth.get(3), suspensionPoints.south.get(3), DirectionDegrees.east, false));
		cableConnectionsSouthWest.get(4).transmit(new Load(4, suspendedWeightsSouth.get(4), suspensionPoints.south.get(4), DirectionDegrees.east, false));

		// Transmit the deck loads to the cables along the northeast edge of the deck
		cableConnectionsNorthEast.get(0).transmit(new Load(0, suspendedWeightsNorth.get(5), suspensionPoints.north.get(5), DirectionDegrees.west, true));
		cableConnectionsNorthEast.get(1).transmit(new Load(1, suspendedWeightsNorth.get(6), suspensionPoints.north.get(6), DirectionDegrees.west, true));
		cableConnectionsNorthEast.get(2).transmit(new Load(2, suspendedWeightsNorth.get(7), suspensionPoints.north.get(7), DirectionDegrees.west, true));
		cableConnectionsNorthEast.get(3).transmit(new Load(3, suspendedWeightsNorth.get(8), suspensionPoints.north.get(8), DirectionDegrees.west, true));
		cableConnectionsNorthEast.get(4).transmit(new Load(4, suspendedWeightsNorth.get(9), suspensionPoints.north.get(9), DirectionDegrees.west, true));

		// Transmit the deck loads to the cables along the southeast edge of the deck
		cableConnectionsSouthEast.get(0).transmit(new Load(0, suspendedWeightsSouth.get(5), suspensionPoints.south.get(5), DirectionDegrees.east, true));
		cableConnectionsSouthEast.get(1).transmit(new Load(1, suspendedWeightsSouth.get(6), suspensionPoints.south.get(6), DirectionDegrees.east, true));
		cableConnectionsSouthEast.get(2).transmit(new Load(2, suspendedWeightsSouth.get(7), suspensionPoints.south.get(7), DirectionDegrees.east, true));
		cableConnectionsSouthEast.get(3).transmit(new Load(3, suspendedWeightsSouth.get(8), suspensionPoints.south.get(8), DirectionDegrees.east, true));
		cableConnectionsSouthEast.get(4).transmit(new Load(4, suspendedWeightsSouth.get(9), suspensionPoints.south.get(9), DirectionDegrees.east, true));

		// Transmit the deck loads to the eastern ground anchor
		groundConnectionsEast.get(0).transmit(new Load(0, groundWeightsEast.get(0), groundPointsEast.get(0), DirectionDegrees.north, false));
		groundConnectionsEast.get(1).transmit(new Load(1, groundWeightsEast.get(1), groundPointsEast.get(1), DirectionDegrees.south, true));

		// Transmit the deck loads to the pylon
		pylonConnections.get(0).transmit(new Load(pylonWeights.get(0), name.get(), false));
		pylonConnections.get(1).transmit(new Load(pylonWeights.get(1), name.get(), true));
	}

	@Override
	public void onFailed(FailureEvent failure)
	{
		logger.info(name.get() + " failed");
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createAttributes()
	{
		super.createAttributes();
		width = new DistanceFeet(90);
		length = new DistanceFeet(600);
		suspensionPoints = new SuspensionPoints();

		groundPointsWest = new ListOrdered<>(List.of(new Point2D(0, 90), new Point2D(0, 0)));
		groundPointsEast = new ListOrdered<>(List.of(new Point2D(600, 90), new Point2D(600, 0)));

		pylonPoints = new ListOrdered<>(List.of(new Point2D(300, 90), new Point2D(300, 0)));

		neCorner = new Point2D(600, 90);
		swCorner = new Point2D(0, 0);
		suspendedWeightsNorth = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(
		0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0)));
		suspendedWeightsSouth = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(
		0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0)));

		groundWeightsWest = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0)));
		groundWeightsEast = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0)));

		pylonWeights = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0)));
	}

	@Override
	protected void createParts()
	{
		super.createParts();
		sections = List.of(
			new BridgeDeckSection( 0, suspensionPoints.north.get(0),       groundPointsWest.get(1)),
			new BridgeDeckSection( 1, suspensionPoints.north.get(1), suspensionPoints.south.get(0)),
			new BridgeDeckSection( 2, suspensionPoints.north.get(2), suspensionPoints.south.get(1)),
			new BridgeDeckSection( 3, suspensionPoints.north.get(3), suspensionPoints.south.get(2)),
			new BridgeDeckSection( 4, suspensionPoints.north.get(4), suspensionPoints.south.get(3)),
			new BridgeDeckSection( 5,            pylonPoints.get(0), suspensionPoints.south.get(4)),
			new BridgeDeckSection( 6, suspensionPoints.north.get(5),            pylonPoints.get(1)),
			new BridgeDeckSection( 7, suspensionPoints.north.get(6), suspensionPoints.south.get(5)),
			new BridgeDeckSection( 8, suspensionPoints.north.get(7), suspensionPoints.south.get(6)),
			new BridgeDeckSection( 9, suspensionPoints.north.get(8), suspensionPoints.south.get(7)),
			new BridgeDeckSection(10, suspensionPoints.north.get(9),	suspensionPoints.south.get(8)),
			new BridgeDeckSection(11,       groundPointsEast.get(0), suspensionPoints.south.get(9)));
	}

	@Override
	protected void createPorts()
	{
		groundConnectionsWest = List.of(
			new DeckToGroundLoadTransmitter(this, 0L, "groundConnectionWestNorth"),
			new DeckToGroundLoadTransmitter(this, 1L,	"groundConnectionWestSouth"));

		cableConnectionsNorthWest = List.of(
			new DeckToCableLoadTransmitter(this, 0L, "cableConnection0NorthWest"),
			new DeckToCableLoadTransmitter(this, 1L, "cableConnection1NorthWest"),
			new DeckToCableLoadTransmitter(this, 2L, "cableConnection2NorthWest"),
			new DeckToCableLoadTransmitter(this, 3L, "cableConnection3NorthWest"),
			new DeckToCableLoadTransmitter(this, 4L, "cableConnection4NorthWest"));

		cableConnectionsNorthEast = List.of(
			new DeckToCableLoadTransmitter(this, 0L, "cableConnection0NorthEast"),
			new DeckToCableLoadTransmitter(this, 1L, "cableConnection1NorthEast"),
			new DeckToCableLoadTransmitter(this, 2L, "cableConnection2NorthEast"),
			new DeckToCableLoadTransmitter(this, 3L, "cableConnection3NorthEast"),
			new DeckToCableLoadTransmitter(this, 4L, "cableConnection4NorthEast"));

		cableConnectionsSouthWest = List.of(
			new DeckToCableLoadTransmitter(this, 0L, "cableConnection0SouthWest"),
			new DeckToCableLoadTransmitter(this, 1L, "cableConnection1SouthWest"),
			new DeckToCableLoadTransmitter(this, 2L, "cableConnection2SouthWest"),
			new DeckToCableLoadTransmitter(this, 3L, "cableConnection3SouthWest"),
			new DeckToCableLoadTransmitter(this, 4L, "cableConnection4SouthWest"));

		cableConnectionsSouthEast = List.of(
			new DeckToCableLoadTransmitter(this, 0L, "cableConnection0SouthEast"),
			new DeckToCableLoadTransmitter(this, 1L, "cableConnection1SouthEast"),
			new DeckToCableLoadTransmitter(this, 2L, "cableConnection2SouthEast"),
			new DeckToCableLoadTransmitter(this, 3L, "cableConnection3SouthEast"),
			new DeckToCableLoadTransmitter(this, 4L, "cableConnection4SouthEast"));

		pylonConnections = List.of(
		new DeckToPylonLoadTransmitter(this, 0L, "pylonConnectionNorth"),
		new DeckToPylonLoadTransmitter(this, 1L, "pylonConnectionSouth"));

		groundConnectionsEast = List.of(
			new DeckToGroundLoadTransmitter(this, 0L, "groundConnectionEastNorth"),
			new DeckToGroundLoadTransmitter(this, 0L, "groundConnectionEastSouth"));

		vehiclesEastBound = new ArrayList<>();
		vehiclesWestBound = new ArrayList<>();
		
		for (long index = 0; index < Vehicles.directionBoundCount; index++)
		{
			vehiclesEastBound.add(new VehicleToDeckLoadReceiver(context.get(), index, String.format("VehicleEB%d", index)));
			vehiclesWestBound.add(new VehicleToDeckLoadReceiver(context.get(), index, String.format("VehicleWB%d", index)));
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new LoadBearingComponentStateMachine(this, false, "BridgeDeckStateMachine"));
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		deckSpecification = new SysMLHyperlink("Cable-Stayed Bridge Deck Specification", "http://BridgeDeckBuilders.com/Specs/CableStayedBridgeDeckSpecification.pdf");
	}
}
