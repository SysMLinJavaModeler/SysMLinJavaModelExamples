package cablestayedbridge;

import java.util.Optional;
import cablestayedbridge.ports.PylonToGroundLoadReceiver;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Hyperlink;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLHyperlink;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.AreaFeetSquare;
import sysmlinjava.valuetypes.WeightPounds;

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
public class GroundBase extends SysMLBlock implements LoadBearingComponent
{
	/**
	 * Full port that receives the load of the pylon
	 */
	@FullPort
	PylonToGroundLoadReceiver pylonLoadReceiver;

	/**
	 * Flow of the weight of the pylon
	 */
	@Flow
	WeightPounds pylonWeight;

	/**
	 * Value for the area of the base
	 */
	@Value
	AreaFeetSquare baseArea;
	/**
	 * Value for the load capacity of the base
	 */
	@Value
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

	@Reception
	@Override
	public void onLoad(Load load)
	{
		logger.info(String.format("pylon load=%,6.2f", load.weight.value));
		pylonWeight.setValue(load.weight);
	}

	@Reception
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
	protected void createValues()
	{
		baseArea = new AreaFeetSquare(150.0 * 50.0);
		baseCapacity = new WeightPounds(300_000_000);
	}

	@Override
	protected void createFlows()
	{
		pylonWeight = new WeightPounds(0);
	}

	@Override
	protected void createFullPorts()
	{
		pylonLoadReceiver = new PylonToGroundLoadReceiver(this, 0L, "PylonToGroundLoadReceiver");
	}

	@SuppressWarnings("unused")
	@Override
	protected void createConstraints()
	{
		constraint = () ->
		{
			boolean fail = !pylonWeight.greaterThan(baseCapacity);
		};
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new LoadBearingComponentStateMachine(this, true, "GroundBaseStateMachine"));
	}

	@Override
	protected void createHyperlinks()
	{
		groundBaseSpecification = new SysMLHyperlink("Cable-Stayed Bridge Ground Base Specification", "http://SuspensionBridgeBuilders.com/Specs/CableStayedBridgeGroundBaseSpecification.pdf");
	}
}
