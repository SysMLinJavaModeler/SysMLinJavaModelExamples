package c4s2.common.objects.information;

import java.util.Optional;
import c4s2.common.valueTypes.StrikeSystemStatesEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.PointGeospatial;

public class StrikeSystemControl extends SysMLClass implements StackedProtocolObject
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
