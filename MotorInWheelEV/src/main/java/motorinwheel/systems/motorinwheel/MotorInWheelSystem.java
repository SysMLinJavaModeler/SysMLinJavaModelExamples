package motorinwheel.systems.motorinwheel;

import motorinwheel.components.brake.MechanicalBrake;
import motorinwheel.components.motor.ElectricMotor;
import motorinwheel.components.wheel.Wheel;
import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.analysis.bom.annotations.BOMLineItem;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Part;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;

/**
 * The motor-in-wheel system - the focus of the modeling and simulation of the
 * MotorInWheelEV. Its primary function is to provide vehicle support/lift as
 * well as forces on the roadway that enable vehicle acceleration and
 * deceleration. It consists of three parts: <ul>
 * <li>Wheel that supports and moves the vehicle</li>
 * <li>Electric motor that provides accelerating torque to the wheel</li>
 * <li>Mechanical brake that provides braking torque to the wheel</li> </ul>
 * 
 * @author ModelerOne
 *
 */
public class MotorInWheelSystem extends SysMLBlock
{
	/**
	 * Part for the wheel
	 */
	@BOMLineItem
	@Part
	public Wheel wheel;
	/**
	 * Part for the electric motor that provides acceleration torque
	 */
	@BOMLineItem
	@Part
	public ElectricMotor motor;
	/**
	 * Part for the mechanical brake that provides deceleration torque
	 */
	@BOMLineItem
	@Part
	public MechanicalBrake brake;

	/**
	 * Function that makes the connection between the motor and its wheel
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction motorToWheelConnectorFunction;
	/**
	 * Function that makes the connection between the brake and its wheel
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction brakeToWheelConnectorFunction;

	/**
	 * Connector that invokes the function that makes the connection between the
	 * motor and its wheel
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector motorToWheelConnector;
	/**
	 * Connector that invokes the function that makes the connection between the
	 * brake and its wheel
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector brakeToWheelConnector;

	/**
	 * Constructor
	 * 
	 * @param vehicle       vehicle of which the motor-in-wheel is a part
	 * @param name          unique name
	 * @param wheelLocation location on the vehicle of this motor-in-wheel
	 */
	public MotorInWheelSystem(Vehicle vehicle, String name, WheelLocationEnum wheelLocation)
	{
		super(vehicle, name, (long)wheelLocation.ordinal);
		wheel.wheelLocation = wheelLocation;
		setWheelLocation(wheelLocation.ordinal);
	}

	/**
	 * Sets the ids of all the ports in this system instance
	 * 
	 * @param id unique id for this system
	 */
	public void setWheelLocation(Integer id)
	{
		wheel.pulseTransmissionLine.id = (long)id;
		wheel.roadwaySurface.id = (long)id;
		wheel.suspensionMount.id = (long)id;
		wheel.tireSurface.id = (long)id;
		wheel.wheelMotorDisc.id = (long)id;
		brake.hydraulicLine.id = (long)id;
		brake.suspensionMount.id = (long)id;
		brake.wheelBrakeDisc.id = (long)id;
		motor.electricalLine.id = (long)id;
		motor.suspensionMount.id = (long)id;
		motor.wheelMotorDisc.id = (long)id;
	}

	@Override
	public void start()
	{
		wheel.start();
		motor.start();
		brake.start();
	}

	@Override
	public void stop()
	{
		wheel.stop();
		motor.stop();
		brake.stop();
	}

	@Override
	protected void createParts()
	{
		brake = new MechanicalBrake(this, "Brake", this.id);
		motor = new ElectricMotor(this, "Motor", this.id);
		wheel = new Wheel(this, "Wheel", this.id);
	}

	@Override
	protected void createConnectorFunctions()
	{
		motorToWheelConnectorFunction = () ->
		{
			motor.wheelMotorDisc.addConnectedPortPeer(wheel.wheelMotorDisc);
		};
		brakeToWheelConnectorFunction = () ->
		{
			brake.wheelBrakeDisc.addConnectedPortPeer(wheel.wheelBrakeDisc);
		};
	}

	@Override
	protected void createConnectors()
	{
		motorToWheelConnector = new SysMLAssociationBlockConnector(motor, wheel, motorToWheelConnectorFunction);
		brakeToWheelConnector = new SysMLAssociationBlockConnector(brake, wheel, brakeToWheelConnectorFunction);
	}
}
