# SysMLinJava Model Examples
### Examples of using SysMLinJava for high precision MBSE

SysMLinJava is a Java-based model development kit (MDK) for high-precision modeling of executable SysML models in the Java programming language.  This repository contains nine example models that are based on SysMLinJava.   Each example can be downloaded and imported into a IDE as a project.  The SysMLinJava module, on which the example model will depend, should be in the IDE as another java project.  The examples are fully tested and can be compiled, built, and executed within the scope of the IDE project.

## Installing the Example Models
The easiest way to install the example models is to download the full set of example models as a zip file from this repository and extract the model folders to a folder on your computer.  Then use your IDE to create a java project for each of the example models you view, build, and/or execute and import or copy the code for the example models into their java projects.  Make sure you include the "module-info.java" file for each model in its project so that the IDE can know how to compile and build the model code.

## Executing the Example Models
Your IDE should be able to automatically compile and build the example model from the module information and source code you added to your project.  It should also automatically create the java command line to run/execute the model.  The command should include the dependencies module path i.e. the --module-path or -p option, and the main class to be "run", i.e. the --module or -m argument.  Then you can use the IDE's "run" feature to execute the model.

### Executaion Displays
Each example model outputs a series of log messages to the console during its execution.  In addition, the example models produce various "displays" of their parameters and/or behavior, e.g. textual representations of the state transitions, interaction sequences, timings, etc. as well as textual representations of graphical displays of data such as line charts, bar charts, etc.  The SysMLinJava API/MDK contains a series of apps that can receive this textual data from the executing models and display it as log messages.

Soon, a tool will be available (for a modest fee) that provides actual graphical displays for model execution.  This tool will provide real-time displays of state machines (as state transition tables), interactions (as sequence diagrams), and timings (as timing diagrams) during model execution, as well as displays of data as line charts, bar charts, HTML renders, scatter charts, and even animations of object in the executing model.  The toll will also have a capability to automate the execution of all of the example models, including the configuration and startup of the graphical displays.  The tool will also be able to provide browsable reports on the model's structure and behaviors.  Together, this will make model execution easy to perform, more readily analyzed, and more pleasant to visualize.

This README will be updated when the tool is available and will provide links to its purchase and download location as soon as it becomes available. (anticipated December, 2022)

## License and Dependencies
The example models are all licensed in accordance with the Apache license cited in the repository.  All models have a "requires" dependency on modules of the Java API (requried by default in you IDE project) and a "requires transitive" dependency on the SysMLinJava module (required via the module-info.java file and located in another IDE project).  Some of the model examples also "require" the SysMLinJavaLibrary module, which should also be located in another IDE project as necessary.

## Example Models
The example SysMLinJava models demonstrate a variety of model types and patterns.  Each is explained briefly as follows.

### AIControlledDBSSystem
The AIControlledDBSSystem model demonstrates how to build a SysMLinJava model for control and monitor systems that use specific types of control mechanisms, in this case control based on a trained artificial neural network (ANN).  It also demonstrates how to build a model that executes in multiple processes (JVMs).  The example model includes a number of jar files that are needed to build, train, and execute the ANN.  These dependencies are included in the model's "module-info" file and should be included on the java command line's "--module-path" for the applicable process.  A .bat file for each of the processes used to execute the model is included in the example model folder for possible use in executing the model.  Processes should be started in the following sequence.

-Controller container
-Sensor container
-Patient container

### C4S2SystemsOfSystems
The C4S2SystemsOfSystems model demonstrates how to build a model for a "system-of-systems" where the systems include fixed and mobile systems that interface/communicate via unique protocols.  The example model executes as a single process with each system component operating asychronously in separate threads.

The model consists of a command/control/computer/communications (C4) system, and surveillance and strike systems (S2).  The surveillance system is a fixed location radar that surveils an area to find/target a recognizble vehicle, e.g. an enemy tank.  The strike system is a drone that, when commanded, flies to the target area and engages/strikes the target fixed by the radar.  It then assesses the target to confirm it was destroyed.  All of this is commanded/controlled by the C4 system which is, in turn, controlled and monitored by an operator.  In the case of this example model, the operator is simulated implying it can be a human or computer.

The C4S2SystemsOfSystems model generates a number of displays, including an animated display of the radar, drone, and tank.  The animation display shows the location and movements of the radar, drone, and tank, including actual engagement of the strike drone on the tank.  The animation is driven by a constraint block in the model that binds to the radar, drone, and tank geospatial locations, translating them into display data that is transmitted to the animation display.  The model available in the repository now lacks the images needed for an actual animation display, but, using the animation display objects in the SysMLinJava API/MDF, a textual representation of the animation can be displayed.  The tool described above will, when available, provide the complete graphical display of the animation.

As with the other example models, this model's dependencies are specified in the model's "module-info.java" file.  In addition to the standard Java API and the SysMLinJava module, it depends on the SysMLinJavaLibrary module as well.  

### CableStayedBridge
The CableStayedBridge model demonstrates how to build a model of many static forces interacting to provide a structure, e.g a "cable-stayed" bridge typical of modern day suspension bridges.  The model uses the SysML full-port to transfer forces from one structural component to another.  As might be expected of a suspension bridge, numerous port connections are used by the model.  The model also includes a number of vehicles that move across the brige deck, transfering their loads to the deck in new locations as they travel.  These vehicle loads are then trasferred via ports to the deck which transfers loads to the cables, which transfer loads to the pylon which transfer loads to the ground.

The model includes a constraint block that binds constraint parameters to the loads on the bridge, translating/constraining the parameters into a dynamic bar chart display that shows the loads on the bridges cables as the vehicles move across the bridge deck.  Once again, a textual representation of the bar chart display can be produced by objects in the SysMLinJava API/MDF, but a tool will soon be available (for a modest fee) that can display a graphical bar chart of the cable loads.

This model dependencies are specified in the model's "module-info.java" file.  In addition to the standard Java API, it depends only on the SysMLinJava module.  

### ConnectedTanks

### ElectricCircuit

### H2OStateMachine

### HFDataLinkedSystem
The C4S2SystemsOfSystems model demonstrates how to build a model for a "system-of-systems" where the systems interfce/communicate via "protocol stacks".  The example model executes as a single process with each system component operating asychronously in separate threads.  Some of the communications protocols also operate asynchronously as well.

The model consists of a command/control/computer/communications (C4) 

### MotorInWheelEV

### TrafficSignalControlSystem

## Comments, Questions, Training, Assistance
