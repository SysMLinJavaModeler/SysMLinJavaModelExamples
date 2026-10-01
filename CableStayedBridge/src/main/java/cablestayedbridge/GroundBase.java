package cablestayedbridge;

import java.util.Optional;

import cablestayedbridge.ports.PylonToGroundLoadReceiver;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.AreaFeetSquare;
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
 * SysMLinJava block-based representation of the ground base for the pylon of a
 * cable-stayed bridge. The ground base supports the load carried by the pylon
 * as well as the weight of the pylon itself. The ground base is located under
 * the pylon at the center of the bridge.
 * <p>
 * The {@code GroundBase} is a load bearing component of the domain of the
 * bridge. It has a single full port that receives the load of the pylon. The
 * {@code GroundBase} is characterized by its values for area and load capacity,
 * and by its flow of the weight of the pylon on the base that is transferred to
 * the ground. The {@code GroundBase} behaves as a
 * {@code LoadBearingComponentStateMachine} and operates asynchronously with the
 * bridge.
 * 
 * @author ModelerOne
 *
 */
public class GroundBase extends SysMLPart implements LoadBearingComponent
{
	/**
	 * Full port that receives the load of the pylon
	 */
	@Port
	PylonToGroundLoadReceiver pylonLoadReceiver;

	/**
	 * Flow of the weight of the pylon
	 */
	@Attribute
	WeightPounds pylonWeight;

	/**
	 * Value for the area of the base
	 */
	@Attribute
	AreaFeetSquare baseArea;
	/**
	 * Value for the load capacity of the base
	 */
	@Attribute
	WeightPounds baseCapacity;

	/**
	 * Constraint for the failure of the ground base
	 */
	@Constraint
	SysMLConstraint constraint;

	/**
	 * Hyperlink to ground base specification document
	 */
	@Hyperlink
	SysMLHyperlink groundBaseSpecification;

	/**
	 * Constructor
	 */
	public GroundBase()
	{
		super("GroundBase", 0L);
	}

	@Action
	@Override
	public void onLoad(Load load)
	{
		logger.info(String.format("pylon load=%,6.2f", load.weight.value));
		pylonWeight.setValue(load.weight);
	}

	@Action
	@Override
	public void onLoaded(Load load)
	{
		try
		{
		onLoad(load);
		
		if (!pylonWeight.greaterThan(baseCapacity))
			logger.info(String.format("%s: load=%,d", name.get(), (int)pylonWeight.value));
		else
			acceptEvent(new FailureEvent());
		}catch(Exception e) {e.printStackTrace();}
	}

	@Override
	public void onFailed(FailureEvent failure)
	{
		logger.warning(String.format("GroundBase %s: failed by %,5.2f ", name.get(), baseCapacity.subtracted(pylonWeight).value));
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createAttributes()
	{
		baseArea = new AreaFeetSquare(150.0 * 50.0);
		baseCapacity = new WeightPounds(300_000_000);
		pylonWeight = new WeightPounds(0);
	}

	@Override
	protected void createPorts()
	{
		pylonLoadReceiver = new PylonToGroundLoadReceiver(this, 0L, "PylonToGroundLoadReceiver");
	}

	@Override
	protected void createConstraints()
	{
		constraint = new SysMLConstraint(Optional.of((BasicConstraintFunction)() ->
		{
			boolean fail = !pylonWeight.greaterThan(baseCapacity);
			return fail;
		}), new SysMLDocumentation("""
			boolean fail = !pylonWeight.greaterThan(baseCapacity);
			return fail;
			"""), "ground base failure constraint", 0L);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new LoadBearingComponentStateMachine(this, true, "GroundBaseStateMachine"));
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		groundBaseSpecification = new SysMLHyperlink("Cable-Stayed Bridge Ground Base Specification", "http://SuspensionBridgeBuilders.com/Specs/CableStayedBridgeGroundBaseSpecification.pdf");
	}
}
