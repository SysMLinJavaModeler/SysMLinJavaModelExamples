package hflink.views;

import java.util.List;

import hflink.domain.HFLinkDomain;
import hflink.viewpoints.HFDataLinkedSystemViewpoints;
import sysmlinjava.metadata.SysMLElementGroup;
import sysmlinjava.views.SysMLView;
import sysmlinjava.views.barcharts.BarGraphRendering;

@SuppressWarnings("javadoc")
public class FrequencyView extends SysMLView
{
	public FrequencyView()
	{
		super("Frequency View", 0L);
	}

	@Override
	protected void createSatisfiedViewpoints()
	{
		satisfiedViewpoints = List.of(HFDataLinkedSystemViewpoints.opsViewpoint, HFDataLinkedSystemViewpoints.developerViewpoint);
	}

	@Override
	protected void createExposedPartTypes()
	{
		exposedPartTypes = List.of(HFLinkDomain.class);
	}

	@Override
	protected void createElementFilters()
	{
		filteredInElements = new SysMLElementGroup(true, List.of(HFLinkDomain.class), List.of(), "includedElements", 0L);
		filteredOutElements = new SysMLElementGroup(true, List.of(), List.of(), "exludedElements", 0L);
	}

	@Override
	protected void createRenderings()
	{
		renderings = List.of(BarGraphRendering.class);
	}

	@Override
	protected void createSubviews()
	{
		subViews = List.of();
	}

}
