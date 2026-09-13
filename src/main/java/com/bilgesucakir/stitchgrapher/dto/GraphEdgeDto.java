package com.bilgesucakir.stitchgrapher.dto;

/**
 * Data Transfer Object representing an edge in the stitch graph.
 *
 * @param source The ID of the source node.
 * @param target The ID of the target node.
 * @param type   "NEXT" for the sequential stitch-working order (can cross rows in
 *               circular mode, at a round's closing stitch), "PARENT" for a true
 *               structural relationship - the stitch(es) the target was worked into.
 */
public record GraphEdgeDto(String source, String target, String type) {

}