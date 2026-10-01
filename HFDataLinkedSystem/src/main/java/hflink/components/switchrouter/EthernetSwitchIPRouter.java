package hflink.components.switchrouter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hflink.common.items.IPPacket;
import hflink.common.ports.ElectricalPowerProtocol;
import hflink.common.ports.EthernetProtocol;
import hflink.common.ports.IP;
import hflink.common.ports.InternetRoutingProtocol;
import hflink.common.ports.ThermalHeatTransferProtocol;
import sysmlinjava.attributetypes.Cost$US;
import sysmlinjava.attributetypes.HeatWatts;
import sysmlinjava.attributetypes.MassKilograms;
import sysmlinjava.attributetypes.Percent;
import sysmlinjava.attributetypes.PowerWatts;
import sysmlinjava.attributetypes.QuantityEach;
import sysmlinjava.attributetypes.VolumeMetersCubic;
import sysmlinjava.connectors.SysMLFlowConnector;
import sysmlinjava.connectors.SysMLFlowConnector.TypesEnum;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.connectors.FlowConnector;
import sysmlinjava.javaannotations.ports.Port;
import sysmlinjava.parts.SysMLPart;

/**
 * The EthernetSwitchIPRouter is a system component that performs local-area
 * network and internetwork communications for connected computers and devices.
 * It transmits and receives IP packets encapsulated in ethernet packets and
 * routes them to connected devices in accordance with installed routing tables.
 * The EthernetSeitchIPRouter operates in accordance with standard IP and
 * Ethernet protocol specifications.
 * <p>
 * The EthernetSwitchIPRouter block includes full ports for each of the
 * protocols used to communicate with the connected devices. These protocols
 * consist of IP over ethernet as well as an internet routing protocol. It also
 * includes full ports for power and heat and their corresponding flows -
 * power-in and heat-out. Specified block values include availability, size,
 * weight, speed, and cost.
 * <p>
 * The block also contains all of the connectors between the ports in the block.
 * These include the connectors between the full ports that represent the
 * protocol stacks of the external interfaces. Note the connectors between
 * EthernetSwitchIPRouter protocols and external systems are specified in the
 * system block that contains the switch/router as a part.
 * 
 * @author ModelerOne
 *
 */
public class EthernetSwitchIPRouter extends SysMLPart
{
	/**
	 * Port for the ethernet protocol 0
	 */
	@Port
	public EthernetProtocol ethernet0;
	/**
	 * Port for the ethernet protocol 1
	 */
	@Port
	public EthernetProtocol ethernet1;
	/**
	 * Port for the ethernet protocol 2
	 */
	@Port
	public EthernetProtocol ethernet2;
	/**
	 * Port for the ethernet protocol 3
	 */
	@Port
	public EthernetProtocol ethernet3;
	/**
	 * Port for the internet protocol (IP)
	 */
	@Port
	public IP ip;
	/**
	 * Port for the IP routing protocol
	 */
	@Port
	public InternetRoutingProtocol ipRouting;

	/**
	 * Port for electric power input
	 */
	@Port
	public ElectricalPowerProtocol electricPower;

	/**
	 * Port for heat transfer output
	 */
	@Port
	public ThermalHeatTransferProtocol thermalHeat;

	/**
	 * Flow of max heat output
	 */
	@Attribute
	public HeatWatts maxHeatOut;
	/**
	 * Flow of max poer input
	 */
	@Attribute
	public PowerWatts maxPowerIn;

	/**
	 * Value for switch/router availability
	 */
	@Attribute
	public Percent systemAvailability;
	/**
	 * Value for max size of switch/router
	 */
	@Attribute
	public VolumeMetersCubic maximumSize;
	/**
	 * Value for max weight of switch/router
	 */
	@Attribute
	public MassKilograms maximumWeight;
	/**
	 * Value for max cost of switch/router
	 */
	@Attribute
	public Cost$US maximumCost;

	/**
	 * Value of number of ethernet ports to be available
	 */
	@Attribute
	public QuantityEach numberEthernetPorts;

	/**
	 * Connector between ethernet
	 * protocols to IP
	 */
	@FlowConnector
	public SysMLFlowConnector ethernetToIP;
	/**
	 * Connector between IP routing protocol to ethernet
	 * protocols
	 */
	@FlowConnector
	public SysMLFlowConnector ipRoutingToEthernet;

	/**
	 * List (ordered) of ethernet ports for list-type access to the ports
	 */
	private List<EthernetProtocol> ethernetPorts;

	/**
	 * Map of IP addresses to ethernet ports
	 */
	public IPAddressToEthernetPortMap ipToEthernetMap;

	/**
	 * Constructor
	 */
	public EthernetSwitchIPRouter()
	{
		super();
		ipToEthernetMap = new IPAddressToEthernetPortMap();
		ethernetPorts = new ArrayList<>();
		ethernetPorts.add(ethernet0);
		ethernetPorts.add(ethernet1);
		ethernetPorts.add(ethernet2);
		ethernetPorts.add(ethernet3);
	}

	/**
	 * Operation to route the received IP packet
	 * 
	 * @param nextPacket packet to be routed
	 */
	@Action
	public void routeIPPacket(IPPacket nextPacket)
	{
		ipRouting.transmit(nextPacket);
	}

	@Override
	public void start()
	{
		ethernetPorts.forEach(port -> port.start());
		super.start();
	}

	@Override
	public void stop()
	{
		ethernetPorts.forEach(port -> port.stop());
		super.stop();
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new EthernetSwitchIPRouterStateMachine(this));
	}

	@Override
	protected void createAttributes()
	{
		numberEthernetPorts = new QuantityEach(4);
		systemAvailability = new Percent(99.99);
		maximumSize = new VolumeMetersCubic(0.05);
		maximumWeight = new MassKilograms(2.0);
		maximumCost = new Cost$US(2_500);
		maxPowerIn = new PowerWatts(100);
		maxHeatOut = new HeatWatts(100);
	}

	@Override
	protected void createPorts()
	{
		ethernet0 = new EthernetProtocol(this, 0L);
		ethernet1 = new EthernetProtocol(this, 1L);
		ethernet2 = new EthernetProtocol(this, 2L);
		ethernet3 = new EthernetProtocol(this, 3L);
		ip = new IP(this, this, 0L);
		ipRouting = new InternetRoutingProtocol(this, 0L);

		electricPower = new ElectricalPowerProtocol(this, 0L);
		thermalHeat = new ThermalHeatTransferProtocol(this, 0L);
	}

	@Override
	protected void createFlowConnectors()
	{
		ethernetToIP = new SysMLFlowConnector(TypesEnum.servertoclient, false,
		List.of(ethernet0,ethernet1,ethernet2,ethernet3), List.of(ip, ip, ip, ip),"", 0L);
		ipRoutingToEthernet = new SysMLFlowConnector(TypesEnum.clienttoserver, false,
		List.of(ipRouting, ipRouting, ipRouting, ipRouting), List.of(ethernet0,ethernet1,ethernet2,ethernet3),"", 0L);
	}
}
