package cablestayedbridge;

import sysmlinjava.valuetypes.AreaInchesSquare;
import sysmlinjava.valuetypes.WeightPounds;

public class CableNW0toSE9 extends Cable
{
	public CableNW0toSE9(String name, long id, CableStayedBridge bridge)
	{
		super(name, id, bridge);
	}

	@Override
	protected void createValues()
	{
		eastPoint = ((CableStayedBridge)contextBlock.get()).deck.suspensionPointsNorth.get(0);
		westPoint = ((CableStayedBridge)contextBlock.get()).deck.suspensionPointsSouth.get(9);
		super.createValues();
		crossSectionArea = new AreaInchesSquare(45.0);
		breakingStrength = new WeightPounds(tensileStrength.value * crossSectionArea.value);
		socketBreakingStrength = new WeightPounds(breakingStrength); // Assumed socket is at least as strong as cable
		weight = new WeightPounds(crossSectionArea.multipliedBy(length).multipliedBy(weightPerCubicFoot));
	}
}
