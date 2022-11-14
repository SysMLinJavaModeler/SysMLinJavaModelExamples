package cablestayedbridge.ports;

import cablestayedbridge.Load;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.valuetypes.DirectionDegrees;
import sysmlinjava.valuetypes.Point2D;
import sysmlinjava.valuetypes.WeightPounds;

/**
 * SysML signal event for the transmission of a load to a load-bearing component
 * of the bridge or domain
 * 
 * @author ModelerOne
 *
 */
public class LoadSignalEvent extends SysMLSignalEvent
{
	/**
	 * Constructor
	 * 
	 * @param loadSignal the signal that transmits the load
	 */
	public LoadSignalEvent(LoadSignal loadSignal)
	{
		super("LoadSignalEvent");
		((LoadSignal)signal).load.name = loadSignal.load.name;
		((LoadSignal)signal).load.id = loadSignal.load.id;
		((LoadSignal)signal).load.weight.value = loadSignal.load.weight.value;
		((LoadSignal)signal).load.direction.value = loadSignal.load.direction.value;
		((LoadSignal)signal).load.location.xValue = loadSignal.load.location.xValue;
		((LoadSignal)signal).load.location.yValue = loadSignal.load.location.yValue;
		((LoadSignal)signal).load.lastLoad = loadSignal.load.lastLoad;
	}

	@Override
	public void createSignal()
	{
		signal = new LoadSignal(new Load(0, new WeightPounds(0), new Point2D(0, 0), new DirectionDegrees(0), false));
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("LoadSignalEvent [signal=");
		builder.append(signal);
		builder.append(", index=");
		builder.append(index);
		builder.append("]");
		return builder.toString();
	}
}
