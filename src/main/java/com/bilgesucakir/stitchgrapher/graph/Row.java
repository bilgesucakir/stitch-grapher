package com.bilgesucakir.stitchgrapher.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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