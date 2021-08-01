# IntelliJ Idea Astor Plugin


<!-- Plugin description -->
**IntelliJ Idea Astor Plugin** is a plugin that integrates `Astor` in Intellij Idea. It communicates with a local Astor backend with gRPC framework.

<!-- Plugin description end -->


## What does this plugin do?
This plugin creates a set of `Action`s, i.e. items in main menu, and a tool window that shows the analysis progress.
While clicking the `Execute Astor` action, it communicates with the backend Astor with gRPC with arguments generated from project settings. 
The logging and stdout are forwarded to the tool window so you can see the real time progress. Then the astor output are analyzed, cleansed and displayed as a Diff window

## Compatibility

### Supported Build system
It supports maven and gradle out of the box, though only maven is slightly tested.

### JDK version
Astor backend should be run with JDK 1.8, this plugin requires JRE 11 to run on Intellij Idea 2021.2.
The code to be analyzed should be targeting Java 1.8


### TODO
- Boots Astor backend ad hoc with jdk 1.8
- Finer controls over which algorithm to use
- Speed up analysis progress
