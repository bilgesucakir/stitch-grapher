# stitch-grapher

Java-based stitch connectivity graph engine for topology-aware crochet pattern parsing and modeling.

## Overview

Stitch-grapher is a Spring Boot application that processes crochet patterns in text format and visualizes them as connectivity graphs. It models crochet stitches as a hierarchical abstraction layer, enabling complex pattern analysis and visualization.

## Project Structure

### Core Packages

- **`parser`** - Pattern parsing pipeline
  - `PatternTokenizer.java` - Tokenizes raw input 
  - `PatternExpander.java` - Expands shorthand notations into full operation lists
  - `PatternParser.java` - Converts tokens into structured parsed model
  - `ParsedPattern.java`, `ParsedRow.java`, `ParsedOperation.java`
  - `OperationType.java` - Enum including stitch types and operations (`inc`, `dec`)
  - `CrochetMode.java` - Enum for FLAT and CIRCULAR modes

- **`validation`** - Pattern validation layer
  - `PatternValidator.java` - Validator interface
  - `FlatPatternValidator.java` - Allows unused stitches
  - `CircularPatternValidator.java` - Requires exact usage 
  - `ValidationException.java`

- **`topology`** - Graph construction 
  - `TopologyBuilder.java` - Interface
  - `FlatTopologyBuilder.java`
  - `CircularTopologyBuilder.java`

- **`service`** - Application layer orchestration
  - `PatternService.java`
  - `FlatPatternService.java`
  - `CircularPatternService.java`
  - Handles: parse → validate → topology

- **`mapper`** - DTO conversion
  - `StitchGraphMapper.java`

- **`graph`** - Graph data structures
  - `StitchGraph.java`
  - `StitchNode.java`
  - `Row.java`
  - `RowDirection.java`

- **`dto`** - API communication
  - `PatternInputDto.java`
  - `StitchGraphDto.java`
  - `GraphNodeDto.java`
  - `GraphEdgeDto.java`

- **`controller`** - REST API
  - `GraphController.java`
### Core Components

- **`StitchType.java`** - Enumeration defining supported crochet stitch types
- **`Stitch.java`** - Stitch implementation with properties and methods
- **`AbstractStitch.java`** - Base abstract class providing common stitch functionality

## Features

### Currently Implemented
- Parse text-based crochet patterns (
- Input validation for pattern syntax and operations
- Model stitch connectivity and topology
- Generate graph visualizations with row-aware positioning
- Support for multiple stitch types 
- Directional row support 
- REST API endpoint for graph generation

### To Be Implemented
- Pattern input validation for edge cases and complex patterns
- Increase operations (inc) - stitch multiplication
- Decrease operations (dec) - stitch reduction
- Better visualization of stitch connections and hierarchy for flat and circular patterns

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
4. Open your browser and navigate to `http://localhost:8080`

## API Usage

### Graph Generation Endpoint

**POST** `/api/graph`

Accepts a JSON payload with crochet pattern rows and returns a graph representation.

**Request Example:**
```json
{
  "rows": [
    "3sc"
  ], 
  "mode": "FLAT"
}
```

**Response Example:**
```json
{
  "nodes": [
    {
      "id": "5ebe4049-239a-4bba-b337-6648d3646009",
      "label": "SC",
      "row": 0,
      "position": 0,
      "direction": "LEFT_TO_RIGHT"
    },
    {
      "id": "604dbe0d-5dae-485f-bb1d-0c715395c801",
      "label": "SC",
      "row": 0,
      "position": 1,
      "direction": "LEFT_TO_RIGHT"
    },
    {
      "id": "3d04c832-a2f0-4a2b-8730-b21134b874b0",
      "label": "SC",
      "row": 0,
      "position": 2,
      "direction": "LEFT_TO_RIGHT"
    }
  ],
  "edges": [
    {
      "source": "5ebe4049-239a-4bba-b337-6648d3646009",
      "target": "604dbe0d-5dae-485f-bb1d-0c715395c801"
    },
    {
      "source": "604dbe0d-5dae-485f-bb1d-0c715395c801",
      "target": "3d04c832-a2f0-4a2b-8730-b21134b874b0"
    }
  ]
}
```

## Technologies

- **Spring Boot** - Web framework
- **Maven** - Build and dependency management
- **Java 17+** - Programming language

## License

MIT