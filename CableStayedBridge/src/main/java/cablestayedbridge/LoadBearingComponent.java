package cablestayedbridge;

import sysmlinjava.javaannotations.actions.Action;

/**
 * Operations/receptions required of a {@code SysMLPart} that represents a load-bearing
 * component of a structure.
 * 
 * @author ModelerOne
 * @see LoadBearingComponentStateMachine
 */
public interface LoadBearingComponent
{
	/**
	 * Reception to calculate/update the state of the load-bearing component for the
	 * specified load being transmitted to the compoent.
	 * 
	 * @param load load being transmitted to the component
	 */
	@Action
	public abstract void onLoad(Load load);

	/**
	 * Reception to calculate/update the state of the load-bearing component for the
	 * specified "final" load being transmitted to the component, i.e. performs
	 * calculations/updates on the component for all individual loads having now
	 * been applied to the component.
	 * 
	 * @param load "final" load being transmitted to the component
	 */
	@Action
	public abstract void onLoaded(Load load);

	/**
	 * Reception to respond to a component failure.
	 * 
	 * @param failureEvent event for the failure.
	 */
	@Action
	public abstract void onFailed(FailureEvent failureEvent);
}
