package c4s2.platforms;

import static sysmlinjava.valuetypes.ElectricalPower.standard110V;
import static sysmlinjava.valuetypes.ElectricalPower.standard50Hz;
import java.util.Optional;
import c4s2.common.valueTypes.C4S2SystemComponentsEnum;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.kinds.SysMLFlowDirectionKind;
import sysmlinjava.valuetypes.CurrentAmps;
import sysmlinjava.valuetypes.ElectricalPower;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.HeatWatts;
import sysmlinjava.valuetypes.IInteger;
import sysmlinjavalibrary.common.objects.energy.thermal.ConvectiveHeat;
import sysmlinjavalibrary.common.ports.energy.electrical.ElectricalPowerSource;
import sysmlinjavalibrary.common.ports.energy.mechanical.RackMountStructure;
import sysmlinjavalibrary.common.ports.energy.thermal.ConvectiveHeatSink;

/**
 * The {@code C4Platform} is a SysMLinJava model of a Command/Control
 * Communications/Computers (C4) platform. It represents the platform on which
 * the {@code C4S2System} resides, i.e. it provides space, power, weight, and
 * cooling support for the components (parts) of the {@code C4S2System}. The
 * {@code C4Platform} could be a building, a ground vehicle, or an aircraft. The
 * current model does not extend to the type of platform is to be used, but
 * rather its focus is solely on the structure and behavior needed to support
 * the {@code C4S2System}.
 * <p>
 * The {@code C4Platform} interacts with the components to provide rack mount
 * space, electrial power, and convective heat transfer, for the
 * {@code C4S2System} components. This is analogous to the classical SWAP-C
 * (size, weight and power, cooling) problem frequently encountered by systems
 * engineers. These components include two computers that provide services for
 * the operator, a SIPRNet router that provides a datalink between the
 * C4S2System and the other (radar and strike) systems that are in the domain,
 * and an ethernet switch/IP router to provide internet communications between
 * them all.
 * <p>
 * The platform behavior is straighforward. Its first behavior is to receive the
 * weight of each of the system components via its rack mount port. Then, when a
 * system component is switched on, the component provides a power "sink" to the
 * platform which the platform then provides a power source for. Once powered
 * on, the component sources its heat to the platform which the platform sinks
 * to its cooling (heat sink) port. For all of these weight-in, power-out, and
 * heat-in reactions, the platform checks to see if it has the capacity to
 * provide the amount of flow that is needed and logs a warning if it does not.
 * This allows the modeler to ensure that all interfaces with the platform
 * specified in the model are valid and realizable.
 * 
 * @author ModelerOne
 *
 */
public class C4Platform extends SysMLBlock
{
	/**
	 * Port that provides electrical power to the ethernet switch/Ip router
	 */
	@FullPort
	public ElectricalPowerSource powerEthernetSwitchIPRouter;
	/**
	 * Port that provides electrical power to the SIPRNet router
	 */
	@FullPort
	public ElectricalPowerSource powerSIPRNetRouter;
	/**
	 * Port that provides electrical power to the C4S2 services computer
	 */
	@FullPort
	public ElectricalPowerSource powerC4S2ServicesComputer;
	/**
	 * Port that provides electrical power to the C4S2 operator services computer
	 */
	@FullPort
	public ElectricalPowerSource powerOperatorServicesComputer;
	/**
	 * Port that provides electrical power to the ethernet switch/Ip router
	 */
	@FullPort
	public ConvectiveHeatSink heatEthernetSwitchIPRouter;
	/**
	 * Port that receives heat from the SIPRNet router
	 */
	@FullPort
	public ConvectiveHeatSink heatSIPRNetRouter;
	/**
	 * Port that receives heat from the C4S2 services computer
	 */
	@FullPort
	public ConvectiveHeatSink heatC4S2ServicesComputer;
	/**
	 * Port that receives heat from the C4S2 operator services computer
	 */
	@FullPort
	public ConvectiveHeatSink heatC4S2OperatorServicesComputer;
	/**
	 * Port that receives the object and weight from all of the C4S2 system
	 * components
	 */
	@FullPort
	public RackMountStructure rackMount;

