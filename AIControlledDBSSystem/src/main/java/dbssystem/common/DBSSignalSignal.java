package dbssystem.common;

import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

public class DBSSignalSignal extends SysMLSignal
{
	@Attribute
	public DBSSignal value;

	public DBSSignalSignal(DBSSignal value)
	{
		super();
		this.value = value;
	}

	@Override
	public String toString()
	{
		return String.format("DBSSignalSignal [value=%s]", value);
	}

	@Override
	public String stackNamesString()
	{
		return "DBS Signal";
	}
}
