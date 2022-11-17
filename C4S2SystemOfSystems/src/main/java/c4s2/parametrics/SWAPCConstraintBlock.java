package c4s2.parametrics;

import java.util.Optional;
import c4s2.components.computer.operator.C4S2OperatorServicesComputer;
import c4s2.components.computer.services.C4S2ServicesComputer;
import c4s2.systems.c4s2.C4S2System;
import sysmlinjava.analysis.htmldisplay.HTMLString;
import sysmlinjava.analysis.htmldisplay.HTMLStringTransmitter;
import sysmlinjava.annotations.parametrics.ConstraintParameter;
import sysmlinjava.annotations.parametrics.ConstraintParameterPort;
import sysmlinjava.annotations.parametrics.ConstraintParameterPortFunction;
import sysmlinjava.constraintblocks.SysMLConstraintBlock;
import sysmlinjava.ports.SysMLConstraintParameterPort;
import sysmlinjava.ports.SysMLConstraintParameterPortFunction;
import sysmlinjava.valuetypes.PowerWatts;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.HeatWatts;
import sysmlinjava.valuetypes.VolumeMetersCubic;
import sysmlinjavalibrary.components.communications.internet.EthernetSwitchIPRouter;
import sysmlinjavalibrary.components.communications.siprnet.SIPRNetRouter;

/**
 * SysMLinJava representation of a constraint block for the constraint of size,
 * weight, and power, cooling (SWAPC) of the C4S2 System. The constraint
 * includes the summation of all four of the parameters for each of the system's
 * components, i.e. SWAPC for the {@code C4S2ServicesComputer},
 * {@code C4S2OperatorServicesComputer}, {@code EthernetSwitchIPRouter}, {@code 
 * SIPRNetRouter}. The constraint results, i.e. the SWAPC sums, are transmitted
 * to a display as an HTML table. As the SWAPC parameters are calculated once
 * during construction of the system model, the SWAPC sum parameters are
 * computed and displayed only once at the start of the model execution.
 * 
 * @author ModelerOne
 *
 */
public class SWAPCConstraintBlock extends SysMLConstraintBlock
{
	/**
	 * Constraint parameter for the sum of the C4S2 System component sizes/volumes
	 */
	@ConstraintParameter
	public VolumeMetersCubic volumeSum;
	/**
	 * Constraint parameter for the sum of the C4S2 System component weights
	 */
	@ConstraintParameter
	public ForceNewtons weightSum;
	/**
	 * Constraint parameter for the sum of the C4S2 System component power inputs
	 */
	@ConstraintParameter
	public PowerWatts powerSum;
	/**
	 * Constraint parameter for the sum of the C4S2 System component heat outputs
	 */
	@ConstraintParameter
	public HeatWatts heatSum;

