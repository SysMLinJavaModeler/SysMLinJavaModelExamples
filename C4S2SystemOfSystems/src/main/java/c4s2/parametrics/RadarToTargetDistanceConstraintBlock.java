package c4s2.parametrics;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import c4s2.systems.radar.RadarSystem;
import c4s2.systems.target.VehicleArmoredLargeTarget;
import sysmlinjava.analysis.common.Axis;
import sysmlinjava.analysis.common.AxisFixedRange;
import sysmlinjava.analysis.linecharts.LineChartData;
import sysmlinjava.analysis.linecharts.LineChartDefinition;
import sysmlinjava.analysis.linecharts.LineChartsDisplay;
import sysmlinjava.analysis.linecharts.LineChartsTransmitter;
import sysmlinjava.annotations.Constraint;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.common.SysMLConstraint;
import sysmlinjava.constraintblocks.SysMLConstraintBlock;
import sysmlinjava.constraintblocks.SysMLConstraintBlockStateMachine;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.Point2D;
import sysmlinjava.valuetypes.PointGeospatial;

/**
 * {@code RadarToTargetDistanceConstraintBlock} is a SysMLinJava model of a
 * SysML constraint block for the calculation of the distance from a tracking
 * radar system and the target it is tracking over time. Its input constraint
 * parameters are the changing geospatial positions of the radar and the target.
 * Its single output parameter is the distance between the two positions. This
 * constraint block also transmits the output parameter to a line chart display
 * that, if executing within the same computer operating system, will display a
 * line for the distances over time. The line chart display provided with the
 * SysMLinJava API is nothing more than a textual/console display of the
 * parameter values, but commercially available displays are available that
 * provide a true graphical display of the line chart. See SysMLinJava.com for
 * more information.
 * 
 * @author ModelerOne
 *
 */
public class RadarToTargetDistanceConstraintBlock extends SysMLConstraintBlock
{
	/**
	 * Function for "binding" the constraint parameter for the target position
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction targetPositionParameterPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the radar position
	 */
	@ConstraintParameterPortFunction
	public SysMLConstraintParameterPortFunction radarPositionParameterPortFunction;

	/**
	 * Port for making the "binding connector" to the constraint parameter for the
	 * target position, i.e. for executing the binding function
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort targetPositionParameterPort;
	/**
	 * Port for making the "binding connector" to the constraint parameter for the
	 * radar position, i.e. for executing the binding function
	 */
	@ConstraintParameterPort
	public SysMLConstraintParameterPort radarPositionParameterPort;

	/**
	 * Output parameter for the distance from target to radar system
	 */
	@ConstraintParameter
	public DistanceMeters distanceTargetToRadar;
	/**
	 * Input (bound) parameter for the target position
	 */
	@ConstraintParameter
	public PointGeospatial targetPosition;
	/**
	 * Input (bound) parameter for the radar position
	 */
	@ConstraintParameter
	public PointGeospatial radarPosition;

	/**
	 * Constraint that calculates the distance from the two positions
	 */
	@Constraint
	public SysMLConstraint targetToRadarDistanceConstraint;

	/**
	 * Start time of the target tracking
	 */
	Instant startTime;
	/**
	 * Transmitter of the output parameter values to the line chart display
	 */
	LineChartsTransmitter dataTransmitter;

	/**
	 * Definition of the line chart that will display the radar-to-target distance
	 * over time
	 */
	LineChartDefinition chartDef;
	/**
	 * Definition of the line chart data for the next points on the line for the
	 * radar-to-target distance over time.
	 */
	LineChartData chartData;

	static final Axis xAxis = new Axis("Time", "seconds", Optional.of(0.0), Optional.of(100.0), 10, 10);
	static final AxisFixedRange yAxis = new AxisFixedRange("Distance", "meters", 0.0, 3_000.0, 500, 10);
	static final ArrayList<AxisFixedRange> yAxes = new ArrayList<>(Arrays.asList(yAxis));

	private static final String targetPositionParamName = "targetPositionParame";
	private static final String radarPositionParamName = "radarPositionParam";

	/**
	 * Constructor that initializes the line chart display and the startTime
	 */
	public RadarToTargetDistanceConstraintBlock()
	{
		super(Optional.empty(), "RadarToTargetDistance");
		dataTransmitter = new LineChartsTransmitter(LineChartsDisplay.udpPort, true);
		chartDef = new LineChartDefinition("Radar-to-Target Distance", yAxes, xAxis);
		dataTransmitter.transmitGraph(chartDef);
		startTime = Instant.now();

		List<List<Point2D>> linesPoints = new ArrayList<List<Point2D>>();
		linesPoints.add(new ArrayList<Point2D>());
		chartData.linesPoints = linesPoints;
	}

	@Override
	protected void performConstraints()
	{
		constraint.apply();
		dataTransmitter.transmitGraphData(chartData);
	}

	@Override
	protected void createConstraintParameters()
	{
		radarPosition = new PointGeospatial();
		targetPosition = new PointGeospatial();
		distanceTargetToRadar = new DistanceMeters(0);
	}

	/**
	 * Creates the constraint which calculates the distance, in geospace, from the
	 * target to the radar and then creates the chart data to update the line chart
	 * display.
	 */
	@Override
	protected void createConstraints()
	{
		constraint = () ->
		{
			radarPosition = (PointGeospatial)constraintParams.get(radarPositionParamName);
			distanceTargetToRadar = targetPosition.distanceTo(radarPosition);
			Duration timeSinceStart = Duration.between(startTime, Instant.now());
			Point2D point2D = new Point2D(timeSinceStart.toSeconds(), distanceTargetToRadar.value);

			chartData.linesPoints.get(0).set(0, point2D);
		};
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		targetPositionParameterPortFunction = (constraintParameterPort, contextBlock) ->
		{
			PointGeospatial parameter = ((VehicleArmoredLargeTarget)contextBlock).currentPosition;
			constraintParameterPort.updateParameterValue(new PointGeospatial(parameter));
		};
		radarPositionParameterPortFunction = (constraintParameterPort, contextBlock) ->
		{
			PointGeospatial parameter = ((RadarSystem)contextBlock).systemPosition;
			constraintParameterPort.updateParameterValue(new PointGeospatial(parameter));
		};
		paramPortFunctions.put(targetPositionParamName, targetPositionParameterPortFunction);
		paramPortFunctions.put(radarPositionParamName, radarPositionParameterPortFunction);
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		targetPositionParameterPort = new SysMLConstraintParameterPort(this, targetPositionParameterPortFunction, targetPositionParamName);
		radarPositionParameterPort = new SysMLConstraintParameterPort(this, radarPositionParameterPortFunction, radarPositionParamName);
		
		paramPorts.put(targetPositionParamName, targetPositionParameterPort);
		paramPorts.put(radarPositionParamName, radarPositionParameterPort);
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new SysMLConstraintBlockStateMachine(this, "RadarToTargetDistanceConstraingBlockStateMachine"));
	}
}
