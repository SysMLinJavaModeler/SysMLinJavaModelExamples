package c4s2.components.computer.services;

import static sysmlinjava.valuetypes.ElectricalPower.standard110V;
import static sysmlinjava.valuetypes.ElectricalPower.standard50Hz;
import java.util.List;
import java.util.Optional;
import c4s2.common.valueTypes.C4S2ServicesComputerStatesEnum;
import c4s2.common.valueTypes.C4S2SystemComponentsEnum;
import c4s2.components.common.LinuxOS;
import c4s2.components.services.radar.RadarServices;
import c4s2.components.services.strike.StrikeServices;
import c4s2.components.services.system.SystemServices;
import c4s2.components.services.target.TargetServices;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.Reception;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.kinds.SysMLFlowDirectionKind;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.Cost$US;
import sysmlinjava.valuetypes.CurrentAmps;
import sysmlinjava.valuetypes.ElectricalPower;
import sysmlinjava.valuetypes.ForceNewtons;
import sysmlinjava.valuetypes.HeatWatts;
import sysmlinjava.valuetypes.IInteger;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.PowerWatts;
import sysmlinjava.valuetypes.QuantityEach;
import sysmlinjava.valuetypes.VolumeMetersCubic;
import sysmlinjavalibrary.common.objects.energy.thermal.ConvectiveHeat;
import sysmlinjavalibrary.common.objects.information.MIB;
import sysmlinjavalibrary.common.objects.information.OnOffSwitch;
import sysmlinjavalibrary.common.objects.information.SNMPRequest;
import sysmlinjavalibrary.common.objects.information.SNMPResponse;
import sysmlinjavalibrary.common.ports.energy.electrical.ElectricalPowerSink;
import sysmlinjavalibrary.common.ports.energy.mechanical.ComponentMountStructure;
import sysmlinjavalibrary.common.ports.energy.mechanical.MechanicalOnOffSwitch;
import sysmlinjavalibrary.common.ports.energy.mechanical.MechanicalOnOffSwitchContact;
import sysmlinjavalibrary.common.ports.energy.thermal.ConvectiveHeatSource;
import sysmlinjavalibrary.common.ports.information.EthernetProtocol;
import sysmlinjavalibrary.common.ports.information.InternetProtocol;
import sysmlinjavalibrary.common.ports.information.SNMPAgentProtocol;
import sysmlinjavalibrary.common.ports.information.UserDatagramProtocol;

/**
 * The {@code C4S2ServicesComputer} is a SysMLinJava model of the computer that
 * provides the services of the {@code C4S2System}. The computer's main function
 * is to provide services for the radar and strike systems as well as for
 * targeting/targeting algorithms and system monitor/control. In essence, the
 * {@code C4S2ServicesComputer} is the general service provider to the operator
 * service that resides in the {@code C4S2OperatorServicesComputer}.
 * <p>
 * The {@code C4S2ServicesComputer} has a number of communications protocol
 * ports that it uses to communicate with other systems. One set of ports -
 * ethernet, IP, and UDP ports - provide a standard protocol stack for
 * internetwork communications. Another port provides an SNMP agent
 * communications, presumably over the internetwork protocol stack. There is a
 * port for receiving electrical power as well as one for the switching of power
 * on/off mechanically and one for switching the power on/off electronically.
 * There is a port for transfering heat to the platform and another for
 * tranfering the computer's space and weight to the platform, i.e. its rack
 * mount points.
 * <p>
 * The {@code C4S2ServicesComputer} also has a number of values that further
 * define it. These values include its current power input, heat output, and its
 * weight. Also defined are its maximum size, weight, power, and heat.
 * Receptions performed by the computer include reaction to power being switched
 * on or off and to receiving SNMP controls. The {@code C4S2ServicesComputer}
 * also holds the connectors between the electronic and mechanical power
 * switches which are connected to provide a common power switching capability.
 * <p>
 * Of course, the primary component parts of the {@code C4S2ServicesComputer}
 * are the services, i.e. the {@code RadarServices} that provide all services on
 * the {@code RadarSystem}, the {@code StrikeServices} that provide all services
 * on the {@code StrikeSystem}, the {@code TargetServices} that provide all
 * services on the tracking/targeting algorithms, and the {@code SystemServices}
 * that provide all services for the monitoring and controlling of the
 * components and services of the {@code C4S2System}.
 * 
 * @author ModelerOne
 *
 * @see c4s2.components.services.radar.RadarServices
 * @see c4s2.components.services.strike.StrikeServices
 * @see c4s2.components.services.system.SystemServices
 * @see c4s2.components.services.target.TargetServices
 */
