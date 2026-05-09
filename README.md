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

- Parse text-based crochet patterns
- Model stitch connectivity and topology
- Generate graph visualizations
- Support for multiple stitch types

## A Basic Example

- **Input Pattern**: `3sc, inc sc inc, 2dec sc`
- **Output**: Sample graph visualization showing stitch connections and hierarchy

![img.png](img.png)
- First row produces 3 stitches. Second row has 2 increase operations using row 1 stitches (1x2 + 1 + 1x2), resulting in 5 stitches. Row 3 has 2 decrease operations using row 2 stitches ((1+1)/2 + (1+1)/2 + 1), resulting in 3 stitches.
- So if you have an increase operation, you should have 2 child nodes in the next step. 
- If you have a decrease operation, you should have 2 parent nodes in the previous step.
- If you have a normal stitch, you should have 1 parent node in the previous step and 1 child node in the next step.

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