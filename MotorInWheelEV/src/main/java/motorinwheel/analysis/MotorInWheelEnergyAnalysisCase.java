package motorinwheel.analysis;

import static motorinwheel.analysis.VehicleEnergyAnalysisCase.millisPerHour;
import static motorinwheel.analysis.VehicleEnergyAnalysisCase.wattsPerKilowatt;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.systems.motorinwheel.MotorInWheelSystem;
import sysmlinjava.analysis.ParametricAnalysisCase;
import sysmlinjava.attributetypes.EnergyKilowattHours;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.connectors.SysMLBindingConnector;
import sysmlinjava.javaannotations.actions.AnalysisCaseAction;
import sysmlinjava.javaannotations.analysis.AnalysisResult;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;

/**
 * Parametric analysis for the energy used by one of the wheels of the vehicle.
 * 
 * @author ModelerOne
 */
public class MotorInWheelEnergyAnalysisCase extends ParametricAnalysisCase
{
	/**
	 * Parameter for the wheel's power
	 */
	@Parameter
	public PowerWatts wheelPower;
	/**
	 * Analysis result parameter for the energy used by the wheel
	 */
	@AnalysisResult
	public EnergyKilowattHours energyKilowattHours;

	/**
	 * Location of the wheel on the vehicle
	 */
	@Parameter
	public WheelLocationEnum wheelLocationEnum;

	/**
	 * Time of the current value of wheel power
	 */
	@Parameter
	public InstantMilliseconds currentPowerTime;
	/**
	 * Time of the previous value of wheel power
	 */
	@Parameter
	public InstantMilliseconds previousPowerTime;
	/**
	 * Previous value of wheel power
	 */
	@Parameter
	public PowerWatts previousPower;

	/**
	 * Constructor
	 * 
	 * @param parent            parent constraint part
	 * @param wheelLocationEnum location of the wheel for this constraint part
	 */
	public MotorInWheelEnergyAnalysisCase(Optional<? extends ParametricAnalysisCase> parent, WheelLocationEnum wheelLocationEnum)
	{
		super(parent, "MotorInWheelEnergy" + wheelLocationEnum.name(), 0L);
		this.wheelLocationEnum = wheelLocationEnum;
	}

	@Override
	protected void onParameterChange(String paramID)
	{
		logger.warning("---> paramID = " + paramID);
		if (paramID.equals("power" + wheelLocationEnum.name()))
		{
			currentPowerTime = InstantMilliseconds.now();
			SysMLBindingConnector connector = paramConnectors.get(paramID);
			if (connector != null)
				wheelPower = (PowerWatts) connector.getAttribute();
			else
				logger.warning("binding connector not found for paramID: " + paramID);
		}
		else
			logger.warning("unexpected paramID: " + paramID + ", i.e. not " + wheelLocationEnum.toString());
	}

	@AnalysisCaseAction
	@Override
	public void perform()
	{
		Duration period = Duration.ofMillis(currentPowerTime.value - previousPowerTime.value);
		double periodKilowattHours = (previousPower.value / wattsPerKilowatt) * (period.toMillis() / millisPerHour);
		double currentKilowattHours = energyKilowattHours.value + periodKilowattHours;
		energyKilowattHours.setValue(currentKilowattHours);
		previousPower.value = wheelPower.value;
		previousPowerTime = currentPowerTime;
	}

	@Override
	protected void createParameters()
	{
		wheelLocationEnum = WheelLocationEnum.leftFront; // Set for real after super (that invokes all "create" calls) called
		wheelPower = new PowerWatts(0);
		previousPower = new PowerWatts(0);
		previousPowerTime = InstantMilliseconds.now();
		currentPowerTime = InstantMilliseconds.MAX;
	}

	@Override
	protected void createObjective()
	{
		objective = MotorInWheelSystemAnalysisRequirements.wheelEnergyAnalysisObjective;
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(MotorInWheelSystem.class);
	}

	@Override
	protected void createActors()
	{
		actors = List.of();
	}

	@Override
	protected void createResult()
	{
		energyKilowattHours = new EnergyKilowattHours(0);
	}
}