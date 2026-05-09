# stitch-grapher

Java-based stitch connectivity graph engine for topology-aware crochet pattern parsing and modeling.

## Overview

Stitch-grapher is a Spring Boot application that processes crochet patterns in text format and visualizes them as connectivity graphs. It models crochet stitches as a hierarchical abstraction layer, enabling complex pattern analysis and visualization.

## Project Structure

### Core Components

- **`StitchType.java`** - Enumeration defining supported crochet stitch types
- **`Stitch.java`** - Concrete stitch implementation with properties and methods
- **`AbstractStitch.java`** - Base abstract class providing common stitch functionality and contracts

## Features

- Parse text-based crochet patterns (bracket notation)
- Model stitch connectivity and topology
- Generate graph visualizations
- Support for multiple stitch types

## Prerequisites

- Java 17+
- Maven 3.8+
- Spring Boot 3.x

## Getting Started

1. Clone the repository
2. Build the project: `mvn clean install`
3. Run the application: `mvn spring-boot:run`

## Technologies

- **Spring Boot** - Web framework
- **Maven** - Build and dependency management
- **Java 17+** - Programming language

## License

MIT