package com.bilgesucakir.stitchgrapher.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StitchGraph {

    private final List<StitchNode> nodes;
    private final List<Row> rows;

    public StitchGraph() {
        this.nodes = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public void addNode(StitchNode node) {
        nodes.add(node);
    }

    public void addRow(Row row) {
        rows.add(row);
        nodes.addAll(row.getStitches());
    }

}