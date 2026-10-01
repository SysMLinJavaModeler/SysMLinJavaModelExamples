package cablestayedbridge;

import sysmlinjava.attributetypes.AreaInchesSquare;
import sysmlinjava.attributetypes.RReal;
import sysmlinjava.attributetypes.WeightPounds;

/**
 * Cable that crosses the pylon's cable saddle to suspend: deck section 3,
 * northwest corner and the deck section 6, southeast corner.
 * 
 * @author ModelerOne
 */
public class CableNW3toSE6 extends Cable
{
	/**
	 * Constructor
	 * 
	 * @param name              unique name of the cable
	 * @param id                unique ID of the cable
	 * @param bridge            bridge of which cable is a part
	 */
	public CableNW3toSE6(String name, long id, CableStayedBridge bridge)
	{
		super(name, id, bridge);
	}

	@Override
	protected void createAttributes()
	{
		SuspensionPoints suspensionPoints = new SuspensionPoints();
		eastPoint = suspensionPoints.south.get(6);
		westPoint = suspensionPoints.north.get(3);
		topPoint = new RReal(suspensionPoints.top.zValue);
		super.createAttributes();
		crossSectionArea = new AreaInchesSquare(34.0);
		breakingStrength = new WeightPounds(tensileStrength.value * crossSectionArea.value);
		socketBreakingStrength = new WeightPounds(breakingStrength); // Assumed socket is at least as strong as cable
		weight = new WeightPounds(crossSectionArea.multipliedBy(length).multipliedBy(weightPerCubicFoot));
	}
}
