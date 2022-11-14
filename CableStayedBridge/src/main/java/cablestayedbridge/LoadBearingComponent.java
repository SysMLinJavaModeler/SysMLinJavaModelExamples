package cablestayedbridge;

import sysmlinjava.annotations.Reception;
import sysmlinjava.blocks.SysMLInterfaceBlock;

/**
 * Operations required of a {@code SysMLBlock} that represents a load-bearing
 * component of a structure.
 * 
 * @author ModelerOne
 * @see LoadBearingComponentStateMachine
 */
public interface LoadBearingComponent extends SysMLInterfaceBlock
{
	/**
	 * Reception to calculate/update the state of the load-bearing component for the
	 * specified load being transmitted to the compoent.
	 * 
	 * @param load load being transmitted to the component
	 */
	@Reception
	public abstract void onLoad(Load load);

	/**
	 * Reception to calculate/update the state of the load-bearing component for the
	 * specified "final" load being transmitted to the component, i.e. performs
	 * calculations/updates on the component for all individual loads having now
	 * been applied to the component.
	 * 
	 * @param load "final" load being transmitted to the component
	 */
	@Reception
	public abstract void onLoaded(Load load);

	/**
	 * Reception to respond to a component failure.
	 * 
	 * @param failureEvent event for the failure.
	 */
	@Reception
	public abstract void onFailed(FailureEvent failureEvent);
}
