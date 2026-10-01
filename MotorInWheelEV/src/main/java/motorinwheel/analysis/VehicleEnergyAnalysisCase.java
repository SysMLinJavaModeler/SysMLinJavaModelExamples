package motorinwheel.analysis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import motorinwheel.components.wheel.WheelLocationEnum;
import motorinwheel.systems.vehicle.Vehicle;
import sysmlinjava.analysis.ParametricAnalysisCase;
import sysmlinjava.attributetypes.DistanceKilometers;
import sysmlinjava.attributetypes.EnergyKilowattHours;
import sysmlinjava.attributetypes.EnergyKilowattHoursPerKilometer;
import sysmlinjava.attributetypes.IInteger;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.SpeedKilometersPerHour;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.javaannotations.actions.AnalysisCaseAction;
import sysmlinjava.javaannotations.analysis.AnalysisResult;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.analysis.parametrics.ParametricAnalysis;
import sysmlinjava.views.common.Axis;
import sysmlinjava.views.common.AxisFixedRange;
import sysmlinjava.views.linechart.LineChartData;
import sysmlinjava.views.linechart.LineChartDefinition;
import sysmlinjava.views.linechart.LineChartsDisplay;
import sysmlinjava.views.linechart.LineChartsTransmitter;

/**
 * Parametric analysis for the vehicle energy expended versus vehicle distance
 * traveled. The analysis provides an example of implementing nested analyses in
 * SysMLinJava as well as using analysis parameters that are in different
 * threads of execution. It also is an example of how to use the graphical
 * display capabilities of SysMLinJava to create, view, save, and analyze
 * parametric data in graphical form.
 * 
 * @author ModelerOne
 */
public class VehicleEnergyAnalysisCase extends ParametricAnalysisCase
{
	/**
	 * Constraint part for calculating the energy input to a wheel
	 */
	@ParametricAnalysis
	public MotorInWheelEnergyAnalysisCase wheelEnergyInLeftFront;
	/**
	 * Constraint part for calculating the energy input to a wheel
	 */
	@ParametricAnalysis
	public MotorInWheelEnergyAnalysisCase wheelEnergyInRightFront;
	/**
	 * Constraint part for calculating the energy input to a wheel
	 */
	@ParametricAnalysis
	public MotorInWheelEnergyAnalysisCase wheelEnergyInLeftRear;
	/**
	 * Constraint part for calculating the energy input to a wheel
	 */
	@ParametricAnalysis
	public MotorInWheelEnergyAnalysisCase wheelEnergyInRightRear;

	/**
	 * Parameter for the vehicle's current speed
	 */
	@Parameter
	public SpeedKilometersPerHour currentSpeedIn;
	/**
	 * Parameter (output) for the distance traveled
	 */
	@Parameter
	public DistanceKilometers distanceTraveledOut;
	/**
	 * Parameter for time of current speed value setting
	 */
	@Parameter
	public InstantMilliseconds currentSpeedTime;
	/**
	 * Parameter for time of last speed value setting
	 */
	@Parameter
	public InstantMilliseconds lastSpeedTime;
	/**
	 * Parameter for last speed value
	 */
	@Parameter
	public SpeedKilometersPerHour lastSpeedIn;

	/**
	 * Parameter (output) for the energy used
	 */
	@AnalysisResult
	public EnergyKilowattHours energyKilowattHoursOut;
	/**
	 * Parameter (output) for the energy used per unit distance traveled
	 */
	@AnalysisResult
	public EnergyKilowattHoursPerKilometer energyKilowattHoursPerKilometerOut;

	/**
	 * Count of operator actions taken so far
	 */
	public IInteger operatorActionsCountIn;
	/**
	 * Last count of operator actions taken
	 */
	public IInteger lastOperatorActionsCount;
	/**
	 * Transmitter of constraint parameter value to graphical line chart display
	 */
	public LineChartsTransmitter graphDataTransmitter;

	/**
	 * Constant for watts per kilowatt
	 */
	static final double wattsPerKilowatt = 1000;
	/**
	 * Constant for millisecs per hour
	 */
	static final double millisPerHour = 1000 * 60 * 60;
	/**
	 * Constant for max energy to display (to avoid display of energy pulse at start
	 * up)
	 */
	static final double maxEnergyKilowattHours = 0.80;
	/**
	 * Constant for max energy per distance to display (to avoid display of energy
	 * pulse at start up)
	 */
	static final double maxEnergyKilowattHoursPerKilometer = 3.0;

