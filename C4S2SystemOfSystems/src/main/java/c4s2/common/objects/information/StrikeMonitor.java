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

public class StrikeMonitor extends SysMLClass implements StackedProtocolObject
{
	@Attribute
	public StrikeSystemStatesEnum state;
	@Attribute
	public Optional<PointGeospatial> location;
	@Attribute
	public Optional<VelocityMetersPerSecondRadians> velocity;
	@Attribute
	public Optional<StrikeOrdnance> loadedOrdnance;
	@Attribute
	public Optional<BBoolean> confirmTargetEngaged;
	@Attribute
	public InstantMilliseconds time;

	public StrikeMonitor(StrikeSystemStatesEnum state, Optional<PointGeospatial> location, Optional<VelocityMetersPerSecondRadians> velocity, Optional<StrikeOrdnance> loadedOrdnance, Optional<BBoolean> confirmTargetEngaged, InstantMilliseconds time)
	{
		super();
		this.state = state;
		this.location = location;
		this.velocity = velocity;
		this.loadedOrdnance = loadedOrdnance;
		this.confirmTargetEngaged = confirmTargetEngaged;
		this.time = time;
	}
	
	public StrikeMonitor(StrikeMonitor copyOf)
	{
		super();
		this.state = copyOf.state;
		this.location = copyOf.location.isPresent() ? Optional.of(new PointGeospatial(copyOf.location.get())) : Optional.empty();
		this.velocity = copyOf.velocity.isPresent() ? Optional.of(new VelocityMetersPerSecondRadians(copyOf.velocity.get())) : Optional.empty();
		this.loadedOrdnance = copyOf.loadedOrdnance.isPresent() ? Optional.of(new StrikeOrdnance(copyOf.loadedOrdnance.get())) : Optional.empty();
		this.confirmTargetEngaged = copyOf.confirmTargetEngaged.isPresent() ? Optional.of(new BBoolean(copyOf.confirmTargetEngaged.get())) : Optional.empty();
		this.time = new InstantMilliseconds(copyOf.time);
	}

	public StrikeMonitor(StrikeSystemMonitor monitor)
	{
		super();
		this.state = monitor.state;
		this.location = Optional.of(new PointGeospatial(monitor.position));
		this.velocity = Optional.of(new VelocityMetersPerSecondRadians(monitor.velocity));
		this.loadedOrdnance = monitor.loadedOrdnance.isPresent() ? Optional.of(new StrikeOrdnance(monitor.loadedOrdnance.get())) : Optional.empty();
		this.confirmTargetEngaged = monitor.confirmTargetEngaged.isPresent() ? Optional.of(new BBoolean(monitor.confirmTargetEngaged.get())) : Optional.empty();
		this.time = new InstantMilliseconds(monitor.time);
	}

	public StrikeMonitor()
	{
		this.state = StrikeSystemStatesEnum.initializing;
		this.location = Optional.empty();
		this.velocity = Optional.empty();
		this.loadedOrdnance = Optional.empty();
		this.confirmTargetEngaged = Optional.empty();
		this.time = InstantMilliseconds.now();
	}

	public boolean isIAW(StrikeControl control)
	{
		boolean stateIsIAW = state == control.state;
		boolean loadedOrdnanceIsIAW = (control.strikeOrdnance.isPresent() && loadedOrdnance.isPresent() && loadedOrdnance.get().type == control.strikeOrdnance.get().type) || control.strikeOrdnance.isEmpty();
		return stateIsIAW && loadedOrdnanceIsIAW;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(state=%s)", getClass().getSimpleName(), state);
	}

	@Override
	public String toString()
	{
		return String.format("StrikeMonitor [state=%s, time=%s, location=%s, velocity=%s, loadedOrdnance=%s, confirmTargetEngaged=%s]", state, time, location, velocity, loadedOrdnance, confirmTargetEngaged);
	}
}