	/**
	 * Constraint parameter for size/volume of the C4S2 Services Computer
	 */
	@ConstraintParameter
	public VolumeMetersCubic c4s2ServicesComputeVolume;
	/**
	 * Constraint parameter for weight of the C4S2 Services Computer
	 */
	@ConstraintParameter
	public ForceNewtons c4s2ServicesComputerWeight;
	/**
	 * Constraint parameter for power input of the C4S2 Services Computer
	 */
	@ConstraintParameter
	public PowerWatts c4s2ServicesComputerPower;
	/**
	 * Constraint parameter for heat output of the C4S2 Services Computer
	 */
	@ConstraintParameter
	public HeatWatts c4s2ServicesComputerHeat;
	/**
	 * Constraint parameter for size/volume of the C4S2 Operator Services Computer
	 */
	@ConstraintParameter
	public VolumeMetersCubic c4s2OperatorServicesComputerVolume;
	/**
	 * Constraint parameter for weight of the C4S2 Operator Services Computer
	 */
	@ConstraintParameter
	public ForceNewtons c4s2OperatorServicesComputerWeight;
	/**
	 * Constraint parameter for power input of the C4S2 Operator Services Computer
	 */
	@ConstraintParameter
	public PowerWatts c4s2OperatorServicesComputerPower;
	/**
	 * Constraint parameter for heat output of the C4S2 Operator Services Computer
	 */
	@ConstraintParameter
	public HeatWatts c4s2OperatorServicesComputerHeat;
	/**
	 * Constraint parameter for size/volume of the Ethernet Switch/IP Router
	 */
	@ConstraintParameter
	public VolumeMetersCubic switchRouterVolume;
	/**
	 * Constraint parameter for weight of the Ethernet Switch/IP Router
	 */
	@ConstraintParameter
	public ForceNewtons switchRouterWeight;
	/**
	 * Constraint parameter for power input of the Ethernet Switch/IP Router
	 */
	@ConstraintParameter
	public PowerWatts switchRouterPower;
	/**
	 * Constraint parameter for heat output of the Ethernet Switch/IP Router
	 */
	@ConstraintParameter
	public HeatWatts switchRouterHeat;
	/**
	 * Constraint parameter for size/volume of the SIPRnet Router
	 */
	@ConstraintParameter
	public VolumeMetersCubic siprnetRouterVolume;
	/**
	 * Constraint parameter for weight of the SIPRnet Router
	 */
	@ConstraintParameter
	public ForceNewtons siprnetRouterWeight;
	/**
	 * Constraint parameter for power input of the SIPRnet Router
	 */
	@ConstraintParameter
	public PowerWatts siprnetRouterPower;
	/**
	 * Constraint parameter for heat output of the SIPRnet Router
	 */
	@ConstraintParameter
	public HeatWatts siprnetRouterHeat;

	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * size
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction c4s2SServicesComputerVolumePortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * size
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction operatorServicesComputerVolumePortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * size
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction switchRouterVolumePortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * size
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction siprnetRouterVolumePortFunction;

	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * weight
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction c4s2SServicesComputerWeightPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * weight
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction operatorServicesComputerWeightPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * weight
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction switchRouterWeightPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * weight
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction siprnetRouterWeightPortFunction;

	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * power
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction c4s2SServicesComputerPowerPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * power
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction operatorServicesComputerPowerPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * power
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction switchRouterPowerPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * power
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction siprnetRouterPowerPortFunction;

	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * cooling
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction c4s2SServicesComputerHeatPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * cooling
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction operatorServicesComputerHeatPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * cooling
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction switchRouterHeatPortFunction;
	/**
	 * Function for "binding" the constraint parameter for the system component's
	 * cooling
	 */
	@ConstraintParameterPortFunction
	private SysMLConstraintParameterPortFunction siprnetRouterHeatPortFunction;

	/**
	 * Port for "binding" the constraint parameter for the system component's size
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort c4s2SServicesComputerVolumePort;
	/**
	 * Port for "binding" the constraint parameter for the system component's size
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort operatorServicesComputerVolumePort;
	/**
	 * Port for "binding" the constraint parameter for the system component's size
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort switchRouterVolumePort;
	/**
	 * Port for "binding" the constraint parameter for the system component's size
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort siprnetRouterVolumePort;

	/**
	 * Port for "binding" the constraint parameter for the system component's weight
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort c4s2SServicesComputerWeightPort;
	/**
	 * Port for "binding" the constraint parameter for the system component's weight
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort operatorServicesComputerWeightPort;
	/**
	 * Port for "binding" the constraint parameter for the system component's weight
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort switchRouterWeightPort;
	/**
	 * Port for "binding" the constraint parameter for the system component's weight
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort siprnetRouterWeightPort;

	/**
	 * Port for "binding" the constraint parameter for the system component's power
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort c4s2SServicesComputerPowerPort;
	/**
	 * Port for "binding" the constraint parameter for the system component's power
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort operatorServicesComputerPowerPort;
	/**
	 * Port for "binding" the constraint parameter for the system component's power
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort switchRouterPowerPort;
	/**
	 * Port for "binding" the constraint parameter for the system component's power
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort siprnetRouterPowerPort;

	/**
	 * Port for "binding" the constraint parameter for the system component's
	 * cooling
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort c4s2SServicesComputerHeatPort;
	/**
	 * Port for "binding" the constraint parameter for the system component's
	 * cooling
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort operatorServicesComputerHeatPort;
	/**
	 * Port for "binding" the constraint parameter for the system component's
	 * cooling
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort switchRouterHeatPort;
	/**
	 * Port for "binding" the constraint parameter for the system component's
	 * cooling
	 */
	@ConstraintParameterPort
	private SysMLConstraintParameterPort siprnetRouterHeatPort;

	/**
	 * Transmitter of the HTML table for the SWAPC sums
	 */
	HTMLStringTransmitter htmlStringTransmitter;

	/**
	 * The C4S2 System for which the SWAPC values are to be constrained/calculated
	 */
	C4S2System c4s2System;

