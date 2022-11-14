package cablestayedbridge;

import static java.lang.Math.pow;
import static java.lang.Math.toDegrees;
import java.util.Optional;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.atan;
import cablestayedbridge.ports.CableToPylonLoadTransmitter;
import cablestayedbridge.ports.DeckToCableLoadReceiver;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.annotations.comments.Problem;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.comments.SysMLProblem;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.AreaInchesSquare;
import sysmlinjava.valuetypes.DirectionDegrees;
import sysmlinjava.valuetypes.DistanceFeet;
import sysmlinjava.valuetypes.ForcePoundsPerInchSquare;
import sysmlinjava.valuetypes.Point2D;
import sysmlinjava.valuetypes.WeightPounds;

/**
 * SysMLinJava block-based representation of a cable used to suspend the deck of
 * a cable-stayed bridge. The cable is connected to the bridge deck at points
 * equidistant from the bridges pylon and on opposite sides of the bridge and is
 * routed over the top of the pylon. The {@code Cable} is characterized by
 * values for its strength, weight, size, and the angle of its force on the
 * point of suspension. It has flows for the vertical weight is suspends at each
 * end and the force on the cable to suspend the weight at end.
 * <p>
 * The cable has a port at each end of the cable to receive the vertical weight,
 * and a port (presumably in the middle of the cable) to transfer the weight it
 * suspends to the bridge's pylon or tower.
 * <p>
 * The calculation of the cable's forces and its failure condition is specified
 * by a {@code constraint} property.
 * 
 * @author ModelerOne
 *
 */
public class Cable extends SysMLBlock implements LoadBearingComponent
{
	/**
	 * Port to receive the load of the bridge deck at the east end of the cable
	 */
	@FullPort
	DeckToCableLoadReceiver deckLoadEastReceiver;
	/**
	 * Port to receive the load of the bridge deck at the west end of the cable
	 */
	@FullPort
	DeckToCableLoadReceiver deckLoadWestReceiver;
	/**
	 * Port to transmit the load suspended by the cable onto the bridge's pylon or
	 * tower
	 */
	@FullPort
	CableToPylonLoadTransmitter pylonLoadTransmitter;

	/**
	 * Flow of the downward force on the cable at its east suspension point
	 */
	@Flow
	WeightPounds eastForceDown;
	/**
	 * Flow of the downward force on the cable at its west suspension point
	 */
	@Flow
	WeightPounds westForceDown;
	/**
	 * Flow of the total downward force on the cable
	 */
	@Flow
	WeightPounds totalForceDown;
	/**
	 * Flow of the tensile force (load) on the cable at its east suspension point
	 */
	@Flow
	WeightPounds eastLoad;
	/**
	 * Flow of the tensile force (load) on the cable at its west suspension point
	 */
	@Flow
	WeightPounds westLoad;
	/**
	 * Flow of the total tensile force (load) on the cable
	 */
	@Flow
	WeightPounds totalLoad;

	/**
	 * Value for the tensile strength of the cable
	 */
	@Value
	ForcePoundsPerInchSquare tensileStrength;
	/**
	 * Value for the cross-sectional area of the cable, i.e. how much "wire" is in
	 * the cable
	 */
	@Value
	AreaInchesSquare crossSectionArea;
	/**
	 * Value for the cable's breaking strength
	 */
	@Value
	WeightPounds breakingStrength;
	/**
	 * Value of the socket's breaking strength for any socket that may be used to
	 * connect the cable to the deck connection point and/or to the pylon.
	 */
	@Value
	WeightPounds socketBreakingStrength;
	/**
	 * Value of the weight per cubic foot of the cable
	 */
	@Value
	WeightPounds weightPerCubicFoot;
	/**
	 * Value for the length of the cable
	 */
	@Value
	DistanceFeet length;
	/**
	 * Value for the weight of the cable
	 */
	@Value
	WeightPounds weight;
	/**
	 * Value for point on the west side of the bridge deck at which this cable
	 * suspends the deck
	 */
	@Value
	Point2D westPoint;
	/**
	 * Value for point on the east side of the bridge deck at which this cable
	 * suspends the deck
	 */
	@Value
	Point2D eastPoint;
	/**
	 * Value for the angle of the cable relative to the deck whose weight is
	 * suspended by the cable. Used to calculate the tension force on the cable to
	 * suspend the vertical force of the deck.
	 */
	@Value
	DirectionDegrees angleToPylonTop;

	/**
	 * Problem comment for insufficient cable weight
	 */
	@Problem
	SysMLProblem cableWeightProblem;

	/**
	 * Constraint for the forces on the cable and its failure conditions
	 */
	@Constraint
	SysMLConstraint constraint;

	/**
	 * Hyperlink for cable specification document
	 */
	@Hyperlink
	SysMLHyperlink cableSpecification;

	/**
	 * Constructor
	 * 
	 * @param name                 unique name
	 * @param id                   unique ID
	 * @param bridge               bridge of which this cable is a part
	 */
	public Cable(String name, long id, CableStayedBridge bridge)
	{
		super(bridge, name, id);
	}

