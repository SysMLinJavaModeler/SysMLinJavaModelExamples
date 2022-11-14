package cablestayedbridge;

import sysmlinjava.annotations.Attribute;
import sysmlinjava.common.SysMLClass;
import sysmlinjava.valuetypes.DirectionDegrees;
import sysmlinjava.valuetypes.WeightPounds;
import sysmlinjava.valuetypes.Point2D;

/**
 * SysMLinJava class-based representation of a load on a bridge or domain
 * component. The {@code Load} is characterized by its weight, direction,
 * location, and whether it is the last load in a set of loads to be transmitted
 * to the component.
 * 
 * @author ModelerOne
 *
 */
public class Load extends SysMLClass
{
	/**
	 * Attribute for the weight of the load
	 */
	@Attribute
	public WeightPounds weight;
	/**
	 * Attribute for the direction of the load. If set, the direction may refer to
	 * the load's direction of movement, cardinal location, or other direction
	 * related characteristice.
	 */
	@Attribute
	public DirectionDegrees direction;
	/**
	 * Attribute for the location of the load
	 */
	@Attribute
	public Point2D location;
	/**
	 * Attribute for whether or not this is the last in a set of loads to be
	 * transmitted (used to indicate loading is complete with the transmission of
	 * this load)
	 */
	@Attribute
	public boolean lastLoad;

	/**
	 * Constructor for all attributes
	 * 
	 * @param index     index of this load in an array or list of loads
	 * @param weight    the weight of the load
	 * @param location  location of the load
	 * @param direction direction of the load
	 * @param lastLoad  true if this is the last in a set of loads being
	 *                  transmitted, false otherwise
	 */
	public Load(Integer index, WeightPounds weight, Point2D location, DirectionDegrees direction, boolean lastLoad)
	{
		super("Load", (long)index);
		this.weight = weight;
		this.direction = direction;
		this.location = location;
		this.lastLoad = lastLoad;
	}

	/**
	 * Constructor for weight and last load attributes
	 * 
	 * @param weight   the weight of the load
	 * @param lastLoad true if this is the last in a set of loads being transmitted,
	 *                 false otherwise
	 */
	public Load(WeightPounds weight, boolean lastLoad)
	{
		super("Load", 0L);
		this.weight = weight;
		this.direction = new DirectionDegrees(0);
		this.location = new Point2D(0, 0);
		this.lastLoad = lastLoad;
	}

	/**
	 * Constructor for weight, name and last load attributes
	 * 
	 * @param weight   the weight of the load
	 * @param name     unique name of the load
	 * @param lastLoad true if this is the last in a set of loads being transmitted,
	 *                 false otherwise
	 */
	public Load(WeightPounds weight, String name, boolean lastLoad)
	{
		super(name, 0L);
		this.weight = weight;
		this.direction = new DirectionDegrees(0);
		this.location = new Point2D(0, 0);
		this.lastLoad = lastLoad;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Load [load=");
		builder.append(weight);
		builder.append(", direction=");
		builder.append(direction);
		builder.append(", location=");
		builder.append(location);
		builder.append(", lastLoad=");
		builder.append(lastLoad);
		builder.append("]");
		return builder.toString();
	}
}
