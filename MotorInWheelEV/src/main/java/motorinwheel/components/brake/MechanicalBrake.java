package motorinwheel.components.brake;

import java.util.Optional;

import motorinwheel.common.ports.energy.BrakeSuspensionMountForceTransmitPort;
import motorinwheel.common.ports.energy.BrakeTorqueTransmitPort;
import motorinwheel.common.ports.energy.HydraulicForceReceivePort;
import motorinwheel.systems.motorinwheel.MotorInWheelSystem;
import sysmlinjava.annotations.SysMLComment;
import sysmlinjava.attributetypes.AreaMetersSquare;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.attributetypes.ForceNewtonsPerMeterSquare;
import sysmlinjava.attributetypes.Percent;
import sysmlinjava.attributetypes.RReal;
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
 * Vehicle component that provides the mechanical brake part of the
 * motor-in-wheel system. Function is to decelerate the vehicle by imposing a
 * negative torque on the wheel. It includes interfaces (ports) for the
 * hydraulic line for hydraulic pressure in, for the brake disc for mechanical
 * braking torque out, and for a suspension mount for the brake's weight out. *
 * 
 * @author ModelerOne
 */
public class MechanicalBrake extends SysMLPart
{
	/**
	 * Port for the hydraulic line
	 */
	@Port
	public HydraulicForceReceivePort hydraulicLine;
	/**
	 * Port for the brake's disk
	 */
	@Port
	public BrakeTorqueTransmitPort wheelBrakeDisc;
	/**
	 * Port for the brake's suspension
	 */
	@Port
	public BrakeSuspensionMountForceTransmitPort suspensionMount;

	/**
	 * Flow of hydraulic pressure in
	 */
	@Attribute
	public ForceNewtonsPerMeterSquare hydraulicPressureIn;
	/**
	 * Flow of the mechanical torque out
	 */
	@Attribute
	public TorqueNewtonMeters mechanicalTorqueOut;
	/**
	 * Flow of the brake's weight out
	 */
	@Attribute
	public ForceNewtons weightOnSuspensionOut;

	/**
	 * Value for the brake's efficiency
	 */
	@BOMLineItemValue
	@Attribute
	public Percent brakeEfficiency;
	/**
	 * Value of the brake's pad area
	 */
	@BOMLineItemValue
	@Attribute
	public AreaMetersSquare breakPadArea;
	/**
	 * Value of the brake pad's coefficient of friction
	 */
	@BOMLineItemValue
	@Attribute
	public RReal coefficientOfFriction;
	/**
	 * Value of the brake disc's mean radius
	 */
	@BOMLineItemValue
	@Attribute
	public DistanceMeters wheelBrakeDiskMeanRadius;
	/**
	 * Value of max brake weight
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
	 * @param motorInWheelSystem the motor-in-wheel in which this brake is mounted
	 *                           and operates
	 * @param name               unique name
	 * @param id                 unique ID
	 */
	public MechanicalBrake(MotorInWheelSystem motorInWheelSystem, String name, Long id)
	{
		super(Optional.of(motorInWheelSystem), name, id);
	}

	/**
	 * Operation to transmit the brakes weight to the suspension
	 */
	@Action
	public void transmitWeight()
	{
		suspensionMount.transmit(weightOnSuspensionOut);
	}

	/**
	 * Reaction to a change in hydraulic pressure in the brake's hydraulic line
	 * 
	 * @param pressure value of the pressure
	 */
	@Action
	public void onHydraulicPressureChange(ForceNewtonsPerMeterSquare pressure)
	{
		hydraulicPressureIn = pressure;
		calculateTorque();
		wheelBrakeDisc.transmit(mechanicalTorqueOut);
	}

	/**
	 * Calculation of the brake's mechanical torque from the flows and values
	 */
	@Calculation
	private void calculateTorque()
	{
		mechanicalTorqueOut.value = coefficientOfFriction.value * hydraulicPressureIn.value * breakPadArea.value * wheelBrakeDiskMeanRadius.value;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new MechanicalBrakeStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		brakeEfficiency = new Percent(90);
		wheelBrakeDiskMeanRadius = new DistanceMeters(0.07);
		breakPadArea = new AreaMetersSquare(2 * 0.016);
		coefficientOfFriction = new RReal(0.35);
		maxWeight = new ForceNewtons(50);
		weightOnSuspensionOut = new ForceNewtons(50, Math.toRadians(180));
		hydraulicPressureIn = new ForceNewtonsPerMeterSquare(0);
		mechanicalTorqueOut = new TorqueNewtonMeters(0);
	}

	@Override
	protected void createPorts()
	{
		hydraulicLine = new HydraulicForceReceivePort(this, this, 0L);
		wheelBrakeDisc = new BrakeTorqueTransmitPort(this, 0L);
		suspensionMount = new BrakeSuspensionMountForceTransmitPort(this, 0L);
	}

	@Override
	protected void createComments()
	{
		bomComment = new SysMLComment("Description:Brake component of motor-in-wheel component\nSource:Competitive sourcing TBD");
	}
}
