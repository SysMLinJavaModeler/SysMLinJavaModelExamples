package hflink.common.events;

import hflink.common.objects.ApplicationUIView;
import hflink.common.signals.ApplicationUIViewSignal;
import sysmlinjava.events.SysMLSignalEvent;

/**
 * Event for the update of the operator's view of the application
 * 
 * @author ModelerOne
 *
 */
public class ApplicationUIViewEvent extends SysMLSignalEvent
{
	public ApplicationUIViewEvent(ApplicationUIView view)
	{
		super("ApplicationUIView");
		((ApplicationUIViewSignal)signal).view = new ApplicationUIView(view);
	}

	public ApplicationUIViewEvent(ApplicationUIViewSignal signal)
	{
		super("ApplicationUIView");
		((ApplicationUIViewSignal)signal).view = new ApplicationUIView(signal.view);
	}

	public ApplicationUIView getView()
	{
		return ((ApplicationUIViewSignal)signal).view;
	}

	@Override
	public String toString()
	{
		return String.format("ApplicationUIViewEvent [signal=%s]", signal);
	}

	@Override
	public void createSignal()
	{
		signal = new ApplicationUIViewSignal(new ApplicationUIView());  
	}
}
