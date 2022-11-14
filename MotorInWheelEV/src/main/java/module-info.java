/**
 * The {@code MotorInWheelEV} is a SysMLinJava model of an electric vehicle that
 * uses so-called motor-in-wheel technology. A minimal block diagram of the
 * domain is as follows:<br>
 * <img src="MotorInWheelEV.png" alt="PNG file not available"
 * height="500"/><br>
 * The concept of the motor-in-wheel based vehicle is that dedicated electric
 * motors each provide torque to one of the four wheels that drive the vehicle.
 * All motors are powered by a central power supply. This technology is
 * different from the more common EV that uses a single larger motor to provides
 * torque to a single mechanical drive train that transfers the torque to the
 * four wheels.
 * <p>
 * The {@code MotorInWheelEV} module uses the {@code motorinwheel} package and
 * all its sub-packages and requires the {@code sysMLinJava} module for its
 * SysML-based modeling and execution.
 * <p>
 * Note that all of the packages in this module are fully exported and opened.
 * This "openness" is consistent with this module being a SysMLinJava model, not
 * just a software module, whereby all the code is what constitutes the model.
 * Exposing all of the model enables its virtually unlimited applications.
 * 
 * @author ModelerOne
 *
 */
module MotorInWheelEV
{
	exports motorinwheel.common.ports.matter;
	exports motorinwheel.common.stateMachine;
	exports motorinwheel.common.ports.energy;
	exports motorinwheel.components.motor;
	exports motorinwheel.systems.motorinwheel;
	exports motorinwheel.systems.operator;
	exports motorinwheel.systems.vehicle;
	exports motorinwheel.components.elecpowersupply;
	exports motorinwheel.constraintblocks.system.vehicle;
	exports motorinwheel.domain;
	exports motorinwheel.constraintblocks;
	exports motorinwheel.components.acceleration;
	exports motorinwheel.components.deceleration;
	exports motorinwheel.components.operatoreyes;
	exports motorinwheel.components.suspensionchassisbody;
	exports motorinwheel.systems.roadway;
	exports motorinwheel.constraintblocks.system;
	exports motorinwheel;
	exports motorinwheel.common;
	exports motorinwheel.components;
	exports motorinwheel.components.brake;
	exports motorinwheel.common.events;
	exports motorinwheel.common.signals;
	exports motorinwheel.systems;
	exports motorinwheel.common.ports.information;
	exports motorinwheel.components.operatorleg;
	exports motorinwheel.components.operatordisplays;
	exports motorinwheel.common.ports;
	exports motorinwheel.systems.atmosphere;
	exports motorinwheel.components.wheel;

	requires java.logging;
	requires transitive sysMLinJava;
}