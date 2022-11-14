/**
 * Module {@code TrafficSignalControlSystem} is a SysMLinJava executable model
 * of a small traffic control system that provides a priority response to the
 * presence of emergency vehicles. A minimal block diagram of the domain is as
 * follows:<br>
 * <img src="TrafficSignalControlSystemModel.png" alt="PNG file not available"
 * height="400"><br>
 * The modeled system consists of four intersection signal systems operating in
 * synchrony with the overall traffic control system. The system operates in
 * either normal operations mode or emergency mode. The system states consist of
 * sub-state machines, one for each intersection, operating in parallel and
 * controlled by the compound state. Interfaces between the overall control
 * system and the individual intersection signal systems is via simple event
 * passing between state machines.
 * <p>
 * Note that all of the packages in this module are fully exported and opened.
 * This "openness" is consistent with this module being a SysMLinJava model, not
 * just a software module, whereby all the code is what constitutes the model.
 * Exposing all of the model enables its virtually unlimited applications.
 * 
 * @author ModelerOne
 *
 */
module TrafficSignalControlSystem
{
	exports trafficcontrolsystem;

	opens trafficcontrolsystem;

	requires transitive sysMLinJava;
}