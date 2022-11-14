/**
 * {@code ElectricCircuit} module is a SysMLinJava model of a simple electrical
 * circuit of an AC voltage source in parallel with an RC series circuit and a
 * RL series circuit. A minimal block diagram of the domain is as follows:<br>
 * <img src="ElectricCircuitModel.png" alt="PNG file not available" height="400"><br>
 * The model is a SysMLinJava implementation of the model by the same name
 * described in "SysML Extension for Physical Interaction and Signal Flow
 * Simulation", Object Management Group, Inc., 2018. The concept of the electric
 * circuit is the use of the SysML proxy port to model the interface between
 * electrical components, and the use of the SysML constraint block to perform
 * parametric specification and analysis of the circuit model.
 * <p>
 * The {@code ElectricCurcuit} module exports its only package -
 * {@code elecriccircuit} - so that the SysMLinJava model can be fully utilized
 * in other models as desired. The module requires only the {@code SysMLinJava}
 * module to specify and execute the model. The module's "{@code main()}" class
 * is the {@code Circuit} block which includes all the parts and constraint
 * blocks for the circuit model, as well as the {@code main()} operation that
 * actually executes the model.
 * <p>
 * The {@code ElectricCurcuit} module utilizes features of the SysMLinJava API
 * that enable use of graphical displays of data for parametric analysis of the
 * model. These graphical displays are available in the SysMLinJava
 * TaskMaster&trade; application available at
 * <a href="http://SysMLinJava.com">http://SysMLinJava.com</a>
 * <p>
 * This module can be executed using the <em>ExecuteModel.bat</em> file on a
 * command line.
 * 
 * @see <a href="http://www.omg.org/spec/SysPhS/1.0/PDF">SysML Extension for
 *      Physical Interaction and Signal Flow Simulation</a>
 * 
 * @author ModelerOne
 */
module ElectricCircuit
{
	exports electriccircuit;

	requires transitive sysMLinJava;
}