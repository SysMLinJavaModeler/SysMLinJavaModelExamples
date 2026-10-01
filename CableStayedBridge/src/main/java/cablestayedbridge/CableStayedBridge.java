package cablestayedbridge;

import java.util.List;
import java.util.Optional;

import cablestayedbridge.ports.LoadSignal;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.ListOrdered;
import sysmlinjava.attributetypes.WeightPounds;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;

/**
 * SysMLinJava model for a cable-stayed bridge. The bridge is assumed to consist
 * of a bridge deck supported by a single pylon at the deck center, base piers
 * in the ground at each end of the deck, and by 10 cables that connect to the
 * deck at equal intervals and hang from a cable saddle at the top of the pylon.
 * <p>
 * The {@code CableStayedBridge} is a {@code LoadBearingComponent} that receives
 * live loads from vehicles crossing the bridge. The {@code CableStayedBridge}
 * operates asynchrously to the vehicles with its components receiving and
 * transmitting loads synchronously to each other and to the ground. The model
 * executes by moving the vehicles as randomly distributed loads on the bridge
 * and transmitting the loads through the bridge components and to the ground.
 * If any loads on a component exceed the component's limits, the component
 * fails and the execution is terminated.
 * 
 * @author ModelerOne
 *
 */
/**
 * Cable that crosses the pylon's cable saddle to suspend:
 * deck section 0, northwest corner and the deck section 9, southeast corner.
 * @author ModelerOne
 *
 */
public class CableStayedBridge extends SysMLPart implements LoadBearingComponent
{
	/**
	 * Part that is the pylon of the bridge
	 */
	@Part
	Pylon pylon;
	/**
	 * Part that is the deck of the bridge
	 */
	@Part
	BridgeDeck deck;
	/**
	 * Part for the cable that suspends the deck at its southwest 0 and northeast 9
	 * points
	 */
	@Part
	CableSW0toNE9 cableSW0toNE9;
	/**
	 * Part for the cable that suspends the deck at its southwest 1 and northeast 8
	 * points
	 */
	@Part
	CableSW1toNE8 cableSW1toNE8;
	/**
	 * Part for the cable that suspends the deck at its southwest 2 and northeast 7
	 * points
	 */
	@Part
	CableSW2toNE7 cableSW2toNE7;
	/**
	 * Part for the cable that suspends the deck at its southwest 3 and northeast 6
	 * points
	 */
	@Part
	CableSW3toNE6 cableSW3toNE6;
	/**
	 * Part for the cable that suspends the deck at its southwest 4 and northeast 5
	 * points
	 */
	@Part
	CableSW4toNE5 cableSW4toNE5;
	/**
	 * Part for the cable that suspends the deck at its northwest 0 and southeast 9
	 * points
	 */
	@Part
	CableNW0toSE9 cableNW0toSE9;
	/**
	 * Part for the cable that suspends the deck at its northwest 1 and southeast 8
	 * points
	 */
	@Part
	CableNW1toSE8 cableNW1toSE8;
	/**
	 * Part for the cable that suspends the deck at its northwest 2 and southeast 7
	 * points
	 */
	@Part
	CableNW2toSE7 cableNW2toSE7;
	/**
	 * Part for the cable that suspends the deck at its northwest 3 and southeast 6
	 * points
	 */
	@Part
	CableNW3toSE6 cableNW3toSE6;
	/**
	 * Part for the cable that suspends the deck at its northwest 4 and southeast 4
	 * points
	 */
	@Part
	CableNW4toSE5 cableNW4toSE5;

	/**
	 * Flow (derived) for the collection of loads on the cables. Derived for use in
	 * constraint block.
	 */
	@Attribute
	ListOrdered<WeightPounds> cableLoads;

	/**
	 * Value (derived) for the collection of strengths of the cables, i.e. capacity
	 * for load. Derived for use in constraint block.
	 */
	@Attribute
	ListOrdered<WeightPounds> cableAvailables;

	/**
	 * Connector that performs the function that connects the deck to the cables
	 */
	@FlowConnector
	SysMLFlowConnector deckToCablesConnector;
	/**
	 * Connector that performs the function that connects the deck to the pylon
	 */
	@FlowConnector
	SysMLFlowConnector deckToPylonConnector;
	/**
	 * Connector that performs the function that connects the cables to the pylon
	 */
	@FlowConnector
	SysMLFlowConnector cablesToPylonConnector;

	/**
	 * List of the cables
	 */
	List<Cable> cablesList;

