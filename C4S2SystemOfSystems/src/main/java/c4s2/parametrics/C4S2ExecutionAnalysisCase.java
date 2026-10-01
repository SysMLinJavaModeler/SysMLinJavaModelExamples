package c4s2.parametrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import c4s2.common.attributetypes.TargetStatusEnum;
import c4s2.requirements.C4S2SystemOfSystemAnalysisRequirements;
import c4s2.systems.c4s2.C4S2System;
import c4s2.systems.target.VehicleArmoredLargeTarget;
import sysmlinjava.actions.SysMLCalculationFunction;
import sysmlinjava.attributetypes.DirectionRadians;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.LatitudeDegrees;
import sysmlinjava.attributetypes.LongitudeDegrees;
import sysmlinjava.attributetypes.LongitudeRadians;
import sysmlinjava.attributetypes.PointGeospatial;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.attributetypes.SpeedMetersPerSecond;
import sysmlinjava.attributetypes.VelocityMetersPerSecondRadians;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.animatedareadisplay.AAImage;
import sysmlinjava.views.animatedareadisplay.AALine;
import sysmlinjava.views.animatedareadisplay.AAText;
import sysmlinjava.views.animatedareadisplay.AAText.FontWeightEnum;
import sysmlinjava.views.animatedareadisplay.AnimatedAreaActionEnum;
import sysmlinjava.views.animatedareadisplay.AnimatedAreaDisplayData;
import sysmlinjava.views.animatedareadisplay.AnimatedAreaGeospatialAnalysisCase;
import sysmlinjava.views.common.ColorEnum;
import sysmlinjava.views.common.GeospatialCoordinateTransform;
import sysmlinjava.views.common.XY;

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
 * The {@code C4S2ExecutionAnalysisCase}, along with the
 * {@code C4A2SystemOfSystems} model demonstrate how a SysMLinJava model can be
 * graphically visualized in real time. Along with the use of other SysMLinJava
 * displays, such as the Sequence Diagarm, State Transition, Timing Diagram, and
 * Line, Bar, and Scatter Chart displays, the Area Display can assist in
 * extensive and highly precise analysis of SysML models - well beyond the more
 * limited analyses that can be performed with the traditional "boxology"-based
 * models.
 * 
 * @author ModelerOne
 */
