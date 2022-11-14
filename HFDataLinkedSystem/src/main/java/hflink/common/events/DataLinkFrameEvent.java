package hflink.common.events;

import hflink.common.objects.DataLinkFrame;
import hflink.common.signals.DataLinkFrameSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * Event for the receipt of a data-link frame
 * 
 * @author ModelerOne
 *
 */
public class DataLinkFrameEvent extends SysMLSignalEvent
{
	public DataLinkFrameEvent(DataLinkFrame frame)
	{
		super("DataLinkFrame");
		((DataLinkFrameSignal)signal).frame = new DataLinkFrame(frame);
	}

	public DataLinkFrameEvent(DataLinkFrameSignal signal)
	{
		super("DataLinkFrame");
		((DataLinkFrameSignal)signal).frame = new DataLinkFrame(signal.frame);
	}

	public DataLinkFrame getFrame()
	{
		return ((DataLinkFrameSignal)signal).frame;
	}

	@Override
	public String toString()
	{
		return String.format("DataLinkFrameEvent [signal=%s]", signal);
	}

	@Override
	public void createSignal()
	{
		signal = new DataLinkFrameSignal(new DataLinkFrame());
	}
}
