package hflink.common.signals;

import java.util.Optional;
import hflink.common.objects.GPSMessage;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLSignal;

/**
 * Signal for a GPS (time) message
 * 
 * @author ModelerOne
 *
 */
public class GPSMessageSignal extends SysMLSignal
{
	@Attribute
	public GPSMessage message;

	public GPSMessageSignal(GPSMessage message)
	{
		super("GPSMessage", 0L);
		this.message = message;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("GPSMessageSignal [name=%s, id=%s, message=%s]", name, id, message);
	}
}