public class C4S2ExecutionAnalysisCase extends AnimatedAreaGeospatialAnalysisCase
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
	public static final String uidScanArea = "scanArea";
	/**
	 * ID for the radar text in the display
	 */
	public static final String uidRadarText = "radarText";
	/**
	 * ID for the target text in the display
	 */
	public static final String uidTargetText = "targetText";
	/**
	 * ID for the strike system text in the display
	 */
	public static final String uidStrikeText = "strikeText";
	/**
	 * ID for the radar scan area text in the display
	 */
	public static final String uidScanAreaText = "scanAreaText";

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
	 * Text label for the radar scan area
	 */
	public static final String scanAreaText = "Radar Scan Area";
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
	public static final String textFontName = "Arial";

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
		backgroundImageFileURL = C4S2ExecutionAnalysisCase.class.getResource("landImage.PNG").toString();
		radarImageFileURL = C4S2ExecutionAnalysisCase.class.getResource("radar.png").toString();
		strikeImageFileURL = C4S2ExecutionAnalysisCase.class.getResource("strike.png").toString();
		targetImageFileURLOperating = C4S2ExecutionAnalysisCase.class.getResource("targetOperating.png").toString();
		targetImagFileURLExploding = C4S2ExecutionAnalysisCase.class.getResource("targetExploding.png").toString();
		targetImagFileURLDestroyed = C4S2ExecutionAnalysisCase.class.getResource("targetDestroyed.png").toString();
	}
	
	/**
	 * Initial position of target
	 */
	@Attribute
	private PointGeospatial initialTargetPosition;

	/**
	 * Initial position of strike system
	 */
	@Attribute
	private PointGeospatial initialStrikePosition;

	/**
	 * Constraint parameter for the current target position
	 */
	@Parameter
	PointGeospatial targetPosition;

	/**
	 * Constraint parameter for the current strike vehicle position
	 */
	@Parameter
	PointGeospatial strikePosition;

	/**
	 * Constraint parameter for the current target state
	 */
	@Parameter
	TargetStatusEnum targetState;

	/**
	 * Constraint parameter for the current strike vehicle velocity
	 */
	@Parameter
	VelocityMetersPerSecondRadians strikeVelocity;

	/**
	 * Value for the geo-spatial location of the center of the radar scan area
	 */
	@Attribute
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
	public C4S2ExecutionAnalysisCase(int udpPort)
	{
		super(udpPort, false);
		paramIDAObjectIDMap = new HashMap<>();
		paramIDAObjectIDMap.put(uidTargetPosition, uidTarget);
		paramIDAObjectIDMap.put(uidStrikePosition, uidStrike);
		paramIDAObjectIDMap.put(uidTargetVelocity, uidTarget);
		paramIDAObjectIDMap.put(uidStrikeVelocity, uidStrike);
		paramIDAObjectIDMap.put(uidTargetState, uidTarget);
	}

	@Override
	protected void createAttributes()
	{
		scanCenterPosition = new PointGeospatial(new LatitudeDegrees(45), new LongitudeDegrees(105));
		initialTargetPosition = scanCenterPosition.movedTo(DirectionRadians.east, new DistanceMeters(850));
		initialStrikePosition = scanCenterPosition.movedTo(DirectionRadians.south, new DistanceMeters(1100));
		
		RReal pointsCartesianPerKilometer = new RReal(975 / 1.8);
		RReal kilometerPerRadianLon = new RReal(LongitudeRadians.kilometersPerRadianLongitude(scanCenterPosition.latitude.value).value);

		RReal kilometerPerRadianLat = GeospatialCoordinateTransform.kilometerPerRadianLat;
		double upperLeftLat = scanCenterPosition.latitude.value + 0.25 / kilometerPerRadianLat.value;
		double upperLeftLon = scanCenterPosition.longitude.value + 0.90 / kilometerPerRadianLon.value;
		PointGeospatial geospatialUpperLeft = new PointGeospatial(upperLeftLat, upperLeftLon);

		RReal pointsPerRadianLatitude = new RReal(pointsCartesianPerKilometer.value * kilometerPerRadianLat.value);
		RReal pointsPerRadianLongitude = new RReal(pointsCartesianPerKilometer.value * kilometerPerRadianLon.value);

		transform = new GeospatialCoordinateTransform(geospatialUpperLeft, pointsPerRadianLatitude, pointsPerRadianLongitude, kilometerPerRadianLon, pointsCartesianPerKilometer);
	}

	@Override
	protected void createParameters()
	{
		targetPosition = new PointGeospatial(initialStrikePosition);
		strikePosition = new PointGeospatial(initialStrikePosition);
		targetState = new TargetStatusEnum(TargetStatusEnum.operating);
		strikeVelocity = new VelocityMetersPerSecondRadians(SpeedMetersPerSecond.zero, DirectionRadians.north);

		params.put(uidTargetPosition, targetPosition);
		params.put(uidStrikePosition, strikePosition);
		params.put(uidTargetState, targetState);
		params.put(uidStrikeVelocity, strikeVelocity);
	}

	/**
	 * Creation of analysis action that calculates and updates the area display from
	 * the analysis parameter values. The analysis function is invoked everytime a
	 * parameter port obtains a new value of a bound parameter and updates one or
	 * more of the analysis parameters accordingly. Performs the analysis as
	 * follows:
	 * <ul>
	 * <li>If a previously updated analysis parameter (last one changed) exists,
	 * nullify the display data for the previous parameter to indicate no changes to
	 * make to the parameter's display.</li>
	 * <li>If a new current analysis parameter (next one changed) exists, update the
	 * parameter's corresponding display data accoringly</li>
	 * </ul>
	 */
	@Override
	protected void createFunction()
	{
		function = (SysMLCalculationFunction) () ->
		{
			if (previousParamID.isPresent())
				if (previousParam instanceof PointGeospatial)
					displayData.updateImage(previousParamID.get(), AnimatedAreaActionEnum.none, null, null, null, null, null, null);
				else if (previousParam instanceof VelocityMetersPerSecondRadians)
				{
					String aimageID = paramIDAObjectIDMap.get(previousParamID.get());
					displayData.updateImage(aimageID, AnimatedAreaActionEnum.none, null, null, null, null, null, null);
				}
				else if (previousParam instanceof TargetStatusEnum)
				{
					String aimageID = paramIDAObjectIDMap.get(previousParamID.get());
					displayData.updateImage(aimageID, AnimatedAreaActionEnum.none, null, null, null, null, null, null);
				}

			if (currentParamID.isPresent())
				switch (currentParamID.get())
				{
				case uidTargetPosition:
				{
					String aimageID = paramIDAObjectIDMap.get(uidTargetPosition);
					if (currentParam instanceof PointGeospatial nextPoint)
					{
						displayData.updateImage(aimageID, AnimatedAreaActionEnum.update, null, null, null, null, transform.toXYData(nextPoint), null);
						displayData.updateText(uidTargetText, AnimatedAreaActionEnum.update, null, null, null, null, null, null, null, transform.toXYData(nextPoint).offset(-50, -50), null);
					}
					else
						logger.warning("unrecognized type for analysis parameter: " + uidTargetPosition);
					break;
				}
				case uidStrikePosition:
				{
					String aimageID = paramIDAObjectIDMap.get(uidStrikePosition);
					if (currentParam instanceof PointGeospatial nextPoint)
					{
						displayData.updateImage(aimageID, AnimatedAreaActionEnum.update, null, null, null, null, transform.toXYData(nextPoint), null);
						displayData.updateText(uidStrikeText, AnimatedAreaActionEnum.update, null, null, null, null, null, null, null, transform.toXYData(nextPoint).offset(35, -10), null);
					}
					else
						logger.warning("unrecognized type for analysis parameter: " + uidStrikePosition);
					break;
				}
				case uidTargetVelocity:
				{
					String aimageID = paramIDAObjectIDMap.get(uidTargetVelocity);
					if (currentParam instanceof VelocityMetersPerSecondRadians velocity)
						displayData.updateImage(aimageID, AnimatedAreaActionEnum.update, null, null, null, (int) Math.toDegrees(velocity.heading.value), null, null);
					else
						logger.warning("unrecognized type for analysis parameter: " + uidTargetVelocity);
					break;
				}
				case uidStrikeVelocity:
				{
					String aimageID = paramIDAObjectIDMap.get(uidStrikeVelocity);
					if (currentParam instanceof VelocityMetersPerSecondRadians velocity)
						displayData.updateImage(aimageID, AnimatedAreaActionEnum.update, null, null, null, (int) Math.toDegrees(velocity.heading.value), null, null);
					else
						logger.warning("unrecognized type for analysis parameter: " + uidStrikeVelocity);
					break;
				}
				case uidTargetState:
				{
					String aimageID = paramIDAObjectIDMap.get(uidTargetState);
					if (currentParam instanceof TargetStatusEnum targetState)
					{
						if (targetState.equals(TargetStatusEnum.exploding))
							displayData.updateImage(aimageID, AnimatedAreaActionEnum.update, targetImagFileURLExploding, 60, 30, null, null, null);
						else if (targetState.equals(TargetStatusEnum.destroyed))
							displayData.updateImage(aimageID, AnimatedAreaActionEnum.update, targetImagFileURLDestroyed, 60, 30, null, null, null);
					}
					else
						logger.warning("unrecognized type for analysis parameter: " + uidTargetState);
					break;
				}
				default:
					break;
				}
		};
	}

	/**
	 * Creates the objective of the analysis case, i.e. to display an animated
	 * display of the executing model of the {@code C4S2Ssystem} radar and strike
	 * systems (subject) and the {@code VehicleArmoredLarge} target (actor).
	 */

	@Override
	protected void createObjective()
	{
		objective = C4S2SystemOfSystemAnalysisRequirements.c4s2ExecutionAnalysisObjective;
	}

	/**
	 * Creates the type of the subject of the analysis case, i.e. the
	 * {@code C4S2Systam} of the {@code C4S2Domain} model.
	 */
	@Override
	protected void createSubject()
	{
		subject = Optional.of(C4S2System.class);
	}

	/**
	 * Creates the types of actors of the analysis case, i.e. the
	 * {@code VehicleArmoredLarge} target of the {@code C4S2Domain} model
	 */
	@Override
	protected void createActors()
	{
		actors = List.of(VehicleArmoredLargeTarget.class);
	}

	/**
	 * Creates the result of the analysis case, i.e. the display data for the
	 * display of the objects in the model animation. These results are dynamic in
	 * that the positions of the objects continually change.
	 */
	@Override
	protected void createResult()
	{
		XY xyTarget = transform.toXYData(initialTargetPosition);
		XY xyStrike = transform.toXYData(initialStrikePosition);

		ArrayList<AAImage> images = new ArrayList<>(List.of(
			new AAImage(uidTarget, AnimatedAreaActionEnum.update, null, null, null, null, xyTarget, null),
			new AAImage(uidStrike, AnimatedAreaActionEnum.update, null, null, null, null, xyStrike, null)));
		ArrayList<AAText> texts = new ArrayList<>(List.of(
			new AAText(uidTargetText, AnimatedAreaActionEnum.update, null, null, null, null, null, null, null, xyTarget.offset(-50, -50), null),
			new AAText(uidStrikeText, AnimatedAreaActionEnum.update, null, null, null, null, null, null, null, xyStrike.offset(35, -10), null)));
		ArrayList<AALine> lines = new ArrayList<>(List.of());
		
		displayData = new AnimatedAreaDisplayData(texts, images, lines);
	}

	/**
	 * Creates the display definition, i.e. the display data for the initial and
	 * non-changing objects in the display, e.g. the background image and the radar
	 * image, radar scan area (fan) line, and target and strike system images. Note
	 * that as a geospatial model, all geospatial locations of objects must be
	 * converted into X,Y positions for the area display. Hence the use of the
	 * {@code transform} to convert geospatial coordinates into X,Y pixel
	 * coordinates.
	 */
	@Override
	protected void createDisplayDefinition()
	{
		displayDefinition.title = title;
		
		displayDefinition.backgroundImageFileURL = backgroundImageFileURL;
		displayDefinition.backgroundWidth = 970;
		displayDefinition.backgroundHeight = 863;

		PointGeospatial radarPointGeo = scanCenterPosition.movedTo(DirectionRadians.south, new DistanceMeters(1000));
		XY xyRadar = transform.toXYData(radarPointGeo);

		XY xyFanNW = transform.toXYData(radarPointGeo.movedTo(new DirectionRadians(Math.toRadians(-22.5)), new DistanceMeters(1250)));
		XY xyFanNE = transform.toXYData(radarPointGeo.movedTo(new DirectionRadians(Math.toRadians(22.5)), new DistanceMeters(1250)));
		XY xyFanSW = transform.toXYData(radarPointGeo.movedTo(new DirectionRadians(Math.toRadians(-22.5)), new DistanceMeters(750)));
		XY xyFanSE = transform.toXYData(radarPointGeo.movedTo(new DirectionRadians(Math.toRadians(22.5)), new DistanceMeters(750)));

		XY xyTarget = transform.toXYData((PointGeospatial) params.get(uidTargetPosition));
		XY xyStrike = transform.toXYData((PointGeospatial) params.get(uidStrikePosition));

		ArrayList<XY> xyFan = new ArrayList<XY>(List.of(xyFanNW, xyFanNE, xyFanSE, xyFanSW, xyFanNW));
		displayDefinition.lines.add(new AALine(uidScanArea, AnimatedAreaActionEnum.create, xyFan, ColorEnum.DARKKHAKI, 2, 6));

		displayDefinition.images.add(new AAImage(uidRadar, AnimatedAreaActionEnum.create, radarImageFileURL, 100, 50, 0, xyRadar, 4));
		displayDefinition.images.add(new AAImage(uidTarget, AnimatedAreaActionEnum.create, targetImageFileURLOperating, 60, 30, 0, xyTarget, 4));
		displayDefinition.images.add(new AAImage(uidStrike, AnimatedAreaActionEnum.create, strikeImageFileURL, 150, 50, 0, xyStrike, 2));

		displayDefinition.texts.add(new AAText(uidRadarText, AnimatedAreaActionEnum.create, radarText, textFontName, 20, FontWeightEnum.bold, ColorEnum.YELLOW, null, null, xyRadar.offset(-60, -30),
		null));
		displayDefinition.texts.add(new AAText(uidScanAreaText, AnimatedAreaActionEnum.create, scanAreaText, textFontName, 20, FontWeightEnum.bold, ColorEnum.YELLOW, null, null,
		xyFanSE.offset(-220, -50), null));
		displayDefinition.texts.add(new AAText(uidTargetText, AnimatedAreaActionEnum.create, targetText, textFontName, 20, FontWeightEnum.bold, ColorEnum.YELLOW, null, null,
		xyTarget.offset(-50, -50), null));
		displayDefinition.texts.add(new AAText(uidStrikeText, AnimatedAreaActionEnum.create, strikeText, textFontName, 20, FontWeightEnum.bold, ColorEnum.YELLOW, null, null,
		xyStrike.offset(35, -10), null));

	}
}
