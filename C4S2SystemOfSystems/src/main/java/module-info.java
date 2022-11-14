/**
 * The {@code C4S2SystemOfSystems} module is the SysMLinJava executable model of
 * a command, control, computers, communications, surveillance, and strike
 * system-of-systems that uses a radar system to find, fix, track, and asses a
 * target, and a strike system (drone, fighter) to target and engage the target.
 * A minimal block diagram of the domain is as follows:<br>
 * <img src="C4S2SystemOfSystems.png" alt="PNG file not available" height=
 * "500"><br>
 * The model is highly-simplified and artifically constrained to demonstrate key
 * concepts of using SysMLinJava to model and simulate a complex
 * system-of-systems.
 * <p>
 * The {@code C4S2SystemOfSystems} module uses the c4s2 package and all its
 * sub-packages and requires the {@code sysMLinJava} and
 * {@code SysMLinJavaLibrary} modules.
 * <p>
 * Note that all of the packages in this module are fully exported. This
 * "openness" is consistent with this module being a SysMLinJava model, not just
 * a software module, whereby all the code is what constitutes the model.
 * Exposing all of the model enables its virtually unlimited applications.
 * 
 * @author ModelerOne
 *
 */
module C4S2SystemOfSystems
{
	exports c4s2;
	exports c4s2.common.events;
	exports c4s2.common.messages;
	exports c4s2.common.objects.information;
	exports c4s2.common.ports;
	exports c4s2.common.ports.information;
	exports c4s2.common.ports.matter;
	exports c4s2.common.signals;
	exports c4s2.common.valueTypes;
	exports c4s2.components;
	exports c4s2.components.common;
	exports c4s2.components.computer.operator;
	exports c4s2.components.computer.services;
	exports c4s2.components.services;
	exports c4s2.components.services.operator;
	exports c4s2.components.services.radar;
	exports c4s2.components.services.strike;
	exports c4s2.components.services.system;
	exports c4s2.components.services.target;
	exports c4s2.domain;
	exports c4s2.parametrics;
	exports c4s2.platforms;
	exports c4s2.requirements;
	exports c4s2.systems.c4s2;
	exports c4s2.systems.radar;
	exports c4s2.systems.strike;
	exports c4s2.systems.target;
	exports c4s2.users;

	requires transitive SysMLinJavaLibrary;
	requires java.logging;
	requires transitive sysMLinJava;
}