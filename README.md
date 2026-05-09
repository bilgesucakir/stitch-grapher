# stitch-grapher

Java-based stitch connectivity graph engine for topology-aware crochet pattern parsing and modeling.

## Overview

Stitch-grapher is a Spring Boot application that processes crochet patterns in text format and visualizes them as connectivity graphs. It models crochet stitches as a hierarchical abstraction layer, enabling complex pattern analysis and visualization.

## Project Structure

### Core Packages

- **`stitch`** - Stitch type definitions and implementations
  - `AbstractStitch.java` - Base abstract class for all stitches
  - `Stitch.java` - Stitch interface contract
  - `StitchType.java` - Enumeration of supported stitch types
  - Concrete implementations: `SingleCrochet`, `DoubleCrochet`, `HalfDoubleCrochet`, `TrebleCrochet`, `SlipStitch`, etc.

- **`parser`** - Pattern parsing and validation
  - `PatternParser.java` - Main parser for text-based crochet patterns
  - `ParsedPattern.java` - Parsed pattern object containing rows
  - `ParsedRow.java` - Individual row representation
  - `ParsedOperation.java` - Single operation (stitch or modifier)
  - `OperationType.java` - Enum for operation types (STITCH, INCREASE, DECREASE)
  - `ParseException.java` - Custom exception for parsing errors

- **`graph`** - Graph data structures and topology
  - `StitchGraph.java` - Main graph representation
  - `StitchNode.java` - Individual node in the graph
  - `Row.java` - Row container with positioning info
  - `RowDirection.java` - Enum for row direction (LEFT_TO_RIGHT, RIGHT_TO_LEFT)

- **`topology`** - Graph construction and connectivity
  - `TopologyBuilder.java` - Builds connectivity graph from parsed patterns

- **`dto`** - Data Transfer Objects for API communication
  - `PatternInputDto.java` - Request DTO for pattern input
  - `StitchGraphDto.java` - Response DTO for graph visualization
  - `GraphNodeDto.java` - Node representation in API response
  - `GraphEdgeDto.java` - Edge representation in API response

- **`controller`** - REST API endpoints
  - `GraphController.java` - REST endpoints for graph generation

### Core Components

- **`StitchType.java`** - Enumeration defining supported crochet stitch types
- **`Stitch.java`** - Stitch implementation with properties and methods
- **`AbstractStitch.java`** - Base abstract class providing common stitch functionality

## Features

### Currently Implemented
- ✅ Parse text-based crochet patterns (bracket notation)
- ✅ Input validation for pattern syntax and operations
- ✅ Model stitch connectivity and topology
- ✅ Generate graph visualizations with row-aware positioning
- ✅ Support for multiple stitch types (Single Crochet, Double Crochet, Half Double Crochet, etc.)
- ✅ Directional row support (left-to-right and right-to-left)
- ✅ REST API endpoint for graph generation

### To Be Implemented
- ⏳ Pattern input valication for edge cases and complex patterns
- ⏳ Increase operations (inc) - stitch multiplication
- ⏳ Decrease operations (dec) - stitch reduction

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
    "3sc",
    "sc, sc, sc",
    "2sc"
  ]
}
```

**Response Example:**
```json
{
  "nodes": [
    {"id": "node-0-0", "label": "sc", "row": 0, "position": 0, "direction": "LEFT_TO_RIGHT"},
    {"id": "node-0-1", "label": "sc", "row": 0, "position": 1, "direction": "LEFT_TO_RIGHT"},
    {"id": "node-0-2", "label": "sc", "row": 0, "position": 2, "direction": "LEFT_TO_RIGHT"}
  ],
  "edges": [
    {"source": "node-0-0", "target": "node-1-0"},
    {"source": "node-0-1", "target": "node-1-1"},
    {"source": "node-0-2", "target": "node-1-2"}
  ]
}
```

## Technologies

- **Spring Boot** - Web framework
- **Maven** - Build and dependency management
- **Java 17+** - Programming language

## License

MIT