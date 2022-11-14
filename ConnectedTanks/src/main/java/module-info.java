/**
 * Module {@code ConnectedTanks} is a SysMLinJava model of a simple system of
 * two tanks of fluid connected by a pipe. A minimal block diagram of the domain
 * is as follows:<br>
 * <img src="ConnectedTanksModel.png" alt="PNG file not available" height=
 * "400"><br>
 * The model is a SysMLinJava implementation of the model by the same name
 * described in "SysML Extension for Physical Interaction and Signal Flow
 * Simulation", Object Management Group, Inc., 2018. The concept of the
 * connected tanks system is the use of the SysML proxy port to model the
 * interface between tank and pipe, and the use of the SysML constraint block to
 * perform parametric specification and analysis of the system model.
 * <p>
 * Note that all of the packages in this module are fully exported and opened.
 * This "openness" is consistent with this module being a SysMLinJava model, not
 * just a software module, whereby all the code is what constitutes the model.
 * Exposing all of the model enables its virtually unlimited extension and
 * application.
 * <p>
 * The {@code ConnectedTanks} module utilizes features of the SysMLinJava API
 * that enable use of graphical displays of data for parametric analysis of the
 * model. These graphical displays are available in the SysMLinJava
 * TaskMaster&trade; application available at
 * <a href="http://SysMLinJava.com">http://SysMLinJava.com</a>
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 */
module ConnectedTanks
{
	exports connectedtanks;

	requires java.logging;
	requires transitive sysMLinJava;
}