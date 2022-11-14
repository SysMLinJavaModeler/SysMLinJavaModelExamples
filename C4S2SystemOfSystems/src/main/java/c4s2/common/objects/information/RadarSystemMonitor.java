package c4s2.common.objects.information;

import java.util.Optional;
import c4s2.common.valueTypes.RadarSystemStatesEnum;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.ProbabilityPercent;

public class RadarSystemMonitor extends SysMLClass implements StackedProtocolObject
{
	/**
	 * The current state of the radar
	 */
	@Attribute
	public RadarSystemStatesEnum currentState;
	/**
	 * Optional value for the current signal transmitted by the radar
	 */
	@Attribute
	public Optional<RadarSignalTransmission> radarSignalTransmission;
	/**
	 * Optional value for the current/last signal returned to the radar
	 */
	@Attribute
	public Optional<RadarSignalReturn> radarSignalReturn;
	/**
	 * Optional value for the confidence in the current/last return to the radar
	 */
	@Attribute
	public Optional<ProbabilityPercent> confidence;
	/**
	 * Time of monitor values
	 */
	@Attribute
	public InstantMilliseconds time;

	/**
	 * Constructor for all fields in the monitor
	 * 
	 * @param currentState            state of the radar
	 * @param radarSignalTransmission radar's signal transmission
	 * @param radarSignalReturn       radar's last return
	 * @param confidence              confidence in the last return
	 * @param time                    time of the monitor data
	 */
	public RadarSystemMonitor(RadarSystemStatesEnum currentState, Optional<RadarSignalTransmission> radarSignalTransmission, Optional<RadarSignalReturn> radarSignalReturn, Optional<ProbabilityPercent> confidence, InstantMilliseconds time)
	{
		super();
		this.currentState = currentState;
		this.radarSignalTransmission = radarSignalTransmission;
		this.radarSignalReturn = radarSignalReturn;
		this.confidence = confidence;
		this.time = time;
	}

	/**
	 * Returns whether or not this monitor is in accordance with (reflects correct
	 * configuration for) the specified control.
	 * 
	 * @param control the RadarControl with which this monitor must be in accord
	 *                with
	 * @return true if this monitor is in accord with the control, false otherwise
	 */
	public boolean isIAW(RadarControl control)
	{
		return control.state == currentState && control.transmission.isPresent() && radarSignalTransmission.isPresent() && radarSignalTransmission.get().isIAW(control.transmission.get());
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(state=%s, signature=%s)", getClass().getSimpleName(), currentState, radarSignalReturn.isPresent() ? radarSignalReturn.get().signature : "none");
	}

	public RadarSystemMonitor(RadarSystemMonitor copied)
	{
		super(copied);
		this.currentState = copied.currentState;
		if (copied.radarSignalTransmission.isPresent())
			this.radarSignalTransmission = Optional.of(new RadarSignalTransmission(copied.radarSignalTransmission.get()));
		else
			this.radarSignalTransmission = Optional.empty();
		if (copied.radarSignalReturn.isPresent())
			this.radarSignalReturn = Optional.of(new RadarSignalReturn(copied.radarSignalReturn.get()));
		else
			this.radarSignalReturn = Optional.empty();
		if (copied.confidence.isPresent())
			this.confidence = Optional.of(new ProbabilityPercent(copied.confidence.get()));
		else
			this.confidence = Optional.empty();
		this.time = new InstantMilliseconds(copied.time);
	}

	@Override
	public String toString()
	{
		return String.format("RadarMonitor [currentState=%s, radarSignalTransmission=%s, radarSignalReturn=%s, confidence=%s]", currentState, radarSignalTransmission, radarSignalReturn, confidence);
	}
}
