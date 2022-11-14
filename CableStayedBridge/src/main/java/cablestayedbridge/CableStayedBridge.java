package cablestayedbridge;

import java.util.List;
import java.util.Optional;
import cablestayedbridge.ports.LoadSignal;
import cablestayedbridge.ports.LoadSignalEvent;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.valuetypes.ListOrdered;
import sysmlinjava.valuetypes.WeightPounds;

/**
 * SysMLinJava model for a cable-stayed bridge. The bridge is assumed to consist
 * of a bridge deck supported by a single pylon at the deck center and by 10
 * cables that connect to the deck at equal intervals and hang from a cable
 * saddle at the top of the pylon.
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
public class CableStayedBridge extends SysMLBlock implements LoadBearingComponent
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
	@Flow
	ListOrdered<WeightPounds> cableLoads;

	/**
	 * Value (derived) for the collection of strengths of the cables, i.e. capacity
	 * for load. Derived for use in constraint block.
	 */
	@Value
	ListOrdered<WeightPounds> cableAvailables;

	/**
	 * Function that connects the deck to the cables
	 */
	@AssociationConnectorFunction
	SysMLAssociationBlockConnectorFunction deckToCablesConnectorFunction;
	/**
	 * Function that connects the deck to the pylon
	 */
	@AssociationConnectorFunction
	SysMLAssociationBlockConnectorFunction deckToPylonConnectorFunction;
	/**
	 * Function that connects the cables to the pylon
	 */
	@AssociationConnectorFunction
	SysMLAssociationBlockConnectorFunction cablesToPylonConnectorFunction;

	/**
	 * Connector that performs the function that connects the deck to the cables
	 */
	@AssociationConnector
	SysMLAssociationBlockConnector deckToCablesConnector;
	/**
	 * Connector that performs the function that connects the deck to the pylon
	 */
	@AssociationConnector
	SysMLAssociationBlockConnector deckToPylonConnector;
	/**
	 * Connector that performs the function that connects the cables to the pylon
	 */
	@AssociationConnector
	SysMLAssociationBlockConnector cablesToPylonConnector;

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

	@Reception
	@Override
	public void onLoad(Load load)
	{
		deck.acceptEvent(new LoadSignalEvent(new LoadSignal(load)));
	}

	boolean firstLoad = false;

	@Reception
	@Override
	public void onLoaded(Load load)
	{
		deck.acceptEvent(new LoadSignalEvent(new LoadSignal(load)));

		cableLoads.get(cableNW0toSE9.id.intValue()).value = cableNW0toSE9.totalLoad.value;
		cableLoads.get(cableNW1toSE8.id.intValue()).value = cableNW1toSE8.totalLoad.value;
		cableLoads.get(cableNW2toSE7.id.intValue()).value = cableNW2toSE7.totalLoad.value;
		cableLoads.get(cableNW3toSE6.id.intValue()).value = cableNW3toSE6.totalLoad.value;
		cableLoads.get(cableNW4toSE5.id.intValue()).value = cableNW4toSE5.totalLoad.value;
		cableLoads.get(cableSW0toNE9.id.intValue()).value = cableSW0toNE9.totalLoad.value;
		cableLoads.get(cableSW1toNE8.id.intValue()).value = cableSW1toNE8.totalLoad.value;
		cableLoads.get(cableSW2toNE7.id.intValue()).value = cableSW2toNE7.totalLoad.value;
		cableLoads.get(cableSW3toNE6.id.intValue()).value = cableSW3toNE6.totalLoad.value;
		cableLoads.get(cableSW4toNE5.id.intValue()).value = cableSW4toNE5.totalLoad.value;
		cableLoads.notifyValueChangeObservers();

		cableAvailables.get(cableNW0toSE9.id.intValue()).value = cableNW0toSE9.breakingStrength.value - cableNW0toSE9.totalLoad.value;
		cableAvailables.get(cableNW1toSE8.id.intValue()).value = cableNW1toSE8.breakingStrength.value - cableNW1toSE8.totalLoad.value;
		cableAvailables.get(cableNW2toSE7.id.intValue()).value = cableNW2toSE7.breakingStrength.value - cableNW2toSE7.totalLoad.value;
		cableAvailables.get(cableNW3toSE6.id.intValue()).value = cableNW3toSE6.breakingStrength.value - cableNW3toSE6.totalLoad.value;
		cableAvailables.get(cableNW4toSE5.id.intValue()).value = cableNW4toSE5.breakingStrength.value - cableNW4toSE5.totalLoad.value;
		cableAvailables.get(cableSW0toNE9.id.intValue()).value = cableSW0toNE9.breakingStrength.value - cableSW0toNE9.totalLoad.value;
		cableAvailables.get(cableSW1toNE8.id.intValue()).value = cableSW1toNE8.breakingStrength.value - cableSW1toNE8.totalLoad.value;
		cableAvailables.get(cableSW2toNE7.id.intValue()).value = cableSW2toNE7.breakingStrength.value - cableSW2toNE7.totalLoad.value;
		cableAvailables.get(cableSW3toNE6.id.intValue()).value = cableSW3toNE6.breakingStrength.value - cableSW3toNE6.totalLoad.value;
		cableAvailables.get(cableSW4toNE5.id.intValue()).value = cableSW4toNE5.breakingStrength.value - cableSW4toNE5.totalLoad.value;
		cableAvailables.notifyValueChangeObservers();
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
	protected void createValues()
	{
		cableAvailables = new ListOrdered<>(List.of(
			new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0),
			new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0)));
	}

	@Override
	protected void createFlows()
	{
		cableLoads = new ListOrdered<>(List.of(
			new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0),
			new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0), new WeightPounds(0)));
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
	protected void createConnectorFunctions()
	{
		deckToCablesConnectorFunction = () ->
		{
			deck.cableConnectionsNorthWest.get(0).addConnectedPortPeer(cableNW0toSE9.deckLoadWestReceiver);
			deck.cableConnectionsNorthWest.get(1).addConnectedPortPeer(cableNW1toSE8.deckLoadWestReceiver);
			deck.cableConnectionsNorthWest.get(2).addConnectedPortPeer(cableNW2toSE7.deckLoadWestReceiver);
			deck.cableConnectionsNorthWest.get(3).addConnectedPortPeer(cableNW3toSE6.deckLoadWestReceiver);
			deck.cableConnectionsNorthWest.get(4).addConnectedPortPeer(cableNW4toSE5.deckLoadWestReceiver);
			deck.cableConnectionsNorthEast.get(0).addConnectedPortPeer(cableSW0toNE9.deckLoadEastReceiver);
			deck.cableConnectionsNorthEast.get(1).addConnectedPortPeer(cableSW1toNE8.deckLoadEastReceiver);
			deck.cableConnectionsNorthEast.get(2).addConnectedPortPeer(cableSW2toNE7.deckLoadEastReceiver);
			deck.cableConnectionsNorthEast.get(3).addConnectedPortPeer(cableSW3toNE6.deckLoadEastReceiver);
			deck.cableConnectionsNorthEast.get(4).addConnectedPortPeer(cableSW4toNE5.deckLoadEastReceiver);

			deck.cableConnectionsSouthWest.get(0).addConnectedPortPeer(cableSW0toNE9.deckLoadWestReceiver);
			deck.cableConnectionsSouthWest.get(1).addConnectedPortPeer(cableSW1toNE8.deckLoadWestReceiver);
			deck.cableConnectionsSouthWest.get(2).addConnectedPortPeer(cableSW2toNE7.deckLoadWestReceiver);
			deck.cableConnectionsSouthWest.get(3).addConnectedPortPeer(cableSW3toNE6.deckLoadWestReceiver);
			deck.cableConnectionsSouthWest.get(4).addConnectedPortPeer(cableSW4toNE5.deckLoadWestReceiver);
			deck.cableConnectionsSouthEast.get(0).addConnectedPortPeer(cableNW0toSE9.deckLoadEastReceiver);
			deck.cableConnectionsSouthEast.get(1).addConnectedPortPeer(cableNW1toSE8.deckLoadEastReceiver);
			deck.cableConnectionsSouthEast.get(2).addConnectedPortPeer(cableNW2toSE7.deckLoadEastReceiver);
			deck.cableConnectionsSouthEast.get(3).addConnectedPortPeer(cableNW3toSE6.deckLoadEastReceiver);
			deck.cableConnectionsSouthEast.get(4).addConnectedPortPeer(cableNW4toSE5.deckLoadEastReceiver);
		};
		deckToPylonConnectorFunction = () ->
		{
			deck.pylonConnections.get(0).addConnectedPortPeer(pylon.deckLoadReceivers.get(0));
			deck.pylonConnections.get(1).addConnectedPortPeer(pylon.deckLoadReceivers.get(1));
		};
		cablesToPylonConnectorFunction = () ->
		{
			cableNW0toSE9.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(0));
			cableNW1toSE8.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(1));
			cableNW2toSE7.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(2));
			cableNW3toSE6.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(3));
			cableNW4toSE5.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(4));

			cableSW0toNE9.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(5));
			cableSW1toNE8.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(6));
			cableSW2toNE7.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(7));
			cableSW3toNE6.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(8));
			cableSW4toNE5.pylonLoadTransmitter.addConnectedPortPeer(pylon.cableLoadReceivers.get(9));
		};
	}

	@Override
	protected void createConnectors()
	{
		deckToCablesConnector = new SysMLAssociationBlockConnector(List.of(deck), cablesList, deckToCablesConnectorFunction);
		deckToPylonConnector = new SysMLAssociationBlockConnector(List.of(deck), List.of(pylon), deckToPylonConnectorFunction);
		cablesToPylonConnector = new SysMLAssociationBlockConnector(cablesList, List.of(pylon), cablesToPylonConnectorFunction);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new LoadBearingComponentStateMachine(this, true, "CableStayedBridgeStateMachine"));
	}

	@Override
	protected void createHyperlinks()
	{
		bridgeSpecification = new SysMLHyperlink("Cable-Stayed Bridge Specification", "http://SuspensionBridgeBuilders.com/Specs/CableStayedBridgeSpecification.pdf");
	}
}