	/**
	 * Constructor
	 * 
	 * @param c4s2System The C4S2 System for which the SWAPC values are to be
	 *                   constrained/calculated
	 * @param udpPort    UDP port to which the HTML is to be transmitted for display
	 */
	public SWAPCConstraintBlock(C4S2System c4s2System, int udpPort)
	{
		super(Optional.empty(), "SWAPSums");
		this.c4s2System = c4s2System;

		htmlStringTransmitter = new HTMLStringTransmitter(udpPort, false);
		htmlStringTransmitter.transmit(new HTMLString(String.format(sumsDisplayFormat, "-", "-", "-", "-", "-")));
	}

	@Override
	protected void performConstraints()
	{
		constraint.apply();
		transmitSums();
	}

	/**
	 * String containing the HTML for the display of the SWAPC values
	 */
	private static final String sumsDisplayFormat = """
		<!DOCTYPE html>
		<html>
		<head>
			<title>SWAPC for C2 System</title>
			<style>
				table {font-family: arial, sans-serif; border-collapse: collapse; width: 100%%;}
				td, th {border: 1px solid #dddddd; text-align: left; padding: 8px;}
				tr:nth-child(even) {background-color: #dddddd;}
			</style>
		</head>
		<body>
			<h2>SWAPC for C2 System</h2>
			<table>
				<tr><th>Parameter</th><th>Units</th><th>Value</th></tr>
				<tr><td>Size</td><td>Cubic Meters</td><td>%s</td></tr>
				<tr><td>Weight</td><td>Newtons</td><td>%s</td></tr>
				<tr><td>Power</td><td>Watts</td><td>%s</td></tr>
				<tr><td>Heat</td><td>Watts</td><td>%s</td></tr>
			</table>
		</body>
		</html>
		""";

	/**
	 * Calculation of the sums of the SWAPC constraint parameters
	 */
	private void calculateSums()
	{
		volumeSum.zero();
		volumeSum.add(c4s2ServicesComputeVolume);
		volumeSum.add(c4s2OperatorServicesComputerVolume);
		volumeSum.add(switchRouterVolume);
		volumeSum.add(siprnetRouterVolume);

		weightSum.zero();
		weightSum.add(c4s2ServicesComputerWeight);
		weightSum.add(c4s2OperatorServicesComputerWeight);
		weightSum.add(switchRouterWeight);
		weightSum.add(siprnetRouterWeight);

		powerSum.zero();
		powerSum.add(c4s2ServicesComputerPower);
		powerSum.add(c4s2OperatorServicesComputerPower);
		powerSum.add(switchRouterPower);
		powerSum.add(siprnetRouterPower);

		heatSum.zero();
		heatSum.add(c4s2ServicesComputerHeat);
		heatSum.add(c4s2OperatorServicesComputerHeat);
		heatSum.add(switchRouterHeat);
		heatSum.add(siprnetRouterHeat);
	}

	/**
	 * Transmits the SWAPC sums in the HTML to the HTML display
	 */
	private void transmitSums()
	{
		HTMLString htmlString = new HTMLString(String.format(sumsDisplayFormat, volumeSum.value, weightSum.value, powerSum.value, heatSum.value));
		htmlStringTransmitter.transmit(htmlString);
	}

