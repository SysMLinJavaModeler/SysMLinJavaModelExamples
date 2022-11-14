package motorinwheel.constraintblocks.system.vehicle;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import motorinwheel.components.operatordisplays.OperatorDisplays;
import motorinwheel.components.operatorleg.OperatorLeg;
import motorinwheel.components.wheel.WheelLocationEnum;
import sysmlinjava.analysis.common.Axis;
import sysmlinjava.analysis.common.AxisFixedRange;
import sysmlinjava.analysis.linecharts.LineChartData;
import sysmlinjava.analysis.linecharts.LineChartDefinition;
import sysmlinjava.analysis.linecharts.LineChartsDisplay;
import sysmlinjava.analysis.linecharts.LineChartsTransmitter;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.parametrics.ConstraintBlock;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.constraintblocks.SysMLConstraintBlock;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.DistanceKilometers;
import sysmlinjava.valuetypes.EnergyKilowattHours;
import sysmlinjava.valuetypes.EnergyKilowattHoursPerKilometer;
import sysmlinjava.valuetypes.IInteger;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.Point2D;
import sysmlinjava.valuetypes.SpeedKilometersPerHour;

/**
 * Constraint block for the calculation of a graph of the vehicle energy
 * expended versus vehicle distance traveled. The constraint block provides an
 * example of implementing nested constraint blocks in SysMLinJava as well as
 * using constraint parameters that are in different threads of execution. It
 * also is an example of how to use the graphical display capabilities of
 * SysMLinJava to create, view, save, and analyze parametric data in graphical
 * form.
 * 
 * @author ModelerOne
 *
 */
public class VehicleEnergyConstraintBlock extends SysMLConstraintBlock
{
	/**
	 * Parameter port for the counter of operator actions taken
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort operatorActionsCountPort;
	/**
	 * Parameter port for the vehicle's speed
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort vehicleSpeedPort;

	/**
	 * Constraint block for calculating the energy input to a wheel
	 */
	@ConstraintBlock
	public MotorInWheelEnergyConstraintBlock wheelEnergyInLeftFront;
	/**
	 * Constraint block for calculating the energy input to a wheel
	 */
	@ConstraintBlock
	public MotorInWheelEnergyConstraintBlock wheelEnergyInRightFront;
	/**
	 * Constraint block for calculating the energy input to a wheel
	 */
	@ConstraintBlock
	public MotorInWheelEnergyConstraintBlock wheelEnergyInLeftRear;
	/**
	 * Constraint block for calculating the energy input to a wheel
	 */
	@ConstraintBlock
	public MotorInWheelEnergyConstraintBlock wheelEnergyInRightRear;

	/**
	 * Parameter for the vehicle's speed
	 */
	@ConstraintParameter
	public SpeedKilometersPerHour speedIn;
	/**
	 * Parameter (output) for the distance traveled
	 */
	@ConstraintParameter
	public DistanceKilometers distanceTraveledOut;
	/**
	 * Parameter (output) for the energy used
	 */
	@ConstraintParameter
	public EnergyKilowattHours energyKilowattHoursOut;
	/**
	 * Parameter (output) for the energy used per unit distance traveled
	 */
	@ConstraintParameter
	public EnergyKilowattHoursPerKilometer energyKilowattHoursPerKilometerOut;
	/**
	 * Parameter for time of current speed value setting
	 */
	@ConstraintParameter
	public InstantMilliseconds currentSpeedTime;
	/**
	 * Parameter for time of last speed value setting
	 */
	@ConstraintParameter
	public InstantMilliseconds lastSpeedTime;
	/**
	 * Parameter for last speed value
	 */
	@ConstraintParameter
	public SpeedKilometersPerHour lastSpeedIn;
	/**
	 * Constraint that calculates the energy used per unit distance traveled
	 */
	@Constraint
	public SysMLConstraint kiloWattHoursPerKilometerCalculation;