	/**
	 * Flow of power out to the ethernet switch/IP router
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ElectricalPower powerOutEthernetSwitchIPRouter;
	/**
	 * Flow of power out to the SIPRNet router
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ElectricalPower powerOutSIPRNetRouter;
	/**
	 * Flow of power out to the C4S2 services computer
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ElectricalPower powerOutC4S2ServicesComputer;
	/**
	 * Flow of power out to C4S2 operator services computer
	 */
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ElectricalPower powerOutC4S2OperatorServicesComputer;
	/**
	 * Flow of heat in from the ethernet switch/IP router
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public ConvectiveHeat heatInEthernetSwitchIPRouter;
	/**
	 * Flow of heat in from the SIPRNet router
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public ConvectiveHeat heatInSIPRNetRouter;
	/**
	 * Flow of heat in from C4S2 services computer
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public ConvectiveHeat heatInC4S2ServicesComputer;
	/**
	 * Flow of heat in from C4S2 operator services computer
	 */
	@Flow(direction = SysMLFlowDirectionKind.in)
	public ConvectiveHeat heatInC4S2OperatorServicesComputer;

	/**
	 * Total weight transmitted to the platform
	 */
	@Value
	public ForceNewtons totalWeightIn;
	/**
	 * Total power being transmitted out by the platform
	 */
	@Value
	public ElectricalPower totalPowerOut;
	/**
	 * Total heat being transmitted to the platform
	 */
	@Value
	public ConvectiveHeat totalHeatIn;
	/**
	 * Maximum amount of current that can be provided by the platform
	 */
	@Value
	private CurrentAmps maxCurrentOut;
	/**
	 * Maximum amount of power that can be provided by the platform
	 */
	@Value
	public ElectricalPower maxPowerOut;
	/**
	 * Maximum amount of heat that can be received into the platform
	 */
	@Value
	public ConvectiveHeat maxHeatIn;
	/**
	 * Maximum amount of weight that can be supported by the platform
	 */
	@Value
	public ForceNewtons maxWeightIn;

	public C4Platform()
	{
		super("C4Platform", 0L);
	}

	/**
	 * Reacts to event of electrical power sink appearing, i.e. a power switch has
	 * been opened. Reaction is to identify the port opening the sink, checking if
	 * the platform can source the sinkable power (current) and then, if so,
	 * sourcing the power sink as required.
	 * 
	 * @param powerSink the power that is needed by the power sink
	 */
	public void onPowerSink(ElectricalPower powerSink)
	{
		logger.info(powerSink.toString());
		if (powerSink.name.isPresent())
		{
			String switchName = powerSink.name.get();
			Optional<ElectricalPowerSource> powerPort = Optional.empty();
			Optional<ElectricalPower> powerPortPowerOut = Optional.empty();
			if (switchName.contains(C4S2SystemComponentsEnum.C4S2ServicesComputer.name()))
			{
				powerPort = Optional.of(powerC4S2ServicesComputer);
				powerPortPowerOut = Optional.of(powerOutC4S2ServicesComputer);
			}
			else if (switchName.contains(C4S2SystemComponentsEnum.C4S2OperatorServicesComputer.name()))
			{
				powerPort = Optional.of(powerOperatorServicesComputer);
				powerPortPowerOut = Optional.of(powerOutC4S2OperatorServicesComputer);
			}
			else if (switchName.contains(C4S2SystemComponentsEnum.EthernetSwitchIPRouter.name()))
			{
				powerPort = Optional.of(powerEthernetSwitchIPRouter);
				powerPortPowerOut = Optional.of(powerOutEthernetSwitchIPRouter);
			}
			else if (switchName.contains(C4S2SystemComponentsEnum.SIPRNetRouter.name()))
			{
				powerPort = Optional.of(powerSIPRNetRouter);
				powerPortPowerOut = Optional.of(powerOutSIPRNetRouter);
			}
			if (powerPort.isPresent() && powerPortPowerOut.isPresent())
			{
				totalPowerOut.current.value -= powerPortPowerOut.get().current.value;
				if (!isCapacityForAdditional(powerSink))
					logger.warning("power sink exceeds power source capacity: " + powerSink.toString());
				powerPortPowerOut.get().current.value = powerSink.current.value;
				totalPowerOut.current.value += powerSink.current.value;
				powerPort.get().transmit(new ElectricalPower(powerSink));
			}
			else
				logger.severe("unable to map power requestor to available power port");
		}
		else
			logger.severe("missing/unknown identity of power requestor");
	}

