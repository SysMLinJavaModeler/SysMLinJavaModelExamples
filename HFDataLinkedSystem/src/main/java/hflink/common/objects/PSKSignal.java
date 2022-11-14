package hflink.common.objects;

import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;

/**
 * Representation of the phase-shift keyed signal that carries the information
 * of a slot in the TDMA protocol
 * 
 * @author ModelerOne
 *
 */
public class PSKSignal extends SysMLClass implements StackedProtocolObject
{
	public boolean isCorrupted;
	@Attribute
	public TDMASlot data;

	public PSKSignal(TDMASlot data)
	{
		super();
		this.isCorrupted = false;
		this.data = data;
	}

	public PSKSignal(PSKSignal data)
	{
		super();
		this.isCorrupted = data.isCorrupted;
		this.data = new TDMASlot(data.data);
	}

	public PSKSignal()
	{
		super();
		this.isCorrupted = false;
		this.data = new TDMASlot();
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.of(data));
	}

	@Override
	public String toString()
	{
		return String.format("PSKSignal [isCorrupted=%s, data=%s]", isCorrupted, data);
	}

}