	/**
	 * Hyperlink to bridge specification document
	 */
	@Hyperlink
	SysMLHyperlink bridgeSpecification;

	/**
	 * Constructor
	 */
	public CableStayedBridge()
	{
		super("CableStayedBridge", 0L);
		cablesList = List.of(cableNW0toSE9, cableNW1toSE8, cableNW2toSE7, cableNW3toSE6, cableNW4toSE5, cableSW0toNE9, cableSW1toNE8, cableSW2toNE7, cableSW3toNE6, cableSW4toNE5);
	}

	@Override
	public void start()
	{
		super.start();
		pylon.start();
		deck.start();
		cablesList.forEach(cable -> cable.start());
	}

	@Action
	@Override
	public void onLoad(Load load)
	{
		deck.acceptEvent(new SysMLSignalEvent(new LoadSignal(load), "LoadEvent", 0L));
	}

	/**
	 * Inidication of whether or not the first load has been received
	 */
	boolean firstLoad = false;

	@Action
	@Override
	public void onLoaded(Load load)
	{
		deck.acceptEvent(new SysMLSignalEvent(new LoadSignal(load), "LoadEvent", 0L));

		cableLoads.setValue(List.of(
			new WeightPounds(cableNW0toSE9.totalLoad),
			new WeightPounds(cableNW1toSE8.totalLoad),
			new WeightPounds(cableNW2toSE7.totalLoad),
			new WeightPounds(cableNW3toSE6.totalLoad),
			new WeightPounds(cableNW4toSE5.totalLoad),
			new WeightPounds(cableSW4toNE5.totalLoad),
			new WeightPounds(cableSW3toNE6.totalLoad),
			new WeightPounds(cableSW2toNE7.totalLoad),
			new WeightPounds(cableSW1toNE8.totalLoad),
			new WeightPounds(cableSW0toNE9.totalLoad)));

		cableAvailables.setValue(List.of(
			new WeightPounds(cableNW0toSE9.breakingStrength.value - cableNW0toSE9.totalLoad.value),
			new WeightPounds(cableNW1toSE8.breakingStrength.value - cableNW1toSE8.totalLoad.value),
			new WeightPounds(cableNW2toSE7.breakingStrength.value - cableNW2toSE7.totalLoad.value),
			new WeightPounds(cableNW3toSE6.breakingStrength.value - cableNW3toSE6.totalLoad.value),
			new WeightPounds(cableNW4toSE5.breakingStrength.value - cableNW4toSE5.totalLoad.value),
			new WeightPounds(cableSW4toNE5.breakingStrength.value - cableSW4toNE5.totalLoad.value),
			new WeightPounds(cableSW3toNE6.breakingStrength.value - cableSW3toNE6.totalLoad.value),
			new WeightPounds(cableSW2toNE7.breakingStrength.value - cableSW2toNE7.totalLoad.value),
			new WeightPounds(cableSW1toNE8.breakingStrength.value - cableSW1toNE8.totalLoad.value),
			new WeightPounds(cableSW0toNE9.breakingStrength.value - cableSW0toNE9.totalLoad.value)));
	}

	@Override
	public void onFailed(FailureEvent failure)
	{
		deck.onFailed(failure);
	}

	@Override
	public void stop()
	{
		cablesList.forEach(cable -> cable.stop());
		deck.stop();
		pylon.stop();
		super.stop();
	}

