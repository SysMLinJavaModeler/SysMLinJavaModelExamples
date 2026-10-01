package connectedtanks;

import java.util.Optional;

import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.PressureNewtonsPerMeterSquare;
import sysmlinjava.attributetypes.VolumeFlowMetersCubicPerSecond;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.ports.SysMLProxyPort;
import sysmlinjava.states.StateBehaviorContext;

/**
 * Port for a flowing volume of fluid across an interface in terms of its rate
 * of flow and pressure. This port is specifically implemented for the flowing
 * volumes into/out of a {@code Tank} that is connected to a {@code Pipe}.
 * 
 * @author ModelerOne
 */
public class VolumeFlowElement extends SysMLProxyPort
{
	/**
	 * Volume that is flowing across the interface/via the port
	 */
	@Attribute
	FlowingVolume vf;

	/**
	 * Constructor
	 * 
	 * @param context             the part or port of which this element is a port
	 * @param implementingContext the part or port in which this proxy port's
	 *                            interface is implemented, i.e. a {@code Tank}
	 * @param id                  unique ID of the port, index if member of an array
	 *                            of ports
	 */
	public VolumeFlowElement(StateBehaviorContext context, Optional<StateBehaviorContext> implementingContext, Long id)
	{
		super(context, implementingContext, "VolumeFlowElement", id);
	}

	/**
	 * Retrieves the current flowing volume values at the interface represented by
	 * the port by invoking the proxy ports interface operation on the implementing
	 * context part or port (a {@code Tank}) to return its flowing volume values.
	 * 
	 * @return the flowing volume values for the interface
	 */
	public FlowingVolume getVolumeFlow()
	{
		FlowingVolume result = null;
		if (!connectedPortsPeers.isEmpty())
			result = ((VolumeFlowElement) connectedPortsPeers.get(0)).getVolumeFlow();
		else if (implementingContext.isPresent())
			result = ((Tank) implementingContext.get()).getVolumeFlow();
		if (result != null)
		{
			vf.p.value = result.p.value;
			vf.q.value = result.q.value;
		}
		else
			logger.warning("no proxy port or implementing context available to calculate flowing volume");

		return result;
	}

	/**
	 * Sets the current flowing volume values at the interface represented by the
	 * port by invoking the proxy ports interface operation on the implementing
	 * context part or port (a {@code Tank}) to set its flowing volume values.
	 * 
	 * @param flowingVolume values for the volume of fluid flowing across the
	 *                      interface, i.e. rate of flow and pressure
	 * @param duration      time during which the values are current
	 */
	public void setVolumeFlow(FlowingVolume flowingVolume, DurationSeconds duration)
	{
		if (implementingContext.isPresent())
			((Tank) implementingContext.get()).setVolumeFlow(flowingVolume, duration);
		else if (!connectedPortsPeers.isEmpty())
			((VolumeFlowElement) connectedPortsPeers.get(0)).setVolumeFlow(flowingVolume, duration);
		vf.p.setValue(flowingVolume.p);
		vf.q.setValue(flowingVolume.q);
	}

	@Override
	protected void createAttributes()
	{
		vf = new FlowingVolume(new VolumeFlowMetersCubicPerSecond(0), new PressureNewtonsPerMeterSquare(0));
	}
}