	@Override
	protected void createConstraintParameters()
	{
		volumeSum = new VolumeMetersCubic(0);
		weightSum = new ForceNewtons(0);
		powerSum = new PowerWatts(0);
		heatSum = new HeatWatts(0);
		c4s2ServicesComputeVolume = new VolumeMetersCubic(0.0);
		c4s2ServicesComputerWeight = new ForceNewtons(0);
		c4s2ServicesComputerPower = new PowerWatts(0);
		c4s2ServicesComputerHeat = new HeatWatts(0);
		c4s2OperatorServicesComputerVolume = new VolumeMetersCubic(0.0);
		c4s2OperatorServicesComputerWeight = new ForceNewtons(0);
		c4s2OperatorServicesComputerPower = new PowerWatts(0);
		c4s2OperatorServicesComputerHeat = new HeatWatts(0);
		switchRouterVolume = new VolumeMetersCubic(0.0);
		switchRouterWeight = new ForceNewtons(0);
		switchRouterPower = new PowerWatts(0);
		switchRouterHeat = new HeatWatts(0);
		siprnetRouterVolume = new VolumeMetersCubic(0.0);
		siprnetRouterWeight = new ForceNewtons(0);
		siprnetRouterPower = new PowerWatts(0);
		siprnetRouterHeat = new HeatWatts(0);

		constraintParams.put(ParamIDs.c4s2ServicesComputerVolume.toString(), c4s2ServicesComputeVolume);
		constraintParams.put(ParamIDs.c4s2ServicesComputerWeight.toString(), c4s2ServicesComputerWeight);
		constraintParams.put(ParamIDs.c4s2ServicesComputerPower.toString(), c4s2ServicesComputerPower);
		constraintParams.put(ParamIDs.c4s2ServicesComputerHeat.toString(), c4s2ServicesComputerHeat);
		constraintParams.put(ParamIDs.c4s2OperatorServicesComputerVolume.toString(), c4s2OperatorServicesComputerVolume);
		constraintParams.put(ParamIDs.c4s2OperatorServicesComputerWeight.toString(), c4s2OperatorServicesComputerWeight);
		constraintParams.put(ParamIDs.c4s2OperatorServicesComputerPower.toString(), c4s2OperatorServicesComputerPower);
		constraintParams.put(ParamIDs.c4s2OperatorServicesComputerHeat.toString(), c4s2OperatorServicesComputerHeat);
		constraintParams.put(ParamIDs.switchRouterVolume.toString(), switchRouterVolume);
		constraintParams.put(ParamIDs.switchRouterWeight.toString(), switchRouterWeight);
		constraintParams.put(ParamIDs.switchRouterPower.toString(), switchRouterPower);
		constraintParams.put(ParamIDs.switchRouterHeat.toString(), switchRouterHeat);
		constraintParams.put(ParamIDs.siprnetRouterVolume.toString(), siprnetRouterVolume);
		constraintParams.put(ParamIDs.siprnetRouterWeight.toString(), siprnetRouterWeight);
		constraintParams.put(ParamIDs.siprnetRouterPower.toString(), siprnetRouterPower);
		constraintParams.put(ParamIDs.siprnetRouterHeat.toString(), siprnetRouterHeat);
	}

	@Override
	protected void createConstraints()
	{
		constraint = () ->
		{
			if (currentParamID.isPresent())
			{
				ParamIDs paramID = ParamIDs.valueOf(currentParamID.get());
				if (paramID != null)
				{
					switch (paramID)
					{
					case c4s2OperatorServicesComputerHeat:
						c4s2OperatorServicesComputerHeat.value = ((HeatWatts)constraintParams.get(currentParamID.get())).value;
						break;
					case c4s2OperatorServicesComputerPower:
						c4s2OperatorServicesComputerPower.value = ((PowerWatts)constraintParams.get(currentParamID.get())).value;
						break;
					case c4s2OperatorServicesComputerVolume:
						c4s2OperatorServicesComputerVolume.value = ((VolumeMetersCubic)constraintParams.get(currentParamID.get())).value;
						break;
					case c4s2OperatorServicesComputerWeight:
						c4s2OperatorServicesComputerWeight.value = ((ForceNewtons)constraintParams.get(currentParamID.get())).value;
						break;
					case c4s2ServicesComputerHeat:
						c4s2ServicesComputerHeat.value = ((HeatWatts)constraintParams.get(currentParamID.get())).value;
						break;
					case c4s2ServicesComputerPower:
						c4s2ServicesComputerPower.value = ((PowerWatts)constraintParams.get(currentParamID.get())).value;
						break;
					case c4s2ServicesComputerVolume:
						c4s2ServicesComputeVolume.value = ((VolumeMetersCubic)constraintParams.get(currentParamID.get())).value;
						break;
					case c4s2ServicesComputerWeight:
						c4s2ServicesComputerWeight.value = ((ForceNewtons)constraintParams.get(currentParamID.get())).value;
						break;
					case siprnetRouterHeat:
						siprnetRouterHeat.value = ((HeatWatts)constraintParams.get(currentParamID.get())).value;
						break;
					case siprnetRouterPower:
						siprnetRouterPower.value = ((PowerWatts)constraintParams.get(currentParamID.get())).value;
						break;
					case siprnetRouterVolume:
						siprnetRouterVolume.value = ((VolumeMetersCubic)constraintParams.get(currentParamID.get())).value;
						break;
					case siprnetRouterWeight:
						siprnetRouterWeight.value = ((ForceNewtons)constraintParams.get(currentParamID.get())).value;
						break;
					case switchRouterHeat:
						switchRouterHeat.value = ((HeatWatts)constraintParams.get(currentParamID.get())).value;
						break;
					case switchRouterPower:
						switchRouterPower.value = ((PowerWatts)constraintParams.get(currentParamID.get())).value;
						break;
					case switchRouterVolume:
						switchRouterVolume.value = ((VolumeMetersCubic)constraintParams.get(currentParamID.get())).value;
						break;
					case switchRouterWeight:
						switchRouterWeight.value = ((ForceNewtons)constraintParams.get(currentParamID.get())).value;
						break;
					default:
						break;
					}
					calculateSums();
				}
				else
					logger.severe("invalid parameter ID: " + currentParamID.get());
			}
		};
	}

