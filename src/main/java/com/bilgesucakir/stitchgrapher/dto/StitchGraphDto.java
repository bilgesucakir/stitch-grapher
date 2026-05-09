package com.bilgesucakir.stitchgrapher.dto;

import java.util.List;

/**
 * DTO for representing the stitch graph to be sent to the client.
 * It contains a list of nodes and a list of edges, where each node represents a stitch and each edge represents a connection between stitches.
 */
public record StitchGraphDto(List<GraphNodeDto> nodes, List<GraphEdgeDto> edges) {
}