	@Reception
	@Override
	public void onLoad(Load load)
	{
		WeightPounds poundsForce = new WeightPounds(load.weight.dividedBy(sin(angleToPylonTop.toRadians().value)));
		if (load.direction.equals(DirectionDegrees.east))
		{
			eastForceDown.setValue(load.weight);
			eastLoad.setValue(poundsForce);
		}
		else if (load.direction.equals(DirectionDegrees.west))
		{
			westForceDown.setValue(load.weight);
			westLoad.setValue(poundsForce);
		}
	}

	@Reception
	@Override
	public void onLoaded(Load load)
	{
		onLoad(load);

		totalLoad.setValue(eastLoad.added(westLoad));
		if (!totalLoad.greaterThan(breakingStrength) && !totalLoad.greaterThan(socketBreakingStrength))
		{
			logger.info(String.format("%s: load=%,d, eastload=%,d, westload=%,d", name.get(), (int)totalLoad.value, (int)eastLoad.value, (int)westLoad.value));

			totalForceDown.setValue(eastForceDown.added(westForceDown).added(weight));
			pylonLoadTransmitter.transmit(new Load(totalForceDown, name.get(), false));
		}
		else
			acceptEvent(new FailureEvent());
	}

	@Override
	public void onFailed(FailureEvent failure)
	{
		logger.info(String.format(" %s: failed by %5.2f ", name.get(), breakingStrength.subtracted(eastLoad.added(westLoad)).value));
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createValues()
	{
		tensileStrength = new ForcePoundsPerInchSquare(300 * 100); // Assumed 100 mpa * 300 kpsi/mpa = 300 * 100 kpsi
		weightPerCubicFoot = new WeightPounds(200);
		double distToPylonNSCenterline = (eastPoint.xValue - westPoint.xValue) / 2;
		double distToPylonEWCenterline = ((CableStayedBridge)contextBlock.get()).deck.width.value / 2;
		double distToPylonCenter = sqrt(pow(distToPylonNSCenterline, 2.0) + pow(distToPylonEWCenterline, 2.0));
		double heightOfPylonTop = ((CableStayedBridge)contextBlock.get()).pylon.heightAboveDeck.value;
		angleToPylonTop = new DirectionDegrees(toDegrees(atan(heightOfPylonTop / distToPylonCenter)));
		double distToPylonTopCenter = sqrt(pow(distToPylonCenter,2.0) + pow(heightOfPylonTop,2.0));
		length = new DistanceFeet(distToPylonTopCenter * 2);
	}

	@Override
	protected void createFlows()
	{
		eastForceDown = new WeightPounds(0);
		westForceDown = new WeightPounds(0);
		totalForceDown = new WeightPounds(0);
		eastLoad = new WeightPounds(0);
		westLoad = new WeightPounds(0);
		totalLoad = new WeightPounds(0);
	}

	/**
	 * Creates the constraint for cable failure by calculating the angle of the
	 * cable's force vector at each end of the cable and then using that angle to
	 * calculate the force on the cable to suspend the vertical loads at each end of
	 * the cable. Failure occurs if/when the total force on the cable exceeds its
	 * breaking strength or exceeds the breaking strength of the socket(s) that
	 * connect the cable to the deck and pylon.
	 */
	@SuppressWarnings("unused")
	@Override
	protected void createConstraints()
	{
		constraint = () ->
		{
			double socketBreakingStrength = 0; // param: breaking strength of socket on cable
			double breakingStrength = 0; // param: breaking strength of cable
			double eastForceDown = 0; // param: vertical load on east end of cable
			double westForceDown = 0; // param: vertical load on west end of cable
			double eastPoint = 0; // param: distance along deck of east connect point
			double westPoint = 0; // param: distance along deck of west connect point
			double deckWidth = 0; // param: width of bridge deck, i.e. distance between connect points
			double pylonHeightAboveDeck = 0; // param: pylon height above connect point
			double distToPylonNSCenterline = (eastPoint - westPoint) / 2;
			double distToPylonEWCenterline = deckWidth / 2;
			double distToPylonCenter = sqrt(pow(distToPylonNSCenterline, 2.0) + pow(distToPylonEWCenterline, 2.0));
			double heightOfPylonTop = pylonHeightAboveDeck;
			double angleToPylonTop = atan(heightOfPylonTop / distToPylonCenter);
			double forceOnCable = eastForceDown / sin(angleToPylonTop) + westForceDown / sin(angleToPylonTop);
			boolean fail = forceOnCable > breakingStrength || forceOnCable > socketBreakingStrength;
		};
	}

	@Override
	protected void createFullPorts()
	{
		deckLoadEastReceiver = new DeckToCableLoadReceiver(this, 0L, "DeckLoadEast");
		deckLoadWestReceiver = new DeckToCableLoadReceiver(this, 0L, "DeckLoadWest");
		pylonLoadTransmitter = new CableToPylonLoadTransmitter(this, 0L, "PylonLoad");
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new LoadBearingComponentStateMachine(this, false, "CableStateMachine"));
	}

	@Override
	protected void createProblems()
	{
		cableWeightProblem = new SysMLProblem("Value for cable weight per cubic foot is just an estimate.  Needs to be based on actual wire guages, materials, bundling, etc.");
	}

	@Override
	protected void createHyperlinks()
	{
		cableSpecification = new SysMLHyperlink("Cable-Stayed Bridge Cable Specification", "http://SuspensionBridgeCableSuppliers.com/Specs/CableStayedBridgeCableSpecification.pdf");
	}
}
