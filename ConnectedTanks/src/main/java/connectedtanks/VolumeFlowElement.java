package connectedtanks;

import java.util.Optional;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.ports.SysMLProxyPort;
import sysmlinjava.valuetypes.DurationSeconds;

/**
 * Port for a flowing volume of fluid across an interface in terms of its rate
 * of flow and pressure. This port is specifically implemented for the flowing
 * volumes into/out of a {@code Tank} that is connected to a {@code Pipe}.
 * 
 * @author ModelerOne
 *
 */
public class VolumeFlowElement extends SysMLProxyPort
{
	/**
	 * Volume that is flowing across the interface/via the port
	 */
	@Value
	FlowingVolume vf;

	/**
	 * Constructor
	 * 
	 * @param contextBlock             the block of which this element is a port
	 * @param implementingContextBlock the block in which this proxy port's
	 *                                 interface is implemented, i.e. a {@code Tank}
	 * @param index                    unique ID of the port, e.g. index if member of
	 *                                 an array of ports
	 */
	public VolumeFlowElement(SysMLBlock contextBlock, Optional<? extends SysMLBlock> implementingContextBlock, Long id)
	{
		super(contextBlock, implementingContextBlock, id);
	}

	/**
	 * Retrieves the current flowing volume values at the interface represented by
	 * the port by invoking the proxy ports interface operation on the implementing
	 * context block (a {@code Tank}) to return its flowing volume values.
	 * 
	 * @return the flowing volume values for the interface
	 */
	public FlowingVolume getVolumeFlow()
	{
		FlowingVolume result = null;
		if (!connectedPortsPeers.isEmpty())
			result = ((VolumeFlowElement)connectedPortsPeers.get(0)).getVolumeFlow();
		else if (implementingContextBlock.isPresent())
			result = ((Tank)implementingContextBlock.get()).getVolumeFlow();
		vf.p.value = result.p.value;
		vf.q.value = result.q.value;
		return result;
	}

	/**
	 * Sets the current flowing volume values at the interface represented by the
	 * port by invoking the proxy ports interface operation on the implementing
	 * context block (a {@code Tank}) to set its flowing volume values.
	 * 
	 * @param flowingVolume values for the volume of fluid flowing across the
	 *                      interface, i.e. rate of flow and pressure
	 * @param duration      time during which the values are current
	 * 
	 */
	public void setVolumeFlow(FlowingVolume flowingVolume, DurationSeconds duration)
	{
		if (implementingContextBlock.isPresent())
			((Tank)implementingContextBlock.get()).setVolumeFlow(flowingVolume, duration);
		else if (!connectedPortsPeers.isEmpty())
			((VolumeFlowElement)connectedPortsPeers.get(0)).setVolumeFlow(flowingVolume, duration);
		vf.p.setValue(flowingVolume.p);
		vf.q.setValue(flowingVolume.q);
	}

	@Override
	protected void createValues()
	{
		vf = new FlowingVolume(new VolumeFlowRate(0), new Pressure(0));
	}
}
