package cablestayedbridge;

import sysmlinjava.attributetypes.ListOrdered;
import sysmlinjava.attributetypes.Point2D;
import sysmlinjava.attributetypes.Point3D;
import sysmlinjava.attributetypes.SysMLAttributeType;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.units.SysMLinJavaUnits;

/**
 * Points of suspension on bridge deck
 */
public final class SuspensionPoints extends SysMLAttributeType
{
	/**
	 * List of suspension points on north side
	 */
	@Attribute
	public ListOrdered<Point2D> north;
	/**
	 * List of suspension points on south side
	 */
	@Attribute
	public ListOrdered<Point2D> south;
	/**
	 * Suspension point on top of pylon
	 */
	@Attribute
	public Point3D top;

	/**
	 * Constructor
	 */
	public SuspensionPoints()
	{
		super("SuspensionPoints", 0L);
	}

	@Override
	protected void createAttributes()
	{
		north = new ListOrdered<>(
		new Point2D( 50, 90), new Point2D(100, 90), new Point2D(150, 90), new Point2D(200, 90), new Point2D(250, 90),
		new Point2D(350, 90), new Point2D(400, 90), new Point2D(450, 90), new Point2D(500, 90), new Point2D(550, 90));

		south = new ListOrdered<>(
		new Point2D( 50,  0), new Point2D(100,  0), new Point2D(150,  0), new Point2D(200,  0), new Point2D(250,  0),
		new Point2D(350,  0), new Point2D(400,  0), new Point2D(450,  0), new Point2D(500,  0), new Point2D(550,  0));

		top = new Point3D(300, 45, 250);
	}

	@Override
	protected void createUnits()
	{
		units = SysMLinJavaUnits.Point;

	}
}
