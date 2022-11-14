package c4s2.parametrics;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import c4s2.common.valueTypes.TargetStatusEnum;
import c4s2.systems.strike.StrikeSystem;
import c4s2.systems.target.VehicleArmoredLargeTarget;
import sysmlinjava.analysis.animatedareadisplay.AAImage;
import sysmlinjava.analysis.animatedareadisplay.AALine;
import sysmlinjava.analysis.animatedareadisplay.AAText;
import sysmlinjava.analysis.animatedareadisplay.AAObject.Action;
import sysmlinjava.analysis.animatedareadisplay.AAText.FontWeightEnum;
import sysmlinjava.analysis.animatedareadisplay.AnimatedAreaGeospatialDisplayConstraintBlock;
import sysmlinjava.analysis.common.ColorEnum;
import sysmlinjava.analysis.common.XYData;
import sysmlinjava.annotations.Value;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.DirectionRadians;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.LatitudeDegrees;
import sysmlinjava.valuetypes.LongitudeDegrees;
import sysmlinjava.valuetypes.LongitudeRadians;
import sysmlinjava.valuetypes.PointGeospatial;
import sysmlinjava.valuetypes.RReal;
import sysmlinjava.valuetypes.SpeedMetersPerSecond;
import sysmlinjava.valuetypes.VelocityMetersPerSecondRadians;

/**
 * Constraint block to calculate an area display of an executing model of the
 * C4S2 System-of-Systems. The constraint block is an extension of the
 * {@code AreaGeospatialDispalyConstraintBlock} which performs much of the task
 * that involves the retrieval/updating/conversion of the constraint parameters
 * as well as their transmission to the area display.
 * <p>
 * The block uses constraint parameters that represent the following:
 * <ul>
 * <li>Radar System's geo-location</li>
 * <li>Strike System's geo-location and velocity</li>
 * <li>Target (VehicleArmoredLargeTarget)'s geo-location and status</li>
 * </ul>
 * The constraint uses the radar, strike, and target parameters to update a
 * graphical display of the three objects' positions, movements, and/or statuses
 * making for an animated depiction of a single scenario of execution of the
 * system-of-systems.
 * <p>
 * The execution of the model commences with the target (a large armored
 * vehicle) approaching the radar scanning area. After entering the scanning
 * area, the vehicle reflects subsequent radar transmissions back to the radar
 * revealing the target's position and signature (type of vehicle). Once the
 * radar confirms the target, the strike system is notified and continuously
 * provided with the target location. The strike system then proceeds to move to
 * the direction of the target, shooting it when within minimum range. When hit,
 * the target explodes and remains in its final position after being destroyed.
 * The radar system assesses the target by continuing to scan it and receives a
 * reflection from the vehicle that indicates is is, in fact destroyed. The
 * strike system is directed to return to its base and it proceeds to do so,
 * thereby ending the model execution scenario.
 * <p>
 * The {@code C4S2ExecutionDisplayConstraintBlock}, along with the
 * {@code C4A2SystemOfSystems} model demonstrate how a SysMLinJava model can be
 * graphically visualized in real time. Along with the use of other SysMLinJava
 * displays, such as the Sequence Diagarm, State Transition, Timing Diagram, and
 * Line, Bar, and Scatter Chart displays, the Area Display can assist in
 * extensive and highly precise analysis of SysML models - well beyond the more
 * limited analyses that can be performed with the traditional "boxology"-based
 * models.
 * 
 * @author ModelerOne
 *
 */
public class C4S2ExecutionDisplayConstraintBlock extends AnimatedAreaGeospatialDisplayConstraintBlock
{
	/**
	 * Title of the display
	 */
	public static final String title = "C4S2 System Execution Scenario 4B";
	/**
	 * ID for the radar in the display
	 */
	public static final String uidRadar = "radar";
	/**
	 * ID for the target in the display
	 */
	public static final String uidTarget = "target";
	/**
	 * ID for the strike system in the display
	 */
	public static final String uidStrike = "strike";
	/**
	 * ID for the radar fan in the display
	 */
	public static final String uidFan = "fan";

	/**
	 * ID for the state of the target 
	 */
	public static final String uidTargetState = "targetState";
	/**
	 * ID for the state of the strike system 
	 */
	public static final String uidStrikePosition = "strikePosition";
	/**
	 * ID for the position of the target 
	 */
	public static final String uidTargetPosition = "targetPosition";
	/**
	 * ID for the velocity of the strike system 
	 */
	public static final String uidStrikeVelocity = "strikeVelocity";
	/**
	 * ID for the velocity of the target 
	 */
	public static final String uidTargetVelocity = "targetVelocity";