	/**
	 * Function that "binds" the operator actions counter to the parameter port
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction operatorActionsCountPortValueChangedFunction;
	/**
	 * Function that "binds" the vehicle's speed to the parameter port
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction vehicleSpeedPortValueChangedFunction;

	/**
	 * Parameter for the operator actions taken so far
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

	static final double wattsPerKilowatt = 1000;
	static final double millisPerHour = 1000 * 60 * 60;
	static final double maxEnergyKilowattHours = 0.80;
	static final double maxEnergyKilowattHoursPerKilometer = 3.0;

	static final String vehicleSpeedParamName = "vehicleSpeedParam";
	static final String operatorActionsCountParamName = "operatorActionsCountParam";

	/**
	 * Constructor
	 * 
	 * @param parent parent constraint block, if any, of this constraint block
	 */
	public VehicleEnergyConstraintBlock(Optional<? extends SysMLConstraintBlock> parent)
	{
		super(parent, "VehicleEnergy");
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

	@Override
	protected void onParameterChange(String paramID)
	{
		operatorActionsCountIn = (IInteger)operatorActionsCountPort.getValue();
		if (operatorActionsCountIn == null || !operatorActionsCountIn.greaterThan(lastOperatorActionsCount))
		{
			currentSpeedTime = InstantMilliseconds.now();
			speedIn = (SpeedKilometersPerHour)vehicleSpeedPort.getValue();
		}
	}

	@Override
	protected void performConstraints()
	{
		if (operatorActionsCountIn != null && operatorActionsCountIn.greaterThan(lastOperatorActionsCount))
		{
			transmitGraphData();
			lastOperatorActionsCount.value = operatorActionsCountIn.value;
		}
		else
		{
			kiloWattHoursPerKilometerCalculation.apply();
			lastSpeedTime = currentSpeedTime;
			lastSpeedIn.value = speedIn.value;
		}
	}

	/**
	 * Transmits the values of the constraint parameters to the graphical line chart
	 * display
	 */
	protected void transmitGraphData()
	{
		List<Point2D> xyEnergy      = new ArrayList<>(Arrays.asList(new Point2D(distanceTraveledOut.value, energyKilowattHoursOut.value)));
		List<Point2D> xyEnergyPerKm = new ArrayList<>(Arrays.asList(new Point2D(distanceTraveledOut.value, energyKilowattHoursPerKilometerOut.value)));
		List<List<Point2D>> xyListList = new ArrayList<>(Arrays.asList(xyEnergy, xyEnergyPerKm));
		LineChartData graphData = new LineChartData("Vehicle Energy", xyListList);
		graphDataTransmitter.transmitGraphData(graphData);
	}

	@Override
	protected void createValues()
	{
		lastOperatorActionsCount = new IInteger(0);
	}

	@Override
	protected void createConstraintParameters()
	{
		operatorActionsCountIn = new IInteger(0);
		speedIn = new SpeedKilometersPerHour(0);
		distanceTraveledOut = new DistanceKilometers(0);
		energyKilowattHoursOut = new EnergyKilowattHours(0);
		energyKilowattHoursPerKilometerOut = new EnergyKilowattHoursPerKilometer(0);
		lastSpeedTime = InstantMilliseconds.now();
		lastSpeedIn = new SpeedKilometersPerHour(0);
		currentSpeedTime = InstantMilliseconds.now();
	}

	@Override
	protected void createConstraints()
	{
		kiloWattHoursPerKilometerCalculation = () ->
		{
			Duration deltaTime = Duration.ofMillis(currentSpeedTime.value - lastSpeedTime.value);
			double totalKilometersTraveled = distanceTraveledOut.value;
			double totalKilowattHours = wheelEnergyInLeftFront.energyKilowattHours.value + wheelEnergyInRightFront.energyKilowattHours.value + wheelEnergyInLeftRear.energyKilowattHours.value
				+ wheelEnergyInRightRear.energyKilowattHours.value;
			if (totalKilowattHours < 0.004)
				totalKilowattHours = 0.0;
			double deltaKilometers = deltaTime.toMillis() / millisPerHour * lastSpeedIn.value;
			totalKilometersTraveled += deltaKilometers;
			double kilowattHoursPerKilometer = totalKilometersTraveled > 0 ? (totalKilowattHours / totalKilometersTraveled) : 0;
			energyKilowattHoursOut.setValue(totalKilowattHours);
			distanceTraveledOut.setValue(totalKilometersTraveled);
			energyKilowattHoursPerKilometerOut.setValue(kilowattHoursPerKilometer);
		};
	}

	@Override
	protected void createConstraintBlocks()
	{
		wheelEnergyInLeftFront = new MotorInWheelEnergyConstraintBlock(Optional.of(this), WheelLocationEnum.leftFront);
		wheelEnergyInLeftFront.energyKilowattHours.addValueChangeObserver(this);
		wheelEnergyInRightFront = new MotorInWheelEnergyConstraintBlock(Optional.of(this), WheelLocationEnum.rightFront);
		wheelEnergyInRightFront.energyKilowattHours.addValueChangeObserver(this);
		wheelEnergyInLeftRear = new MotorInWheelEnergyConstraintBlock(Optional.of(this), WheelLocationEnum.leftRear);
		wheelEnergyInLeftRear.energyKilowattHours.addValueChangeObserver(this);
		wheelEnergyInRightRear = new MotorInWheelEnergyConstraintBlock(Optional.of(this), WheelLocationEnum.rightRear);
		wheelEnergyInRightRear.energyKilowattHours.addValueChangeObserver(this);
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		vehicleSpeedPortValueChangedFunction = (constraintParameterPort, contextBlock) ->
		{
			try
			{
				SpeedKilometersPerHour parameter = ((OperatorDisplays)contextBlock).speedViewOut;
				constraintParameterPort.queuedParameterValues.put(new SpeedKilometersPerHour(parameter.value));
				constraintParameterPort.constraintBlock.valueChanged();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		};
		operatorActionsCountPortValueChangedFunction = (constraintParameterPort, contextBlock) ->
		{
			try
			{
				IInteger parameter = ((OperatorLeg)contextBlock).actionsCount;
				constraintParameterPort.queuedParameterValues.put(new IInteger(parameter.value));
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
		vehicleSpeedPort = new SysMLConstraintParameterPort(this, vehicleSpeedPortValueChangedFunction, vehicleSpeedParamName);
		operatorActionsCountPort = new SysMLConstraintParameterPort(this, operatorActionsCountPortValueChangedFunction, operatorActionsCountParamName);
	}
}