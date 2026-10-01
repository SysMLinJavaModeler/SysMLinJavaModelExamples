package cablestayedbridge;

import java.util.List;
import java.util.Optional;

import cablestayedbridge.ports.CableToPylonLoadReceiver;
import cablestayedbridge.ports.DeckToPylonLoadReceiver;
import cablestayedbridge.ports.PylonToGroundLoadTransmitter;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.DistanceFeet;
import sysmlinjava.attributetypes.KeyValueMap;
import sysmlinjava.attributetypes.WeightPounds;
import sysmlinjava.constraint.BasicConstraintFunction;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalEvent;

/**
 * SysMLinJava block-based representation of the pylon of a cable-stayed bridge.
 * The pylon supports all of the cables that suspend the bridge deck as well as
 * part of the bridge deck itself.
 * <p>
 * The {@code Pylon} is a load bearing component of the bridge. It has a number
 * of full ports that receive the loads of the cables and part of the bridge
 * deck and transmits the load of the pylon to the ground base of the bridge.
 * The {@code Pylon} is characterized by its values for height, weight, breaking
 * strength, and height above the deck, and by its flows of the weight of the
 * cables and the deck on the pylon that are transferred to the ground. The
 * {@code Pylon} behaves as a {@code LoadBearingComponentStateMachine} and
 * operates synchronously with the other components of the bridge.
 * 
 * @author ModelerOne
 *
 */
public class Pylon extends SysMLPart implements LoadBearingComponent
{
	/**
	 * Port that receives the load of the cables that carry the bridge deck
	 */
	@Port
	List<CableToPylonLoadReceiver> cableLoadReceivers;
	/**
	 * Port that receives the load of the part/center of the deck that rests on the
	 * pylon
	 */
	@Port
	List<DeckToPylonLoadReceiver> deckLoadReceivers;
	/**
	 * Port that transmits the load of the pylon to the ground
	 */
	@Port
	PylonToGroundLoadTransmitter groundLoadTransmitter;

	/**
	 * Flow of weight of the cables on the pylon
	 */
	@Attribute
	KeyValueMap<Integer, WeightPounds> cableLoads;
	/**
	 * Flow of weight of the deck sections on the pylon
	 */
	@Attribute
	KeyValueMap<Integer, WeightPounds> deckLoads;
	/**
	 * Flow of total weight of all (cable and deck sections) loads on the pylon
	 */
	@Attribute
	WeightPounds totalLoad;

//	@Attribute
//	ListOrdered<WeightPounds> cableLoads;
	/**
	 * Value for the height of the pylon from the ground base
	 */
	@Attribute
	DistanceFeet height;
	/**
	 * Value for the height of the pylon from the ground base
	 */
	@Attribute
	DistanceFeet width;
	/**
	 * Value for the width of the pylon from the ground base
	 */
	@Attribute
	DistanceFeet depth;
	/**
	 * Value for the depth of the pylon above the deck
	 */
	@Attribute
	DistanceFeet heightAboveDeck;
	/**
	 * Value for the breaking strength of the pylon, i.e. the amount of weight
	 * received from the cables and deck before failure
	 */
	@Attribute
	WeightPounds breakingStrength;
	/**
	 * Value for the weight of the pylon material
	 */
	@Attribute
	WeightPounds materialWeightPerFootCubic;
	/**
	 * Value for the weight of the pylon itself
	 */
	@Attribute
	WeightPounds weight;

	/**
	 * Constraint for the transfer of the cable and deck loads to the ground
	 */
	@Constraint
	SysMLConstraint constraint;
	
	/**
	 * Hyperlink for pylon specification document
	 */
	@Hyperlink
	SysMLHyperlink pylonSpecification;

	/**
	 * Constructor
	 */
	public Pylon()
	{
		super("Pylon", 0L);
	}

	@Action
	@Override
	public void onLoad(Load load)
	{
		if (load.name.get().startsWith("Cable"))
			cableLoads.put(load.id.intValue(), load.weight);
		else if (load.name.get().startsWith("BridgeSection"))
			deckLoads.put(load.id.intValue(), load.weight);
	}

