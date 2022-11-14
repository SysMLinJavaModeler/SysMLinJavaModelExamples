package connectedtanks;

import java.util.Optional;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.constraintblocks.SysMLConstraintBlock;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import static sysmlinjava.valuetypes.AccelerationMetersPerSecondPerSecond.gravity;
import sysmlinjava.valuetypes.AreaMetersSquare;
import sysmlinjava.valuetypes.DensityKilogramsPerMeterCubic;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.DurationSeconds;

/**
 * Constraint block for the parameteric analysis of the {@code Tank} of the
 * {@code ConnectedTanks} system. {@code TankConstraint} includes parameters for
 * dynamic and static values of the tank and specifies a set of constraints on
 * the parameters as specified in the OMG document from which this example
 * SysMLinJava model was derived, i.e. "SysML Extension for Physical Interaction
 * and Signal Flow Simulation", Object Management Group, Inc., 2018.
 * 
 * @author ModelerOne
 *
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 */public class TankConstraint extends SysMLConstraintBlock
{
	/**
	 * Pressure at the tank opening to the pipe
	 */
	@ConstraintParameter
	public Pressure pressure;
	/**
	 * Rate of flow of the fluid out of the tank
	 */
	@ConstraintParameter
	public VolumeFlowRate fluidFlow;
	/**
	 * Height of the fluid in the tank
	 */
	@ConstraintParameter
	public DistanceMeters fluidHeight;
	/**
	 * Density of the fluid in the tank
	 */
	@ConstraintParameter
	public DensityKilogramsPerMeterCubic fluidDensity;
	/**
	 * Surface area of the fluid in the tank
	 */
	@ConstraintParameter
	public AreaMetersSquare surfaceArea;
	/**
	 * Time interval between steps in the model execution/simulation
	 */
	@ConstraintParameter
	public DurationSeconds deltaTime;
			
	/**
	 * Function that performs the binding of the tank's pressure at opening to pipe to constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction pressurePortFunction;
	/**
	 * Function that performs the binding of the tank's fluid flow at opening to pipe to constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction fluidFlowPortFunction;
	/**
	 * Function that performs the binding of the tank's fluid level/height to constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction fluidLevelPortFunction;
	/**
	 * Function that performs the binding of the tank's fluid density to constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction fluidDensityPortFunction;
	/**
	 * Function that performs the binding of the tank's fluid surface area to constraint parameter
	 */
	@ConstraintParameterPortFunction
	SysMLConstraintParameterPortFunction surfaceAreaPortFunction;

	/**
	 * Port that executes the function that performs the binding of the tank's pressure to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort pressurePort;
	/**
	 * Port that executes the function that performs the binding of the tank's fluid flow to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort fluidFlowPort;
	/**
	 * Port that executes the function that performs the binding of the tank's fluid level to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort fluidLevelPort;
	/**
	 * Port that executes the function that performs the binding of the tank's fluid density to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort fluidDensityPort;
	/**
	 * Port that executes the function that performs the binding of the tank's surface area to constraint parameter
	 */
	@ConstraintParameterPort
	SysMLConstraintParameterPort surfaceAreaPort;

	/**
	 * Enumeration of the constraint parameters
	 * 
	 * @author ModelerOne
	 *
	 */
	enum TankParams {pressure, fluidFlow, fluidHeight, fluidDensity, surfaceArea};

	/**
	 * Constructor
	 */
	public TankConstraint()
	{
		super(Optional.empty(), "TankConstraint");
	}

	@Override
	protected void onParameterChange(String paramID)
	{
		if (!paramID.isEmpty())
		{
			previousParamID = currentParamID;
			currentParamID = Optional.of(paramID);
			SysMLConstraintParameterPort paramPort = paramPorts.get(currentParamID.get());
			if (paramPort != null)
			{
				if (currentParam != null)
					previousParam = currentParam;
				currentParam = paramPort.getValue();
				if (currentParam != null)
				{
					TankParams paramEnum = TankParams.valueOf(currentParamID.get());
					if(paramEnum != null)
						switch(paramEnum)
						{
						case fluidDensity:
							fluidDensity.value = ((DensityKilogramsPerMeterCubic)currentParam).value;
							logger.info(String.format("paramID=%s value=%f", paramID, fluidDensity.value));
							break;
						case fluidFlow:
							fluidFlow.value = ((VolumeFlowRate)currentParam).value;
							logger.info(String.format("paramID=%s value=%f", paramID, fluidFlow.value));
							break;
						case fluidHeight:
							fluidHeight.value = ((DistanceMeters)currentParam).value;
							logger.info(String.format("paramID=%s value=%f", paramID, fluidHeight.value));
							break;
						case pressure:
							pressure.value = ((Pressure)currentParam).value;
							logger.info(String.format("paramID=%s value=%f", paramID, pressure.value));
							break;
						case surfaceArea:
							surfaceArea.value = ((AreaMetersSquare)currentParam).value;
							logger.info(String.format("paramID=%s value=%f", paramID, surfaceArea.value));
							break;
						default:
							break;
						}
					else
						logger.severe("unrecognized parameter ID for parameter to be retrieved: " + paramID);
				}
				else
					logger.severe("bound parameter value not retrieved from its bound parameter port: " + paramID);
			}
			else
				logger.severe("bound parameter port not found for parameter: " + paramID);
		}
		else
			currentParamID = Optional.empty();
	}
	
	@Override
	protected void performConstraints()
	{
		// Constraints are applied only if/when the "last" parameter (tank pressure)
		// is received which is when all parameters have been updated to their
		// next/updated value.
		if(currentParamID.isPresent() && currentParamID.get().equals(TankParams.pressure.toString()))
		{
			constraint.apply();
			logger.info(String.format("tank: %s fluidHeight=%2.5f pressure=%4.4f", name.get(), fluidHeight.value, pressure.value));
		}
	}


	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.empty();
	}

	@Override
	protected void createConstraints()
	{
		// Constraints as specified in "SysML Extension for Physical Interaction and
		// Signal Flow Simulation", Object Management Group, Inc., 2018
		constraint = () ->
		{
			fluidHeight.value = fluidHeight.value - ((fluidFlow.value * deltaTime.value) / surfaceArea.value);
			pressure.value = gravity.value * fluidHeight.value * fluidDensity.value;
		};		
	}

	@Override
	protected void createConstraintParameters()
	{
		// All params initialized to zero when first created. Will be set to first
		// values for simulation by port constructors.
		pressure = new Pressure(0);
		fluidFlow = new VolumeFlowRate(0);
		fluidHeight = new DistanceMeters(0);
		fluidDensity = new DensityKilogramsPerMeterCubic(0);
		surfaceArea = new AreaMetersSquare(0);
		deltaTime = new DurationSeconds(1.0);
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		pressurePortFunction = (constraintParameterPort, contextBlock) ->
		{
			Pressure parameter = ((Tank)contextBlock).tankOpening.vf.p;
			constraintParameterPort.updateParameterValue(new Pressure(parameter));
		};
		fluidFlowPortFunction = (constraintParameterPort, contextBlock) ->
		{
			VolumeFlowRate parameter = ((Tank)contextBlock).tankOpening.vf.q;
			constraintParameterPort.updateParameterValue(new VolumeFlowRate(parameter));
		};
		fluidLevelPortFunction = (constraintParameterPort, contextBlock) ->
		{
			DistanceMeters parameter = ((Tank)contextBlock).fluidLevel;
			constraintParameterPort.updateParameterValue(new DistanceMeters(parameter));
		};
		fluidDensityPortFunction = (constraintParameterPort, contextBlock) ->
		{
			DensityKilogramsPerMeterCubic parameter = ((Tank)contextBlock).fluidDensity;
			constraintParameterPort.updateParameterValue(new DensityKilogramsPerMeterCubic(parameter));
		};
		surfaceAreaPortFunction = (constraintParameterPort, contextBlock) ->
		{
			AreaMetersSquare parameter = ((Tank)contextBlock).area;
			constraintParameterPort.updateParameterValue(new AreaMetersSquare(parameter));
		};
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		pressurePort = new SysMLConstraintParameterPort(this, pressurePortFunction, TankParams.pressure.toString());
		fluidFlowPort = new SysMLConstraintParameterPort(this, fluidFlowPortFunction, TankParams.fluidFlow.toString());
		fluidLevelPort = new SysMLConstraintParameterPort(this, fluidLevelPortFunction, TankParams.fluidHeight.toString());
		fluidDensityPort = new SysMLConstraintParameterPort(this, fluidDensityPortFunction, TankParams.fluidDensity.toString());
		surfaceAreaPort = new SysMLConstraintParameterPort(this, surfaceAreaPortFunction, TankParams.surfaceArea.toString());
		
		//Add each port to the map of ports for use by retrievParameters() operation
		paramPorts.put(TankParams.pressure.toString(), pressurePort);
		paramPorts.put(TankParams.fluidFlow.toString(), fluidFlowPort);
		paramPorts.put(TankParams.fluidHeight.toString(), fluidLevelPort);
		paramPorts.put(TankParams.fluidDensity.toString(), fluidDensityPort);
		paramPorts.put(TankParams.surfaceArea.toString(), surfaceAreaPort);
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