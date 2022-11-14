package c4s2.components.computer.operator;

import static sysmlinjava.valuetypes.ElectricalPower.standard110V;
import static sysmlinjava.valuetypes.ElectricalPower.standard50Hz;
import java.util.List;
import java.util.Optional;
import c4s2.common.valueTypes.C4S2OperatorServicesComputerStatesEnum;
import c4s2.common.valueTypes.C4S2SystemComponentsEnum;
import c4s2.components.common.LinuxOS;
import c4s2.components.services.operator.OperatorServices;
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
 * The {@code C4S2OperatorServicesComputer} is a SysMLinJava model of the
 * computer that provides services to the operator of the {@code C4S2System}.
 * The computer's main function is to provide the displays and services needed
 * for the operator to interact with the {@code C4S2System} and, via the
 * {@code C4S2System}, with the attached radar and strike systems, ultimately to
 * perform the F2T2EA process. In essence, the
 * {@code C4S2OperatorServicesComputer} is the operator's "user interface" with
 * the systems in the C4S2 domain.
 * <p>
 * The {@code C4S2OperatorServicesComputer} has a number of communications
 * protocol ports that it uses to communicate with other systems. One set of
 * ports - ethernet, IP, and UDP ports - provide a standard protocol stack for
 * internetwork communications. Another port provides an SNMP agent
 * communications, presumably over the internetwork protocol stack. There is a
 * port for receiving electrical power as well as one for the switching of power
 * on/off mechanically and one for switching the power on/off electronically.
 * There is a port for transfering heat to the platform and another for
 * tranfering the computer's space and weight to the platform, i.e. its rack
 * mount points.
 * <p>
 * The {@code C4S2OperatorServicesComputer} also has a number of values that
 * further define it. These values include its current power input, heat output,
 * and its weight. Also defined are its maximum size, weight, power, and heat.
 * Receptions performed by the computer include reaction to power being switched
 * on or off and to receiving SNMP controls. The
 * {@code C4S2OperatorServicesComputer} also holds the connectors between the
 * electronic and mechanical power switches which are connected to provide a
 * common power switching capability.
 * <p>
 * Of course, the primary component part of the
 * {@code C4S2OperatorServicesComputer} is the {@code OperatorServices} software
 * service. This service provides the display content for all the operator
 * displays and translates the operator's displays to/from messages to the other
 * services and systems for their actions as applicable.
 * 
 * @author ModelerOne
 *
 * @see c4s2.components.services.operator.OperatorServices
 * @see sysmlinjavalibrary.common.ports.energy.electrical.ElectricalPowerSink
 * @see sysmlinjavalibrary.common.ports.energy.mechanical.ComponentMountStructure
 * @see sysmlinjavalibrary.common.ports.energy.mechanical.MechanicalOnOffSwitch
 * @see sysmlinjavalibrary.common.ports.energy.mechanical.MechanicalOnOffSwitchContact
 * @see sysmlinjavalibrary.common.ports.energy.thermal.ConvectiveHeatSource
 * @see sysmlinjavalibrary.common.ports.information.EthernetProtocol
 * @see sysmlinjavalibrary.common.ports.information.InternetProtocol
 * @see sysmlinjavalibrary.common.ports.information.SNMPAgentProtocol
 * @see sysmlinjavalibrary.common.ports.information.UserDatagramProtocol
 */