	@Override
	protected void createConstraintParameterPortFunctions()
	{
		c4s2SServicesComputerVolumePortFunction = (constraintParameterPort, contextBlock) ->
		{
			VolumeMetersCubic volume = ((C4S2ServicesComputer)contextBlock).maxSize;
			constraintParameterPort.updateParameterValue(new VolumeMetersCubic(volume));
		};
		operatorServicesComputerVolumePortFunction = (constraintParameterPort, contextBlock) ->
		{
			VolumeMetersCubic volume = ((C4S2OperatorServicesComputer)contextBlock).maxSize;
			constraintParameterPort.updateParameterValue(new VolumeMetersCubic(volume));
		};
		switchRouterVolumePortFunction = (constraintParameterPort, contextBlock) ->
		{
			VolumeMetersCubic volume = ((EthernetSwitchIPRouter)contextBlock).maxSize;
			constraintParameterPort.updateParameterValue(new VolumeMetersCubic(volume));
		};
		siprnetRouterVolumePortFunction = (constraintParameterPort, contextBlock) ->
		{
			VolumeMetersCubic volume = ((SIPRNetRouter)contextBlock).maxSize;
			constraintParameterPort.updateParameterValue(new VolumeMetersCubic(volume));
		};

		c4s2SServicesComputerWeightPortFunction = (constraintParameterPort, contextBlock) ->
		{
			ForceNewtons weight = ((C4S2ServicesComputer)contextBlock).maxWeight;
			constraintParameterPort.updateParameterValue(new ForceNewtons(weight));
		};
		operatorServicesComputerWeightPortFunction = (constraintParameterPort, contextBlock) ->
		{
			ForceNewtons weight = ((C4S2OperatorServicesComputer)contextBlock).maxWeight;
			constraintParameterPort.updateParameterValue(new ForceNewtons(weight));
		};
		switchRouterWeightPortFunction = (constraintParameterPort, contextBlock) ->
		{
			ForceNewtons weight = ((EthernetSwitchIPRouter)contextBlock).maxWeight;
			constraintParameterPort.updateParameterValue(new ForceNewtons(weight));
		};
		siprnetRouterWeightPortFunction = (constraintParameterPort, contextBlock) ->
		{
			ForceNewtons weight = ((SIPRNetRouter)contextBlock).maxWeight;
			constraintParameterPort.updateParameterValue(new ForceNewtons(weight));
		};

		c4s2SServicesComputerPowerPortFunction = (constraintParameterPort, contextBlock) ->
		{
			PowerWatts power = ((C4S2ServicesComputer)contextBlock).maxPowerIn;
			constraintParameterPort.updateParameterValue(new PowerWatts(power));
		};
		operatorServicesComputerPowerPortFunction = (constraintParameterPort, contextBlock) ->
		{
			PowerWatts power = ((C4S2OperatorServicesComputer)contextBlock).maxPowerIn;
			constraintParameterPort.updateParameterValue(new PowerWatts(power));
		};
		switchRouterPowerPortFunction = (constraintParameterPort, contextBlock) ->
		{
			PowerWatts power = ((EthernetSwitchIPRouter)contextBlock).maxPowerIn;
			constraintParameterPort.updateParameterValue(new PowerWatts(power));
		};
		siprnetRouterPowerPortFunction = (constraintParameterPort, contextBlock) ->
		{
			PowerWatts power = ((SIPRNetRouter)contextBlock).maxPowerIn;
			constraintParameterPort.updateParameterValue(new PowerWatts(power));
		};

		c4s2SServicesComputerHeatPortFunction = (constraintParameterPort, contextBlock) ->
		{
			HeatWatts heat = ((C4S2ServicesComputer)contextBlock).maxHeatOut;
			constraintParameterPort.updateParameterValue(new HeatWatts(heat));
		};
		operatorServicesComputerHeatPortFunction = (constraintParameterPort, contextBlock) ->
		{
			HeatWatts heat = ((C4S2OperatorServicesComputer)contextBlock).maxHeatOut;
			constraintParameterPort.updateParameterValue(new HeatWatts(heat));
		};
		switchRouterHeatPortFunction = (constraintParameterPort, contextBlock) ->
		{
			HeatWatts heat = ((EthernetSwitchIPRouter)contextBlock).maxHeatOut;
			constraintParameterPort.updateParameterValue(new HeatWatts(heat));
		};
		siprnetRouterHeatPortFunction = (constraintParameterPort, contextBlock) ->
		{
			HeatWatts heat = ((SIPRNetRouter)contextBlock).maxHeatOut;
			constraintParameterPort.updateParameterValue(new HeatWatts(heat));
		};

		paramPortFunctions.put(ParamIDs.c4s2ServicesComputerVolume.toString(), c4s2SServicesComputerVolumePortFunction);
		paramPortFunctions.put(ParamIDs.c4s2OperatorServicesComputerVolume.toString(), operatorServicesComputerVolumePortFunction);
		paramPortFunctions.put(ParamIDs.switchRouterVolume.toString(), switchRouterVolumePortFunction);
		paramPortFunctions.put(ParamIDs.siprnetRouterVolume.toString(), siprnetRouterVolumePortFunction);

		paramPortFunctions.put(ParamIDs.c4s2ServicesComputerWeight.toString(), c4s2SServicesComputerWeightPortFunction);
		paramPortFunctions.put(ParamIDs.c4s2OperatorServicesComputerWeight.toString(), operatorServicesComputerWeightPortFunction);
		paramPortFunctions.put(ParamIDs.switchRouterWeight.toString(), switchRouterWeightPortFunction);
		paramPortFunctions.put(ParamIDs.siprnetRouterWeight.toString(), siprnetRouterWeightPortFunction);

		paramPortFunctions.put(ParamIDs.c4s2ServicesComputerPower.toString(), c4s2SServicesComputerPowerPortFunction);
		paramPortFunctions.put(ParamIDs.c4s2OperatorServicesComputerPower.toString(), operatorServicesComputerPowerPortFunction);
		paramPortFunctions.put(ParamIDs.switchRouterPower.toString(), switchRouterPowerPortFunction);
		paramPortFunctions.put(ParamIDs.siprnetRouterPower.toString(), siprnetRouterPowerPortFunction);

		paramPortFunctions.put(ParamIDs.c4s2ServicesComputerHeat.toString(), c4s2SServicesComputerHeatPortFunction);
		paramPortFunctions.put(ParamIDs.c4s2OperatorServicesComputerHeat.toString(), operatorServicesComputerHeatPortFunction);
		paramPortFunctions.put(ParamIDs.switchRouterHeat.toString(), switchRouterHeatPortFunction);
		paramPortFunctions.put(ParamIDs.siprnetRouterHeat.toString(), siprnetRouterHeatPortFunction);
	}

