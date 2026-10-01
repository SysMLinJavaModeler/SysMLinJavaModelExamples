package dbssystem.controller;

import dbssystem.common.DBSControl;
import dbssystem.common.DBSControlSignal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to transmit a DBS actuator control out of the DBS controller
 * 
 * @author ModelerOne
 *
 */
public class DBSControlOutPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param context the DBSController of which this is a port
	 * @param index       unique indec
	 * @param name        unique name
	 */
	public DBSControlOutPort(DBSController context, Long index, String name)
	{
		super(context, index, name);
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof DBSControl dbsControl)
			result = new DBSControlSignal(dbsControl);
		return result;
	}

}
