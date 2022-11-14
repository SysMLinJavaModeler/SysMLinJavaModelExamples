package cablestayedbridge;

import sysmlinjava.valuetypes.AreaInchesSquare;
import sysmlinjava.valuetypes.WeightPounds;

public class CableNW2toSE7 extends Cable
{
	public CableNW2toSE7(String name, long id, CableStayedBridge bridge)
	{
		super(name, id, bridge);
	}

	@Override
	protected void createValues()
	{
		eastPoint = ((CableStayedBridge)contextBlock.get()).deck.suspensionPointsNorth.get(2);
		westPoint = ((CableStayedBridge)contextBlock.get()).deck.suspensionPointsSouth.get(7);
		super.createValues();
		crossSectionArea = new AreaInchesSquare(38.0);
		breakingStrength = new WeightPounds(tensileStrength.value * crossSectionArea.value);
		socketBreakingStrength = new WeightPounds(breakingStrength); // Assumed socket is at least as strong as cable
		weight = new WeightPounds(crossSectionArea.multipliedBy(length).multipliedBy(weightPerCubicFoot));
	}
}
