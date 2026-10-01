package hflink.views;

import java.util.List;

import hflink.domain.HFLinkDomain;
import hflink.viewpoints.HFDataLinkedSystemViewpoints;
import sysmlinjava.metadata.SysMLElementGroup;
import sysmlinjava.views.SysMLView;
import sysmlinjava.views.tabulardisplay.TabularRendering;

/**
 * View for bill-of materials for modeled part types.
 */
public class BillOfMaterialsView extends SysMLView
{
	/**
	 * Constructor
	 * @param name unique name
	 * @param id unique identifier
	 */
	public BillOfMaterialsView(String name, Long id)
	{
		super(name, id);
	}

	@Override
	protected void createSatisfiedViewpoints()
	{
		satisfiedViewpoints = List.of(HFDataLinkedSystemViewpoints.architectureViewpoint, HFDataLinkedSystemViewpoints.developerViewpoint, HFDataLinkedSystemViewpoints.acquisitionViewpoint);
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
		renderings = List.of(TabularRendering.class);
	}

	@Override
	protected void createSubviews()
	{
		subViews = List.of();
	}

}
