package cablestayedbridge;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import cablestayedbridge.ports.DeckToCableLoadTransmitter;
import cablestayedbridge.ports.DeckToGroundLoadTransmitter;
import cablestayedbridge.ports.DeckToPylonLoadTransmitter;
import cablestayedbridge.ports.VehicleToDeckLoadReceiver;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.DirectionDegrees;
import sysmlinjava.valuetypes.DistanceFeet;
import sysmlinjava.valuetypes.ListOrdered;
import sysmlinjava.valuetypes.Point2D;
import sysmlinjava.valuetypes.WeightPounds;

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
 *
 */
public class BridgeDeck extends SysMLBlock implements LoadBearingComponent
{
	/**
	 * Full ports for transmitting loads to the western ground anchor connections
	 * with the deck
	 */
	@FullPort
	List<DeckToGroundLoadTransmitter> groundConnectionsWest;
	/**
	 * Full ports for transmitting loads to the cables along northwestern edge of
	 * deck
	 */
	@FullPort
	List<DeckToCableLoadTransmitter> cableConnectionsNorthWest;
	/**
	 * Full ports for transmitting loads to the cables along southwestern edge of
	 * deck
	 */
	@FullPort
	List<DeckToCableLoadTransmitter> cableConnectionsSouthWest;
	/**
	 * Full ports for transmitting loads to the pylon connections with the deck
	 */
	@FullPort
	List<DeckToPylonLoadTransmitter> pylonConnections;
	/**
	 * Full ports for transmitting loads to the cables along northeastern edge of
	 * deck
	 */
	@FullPort
	List<DeckToCableLoadTransmitter> cableConnectionsNorthEast;
	/**
	 * Full ports for transmitting loads to the cables along southeastern edge of
	 * deck
	 */
	@FullPort
	List<DeckToCableLoadTransmitter> cableConnectionsSouthEast;
	/**
	 * Full port for transmitting loads to the eastern ground anchor connections
	 * with the deck
	 */
	@FullPort
	List<DeckToGroundLoadTransmitter> groundConnectionsEast;
	/**
	 * Full port for the deck to receive the loads of the eastbound vehicles
	 */
	@FullPort
	List<VehicleToDeckLoadReceiver> vehiclesEastBound;
	/**
	 * Full port for the deck to receive the loads of the westbound vehicles
	 */
	@FullPort
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
	@Flow
	ListOrdered<WeightPounds> suspendedWeightsNorth;
	/**
	 * Flows of the weights of the deck suspended by the cable on the south side of
	 * the deck
	 */
	@Flow
	ListOrdered<WeightPounds> suspendedWeightsSouth;
	/**
	 * Flows of the weights of the deck suspended by the pylon
	 */
	@Flow
	ListOrdered<WeightPounds> pylonWeights;
	/**
	 * Flows of the weights of the deck suspended by the western ground anchor
	 */
	@Flow
	ListOrdered<WeightPounds> groundWeightsWest;
	/**
	 * Flows of the weights of the deck suspended by the eastern ground anchor
	 */
	@Flow
	ListOrdered<WeightPounds> groundWeightsEast;
	/**
	 * Ordered list of the flows of the forces (weights) of the deck on each of the
	 * suspension cables
	 */
	@Flow
	public ListOrdered<WeightPounds> cableForces;
	/**
	 * Ordered list of the values of the breaking strengths of the suspension cables
	 */
	@Flow
	public ListOrdered<WeightPounds> cableStrengths;

