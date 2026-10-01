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
 * Stakeholder for system development
 */
public class DeveloperStakeholder extends SysMLStakeholder
{
	/**
	 * Concern for affordability of system
	 */
	@Concern
	public SysMLConcern affordabilityConcern;

	/**
	 * Concern for feasibility of system
	 */
	@Concern
	public SysMLConcern feasibilityConcern;
	
	/**
	 * Availability for reviews
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
	public DeveloperStakeholder()
	{
		super("DeveloperStakeholder", 0L);
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
		availabilityForReview = new Percent(100);
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