	@Action
	@Override
	public void onLoaded(Load load)
	{
		onLoad(load);

		WeightPounds allLoads = new WeightPounds(0);
		cableLoads.values().forEach(loadValue -> allLoads.add(loadValue));
		deckLoads.values().forEach(loadValue -> allLoads.add(loadValue));
		totalLoad.setValue(allLoads);
		if (!allLoads.greaterThan(breakingStrength))
		{
			WeightPounds transmitLoads = new WeightPounds(weight.added(allLoads)); 
			logger.info(String.format("%s: load=%,d", name.get(), (int)transmitLoads.value));
			groundLoadTransmitter.transmit(new Load(transmitLoads, true));
		}
		else
			acceptEvent(new FailureEvent());
	}

	@Override
	public void onFailed(FailureEvent failure)
	{
		logger.warning(String.format("Pylon %s: failed by %5.2f ", name.get(), breakingStrength.subtracted(totalLoad).value));
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createAttributes()
	{
		height = new DistanceFeet(300);
		width = new DistanceFeet(150);
		depth = new DistanceFeet(50);
		heightAboveDeck = new DistanceFeet(250);
		breakingStrength = new WeightPounds(5_000_000);
		materialWeightPerFootCubic = new WeightPounds(120);
		weight = new WeightPounds(height.multipliedBy(width).multipliedBy(depth).multipliedBy(materialWeightPerFootCubic)); // 120 lbs/ft3 * 150 ft tall * 150 ft wide * 50 ft deep
		cableLoads = new KeyValueMap<>();
		deckLoads = new KeyValueMap<>();
		totalLoad = new WeightPounds(0);
	}

	@Override
	protected void createConstraints()
	{
		constraint = new SysMLConstraint(Optional.of((BasicConstraintFunction)() ->
		{
			WeightPounds allLoads = new WeightPounds(0);
			cableLoads.values().forEach(loadValue -> allLoads.add(loadValue));
			deckLoads.values().forEach(loadValue -> allLoads.add(loadValue));
			boolean fail = allLoads.greaterThan(breakingStrength);
			return fail;
		}), new SysMLDocumentation("""
			WeightPounds allLoads = new WeightPounds(0);
			cableLoads.values().forEach(loadValue -> allLoads.add(loadValue));
			deckLoads.values().forEach(loadValue -> allLoads.add(loadValue));
			boolean fail = allLoads.greaterThan(breakingStrength);
			return fail;
		"""), "pylon constraint", 0L);
	}

	@Override
	protected void createPorts()
	{
		cableLoadReceivers = List.of(
			new CableToPylonLoadReceiver(this, 0L, "cableLoadReceiverNW0toSE9"),
			new CableToPylonLoadReceiver(this, 1L, "cableLoadReceiverNW1toSE8"),
			new CableToPylonLoadReceiver(this, 2L, "cableLoadReceiverNW2toSE7"),
			new CableToPylonLoadReceiver(this, 3L, "cableLoadReceiverNW3toSE6"),
			new CableToPylonLoadReceiver(this, 4L, "cableLoadReceiverNW4toSE5"),
			new CableToPylonLoadReceiver(this, 5L, "cableLoadReceiverSW0toNE9"),
			new CableToPylonLoadReceiver(this, 6L, "cableLoadReceiverSW1toNE8"),
			new CableToPylonLoadReceiver(this, 7L, "cableLoadReceiverSW2toNE7"),
			new CableToPylonLoadReceiver(this, 8L, "cableLoadReceiverSW3toNE6"),
			new CableToPylonLoadReceiver(this, 9L, "cableLoadReceiverSW4toNE5"));

		deckLoadReceivers = List.of(
			new DeckToPylonLoadReceiver(this, 0L, "deckLoadReceiverNorth"),
			new DeckToPylonLoadReceiver(this, 1L, "deckLoadReceiverSouth"));

		groundLoadTransmitter = new PylonToGroundLoadTransmitter(this, 0L, "groundLoadTransmitter");
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new LoadBearingComponentStateMachine(this, false, "PylonStateMachine"));
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		pylonSpecification = new SysMLHyperlink("Cable-Stayed Bridge Pylon Specification", "http://SuspensionBridgePylonBuilders.com/Specs/CableStayedBridgePylonSpecification.pdf");
	}
}