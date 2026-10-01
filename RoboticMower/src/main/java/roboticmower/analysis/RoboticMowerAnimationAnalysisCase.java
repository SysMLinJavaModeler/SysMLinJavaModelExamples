package roboticmower.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import roboticmower.RoboticMowerDomain;
import sysmlinjava.actions.SysMLCalculationFunction;
import sysmlinjava.attributetypes.DirectionRadians;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.Polyline2D;
import sysmlinjava.attributetypes.SpeedMetersPerSecond;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.Vector2D;
import sysmlinjava.attributetypes.VelocityMetersPerSecondRadians;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.animatedareadisplay.AAImage;
import sysmlinjava.views.animatedareadisplay.AALine;
import sysmlinjava.views.animatedareadisplay.AAText;
import sysmlinjava.views.animatedareadisplay.AAText.FontWeightEnum;
import sysmlinjava.views.animatedareadisplay.AnimatedAreaActionEnum;
import sysmlinjava.views.animatedareadisplay.AnimatedAreaAnalysisCase;
import sysmlinjava.views.animatedareadisplay.AnimatedAreaDisplayData;
import sysmlinjava.views.common.CartesianCoordinateTransform;
import sysmlinjava.views.common.ColorEnum;
import sysmlinjava.views.common.XY;

/**
 * Analysis case to produce an area display of an executing model of the
 * {@code RoboticMower} system. The analysis is an extension of the
 * {@code AreaDisplayAnalysisCase} and the {@code ParametricAnalysisCase} which
 * perform much of the retrieval/updating/conversion of the analysis parameters
 * as well as their transmission of the display data to the area display.
 * <p>
 * The parametric analysis uses parameters that represent the following:
 * <ul>
 * <li>Position controller's position</li>
 * <li>Position vector from controller to mower</li>
 * <li>Mower's position</li>
 * <li>Mower's velocity</li>
 * </ul>
 * The analysis uses the position controller and mower parameters to update a
 * graphical display of the two objects' positions and movements for an animated
 * depiction of a single scenario of execution of the system-of-systems.
 * <p>
 * The {@code  RoboticMowerAnimationAnalysisCase}, along with the
 * {@code RoboticMower} model demonstrate how a SysMLinJava model can be
 * graphically visualized in real time. Along with the use of other SysMLinJava
 * displays, such as the Sequence Diagram, State Transition, Timing Diagram, and
 * Line, Bar, and Scatter Chart displays, the Animated Area Display can assist
 * in extensive and highly precise analysis of SysML models - well beyond the
 * more limited analyses that can be performed with the traditional
 * "boxology"-based models.
 * 
 * @author ModelerOne
 */
public class RoboticMowerAnimationAnalysisCase extends AnimatedAreaAnalysisCase
{
	/**
	 * Title of the display
	 */
	static final String title = " RoboticMower System Execution Scenario 1";
	/**
	 * ID for the mower in the display
	 */
	public static final String uidMower = "mower";
	/**
	 * ID for the position controller in the display
	 */
	public static final String uidPositionController = "positionController";
	/**
	 * ID for the mower vector in the display
	 */
	public static final String uidMowerVector = "mowerVector";
	/**
	 * ID for the position of the mower
	 */
	public static final String uidMowerPosition = "mowerPosition";
	/**
	 * ID for the position of the position controller
	 */
	public static final String uidPositionControllerPosition = "positionControllerPosition";
	/**
	 * ID for the velocity of the mower
	 */
	public static final String uidMowerVelocity = "mowerVelocity";
	/**
	 * ID for the velocity of the mower
	 */
	public static final String uidMowerVectorValue = "mowerVectorValue";

	/**
	 * Text label for the position controller
	 */
	static final String positionControllerText = "Position\nController";
	/**
	 * Text label for the mower
	 */
	static final String mowerText = "Mower";
	/**
	 * Text for the name of the font used in the display
	 */
	static final String textFontName = "Arial";

	/**
	 * Number of pixels along the width of the lawn image used as background
	 */
	private static final double lawnImageWidthPixels = 1000;
	/**
	 * Number of pixels along the height of the lawn image used as background
	 */
	private static final double lawnImageHeightPixels = lawnImageWidthPixels;
	/**
	 * URL for the image file for the display background
	 */
	static String backgroundImageFileURL;
	/**
	 * URL for the image file for the mower
	 */
	static String mowerImageFileURL;
	/**
	 * URL for the image file for the position controller
	 */
	static String positionControllerImageFileURL;
	/**
	 * Transform for converting Point2D position to XY pixel
	 */
	static CartesianCoordinateTransform transform;

	/**
	 * Initialization of the URLs for each of the image files used in the display
	 */
	static
	{
		backgroundImageFileURL = RoboticMowerAnimationAnalysisCase.class.getResource("lawn3.png").toString();
		positionControllerImageFileURL = RoboticMowerAnimationAnalysisCase.class.getResource("positionController.png").toString();
		mowerImageFileURL = RoboticMowerAnimationAnalysisCase.class.getResource("mower.png").toString();
		transform = new CartesianCoordinateTransform(Optional.empty(), Optional.of(100.0), Optional.of(1000.0));
	}

