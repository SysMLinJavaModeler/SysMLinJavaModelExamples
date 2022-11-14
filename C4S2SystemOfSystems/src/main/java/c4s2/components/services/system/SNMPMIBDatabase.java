package c4s2.components.services.system;

import java.util.ArrayList;
import java.util.List;
import sysmlinjava.common.SysMLClass;
import sysmlinjavalibrary.common.objects.information.MIB;

public class SNMPMIBDatabase extends SysMLClass
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