	/**
	 * Name of parameter for vehicle speed
	 */
	public static final String vehicleSpeedParamName = "vehicleSpeedParam";
	/**
	 * Name of parameter for operator actions count
	 */
	public static final String operatorActionsCountParamName = "operatorActionsCountParam";
	/**
	 * Name of parameter for left front wheel power
	 */
	public static final String powerLeftFrontParamName = "powerLeftFront";
	/**
	 * Name of parameter for right front wheel power
	 */
	public static final String powerRightFrontParamName = "powerRightFront";
	/**
	 * Name of parameter for left rear wheel power
	 */
	public static final String powerLeftRearParamName = "powerLeftRear";
	/**
	 * Name of parameter for right rear wheel power
	 */
	public static final String powerRightRearParamName = "powerRightRear";
	/**
	 * Name of parameter for left front wheel energy
	 */
	public static final String energyLeftFrontParamName = "energyLeftFront";
	/**
	 * Name of parameter for right front wheel energy
	 */
	public static final String energyRightFrontParamName = "energyRightFront";
	/**
	 * Name of parameter for left rear wheel energy
	 */
	public static final String energyLeftRearParamName = "energyLeftRear";
	/**
	 * Name of parameter for right rear wheel energy
	 */
	public static final String energyRightRearParamName = "energyRightRear";

	/**
	 * Constructor
	 * 
	 * @param parent parent constraint part, if any, of this constraint part
	 */
	public VehicleEnergyAnalysisCase()
	{
		super(Optional.empty(), "VehicleEnergyAnalysis", 0L);
		graphDataTransmitter = new LineChartsTransmitter(LineChartsDisplay.udpPort, true);
		transmitGraph();
	}

	@Override
	public void start()
	{
		super.start();
		wheelEnergyInLeftFront.start();
		wheelEnergyInRightFront.start();
		wheelEnergyInLeftRear.start();
		wheelEnergyInRightRear.start();
	}

	@Override
	public void stop()
	{
		wheelEnergyInLeftFront.stop();
		wheelEnergyInRightFront.stop();
		wheelEnergyInLeftRear.stop();
		wheelEnergyInRightRear.stop();
		super.stop();
	}

	/**
	 * Transmits the definition of the graph of the constraint parameters to the
	 * graphical line chart display
	 */
	protected void transmitGraph()
	{
		Axis xAxis = new Axis("Distance", "kilometers", Optional.of(0.0), Optional.of(0.8), 0.05, 5);
		AxisFixedRange y0EnergyAxis = new AxisFixedRange("Energy", "kilowatt-hours", 0.0, maxEnergyKilowattHours, 0.05, 5);
		AxisFixedRange y1EnergyPerDistanceAxis = new AxisFixedRange("Energy/Distance", "kilowatt-hours/kilometer", 0.0, maxEnergyKilowattHoursPerKilometer, 0.5, 5);
		ArrayList<AxisFixedRange> yAxes = new ArrayList<>(Arrays.asList(y0EnergyAxis, y1EnergyPerDistanceAxis));
		LineChartDefinition graph = new LineChartDefinition("Vehicle Energy", yAxes, xAxis);
		graphDataTransmitter.transmitGraph(graph);
	}

