package dbssystem.common;

import sysmlinjava.attributetypes.FrequencyHertz;
import sysmlinjava.attributetypes.PhaseShiftRadians;
import sysmlinjava.attributetypes.PotentialElectricalVolts;
import sysmlinjava.items.SysMLItem;
import sysmlinjava.javaannotations.attributes.Attribute;

public class DBSSignal extends SysMLItem
{
	@Attribute
	public FrequencyHertz frequency;
	@Attribute
	public PotentialElectricalVolts amplitude;
	@Attribute
	public PhaseShiftRadians phaseShift;
	
	public DBSSignal(FrequencyHertz frequency, PotentialElectricalVolts amplitude, PhaseShiftRadians phaseShift)
	{
		super();
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.phaseShift = phaseShift;
	}

	public void setValue(DBSSignal signal)
	{
		this.frequency.value = signal.frequency.value;
		this.amplitude.value = signal.amplitude.value;
		this.phaseShift.value = signal.phaseShift.value;
	}
}
