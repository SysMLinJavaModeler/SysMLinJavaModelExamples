package motorinwheel.components.deceleration;

import java.util.Optional;

import motorinwheel.common.ports.energy.BrakePedalForceReceivePort;
import motorinwheel.common.ports.energy.HydraulicForceTransmitPort;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.attributetypes.AreaMetersSquare;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.attributetypes.ForceNewtonsPerMeterSquare;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * Vehicle component provides the operator with deceleration (braking) control.
 * Function is to translate operator's force on pedal to amount of hydraulic
 * pressure to be delivered to the brakes in the motor-in-wheels systems.
 * 
 * @author ModelerOne
 */
public class Decelerator extends SysMLPart
{
	/**
	 * Port for the receipt of force on the brake pedal
	 */
	@Port
	public BrakePedalForceReceivePort brakePedal;
	/**
	 * Port for the transmission of hydraulic pressure force on a brake
	 */
	@Port
	public HydraulicForceTransmitPort brakeLeftFront;
	/**
	 * Port for the transmission of hydraulic pressure force on a brake
	 */
	@Port
	public HydraulicForceTransmitPort brakeRightFront;
	/**
	 * Port for the transmission of hydraulic pressure force on a brake
	 */
	@Port
	public HydraulicForceTransmitPort brakeLeftRear;
	/**
	 * Port for the transmission of hydraulic pressure force on a brake
	 */
	@Port
	public HydraulicForceTransmitPort brakeRightRear;

	/**
	 * Flow value for the input brake pedal force
	 */
	@Attribute
	public ForceNewtons brakePedalForceIn;
	/**
	 * Flow value for the output hydraulic pressure force
	 */
	@Attribute
	public ForceNewtonsPerMeterSquare brakeHydraulicForceOut;

	/**
	 * Value of the wheels diameter
	 */
	@Attribute
	public DistanceMeters wheelDiameter;
	/**
	 * Value of the area of the bore of the brake's hydraulic actuator
	 */
	@Attribute
	public AreaMetersSquare brakeActuatorBoreArea;
	/**
	 * Value of the brake pedals mechanical advantage in imposing a force on the
	 * hydraulic actuator
	 */
	@Attribute
	public RReal brakePedalMechanicalAdvantage;

	public Decelerator(SysMLPart contextPart, String name, Long id)
	{
		super(Optional.of(contextPart), name, id);
	}

	public void onBrakePedal(ForceNewtons brakePedalForce)
	{
		brakePedalForceIn.value = brakePedalForce.value;
		calculateBrakeHydraulicForce();
		brakeLeftFront.transmit(brakeHydraulicForceOut);
		brakeLeftRear.transmit(brakeHydraulicForceOut);
		brakeRightFront.transmit(brakeHydraulicForceOut);
		brakeRightRear.transmit(brakeHydraulicForceOut);
	}

	/**
	 * Calculation of the hydraulic pressure force on the brakes for the given force
	 * on the brake pedal
	 */
	@Calculation
	private void calculateBrakeHydraulicForce()
	{
		brakeHydraulicForceOut.value = (brakePedalForceIn.value * brakePedalMechanicalAdvantage.value) / brakeActuatorBoreArea.value;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new DeceleratorStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		wheelDiameter = new DistanceMeters(24 / 39.37007874);
		brakeActuatorBoreArea = new AreaMetersSquare(0.0005);
		brakePedalMechanicalAdvantage = new RReal(1.5);
		brakeHydraulicForceOut = new ForceNewtonsPerMeterSquare(0);
		brakePedalForceIn = new ForceNewtons(0);
	}

	@Override
	protected void createPorts()
	{
		brakePedal = new BrakePedalForceReceivePort(this, this, 0L);
		brakeLeftFront = new HydraulicForceTransmitPort(this, (long) WheelLocationEnum.leftFront.ordinal());
		brakeLeftRear = new HydraulicForceTransmitPort(this, (long) WheelLocationEnum.rightFront.ordinal());
		brakeRightFront = new HydraulicForceTransmitPort(this, (long) WheelLocationEnum.leftRear.ordinal());
		brakeRightRear = new HydraulicForceTransmitPort(this, (long) WheelLocationEnum.rightRear.ordinal());
	}
}
