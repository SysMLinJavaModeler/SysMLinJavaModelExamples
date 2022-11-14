package cablestayedbridge;

import java.util.Optional;
import cablestayedbridge.ports.VehicleToDeckLoadTransmitter;
import sysmlinjava.annotations.FullPort;
import sysmlinjava.annotations.Value;
import sysmlinjava.blocks.SysMLBlock;
import sysmlinjava.valuetypes.DirectionDegrees;
import sysmlinjava.valuetypes.DurationSeconds;
import sysmlinjava.valuetypes.InstantMilliseconds;
import sysmlinjava.valuetypes.Point2D;
import sysmlinjava.valuetypes.RReal;
import sysmlinjava.valuetypes.VelocityMilesPerHourDegrees;
import sysmlinjava.valuetypes.WeightPounds;

/**
 * SysMLinJava block-based representation of a vehicle on a cable-stayed bridge.
 * The {@code Vehicle} is one of a collection of vehicles that travels in a
 * single eastbound or westbound lane on the deck of the bridge. It travels at a
 * fixed speed, transmitting its weight to the deck at its current location on
 * the the deck.
 * <p>
 * The {@code Vehicle} is characterized by its values for weight, location,
 * speed, and direction. It transmits its weight to the bridge deck via a load
 * transmitter port.
 * 
 * @author ModelerOne
 *
 */
public class Vehicle extends SysMLBlock
{
	/**
	 * Full port by which the vehicle weight is transmitted to the deck
	 */
	@FullPort
	VehicleToDeckLoadTransmitter deckLoadTransmitter;

	/**
	 * Value for the vehicle's weight
	 */
	@Value
	WeightPounds weight;
	/**
	 * Value for the vehicle's location
	 */
	@Value
	Point2D location;
	/**
	 * Value for the vehicle's velocity (speed, direction)
	 */
	@Value
	VelocityMilesPerHourDegrees velocity;
	/**
	 * Value for the vehicle's heading factor (1 = eastbound, -1 = westbound)
	 */
	@Value
	RReal directionFactor;

	/**
	 * Constructor
	 * 
	 * @param id    index of this vehicle in a list or array of vehicles
	 * @param weight   weight of the vehicle
	 * @param location location of the vehicle
	 * @param velocity velocity (speed, direction) of the vehicle
	 */
	public Vehicle(Long id, WeightPounds weight, Point2D location, VelocityMilesPerHourDegrees velocity)
	{
		super(String.format("Vehicle%d", id), id);
		this.weight.setValue(weight);
		this.location.setValue(location);
		this.velocity.setValue(velocity);
		this.velocity.heading.setValue(velocity.heading);
		this.directionFactor = velocity.heading.equals(DirectionDegrees.east) ? new RReal(1) : new RReal(-1);
	}

	/**
	 * Factor to convert miles-per-hour to feet-per-second
	 */
	private static final RReal mphTofps = new RReal(1.609344);

	/**
	 * Operation to update the vehicles location for the next movement and to
	 * transmit the vehicle load to the deck at that new location
	 * 
	 * @param now              time of current move
	 * @param previousMoveTime time of previous move, if not first move. Otherwise,
	 *                         empty value
	 * @param lastLoad         whether this is the last vehicle of the set of next
	 *                         vehicle loads to be transmitted to the deck
	 */
	public void onMoveTime(InstantMilliseconds now, Optional<InstantMilliseconds> previousMoveTime, boolean lastLoad)
	{
		if (!previousMoveTime.isEmpty())
		{
			DurationSeconds deltaSeconds = DurationSeconds.between(previousMoveTime.get(), now);
			double deltaFeet = velocity.multipliedBy(mphTofps).value * deltaSeconds.value * directionFactor.value;
			location.add(new Point2D(deltaFeet, 0));
		}
		deckLoadTransmitter.transmit(new Load(id.intValue(), weight, location, velocity.heading, lastLoad));
	}

	@Override
	protected void createValues()
	{
		weight = new WeightPounds(0);
		location = new Point2D(0, 0);
		velocity = new VelocityMilesPerHourDegrees(0, 90);
		directionFactor = new RReal(1);
	}

	@Override
	protected void createFullPorts()
	{
		deckLoadTransmitter = new VehicleToDeckLoadTransmitter(this, 0L, "DeckLoadTransmitter");
	}
}