public class C4S2ServicesComputer extends SysMLBlock
{
	@FullPort
	public SNMPAgentProtocol snmpAgent;
	@FullPort
	public UserDatagramProtocol udp;
	@FullPort
	public InternetProtocol ip;
	@FullPort
	public EthernetProtocol ethernet;
	@FullPort
	public ElectricalPowerSink electricalPower;
	@FullPort
	public MechanicalOnOffSwitch mechanicalPowerOnOffSwitch;
	@FullPort
	public MechanicalOnOffSwitchContact electronicPowerOnOffSwitch;
	@FullPort
	public ConvectiveHeatSource convectiveHeat;
	@FullPort
	public ComponentMountStructure rackMount;

	@Part
	public SystemServices systemServices;
	@Part
	public RadarServices radarServices;
	@Part
	public TargetServices targetServices;
	@Part
	public StrikeServices strikeServices;
	@Part
	public LinuxOS operatingSystem;

	@Flow(direction = SysMLFlowDirectionKind.out)
	public VolumeMetersCubic sizeOut;
	@Flow(direction = SysMLFlowDirectionKind.in)
	public ElectricalPower powerIn;
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ConvectiveHeat heatOut;
	@Flow(direction = SysMLFlowDirectionKind.out)
	public ForceNewtons weightOut;

	@Value
	public VolumeMetersCubic maxSize;
	@Value
	public ForceNewtons maxWeight;
	@Value
	public CurrentAmps minCurrentIn;
	@Value
	public CurrentAmps maxCurrentIn;
	@Value
	public PowerWatts maxPowerIn;
	@Value
	public HeatWatts maxHeatOut;
	@Value
	public Cost$US maxCost;
	@Value
	public QuantityEach numberMountPoints;
	@Value
	public IInteger rackMountHole;

	@AssociationConnectorFunction
	private SysMLAssociationBlockConnectorFunction electronicToMechanicalPowerOnOffSwitchConnectorFunction;

	@AssociationConnector
	private SysMLAssociationBlockConnector electronicToMechanicalPowerOnOffSwitchConnector;

	public C4S2ServicesComputer()
	{
		super(C4S2SystemComponentsEnum.C4S2ServicesComputer.name, 0L);
	}

	@Override
	public void start()
	{
		super.start();
		maxSize.notifyValueChangeObservers();
		maxWeight.notifyValueChangeObservers();
		maxPowerIn.notifyValueChangeObservers();
		maxHeatOut.notifyValueChangeObservers();
	}

	@Operation
	public void initialize()
	{
		double weightPerMountPoint = weightOut.value / numberMountPoints.value;
		rackMount.mountLeftFront .transmit(new ForceNewtons(weightPerMountPoint, 0, rackMountHole.value + 0));
		rackMount.mountRightFront.transmit(new ForceNewtons(weightPerMountPoint, 0, rackMountHole.value + 1));
		rackMount.mountLeftRear  .transmit(new ForceNewtons(weightPerMountPoint, 0, rackMountHole.value + 2));
		rackMount.mountRightRear .transmit(new ForceNewtons(weightPerMountPoint, 0, rackMountHole.value + 3));
	}

	@Reception
	public void onSNMPRequest(SNMPRequest request)
	{
		logger.info(request.toString());
		List<String> dataStrings = request.mib.getDataStrings();
		if (dataStrings.get(0).contains(this.getClass().getSimpleName()))
		{
			String state = dataStrings.get(1);
			MIB mib;
			if (state.equals("Operational"))
			{
				mib = new MIB(InstantMilliseconds.now(), this.getClass().getSimpleName(), state);
				snmpAgent.transmit(new SNMPResponse(InstantMilliseconds.now(), mib));
			}
			else
			{
				logger.severe("invalid reception for requested state: " + state);
				mib = new MIB(InstantMilliseconds.now(), this.getClass().getSimpleName(), C4S2ServicesComputerStatesEnum.Operational.toString());
				snmpAgent.transmit(new SNMPResponse(InstantMilliseconds.now(), mib));
			}
		}
	}

