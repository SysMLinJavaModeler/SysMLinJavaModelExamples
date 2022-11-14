package hflink.components.switchrouter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import hflink.common.objects.IPPacket;
import hflink.common.ports.ElectricalPowerProtocol;
import hflink.common.ports.EthernetProtocol;
import hflink.common.ports.IP;
import hflink.common.ports.InternetRoutingProtocol;
import hflink.common.ports.ThermalHeatTransferProtocol;
import sysmlinjava.annotations.AssociationConnector;
import sysmlinjava.annotations.AssociationConnectorFunction;
import sysmlinjava.annotations.Flow;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Operation;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.connectors.SysMLAssociationBlockConnector;
import sysmlinjava.connectors.SysMLAssociationBlockConnectorFunction;
import sysmlinjava.valuetypes.Cost$US;
import sysmlinjava.valuetypes.HeatWatts;
import sysmlinjava.valuetypes.MassKilograms;
import sysmlinjava.valuetypes.Percent;
import sysmlinjava.valuetypes.PowerWatts;
import sysmlinjava.valuetypes.QuantityEach;
import sysmlinjava.valuetypes.VolumeMetersCubic;

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
public class EthernetSwitchIPRouter extends SysMLBlock
{
	/**
	 * Port for the ethernet protocol 0
	 */
	@FullPort
	public EthernetProtocol ethernet0;
	/**
	 * Port for the ethernet protocol 1
	 */
	@FullPort
	public EthernetProtocol ethernet1;
	/**
	 * Port for the ethernet protocol 2
	 */
	@FullPort
	public EthernetProtocol ethernet2;
	/**
	 * Port for the ethernet protocol 3
	 */
	@FullPort
	public EthernetProtocol ethernet3;
	/**
	 * Port for the internet protocol (IP)
	 */
	@FullPort
	public IP ip;
	/**
	 * Port for the IP routing protocol
	 */
	@FullPort
	public InternetRoutingProtocol ipRouting;

	/**
	 * Port for electric power input
	 */
	@FullPort
	public ElectricalPowerProtocol electricPower;

	/**
	 * Port for heat transfer output
	 */
	@FullPort
	public ThermalHeatTransferProtocol thermalHeat;

	/**
	 * Flow of max heat output
	 */
	@Flow
	public HeatWatts maxHeatOut;
	/**
	 * Flow of max poer input
	 */
	@Flow
	public PowerWatts maxPowerIn;

	/**
	 * Value for switch/router availability
	 */
	@Value
	public Percent systemAvailability;
	/**
	 * Value for max size of switch/router
	 */
	@Value
	public VolumeMetersCubic maximumSize;
	/**
	 * Value for max weight of switch/router
	 */
	@Value
	public MassKilograms maximumWeight;
	/**
	 * Value for max cost of switch/router
	 */
	@Value
	public Cost$US maximumCost;

	/**
	 * Value of number of ethernet ports to be available
	 */
	@Value
	public QuantityEach numberEthernetPorts;

	/**
	 * Function to make connection between ethernet protocols to IP
	 */
	@AssociationConnectorFunction
	public SysMLAssociationBlockConnectorFunction ethernetToIPConnectorsFunction;
	/**
	 * Connector that invokdes the function to make connection between ethernet
	 * protocols to IP
	 */
	@AssociationConnector
	public SysMLAssociationBlockConnector ethernetToIPConnectors;

	/**
	 * List (ordered) of ethernet ports for list-type access to the ports
	 */
	private List<EthernetProtocol> ethernetPorts;

	/**
	 * Map of IP addresses to ethernet ports
	 */
	public IPAddressToEthernetPortMap ipToEthernetMap;

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
	@Operation
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
	protected void createValues()
	{
		numberEthernetPorts = new QuantityEach(4);
		systemAvailability = new Percent(99.99);
		maximumSize = new VolumeMetersCubic(0.05);
		maximumWeight = new MassKilograms(2.0);
		maximumCost = new Cost$US(2_500);
	}

	@Override
	protected void createFlows()
	{
		maxPowerIn = new PowerWatts(100);
		maxHeatOut = new HeatWatts(100);
	}

	@Override
	protected void createFullPorts()
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
	protected void createConnectorFunctions()
	{
		ethernetToIPConnectorsFunction = () ->
		{
			ethernet0.addConnectedPortClient(ip);
			ethernet1.addConnectedPortClient(ip);
			ethernet2.addConnectedPortClient(ip);
			ethernet3.addConnectedPortClient(ip);
			ipRouting.addConnectedPortServer(ethernet0);
			ipRouting.addConnectedPortServer(ethernet1);
			ipRouting.addConnectedPortServer(ethernet2);
			ipRouting.addConnectedPortServer(ethernet3);
		};
	}

	@Override
	protected void createConnectors()
	{
		ethernetToIPConnectors = new SysMLAssociationBlockConnector(this, this, ethernetToIPConnectorsFunction);
	}

	public class IPAddressToEthernetPortMap extends HashMap<Integer, Integer>
	{
		private static final long serialVersionUID = -3171005757832433632L;

		public IPAddressToEthernetPortMap()
		{
			super();
		}

		public Integer ethernetPortFor(Integer ipAddress)
		{
			return get(ipAddress);
		}
	}
}
