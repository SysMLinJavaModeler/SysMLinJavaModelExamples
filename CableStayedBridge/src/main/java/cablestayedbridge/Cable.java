package cablestayedbridge;

import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;

import java.util.Optional;

import cablestayedbridge.ports.CableToPylonLoadTransmitter;
import cablestayedbridge.ports.DeckToCableLoadReceiver;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.AreaInchesSquare;
import sysmlinjava.attributetypes.DirectionDegrees;
import sysmlinjava.attributetypes.DistanceFeet;
import sysmlinjava.attributetypes.ForcePoundsPerInchSquare;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.attributetypes.WeightPounds;
import sysmlinjava.constraint.BasicConstraintFunction;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.metadata.Issue;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.metadata.SysMLIssue;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalEvent;

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
 */
public class Cable extends SysMLPart implements LoadBearingComponent
{
	/**
	 * Port to receive the load of the bridge deck at the east end of the cable
	 */
	@Port
	DeckToCableLoadReceiver deckLoadEastReceiver;
	/**
	 * Port to receive the load of the bridge deck at the west end of the cable
	 */
	@Port
	DeckToCableLoadReceiver deckLoadWestReceiver;
	/**
	 * Port to transmit the load suspended by the cable onto the bridge's pylon or
	 * tower
	 */
	@Port
	CableToPylonLoadTransmitter pylonLoadTransmitter;

	/**
	 * Flow of the downward force on the cable at its east suspension point
	 */
	@Attribute
	WeightPounds eastForceDown;
	/**
	 * Flow of the downward force on the cable at its west suspension point
	 */
	@Attribute
	WeightPounds westForceDown;
	/**
	 * Flow of the total downward force on the cable
	 */
	@Attribute
	WeightPounds totalForceDown;
	/**
	 * Flow of the tensile force (load) on the cable at its east suspension point
	 */
	@Attribute
	WeightPounds eastLoad;
	/**
	 * Flow of the tensile force (load) on the cable at its west suspension point
	 */
	@Attribute
	WeightPounds westLoad;
	/**
	 * Flow of the total tensile force (load) on the cable
	 */
	@Attribute
	WeightPounds totalLoad;

	/**
	 * Value for the tensile strength of the cable
	 */
	@Attribute
	ForcePoundsPerInchSquare tensileStrength;
	/**
	 * Value for the cross-sectional area of the cable, i.e. how much "wire" is in
	 * the cable
	 */
	@Attribute
	AreaInchesSquare crossSectionArea;
	/**
	 * Value for the cable's breaking strength
	 */
	@Attribute
	WeightPounds breakingStrength;
	/**
	 * Value of the socket's breaking strength for any socket that may be used to
	 * connect the cable to the deck connection point and/or to the pylon.
	 */
	@Attribute
	WeightPounds socketBreakingStrength;
	/**
	 * Value of the weight per cubic foot of the cable
	 */
	@Attribute
	WeightPounds weightPerCubicFoot;
	/**
	 * Value for the length of the cable
	 */
	@Attribute
	DistanceFeet length;
	/**
	 * Value for the weight of the cable
	 */
	@Attribute
	WeightPounds weight;
	/**
	 * Value for point on the west side of the bridge deck at which this cable
	 * suspends the deck
	 */
	@Attribute
	Point2D westPoint;
	/**
	 * Value for point on the east side of the bridge deck at which this cable
	 * suspends the deck
	 */
	@Attribute
	Point2D eastPoint;
	/**
	 * Value for point on the top of the pylon where the cable load is transferred
	 * to the pylon, i.e. the point of the cable saddle suspends the deck
	 */
	@Attribute
	RReal topPoint;
	/**
	 * Value for the angle of the cable relative to the deck whose weight is
	 * suspended by the cable. Used to calculate the tension force on the cable to
	 * suspend the vertical force of the deck.
	 */
	@Attribute
	DirectionDegrees angleToPylonTop;

	/**
	 * Problem comment for insufficient cable weight
	 */
	@Issue
	SysMLIssue cableWeightProblem;

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
	 * @param name   unique name
	 * @param id     unique ID
	 * @param bridge bridge of which this cable is a part
	 */
	public Cable(String name, long id, CableStayedBridge bridge)
	{
		super(Optional.of(bridge), name, id);
	}

	@Action
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

	@Action
	@Override
	public void onLoaded(Load load)
	{
		onLoad(load);

		totalLoad.setValue(eastLoad.added(westLoad));
		if (!totalLoad.greaterThan(breakingStrength) && !totalLoad.greaterThan(socketBreakingStrength))
		{
			logger.info(String.format("%s: load=%,d, eastload=%,d, westload=%,d", name.get(), (int) totalLoad.value, (int) eastLoad.value, (int) westLoad.value));

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
	protected void createAttributes()
	{
		tensileStrength = new ForcePoundsPerInchSquare(300 * 100); // Assumed 100 mpa * 300 kpsi/mpa = 300 * 100 kpsi
		weightPerCubicFoot = new WeightPounds(200);
		double distToPylonNSCenterline = (eastPoint.xValue - westPoint.xValue) / 2;
		double distToPylonEWCenterline = (Math.abs(eastPoint.yValue - westPoint.xValue)) / 2;
		double distToPylonCenter = sqrt(pow(distToPylonNSCenterline, 2.0) + pow(distToPylonEWCenterline, 2.0));
		double heightOfPylonTop = topPoint.value;
		angleToPylonTop = new DirectionDegrees(toDegrees(atan(heightOfPylonTop / distToPylonCenter)));
		double distToPylonTopCenter = sqrt(pow(distToPylonCenter, 2.0) + pow(heightOfPylonTop, 2.0));
		length = new DistanceFeet(distToPylonTopCenter * 2);
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
	@Override
	protected void createConstraints()
	{
		constraint = new SysMLConstraint(Optional.of((BasicConstraintFunction) () ->
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
			return fail;
		}), new SysMLDocumentation("""
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
		return fail;
		"""), "cable failure constraint", 0L);
	}

	@Override
	protected void createPorts()
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
	protected void createIssues()
	{
		cableWeightProblem = new SysMLIssue("Value for cable weight per cubic foot is just an estimate.  Needs to be based on actual wire guages, materials, bundling, etc.",
		"estimatedCableStrengths", 0L);
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		cableSpecification = new SysMLHyperlink("Cable-Stayed Bridge Cable Specification", "http://SuspensionBridgeCableSuppliers.com/Specs/CableStayedBridgeCableSpecification.pdf");
	}
}
