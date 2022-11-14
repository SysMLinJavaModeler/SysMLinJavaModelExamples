package cablestayedbridge;

import java.util.List;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.parametrics.BindingConnector;
import sysmlinjava.annotations.parametrics.BindingConnectorFunction;
import sysmlinjava.annotations.parametrics.ConstraintBlock;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLBindingConnectorFunction;
import sysmlinjava.ports.SysMLConstraintParameterPort;

/**
 * SysMLinJava block-base representation of the domain in which the cable-stayed
 * bridge exists and operates. The domain is comprised of the cable-stayed
 * bridge, the vehicles that travel on and across the bridge, and the three
 * ground locations that anchor/support the bridge. It includes a constraint
 * block for the display of the live loads on the bridge along the bridge deck.
 * <p>
 * The {@code CableStayedBridgeDomain} is a SysML block extension that has the
 * domain components as SysML parts. It also contains all the connectors that
 * make the connectors between the domain parts as appropriate. The
 * {@code CableStayedBridgeDomain} provides the {@code main()} operation that
 * provides the context for the execution/simulation of the model.
 * 
 * @author ModelerOne
 *
 */
public class CableStayedBridgeDomain extends SysMLBlock
{
	/**
	 * Part representing the cable-stayed bridge
	 */
	@Part
	CableStayedBridge bridge;
	/**
	 * Part representing the ground anchor for the bridge at its west end
	 */
	@Part
	GroundAnchor groundAnchorWest;
	/**
	 * Part representing the ground anchor for the bridge at its east end
	 */
	@Part
	GroundAnchor groundAnchorEast;
	/**
	 * Part representing the ground base for the bridge under its single pylon
	 */
	@Part
	GroundBase groundBase;
	/**
	 * Part representing the vehicles that travel on and across the cable-stayed
	 * bridge
	 */
	@Part
	Vehicles vehicles;

	/**
	 * Constraint block used to display the bridge deck loads.
	 */
	@ConstraintBlock
	public BridgeDeckLoadsConstraintBlock bridgeDeckLoadsConstraintBlock;

	/**
	 * Function that connects the pylon to the ground base
	 */
	@AssociationConnectorFunction
	SysMLAssociationBlockConnectorFunction pylonToGroundConnectorFunction;
	/**
	 * Function that connects the bridge deck to the ground anchor on the west side
	 * of the bridge
	 */
	@AssociationConnectorFunction
	SysMLAssociationBlockConnectorFunction deckToWestGroundAnchorConnectorFunction;
	/**
	 * Function that connects the bridge deck to the ground anchor on the east side
	 * of the bridge
	 */
	@AssociationConnectorFunction
	SysMLAssociationBlockConnectorFunction deckToEastGroundAnchorConnectorFunction;
	/**
	 * Function that connects the vehicles to the bridge deck
	 */
	@AssociationConnectorFunction
	SysMLAssociationBlockConnectorFunction vehiclesToDeckConnectorFunction;

	/**
	 * Connector that performs the function that connects the pylon to the ground
	 * base
	 */
	@AssociationConnector
	SysMLAssociationBlockConnector pylonToGroundConnector;
	/**
	 * Connector that performs the function that connects the bridge deck to the
	 * ground anchor on the west side of the bridge
	 */
	@AssociationConnector
	SysMLAssociationBlockConnector deckToWestGroundAnchorConnector;
	/**
	 * Connector that performs the function that connects the bridge deck to the
	 * ground anchor on the east side of the bridge
	 */
	@AssociationConnector
	SysMLAssociationBlockConnector deckToEastGroundAnchorConnector;
	/**
	 * Connector that performs the function that connects the vehicles to the bridge
	 * deck
	 */
	@AssociationConnector
	SysMLAssociationBlockConnector vehiclesToDeckConnector;

	/**
	 * Binding connector function that makes the "connections" between the bridge
	 * cable load values and the associated parameter ports of the constraint block
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction cableLoadsConnectorFunction;
	/**
	 * Binding connector function that makes the "connections" between the bridge
	 * cable available capacity values and the associated parameter ports of the
	 * constraint block
	 */
	@BindingConnectorFunction
	public SysMLBindingConnectorFunction cableAvailablesConnectorFunction;

	/**
	 * Binding connector that invokes the function that makes the "connections"
	 * between the bridge cable load values and the associated parameter ports of
	 * the constraint block
	 */
	@BindingConnector
	public SysMLBindingConnector cableLoadsConnector;
	/**
	 * Binding connector that invokes the function that makes the "connections"
	 * between the bridge cable available capacity values and the associated
	 * parameter ports of the constraint block
	 */
	@BindingConnector
	public SysMLBindingConnector cableAvailablesConnector;

	/**
	 * Constructor
	 */
	public CableStayedBridgeDomain()
	{
		super();
	}

