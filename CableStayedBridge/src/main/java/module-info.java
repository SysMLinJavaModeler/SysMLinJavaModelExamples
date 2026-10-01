/**
 * Module for executable model of a cable-stayed bridge. All compilation units
 * are exported for use in testing/analysis as needed.
 */
module cableStayedBridge
{
	exports cablestayedbridge.ports;
	exports cablestayedbridge;

	requires java.logging;
	requires transitive sysMLinJava;
}