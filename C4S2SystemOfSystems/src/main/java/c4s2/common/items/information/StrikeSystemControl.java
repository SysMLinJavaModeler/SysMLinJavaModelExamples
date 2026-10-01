package c4s2.common.items.information;

import java.util.Optional;

import c4s2.common.attributetypes.StrikeSystemStatesEnum;
import sysmlinjava.attributetypes.PointGeospatial;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

@SuppressWarnings("javadoc")
public class StrikeSystemControl extends SysMLAnything implements StackedProtocolObject
{
	@Attribute
	public StrikeSystemStatesEnum state;
	@Attribute
	public Optional<PointGeospatial> location;
	@Attribute
	public Optional<StrikeOrdnance> ordnance;
	
	public StrikeSystemControl(StrikeSystemStatesEnum state, Optional<PointGeospatial> location, Optional<StrikeOrdnance> ordnance)
	{
		super();
		this.state = state;
		this.location = location;
		this.ordnance = ordnance;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(state=%s)", getClass().getSimpleName(), state);
	}

	@Override
	public String toString()
	{
		return String.format("StrikeControl [name=%s, id=%s, toState=%s, strikeLocation=%s, strikeOrdnance=%s]", name, id, state, location, ordnance);
	}
}
