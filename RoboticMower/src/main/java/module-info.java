/**
 * Module for the Robotic Mower model in SysMLinJava. Module exports all
 * packages, consistent with being a SysMLinJava-based model. It requires only
 * the SysMLinJava module and the java modules.
 * <p>
 * The model is executed via run of the {@code roboticmower.Domain}'s main
 * method. Assuming the SysMLinJava module and the RoboticMower module are each
 * configured as a project in an IDE workspace, the following command, with file
 * paths adapted to the particular environment, should execute the model.
 * 
 * <pre>{@code
 * java.exe
-Dfile.encoding=UTF-8
-p "C:\Users\ModelerOne\Workspace\RoboticMower\bin;C:\Users\ModelerOne\Workspace\SysMLinJava\bin"
-XX:+ShowCodeDetailsInExceptionMessages
-m RoboticMower/roboticmower.Domain}
 * </pre>
 *
 */
module RoboticMower
{
	exports roboticmower;
	exports roboticmower.analysis;
	exports roboticmower.positioncontroller;
	exports roboticmower.mower;
	exports roboticmower.ports;
	exports roboticmower.signals;
	exports roboticmower.info;

	requires java.logging;
	requires transitive sysMLinJava;
}