	/**
	 * Reacts to event of a heat source appearing, i.e. a new source of heat
	 * convection has started. Reaction is to identify the port opening the source,
	 * checking if the platform can sink the sources heat and then, if so, sinking
	 * the power source as required.
	 * 
	 * @param heatSource the convective heat that is being received from the source
	 */
	public void onHeatSource(ConvectiveHeat heatSource)
	{
		logger.info(heatSource.toString());
		if (heatSource.name.isPresent())
		{
			String sourceName = heatSource.name.get();
			Optional<ConvectiveHeatSink> heatPort = Optional.empty();
			Optional<ConvectiveHeat> heatPortHeatIn = Optional.empty();
			if (sourceName.contains(C4S2SystemComponentsEnum.C4S2ServicesComputer.name()))
			{
				heatPort = Optional.of(heatC4S2ServicesComputer);
				heatPortHeatIn = Optional.of(heatInC4S2ServicesComputer);
			}
			else if (sourceName.contains(C4S2SystemComponentsEnum.C4S2OperatorServicesComputer.name()))
			{
				heatPort = Optional.of(heatC4S2OperatorServicesComputer);
				heatPortHeatIn = Optional.of(heatInC4S2OperatorServicesComputer);
			}
			else if (sourceName.contains(C4S2SystemComponentsEnum.EthernetSwitchIPRouter.name()))
			{
				heatPort = Optional.of(heatEthernetSwitchIPRouter);
				heatPortHeatIn = Optional.of(heatInEthernetSwitchIPRouter);
			}
			else if (sourceName.contains(C4S2SystemComponentsEnum.SIPRNetRouter.name()))
			{
				heatPort = Optional.of(heatSIPRNetRouter);
				heatPortHeatIn = Optional.of(heatInSIPRNetRouter);
			}
			if (heatPort.isPresent())
			{
				totalHeatIn.heat.value -= heatPortHeatIn.get().heat.value;
				if (!isCapacityForAdditional(heatSource))
					logger.warning("heat source exceeds heat sink capacity: " + heatSource.toString());
				heatPortHeatIn.get().heat.value = heatSource.heat.value;
				totalHeatIn.heat.value += heatSource.heat.value;
			}
			else
				logger.severe("unable to map heat source to available heat ports: " + heatSource.toString());
		}
		else
			logger.severe("missing/unknown identity of heat source: " + heatSource.toString());
	}

	/**
	 * Reacts to event of a weight source appearing, i.e. a system in a rack sources
	 * its weight on the rack mount point. Reaction is to check if the platform can
	 * sink the sources weight. If it can, the rack mount point is identified and
	 * the weight is added to the rack mount rail as determined from the index.
	 * 
	 * @param mountPointIndex index to the actual rack rail hole to sink the weight.
	 *                        The rack rail is the quotient for a divisor of 10 and
	 *                        the hole is the modulo of 10.
	 * @param weightSource    the weight force that is being received from the
	 *                        source
	 */
	public void onWeightSource(ForceNewtons weightSource, IInteger mountPointIndex)
	{
		logger.info(weightSource.toString());
		if (!isCapacityForAdditional(weightSource))
			logger.warning("weightSource exceeds weight sink capacity: " + weightSource.toString());
		totalWeightIn.value += weightSource.value;

		long rackMountHoleIndex = mountPointIndex.dividedBy(10);
		switch ((int)mountPointIndex.moduloOf(10))
		{
		case 0:
			rackMount.weightOnRailLeftFront.add((int)rackMountHoleIndex, weightSource);
			break;
		case 1:
			rackMount.weightOnRailRightFront.add((int)rackMountHoleIndex, weightSource);
			break;
		case 2:
			rackMount.weightOnRailLeftRear.add((int)rackMountHoleIndex, weightSource);
			break;
		case 3:
			rackMount.weightOnRailRightRear.add((int)rackMountHoleIndex, weightSource);
			break;
		}
	}