	/**
	 * Locations of cable suspension points on the north side of the deck
	 */
	@Value
	ListOrdered<Point2D> suspensionPointsNorth;
	/**
	 * Locations of cable suspension points on the south side of the deck
	 */
	@Value
	ListOrdered<Point2D> suspensionPointsSouth;
	/**
	 * Locations of ground anchor points on the west end of the deck
	 */
	@Value
	ListOrdered<Point2D> groundPointsWest;
	/**
	 * Locations of ground anchor points on the east end of the deck
	 */
	@Value
	ListOrdered<Point2D> groundPointsEast;
	/**
	 * Locations of the mount points on the pylon
	 */
	@Value
	ListOrdered<Point2D> pylonPoints;
	/**
	 * Value for the length of the deck
	 */
	@Value
	DistanceFeet length;
	/**
	 * Value for the width of the deck
	 */
	@Value
	DistanceFeet width;
	/**
	 * Value for the deck's location at its northeast corner
	 */
	@Value
	Point2D neCorner;
	/**
	 * Value for the deck's location at its southwest corner
	 */
	@Value
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
		super(bridge, "BridgeDeck", 0L);
	}

	@Reception
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

	@Reception
	@Override
	public void onLoaded(Load load)
	{
		// On last load
		onLoad(load);

		// Zero all of the loads before accumulating weights to them
		logger.info(String.format("load:%s", load.location));
		suspendedWeightsNorth.forEach(weight -> weight.zero());
		suspendedWeightsSouth.forEach(weight -> weight.zero());
		groundWeightsWest.forEach(weight -> weight.zero());
		groundWeightsEast.forEach(weight -> weight.zero());
		pylonWeights.forEach(weight -> weight.zero());

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
		cableConnectionsNorthWest.get(0).transmit(new Load(0, suspendedWeightsNorth.get(0), suspensionPointsNorth.get(0), DirectionDegrees.west, false));
		cableConnectionsNorthWest.get(1).transmit(new Load(1, suspendedWeightsNorth.get(1), suspensionPointsNorth.get(1), DirectionDegrees.west, false));
		cableConnectionsNorthWest.get(2).transmit(new Load(2, suspendedWeightsNorth.get(2), suspensionPointsNorth.get(2), DirectionDegrees.west, false));
		cableConnectionsNorthWest.get(3).transmit(new Load(3, suspendedWeightsNorth.get(3), suspensionPointsNorth.get(3), DirectionDegrees.west, false));
		cableConnectionsNorthWest.get(4).transmit(new Load(4, suspendedWeightsNorth.get(4), suspensionPointsNorth.get(4), DirectionDegrees.west, false));

		// Transmit the deck loads to the cables along the southwest edge of the deck
		cableConnectionsSouthWest.get(0).transmit(new Load(0, suspendedWeightsSouth.get(0), suspensionPointsSouth.get(0), DirectionDegrees.east, false));
		cableConnectionsSouthWest.get(1).transmit(new Load(1, suspendedWeightsSouth.get(1), suspensionPointsSouth.get(1), DirectionDegrees.east, false));
		cableConnectionsSouthWest.get(2).transmit(new Load(2, suspendedWeightsSouth.get(2), suspensionPointsSouth.get(2), DirectionDegrees.east, false));
		cableConnectionsSouthWest.get(3).transmit(new Load(3, suspendedWeightsSouth.get(3), suspensionPointsSouth.get(3), DirectionDegrees.east, false));
		cableConnectionsSouthWest.get(4).transmit(new Load(4, suspendedWeightsSouth.get(4), suspensionPointsSouth.get(4), DirectionDegrees.east, false));

		// Transmit the deck loads to the cables along the northeast edge of the deck
		cableConnectionsNorthEast.get(0).transmit(new Load(0, suspendedWeightsNorth.get(5), suspensionPointsNorth.get(5), DirectionDegrees.west, true));
		cableConnectionsNorthEast.get(1).transmit(new Load(1, suspendedWeightsNorth.get(6), suspensionPointsNorth.get(6), DirectionDegrees.west, true));
		cableConnectionsNorthEast.get(2).transmit(new Load(2, suspendedWeightsNorth.get(7), suspensionPointsNorth.get(7), DirectionDegrees.west, true));
		cableConnectionsNorthEast.get(3).transmit(new Load(3, suspendedWeightsNorth.get(8), suspensionPointsNorth.get(8), DirectionDegrees.west, true));
		cableConnectionsNorthEast.get(4).transmit(new Load(4, suspendedWeightsNorth.get(9), suspensionPointsNorth.get(9), DirectionDegrees.west, true));

		// Transmit the deck loads to the cables along the southeast edge of the deck
		cableConnectionsSouthEast.get(0).transmit(new Load(0, suspendedWeightsSouth.get(5), suspensionPointsSouth.get(5), DirectionDegrees.east, true));
		cableConnectionsSouthEast.get(1).transmit(new Load(1, suspendedWeightsSouth.get(6), suspensionPointsSouth.get(6), DirectionDegrees.east, true));
		cableConnectionsSouthEast.get(2).transmit(new Load(2, suspendedWeightsSouth.get(7), suspensionPointsSouth.get(7), DirectionDegrees.east, true));
		cableConnectionsSouthEast.get(3).transmit(new Load(3, suspendedWeightsSouth.get(8), suspensionPointsSouth.get(8), DirectionDegrees.east, true));
		cableConnectionsSouthEast.get(4).transmit(new Load(4, suspendedWeightsSouth.get(9), suspensionPointsSouth.get(9), DirectionDegrees.east, true));

		// Transmit the deck loads to the western ground anchor
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
	protected void createValues()
	{
		super.createValues();
		width = new DistanceFeet(90);
		length = new DistanceFeet(600);
		suspensionPointsNorth = new ListOrdered<>(List.of(new Point2D(50, 90), new Point2D(100, 90), new Point2D(150, 90), new Point2D(200, 90), new Point2D(250, 90), new Point2D(350, 90), new Point2D(400, 90), new Point2D(450, 90), new Point2D(500, 90),
			new Point2D(550, 90)));
		suspensionPointsSouth = new ListOrdered<>(List.of(new Point2D(50, 0), new Point2D(100, 0), new Point2D(150, 0), new Point2D(200, 0), new Point2D(250, 0), new Point2D(350, 0), new Point2D(400, 0), new Point2D(450, 0), new Point2D(500, 0),
			new Point2D(550, 0)));

		groundPointsWest = new ListOrdered<>(List.of(new Point2D(0, 90), new Point2D(0, 0)));
		groundPointsEast = new ListOrdered<>(List.of(new Point2D(600, 90), new Point2D(600, 0)));

		pylonPoints = new ListOrdered<>(List.of(new Point2D(300, 90), new Point2D(300, 0)));

		neCorner = new Point2D(600, 90);
		swCorner = new Point2D(0, 0);
	}

	@Override
	protected void createFlows()
	{
		super.createFlows();
		suspendedWeightsNorth = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0),
			new WeightPounds(0)));
		suspendedWeightsSouth = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0),
			new WeightPounds(0)));

		groundWeightsWest = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0)));
		groundWeightsEast = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0)));

		pylonWeights = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0)));

	}

	@Override
	protected void createParts()
	{
		sections = new ListOrdered<>(List.of(new BridgeDeckSection(0, suspensionPointsNorth.get(0), groundPointsWest.get(1)), new BridgeDeckSection(1, suspensionPointsNorth.get(1), suspensionPointsSouth.get(0)),
			new BridgeDeckSection(2, suspensionPointsNorth.get(2), suspensionPointsSouth.get(1)), new BridgeDeckSection(3, suspensionPointsNorth.get(3), suspensionPointsSouth.get(2)),
			new BridgeDeckSection(4, suspensionPointsNorth.get(4), suspensionPointsSouth.get(3)), new BridgeDeckSection(5, pylonPoints.get(0), suspensionPointsSouth.get(4)),
			new BridgeDeckSection(6, suspensionPointsNorth.get(5), pylonPoints.get(1)), new BridgeDeckSection(7, suspensionPointsNorth.get(6), suspensionPointsSouth.get(5)),
			new BridgeDeckSection(8, suspensionPointsNorth.get(7), suspensionPointsSouth.get(6)), new BridgeDeckSection(9, suspensionPointsNorth.get(8), suspensionPointsSouth.get(7)),
			new BridgeDeckSection(10, suspensionPointsNorth.get(9), suspensionPointsSouth.get(8)), new BridgeDeckSection(11, groundPointsEast.get(0), suspensionPointsSouth.get(9))));
	}

	@Override
	protected void createFullPorts()
	{

		groundConnectionsWest.add(new DeckToGroundLoadTransmitter(this, 0L, "groundConnectionWestNorth"));
		groundConnectionsWest.add(new DeckToGroundLoadTransmitter(this, 1L, "groundConnectionWestSouth"));

		cableConnectionsNorthWest.add(new DeckToCableLoadTransmitter(this, 0L, "cableConnection0NorthWest"));
		cableConnectionsNorthWest.add(new DeckToCableLoadTransmitter(this, 1L, "cableConnection1NorthWest"));
		cableConnectionsNorthWest.add(new DeckToCableLoadTransmitter(this, 2L, "cableConnection2NorthWest"));
		cableConnectionsNorthWest.add(new DeckToCableLoadTransmitter(this, 3L, "cableConnection3NorthWest"));
		cableConnectionsNorthWest.add(new DeckToCableLoadTransmitter(this, 4L, "cableConnection4NorthWest"));

		pylonConnections.add(new DeckToPylonLoadTransmitter(this, 0L, "pylonConnectionNorth"));

		cableConnectionsNorthEast.add(new DeckToCableLoadTransmitter(this, 0L, "cableConnection0NorthEast"));
		cableConnectionsNorthEast.add(new DeckToCableLoadTransmitter(this, 1L, "cableConnection1NorthEast"));
		cableConnectionsNorthEast.add(new DeckToCableLoadTransmitter(this, 2L, "cableConnection2NorthEast"));
		cableConnectionsNorthEast.add(new DeckToCableLoadTransmitter(this, 3L, "cableConnection3NorthEast"));
		cableConnectionsNorthEast.add(new DeckToCableLoadTransmitter(this, 4L, "cableConnection4NorthEast"));

		cableConnectionsSouthWest.add(new DeckToCableLoadTransmitter(this, 0L, "cableConnection0SouthWest"));
		cableConnectionsSouthWest.add(new DeckToCableLoadTransmitter(this, 1L, "cableConnection1SouthWest"));
		cableConnectionsSouthWest.add(new DeckToCableLoadTransmitter(this, 2L, "cableConnection2SouthWest"));
		cableConnectionsSouthWest.add(new DeckToCableLoadTransmitter(this, 3L, "cableConnection3SouthWest"));
		cableConnectionsSouthWest.add(new DeckToCableLoadTransmitter(this, 4L, "cableConnection4SouthWest"));

		pylonConnections.add(new DeckToPylonLoadTransmitter(this, 1L, "pylonConnectionSouth"));

		cableConnectionsSouthEast.add(new DeckToCableLoadTransmitter(this, 0L, "cableConnection0SouthEast"));
		cableConnectionsSouthEast.add(new DeckToCableLoadTransmitter(this, 1L, "cableConnection1SouthEast"));
		cableConnectionsSouthEast.add(new DeckToCableLoadTransmitter(this, 2L, "cableConnection2SouthEast"));
		cableConnectionsSouthEast.add(new DeckToCableLoadTransmitter(this, 3L, "cableConnection3SouthEast"));
		cableConnectionsSouthEast.add(new DeckToCableLoadTransmitter(this, 4L, "cableConnection4SouthEast"));

		groundConnectionsEast.add(new DeckToGroundLoadTransmitter(this, 0L, "groundConnectionEastNorth"));
		groundConnectionsEast.add(new DeckToGroundLoadTransmitter(this, 0L, "groundConnectionEastSouth"));

		for (long index = 0; index < Vehicles.directionBoundCount; index++)
		{
			vehiclesEastBound.add(new VehicleToDeckLoadReceiver(contextBlock.get(), index, String.format("VehicleEB%d", index)));
			vehiclesWestBound.add(new VehicleToDeckLoadReceiver(contextBlock.get(), index, String.format("VehicleWB%d", index)));
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new LoadBearingComponentStateMachine(this, false, "BridgeDeckStateMachine"));
	}

	@Override
	protected void createHyperlinks()
	{
		deckSpecification = new SysMLHyperlink("Cable-Stayed Bridge Deck Specification", "http://BridgeDeckBuilders.com/Specs/CableStayedBridgeDeckSpecification.pdf");
	}

	@Override
	protected void preCreate()
	{
		super.preCreate();
		cableConnectionsNorthEast = new ArrayList<>();
		cableConnectionsSouthEast = new ArrayList<>();
		cableConnectionsNorthWest = new ArrayList<>();
		cableConnectionsSouthWest = new ArrayList<>();
		pylonConnections = new ArrayList<>();
		groundConnectionsWest = new ArrayList<>();
		groundConnectionsEast = new ArrayList<>();
		vehiclesEastBound = new ArrayList<>();
		vehiclesWestBound = new ArrayList<>();
	}
}