	@Attribute
	Point2D initialPositionControllerPosition;

	@Attribute
	Point2D initialMowerPosition;
	
	@Attribute
	VelocityMetersPerSecondRadians initialMowerVelocity;
	
	@Attribute
	Vector2D initialMowerVector;
	
	
	/**
	 * Constraint parameter for the mower's current position
	 */
	@Parameter
	Point2D mowerPosition;

	/**
	 * Constraint parameter for the position controller's current position
	 */
	@Parameter
	Point2D positionControllerPosition;
	/**
	 * Constraint parameter for the mower's current velocity
	 */
	@Parameter
	VelocityMetersPerSecondRadians mowerVelocity;
	/**
	 * Constraint parameter for the mower's current vector from position controller
	 */
	@Parameter
	Vector2D mowerVector;

	/**
	 * Constructor
	 * 
	 * @param udpPort UDP port of the area display to which display data is to be
	 *                sent
	 */
	public RoboticMowerAnimationAnalysisCase(int udpPort)
	{
		super(udpPort, false);
		paramIDAObjectIDMap = new HashMap<>();
		paramIDAObjectIDMap.put(uidPositionControllerPosition, uidPositionController);
		paramIDAObjectIDMap.put(uidMowerPosition, uidMower);
		paramIDAObjectIDMap.put(uidMowerVelocity, uidMower);
		paramIDAObjectIDMap.put(uidMowerVectorValue, uidMowerVector);
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType currentParam)
	{
		switch (paramID)
		{
		case uidPositionControllerPosition:
			positionControllerPosition.setValue((Point2D) currentParam);
			break;
		case uidMowerPosition:
			mowerPosition.setValue((Point2D) currentParam);
			break;
		case uidMowerVelocity:
			mowerVelocity.setValue((VelocityMetersPerSecondRadians) currentParam);
			break;
		case uidMowerVectorValue:
			mowerVector.setValue((Vector2D) currentParam);
			break;
		default:
			break;
		}
	}

	@Override
	protected void createAttributes()
	{
		initialPositionControllerPosition = new Point2D(9.5, 5);
		initialMowerPosition = new Point2D(0.5, 0);
		initialMowerVelocity = new VelocityMetersPerSecondRadians(SpeedMetersPerSecond.zero, DirectionRadians.north);
		initialMowerVector = new Vector2D(Math.sqrt(9 * 9 + 4 * 4), 1.5 * Math.PI - Math.atan(4.0 / 9.0));
	}

	@Override
	protected void createParameters()
	{
		positionControllerPosition = new Point2D(initialPositionControllerPosition);
		mowerPosition = new Point2D(initialMowerPosition);
		mowerVelocity = new VelocityMetersPerSecondRadians(initialMowerVelocity);
		mowerVector = new Vector2D(initialMowerVector);

		params.put(uidPositionControllerPosition, positionControllerPosition);
		params.put(uidMowerPosition, mowerPosition);
		params.put(uidMowerVelocity, mowerVelocity);
		params.put(uidMowerVectorValue, mowerVector);
	}

	@Override
	protected void createObjective()
	{
		objective = RoboticMowerSystemAnalysisRequirements.animationAnalysisObjective;
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(RoboticMowerDomain.class);
	}

	@Override
	protected void createActors()
	{
		actors = List.of();
	}

	/**
	 * Creates the display data, i.e. the data for the objects in the display that
	 * are dynamic in that that their position, scale, and/or rotation in the
	 * display will change.
	 */
	@Override
	protected void createResult()
	{
		XY xyMower = transform.xyValueOf(initialMowerPosition);
		XY xyPositionController = transform.xyValueOf(initialPositionControllerPosition);
		ArrayList<XY> xyVector = new ArrayList<XY>(List.of(xyPositionController, xyMower));

		List<AAText> texts = List.of(new AAText(mowerText, AnimatedAreaActionEnum.update, null, null, null, null, null, null, null, xyMower.offset(80, 0), null));
		List<AAImage> images = List.of(
			new AAImage(uidPositionController, AnimatedAreaActionEnum.update, null, null, null, null, transform.xyValueOf(initialPositionControllerPosition), null),
			new AAImage(uidMower, AnimatedAreaActionEnum.update, null, null, null, null, transform.xyValueOf(initialMowerPosition), null));
		List<AALine> lines = List.of(new AALine(uidMowerVector, AnimatedAreaActionEnum.update, xyVector, null, null, null));
		
		displayData = new AnimatedAreaDisplayData(new ArrayList<>(texts), new ArrayList<>(images), new ArrayList<>(lines));
	}

