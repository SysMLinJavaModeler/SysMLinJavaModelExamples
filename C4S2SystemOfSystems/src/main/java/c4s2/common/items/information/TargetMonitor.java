package c4s2.common.items.information;

import java.util.ArrayList;
import java.util.List;

import c4s2.common.attributetypes.RadarReturnSignatureEnum;
import c4s2.common.attributetypes.TargetDevelopmentAlgorithmsEnum;
import sysmlinjava.attributetypes.DistanceMeters;
import sysmlinjava.attributetypes.DurationSeconds;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.PointGeospatial;
import sysmlinjava.attributetypes.ProbabilityPercent;
import sysmlinjava.common.SysMLAnything;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.views.common.StackedProtocolObject;

@SuppressWarnings("javadoc")
public class TargetMonitor extends SysMLAnything implements StackedProtocolObject
{
	@Attribute
	public TargetDevelopmentAlgorithmsEnum algorithm;
	@Attribute
	public RadarReturnSignatureEnum signature;
	@Attribute
	public List<Waypoint> pastWaypoints;
	@Attribute
	public List<Waypoint> projectedWaypoints;
	@Attribute
	public ProbabilityPercent confidence;
	@Attribute
	public InstantMilliseconds time;

	public static final DistanceMeters maxBetweenWaypointsForStop = new DistanceMeters(100);
	
	public TargetMonitor()
	{
		signature = RadarReturnSignatureEnum.unknown;
		algorithm = TargetDevelopmentAlgorithmsEnum.Simple;
		pastWaypoints = new ArrayList<>();
		projectedWaypoints = new ArrayList<>();
		confidence = new ProbabilityPercent(0);
		time = InstantMilliseconds.now();
	}

	public TargetMonitor(TargetMonitor copied)
	{
		signature = copied.signature;
		algorithm = copied.algorithm;
		pastWaypoints = new ArrayList<>(copied.pastWaypoints);
		projectedWaypoints = new ArrayList<>(copied.projectedWaypoints);
		confidence = new ProbabilityPercent(copied.confidence);
		time = new InstantMilliseconds(copied.time);
	}

	public boolean isIAW(TargetControl targetControl)
	{
		return algorithm == targetControl.algorithm;
	}

	public void updateProjectedWaypoints(RadarMonitor radarMonitor)
	{
		PointGeospatial nextPosition = radarMonitor.radarSignalReturn.get().position;
		Waypoint nextWaypoint = new Waypoint(InstantMilliseconds.now(), nextPosition);
		pastWaypoints.add(nextWaypoint);
		time = InstantMilliseconds.now();
	}

	public void projectComplexFutureTrack()
	{
		//TODO implement complex algorithm
		projectSimpleFutureTrack(); //temp use of simple algorithm
	}
	
	public void projectDifficultFutureTrack()
	{
		//TODO implement difficult algorithm
		projectSimpleFutureTrack(); //temp use of simple algorithm
	}
	
	public void projectSimpleFutureTrack()
	{
		if (!pastWaypoints.isEmpty())
			if (pastWaypoints.size() >= 2)
			{
				int lastIndex = pastWaypoints.size() - 1;
				int nextTolastIndex = pastWaypoints.size() - 2;
				Waypoint firstWaypoint = pastWaypoints.get(nextTolastIndex);
				Waypoint secondWaypoint = pastWaypoints.get(lastIndex);
				PointGeospatial firstPosition = firstWaypoint.location;
				PointGeospatial secondPosition = secondWaypoint.location;
				DurationSeconds durationSeconds = secondWaypoint.time.subtracted(firstWaypoint.time);

				PointGeospatial nextPosition = PointGeospatial.projected(firstPosition, secondPosition);
				InstantMilliseconds nextTime = secondWaypoint.time.add(durationSeconds);
				projectedWaypoints.add(new Waypoint(nextTime, nextPosition));
			}
			else
				projectedWaypoints.add(pastWaypoints.get(0));
	}

	public boolean isStopProjected()
	{
		boolean result = false;
		if (pastWaypoints.size() >= 2)
		{
			int lastIndex = pastWaypoints.size() - 1;
			PointGeospatial lastPosition = pastWaypoints.get(lastIndex).location;
			PointGeospatial nextToLastPosition = pastWaypoints.get(lastIndex - 1).location;
			if (lastPosition.distanceTo(nextToLastPosition).lessThanOrEqualTo(maxBetweenWaypointsForStop))
				result = true;
		}
		return result;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("%s(signature=%s)", getClass().getSimpleName(), signature);
	}

	@Override
	public String toString()
	{
		StringBuilder pastbuild = new StringBuilder();
		StringBuilder projbuild = new StringBuilder();
		if(!pastWaypoints.isEmpty())
			pastbuild.append(pastWaypoints.get(pastWaypoints.size()-1) + "\n");
		if(!projectedWaypoints.isEmpty())
			projbuild.append(projectedWaypoints.get(projectedWaypoints.size()-1));
		return String.format("TargetMonitor signature=%s, confidence=%s, algorithm=%s%npastWaypoint=%s, projectedWaypoint=%s", signature, confidence, algorithm, pastbuild, projbuild);
//		pastWaypoints.forEach(waypoint -> pastbuild.append(waypoint.toString() + "\n"));
//		projectedWaypoints.forEach(waypoint -> projbuild.append(waypoint.toString() + "\n"));
//		return String.format("TargetMonitor [signature=%s, algorithm=%s%npastWaypoints=%n%s, projectedWaypoints=%n%s, confidence=%s]", signature, algorithm, pastbuild, projbuild, confidence);
	}
}
