package roboticmower.signals;

import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.items.SysMLSignal;
import sysmlinjava.javaannotations.attributes.Attribute;

/**
 * Signal for transmission of a {@code Point2D} instance between ports
 * 
 * @author ModelerOne
 *
 */
public class Point2DSignal extends SysMLSignal
{
	/**
	 * Attribute for the {@code Point2D} instance carried by the signal
	 */
	@Attribute
	public Point2D point;

	/**
	 * Constructor
	 * 
	 * @param point {@code Point2D} instance to be carried by the signal
	 */
	public Point2DSignal(Point2D point)
	{
		super();
		this.point = point;
	}

	@Override
	public String stackNamesString()
	{
		return String.format("Point2D: %2.2f x, %2.2f y", point.xValue, point.yValue);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Point2DSignal [point=");
		builder.append(point);
		builder.append(", name=");
		builder.append(name);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
}
