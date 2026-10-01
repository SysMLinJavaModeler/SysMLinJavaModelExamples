package dbssystem.patient;

import java.util.Optional;

import dbssystem.common.DBSSignalSignal;
import sysmlinjava.events.SysMLSignalEvent;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.ports.SysMLPort;

/**
 * Port for receiving the DBS actuator's signal by the patient.
 * 
 * @author ModelerOne
 *
 */
public class DBSSignalInPort extends SysMLPort
{
	/**
	 * Constructor
	 * 
	 * @param contextBlock the Patient which is to receive the DBS signal
	 */
	public DBSSignalInPort(Patient contextBlock)
	{
		super(contextBlock, Optional.of(contextBlock), 0L, "DBSSignalInPort");
	}

	@Override
	protected SysMLSignalEvent eventFor(SysMLSignal signal)
	{
		SysMLSignalEvent result = null;
		if (signal instanceof DBSSignalSignal dbsSignalSignal)
			result = new SysMLSignalEvent(dbsSignalSignal, "DBSSignalEvent", 0L);
		return result;
	}
}
