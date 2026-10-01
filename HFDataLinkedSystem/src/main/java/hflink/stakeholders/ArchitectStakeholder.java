package hflink.stakeholders;

import hflink.common.HFDatalinkedSystemHyperlinks;
import hflink.requirements.HFDataLinkedSystemConcerns;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.Percent;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.requirements.Concern;
import sysmlinjava.parts.SysMLStakeholder;
import sysmlinjava.requirements.SysMLConcern;

/**
 * Stakeholder for system architecture
 */
public class ArchitectStakeholder extends SysMLStakeholder
{
	/**
	 * Concern for system affordability
	 */
	@Concern
	public SysMLConcern affordabilityConcern;
	
	/**
	 * Concern for system feasibility
	 */
	@Concern
	public SysMLConcern feasibilityConcern;
	
	/**
	 * Link to system specification document
	 */
	@Hyperlink
	public static SysMLHyperlink systemSpec;

	/**
	 * Availability for reviews
	 */
	@Attribute
	public Percent availabilityForReview;

	/**
	 * Constructor
	 */
	public ArchitectStakeholder()
	{
		super("ArchitectStakeholder", 0L);
	}

	/**
	 * Process for stakeholder to review model
	 */
	@Action
	public void reviewModel()
	{
		receiveModel();
		generateModelViews();
		reviewViews();
		createComments();
		sendComments();
	}

	private void sendComments()
	{
		// TODO Auto-generated method stub

	}

	private void reviewViews()
	{
		// TODO Auto-generated method stub

	}

	private void generateModelViews()
	{
		// TODO Auto-generated method stub

	}

	private void receiveModel()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void createAttributes()
	{
		availabilityForReview = new Percent(30);
	}

	@Override
	protected void createConcerns()
	{
		affordabilityConcern = HFDataLinkedSystemConcerns.affordabilityConcern;
		feasibilityConcern = HFDataLinkedSystemConcerns.feasibilityConcern;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		systemSpec = HFDatalinkedSystemHyperlinks.systemSpec;
	}
}
