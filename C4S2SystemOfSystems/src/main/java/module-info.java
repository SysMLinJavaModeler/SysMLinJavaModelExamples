/**
 * Module for the Command/Control System of System executable model
 */
module c4S2SystemOfSystems
{
	exports c4s2.parametrics;
	exports c4s2.components.services.system;
	exports c4s2.components.services.target;
	exports c4s2.domain;
	exports c4s2.components.services;
	exports c4s2.common.attributetypes;
	exports c4s2.common.ports.matter;
	exports c4s2.systems.c4s2;
	exports c4s2.common.items;
	exports c4s2.components;
	exports c4s2.common.messages;
	exports c4s2.common.ports;
	exports c4s2.components.computer.services;
	exports c4s2.components.computer.operator;
	exports c4s2.requirements;
	exports c4s2.common.signals;
	exports c4s2.systems.radar;
	exports c4s2.systems.target;
	exports c4s2.common.ports.information;
	exports c4s2.components.services.radar;
	exports c4s2.common.items.information;
	exports c4s2.systems;
	exports c4s2.systems.strike;
	exports c4s2.components.services.strike;
	exports c4s2.components.common;
	exports c4s2.components.services.operator;
	exports c4s2.common;
	exports c4s2.users;
	exports c4s2;
	exports c4s2.platforms;

	requires transitive sysMLinJava;
	requires transitive sysMLinJavaLibrary;
	requires java.logging;
}