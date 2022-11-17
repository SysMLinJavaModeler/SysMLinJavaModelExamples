# SysMLinJava Model Examples

### Examples of using SysMLinJava for high precision MBSE
SysMLinJava is a Java-based model development kit (MDK) for high-precision modeling of executable SysML models in the Java programming language.  This repository contains nine example models that are based on SysMLinJava.   Each example can be downloaded and imported into a IDE as a project.  The SysMLinJava module, on which the example model will depend, should be in the IDE as another java project.  The examples are fully tested and can be compiled, built, and executed within the scope of the IDE project.

## Installing the Example Models
The easiest way to install the example models is to download the full set of example models as a zip file from this repository and extract the model folders to a folder on your computer.  Then use your IDE to create a java project for each of the example models you wish to view, build, and execute.  Then you can import or copy the code for the example models into their corresponding Java projects.  Make sure you include the "module-info.java" file for each model in its project so that the IDE can know how to compile and build the model code.  You may have to configure the project's build path to accomodate the module-info.java's required modules.

## Executing the Example Models
Your IDE should be able to automatically compile and build the example model from the module information and source code you added to your project.  It should also automatically create the java command line to run/execute the model.  The command should include the dependencies module path i.e. the --module-path or -p option, and the main class to be "run", i.e. the --module or -m argument.  Then you can use the IDE's "run" feature to execute the model.

Alternately, you may want to execute the example models via the command line interface (CLI).  Each example model includes an ExecuteModel.bat file with the command, options, and arguments needed to execute/run the model.  You can enter the command line into the CLI or simply execute the ExecuteModel.bat file to execute the model.

### Execution Displays
Each example model outputs a series of log messages to the console during its execution.  In addition, the example models produce various "displays" of their parameters and/or behavior, e.g. textual representations of the state transitions, interaction sequences, timings, etc. as well as textual representations of graphical displays of data such as line charts, bar charts, etc.  The SysMLinJava API/MDK contains a series of apps that can receive this textual data from the executing models and display it as log messages.

Soon, a tool will be available (for a modest fee) that provides actual graphical displays for model execution.  This tool will provide real-time displays of state machines (as state transition tables), interactions (as sequence diagrams), and timings (as timing diagrams) during model execution, as well as displays of data as line charts, bar charts, HTML renders, scatter charts, and even animations of object in the executing model.  The toll will also have a capability to automate the execution of all of the example models, including the configuration and startup of the graphical displays.  The tool will also be able to provide browsable reports on the model's structure and behaviors.  Together, this will make model execution easy to perform, more readily analyzed, and more pleasant to visualize.

This README will be updated when the tool is available and will provide links to its purchase and download location as soon as it becomes available. (anticipated December, 2022)

## License and Dependencies
The example models are all licensed in accordance with the Apache license cited in the repository.  All models have a "requires" dependency on modules of the Java API (requried by default in you IDE project) and a "requires transitive" dependency on the SysMLinJava module (required via the module-info.java file and located in another IDE project).  Some of the model examples also "require" the SysMLinJavaLibrary module, which should also be located in another IDE project as necessary.

## Example Models
The example SysMLinJava models demonstrate a variety of model types and patterns.  Each is explained briefly as follows.

### AIControlledDBSSystem
The AIControlledDBSSystem model demonstrates how to build a SysMLinJava model for control and monitor systems that use specific types of control mechanisms, in this case control based on a trained artificial neural network (ANN).  It also demonstrates how to build a model that executes in multiple processes (JVMs).  The example model includes a number of jar files that are needed to build, train, and execute the ANN.  These dependencies are included in the model's "module-info" file and should be included on the java command line's "--module-path" for the applicable process.  A .bat file for each of the processes used to execute the model is included in the example model folder for possible use in executing the model.  Processes should be started in the following sequence.

 - Controller container
 - Sensor container
 - Patient container

### C4S2SystemsOfSystems
The C4S2SystemsOfSystems model demonstrates how to build a model for a "system-of-systems" where the systems include fixed and mobile systems that interface/communicate via unique protocols.  The example model executes as a single process with each system component operating asychronously in separate threads.

The model consists of a command/control/computer/communications (C4) system, and surveillance and strike systems (S2).  The surveillance system is a fixed location radar that surveils an area to find/target a recognizble vehicle, e.g. an enemy tank.  The strike system is a drone that, when commanded, flies to the target area and engages/strikes the target fixed by the radar.  It then assesses the target to confirm it was destroyed.  All of this is commanded/controlled by the C4 system which is, in turn, controlled and monitored by an operator.  In the case of this example model, the operator is simulated implying it can be a human or computer.

The C4S2SystemsOfSystems model generates a number of displays, including an animated display of the radar, drone, and tank.  The animation display shows the location and movements of the radar, drone, and tank, including actual engagement of the strike drone on the tank.  The animation is driven by a constraint block in the model that binds to the radar, drone, and tank geospatial locations, translating them into display data that is transmitted to the animation display.  The model available in the repository now lacks the images needed for an actual animation display, but, using the animation display objects in the SysMLinJava API/MDF, a textual representation of the animation can be displayed.  The tool described above will, when available, provide the complete graphical display of the animation.

As with the other example models, this model's dependencies are specified in the model's "module-info.java" file.  In addition to the standard Java API and the SysMLinJava module, it "requires" the SysMLinJavaLibrary module as well.  