	@Override
	public void start()
	{
		groundBase.start();
		groundAnchorEast.start();
		groundAnchorWest.start();
		bridge.start();
		vehicles.start();
		bridgeDeckLoadsConstraintBlock.start();
	}

	@Override
	public void stop()
	{
		bridgeDeckLoadsConstraintBlock.stop();
		vehicles.stop();
		bridge.stop();
		groundAnchorWest.stop();
		groundAnchorEast.stop();
		groundBase.stop();
	}

	@Override
	protected void createParts()
	{
		bridge = new CableStayedBridge();
		groundAnchorWest = new GroundAnchor("GroundAnchorWest", 0L, 0);
		groundAnchorEast = new GroundAnchor("GroundAnchorEast", 0L, 0);
		groundBase = new GroundBase();
		vehicles = new Vehicles();
	}

	@Override
	protected void createConstraintBlocks()
	{
		bridgeDeckLoadsConstraintBlock = new BridgeDeckLoadsConstraintBlock();
	}

	@Override
	protected void createConnectorFunctions()
	{
		deckToWestGroundAnchorConnectorFunction = () ->
		{
			bridge.deck.groundConnectionsWest.get(0).addConnectedPortPeer(groundAnchorWest.northDeckLoadReceiver);
			bridge.deck.groundConnectionsWest.get(1).addConnectedPortPeer(groundAnchorWest.southDeckLoadReceiver);
		};
		deckToEastGroundAnchorConnectorFunction = () ->
		{
			bridge.deck.groundConnectionsEast.get(0).addConnectedPortPeer(groundAnchorEast.northDeckLoadReceiver);
			bridge.deck.groundConnectionsEast.get(1).addConnectedPortPeer(groundAnchorEast.southDeckLoadReceiver);
		};
		pylonToGroundConnectorFunction = () ->
		{
			bridge.pylon.groundLoadTransmitter.addConnectedPortPeer(groundBase.pylonLoadReceiver);
		};
		vehiclesToDeckConnectorFunction = () ->
		{
			for (int index = 0; index < Vehicles.directionBoundCount; index++)
			{
				vehicles.vehiclesEastbound.get(index).deckLoadTransmitter.addConnectedPortPeer(bridge.deck.vehiclesEastBound.get(index));
				vehicles.vehiclesWestbound.get(index).deckLoadTransmitter.addConnectedPortPeer(bridge.deck.vehiclesWestBound.get(index));
			}
		};
	}

	@Override
	protected void createConnectors()
	{
		deckToWestGroundAnchorConnector = new SysMLAssociationBlockConnector(List.of(bridge.deck), List.of(groundAnchorWest), deckToWestGroundAnchorConnectorFunction);
		deckToEastGroundAnchorConnector = new SysMLAssociationBlockConnector(List.of(bridge.deck), List.of(groundAnchorEast), deckToEastGroundAnchorConnectorFunction);
		pylonToGroundConnector = new SysMLAssociationBlockConnector(List.of(bridge.pylon), List.of(groundBase), pylonToGroundConnectorFunction);
		vehiclesToDeckConnector = new SysMLAssociationBlockConnector(List.of(vehicles), List.of(bridge.deck), vehiclesToDeckConnectorFunction);
	}

	@Override
	protected void createConstraintParameterConnectorFunctions()
	{
		cableLoadsConnectorFunction = () ->
		{
			SysMLConstraintParameterPort cableLoadsPort = bridgeDeckLoadsConstraintBlock.paramPorts.get(BridgeDeckLoadsConstraintBlock.cableLoadsKey);
			cableLoadsPort.setParameterContextBlock(bridge);
			bridge.cableLoads.bindTo(cableLoadsPort);
		};
		cableAvailablesConnectorFunction = () ->
		{
			SysMLConstraintParameterPort cableAvailablesPort = bridgeDeckLoadsConstraintBlock.paramPorts.get(BridgeDeckLoadsConstraintBlock.cableAvailablesKey);
			cableAvailablesPort.setParameterContextBlock(bridge);
			bridge.cableAvailables.bindTo(cableAvailablesPort);
		};
	}

	@Override
	protected void createConstraintParameterConnectors()
	{
		cableLoadsConnector = new SysMLBindingConnector(bridge, bridgeDeckLoadsConstraintBlock, cableLoadsConnectorFunction);
		cableAvailablesConnector = new SysMLBindingConnector(bridge, bridgeDeckLoadsConstraintBlock, cableAvailablesConnectorFunction);
	}

	/**
	 * Main operation that executes the model of the
	 * {@code CableStayedBridgeDomain}. Execution consists of the starting of the
	 * domain parts and then simply waiting for a specified period of time before
	 * stopping the parts. The domain parts operate asynchrously with each other as
	 * individual blocks executing as event-driven state machines.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args)
	{
		CableStayedBridgeDomain domain = new CableStayedBridgeDomain();
		domain.start();
		try
		{
			Thread.sleep(60_000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		domain.stop();
		Runtime.getRuntime().exit(0);
	}
}
