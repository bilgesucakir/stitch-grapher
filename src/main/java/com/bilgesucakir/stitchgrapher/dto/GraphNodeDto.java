package com.bilgesucakir.stitchgrapher.dto;

/**
 * Data Transfer Object representing a node in the stitch graph.
 *
 * @param id        The unique identifier of the node.
 * @param label     The label or name of the node.
 * @param row       The row number where the node is located in the graph.
 * @param position  The position of the node within its row.
 * @param direction The direction of the row (e.g., "LEFT_TO_RIGHT" or "RIGHT_TO_LEFT").
 */
public record GraphNodeDto(String id, String label, int row, int position, String direction) {
}