	@Reception
	public void onSNMPRequestToPowerOff()
	{
		logger.info("control to power off");
		radarServices.stop();
		targetServices.stop();
		strikeServices.stop();
		systemServices.stop();
		operatingSystem.stop();
		electronicPowerOnOffSwitch.transmit(new OnOffSwitch(false));
	}

	@Reception
	public void onSwitchToPowerOn()
	{
		logger.info("switch to power on");
		powerIn.current.setValue(maxCurrentIn.added(minCurrentIn).dividedBy(2.0)); //assume "medium" activity
		powerIn.name = this.name;
		electricalPower.transmit(new ElectricalPower(powerIn));
	}

	@Reception
	public void onSwitchToPowerOff()
	{
		logger.info("switch to power off");
		powerIn.current.setValue(0.0);
		electricalPower.transmit(powerIn);
	}

	@Reception
	public void onElectricalPowerOn(ElectricalPower power)
	{
		logger.info(power.toString());
		if (power.current.greaterThanOrEqualTo(minCurrentIn) && power.current.lessThanOrEqualTo(maxCurrentIn))
		{
			powerIn.current.setValue(power.current);
			heatOut.heat.setValue(power.watts());
			heatOut.name = Optional.of(C4S2SystemComponentsEnum.C4S2ServicesComputer.name());
			convectiveHeat.transmit(heatOut);
			operatingSystem.start();
			radarServices.start();
			strikeServices.start();
			targetServices.start();
			systemServices.start();
		}
		else
			logger.severe("power not in acceptable range: " + power.toString());
	}

	@Reception
	public void onElectricalPowerOff(ElectricalPower power)
	{
		logger.info(power.toString());
		powerIn.current.setValue(0);
		heatOut.heat.setValue(0);
		convectiveHeat.transmit(heatOut);
		MIB mib = new MIB(InstantMilliseconds.now(), this.getClass().getSimpleName(), C4S2ServicesComputerStatesEnum.PowerOff.toString());
		snmpAgent.transmit(new SNMPResponse(InstantMilliseconds.now(), mib));
		delay(2);
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new C4S2ServicesComputerStateMachine(this));
	}

	@Override
	protected void createValues()
	{
		maxSize = new VolumeMetersCubic(0.1);
		maxWeight = new ForceNewtons(50.0);
		minCurrentIn = new CurrentAmps(2);
		maxCurrentIn = new CurrentAmps(7);
		maxPowerIn = new PowerWatts(maxCurrentIn.multipliedBy(standard110V));
		maxHeatOut = new HeatWatts(maxPowerIn.value);
		maxCost = new Cost$US(20_000.00);
		numberMountPoints = new QuantityEach(4);
		rackMountHole = new IInteger(0);
	}

	@Override
	protected void createFlows()
	{
		sizeOut = new VolumeMetersCubic(0.04);
		powerIn = new ElectricalPower(standard50Hz, standard110V, new CurrentAmps(0));
		heatOut = new ConvectiveHeat(new HeatWatts(0));
		weightOut = new ForceNewtons(40);
	}

	@Override
	protected void createParts()
	{
		operatingSystem = new LinuxOS();
		systemServices = new SystemServices();
		radarServices = new RadarServices();
		targetServices = new TargetServices();
		strikeServices = new StrikeServices();
	}

	@Override
	protected void createFullPorts()
	{
		super.createFullPorts();
		snmpAgent = new SNMPAgentProtocol(this, 0L);
		udp = new UserDatagramProtocol(this, 0L);
		ethernet = new EthernetProtocol(this, 0L);
		ip = new InternetProtocol(this, 0L);
		mechanicalPowerOnOffSwitch = new MechanicalOnOffSwitch(this, 0L);
		electronicPowerOnOffSwitch = new MechanicalOnOffSwitchContact(this, 0L);
		electricalPower = new ElectricalPowerSink(this, 0L);
		convectiveHeat = new ConvectiveHeatSource(this, 0L);
		rackMount = new ComponentMountStructure(this, 0L);
	}

	@Override
	protected void createConnectorFunctions()
	{
		electronicToMechanicalPowerOnOffSwitchConnectorFunction = () ->
		{
			electronicPowerOnOffSwitch.addConnectedPortPeer(mechanicalPowerOnOffSwitch);
		};
	}

	@Override
	protected void createConnectors()
	{
		electronicToMechanicalPowerOnOffSwitchConnector = new SysMLAssociationBlockConnector(this, this, electronicToMechanicalPowerOnOffSwitchConnectorFunction);
	}
}