	/**
	 * Creation of the analysis action that performs the analysis on current
	 * parameter values. This function is invoked by the inherited {@code perform()}
	 * method. The action is as follows:
	 * <ul>
	 * <li>If there was a previous parameter changed, then clear out its display
	 * update information in case it is not used for this next update</li>
	 * <li>If there is a current parameter changed, then update its display data
	 * accordingly</li>
	 * </ul>
	 * The updated display data will then be transmitted to the graph display by the
	 * inherited {@code perform()} method after it invokes this function.
	 * 
	 * @see sysmlinjava.views.animatedareadisplay.AnimatedAreaAnalysisCase#perform()
	 */
	@Override
	protected void createFunction()
	{
		function = (SysMLCalculationFunction) () ->
		{
			if (previousParamID.isPresent())
			{
				if (previousParam instanceof Point2D)
					displayData.updateImage(previousParamID.get(), AnimatedAreaActionEnum.none, null, null, null, null, null, null);
				if (previousParam instanceof Vector2D)
					displayData.updateLine(previousParamID.get(), AnimatedAreaActionEnum.none, null, null, null, null);
				else if (previousParam instanceof VelocityMetersPerSecondRadians)
				{
					String aimageID = paramIDAObjectIDMap.get(previousParamID.get());
					displayData.updateImage(aimageID, AnimatedAreaActionEnum.none, null, null, null, null, null, null);
				}
			}

			if (currentParamID.isPresent())
			{
				switch (currentParamID.get())
				{
				case uidPositionControllerPosition:
				{
					String objectID = paramIDAObjectIDMap.get(uidPositionControllerPosition);
					XY xyPosition = transform.xyValueOf(positionControllerPosition);
					displayData.updateImage(objectID, AnimatedAreaActionEnum.update, null, null, null, null, xyPosition, null);
					break;
				}
				case uidMowerPosition:
				{
					String objectID = paramIDAObjectIDMap.get(uidMowerPosition);
					XY xyPosition = transform.xyValueOf(mowerPosition);
					displayData.updateImage(objectID, AnimatedAreaActionEnum.update, null, null, null, null, xyPosition, null);
					displayData.updateText(mowerText, AnimatedAreaActionEnum.update, null, null, null, null, null, null, null, xyPosition.offset(70, 0), null);
					break;
				}
				case uidMowerVelocity:
				{
					String objectID = paramIDAObjectIDMap.get(uidMowerVelocity);
					displayData.updateImage(objectID, AnimatedAreaActionEnum.update, null, null, null, (int) Math.toDegrees(mowerVelocity.heading.value), null, null);
					break;
				}
				case uidMowerVectorValue:
				{
					String objectID = paramIDAObjectIDMap.get(uidMowerVectorValue);
					Point2D mowerPoint = positionControllerPosition.moved(mowerVector.value, mowerVector.direction.value);
					Point2D[] vectorEndPoints = { positionControllerPosition, mowerPoint };
					Polyline2D polyline = new Polyline2D(vectorEndPoints);
					displayData.updateLine(objectID, AnimatedAreaActionEnum.update, transform.xyListValueOf(polyline), null, null, null);
					displayData.updateImage(uidPositionController, AnimatedAreaActionEnum.update, null, null, null, (int) Math.toDegrees(mowerVector.direction.value), null, null);
					break;
				}
				default:
					logger.severe("unrecognized constraint param ID: " + currentParamID.get());
				}
			}
		};
	}

	/**
	 * Creates the display definition, i.e. the display data for the initial and
	 * non-changing objects in the display, e.g. the background image and the position controller
	 * and mower images and text, and the mower vector line.
	 */
	@Override
	protected void createDisplayDefinition()
	{
		displayDefinition.title = title;

		displayDefinition.backgroundImageFileURL = backgroundImageFileURL;
		displayDefinition.backgroundWidth = (int) lawnImageWidthPixels;
		displayDefinition.backgroundHeight = (int) lawnImageHeightPixels;

		XY xyMower = transform.xyValueOf(mowerPosition);
		XY xyPositionController = transform.xyValueOf(positionControllerPosition);
		ArrayList<XY> xyVector = new ArrayList<XY>(List.of(xyPositionController, xyMower));
		Vector2D vector2D = new Vector2D(new Point2D(xyPositionController.xValue, xyPositionController.yValue), new Point2D(xyMower.xValue, xyMower.yValue));
		displayDefinition.lines.add(new AALine(uidMowerVector, AnimatedAreaActionEnum.create, xyVector, ColorEnum.YELLOW, 2, 4));

		displayDefinition.images.add(new AAImage(uidMower, AnimatedAreaActionEnum.create, mowerImageFileURL, 110, 132, 0, xyMower, 4));
		displayDefinition.images.add(new AAImage(uidPositionController, AnimatedAreaActionEnum.create, positionControllerImageFileURL, 30, 30, (int) Math.toDegrees(vector2D.direction.value),
		xyPositionController, 4));

		displayDefinition.texts.add(new AAText(positionControllerText, AnimatedAreaActionEnum.create, positionControllerText, textFontName, 22, FontWeightEnum.bold, ColorEnum.WHITE, null, null,
		xyPositionController.offset(-50, -50), 4));
		displayDefinition.texts.add(new AAText(mowerText, AnimatedAreaActionEnum.create, mowerText, textFontName, 22, FontWeightEnum.bold, ColorEnum.WHITE, null, null, xyMower.offset(70, 0), 4));
	}
}
