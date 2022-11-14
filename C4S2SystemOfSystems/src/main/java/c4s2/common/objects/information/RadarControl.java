package c4s2.common.objects.information;

import java.util.Optional;
import c4s2.common.valueTypes.RadarSystemStatesEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.InstantMilliseconds;

public class RadarControl extends SysMLClass implements StackedProtocolObject
{
	/**
	 * State of the radar
	 */
	@Attribute
	public RadarSystemStatesEnum state;
	/**
	 * Optional transmission specification
	 */
	@Attribute
	public Optional<RadarSignalTransmission> transmission;
	/**
	 * Time of the control
	 */
	@Attribute
	public InstantMilliseconds time;

	/**
	 * Constructor for all attributes
	 * @param state state of the radar
	 * @param transmission optional transmission values
	 * @param time time of the control
	 */
	public RadarControl(RadarSystemStatesEnum state, Optional<RadarSignalTransmission> transmission, InstantMilliseconds time)
	{
		super();
		this.state = state;
		this.transmission = transmission;
		this.time = time;
	}

	/**
	 * Constructor for deep copy
	 * 
	 * @param copied radar control which is to be copied
	 */
	public RadarControl(RadarControl copied)
	{
		super(copied);
		this.state = copied.state;
		if(copied.transmission.isPresent())
			transmission = Optional.of(new RadarSignalTransmission(copied.transmission.get()));
		else
			transmission = Optional.empty();
		this.time = new InstantMilliseconds(copied.time);
	}

	/**
	 * Constructor for default values
	 */
	public RadarControl()
	{
		state = RadarSystemStatesEnum.Idle;
		transmission = Optional.of(new RadarSignalTransmission());
		time = InstantMilliseconds.now();
	}

	/**
	 * Sets the radar control to configured values
	 * @param signalTransmission transmission for the current configuration
	 */
	public void toConfigured(RadarSignalTransmission signalTransmission)
	{
		state = RadarSystemStatesEnum.Idle;
		transmission = Optional.of(signalTransmission);
		time = InstantMilliseconds.now();
	}

	/**
	 * Sets the control to the find/fix/track/target state's configuration
	 */
	public void toF2T2ing()
	{
		state = RadarSystemStatesEnum.F2T2Scanning;
		time = InstantMilliseconds.now();
	}

	/**
	 * Sets the control to the engage/assess state's configuration
	 */
	public void toAssessing()
	{
		state = RadarSystemStatesEnum.EAScanning;
		time = InstantMilliseconds.now();
	}

	/**
	 * Set the control to the idled state
	 */
	public void toIdled()
	{
		state = RadarSystemStatesEnum.Idle;
		time = InstantMilliseconds.now();
	}

	public void toFinalized()
	{
		state = RadarSystemStatesEnum.Detached;
		transmission = Optional.empty();
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
		return String.format("RadarControl [state=%s, transmission=%s, time=%s]", state, transmission, time);
	}
}
