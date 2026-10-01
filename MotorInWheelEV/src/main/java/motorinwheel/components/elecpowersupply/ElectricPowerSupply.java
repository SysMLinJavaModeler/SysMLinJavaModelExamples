package motorinwheel.components.elecpowersupply;

import java.util.Optional;

import motorinwheel.common.ports.energy.ElectricalPowerTransmitPort;
import motorinwheel.common.ports.energy.MechanicalForceReceivePort;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * Vehicle component that provides the electrical power to all of the motors in
 * the motor-in-wheels. Function is to translate operator's force on a "switch"
 * into electrical power to be delivered to the motors-in-wheels.
 * 
 * @author ModelerOne
 */
public class ElectricPowerSupply extends SysMLPart
{
	/**
	 * Port to receive control of the power supply from the accelerator
	 */
	@Port
	public MechanicalForceReceivePort control;
	/**
	 * Port to transmit electric power to one of the motors in a wheel
	 */
	@Port
	public ElectricalPowerTransmitPort motorPowerLineLeftFront;
	/**
	 * Port to transmit electric power to one of the motors in a wheel
	 */
	@Port
	public ElectricalPowerTransmitPort motorPowerLineRightFront;
	/**
	 * Port to transmit electric power to one of the motors in a wheel
	 */
	@Port
	public ElectricalPowerTransmitPort motorPowerLineLeftRear;
	/**
	 * Port to transmit electric power to one of the motors in a wheel
	 */
	@Port
	public ElectricalPowerTransmitPort motorPowerLineRightRear;

	/**
	 * Flow value of the force on the power supply "switch"
	 */
	@Attribute
	public ForceNewtons controlIn;
	/**
	 * Flow value of the electric power that is sent to the motor in a wheel
	 */
	@Attribute
	public PowerWatts powerLeftFrontOut;
	/**
	 * Flow value of the electric power that is sent to the motor in a wheel
	 */
	@Attribute
	public PowerWatts powerRightFrontOut;
	/**
	 * Flow value of the electric power that is sent to the motor in a wheel
	 */
	@Attribute
	public PowerWatts powerLeftRearOut;
	/**
	 * Flow value of the electric power that is sent to the motor in a wheel
	 */
	@Attribute
	public PowerWatts powerRightRearOut;
	/**
	 * Value of the ratio of control force to electric power out
	 */
	@Attribute
	public RReal controlForceToPowerOutRatio;
	/**
	 * Value of the number of motors powered by the power supply
	 */
	@Attribute
	public IInteger numberMotors;

	/**
	 * Constructor
	 * 
	 * @param vehicle vehicle in which the power supply resides
	 * @param name    unique name
	 * @param id      unique ID
	 */
	public ElectricPowerSupply(Vehicle vehicle, String name, Long id)
	{
		super(Optional.of(vehicle), name, id);
	}

	/**
	 * Reception that reacts to the receipt of a new control input force
	 * 
	 * @param control force on the "on" control switch/lever
	 */
	@Action
	public void onControl(ForceNewtons control)
	{
		controlIn.value = control.value;
		calculatePowerOuts();
		motorPowerLineLeftFront.transmit(powerLeftFrontOut);
		motorPowerLineRightFront.transmit(powerRightFrontOut);
		motorPowerLineLeftRear.transmit(powerLeftRearOut);
		motorPowerLineRightRear.transmit(powerRightRearOut);
	}

	/**
	 * Calculation of the power to send out to each motor in a wheel. Note the
	 * current constraint assumes each wheel gets the same power. More advanced
	 * model could make this power a function of steering angle, tire slip, etc.
	 */
	@Calculation
	private void calculatePowerOuts()
	{
		double powerOutPerMotor = controlIn.value * controlForceToPowerOutRatio.value / numberMotors.value;
		powerLeftFrontOut.value = powerOutPerMotor;
		powerRightFrontOut.value = powerOutPerMotor;
		powerLeftRearOut.value = powerOutPerMotor;
		powerRightRearOut.value = powerOutPerMotor;
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new ElectricPowerSupplyStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		controlForceToPowerOutRatio = new RReal(1500);
		numberMotors = new IInteger(4);
		controlIn = new ForceNewtons(0);
		powerLeftFrontOut = new PowerWatts(0);
		powerRightFrontOut = new PowerWatts(0);
		powerLeftRearOut = new PowerWatts(0);
		powerRightRearOut = new PowerWatts(0);
	}

	@Override
	protected void createPorts()
	{
		control = new MechanicalForceReceivePort(this, this, 0L);
		motorPowerLineLeftFront = new ElectricalPowerTransmitPort(this, 0L);
		motorPowerLineRightFront = new ElectricalPowerTransmitPort(this, 0L);
		motorPowerLineLeftRear = new ElectricalPowerTransmitPort(this, 0L);
		motorPowerLineRightRear = new ElectricalPowerTransmitPort(this, 0L);
	}
}