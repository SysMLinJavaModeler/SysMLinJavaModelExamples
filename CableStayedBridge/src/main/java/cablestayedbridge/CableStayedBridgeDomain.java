package cablestayedbridge;

import java.util.ArrayList;
import java.util.List;

import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.javaannotations.connectors.BindingConnector;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;

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
 */
/**
 * Cable that crosses the pylon's cable saddle to suspend: deck section 0,
 * northwest corner and the deck section 9, southeast corner.
 * 
 * @author ModelerOne
 */
public class CableStayedBridgeDomain extends SysMLPart
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
	 * Parametric analysis that calculates and displays the bridge deck loads.
	 */
	@ParametricAnalysis
	public BridgeDeckLoadsAnalysisCase bridgeDeckLoadsAnalysisCase;

	/**
	 * Connector that connects the pylon to the ground base
	 */
	@FlowConnector
	SysMLFlowConnector pylonToGroundConnector;
	/**
	 * Connector that connects the bridge deck to the ground anchor on the west side
	 * of the bridge
	 */
	@FlowConnector
	SysMLFlowConnector deckToWestGroundAnchorConnector;
	/**
	 * Connector that connects the bridge deck to the ground anchor on the east side
	 * of the bridge
	 */
	@FlowConnector
	SysMLFlowConnector deckToEastGroundAnchorConnector;
	/**
	 * Connector that connects the vehicles to the bridge deck
	 */
	@FlowConnector
	List<SysMLFlowConnector> vehiclesToDeckConnector;

	/**
	 * Binding connector that binds the bridge cable load values to the analysis
	 * case
	 */
	@BindingConnector
	public SysMLBindingConnector cableLoadsBindingConnector;
	/**
	 * Binding connector that binds the bridge cable available capacity values to
	 * the analysis case
	 */
	@BindingConnector
	public SysMLBindingConnector cableAvailablesBindingConnector;

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
		bridgeDeckLoadsAnalysisCase.start();
	}

	@Override
	public void stop()
	{
		bridgeDeckLoadsAnalysisCase.stop();
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
		groundAnchorEast = new GroundAnchor("GroundAnchorEast", 0L, 1);
		groundBase = new GroundBase();
		vehicles = new Vehicles();
	}

	@Override
	protected void createAnalysisCases()
	{
		bridgeDeckLoadsAnalysisCase = new BridgeDeckLoadsAnalysisCase();
	}

	@Override
	protected void createFlowConnectors()
	{
		deckToWestGroundAnchorConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, List.of(bridge.deck.groundConnectionsWest.get(0), bridge.deck.groundConnectionsWest.get(1)),
		List.of(groundAnchorWest.northDeckLoadReceiver, groundAnchorWest.southDeckLoadReceiver), "DeckToWestGroundAnchor", 0L);

		deckToEastGroundAnchorConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, List.of(bridge.deck.groundConnectionsEast.get(0), bridge.deck.groundConnectionsEast.get(1)),
		List.of(groundAnchorEast.northDeckLoadReceiver, groundAnchorEast.southDeckLoadReceiver), "DeckToEastGroundAnchor", 0L);

		pylonToGroundConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, bridge.pylon.groundLoadTransmitter, groundBase.pylonLoadReceiver, "PylonToGround", 0L);

		vehiclesToDeckConnector = new ArrayList<>(Vehicles.directionBoundCount * 2);
		for (int index = 0; index < Vehicles.directionBoundCount; index++)
		{
			vehiclesToDeckConnector.add(new SysMLFlowConnector(TypesEnum.peertopeer, false,
			List.of(vehicles.vehiclesEastbound.get(index).deckLoadTransmitter, vehicles.vehiclesWestbound.get(index).deckLoadTransmitter),
			List.of(bridge.deck.vehiclesEastBound.get(index), bridge.deck.vehiclesWestBound.get(index)), "VehiclesToDeck", 0L));
		}
	}

	@Override
	protected void createBindingConnectors()
	{
		cableLoadsBindingConnector = new SysMLBindingConnector(bridge.cableLoads, bridgeDeckLoadsAnalysisCase, BridgeDeckLoadsAnalysisCase.cableLoadsKey);
		cableAvailablesBindingConnector = new SysMLBindingConnector(bridge.cableAvailables, bridgeDeckLoadsAnalysisCase, BridgeDeckLoadsAnalysisCase.cableAvailablesKey);

		// Put the connectors into the maps used by the parametric analysis to obtain
		// the updated values of the bound attributes
		bridgeDeckLoadsAnalysisCase.paramConnectors.put(BridgeDeckLoadsAnalysisCase.cableLoadsKey, cableLoadsBindingConnector);
		bridgeDeckLoadsAnalysisCase.paramConnectors.put(BridgeDeckLoadsAnalysisCase.cableAvailablesKey, cableAvailablesBindingConnector);
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
