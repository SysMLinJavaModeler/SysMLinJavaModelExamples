package cablestayedbridge;

import sysmlinjava.valuetypes.AreaInchesSquare;
import sysmlinjava.valuetypes.WeightPounds;

public class CableNW1toSE8 extends Cable
{
	public CableNW1toSE8(String name, long id, CableStayedBridge bridge)
	{
		super(name, id, bridge);
	}

	@Override
	protected void createValues()
	{
		eastPoint = ((CableStayedBridge)contextBlock.get()).deck.suspensionPointsNorth.get(1);
		westPoint = ((CableStayedBridge)contextBlock.get()).deck.suspensionPointsSouth.get(8);
		super.createValues();
		crossSectionArea = new AreaInchesSquare(42.0);
		breakingStrength = new WeightPounds(tensileStrength.value * crossSectionArea.value);
		socketBreakingStrength = new WeightPounds(breakingStrength); // Assumed socket is at least as strong as cable
		weight = new WeightPounds(crossSectionArea.multipliedBy(length).multipliedBy(weightPerCubicFoot));
	}
}
