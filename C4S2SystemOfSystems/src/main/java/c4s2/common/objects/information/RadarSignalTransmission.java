package c4s2.common.objects.information;

import static java.lang.Math.abs;
import java.util.Optional;
import sysmlinjava.analysis.common.StackedProtocolObject;
import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.DirectionRadians;
import sysmlinjava.valuetypes.DistanceMeters;
import sysmlinjava.valuetypes.FrequencyHertz;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.PointGeospatial;
import sysmlinjava.valuetypes.PowerWatts;

public class RadarSignalTransmission extends SysMLClass implements StackedProtocolObject
{
	@Attribute
	public PointGeospatial systemPosition;
	@Attribute
	public PointGeospatial scanCenterPosition;
	@Attribute
	public DistanceMeters scanHalfDistance;
	@Attribute
	public DirectionRadians scanHalfAngle;
	@Attribute
	public FrequencyHertz scanFrequency;
	@Attribute
	public PowerWatts scanPower;
	@Attribute
	public InstantMilliseconds scanStartTime;

	public RadarSignalTransmission(PointGeospatial systemLocation, PointGeospatial scanCenterLocation, DistanceMeters scanHalfDistance, DirectionRadians scanHalfAngle, FrequencyHertz scanFrequency, PowerWatts scanPower, InstantMilliseconds scanStartTime)
	{
		super();
		this.systemPosition = systemLocation;
		this.scanCenterPosition = scanCenterLocation;
		this.scanHalfDistance = scanHalfDistance;
		this.scanHalfAngle = scanHalfAngle;
		this.scanFrequency = scanFrequency;
		this.scanPower = scanPower;
		this.scanStartTime = scanStartTime;
	}

	public RadarSignalTransmission(RadarSignalTransmission copied)
	{
		super(copied);
		this.systemPosition = new PointGeospatial(copied.systemPosition);
		this.scanCenterPosition = new PointGeospatial(copied.scanCenterPosition);
		this.scanHalfDistance = new DistanceMeters(copied.scanHalfDistance);
		this.scanHalfAngle = new DirectionRadians(copied.scanHalfAngle);
		this.scanFrequency = new FrequencyHertz(copied.scanFrequency);
		this.scanPower = new PowerWatts(copied.scanPower);
		this.scanStartTime = new InstantMilliseconds(copied.scanStartTime);
	}

	public RadarSignalTransmission()
	{
		this.systemPosition = new PointGeospatial();
		this.scanCenterPosition = new PointGeospatial();
		this.scanHalfDistance = new DistanceMeters();
		this.scanHalfAngle = new DirectionRadians();
		this.scanFrequency = new FrequencyHertz();
		this.scanPower = new PowerWatts();
		this.scanStartTime = new InstantMilliseconds();
	}

	public boolean includes(PointGeospatial position)
	{
		boolean result = false;
		if(position.isWithinRadialSection(systemPosition, scanCenterPosition, scanHalfDistance, scanHalfAngle))
			result = true;
		return result;
	}

	/**
	 * Returns whether or not this RadarSignalTransmission is in accordance with
	 * (reflects correct configuration for) the specified transmission. Accordance
	 * is computed by determining whether or not each of the transmission values is
	 * within a certain range of the specified transmission's values.
	 * 
	 * @param transmission The RadarSignalTransmission with which this transmission
	 *                     must be in accord with
	 * @return true if this transmission is in accord with the specifed
	 *         transmission, false otherwise
	 */
	public boolean isIAW(RadarSignalTransmission transmission)
	{
		boolean scanCenterIsIAW = scanCenterPosition.isWithinCircle(transmission.scanCenterPosition, new DistanceMeters(100));
		boolean scanHalfDistanceIsIAW = abs(scanHalfDistance.value - transmission.scanHalfDistance.value) < 100;
		boolean scanHalfAngleIsIAW = abs(scanHalfAngle.value - transmission.scanHalfAngle.value) < 0.0001;
		boolean scanFrequencyIsIAW = abs(scanFrequency.value - transmission.scanFrequency.value) < 100;
		boolean scanPowerIsIAW = abs(scanPower.value - transmission.scanPower.value) < 100;
		return scanCenterIsIAW && scanHalfDistanceIsIAW && scanHalfAngleIsIAW && scanFrequencyIsIAW && scanPowerIsIAW;
	}

	@Override
	public String stackNamesString()
	{
		return stackNamesString(this, Optional.empty());
	}

	@Override
	public String toString()
	{
		return String.format("RadarSignalTransmission [name=%s, id=%s, systemLocation=%s, scanCenterLocation=%s, scanHalfDistance=%s, scanHalfAngle=%s, scanFrequency=%s, scanPower=%s, scanStartTime=%s]", name, id, systemPosition,
			scanCenterPosition, scanHalfDistance, scanHalfAngle, scanFrequency, scanPower, scanStartTime);
	}
}
