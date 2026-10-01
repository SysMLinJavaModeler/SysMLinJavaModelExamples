package dbssystem.actuators;

import dbssystem.common.DBSSignal;
import dbssystem.common.DBSSignalSignal;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.ports.SysMLPort;

/**
 * Port to transmit a low-power signal out of the DBS actuator
 * 
 * @author ModelerOne
 */
public class DBSSignalOutPort extends SysMLPort
{

	/**
	 * Constructor
	 * 
	 * @param contextPart part in whose context this port executes, i.e. the DBS
	 *                    actuator
	 */
	public DBSSignalOutPort(DBSActuator contextPart)
	{
		super(contextPart, 0L, "DBSSignalOutPort");
	}

	@Action
	@Override
	protected SysMLSignal signalFor(SysMLAnything object)
	{
		SysMLSignal result = null;
		if (object instanceof DBSSignal dbsSignal)
			result = new DBSSignalSignal(dbsSignal);
		return result;
	}

}
