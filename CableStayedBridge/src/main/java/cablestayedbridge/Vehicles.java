package cablestayedbridge;

import java.util.List;
import java.util.Optional;

import sysmlinjava.attributetypes.DirectionDegrees;
import sysmlinjava.attributetypes.InstantMilliseconds;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.SpeedMilesPerHour;
import sysmlinjava.attributetypes.VelocityMilesPerHourDegrees;
import sysmlinjava.attributetypes.WeightPounds;
import sysmlinjava.events.SysMLChangeEvent;
import sysmlinjava.javaannotations.metadata.Issue;
import sysmlinjava.javaannotations.metadata.Rationale;
import sysmlinjava.javaannotations.parts.Part;
import sysmlinjava.metadata.SysMLIssue;
import sysmlinjava.metadata.SysMLRationale;
import sysmlinjava.parts.SysMLPart;
import sysmlinjava.states.FinalEvent;

/**
 * SysMLinJava block-based representation of a set of vehicles crossing a
 * cable-stayed bridge. The set consists of two groups of vehicles, one
 * eastbound, the other westbound. The {@code Vehicles} operate as a single
 * block that executes asynchronously from the bridge in its own thread of
 * execution.
 * <p>
 * The {@code Vehicles} consists of two parts, one for the eastbound
 * {@code Vehicle}s and one for the westbound {@code Vehicle}s. The
 * {@code Vehicles} respond to a timer to perform another movement of all the
 * {@code Vehicle}s and their loads to the next locations on the bridge deck to
 * thereby simulate the live loads that are imposed on the bridge over time.
 * 
 * @author ModelerOne
 *
 */
public class Vehicles extends SysMLPart
{
	/**
	 * Number of vehicles in each direction
	 */
	public static final int directionBoundCount = 10;

	/**
	 * Part for the eastbound vehicles
	 */
	@Part
	List<Vehicle> vehiclesEastbound;
	/**
	 * Part for the westbound vehicles
	 */
	@Part
	List<Vehicle> vehiclesWestbound;

	/**
	 * Rationale for setting all vehicles as trucks for simulation
	 */
	@Rationale
	SysMLRationale allTrucksRationale;
	/**
	 * Problem for use of uniform (versus random) vehicle arrivals on bridge
	 */
	@Issue
	SysMLIssue uniformVehicleArrivals;
	/**
	 * Time of previous vehicle movement
	 */
	Optional<InstantMilliseconds> previousMoveTime;

	/**
	 * Constructor
	 */
	public Vehicles()
	{
		super("Vehicles", 0L);
		this.previousMoveTime = Optional.empty();
	}

	/**
	 * Operation to perform the next movement of the vehicles by simply computing
	 * their next location on the deck and transmitting the moved load to the deck.
	 */
	public void onMoveTime()
	{
		InstantMilliseconds now = InstantMilliseconds.now();
		int i;
		for (i = 0; i < vehiclesEastbound.size(); i++)
			vehiclesEastbound.get(i).onMoveTime(now, previousMoveTime, false);
		for (i = 0; i < vehiclesWestbound.size() - 1; i++)
			vehiclesWestbound.get(i).onMoveTime(now, previousMoveTime, false);
		vehiclesWestbound.get(i).onMoveTime(now, previousMoveTime, true);
		previousMoveTime = Optional.of(now);
	}

	@Override
	public void stop()
	{
		acceptEvent(new SysMLChangeEvent("Stop", "VehiclesStopEvent", 0L));
		acceptEvent(new FinalEvent());
		super.stop();
	}

	@Override
	protected void createParts()
	{
		vehiclesEastbound = List.of(
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-0 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)),
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-1 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)),
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-2 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)),
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-3 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)),
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-4 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)),
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-5 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)),
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-6 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)),
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-7 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)),
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-8 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)),
			new Vehicle(id, new WeightPounds(80_000), new Point2D(-9 * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)));

		vehiclesWestbound = List.of(
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-0 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)),
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-1 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)),
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-2 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)),
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-3 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)),
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-4 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)),
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-5 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)),
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-6 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)),
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-7 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)),
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-8 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)),
			new Vehicle(id+10, new WeightPounds(80_000), new Point2D(600 + (-9 * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)));
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new VehiclesStateMachine(this));
	}

	@Override
	protected void createRationales()
	{
		allTrucksRationale = new SysMLRationale("All vehicles assumed to be max-loaded semi-tractor trailers for \"worst-case\" scenario of model execution.", "worstCaseLoads", 0L);
	}

	@Override
	protected void createIssues()
	{
		uniformVehicleArrivals = new SysMLIssue("Uniform vehicle arrivals should be replaced with random (Poisson distribution) arrivals for more realistic simulation of dynamic loading of bridge deck", "uniformVehicleArrivals", 0L);
	}
}
