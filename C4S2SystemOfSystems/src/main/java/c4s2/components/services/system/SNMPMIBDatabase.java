package c4s2.components.services.system;

import java.util.ArrayList;
import java.util.List;

import sysmlinjava.common.SysMLAnything;
import sysmlinjavalibrary.common.objects.information.MIB;

/**
 * Database of the Simple Network Management Protocol's management information
 * base (MIB)s sent to and received from the system components
 */
@SuppressWarnings("javadoc")
public class SNMPMIBDatabase extends SysMLAnything
{
	public List<MIB> mibs;

	public SNMPMIBDatabase()
	{
		super();
		this.mibs = new ArrayList<>();
	}

	public void add(MIB mib)
	{
		mibs.add(mib);
	}
}