	/**
	 * Returns whether or not the platform has the capacity for the additional power
	 * sink.
	 * 
	 * @param power additional power to be sourced
	 * @return true if capacity is available, false otherwise
	 */
	private boolean isCapacityForAdditional(ElectricalPower power)
	{
		return power.current.lessThan(maxPowerOut.current.subtracted(totalPowerOut.current));
	}

	/**
	 * Returns whether or not the platform has the capacity for the additional heat
	 * source.
	 * 
	 * @param heat additional heat to be sunk
	 * @return true if capacity is available, false otherwise
	 */
	private boolean isCapacityForAdditional(ConvectiveHeat heat)
	{
		return heat.heat.lessThanOrEqualTo(maxHeatIn.heat.subtracted(totalHeatIn.heat));
	}

	/**
	 * Returns whether or not the platform has the capacity for the additional
	 * weight source.
	 * 
	 * @param weight additional weight to be sunk
	 * @return true if capacity is available, false otherwise
	 */
	private boolean isCapacityForAdditional(ForceNewtons weight)
	{
		return weight.lessThanOrEqualTo(maxWeightIn.subtracted(totalWeightIn));
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new C4PlatformStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		super.createValues();
		maxCurrentOut = new CurrentAmps(65);
		maxPowerOut = new ElectricalPower(standard50Hz, standard110V, maxCurrentOut);
		maxHeatIn = new ConvectiveHeat(new HeatWatts(800));
		maxWeightIn = new ForceNewtons(1000);
		totalHeatIn = new ConvectiveHeat(new HeatWatts(0));
		totalWeightIn = new ForceNewtons(0);
		totalPowerOut = new ElectricalPower(standard50Hz, standard110V, new CurrentAmps(0));
	}

	@Override
	protected void createFlows()
	{
		powerOutEthernetSwitchIPRouter = new ElectricalPower(standard50Hz, standard110V, new CurrentAmps(0));
		powerOutSIPRNetRouter = new ElectricalPower(standard50Hz, standard110V, new CurrentAmps(0));
		powerOutC4S2ServicesComputer = new ElectricalPower(standard50Hz, standard110V, new CurrentAmps(0));
		powerOutC4S2OperatorServicesComputer = new ElectricalPower(standard50Hz, standard110V, new CurrentAmps(0));
		heatInEthernetSwitchIPRouter = new ConvectiveHeat(new HeatWatts(0));
		heatInSIPRNetRouter = new ConvectiveHeat(new HeatWatts(0));
		heatInC4S2ServicesComputer = new ConvectiveHeat(new HeatWatts(0));
		heatInC4S2OperatorServicesComputer = new ConvectiveHeat(new HeatWatts(0));
	}

	@Override
	protected void createFullPorts()
	{
		powerC4S2ServicesComputer = new ElectricalPowerSource(this, 0L);
		powerEthernetSwitchIPRouter = new ElectricalPowerSource(this, 0L);
		powerOperatorServicesComputer = new ElectricalPowerSource(this, 0L);
		powerSIPRNetRouter = new ElectricalPowerSource(this, 0L);
		heatEthernetSwitchIPRouter = new ConvectiveHeatSink(this, 0L);
		heatSIPRNetRouter = new ConvectiveHeatSink(this, 0L);
		heatC4S2OperatorServicesComputer = new ConvectiveHeatSink(this, 0L);
		heatC4S2ServicesComputer = new ConvectiveHeatSink(this, 0L);
		rackMount = new RackMountStructure(this, 0L);
	}
}
