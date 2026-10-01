package cablestayedbridge;

import java.util.Optional;

import cablestayedbridge.ports.DeckToGroundLoadReceiver;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.DirectionDegrees;
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
 * SysMLinJava block-based representation of the ground anchor of a cable-stayed
 * bridge. The ground anchor supports one end of the bridge deck. There is a
 * ground anchor at each end of the bridge which is anchored to the ground to
 * support the weight at the end of the deck as well as hold the deck in place
 * lateraly.
 * <p>
 * The {@code GroundAnchor} is a load bearing component of the domain of the
 * bridge. It has two full ports that receive the loads of the two corners of
 * the end section of the bridge deck. The {@code GroundAnchor} is characterized
 * by its values for load capacity at each the two load points, and by its flows
 * of the weight of the end of the deck on the anchor that are transferred to
 * the ground. The {@code GroundAnchor} behaves as a
 * {@code LoadBearingComponentStateMachine} and operates asynchronously with the
 * bridge.
 * 
 * @author ModelerOne
 *
 */
public class GroundAnchor extends SysMLPart implements LoadBearingComponent
{
	/**
	 * Full port that receives the load of the north corner of the deck
	 */
	@Port
	DeckToGroundLoadReceiver northDeckLoadReceiver;
	/**
	 * Full port that receives the load of the south corner of the deck
	 */
	@Port
	DeckToGroundLoadReceiver southDeckLoadReceiver;

	/**
	 * Flow of the weight received from the north corner of the deck
	 */
	@Attribute
	WeightPounds northLoad;
	/**
	 * Flow of the weight received from the south corner of the deck
	 */
	@Attribute
	WeightPounds southLoad;

	/**
	 * Value of the weight capacity of the north end of the anchor
	 */
	@Attribute
	WeightPounds northLoadCapacity;
	/**
	 * Value of the weight capacity of the south end of the anchor
	 */
	@Attribute
	WeightPounds southLoadCapacity;

	/**
	 * Constraint for failure condition
	 */
	@Constraint
	SysMLConstraint constraint;
	
	/**
	 * Hyperlink to ground anchor speicification document
	 */
	@Hyperlink
	SysMLHyperlink groundAnchorSpecification;

	/**
	 * Constructor
	 * 
	 * @param name  unique name
	 * @param id    unique ID
	 * @param index index of this anchor into a list of anchors, where 0 is the west
	 *              anchor, 1 east
	 */
	public GroundAnchor(String name, Long id, Integer index)
	{
		super(name, id);
	}

	@Action
	@Override
	public void onLoad(Load load)
	{
		if (load.direction.equals(DirectionDegrees.north))
			northLoad.setValue(load.weight);
		else if (load.direction.equals(DirectionDegrees.south))
			southLoad.setValue(load.weight);
	}

	@Action
	@Override
	public void onLoaded(Load load)
	{
		onLoad(load);
		if (!northLoad.greaterThan(northLoadCapacity) && !southLoad.greaterThan(southLoadCapacity))
			logger.info(String.format("%s: load=%,d", name.get(), (int)northLoad.added(southLoad).value));
		else
			acceptEvent(new FailureEvent());
	}

	@Override
	public void onFailed(FailureEvent failure)
	{
		logger.warning(String.format("GroundAnchor %s: failed by %5.2f ", name.get(), northLoad.added(southLoad).subtracted(northLoadCapacity.added(southLoadCapacity)).value));
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createAttributes()
	{
		northLoadCapacity = new WeightPounds(800_000);
		southLoadCapacity = new WeightPounds(800_000);
		northLoad = new WeightPounds(0);
		southLoad = new WeightPounds(0);
	}

	@Override
	protected void createPorts()
	{
		northDeckLoadReceiver = new DeckToGroundLoadReceiver(this, 0L, "NorthDeckLoadReceiver");
		southDeckLoadReceiver = new DeckToGroundLoadReceiver(this, 0L, "SouthDeckLoadReceiver");
	}

	@Override
	protected void createConstraints()
	{
		constraint = new SysMLConstraint(Optional.of((BasicConstraintFunction)() ->
		{
			boolean fail = !northLoad.greaterThan(northLoadCapacity) &&
				           !southLoad.greaterThan(southLoadCapacity);
			return fail;
		}), new SysMLDocumentation("""
			boolean fail = !northLoad.greaterThan(northLoadCapacity) &&
                           !southLoad.greaterThan(southLoadCapacity);
			return fail;
			"""), "ground-anchor failure constraint", 0L);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new LoadBearingComponentStateMachine(this, true, "GroundAnchorStateMachine"));
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		groundAnchorSpecification = new SysMLHyperlink("Cable-Stayed Bridge Ground Anchor Specification", "http://SuspensionBridgeBuilders.com/Specs/CableStayedBridgeGroundAnchorSpecification.pdf");
	}
}
