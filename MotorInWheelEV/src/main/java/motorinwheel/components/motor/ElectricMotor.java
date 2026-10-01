package motorinwheel.components.motor;

import java.util.Optional;

import motorinwheel.common.ports.energy.ElectricalPowerReceivePort;
import motorinwheel.common.ports.energy.MechanicalForceTransmitPort;
import motorinwheel.common.ports.energy.MotorTorqueTransmitPort;
import motorinwheel.systems.motorinwheel.MotorInWheelSystem;
import sysmlinjava.annotations.SysMLComment;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.attributetypes.Percent;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.attributetypes.RevolutionsPerMinute;
import sysmlinjava.attributetypes.TorqueNewtonMeters;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.annotations.Comment;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.views.bom.annotations.BOMLineItemComment;
import sysmlinjava.views.bom.annotations.BOMLineItemValue;

/**
 * Vehicle component for the electric motor part of the Motor-In-Wheel system.
 * Primary function is to provide torque to the wheel using received electrical
 * power. It has ports/interfaces with the vehicles electrical power supply, the
 * wheel, and the suspension on which it is mounted.
 * 
 * @author ModelerOne
 */
public class ElectricMotor extends SysMLPart
{
	/**
	 * Port for the receipt of electrical power from the power supply
	 */
	@Port
	public ElectricalPowerReceivePort electricalLine;
	/**
	 * Port for the transmission of torque to the wheel
	 */
	@Port
	public MotorTorqueTransmitPort wheelMotorDisc;
	/**
	 * Port for the transmission of the motor's weight to the suspension
	 */
	@Port
	public MechanicalForceTransmitPort suspensionMount;

	/**
	 * Attribute for electrical power into the motor
	 */
	@Attribute
	public PowerWatts electricalPowerIn;
	/**
	 * Attribute for mechanical torque out to the wheel
	 */
	@Attribute
	public TorqueNewtonMeters mechanicalTorqueOut;
	/**
	 * Attribute for motor weight out to the suspension
	 */
	@Attribute
	public ForceNewtons weightOnSuspensionOut;

	/**
	 * Value of current torque output per power input
	 */
	@Attribute
	public TorqueNewtonMetersPerKilowatt torqueNmPerKw;
	/**
	 * Value of current RPM of the motor
	 */
	@Attribute
	public RevolutionsPerMinute revolutionsPerMinute;
	/**
	 * Value of minimum motor efficiency
	 */
	@BOMLineItemValue
	@Attribute
	public Percent minMotorEfficiency;
	/**
	 * Value of minimum RPM of the motor
	 */
	@BOMLineItemValue
	@Attribute
	public RevolutionsPerMinute minRevolutionsPerMinute;
	/**
	 * Value of motor efficiency
	 */
	@BOMLineItemValue
	@Attribute
	public Percent motorEfficiency;
	/**
	 * Value of min current torque output per power input
	 */
	@BOMLineItemValue
	@Attribute
	public TorqueNewtonMetersPerKilowatt minTorqueNmPerKw;
	/**
	 * Value of max motor weight
	 */
	@BOMLineItemValue
	@Attribute
	public ForceNewtons maxWeight;

	/**
	 * Comment used in bill-of-materials
	 */
	@BOMLineItemComment
	@Comment
	public SysMLComment bomComment;

	/**
	 * Constructor
	 * 
	 * @param motorInWheelSystem motor-in-wheel system of which the motor is a part
	 * @param name               unique name
	 * @param id                 unique ID
	 */
	public ElectricMotor(MotorInWheelSystem motorInWheelSystem, String name, Long id)
	{
		super(Optional.of(motorInWheelSystem), name, id);
	}

	/**
	 * Operation to transmit the weight of the motor onto the suspension
	 */
	@Action
	public void transmitWeight()
	{
		suspensionMount.transmit(weightOnSuspensionOut);
	}

	/**
	 * Reception to react to the receipt of specified power via the electric line
	 * port, i.e. to change the torque output to the wheel.
	 * 
	 * @param powerWatts power received
	 */
	@Action
	public void onElectricalPower(PowerWatts powerWatts)
	{
		electricalPowerIn.setValue(powerWatts.value);
		calculateTorqueOut();
		wheelMotorDisc.transmit(mechanicalTorqueOut);
	}

	/**
	 * Calculation of the mechanical torque of the motor for the specified input
	 * power and motor efficiency
	 */
	@Calculation
	private void calculateTorqueOut()
	{
		mechanicalTorqueOut.value = torqueNmPerKw.value * (electricalPowerIn.value / 1000) * motorEfficiency.asFraction(); // (60 / (2 * Math.PI)) * (electricalPowerIn.value /
																																									// revolutionsPerMinute.value)
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new ElectricMotorStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		torqueNmPerKw = new TorqueNewtonMetersPerKilowatt(5);
		revolutionsPerMinute = new RevolutionsPerMinute(240);
		motorEfficiency = new Percent(95);
		maxWeight = new ForceNewtons(25);
		minTorqueNmPerKw = new TorqueNewtonMetersPerKilowatt(5);
		minRevolutionsPerMinute = new RevolutionsPerMinute(240);
		minMotorEfficiency = new Percent(95);
		electricalPowerIn = new PowerWatts(0, name.get(), 0L);
		mechanicalTorqueOut = new TorqueNewtonMeters(0);
		weightOnSuspensionOut = new ForceNewtons(25, Math.toRadians(180));
	}

	@Override
	protected void createPorts()
	{
		electricalLine = new ElectricalPowerReceivePort(this, this, 0L);
		wheelMotorDisc = new MotorTorqueTransmitPort(this, 0L);
		suspensionMount = new MechanicalForceTransmitPort(this, 0L);
	}

	@Override
	protected void createComments()
	{
		bomComment = new SysMLComment("Description:Motor component of motor-in-wheel component\nSource:Competitive sourcing TBD");
	}
}
