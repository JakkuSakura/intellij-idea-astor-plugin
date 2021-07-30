<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# IntelliJ Idea Astor Plugin ChangeLog

## [Unreleased]
## [0.1.8]
### Added
- Logging forawrding
- Multithreading
- Separate gRPC without direct dependency on astor
- Load result config

## [0.1.7]
### Added
- Text panel
- Unfinished stdout streaming

## [0.1.6]
### Changes
- use gRPC
- use the latest IDEA and java 11

## [0.1.5]
### Problems
- no javax.tools.ToolProvider.getSystemJavaCompiler in JRE
### Fixes
- java_home error from astor
### Added
- full arguments
## [0.1.4]
### Added
- Extract jreVersion
- Autodetect gradle/maven and fill in arguments for astor
- Dependencies on core plugins

## [0.1.3]
### Fixed
- Chose a proper set of IDEA and JDK
- Download proper source in download_idea.sh

### Added
- Read basic config from IDEA

## [0.1.2]
### Fixed
- GitHub action
### Added
- Added Astor menu item

## [0.1.1]
### Fixed
- pluginUntilBuild does not include 2021.1.2

## [0.1.0]
### Added
- Initialize project


