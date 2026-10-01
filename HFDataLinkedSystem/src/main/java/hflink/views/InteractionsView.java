package hflink.views;

import java.util.List;

import hflink.domain.HFLinkDomain;
import hflink.viewpoints.HFDataLinkedSystemViewpoints;
import sysmlinjava.metadata.SysMLElementGroup;
import sysmlinjava.views.SysMLView;
import sysmlinjava.views.interactionssequencediagram.InteractionMessageSequenceDisplay;

@SuppressWarnings("javadoc")
public class InteractionsView extends SysMLView
{
	public InteractionsView()
	{
		super("Interaction Diagram", 0L);
	}

	@Override
	protected void createSatisfiedViewpoints()
	{
		satisfiedViewpoints = List.of(HFDataLinkedSystemViewpoints.opsViewpoint, HFDataLinkedSystemViewpoints.architectureViewpoint, HFDataLinkedSystemViewpoints.developerViewpoint);
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
		renderings = List.of(InteractionMessageSequenceDisplay.class);
	}

	@Override
	protected void createSubviews()
	{
		subViews = List.of();
	}
}
