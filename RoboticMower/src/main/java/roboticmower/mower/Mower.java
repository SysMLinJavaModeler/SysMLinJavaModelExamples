package roboticmower.mower;

import sysmlinjava.attributetypes.Cost$US;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.parts.SysMLPart;

/**
 * The RoboticMower mower system. The mower consists of a blade (cutting) subsystem,
 * a drive subsystem, and a control subsystem. For this first version of the
 * SysMLinJava model, only the position/navigation related subsystems are
 * modeled.
 * <p>
 * The controller part interacts with the position controller to determine the
 * mower's velocity over time. THe controller uses this controlled velocity to
 * control the drive subsystem's wheels' speed and direction, thereby
 * controlling the mower's movement.
 * <p>
 * The RoboticMower mower system block also performs the connections of it parts.
 * Connectors include those between the mower's controller and the position
 * controller as well as between the mower controller and the drive subsystem.
 * 
 * @author ModelerOne
 *
 */
public class Mower extends SysMLPart
{
//	/**
//	 * Part for the mower's cutting (blade) components
//	 */
//	@Part
//	BladeSubssytem blade;
	/**
	 * Part for the mower's drive (wheels, etc.)
	 */
	@Part
	public	MowerDriver drive;
	/**
	 * Part for the mower's controller
	 */
	@Part
	public	MowerController controller;

	/**
	 * Value for the maximum cost of the system, as a constraint to the design
	 */
	@Attribute
	public Cost$US maxCost;

	/**
	 * Connector thatperforms the function that makes the connection of the
	 * controller to the drive subsystem
	 */
	@FlowConnector
	SysMLFlowConnector contollerToDriveConnector;

	/**
	 * Constructor, which invokes all the creation methods that
	 * instantiate/initialize all parts, connectors, and values
	 */
	public Mower()
	{
		super("Mower", 0L);
	}

	@Override
	public void start()
	{
		drive.start();
		controller.start();
		// blade.start();
	}

	@Override
	public void stop()
	{
		// blade.stop();
		controller.stop();
		drive.stop();
	}

	@Override
	protected void createAttributes()
	{
		maxCost = new Cost$US(800.00);
	}

	@Override
	protected void createParts()
	{
		// blade = new BladeSubsystem(this);
		drive = new MowerDriver(this);
		controller = new MowerController(this);
	}

	@Override
	protected void createFlowConnectors()
	{
		contollerToDriveConnector = new SysMLFlowConnector(TypesEnum.peertopeer, false, controller.wheelsControlTransmitter, drive.wheelsControlReceiver, "", 0L);
	}
}
