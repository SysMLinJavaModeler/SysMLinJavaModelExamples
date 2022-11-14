/**
 * The {@code H2OStateMachine} module is the SysMLinJava executable model of
 * water in all of its possible states and the "triggers" that transition it
 * between the states. The {@code H2OStateMachine} is a SysMLinJava
 * implementation of the SysML state machine for H2O found in the book "A
 * Practical Guide to SysML - The Systems Modeling Language 3rd edition" by
 * Sanford Friedenthal, et al; Object Management Group; Morgan Kaufman
 * publisher; &copy; 2015.
 * <p>
 * Note that all of the packages in this module are fully exported and opened.
 * This "openness" is consistent with this module being a SysMLinJava model, not
 * just a software module, whereby all the code is what constitutes the model.
 * Exposing all of the model enables its virtually unlimited applications.
 * 
 * @author ModelerOne
 *
 */
module H2OStateMachine
{
	exports h2ostates;

	requires java.logging;
	requires transitive sysMLinJava;
}