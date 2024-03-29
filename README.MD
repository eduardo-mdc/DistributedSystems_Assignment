
# Distributed Systems Practical Assignment

## Overview

This project consists of three practical assignments for the Distributed Systems course, focusing on implementing application prototypes using Java. The three scenarios include Mutual Exclusion with the Token Ring Algorithm, Data Dissemination using the Anti-Entropy Algorithm, and a Basic Chat Application using Totally Ordered Multicast.

## Prerequisites

- Java: Ensure Java JDK 11 or higher is installed on your Linux system.
- Gradle: This project uses Gradle as a build tool. Install Gradle version 7.0 or higher.


## Installation

### 1. Install Java JDK:

Update the package index: 
```bash
sudo apt update
```
Install Java JDK (can be a newer version):
```bash
sudo apt install openjdk-11-jdk
```
Verify the installation:
```bash
java -version
```

## Running the Project

### Build

Run Gradle Build: Execute the following command, in the root of the project, to build the project and download all necessary dependencies from public repositories:
```bash
./gradlew build
```

**Optional** : To remove previous build, run the following command.
```bash
./gradlew clean
```

### Execution

#### 1.  Mutual Exclusion with the Token Ring Algorithm

In the root of the project run the following command.
```bash
./gradlew runRingGenrator
```
#### 2.  Data Dissemination Using the Anti-Entropy Algorithm

In the root of the project run the following command.
```bash
./gradlew runAntiEntropyGenerator
```
#### 3.  Basic Chat Application Using Totally Ordered Multicast

In the root of the project run the following command.
```bash
./gradlew runChatGenerator
```