	@Override
	protected void createConstraintParameterPorts()
	{
		c4s2SServicesComputerVolumePort = new SysMLConstraintParameterPort(this, c4s2SServicesComputerVolumePortFunction, ParamIDs.c4s2ServicesComputerVolume.toString());
		paramPorts.put(ParamIDs.c4s2ServicesComputerVolume.toString(), c4s2SServicesComputerVolumePort);
		operatorServicesComputerVolumePort = new SysMLConstraintParameterPort(this, operatorServicesComputerVolumePortFunction, ParamIDs.c4s2OperatorServicesComputerVolume.toString());
		paramPorts.put(ParamIDs.c4s2OperatorServicesComputerVolume.toString(), operatorServicesComputerVolumePort);
		switchRouterVolumePort = new SysMLConstraintParameterPort(this, switchRouterVolumePortFunction, ParamIDs.switchRouterVolume.toString());
		paramPorts.put(ParamIDs.switchRouterVolume.toString(), switchRouterVolumePort);
		siprnetRouterVolumePort = new SysMLConstraintParameterPort(this, siprnetRouterVolumePortFunction, ParamIDs.siprnetRouterVolume.toString());
		paramPorts.put(ParamIDs.siprnetRouterVolume.toString(), siprnetRouterVolumePort);

		c4s2SServicesComputerWeightPort = new SysMLConstraintParameterPort(this, c4s2SServicesComputerWeightPortFunction, ParamIDs.c4s2ServicesComputerWeight.toString());
		paramPorts.put(ParamIDs.c4s2ServicesComputerWeight.toString(), c4s2SServicesComputerWeightPort);
		operatorServicesComputerWeightPort = new SysMLConstraintParameterPort(this, operatorServicesComputerWeightPortFunction, ParamIDs.c4s2OperatorServicesComputerWeight.toString());
		paramPorts.put(ParamIDs.c4s2OperatorServicesComputerWeight.toString(), operatorServicesComputerWeightPort);
		switchRouterWeightPort = new SysMLConstraintParameterPort(this, switchRouterWeightPortFunction, ParamIDs.switchRouterWeight.toString());
		paramPorts.put(ParamIDs.switchRouterWeight.toString(), switchRouterWeightPort);
		siprnetRouterWeightPort = new SysMLConstraintParameterPort(this, siprnetRouterWeightPortFunction, ParamIDs.siprnetRouterWeight.toString());
		paramPorts.put(ParamIDs.siprnetRouterWeight.toString(), siprnetRouterWeightPort);

		c4s2SServicesComputerPowerPort = new SysMLConstraintParameterPort(this, c4s2SServicesComputerPowerPortFunction, ParamIDs.c4s2ServicesComputerPower.toString());
		paramPorts.put(ParamIDs.c4s2ServicesComputerPower.toString(), c4s2SServicesComputerPowerPort);
		operatorServicesComputerPowerPort = new SysMLConstraintParameterPort(this, operatorServicesComputerPowerPortFunction, ParamIDs.c4s2OperatorServicesComputerPower.toString());
		paramPorts.put(ParamIDs.c4s2OperatorServicesComputerPower.toString(), operatorServicesComputerPowerPort);
		switchRouterPowerPort = new SysMLConstraintParameterPort(this, switchRouterPowerPortFunction, ParamIDs.switchRouterPower.toString());
		paramPorts.put(ParamIDs.switchRouterPower.toString(), switchRouterPowerPort);
		siprnetRouterPowerPort = new SysMLConstraintParameterPort(this, siprnetRouterPowerPortFunction, ParamIDs.siprnetRouterPower.toString());
		paramPorts.put(ParamIDs.siprnetRouterPower.toString(), siprnetRouterPowerPort);

		c4s2SServicesComputerHeatPort = new SysMLConstraintParameterPort(this, c4s2SServicesComputerHeatPortFunction, ParamIDs.c4s2ServicesComputerHeat.toString());
		paramPorts.put(ParamIDs.c4s2ServicesComputerHeat.toString(), c4s2SServicesComputerHeatPort);
		operatorServicesComputerHeatPort = new SysMLConstraintParameterPort(this, operatorServicesComputerHeatPortFunction, ParamIDs.c4s2OperatorServicesComputerHeat.toString());
		paramPorts.put(ParamIDs.c4s2OperatorServicesComputerHeat.toString(), operatorServicesComputerHeatPort);
		switchRouterHeatPort = new SysMLConstraintParameterPort(this, switchRouterHeatPortFunction, ParamIDs.switchRouterHeat.toString());
		paramPorts.put(ParamIDs.switchRouterHeat.toString(), switchRouterHeatPort);
		siprnetRouterHeatPort = new SysMLConstraintParameterPort(this, siprnetRouterHeatPortFunction, ParamIDs.siprnetRouterHeat.toString());
		paramPorts.put(ParamIDs.siprnetRouterHeat.toString(), siprnetRouterHeatPort);
	}

	/**
	 * Enumeration of the identifiers of the constraint parameters used to
	 * constrain/calculate the SWAPC sums
	 * 
	 * @author ModelerOne
	 *
	 */
	public enum ParamIDs
	{
		c4s2ServicesComputerVolume, c4s2ServicesComputerWeight, c4s2ServicesComputerPower, c4s2ServicesComputerHeat, c4s2OperatorServicesComputerVolume, c4s2OperatorServicesComputerWeight, c4s2OperatorServicesComputerPower, c4s2OperatorServicesComputerHeat, switchRouterVolume, switchRouterWeight, switchRouterPower, switchRouterHeat, siprnetRouterVolume, siprnetRouterWeight, siprnetRouterPower, siprnetRouterHeat;
	}
}
