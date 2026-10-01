package c4s2.parametrics;

import java.util.List;
import java.util.Optional;

import c4s2.requirements.C4S2SystemOfSystemAnalysisRequirements;
import c4s2.systems.c4s2.C4S2System;
import sysmlinjava.analysis.ParametricAnalysisCase;
import sysmlinjava.attributetypes.ForceNewtons;
import sysmlinjava.attributetypes.HeatWatts;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.attributetypes.VolumeMetersCubic;
import sysmlinjava.javaannotations.actions.Calculation;
import sysmlinjava.javaannotations.analysis.AnalysisResult;
import sysmlinjava.javaannotations.analysis.parametrics.Parameter;
import sysmlinjava.views.htmldisplay.HTMLString;
import sysmlinjava.views.htmldisplay.HTMLStringTransmitter;

/**
 * SysMLinJava representation of an analysis of the size, weight, and power,
 * cooling (SWAPC) of the C4S2 System. The analysis includes the summation of
 * all four of the parameters for each of the system's components, i.e. SWAPC
 * for the {@code C4S2ServicesComputer}, {@code C4S2OperatorServicesComputer},
 * {@code EthernetSwitchIPRouter}, {@code
 * SIPRNetRouter}. The analysis results, i.e. the SWAPC sums, are transmitted to
 * a display as an HTML table. As the SWAPC parameters are calculated once
 * during construction of the system model, the SWAPC sum parameters are
 * computed and displayed only once at the start of the model execution.
 * 
 * @author ModelerOne
 */
public class SWAPCAnalysisCase extends ParametricAnalysisCase
{
	/**
	 * Analysis result for the sum of the C4S2 System component sizes/volumes
	 */
	@AnalysisResult
	public VolumeMetersCubic volumeSum;
	/**
	 * Analysis result for the sum of the C4S2 System component weights
	 */
	@AnalysisResult
	public ForceNewtons weightSum;
	/**
	 * Analysis result for the sum of the C4S2 System component power inputs
	 */
	@AnalysisResult
	public PowerWatts powerSum;
	/**
	 * Analysis result for the sum of the C4S2 System component heat outputs
	 */
	@AnalysisResult
	public HeatWatts heatSum;

	/**
	 * Constraint parameter for size/volume of the C4S2 Services Computer
	 */
	@Parameter
	public VolumeMetersCubic c4s2ServicesComputeVolume;
	/**
	 * Constraint parameter for weight of the C4S2 Services Computer
	 */
	@Parameter
	public ForceNewtons c4s2ServicesComputerWeight;
	/**
	 * Constraint parameter for power input of the C4S2 Services Computer
	 */
	@Parameter
	public PowerWatts c4s2ServicesComputerPower;
	/**
	 * Constraint parameter for heat output of the C4S2 Services Computer
	 */
	@Parameter
	public HeatWatts c4s2ServicesComputerHeat;
	/**
	 * Constraint parameter for size/volume of the C4S2 Operator Services Computer
	 */
	@Parameter
	public VolumeMetersCubic c4s2OperatorServicesComputerVolume;
	/**
	 * Constraint parameter for weight of the C4S2 Operator Services Computer
	 */
	@Parameter
	public ForceNewtons c4s2OperatorServicesComputerWeight;
	/**
	 * Constraint parameter for power input of the C4S2 Operator Services Computer
	 */
	@Parameter
	public PowerWatts c4s2OperatorServicesComputerPower;
	/**
	 * Constraint parameter for heat output of the C4S2 Operator Services Computer
	 */
	@Parameter
	public HeatWatts c4s2OperatorServicesComputerHeat;
	/**
	 * Constraint parameter for size/volume of the Ethernet Switch/IP Router
	 */
	@Parameter
	public VolumeMetersCubic switchRouterVolume;
	/**
	 * Constraint parameter for weight of the Ethernet Switch/IP Router
	 */
	@Parameter
	public ForceNewtons switchRouterWeight;
	/**
	 * Constraint parameter for power input of the Ethernet Switch/IP Router
	 */
	@Parameter
	public PowerWatts switchRouterPower;
	/**
	 * Constraint parameter for heat output of the Ethernet Switch/IP Router
	 */
	@Parameter
	public HeatWatts switchRouterHeat;
	/**
	 * Constraint parameter for size/volume of the SIPRnet Router
	 */
	@Parameter
	public VolumeMetersCubic siprnetRouterVolume;
	/**
	 * Constraint parameter for weight of the SIPRnet Router
	 */
	@Parameter
	public ForceNewtons siprnetRouterWeight;
	/**
	 * Constraint parameter for power input of the SIPRnet Router
	 */
	@Parameter
	public PowerWatts siprnetRouterPower;
	/**
	 * Constraint parameter for heat output of the SIPRnet Router
	 */
	@Parameter
	public HeatWatts siprnetRouterHeat;

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
	public SWAPCAnalysisCase(C4S2System c4s2System, int udpPort)
	{
		super(Optional.empty(), "SWAPSums", 0L);
		this.c4s2System = c4s2System;

		htmlStringTransmitter = new HTMLStringTransmitter(udpPort, false);
		htmlStringTransmitter.transmit(new HTMLString(String.format(sumsDisplayFormat, "-", "-", "-", "-", "-")));
	}

