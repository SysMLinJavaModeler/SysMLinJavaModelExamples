package motorinwheel.components.elecpowersupply;

import java.util.Optional;
import motorinwheel.common.ports.energy.ElectricalPowerTransmitPort;
import motorinwheel.common.ports.energy.MechanicalForceReceivePort;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.kinds.SysMLFlowDirectionKind;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.IInteger;
import sysmlinjava.valuetypes.PowerWatts;
import sysmlinjava.valuetypes.RReal;

/**
 * Vehicle component that provides the electrical power to all of the motors in
 * the motor-in-wheels. Function is to translate operator's force on a "switch"
 * into electrical power to be delivered to the motors-in-wheels.
 * 
 * @author ModelerOne
 *
 */
public class ElectricPowerSupply extends SysMLBlock
{
	/**
	 * Port to receive control of the power supply from the accelerator
	 */
	@FullPort
	public MechanicalForceReceivePort control;
	/**
	 * Port to transmit electric power to one of the motors in a wheel
	 */
	@FullPort
	public ElectricalPowerTransmitPort motorPowerLineLeftFront;
	/**
	 * Port to transmit electric power to one of the motors in a wheel
	 */
	@FullPort
	public ElectricalPowerTransmitPort motorPowerLineRightFront;
	/**
	 * Port to transmit electric power to one of the motors in a wheel
	 */
	@FullPort
	public ElectricalPowerTransmitPort motorPowerLineLeftRear;
	/**
	 * Port to transmit electric power to one of the motors in a wheel
	 */
	@FullPort
	public ElectricalPowerTransmitPort motorPowerLineRightRear;

	/**
	 * Flow value of the force on the power supply "switch"
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public ForceNewtons controlIn;
	/**
	 * Flow value of the electric power that is sent to the motor in a wheel
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public PowerWatts powerLeftFrontOut;
	/**
	 * Flow value of the electric power that is sent to the motor in a wheel
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public PowerWatts powerRightFrontOut;
	/**
	 * Flow value of the electric power that is sent to the motor in a wheel
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public PowerWatts powerLeftRearOut;
	/**
	 * Flow value of the electric power that is sent to the motor in a wheel
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public PowerWatts powerRightRearOut;

	/**
	 * Value of the ratio of control force to electric power out
	 */
	@Value
	public RReal controlForceToPowerOutRatio;
	/**
	 * Value of the number of motors powered by the power supply
	 */
	@Value
	public IInteger numberMotors;

	/**
	 * Constraint that calculates the power to send out to each motor in a wheel.
	 * Note the current constraint assumes each wheel gets the same power. More
	 * advanced model could make this power a function of steering angle, tire slip,
	 * etc.
	 */
	@Constraint
	public SysMLConstraint powerOutsCalculator;

	/**
	 * Constructor
	 * 
	 * @param vehicle vehicle in which the power supply resides
	 * @param name    unique name
	 * @param id      unique ID
	 */
	public ElectricPowerSupply(Vehicle vehicle, String name, Long id)
	{
		super(vehicle, name, id);
	}

	/**
	 * Reception that reacts to the receipt of a new control input force
	 * 
	 * @param control force on the "on" control switch/lever
	 */
	@Reception
	public void onControl(ForceNewtons control)
	{
		controlIn.value = control.value;
		powerOutsCalculator.apply();
		motorPowerLineLeftFront.transmit(powerLeftFrontOut);
		motorPowerLineRightFront.transmit(powerRightFrontOut);
		motorPowerLineLeftRear.transmit(powerLeftRearOut);
		motorPowerLineRightRear.transmit(powerRightRearOut);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new ElectricPowerSupplyStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		controlForceToPowerOutRatio = new RReal(1500);
		numberMotors = new IInteger(4);
	}

	@Override
	protected void createFlows()
	{
		controlIn = new ForceNewtons(0);
		powerLeftFrontOut = new PowerWatts(0);
		powerRightFrontOut = new PowerWatts(0);
		powerLeftRearOut = new PowerWatts(0);
		powerRightRearOut = new PowerWatts(0);
	}

	@Override
	protected void createFullPorts()
	{
		control = new MechanicalForceReceivePort(this, this, 0L);
		motorPowerLineLeftFront = new ElectricalPowerTransmitPort(this, 0L);
		motorPowerLineRightFront = new ElectricalPowerTransmitPort(this, 0L);
		motorPowerLineLeftRear = new ElectricalPowerTransmitPort(this, 0L);
		motorPowerLineRightRear = new ElectricalPowerTransmitPort(this, 0L);
	}

	@Override
	protected void createConstraints()
	{
		powerOutsCalculator = () ->
		{
			double powerOutPerMotor = controlIn.value * controlForceToPowerOutRatio.value / numberMotors.value;
			powerLeftFrontOut.value = powerOutPerMotor;
			powerRightFrontOut.value = powerOutPerMotor;
			powerLeftRearOut.value = powerOutPerMotor;
			powerRightRearOut.value = powerOutPerMotor;
		};
	}
}