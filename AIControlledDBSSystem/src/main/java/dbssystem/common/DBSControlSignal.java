package dbssystem.common;

import java.io.Serializable;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

public class DBSControlSignal extends SysMLSignal implements Serializable
{
	private static final long serialVersionUID = -7830462439899597766L;

	@Attribute
	public DBSControl control;

	public DBSControlSignal(DBSControl control)
	{
		super();
		this.control = control;
	}

	@Override
	public String stackNamesString()
	{
		return "DBS Control";
	}
}
