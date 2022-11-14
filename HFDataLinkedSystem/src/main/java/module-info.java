/**
 * Module {@code HFDataLinkedSystem} is a SysMLinJava model of a HF data
 * link-based system-of-systems. A minimal block diagram of the domain is as
 * follows:<br>
 * <img src="HFDataLinkedSystem.png" alt="PNG file not available" height=
 * "500"><br>
 * The concept of the HF data link-based systems is the command/control of
 * long-range remote systems via basic internet protocols over TDMA over HF
 * communications links. A {@code CommandControlSystem} is used by an operator
 * to communicate with remote {@code DeployedSystem}s that serve as proxies for
 * some remote system such as an oil rig, maritime vessel, weather monitor, etc.
 * <p>
 * Note that all of the packages in this module are fully exported and opened.
 * This "openness" is consistent with this module being a SysMLinJava model, not
 * just a software module, whereby all the code is what constitutes the model.
 * Exposing all of the model enables its virtually unlimited applications.
 * 
 * @author ModelerOne
 */
module HFDataLinkedSystem
{
	exports hflink.common.events;
	exports hflink.common.objects;
	exports hflink.common.ports;
	exports hflink.common.signals;
	exports hflink.components.deployedcomputer;
	exports hflink.components.modemradio;
	exports hflink.components.switchrouter;
	exports hflink.components.c2computer;
	exports hflink.constraintblocks;
	exports hflink.domain;
	exports hflink.requirements;
	exports hflink.systems.gps;
	exports hflink.systems.c2system;
	exports hflink.systems.deployedsystem;
	exports hflink.tests;
	exports hflink.views;

	requires java.logging;
	requires transitive sysMLinJava;
}