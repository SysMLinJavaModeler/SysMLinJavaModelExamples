package motorinwheel.components.deceleration;

import java.util.Optional;
import motorinwheel.common.ports.energy.BrakePedalForceReceivePort;
import motorinwheel.common.ports.energy.HydraulicForceTransmitPort;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.kinds.SysMLFlowDirectionKind;
import sysmlinjava.valuetypes.AreaMetersSquare;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.ForceNewtonsPerMeterSquare;
import sysmlinjava.valuetypes.RReal;

/**
 * Vehicle component provides the operator with deceleration (braking) control.
 * Function is to translate operator's force on pedal to amount of hydraulic
 * pressure to be delivered to the brakes in the motor-in-wheels systems.
 * 
 * @author ModelerOne
 *
 */
public class Decelerator extends SysMLBlock
{
	/**
	 * Port for the receipt of force on the brake pedal
	 */
	@FullPort
	public BrakePedalForceReceivePort brakePedal;
	/**
	 * Port for the transmission of hydraulic pressure force on a brake
	 */
	@FullPort
	public HydraulicForceTransmitPort brakeLeftFront;
	/**
	 * Port for the transmission of hydraulic pressure force on a brake
	 */
	@FullPort
	public HydraulicForceTransmitPort brakeRightFront;
	/**
	 * Port for the transmission of hydraulic pressure force on a brake
	 */
	@FullPort
	public HydraulicForceTransmitPort brakeLeftRear;
	/**
	 * Port for the transmission of hydraulic pressure force on a brake
	 */
	@FullPort
	public HydraulicForceTransmitPort brakeRightRear;

	/**
	 * Flow value for the input brake pedal force
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public ForceNewtons brakePedalForceIn;
	/**
	 * Flow value for the output hydraulic pressure force
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ForceNewtonsPerMeterSquare brakeHydraulicForceOut;

	/**
	 * Value of the wheels diameter
	 */
	@Value
	public DistanceMeters wheelDiameter;
	/**
	 * Value of the area of the bore of the brake's hydraulic actuator
	 */
	@Value
	public AreaMetersSquare brakeActuatorBoreArea;
	/**
	 * Value of the brake pedals mechanical advantage in imposing a force on the
	 * hydraulic actuator
	 */
	@Value
	public RReal brakePedalMechanicalAdvantage;

	/**
	 * Constraint for the calculation of the hydraulic pressure force on the brakes
	 * for the given force on the brake pedal
	 */
	@Constraint
	public SysMLConstraint brakeHydraulicForceCalculation;

	public Decelerator(SysMLBlock contextBlock, String name, Long id)
	{
		super(contextBlock, name, id);
	}

	public void onBrakePedal(ForceNewtons brakePedalForce)
	{
		logger.info(brakePedalForce.toString());
		brakePedalForceIn.value = brakePedalForce.value;
		brakeHydraulicForceCalculation.apply();
		brakeLeftFront.transmit(brakeHydraulicForceOut);
		brakeLeftRear.transmit(brakeHydraulicForceOut);
		brakeRightFront.transmit(brakeHydraulicForceOut);
		brakeRightRear.transmit(brakeHydraulicForceOut);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new DeceleratorStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		wheelDiameter = new DistanceMeters(24 / 39.37007874);
		brakeActuatorBoreArea = new AreaMetersSquare(0.0005);
		brakePedalMechanicalAdvantage = new RReal(1.5);
	}

	@Override
	protected void createFlows()
	{
		brakeHydraulicForceOut = new ForceNewtonsPerMeterSquare(0);
		brakePedalForceIn = new ForceNewtons(0);
	}

	@Override
	protected void createFullPorts()
	{
		brakePedal = new BrakePedalForceReceivePort(this, this, 0L);
		brakeLeftFront = new HydraulicForceTransmitPort(this, (long)WheelLocationEnum.leftFront.ordinal);
		brakeLeftRear = new HydraulicForceTransmitPort(this, (long)WheelLocationEnum.rightFront.ordinal);
		brakeRightFront = new HydraulicForceTransmitPort(this, (long)WheelLocationEnum.leftRear.ordinal);
		brakeRightRear = new HydraulicForceTransmitPort(this, (long)WheelLocationEnum.rightRear.ordinal);
	}

	@Override
	protected void createConstraints()
	{
		brakeHydraulicForceCalculation = () ->
		{
			brakeHydraulicForceOut.value = (brakePedalForceIn.value * brakePedalMechanicalAdvantage.value) / brakeActuatorBoreArea.value;
		};
	}
}
