package cablestayedbridge;

import sysmlinjava.attributetypes.AreaInchesSquare;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.attributetypes.WeightPounds;

/**
 * Cable that crosses the pylon's cable saddle to suspend: deck section 1,
 * southwest corner and the deck section 8, northeast corner.
 * 
 * @author ModelerOne
 */
public class CableSW1toNE8 extends Cable
{
	/**
	 * Constructor
	 * 
	 * @param name              unique name of the cable
	 * @param id                unique ID of the cable
	 * @param bridge            bridge of which cable is a part
	 */
	public CableSW1toNE8(String name, long id, CableStayedBridge bridge)
	{
		super(name, id, bridge);
	}

	@Override
	protected void createAttributes()
	{
		SuspensionPoints suspensionPoints = new SuspensionPoints();
		eastPoint = suspensionPoints.north.get(8);
		westPoint = suspensionPoints.south.get(1);
		topPoint = new RReal(suspensionPoints.top.zValue);
		super.createAttributes();
		crossSectionArea = new AreaInchesSquare(42.0);
		breakingStrength = new WeightPounds(tensileStrength.value * crossSectionArea.value);
		socketBreakingStrength = new WeightPounds(breakingStrength); // Assumed socket is at least as strong as cable
		weight = new WeightPounds(crossSectionArea.multipliedBy(length).multipliedBy(weightPerCubicFoot));
	}
}
