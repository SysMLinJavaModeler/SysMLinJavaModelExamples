package dbssystem.common;

import java.io.Serializable;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

public class TremorLevelSignal extends SysMLSignal implements Serializable
{
	private static final long serialVersionUID = -1939973933154344946L;

	@Attribute
	public TremorLevel value;

	public TremorLevelSignal(TremorLevel value)
	{
		super();
		this.value = value;
	}

	@Override
	public String stackNamesString()
	{
		return "Tremor Level";
	}
}
