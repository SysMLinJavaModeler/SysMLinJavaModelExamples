package hflink.common.events;

import hflink.common.objects.ApplicationUIControl;
import hflink.common.signals.ApplicationUIControlSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * Event for the operator's input of application control information
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIControlEvent extends SysMLSignalEvent
{
	public ApplicationUIControlEvent(ApplicationUIControl control)
	{
		super("ApplicationUIControl");
		((ApplicationUIControlSignal)signal).control = new ApplicationUIControl(control);
	}

	public ApplicationUIControlEvent(ApplicationUIControlSignal signal)
	{
		super("ApplicationUIControl");
		((ApplicationUIControlSignal)signal).control = new ApplicationUIControl(signal.control);
	}

	public ApplicationUIControl getControl()
	{
		return ((ApplicationUIControlSignal)signal).control;
	}

	@Override
	public void createSignal()
	{
		signal = new ApplicationUIControlSignal(new ApplicationUIControl());
	}

	@Override
	public String toString()
	{
		return String.format("ApplicationUIControlEvent [signal=%s]", signal);
	}
}
