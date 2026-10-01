package roboticmower.mower;

import java.util.Optional;
import roboticmower.info.WheelsControl;
import roboticmower.ports.WheelsControlReceiver;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalEvent;

/**
 * Controller for the mower's drive components, i.e the wheels. The controller
 * periodically receives wheel controls from the mower controller. In this first
 * version of the RoboticMower mower model, the {@code MowerDriver} does nothing
 * more than log the receipt of the controls, but in a more completed version of
 * the model, the mower driver would use the received control data to change the
 * speed and direction of the mower by physically controlling the rotation and
 * steering of the wheels over time.
 * 
 * @author ModelerOne
 * @see roboticmower.mower.MowerControllerStateMachine
 */
public class MowerDriver extends SysMLPart
{
	/**
	 * Port to receive the wheel controls
	 */
	@Port
	WheelsControlReceiver wheelsControlReceiver;

	/**
	 * Value for the wheels' diameter - for use in calculating wheel RPM for the
	 * controlled mower speed
	 */
	@Attribute
	public DistanceMeters wheelDiameter;

	/**
	 * Constructor
	 * 
	 * @param contextBlock {@code Mower} in whose context this driver is to operate
	 */
	public MowerDriver(Mower contextBlock)
	{
		super(Optional.of(contextBlock), "MowerDriver", 0L);
	}

	/**
	 * Operation to initialize the mower driver for operation
	 */
	@Action
	public void initialize()
	{
		logger.info("initialized");
	}

	/**
	 * Reception to respond to the receipt of a wheels control
	 * 
	 * @param control wheels control received via the port
	 */
	@Action
	public void onWheelsControl(WheelsControl control)
	{
		logger.info(control.toString());
	}

	@Override
	public void stop()
	{
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new MowerDriverStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		wheelDiameter = new DistanceMeters(0.2);
	}

	@Override
	protected void createPorts()
	{
		wheelsControlReceiver = new WheelsControlReceiver(this, 0L, "WheelsControlReceiver");
	}
}
