package motorinwheel.constraintblocks.system.vehicle;

import java.time.Duration;
import java.util.Optional;
import motorinwheel.components.motor.ElectricMotor;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.constraintblocks.SysMLConstraintBlock;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.EnergyKilowattHours;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.PowerWatts;

/**
 * Constraint block for the calculation of the energy used by one of the wheels
 * of the vehicle.
 * 
 * @author ModelerOne
 *
 */
public class MotorInWheelEnergyConstraintBlock extends SysMLConstraintBlock
{
	/**
	 * Parameter port for the value of the wheel's power
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort wheelPowerPort;
	
	/**
	 * Function that "binds" the value of the wheel's power to the port
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction wheelPowerPortFunction;
	
	/**
	 * Parameter for the wheel's power
	 */
	@ConstraintParameter
	public PowerWatts wheelPower;
	/**
	 * Parameter for the energy used by the wheel
	 */
	@ConstraintParameter
	public EnergyKilowattHours energyKilowattHours;

	/**
	 * Constraint that calculates the energy used from the wheel's power
	 */
	@Constraint
	public SysMLConstraint energyCalculation;

	/**
	 * Location of the wheel on the vehicle
	 */
	@ConstraintParameter
	public WheelLocationEnum wheelLocationEnum;

	/**
	 * Time of the current value of wheel power
	 */
	@ConstraintParameter
	public InstantMilliseconds currentPowerTime;
	/**
	 * Time of the previous value of wheel power
	 */
	@ConstraintParameter
	public InstantMilliseconds previousPowerTime;
	/**
	 * Previous value of wheel power
	 */
	@ConstraintParameter
	public PowerWatts previousPower;

	private static final String wheelPowerParamName = "wheelPowerParam";

	/**
	 * Constructor
	 * @param parent parent constraint block
	 * @param wheelLocationEnum location of the wheel for this constraint block
	 */
	public MotorInWheelEnergyConstraintBlock(Optional<? extends SysMLConstraintBlock> parent, WheelLocationEnum wheelLocationEnum)
	{
		super(parent, "MotorInWheelEnergy");
		this.wheelLocationEnum = wheelLocationEnum;
	}

	@Override
	protected void onParameterChange(String paramID)
	{
		currentPowerTime = InstantMilliseconds.now();
		wheelPower = (PowerWatts)wheelPowerPort.getValue();
	}

	@Override
	protected void performConstraints()
	{
		energyCalculation.apply();
		logger.info(wheelLocationEnum.name + ": " + wheelPower.toString() + " --> " + energyKilowattHours.toString());
		previousPower.value = wheelPower.value;
		previousPowerTime = currentPowerTime;
	}

	@Override
	protected void createConstraintParameters()
	{
		wheelLocationEnum = WheelLocationEnum.leftFront;  //Set for real after super (that invokes all "create" calls) called
		energyKilowattHours = new EnergyKilowattHours(0);
		wheelPower = new PowerWatts(0);
		previousPower = new PowerWatts(0);
		previousPowerTime = InstantMilliseconds.now();
		currentPowerTime = InstantMilliseconds.MAX;
	}

	@Override
	protected void createConstraints()
	{
		energyCalculation = () ->
		{
			Duration period = Duration.ofMillis(currentPowerTime.value - previousPowerTime.value);
			double periodKilowattHours = (previousPower.value / VehicleEnergyConstraintBlock.wattsPerKilowatt) * (period.toMillis() / VehicleEnergyConstraintBlock.millisPerHour);
			double currentKilowattHours = energyKilowattHours.value + periodKilowattHours;
			energyKilowattHours.setValue(currentKilowattHours);
		};
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		wheelPowerPortFunction = (constraintParameterPort, contextBlock) ->
		{
			try
			{
				PowerWatts parameter = ((ElectricMotor)contextBlock).electricalPowerIn;
				constraintParameterPort.queuedParameterValues.put(new PowerWatts(parameter.value));
				constraintParameterPort.constraintBlock.valueChanged();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		wheelPowerPort = new SysMLConstraintParameterPort(this, wheelPowerPortFunction, wheelPowerParamName);
	}
}