	@Override
	protected void createAttributes()
	{
		cableAvailables = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0)));
		cableLoads = new ListOrdered<>(List.of(new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0)));
	}

	@Override
	protected void createParts()
	{
		pylon = new Pylon();
		deck = new BridgeDeck(this);
		cableNW0toSE9 = new CableNW0toSE9("CableNW0toSE9", 0L, this);
		cableNW1toSE8 = new CableNW1toSE8("CableNW1toSE8", 1L, this);
		cableNW2toSE7 = new CableNW2toSE7("CableNW2toSE7", 2L, this);
		cableNW3toSE6 = new CableNW3toSE6("CableNW3toSE6", 3L, this);
		cableNW4toSE5 = new CableNW4toSE5("CableNW4toSE5", 4L, this);
		cableSW4toNE5 = new CableSW4toNE5("CableSW4toNE5", 5L, this);
		cableSW3toNE6 = new CableSW3toNE6("CableSW3toNE6", 6L, this);
		cableSW2toNE7 = new CableSW2toNE7("CableSW2toNE7", 7L, this);
		cableSW1toNE8 = new CableSW1toNE8("CableSW1toNE8", 8L, this);
		cableSW0toNE9 = new CableSW0toNE9("CableSW0toNE9", 9L, this);
	}

	@Override
	protected void createFlowConnectors()
	{
		deckToCablesConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false,
		List.of(
		deck.cableConnectionsNorthWest.get(0),
		deck.cableConnectionsNorthWest.get(1),
		deck.cableConnectionsNorthWest.get(2),
		deck.cableConnectionsNorthWest.get(3),
		deck.cableConnectionsNorthWest.get(4),
		deck.cableConnectionsNorthEast.get(0),
		deck.cableConnectionsNorthEast.get(1),
		deck.cableConnectionsNorthEast.get(2),
		deck.cableConnectionsNorthEast.get(3),
		deck.cableConnectionsNorthEast.get(4),
		deck.cableConnectionsSouthWest.get(0),
		deck.cableConnectionsSouthWest.get(1),
		deck.cableConnectionsSouthWest.get(2),
		deck.cableConnectionsSouthWest.get(3),
		deck.cableConnectionsSouthWest.get(4),
		deck.cableConnectionsSouthEast.get(0),
		deck.cableConnectionsSouthEast.get(1),
		deck.cableConnectionsSouthEast.get(2),
		deck.cableConnectionsSouthEast.get(3),
		deck.cableConnectionsSouthEast.get(4)),
		List.of(
		cableNW0toSE9.deckLoadWestReceiver,
		cableNW1toSE8.deckLoadWestReceiver,
		cableNW2toSE7.deckLoadWestReceiver,
		cableNW3toSE6.deckLoadWestReceiver,
		cableNW4toSE5.deckLoadWestReceiver,
		cableSW0toNE9.deckLoadEastReceiver,
		cableSW1toNE8.deckLoadEastReceiver,
		cableSW2toNE7.deckLoadEastReceiver,
		cableSW3toNE6.deckLoadEastReceiver,
		cableSW4toNE5.deckLoadEastReceiver,
		cableSW0toNE9.deckLoadWestReceiver,
		cableSW1toNE8.deckLoadWestReceiver,
		cableSW2toNE7.deckLoadWestReceiver,
		cableSW3toNE6.deckLoadWestReceiver,
		cableSW4toNE5.deckLoadWestReceiver,
		cableNW0toSE9.deckLoadEastReceiver,
		cableNW1toSE8.deckLoadEastReceiver,
		cableNW2toSE7.deckLoadEastReceiver,
		cableNW3toSE6.deckLoadEastReceiver,
		cableNW4toSE5.deckLoadEastReceiver), "DeckToCables", 0L);

		deckToPylonConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false,
		List.of(deck.pylonConnections.get(0), deck.pylonConnections.get(1)),
		List.of(pylon.deckLoadReceivers.get(0), pylon.deckLoadReceivers.get(1)), "DeckToPylon", 0L);

		cablesToPylonConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false,
		List.of(
		cableNW0toSE9.pylonLoadTransmitter,
		cableNW1toSE8.pylonLoadTransmitter,
		cableNW2toSE7.pylonLoadTransmitter,
		cableNW3toSE6.pylonLoadTransmitter,
		cableNW4toSE5.pylonLoadTransmitter,
		cableSW0toNE9.pylonLoadTransmitter,
		cableSW1toNE8.pylonLoadTransmitter,
		cableSW2toNE7.pylonLoadTransmitter,
		cableSW3toNE6.pylonLoadTransmitter,
		cableSW4toNE5.pylonLoadTransmitter),
		List.of(
		pylon.cableLoadReceivers.get(0),
		pylon.cableLoadReceivers.get(1),
		pylon.cableLoadReceivers.get(2),
		pylon.cableLoadReceivers.get(3),
		pylon.cableLoadReceivers.get(4),

		pylon.cableLoadReceivers.get(5),
		pylon.cableLoadReceivers.get(6),
		pylon.cableLoadReceivers.get(7),
		pylon.cableLoadReceivers.get(8),
		pylon.cableLoadReceivers.get(9)), "CablesToPylon", 0L);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new LoadBearingComponentStateMachine(this, true, "CableStayedBridgeStateMachine"));
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		bridgeSpecification = new SysMLHyperlink("Cable-Stayed Bridge Specification", "http://SuspensionBridgeBuilders.com/Specs/CableStayedBridgeSpecification.pdf");
	}
}