### CableStayedBridge
The CableStayedBridge model demonstrates how to build a model of many static forces interacting to provide a structure, e.g a "cable-stayed" bridge typical of modern day suspension bridges.  The model uses the SysML full-port to transfer forces from one structural component to another.  As might be expected of a suspension bridge, numerous port connections are used by the model.  The model also includes a number of vehicles that move across the brige deck, transfering their loads to the deck in new locations as they travel.  These vehicle loads are then trasferred via ports to the deck which transfers loads to the cables, which transfer loads to the pylon which transfer loads to the ground.

The model includes a constraint block that binds constraint parameters to the loads on the bridge, translating/constraining the parameters into a dynamic bar chart display that shows the loads on the bridges cables as the vehicles move across the bridge deck.  Once again, a textual representation of the bar chart display can be produced by objects in the SysMLinJava API/MDF, but a tool will soon be available (for a modest fee) that can display a graphical bar chart of the cable loads.

This model's dependencies are specified in the model's "module-info.java" file.  In addition to the standard Java API, it "requires transitive" only the SysMLinJava module.  

### ConnectedTanks
The ConnectedTanks model demonstrates how to build a model of dynamic flows between "parts" of a block.  The model is a SysMLinJava implementation of the model by the same name described in "SysML Extension for Physical Interaction and Signal Flow Simulation", Object Management Group, Inc., 2018.  The SysMLinJava model is of a simple system of two tanks of fluid connected by a pipe. A minimal block structure consists of the two tanks and a pipe that connects the two.  The two tanks begin with different amounts of fluid in each with constraints used to specify the flows between the tanks via the connecting pipe.  The executable model changes the flow rates through the pipe and the amounts of fluid in the tanks until an equal pressure point is reached.


The model uses the proxy port to represent the pipe/tank interfaces.  It includes a constraint block that binds constraint parameters to the fluid levels of the tanks.  The constraint parameters are translated/constrained into values on a line chart for time-based display of the equalizing fluid levels.  Once again, a textual representation of the line chart display can be produced by objects in the SysMLinJava API/MDF, but a tool will soon be available (for a modest fee) that can display a graphical line chart of the tank levels.

This model's dependencies are specified in the model's "module-info.java" file.  In addition to the standard Java API, it "requires transitive" only the SysMLinJava module.  

### ElectricCircuit
The ElectricCircuit model demonstrates how to build a model of system "parts" whose values are constrained and change over time.  The model is a SysMLinJava model implementation of the model of the same name as described in "SysML Extension for Physical Interaction and Signal Flow Simulation", Object Management Group, Inc., 2018.  The SysMLinJava model is of a simple electrical circuit.  The circuit consists of an AC voltage source in parallel with an RC series circuit and a RL series circuit.

The model of the electric circuit uses of the SysML proxy port to model the interfaces between the electrical components.  It also uses the SysML constraint block to bind constraint parameters to the voltages and currents of the system components, i.e. the resistors, capacitor, and inductor.  These constraint parameters are translated/constrained into values on a series of line charts for time-based display of the voltages and currents.  Once again, a textual representation of the line chart displays can be produced by objects in the SysMLinJava API/MDF, but a tool will soon be available (for a modest fee) that can display the graphical version of the line charts of the voltages and currents.

This model's dependencies are specified in the model's "module-info.java" file.  In addition to the standard Java API, it "requires transitive" only the SysMLinJava module.  
 
### H2OStateMachine
The H2OStateMachine model demonstrates how to model a behavior (a continuous process) based on a SysML state machine.  The object of the behavior is water (H2O) and its transitions from ice to liquid to gas to decomposed states.  The H2OStateMachine is a SysMLinJava implementation of the SysML state machine for H2O found in the book "A Practical Guide to SysML - The Systems Modeling Language 3rd edition" by Sanford Friedenthal, et al; Object Management Group; Morgan Kaufman publisher; copyright 2015.

The model is almost exclusively an application of the SysMLinJava state machine.  It generates the data for a state transitions table and timing diagram.  A textual representation of the state transitions table and timing diagram can be produced by objects in the SysMLinJava API/MDF.  A tool will soon be available (for a modest fee) that can display the state transitions table and timing diagram in graphical form.

This model's dependencies are specified in the model's "module-info.java" file.  In addition to the standard Java API, it "requires transitive" only the SysMLinJava module.  

### HFDataLinkedSystem
The C4S2SystemsOfSystems model demonstrates how to build a model for a "system-of-systems" where the systems interfce/communicate via "protocol stacks".  The example model executes as a single process with each system component operating asychronously in separate threads.  Some of the communications protocols also operate asynchronously as well.

The model consists of a command/control/computer/communications (C4) 

### MotorInWheelEV
The MotorInWheelEV model demonstrates how to build a model of a system of mechanical, electrical, and structural components that is monitored and controlled by a human operator.  It is a SysMLinJava model of an electric vehicle that uses so-called "motor-in-wheel" technolog, i.e. each wheel is driven by its own electric motor.

The model of the EV uses the SysML full port for all its many interfaces between components and between the vehicle and the operator.  The operator and all components operate (execute) as finite state machines asynchronously (in their owh threads).  The model also uses a constraint block that binds constraint parameters to the electrical power values at each wheel.  The constraint parameters are then translated/constrained to power usage parameters over time.  These output parameters are sent to a line-chart display for a view of the power usage and efficiency versus distance traveled.  A textual representation of the line chart can be produced by objects in the SysMLinJava API/MDF.  A tool will soon be available (for a modest fee) that can display the line chart in graphical form. 

## TrafficSignalControlSystem
The TrafficSignalControlSystem model demonstrates how to model a system having sub-state machines for states in its state-machine.
## Conact for Comments, Questions, Requests for Training or Assistance
Send any comments, questions, or requests for training or assistance to sysmlinjava@earthlink.net.
