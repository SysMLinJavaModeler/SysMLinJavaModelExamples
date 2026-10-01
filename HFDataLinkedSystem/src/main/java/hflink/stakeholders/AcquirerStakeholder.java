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
 * Stakeholder for those responsible for system aquisition
 */
public class AcquirerStakeholder extends SysMLStakeholder
{
	/**
	 * Concern for system availability
	 */
	@Concern
	SysMLConcern subsystemAvailabilityConcern;

	/**
	 * Concern for system affordability
	 */
	@Concern
	SysMLConcern affordabilityConcern;

	/**
	 * Hyperlink for system specification
	 */
	@Hyperlink
	public SysMLHyperlink systemSpec;

	/**
	 * Availability for reviews
	 */
	@Attribute
	public Percent availabilityForReview;

	/**
	 * Constructor
	 */
	public AcquirerStakeholder()
	{
		super("AcquirerStakeholder", 0L);
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
		availabilityForReview = new Percent(50);
	}

	@Override
	protected void createConcerns()
	{
		subsystemAvailabilityConcern = HFDataLinkedSystemConcerns.subsystemAvailabilityConcern;
		affordabilityConcern = HFDataLinkedSystemConcerns.affordabilityConcern;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		systemSpec = HFDatalinkedSystemHyperlinks.systemSpec;
	}
}
