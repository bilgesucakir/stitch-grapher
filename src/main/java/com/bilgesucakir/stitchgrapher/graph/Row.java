package com.bilgesucakir.stitchgrapher.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a row of stitches in the stitch graph.
 * Each row has an index, a direction (right or left),
 * and a list of stitch nodes that belong to that row.
 */
@Getter
public class Row {

    private final int index;
    private final RowDirection direction;
    private final List<StitchNode> stitches;

    public Row(int index, RowDirection direction) {
        this.index = index;
        this.direction = direction;
        this.stitches = new ArrayList<>();
    }

    public void addStitch(StitchNode stitch) {
        stitches.add(stitch);
    }

}