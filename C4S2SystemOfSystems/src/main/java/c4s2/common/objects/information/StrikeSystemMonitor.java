package c4s2.common.objects.information;

import java.util.Optional;
import c4s2.common.valueTypes.StrikeSystemStatesEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.BBoolean;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.PointGeospatial;
import sysmlinjava.valuetypes.VelocityMetersPerSecondRadians;

public class StrikeSystemMonitor extends SysMLClass implements StackedProtocolObject
{
	@Attribute
	public StrikeSystemStatesEnum state;
	@Attribute
	public InstantMilliseconds time;
	@Attribute
	public PointGeospatial position;
	@Attribute
	public VelocityMetersPerSecondRadians velocity;
	@Attribute
	public Optional<StrikeOrdnance> loadedOrdnance;
	@Attribute
	public Optional<BBoolean> confirmTargetEngaged;

	public StrikeSystemMonitor(StrikeSystemStatesEnum state, InstantMilliseconds time, PointGeospatial position, VelocityMetersPerSecondRadians velocity, Optional<StrikeOrdnance> loadedOrdnance, Optional<BBoolean> confirmTargetDestroyed)
	{
		super();
		this.state = state;
		this.time = time;
		this.position = position;
		this.velocity = velocity;
		this.loadedOrdnance = loadedOrdnance;
		this.confirmTargetEngaged = confirmTargetDestroyed;
	}
	
	public StrikeSystemMonitor(StrikeSystemMonitor copyOf)
	{
		super();
		this.state = copyOf.state;
		this.time = new InstantMilliseconds(copyOf.time);
		this.position = new PointGeospatial(copyOf.position);
		this.velocity = new VelocityMetersPerSecondRadians(copyOf.velocity);
		this.loadedOrdnance = copyOf.loadedOrdnance.isPresent() ? Optional.of(new StrikeOrdnance(copyOf.loadedOrdnance.get())) : Optional.empty();
		this.confirmTargetEngaged = copyOf.confirmTargetEngaged.isPresent() ? Optional.of(new BBoolean(copyOf.confirmTargetEngaged.get())) : Optional.empty();
	}

	public boolean isIAW(StrikeControl control)
	{
		return state == control.state;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(state=%s)", getClass().getSimpleName(), state);
	}

	@Override
	public String toString()
	{
		return String.format("StrikeSystemMonitor [state=%s, time=%s, position=%s, velocity=%s, loadedOrdnance=%s, confirmTargetDestroyed=%s]", state, time, position, velocity, loadedOrdnance, confirmTargetEngaged);
	}

}