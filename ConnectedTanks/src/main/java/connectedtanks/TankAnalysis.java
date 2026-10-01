package connectedtanks;

import static sysmlinjava.attributetypes.AccelerationMetersPerSecondPerSecond.gravity;

import java.util.List;
import java.util.Optional;

import connectedtanks.PipeAnalysis.ComponentConstraintFunction;
import sysmlinjava.analysis.ParametricAnalysisCase;
import sysmlinjava.annotations.SysMLDocumentation;
import sysmlinjava.attributetypes.AreaMetersSquare;
import sysmlinjava.attributetypes.BBoolean;
import sysmlinjava.attributetypes.DensityKilogramsPerMeterCubic;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.PressureNewtonsPerMeterSquare;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.VolumeFlowMetersCubicPerSecond;
import sysmlinjava.constraint.SysMLConstraint;
import sysmlinjava.constraint.SysMLConstraintFunction;
import sysmlinjava.javaannotations.analysis.AnalysisResult;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.javaannotations.annotations.Documentation;
import sysmlinjava.javaannotations.constraint.Constraint;
import sysmlinjava.javaannotations.constraint.ConstraintFunction;

/**
 * Parameteric analysis of the {@code Tank} of the {@code ConnectedTanks}
 * system. {@code TankAnalysis} includes parameters for dynamic and static
 * values of the tank and specifies a set of constraints on the parameters as
 * specified in the OMG document from which this example SysMLinJava model was
 * derived, i.e. "SysML Extension for Physical Interaction and Signal Flow
 * Simulation", Object Management Group, Inc., 2018.
 * 
 * @author ModelerOne
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 */
public class TankAnalysis extends ParametricAnalysisCase
{
	/**
	 * Function of the constraint on the tank
	 */
	@ConstraintFunction
	protected SysMLConstraintFunction tankConstraintFunction;
	/**
	 * Text of the constraint on the tank
	 */
	@Documentation
	protected SysMLDocumentation tankConstraintText;
	/**
	 * Constraint on the tank
	 */
	@Constraint
	protected SysMLConstraint tankConstraint;
	/**
	 * Result of the constraint application, i.e. whether the constraint is
	 * satisfied
	 */
	@AnalysisResult
	protected BBoolean result;

	/**
	 * Pressure at the tank opening to the tank
	 */
	@Parameter
	public PressureNewtonsPerMeterSquare pressure;
	/**
	 * Rate of flow of the fluid out of the tank
	 */
	@Parameter
	public VolumeFlowMetersCubicPerSecond fluidFlow;
	/**
	 * Height of the fluid in the tank
	 */
	@Parameter
	public DistanceMeters fluidHeight;
	/**
	 * Density of the fluid in the tank
	 */
	@Parameter
	public DensityKilogramsPerMeterCubic fluidDensity;
	/**
	 * Surface area of the fluid in the tank
	 */
	@Parameter
	public AreaMetersSquare surfaceArea;
	/**
	 * Time interval between steps in the model execution/simulation
	 */
	@Parameter
	public DurationSeconds deltaTime;

	/**
	 * Constructor
	 * @param fluidReservoir1
	 */
	public TankAnalysis(String name)
	{
		super(Optional.empty(), name, 0L);
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		TankParams paramEnum = TankParams.valueOf(paramID);
		if (paramEnum != null)
			switch (paramEnum)
			{
			case fluidDensity:
				fluidDensity.value = ((DensityKilogramsPerMeterCubic) paramValue).value;
				break;
			case fluidFlow:
				fluidFlow.value = ((VolumeFlowMetersCubicPerSecond) paramValue).value;
				break;
			case fluidHeight:
				fluidHeight.value = ((DistanceMeters) paramValue).value;
				break;
			case pressure:
				pressure.value = ((PressureNewtonsPerMeterSquare) paramValue).value;
				break;
			case surfaceArea:
				surfaceArea.value = ((AreaMetersSquare) paramValue).value;
				break;
			default:
				break;
			}
		else
			logger.severe("unrecognized parameter ID for parameter to be retrieved: " + paramID);
	}

	@Override
	public void perform()
	{
		// Constraints are applied only if/when the "last" parameter (tank pressure)
		// is received which is when all parameters have been updated to their
		// next/updated value.
		if (currentParamID.isPresent() && currentParamID.get().equals(TankParams.pressure.toString()))
		{
			result.setValue(((ComponentConstraintFunction) tankConstraint.function.get()).apply());
			logger.info(String.format("tank: %s fluidHeight=%2.5f pressure=%4.4f", name.get(), fluidHeight.value, pressure.value));
		}
	}

	/**
	 * No state machine as tanks analysis is synchronous with model execution
	 */
	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.empty();
	}

	@Override
	protected void createParameters()
	{
		/**
		 * All params initialized to zero when first created. Will be set to analysis
		 * values by {@code updateBoundParam()} method when invoked by binding
		 * connectors
		 */
		pressure = new PressureNewtonsPerMeterSquare(0);
		fluidFlow = new VolumeFlowMetersCubicPerSecond(0);
		fluidHeight = new DistanceMeters(0);
		fluidDensity = new DensityKilogramsPerMeterCubic(0);
		surfaceArea = new AreaMetersSquare(0);
		deltaTime = new DurationSeconds(1.0);
	}

	@Override
	protected void createConstraintFunctions()
	{
		tankConstraintFunction = (ComponentConstraintFunction) () ->
		{
			fluidHeight.value = fluidHeight.value - ((fluidFlow.value * deltaTime.value) / surfaceArea.value);
			pressure.value = gravity.value * fluidHeight.value * fluidDensity.value;
			return BBoolean.True;
		};
	}

	@Override
	protected void createDocumentations()
	{
		super.createDocumentations();
		tankConstraintText = new SysMLDocumentation("""
			fluidHeight.value = fluidHeight.value - ((fluidFlow.value * deltaTime.value) / surfaceArea.value);
			pressure.value = gravity.value * fluidHeight.value * fluidDensity.value;
		""");
	}

	@Override
	protected void createConstraints()
	{
		tankConstraint = new SysMLConstraint(Optional.of(tankConstraintFunction), tankConstraintText, "Tank constraint", 0L);
	}

	@Override
	protected void createResult()
	{
		result = new BBoolean(false);
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(Tank.class);
	}

	@Override
	protected void createObjective()
	{
		objective = ConnectedTanksSystemAnalysisRequirements.tankAnalysisObjective;
	}

	@Override
	protected void createActors()
	{
		actors = List.of(Pipe.class);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("TankConstraint [name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append(", pressure=");
		builder.append(pressure);
		builder.append(", fluidFlow=");
		builder.append(fluidFlow);
		builder.append(", fluidHeight=");
		builder.append(fluidHeight);
		builder.append(", fluidDensity=");
		builder.append(fluidDensity);
		builder.append(", surfaceArea=");
		builder.append(surfaceArea);
		builder.append("]");
		return builder.toString();
	}
}