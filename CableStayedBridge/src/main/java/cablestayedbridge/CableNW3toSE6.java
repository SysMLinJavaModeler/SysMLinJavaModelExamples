package cablestayedbridge;

import sysmlinjava.valuetypes.AreaInchesSquare;
import sysmlinjava.valuetypes.WeightPounds;

public class CableNW3toSE6 extends Cable
{
	public CableNW3toSE6(String name, long id, CableStayedBridge bridge)
	{
		super(name, id, bridge);
	}

	@Override
	protected void createValues()
	{
		eastPoint = ((CableStayedBridge)contextBlock.get()).deck.suspensionPointsNorth.get(3);
		westPoint = ((CableStayedBridge)contextBlock.get()).deck.suspensionPointsSouth.get(6);
		super.createValues();
		crossSectionArea = new AreaInchesSquare(34.0);
		breakingStrength = new WeightPounds(tensileStrength.value * crossSectionArea.value);
		socketBreakingStrength = new WeightPounds(breakingStrength); // Assumed socket is at least as strong as cable
		weight = new WeightPounds(crossSectionArea.multipliedBy(length).multipliedBy(weightPerCubicFoot));
	}
}
