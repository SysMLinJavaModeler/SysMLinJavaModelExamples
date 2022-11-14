package cablestayedbridge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import sysmlinjava.annotations.Part;
import sysmlinjava.annotations.comments.Problem;
import sysmlinjava.annotations.comments.Rationale;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.comments.SysMLProblem;
import sysmlinjava.comments.SysMLRationale;
import sysmlinjava.statemachine.FinalEvent;
import sysmlinjava.valuetypes.DirectionDegrees;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.Point2D;
import sysmlinjava.valuetypes.SpeedMilesPerHour;
import sysmlinjava.valuetypes.VelocityMilesPerHourDegrees;
import sysmlinjava.valuetypes.WeightPounds;

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
public class Vehicles extends SysMLBlock
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
	@Problem
	SysMLProblem uniformVehicleArrivals;
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

	@Override
	public void start()
	{
		super.start();
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
		acceptEvent(new VehiclesEvent("Stop"));
		acceptEvent(new FinalEvent());
		super.stop();
	}

	@Override
	protected void createParts()
	{
		for (int index = 0; index < directionBoundCount; index++)
		{
			vehiclesEastbound.add(new Vehicle(id, new WeightPounds(80_000), new Point2D(-index * 25, 30), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.east)));
			vehiclesWestbound.add(new Vehicle(directionBoundCount + id, new WeightPounds(80_000), new Point2D(600 + (index * 25), 60), new VelocityMilesPerHourDegrees(new SpeedMilesPerHour(25), DirectionDegrees.west)));
		}
	}

	@Override
	protected void createStateMachine()
	{
		stateMachine = Optional.of(new VehiclesStateMachine(this));
	}

	@Override
	protected void preCreate()
	{
		vehiclesEastbound = new ArrayList<>();
		vehiclesWestbound = new ArrayList<>();
	}

	@Override
	protected void createRationales()
	{
		allTrucksRationale = new SysMLRationale("All vehicles assumed to be max-loaded semi-tractor trailers for \"worst-case\" scenario of model execution.");
	}

	@Override
	protected void createProblems()
	{
		uniformVehicleArrivals = new SysMLProblem("Uniform vehicle arrivals should be replaced with random (Poisson distribution) arrivals for more realistic simulation of dynamic loading of bridge deck");
	}
}
