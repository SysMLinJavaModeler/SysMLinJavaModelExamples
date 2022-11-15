/**
 * The CableStayedBridge model demonstrates how to build a model of many static
 * forces interacting to provide a structure, e.g a "cable-stayed" bridge
 * typical of modern day suspension bridges. The model uses the SysML full-port
 * to transfer forces from one structural component to another. As might be
 * expected of a suspension bridge, numerous port connections are used by the
 * model. The model also includes a number of vehicles that move across the
 * brige deck, transfering their loads to the deck in new locations as they
 * travel. These vehicle loads are then trasferred via ports to the deck which
 * transfers loads to the cables, which transfer loads to the pylon which
 * transfer loads to the ground.
 * 
 * The model includes a constraint block that binds constraint parameters to the
 * loads on the bridge, translating/constraining the parameters into a dynamic
 * bar chart display that shows the loads on the bridges cables as the vehicles
 * move across the bridge deck. Once again, a textual representation of the bar
 * chart display can be produced by objects in the SysMLinJava API/MDF, but a
 * tool will soon be available (for a modest fee) that can display a graphical
 * bar chart of the cable loads.
 * 
 * This model dependencies include the standard Java API and the SysMLinJava
 * module.
 * 
 * @author ModelerOne
 *
 */
module CableStayedBridge
{
	exports cablestayedbridge.ports;
	exports cablestayedbridge;

	requires java.logging;
	requires transitive sysMLinJava;
}