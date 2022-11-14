package motorinwheel.components.motor;

import java.util.Optional;
import motorinwheel.common.ports.energy.ElectricalPowerReceivePort;
import motorinwheel.common.ports.energy.MechanicalForceTransmitPort;
import motorinwheel.common.ports.energy.MotorTorqueTransmitPort;
import motorinwheel.systems.motorinwheel.MotorInWheelSystem;
import sysmlinjava.analysis.bom.annotations.BOMLineItemComment;
import sysmlinjava.analysis.bom.annotations.BOMLineItemValue;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.comments.Comment;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLComment;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.Percent;
import sysmlinjava.valuetypes.PowerWatts;
import sysmlinjava.valuetypes.RevolutionsPerMinute;
import sysmlinjava.valuetypes.TorqueNewtonMeters;

/**
 * Vehicle component for the electric motor part of the Motor-In-Wheel system.
 * Primary function is to provide torque to the wheel using received electrical
 * power. It has ports/interfaces with the vehicles electrical power supply, the
 * wheel, and the suspension on which it is mounted.
 * 
 * @author ModelerOne
 *
 */
public class ElectricMotor extends SysMLBlock
{
	/**
	 * Port for the receipt of electrical power from the power supply
	 */
	@FullPort
	public ElectricalPowerReceivePort electricalLine;
	/**
	 * Port for the transmission of torque to the wheel
	 */
	@FullPort
	public MotorTorqueTransmitPort wheelMotorDisc;
	/**
	 * Port for the transmission of the motor's weight to the suspension
	 */
	@FullPort
	public MechanicalForceTransmitPort suspensionMount;

	/**
	 * Flow of electrical power into the motor
	 */
	@Flow
	public PowerWatts electricalPowerIn;
	/**
	 * Flow of mechanical torque out to the wheel
	 */
	@Flow
	public TorqueNewtonMeters mechanicalTorqueOut;
	/**
	 * Flow of motor weight out to the suspension
	 */
	@Flow
	public ForceNewtons weightOnSuspensionOut;

	/**
	 * Value of current torque output per power input
	 */
	@Value
	public TorqueNewtonMetersPerKilowatt torqueNmPerKw;
	/**
	 * Value of current RPM of the motor
	 */
	@Value
	public RevolutionsPerMinute revolutionsPerMinute;
	/**
	 * Value of minimum motor efficiency
	 */
	@BOMLineItemValue
	@Value
	public Percent minMotorEfficiency;
	/**
	 * Value of minimum RPM of the motor
	 */
	@BOMLineItemValue
	@Value
	public RevolutionsPerMinute minRevolutionsPerMinute;
	/**
	 * Value of motor efficiency
	 */
	@BOMLineItemValue
	@Value
	public Percent motorEfficiency;
	/**
	 * Value of min current torque output per power input
	 */
	@BOMLineItemValue
	@Value
	public TorqueNewtonMetersPerKilowatt minTorqueNmPerKw;
	/**
	 * Value of max motor weight
	 */
	@BOMLineItemValue
	@Value
	public ForceNewtons maxWeight;

	/**
	 * Constraint to calculate the mechanical torque of the motor for the specified
	 * input power and motor efficiency
	 */
	@Constraint
	public SysMLConstraint mechanicalTorqueCalculation;

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
		super(motorInWheelSystem, name, id);
	}

	/**
	 * Operation to transmit the weight of the motor onto the suspension
	 */
	@Operation
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
	@Reception
	public void onElectricalPower(PowerWatts powerWatts)
	{
		logger.info(powerWatts.toString());
		electricalPowerIn.setValue(powerWatts.value);
		mechanicalTorqueCalculation.apply();
		wheelMotorDisc.transmit(mechanicalTorqueOut);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new ElectricMotorStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		torqueNmPerKw = new TorqueNewtonMetersPerKilowatt(5);
		revolutionsPerMinute = new RevolutionsPerMinute(240);
		motorEfficiency = new Percent(95);
		maxWeight = new ForceNewtons(25);
		minTorqueNmPerKw = new TorqueNewtonMetersPerKilowatt(5);
		minRevolutionsPerMinute = new RevolutionsPerMinute(240);
		minMotorEfficiency = new Percent(95);
	}

	@Override
	protected void createFlows()
	{
		electricalPowerIn = new PowerWatts(0);
		mechanicalTorqueOut = new TorqueNewtonMeters(0);
		weightOnSuspensionOut = new ForceNewtons(25, Math.toRadians(180));
	}

	@Override
	protected void createFullPorts()
	{
		electricalLine = new ElectricalPowerReceivePort(this, this, 0L);
		wheelMotorDisc = new MotorTorqueTransmitPort(this, 0L);
		suspensionMount = new MechanicalForceTransmitPort(this, 0L);
	}

	@Override
	protected void createConstraints()
	{
		mechanicalTorqueCalculation = () ->
		{
			mechanicalTorqueOut.value = torqueNmPerKw.value * (electricalPowerIn.value / 1000) * motorEfficiency.asFraction(); // (60 / (2 * Math.PI)) * (electricalPowerIn.value / revolutionsPerMinute.value)
																																 // * (motorEfficiency.value / 100);
		};
	}

	@Override
	protected void createComments()
	{
		bomComment = new SysMLComment("Description:Motor component of motor-in-wheel component\nSource:Competitive sourcing TBD");
	}
}
