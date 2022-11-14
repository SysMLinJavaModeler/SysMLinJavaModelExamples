package c4s2.common.objects.information;

import java.util.Optional;
import c4s2.common.valueTypes.OrdnanceStatesEnum;
import c4s2.common.valueTypes.OrdnanceTypeEnum;
import c4s2.common.valueTypes.StrikeSystemStatesEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.PointGeospatial;

public class StrikeControl extends SysMLClass implements StackedProtocolObject
{
	/**
	 * Optional state for the StrikeSystem to transition to
	 */
	@Attribute
	public StrikeSystemStatesEnum state;
	/**
	 * Optional location for the strike system to be at
	 */
	@Attribute
	public Optional<PointGeospatial> strikeLocation;
	/**
	 * Optional specification of the ordnance to be on the strike system.
	 */
	@Attribute
	public Optional<StrikeOrdnance> strikeOrdnance;
	/**
	 * Time of creation of this control
	 */
	@Attribute
	public InstantMilliseconds time;

	public StrikeControl(StrikeSystemStatesEnum toState, Optional<PointGeospatial> strikeLocation, Optional<StrikeOrdnance> strikeOrdnance, InstantMilliseconds time)
	{
		super();
		this.state = toState;
		this.strikeLocation = strikeLocation;
		this.strikeOrdnance = strikeOrdnance;
		this.time = time;
	}

	public StrikeControl(StrikeControl copyOf)
	{
		super();
		this.state = copyOf.state;
		this.strikeLocation = copyOf.strikeLocation.isPresent() ? Optional.of(new PointGeospatial(copyOf.strikeLocation.get())) : Optional.empty();
		this.strikeOrdnance = copyOf.strikeOrdnance.isPresent() ? Optional.of(new StrikeOrdnance(copyOf.strikeOrdnance.get())) : Optional.empty();;
		this.time = new InstantMilliseconds(copyOf.time);
	}
	
	public StrikeControl()
	{
		this.state = StrikeSystemStatesEnum.initializing;
		this.strikeLocation = Optional.empty();
		this.strikeOrdnance = Optional.empty();;
		this.time = InstantMilliseconds.now();
	}

	public void toConfigured(PointGeospatial systemPosition)
	{
		state = StrikeSystemStatesEnum.standingby;
		strikeOrdnance = Optional.of(new StrikeOrdnance(OrdnanceTypeEnum.large, OrdnanceStatesEnum.unarmed, systemPosition, InstantMilliseconds.now()));
		strikeLocation = Optional.of(systemPosition);
		time = InstantMilliseconds.now();
	}

	public void toEngaging()
	{
		state = StrikeSystemStatesEnum.striking;
		time = InstantMilliseconds.now();
	}

	public void toAssessing()
	{
		state = StrikeSystemStatesEnum.returned;
		strikeLocation = Optional.empty();
		strikeOrdnance = Optional.empty();
		time = InstantMilliseconds.now();
	}

	/**
	 * Sets the control for finalizing the StrikeSystem, i.e. detaching it from this
	 * current C4S2 operation.
	 */
	public void toFinalized()
	{
		state = StrikeSystemStatesEnum.detached;
		strikeLocation = Optional.empty();
		strikeOrdnance = Optional.empty();
		time = InstantMilliseconds.now();
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(state=%s)", getClass().getSimpleName(), state);
	}

	@Override
	public String toString()
	{
		return String.format("StrikeControl [state=%s, strikeLocation=%s, strikeOrdnance=%s, time=%s]", state, strikeLocation, strikeOrdnance, time);
	}
}
