package com.bilgesucakir.stitchgrapher.dto;

/**
 * Data Transfer Object representing an edge in the stitch graph.
 *
 * @param source The ID of the source node.
 * @param target The ID of the target node.
 */
public record GraphEdgeDto(String source, String target) {

}