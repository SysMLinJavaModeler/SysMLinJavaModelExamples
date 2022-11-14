package hflink.common.signals;

import java.util.Optional;
import hflink.common.objects.DataLinkFrame;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

public class DataLinkFrameSignal extends SysMLSignal
{
	@Attribute
	public DataLinkFrame frame;

	public DataLinkFrameSignal(DataLinkFrame frame)
	{
		super();
		this.frame = frame;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("DataLinkFrameSignal [frame=%s]", frame);
	}
}
