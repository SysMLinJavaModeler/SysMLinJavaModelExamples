package cablestayedbridge.ports;

import cablestayedbridge.Load;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

/**
 * SysML signal for the transmission of a load to a load-bearing component of
 * the bridge or domain
 * 
 * @author ModelerOne
 *
 */
public class LoadSignal extends SysMLSignal
{
	/**
	 * Load to be transmitted
	 */
	@Attribute
	public Load load;

	/**
	 * Constructor
	 * 
	 * @param load load to be transmitted
	 */
	public LoadSignal(Load load)
	{
		super("LoadSignal", 0L);
		this.load = load;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("LoadSignal [load=");
		builder.append(load);
		builder.append("]");
		return builder.toString();
	}
}
