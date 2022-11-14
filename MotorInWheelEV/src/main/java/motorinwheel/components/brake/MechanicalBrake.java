package motorinwheel.components.brake;

import motorinwheel.common.ports.energy.HydraulicForceReceivePort;
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
import sysmlinjava.valuetypes.AreaMetersSquare;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.ForceNewtonsPerMeterSquare;
import sysmlinjava.valuetypes.Percent;
import sysmlinjava.valuetypes.RReal;
import sysmlinjava.valuetypes.TorqueNewtonMeters;
import java.util.Optional;
import motorinwheel.common.ports.energy.BrakeSuspensionMountForceTransmitPort;
import motorinwheel.common.ports.energy.BrakeTorqueTransmitPort;

/**
 * Vehicle component that provides the mechanical brake part of the
 * motor-in-wheel system. Function is to decelerate the vehicle by imposing a
 * negative torque on the wheel. It includes interfaces (ports) for the
 * hydraulic line for hydraulic pressure in, for the brake disc for mechanical
 * braking torque out, and for a suspension mount for the brake's weight out. *
 * 
 * @author ModelerOne
 *
 */
public class MechanicalBrake extends SysMLBlock
{
	/**
	 * Port for the hydraulic line
	 */
	@FullPort
	public HydraulicForceReceivePort hydraulicLine;
	/**
	 * Port for the brake's disk
	 */
	@FullPort
	public BrakeTorqueTransmitPort wheelBrakeDisc;
	/**
	 * Port for the brake's suspension
	 */
	@FullPort
	public BrakeSuspensionMountForceTransmitPort suspensionMount;

	/**
	 * Flow of hydraulic pressure in
	 */
	@Flow
	public ForceNewtonsPerMeterSquare hydraulicPressureIn;
	/**
	 * Flow of the mechanical torque out
	 */
	@Flow
	public TorqueNewtonMeters mechanicalTorqueOut;
	/**
	 * Flow of the brake's weight out
	 */
	@Flow
	public ForceNewtons weightOnSuspensionOut;

	/**
	 * Value for the brake's efficiency
	 */
	@BOMLineItemValue
	@Value
	public Percent brakeEfficiency;
	/**
	 * Value of the brake's pad area
	 */
	@BOMLineItemValue
	@Value
	public AreaMetersSquare breakPadArea;
	/**
	 * Value of the brake pad's coefficient of friction
	 */
	@BOMLineItemValue
	@Value
	public RReal coefficientOfFriction;
	/**
	 * Value of the brake disc's mean radius
	 */
	@BOMLineItemValue
	@Value
	public DistanceMeters wheelBrakeDiskMeanRadius;
	/**
	 * Value of max brake weight
	 */
	@BOMLineItemValue
	@Value
	public ForceNewtons maxWeight;

	/**
	 * Constraint to calculate the brake's mechanical torque from the flows and
	 * values
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
	 * @param motorInWheelSystem the motor-in-wheel in which this brake is mounted
	 *                           and operates
	 * @param name               unique name
	 * @param id                 unique ID
	 */
	public MechanicalBrake(MotorInWheelSystem motorInWheelSystem, String name, Long id)
	{
		super(motorInWheelSystem, name, id);
	}

	/**
	 * Operation to transmit the brakes weight to the suspension
	 */
	@Operation
	public void transmitWeight()
	{
		suspensionMount.transmit(weightOnSuspensionOut);
	}

	/**
	 * Reception for the reaction to a change in hydraulic pressure in the brake's
	 * hydraulic line
	 * 
	 * @param pressure value of the pressure
	 */
	@Reception
	public void onHydraulicPressureChange(ForceNewtonsPerMeterSquare pressure)
	{
		logger.info(pressure.toString());
		hydraulicPressureIn = pressure;
		mechanicalTorqueCalculation.apply();
		wheelBrakeDisc.transmit(mechanicalTorqueOut);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new MechanicalBrakeStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		brakeEfficiency = new Percent(90);
		wheelBrakeDiskMeanRadius = new DistanceMeters(0.07);
		breakPadArea = new AreaMetersSquare(2 * 0.016);
		coefficientOfFriction = new RReal(0.35);
		maxWeight = new ForceNewtons(50);
	}

	@Override
	protected void createFlows()
	{
		weightOnSuspensionOut = new ForceNewtons(50, Math.toRadians(180));
		hydraulicPressureIn = new ForceNewtonsPerMeterSquare(0);
		mechanicalTorqueOut = new TorqueNewtonMeters(0);
	}

	@Override
	protected void createFullPorts()
	{
		hydraulicLine = new HydraulicForceReceivePort(this, this, 0L);
		wheelBrakeDisc = new BrakeTorqueTransmitPort(this, 0L);
		suspensionMount = new BrakeSuspensionMountForceTransmitPort(this, 0L);
	}

	@Override
	protected void createConstraints()
	{
		mechanicalTorqueCalculation = () ->
		{
			mechanicalTorqueOut.value = coefficientOfFriction.value * hydraulicPressureIn.value * breakPadArea.value * wheelBrakeDiskMeanRadius.value;
		};
	}

	@Override
	protected void createComments()
	{
		bomComment = new SysMLComment("Description:Brake component of motor-in-wheel component\nSource:Competitive sourcing TBD");
	}
}