	/**
	 * Performs the analysis calculating the new sums, and transmitting the data to
	 * the SWAP display
	 */
	@Override
	public void perform()
	{
		calculateSums();
		transmitSums();
	}

	@Override
	protected void onParameterChange(String paramID, SysMLAttributeType paramValue)
	{
		ParamIDs id = ParamIDs.valueOf(paramID);
		if (id != null)
		{
			switch (id)
			{
			case c4s2OperatorServicesComputerHeat:
				c4s2OperatorServicesComputerHeat.value = ((HeatWatts) paramValue).value;
				break;
			case c4s2OperatorServicesComputerPower:
				c4s2OperatorServicesComputerPower.value = ((PowerWatts) paramValue).value;
				break;
			case c4s2OperatorServicesComputerVolume:
				c4s2OperatorServicesComputerVolume.value = ((VolumeMetersCubic) paramValue).value;
				break;
			case c4s2OperatorServicesComputerWeight:
				c4s2OperatorServicesComputerWeight.value = ((ForceNewtons) paramValue).value;
				break;
			case c4s2ServicesComputerHeat:
				c4s2ServicesComputerHeat.value = ((HeatWatts) paramValue).value;
				break;
			case c4s2ServicesComputerPower:
				c4s2ServicesComputerPower.value = ((PowerWatts) paramValue).value;
				break;
			case c4s2ServicesComputerVolume:
				c4s2ServicesComputeVolume.value = ((VolumeMetersCubic) paramValue).value;
				break;
			case c4s2ServicesComputerWeight:
				c4s2ServicesComputerWeight.value = ((ForceNewtons) paramValue).value;
				break;
			case siprnetRouterHeat:
				siprnetRouterHeat.value = ((HeatWatts) paramValue).value;
				break;
			case siprnetRouterPower:
				siprnetRouterPower.value = ((PowerWatts) paramValue).value;
				break;
			case siprnetRouterVolume:
				siprnetRouterVolume.value = ((VolumeMetersCubic) paramValue).value;
				break;
			case siprnetRouterWeight:
				siprnetRouterWeight.value = ((ForceNewtons) paramValue).value;
				break;
			case switchRouterHeat:
				switchRouterHeat.value = ((HeatWatts) paramValue).value;
				break;
			case switchRouterPower:
				switchRouterPower.value = ((PowerWatts) paramValue).value;
				break;
			case switchRouterVolume:
				switchRouterVolume.value = ((VolumeMetersCubic) paramValue).value;
				break;
			case switchRouterWeight:
				switchRouterWeight.value = ((ForceNewtons) paramValue).value;
				break;
			default:
				break;
			}
		}
		else
			logger.severe("invalid parameter ID: " + currentParamID.get());
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
	@Calculation
	private void calculateSums()
	{
		calculateVolumeSum();
		calculateWeightSum();
		calculatePowerSum();
		calculateHeatSum();
	}

	@Calculation
	private void calculateVolumeSum()
	{
		volumeSum.zero();
		volumeSum.add(c4s2ServicesComputeVolume);
		volumeSum.add(c4s2OperatorServicesComputerVolume);
		volumeSum.add(switchRouterVolume);
		volumeSum.add(siprnetRouterVolume);
	}

	@Calculation
	private void calculateWeightSum()
	{
		weightSum.zero();
		weightSum.add(c4s2ServicesComputerWeight);
		weightSum.add(c4s2OperatorServicesComputerWeight);
		weightSum.add(switchRouterWeight);
		weightSum.add(siprnetRouterWeight);
	}

	@Calculation
	private void calculatePowerSum()
	{
		powerSum.zero();
		powerSum.add(c4s2ServicesComputerPower);
		powerSum.add(c4s2OperatorServicesComputerPower);
		powerSum.add(switchRouterPower);
		powerSum.add(siprnetRouterPower);
	}

	@Calculation
	private void calculateHeatSum()
	{
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
	protected void createParameters()
	{
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

//		params.put(ParamIDs.c4s2ServicesComputerVolume.toString(), c4s2ServicesComputeVolume);
//		params.put(ParamIDs.c4s2ServicesComputerWeight.toString(), c4s2ServicesComputerWeight);
//		params.put(ParamIDs.c4s2ServicesComputerPower.toString(), c4s2ServicesComputerPower);
//		params.put(ParamIDs.c4s2ServicesComputerHeat.toString(), c4s2ServicesComputerHeat);
//		params.put(ParamIDs.c4s2OperatorServicesComputerVolume.toString(), c4s2OperatorServicesComputerVolume);
//		params.put(ParamIDs.c4s2OperatorServicesComputerWeight.toString(), c4s2OperatorServicesComputerWeight);
//		params.put(ParamIDs.c4s2OperatorServicesComputerPower.toString(), c4s2OperatorServicesComputerPower);
//		params.put(ParamIDs.c4s2OperatorServicesComputerHeat.toString(), c4s2OperatorServicesComputerHeat);
//		params.put(ParamIDs.switchRouterVolume.toString(), switchRouterVolume);
//		params.put(ParamIDs.switchRouterWeight.toString(), switchRouterWeight);
//		params.put(ParamIDs.switchRouterPower.toString(), switchRouterPower);
//		params.put(ParamIDs.switchRouterHeat.toString(), switchRouterHeat);
//		params.put(ParamIDs.siprnetRouterVolume.toString(), siprnetRouterVolume);
//		params.put(ParamIDs.siprnetRouterWeight.toString(), siprnetRouterWeight);
//		params.put(ParamIDs.siprnetRouterPower.toString(), siprnetRouterPower);
//		params.put(ParamIDs.siprnetRouterHeat.toString(), siprnetRouterHeat);
	}

	@Override
	protected void createObjective()
	{
		objective = C4S2SystemOfSystemAnalysisRequirements.swapcAnalysisObjective;
	}

	@Override
	protected void createSubject()
	{
		subject = Optional.of(C4S2System.class);
	}

	@Override
	protected void createActors()
	{
		actors = List.of();
	}

	@Override
	protected void createResult()
	{
		volumeSum = new VolumeMetersCubic(0);
		weightSum = new ForceNewtons(0);
		powerSum = new PowerWatts(0);
		heatSum = new HeatWatts(0);
	}

	/**
	 * Enumeration of the identifiers of the constraint parameters used to
	 * constrain/calculate the SWAPC sums
	 * 
	 * @author ModelerOne
	 */
	public enum ParamIDs
	{
		c4s2ServicesComputerVolume,
		c4s2ServicesComputerWeight,
		c4s2ServicesComputerPower,
		c4s2ServicesComputerHeat,
		c4s2OperatorServicesComputerVolume,
		c4s2OperatorServicesComputerWeight,
		c4s2OperatorServicesComputerPower,
		c4s2OperatorServicesComputerHeat,
		switchRouterVolume,
		switchRouterWeight,
		switchRouterPower,
		switchRouterHeat,
		siprnetRouterVolume,
		siprnetRouterWeight,
		siprnetRouterPower,
		siprnetRouterHeat;
	}
}