public class C4S2OperatorServicesComputer extends SysMLBlock
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
	public OperatorServices c4s2OperatorServices;
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
	public HeatWatts maxHeatOut;
	@Value
	public Cost$US maxCost;
	@Value
	public QuantityEach numberMountPoints;
	@Value
	public IInteger rackMountHole;

	public C4S2OperatorServicesComputerStatesEnum currentState;

	@AssociationConnectorFunction
	private SysMLAssociationBlockConnectorFunction electronicToMechanicalPowerOnOffSwitchConnectorFunction;

	@AssociationConnector
	private SysMLAssociationBlockConnector electronicToMechanicalPowerOnOffSwitchConnector;
	public PowerWatts maxPowerIn;

	public C4S2OperatorServicesComputer()
	{
		super(C4S2SystemComponentsEnum.C4S2OperatorServicesComputer.name(), 0L);
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
		logger.info("initializing...");
		double weightPerMountPoint = weightOut.value / numberMountPoints.value;
		rackMount.mountLeftFront .transmit(new ForceNewtons(weightPerMountPoint, 0, rackMountHole.value + 0));
		rackMount.mountRightFront.transmit(new ForceNewtons(weightPerMountPoint, 0, rackMountHole.value + 1));
		rackMount.mountLeftRear  .transmit(new ForceNewtons(weightPerMountPoint, 0, rackMountHole.value + 2));
		rackMount.mountRightRear .transmit(new ForceNewtons(weightPerMountPoint, 0, rackMountHole.value + 3));
	}

	@Reception
	public void onSwitchToPowerOn()
	{
		logger.info("switch to power on");
		powerIn.current.setValue(maxCurrentIn.added(minCurrentIn).dividedBy(2.0)); //start with medium activity
		powerIn.name = this.name;
		electricalPower.transmit(new ElectricalPower(powerIn));
	}

	@Reception
	public void onSwitchToPowerOff()
	{
		logger.info("switch to power off");
		powerIn.current.setValue(0.0); //all stopped
		electricalPower.transmit(powerIn);
	}

	@Reception
	public void onElectricalPowerOn(ElectricalPower power)
	{
		logger.info(power.toString());
		if (power.current.greaterThanOrEqualTo(minCurrentIn) && power.current.lessThanOrEqualTo(maxCurrentIn))
		{
			powerIn.current.setValue(power.current);
			heatOut.name = Optional.of(C4S2SystemComponentsEnum.C4S2OperatorServicesComputer.name());
			heatOut.heat.setValue(power.watts());
			convectiveHeat.transmit(heatOut);
			operatingSystem.start();
			c4s2OperatorServices.start();
		}
		else
			logger.severe("power not in acceptable range: " + power.toString());
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
				mib = new MIB(InstantMilliseconds.now(), this.getClass().getSimpleName(), C4S2OperatorServicesComputerStatesEnum.Operational.toString());
				snmpAgent.transmit(new SNMPResponse(InstantMilliseconds.now(), mib));
			}
		}
	}

	@Reception
	public void onSNMPRequestToPowerOff()
	{
		logger.info("control to power off");
		c4s2OperatorServices.stop();
		operatingSystem.stop();
		electronicPowerOnOffSwitch.transmit(new OnOffSwitch(false));
	}

	@Reception
	public void onElectricalPowerOff(ElectricalPower power)
	{
		logger.info(power.toString());
		powerIn.current.value = 0;
		heatOut.heat.value = 0;
		convectiveHeat.transmit(heatOut);
		MIB mib = new MIB(InstantMilliseconds.now(), this.getClass().getSimpleName(), C4S2OperatorServicesComputerStatesEnum.PowerOff.toString());
		snmpAgent.transmit(new SNMPResponse(InstantMilliseconds.now(), mib));
		delay(2);
		acceptEvent(new FinalEvent());
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new C4S2OperatorServicesComputerStateMachine(this));
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
		rackMountHole = new IInteger(1);
	}

	@Override
	protected void createFlows()
	{
		sizeOut = new VolumeMetersCubic(0.04);
		powerIn = new ElectricalPower(standard50Hz, standard110V, new CurrentAmps(0));
		powerIn.name = Optional.of(name.isPresent() ? name.get() : getClass().getSimpleName());
		heatOut = new ConvectiveHeat(new HeatWatts(0));
		weightOut = new ForceNewtons(40);
	}

	@Override
	protected void createParts()
	{
		operatingSystem = new LinuxOS();
		c4s2OperatorServices = new OperatorServices();
	}

	@Override
	protected void createFullPorts()
	{
		snmpAgent = new SNMPAgentProtocol(this, 0L);
		udp = new UserDatagramProtocol(this, 0L);
		ip = new InternetProtocol(this, 0L);
		ethernet = new EthernetProtocol(this, 0L);
		mechanicalPowerOnOffSwitch = new MechanicalOnOffSwitch(this, 0L);
		electronicPowerOnOffSwitch = new MechanicalOnOffSwitchContact(this, 0L);
		electricalPower = new ElectricalPowerSink(this, 0L);
		convectiveHeat = new ConvectiveHeatSource(this, 0L);
		rackMount = new ComponentMountStructure(this, 0L);
	}

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