	/**
	 * Text label for the radar
	 */
	public static final String radarText = "Radar System";
	/**
	 * Text label for the target
	 */
	public static final String targetText = "Target System";
	/**
	 * Text label for the strike system
	 */
	public static final String strikeText = "Strike System";
	/**
	 * Text for the name of the font used in the display
	 */
	public static final String textFontName = "Arial Bold";

	/**
	 * URL for the image file for the display background
	 */
	public static String backgroundImageFileURL;
	/**
	 * URL for the image file for the radar
	 */
	public static String radarImageFileURL;
	/**
	 * URL for the image file for the strike system
	 */
	public static String strikeImageFileURL;
	/**
	 * URL for the image file for the target while operating/moving
	 */
	public static String targetImageFileURLOperating;
	/**
	 * URL for the image file for the target while exploding
	 */
	public static String targetImagFileURLExploding;
	/**
	 * URL for the image file for the target after destroyed
	 */
	public static String targetImagFileURLDestroyed;

	/**
	 * Initialization of the URLs for each of the image files used in the display
	 */
	static
	{
		final String imageDir = "C:\\Users\\Joe.LAPTOP-VNMR71J5\\EclipseWorkspace\\C4S2SystemOfSystems\\";
		try
		{
			backgroundImageFileURL = Path.of(imageDir, "landImage.PNG").toAbsolutePath().toUri().toURL().toString();
			radarImageFileURL = Path.of(imageDir, "radar.png").toAbsolutePath().toUri().toURL().toString();
			strikeImageFileURL = Path.of(imageDir, "strike.png").toAbsolutePath().toUri().toURL().toString();
			targetImageFileURLOperating = Path.of(imageDir, "targetOperating.png").toAbsolutePath().toUri().toURL().toString();
			targetImagFileURLExploding = Path.of(imageDir, "targetExploding.png").toAbsolutePath().toUri().toURL().toString();
			targetImagFileURLDestroyed = Path.of(imageDir, "targetDestroyed.png").toAbsolutePath().toUri().toURL().toString();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Constraint parameter for the current target position
	 */
	@ConstraintParameter
	PointGeospatial targetPosition;

	/**
	 * Constraint parameter for the current strike vehicle position
	 */
	@ConstraintParameter
	PointGeospatial strikePosition;

	/**
	 * Constraint parameter for the current target state
	 */
	@ConstraintParameter
	TargetStatusEnum targetState;

	/**
	 * Constraint parameter for the current strike vehicle velocity
	 */
	@ConstraintParameter
	VelocityMetersPerSecondRadians strikeVelocity;

	/**
	 * Function to retrieve the bound value for the constraint parameter for the
	 * current target position
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction targetPositionPortFunction;
	/**
	 * Function to retrieve the bound value for the constraint parameter for the
	 * current strike vehicle position
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction strikePositionPortFunction;
	/**
	 * Function to retrieve the bound value for the constraint parameter for the
	 * current strike vehicle velocity
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction strikeVelocityPortFunction;
	/**
	 * Function to retrieve the bound value for the constraint parameter for the
	 * current state of the target
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction targetStatePortFunction;

	/**
	 * Parameter port that retrieves the bound value for the constraint parameter
	 * for the current target position
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort targetPositionPort;
	/**
	 * Parameter port that retrieves the bound value for the constraint parameter
	 * for the current strike vehicle position
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort strikePositionPort;
	/**
	 * Parameter port that retrieves the bound value for the constraint parameter
	 * for the current strike vehicle velocity
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort strikeVelocityPort;
	/**
	 * Parameter port that retrieves the bound value for the constraint parameter
	 * for the current state of the target
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort targetStatePort;

	/**
	 * Value for the geo-spatial location of the center of the radar scan area
	 */
	@Value
	PointGeospatial scanCenterPosition;

	/**
	 * Map of the constraint parameter IDs to objects on the area display
	 */
	Map<String, String> paramIDAObjectIDMap;

	/**
	 * Constructor
	 * 
	 * @param udpPort UDP port of the area display to which display data is to be
	 *                sent
	 */
	public C4S2ExecutionDisplayConstraintBlock(int udpPort)
	{
		super(udpPort, true);
		paramIDAObjectIDMap = new HashMap<>();
		paramIDAObjectIDMap.put(uidTargetPosition, uidTarget);
		paramIDAObjectIDMap.put(uidStrikePosition, uidStrike);
		paramIDAObjectIDMap.put(uidTargetVelocity, uidTarget);
		paramIDAObjectIDMap.put(uidStrikeVelocity, uidStrike);
		paramIDAObjectIDMap.put(uidTargetState, uidTarget);
	}

	@Override
	protected void createValues()
	{
		super.createValues();

		scanCenterPosition = new PointGeospatial(new LatitudeDegrees(45), new LongitudeDegrees(105));
		pointsCartesianPerKilometer = new RReal(975 / 1.8);
		kilometerPerRadianLon = new RReal(LongitudeRadians.kilometersPerRadianLongitude(scanCenterPosition.latitude.value).value);

		double upperLeftLat = scanCenterPosition.latitude.value + 0.25 / kilometerPerRadianLat.value;
		double upperLeftLon = scanCenterPosition.longitude.value + 0.90 / kilometerPerRadianLon.value;
		geospatialUpperLeft = new PointGeospatial(upperLeftLat, upperLeftLon);

		pointsPerRadianLatitude = new RReal(pointsCartesianPerKilometer.value * kilometerPerRadianLat.value);
		pointsPerRadianLongitude = new RReal(pointsCartesianPerKilometer.value * kilometerPerRadianLon.value);
	}

	@Override
	protected void createConstraintParameters()
	{
		targetPosition = scanCenterPosition.movedTo(DirectionRadians.east, new DistanceMeters(1000));
		strikePosition = new PointGeospatial(scanCenterPosition.movedTo(DirectionRadians.south, new DistanceMeters(1100)));
		targetState = new TargetStatusEnum(TargetStatusEnum.operating);
		strikeVelocity = new VelocityMetersPerSecondRadians(SpeedMetersPerSecond.zero, DirectionRadians.north);

		constraintParams.put(uidTargetPosition, targetPosition);
		constraintParams.put(uidStrikePosition, strikePosition);
		constraintParams.put(uidTargetState, targetState);
		constraintParams.put(uidStrikeVelocity, strikeVelocity);
	}

	/**
	 * Creates the constraint which performs as follows:
	 * <ul>
	 * <li>If a previously updated constraint parameter (last one changed) exists,
	 * nullify the display data for the previous parameter to indicate no changes to
	 * make to the parameter's display.</li>
	 * <li>If a new current constraint parameter (next one changed) exists, update
	 * the parameter's corresponding display data accoringly</li>
	 * </ul>
	 */
	@Override
	protected void createConstraints()
	{
		constraint = () ->
		{
			if (previousParamID.isPresent())
			{
				if (previousParam instanceof PointGeospatial)
					displayData.updateImage(previousParamID.get(), Action.none, null, null, null, null, null, null);
				else if (previousParam instanceof VelocityMetersPerSecondRadians velocity)
				{
					String aimageID = paramIDAObjectIDMap.get(previousParamID.get());
					displayData.updateImage(aimageID, Action.none, null, null, null, null, null, null);
				}
				else if (previousParam instanceof TargetStatusEnum targetStatus)
				{
					String aimageID = paramIDAObjectIDMap.get(previousParamID.get());
					displayData.updateImage(aimageID, Action.none, null, null, null, null, null, null);
				}
			}

			if (currentParamID.isPresent())
			{
				String aimageID = paramIDAObjectIDMap.get(currentParamID.get());
				if (currentParam instanceof PointGeospatial nextPoint)
					displayData.updateImage(aimageID, AAImage.Action.update, null, null, null, null, toPoint2D(nextPoint), null);
				else if (currentParam instanceof VelocityMetersPerSecondRadians velocity)
					displayData.updateImage(aimageID, AAImage.Action.update, null, null, null, (int)Math.toDegrees(velocity.heading.value), null, null);
				else if (currentParam instanceof TargetStatusEnum targetStatus)
				{
					if (targetStatus.equals(TargetStatusEnum.exploding))
						displayData.updateImage(aimageID, AAImage.Action.update, targetImagFileURLExploding, 60, 30, null, null, null);
					else if (targetStatus.equals(TargetStatusEnum.destroyed))
						displayData.updateImage(aimageID, AAImage.Action.update, targetImagFileURLDestroyed, 60, 30, null, null, null);
				}
			}
		};
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		targetPositionPortFunction = (constraintParameterPort, contextBlock) ->
		{
			PointGeospatial targetPosition = ((VehicleArmoredLargeTarget)contextBlock).currentPosition;
			constraintParameterPort.updateParameterValue(new PointGeospatial(targetPosition));
		};
		strikePositionPortFunction = (constraintParameterPort, contextBlock) ->
		{
			PointGeospatial strikePosition = ((StrikeSystem)contextBlock).currentPosition;
			constraintParameterPort.updateParameterValue(new PointGeospatial(strikePosition));
		};
		strikeVelocityPortFunction = (constraintParameterPort, contextBlock) ->
		{
			VelocityMetersPerSecondRadians strikeVelocity = ((StrikeSystem)contextBlock).currentVelocity;
			constraintParameterPort.updateParameterValue(new VelocityMetersPerSecondRadians(strikeVelocity));
		};
		targetStatePortFunction = (constraintParameterPort, contextBlock) ->
		{
			TargetStatusEnum targetState = ((VehicleArmoredLargeTarget)contextBlock).operatingStatus;
			constraintParameterPort.updateParameterValue(new TargetStatusEnum(targetState));
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		targetPositionPort = new SysMLConstraintParameterPort(this, targetPositionPortFunction, uidTargetPosition);
		strikePositionPort = new SysMLConstraintParameterPort(this, strikePositionPortFunction, uidStrikePosition);
		strikeVelocityPort = new SysMLConstraintParameterPort(this, strikeVelocityPortFunction, uidStrikeVelocity);
		targetStatePort = new SysMLConstraintParameterPort(this, targetStatePortFunction, uidTargetState);

		paramPorts.put(uidTargetPosition, targetPositionPort);
		paramPorts.put(uidStrikePosition, strikePositionPort);
		paramPorts.put(uidStrikeVelocity, strikeVelocityPort);
		paramPorts.put(uidTargetState, targetStatePort);
	}

	/**
	 * Creates the display definition, i.e. the display data for the initial and
	 * non-changing objects in the display, e.g. the background image and the radar
	 * image, radar scan area (fan) line, and target and strike system imagews. Note
	 * that as a geospatial model, all geospatial locations of objects must be
	 * converted into X,Y positions for the area display.
	 */
	@Override
	protected void createDisplayDefinition()
	{
		displayDefinition.title = title;
		PointGeospatial radarPointGeo = scanCenterPosition.movedTo(DirectionRadians.south, new DistanceMeters(1000));
		XYData xyRadar = toXYData(radarPointGeo);

		XYData xyFanNW = toXYData(radarPointGeo.movedTo(new DirectionRadians(Math.toRadians(-22.5)), new DistanceMeters(1250)));
		XYData xyFanNE = toXYData(radarPointGeo.movedTo(new DirectionRadians(Math.toRadians(22.5)), new DistanceMeters(1250)));
		XYData xyFanSW = toXYData(radarPointGeo.movedTo(new DirectionRadians(Math.toRadians(-22.5)), new DistanceMeters(750)));
		XYData xyFanSE = toXYData(radarPointGeo.movedTo(new DirectionRadians(Math.toRadians(22.5)), new DistanceMeters(750)));

		XYData xyTarget = toXYData((PointGeospatial)constraintParams.get(uidTargetPosition));
		XYData xyStrike = toXYData((PointGeospatial)constraintParams.get(uidStrikePosition));

		ArrayList<XYData> xyFan = new ArrayList<XYData>(List.of(xyFanNW, xyFanNE, xyFanSE, xyFanSW, xyFanNW));
		displayDefinition.lines.add(new AALine(uidFan, Action.create, xyFan, ColorEnum.DARKKHAKI, 2, 6));

		displayDefinition.images.add(new AAImage(uidRadar, Action.create, radarImageFileURL, 100, 50, 0, xyRadar, 4));
		displayDefinition.images.add(new AAImage(uidTarget, Action.create, targetImageFileURLOperating, 60, 30, 0, xyTarget, 4));
		displayDefinition.images.add(new AAImage(uidStrike, Action.create, strikeImageFileURL, 150, 50, 0, xyStrike, 2));

		displayDefinition.texts.add(new AAText(radarText, Action.create, radarText, textFontName, 14, FontWeightEnum.bold, ColorEnum.YELLOW, null, null, xyRadar.offset(30, 0), null));
		displayDefinition.texts.add(new AAText(targetText, Action.create, targetText, textFontName, 14, FontWeightEnum.bold, ColorEnum.YELLOW, null, null, xyTarget.offset(-50, 30), null));
		displayDefinition.texts.add(new AAText(strikeText, Action.create, strikeText, textFontName, 14, FontWeightEnum.bold, ColorEnum.YELLOW, null, null, xyStrike.offset(30, 0), null));

		displayDefinition.backgroundImageFileURL = backgroundImageFileURL;
		displayDefinition.backgroundWidth =  970;
		displayDefinition.backgroundHeight = 863;
	}

	/**
	 * Creates the display data, i.e. the data for the objects in the display that
	 * are dynamic in that that their positions in the display continually change.
	 */
	@Override
	protected void createDisplayData()
	{
		displayData.images.add(new AAImage(uidTarget, Action.update, null, null, null, null, toXYData((PointGeospatial)constraintParams.get(uidTargetPosition)), null));
		displayData.images.add(new AAImage(uidStrike, Action.update, null, null, null, null, toXYData((PointGeospatial)constraintParams.get(uidStrikePosition)), null));
	}
}
