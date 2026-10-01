/**
 * Module for the AI controlled deep-brain stimulation system executable SysML
 * model
 */
module aiControlledDBSSystem
{
	exports dbssystem.sensors;
	exports dbssystem;
	exports dbssystem.controller;
	exports dbssystem.actuators;
	exports dbssystem.common;
	exports dbssystem.patient;

	requires java.logging;
	requires neurophcore;
	requires transitive sysMLinJava;
	requires visrecapi;
}