	/**
	 * Updates the specified parameter for the bound attribute. This override
	 * updates the parameters declared in this analysis case directly vs. the
	 * {@code super}'s update of parameter values stored in the {@code params} map.
	 */
	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType boundAttribute)
	{
		super.onParameterChange(paramID, boundAttribute); // TODO not needed?
		switch (paramID)
		{
		case operatorActionsCountParamName:
			operatorActionsCountIn.value = ((IInteger) boundAttribute).value;
			// If the count hasn't changed, then
			if (operatorActionsCountIn == null || !operatorActionsCountIn.greaterThan(lastOperatorActionsCount))
			{
				// Get the current speed from the binding connector
			}
			break;
		case vehicleSpeedParamName:
			currentSpeedTime = InstantMilliseconds.now();
			currentSpeedIn.value = ((SpeedKilometersPerHour) boundAttribute).value;
			break;
		case energyLeftFrontParamName:
		case energyRightFrontParamName:
		case energyLeftRearParamName:
		case energyRightRearParamName:
			energyKilowattHoursOut.add((EnergyKilowattHours) boundAttribute);
			break;
		}
	}

	@AnalysisCaseAction
	@Override
	public void perform()
	{
		/*
		 * If the the operator performed an action on the vehicle, i.e. changed its
		 * speed, then Transmit data to the graph display and update the last count of
		 * operator actions
		 */
		if (operatorActionsCountIn != null && operatorActionsCountIn.greaterThan(lastOperatorActionsCount))
		{
			transmitGraphData();
			lastOperatorActionsCount.value = operatorActionsCountIn.value;
		}
		/*
		 * Otherwise, calculate the new values for energy used and distance traveled and
		 * save current time and speed values for use in next calculation
		 */
		else
		{
			Duration deltaTime = Duration.ofMillis(currentSpeedTime.value - lastSpeedTime.value);
			double totalKilometersTraveled = distanceTraveledOut.value;
			double totalKilowattHours = wheelEnergyInLeftFront.energyKilowattHours.value + wheelEnergyInRightFront.energyKilowattHours.value
			+ wheelEnergyInLeftRear.energyKilowattHours.value + wheelEnergyInRightRear.energyKilowattHours.value;
			if (totalKilowattHours < 0.004)
				totalKilowattHours = 0.0;
			double deltaKilometers = deltaTime.toMillis() / millisPerHour * lastSpeedIn.value;
			totalKilometersTraveled += deltaKilometers;
			double kilowattHoursPerKilometer = totalKilometersTraveled > 0 ? (totalKilowattHours / totalKilometersTraveled) : 0;
			energyKilowattHoursOut.setValue(totalKilowattHours);
			distanceTraveledOut.setValue(totalKilometersTraveled);
			energyKilowattHoursPerKilometerOut.setValue(kilowattHoursPerKilometer);

			lastSpeedTime = currentSpeedTime;
			lastSpeedIn.value = currentSpeedIn.value;
		}
	}

	/**
	 * Transmits the values of the constraint parameters to the graphical line chart
	 * display
	 */
	protected void transmitGraphData()
	{
		List<Point2D> xyEnergy = new ArrayList<>(Arrays.asList(new Point2D(distanceTraveledOut.value, energyKilowattHoursOut.value)));
		List<Point2D> xyEnergyPerKm = new ArrayList<>(Arrays.asList(new Point2D(distanceTraveledOut.value, energyKilowattHoursPerKilometerOut.value)));
		List<List<Point2D>> xyListList = new ArrayList<>(Arrays.asList(xyEnergy, xyEnergyPerKm));
		LineChartData graphData = new LineChartData("Vehicle Energy", xyListList);
		graphDataTransmitter.transmitGraphData(graphData);
	}

	@Override
	protected void createAttributes()
	{
		lastOperatorActionsCount = new IInteger(0);
	}

	@Override
	protected void createParameters()
	{
		operatorActionsCountIn = new IInteger(0);
		currentSpeedIn = new SpeedKilometersPerHour(0);
		distanceTraveledOut = new DistanceKilometers(0);
		lastSpeedTime = InstantMilliseconds.now();
		lastSpeedIn = new SpeedKilometersPerHour(0);
		currentSpeedTime = InstantMilliseconds.now();
	}

	@Override
	protected void createObjective()
	{
		objective = MotorInWheelSystemAnalysisRequirements.vehicleEnergyAnalysisObjective;
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(Vehicle.class);
	}

	@Override
	protected void createActors()
	{
		List.of();
	}

	@Override
	protected void createAnalysisCases()
	{
		wheelEnergyInLeftFront = new MotorInWheelEnergyAnalysisCase(Optional.of(this), WheelLocationEnum.leftFront);
		wheelEnergyInRightFront = new MotorInWheelEnergyAnalysisCase(Optional.of(this), WheelLocationEnum.rightFront);
		wheelEnergyInLeftRear = new MotorInWheelEnergyAnalysisCase(Optional.of(this), WheelLocationEnum.leftRear);
		wheelEnergyInRightRear = new MotorInWheelEnergyAnalysisCase(Optional.of(this), WheelLocationEnum.rightRear);
	}

	@Override
	protected void createResult()
	{
		energyKilowattHoursOut = new EnergyKilowattHours(0);
		energyKilowattHoursPerKilometerOut = new EnergyKilowattHoursPerKilometer(0);
	}

}