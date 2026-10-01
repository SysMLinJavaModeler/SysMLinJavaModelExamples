package hflink.views;

import java.util.List;

import hflink.domain.HFLinkDomain;
import hflink.viewpoints.HFDataLinkedSystemViewpoints;
import sysmlinjava.metadata.SysMLElementGroup;
import sysmlinjava.views.SysMLView;
import sysmlinjava.views.requirementsdisplay.RequirementsSpecRendering;

@SuppressWarnings("javadoc")
public class RequirementsView extends SysMLView
{
	public RequirementsView()
	{
		super("Requirements Spec", 0L);
	}

	@Override
	protected void createSatisfiedViewpoints()
	{
		satisfiedViewpoints = List.of(HFDataLinkedSystemViewpoints.opsViewpoint, HFDataLinkedSystemViewpoints.architectureViewpoint, HFDataLinkedSystemViewpoints.developerViewpoint, HFDataLinkedSystemViewpoints.acquisitionViewpoint);
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
		renderings = List.of(RequirementsSpecRendering.class);
	}

	@Override
	protected void createSubviews()
	{
		subViews = List.of();
	}

}
