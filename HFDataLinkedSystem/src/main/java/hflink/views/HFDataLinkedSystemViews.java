package hflink.views;

import java.util.List;
import sysmlinjava.annotations.views.Concern;
import sysmlinjava.annotations.views.Language;
import sysmlinjava.annotations.views.Method;
import sysmlinjava.annotations.views.Presentation;
import sysmlinjava.annotations.views.Purpose;
import sysmlinjava.annotations.views.Stakeholder;
import sysmlinjava.annotations.views.View;
import sysmlinjava.annotations.views.Viewpoint;
import sysmlinjava.views.SysMLStakeholder;
import sysmlinjava.views.SysMLView;
import sysmlinjava.views.SysMLViewpoint;
import sysmlinjava.views.SysMLViews;

public class HFDataLinkedSystemViews extends SysMLViews
{
	@Purpose public static final String purposeA = "This is purpose A";
	@Purpose public static final String purposeB = "This is purpose B";
	@Purpose public static final String purposeC = "This is purpose C";

	@Concern public static final String concernA = "This is concern A";
	@Concern public static final String concernB = "This is concern B";
	@Concern public static final String concernC = "This is concern C";

	@Language public static final String languageA = "This is language A";
	@Language public static final String languageB = "This is language B";
	@Language public static final String languageC = "This is language C";

	@Method public static final String methodA = "This is method A";
	@Method public static final String methodB = "This is method B";
	@Method public static final String methodC = "This is method C";

	@Presentation public static final String presentationA = "This is presentation A";
	@Presentation public static final String presentationB = "This is presentation B";
	@Presentation public static final String presentationC = "This is presentation C";

	@Stakeholder
	public static final SysMLStakeholder stakeholderA = new SysMLStakeholder("StakeholderA", List.of(concernA));
	@Stakeholder
	public static final SysMLStakeholder stakeholderB = new SysMLStakeholder("StakeholderB", List.of(concernB));
	@Stakeholder
	public static final SysMLStakeholder stakeholderC = new SysMLStakeholder("StakeholderC", List.of(concernB, concernC));

	@Viewpoint
	public static final SysMLViewpoint viewpointA = new SysMLViewpoint("ViewpointA", List.of(stakeholderA), purposeA, List.of(concernA), List.of(languageA), List.of(presentationA), List.of(methodA));
	@Viewpoint
	public static final SysMLViewpoint viewpointB = new SysMLViewpoint("ViewpointB", List.of(stakeholderB), purposeB, List.of(concernB), List.of(languageB), List.of(presentationB), List.of(methodB));
	@Viewpoint
	public static final SysMLViewpoint viewpointC = new SysMLViewpoint("ViewpointC", List.of(stakeholderB, stakeholderC), purposeC, List.of(concernB, concernC), List.of(languageB, languageC), List.of(presentationB, presentationC), List.of(methodB, methodC));

	@View
	public static final SysMLView viewA = new SysMLView("ViewA", viewpointA, List.of());
	@View
	public static final SysMLView viewB = new SysMLView("ViewB", viewpointB, List.of());
	@View
	public static final SysMLView viewC = new SysMLView("ViewC", viewpointC, List.of(viewA, viewB));

	static
	{
		stakeholderInstances.addAll(List.of(stakeholderA, stakeholderB, stakeholderC));
		viewpointInstances.addAll(List.of(viewpointA, viewpointB, viewpointC));
		viewInstances.addAll(List.of(viewA, viewB, viewC));
	}
}
