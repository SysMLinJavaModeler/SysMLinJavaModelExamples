package hflink.stakeholders;

import hflink.requirements.HFDataLinkedSystemConcerns;
import hflink.requirements.HFDataLinkedSystemHyperlinks;
import sysmlinjava.annotations.SysMLHyperlink;
import sysmlinjava.attributetypes.Percent;
import sysmlinjava.javaannotations.actions.Action;
import sysmlinjava.javaannotations.annotations.Hyperlink;
import sysmlinjava.javaannotations.attributes.Attribute;
import sysmlinjava.javaannotations.requirements.Concern;
import sysmlinjava.parts.SysMLStakeholder;
import sysmlinjava.requirements.SysMLConcern;

/**
 * Stakeholder for system operation
 */
public class OperatorStakeholder extends SysMLStakeholder
{
	/**
	 * Concern for system usability
	 */
	@Concern
	public SysMLConcern usabilityConcern;

	/**
	 * Concern for system use trainability
	 */
	@Concern
	public SysMLConcern trainabilityConcern;
	
	/**
	 * Hyperlink for systems specification
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
	public OperatorStakeholder()
	{
		super("OperatorStakeholder", 0L);
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
		availabilityForReview = new Percent(80);
	}

	@Override
	protected void createConcerns()
	{
		usabilityConcern = HFDataLinkedSystemConcerns.usabilityConcern;
		trainabilityConcern = HFDataLinkedSystemConcerns.trainabilityConcern;
	}

	@Override
	protected void createSupportingInformationLinks()
	{
		systemSpec = HFDataLinkedSystemHyperlinks.systemSpec;
	}
}
