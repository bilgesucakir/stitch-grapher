package com.bilgesucakir.stitchgrapher.topology;

import com.bilgesucakir.stitchgrapher.graph.Row;
import com.bilgesucakir.stitchgrapher.graph.RowDirection;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.graph.StitchNode;
import com.bilgesucakir.stitchgrapher.parser.*;
import com.bilgesucakir.stitchgrapher.stitch.*;

import java.util.ArrayList;
import java.util.List;

import static com.bilgesucakir.stitchgrapher.graph.RowDirection.LEFT_TO_RIGHT;
import static com.bilgesucakir.stitchgrapher.graph.RowDirection.RIGHT_TO_LEFT;
import static com.bilgesucakir.stitchgrapher.parser.CrochetMode.CIRCULAR;
import static com.bilgesucakir.stitchgrapher.parser.CrochetMode.FLAT;

//TODO: increase and decrease operations
public class TopologyBuilder {

    public StitchGraph build(ParsedPattern pattern, CrochetMode mode) {

        //temp
        System.out.println("Building topology for pattern with " + pattern.getRows().size() + " rows in " + mode + " mode.");

        StitchGraph graph = new StitchGraph();
        switch (mode)
        {
            case FLAT -> buildFlat(pattern, graph);
            case CIRCULAR -> buildCircular(pattern, graph);
        }

        return graph;
    }

    private void buildFlat(ParsedPattern pattern, StitchGraph graph) {

        List<Row> builtRows = new ArrayList<>();
        for (ParsedRow parsedRow : pattern.getRows()) {
            RowDirection direction = parsedRow.getIndex() % 2 == 0 ? LEFT_TO_RIGHT : RIGHT_TO_LEFT;
            Row row = new Row(parsedRow.getIndex(), direction);

            List<StitchNode> rowNodes = new ArrayList<>();
            for (ParsedOperation operation : parsedRow.getOperations()) {
                StitchNode node = new StitchNode(createStitch(operation.getType()));
                row.addStitch(node);
                rowNodes.add(node);
            }

            for (int i = 0; i < rowNodes.size() - 1; i++) {
                rowNodes.get(i).connectNext(rowNodes.get(i + 1));
            }

            if (!builtRows.isEmpty()) {
                Row previousRow = builtRows.get(builtRows.size() - 1);
                List<StitchNode> previousNodes = previousRow.getStitches();

                //TODO: current row's stitch requirement validator needed
                for (int i = 0; i < rowNodes.size(); i++) {

                    StitchNode parent = previousNodes.get(previousNodes.size() - 1 - i);
                    parent.addChild(rowNodes.get(i));
                }
            }

            builtRows.add(row);
            graph.addRow(row);
        }
    }


    //TODO: dangling not allowed except very last stitch
    //TODO: current row's stitch requirement validator needed, increase and decrease operations should be considered here
    private void buildCircular(ParsedPattern pattern, StitchGraph graph) {

        List<Row> builtRows = new ArrayList<>();

        for (ParsedRow parsedRow : pattern.getRows()) {
            Row row = new Row(parsedRow.getIndex(), LEFT_TO_RIGHT);

            List<StitchNode> rowNodes = new ArrayList<>();
            for (ParsedOperation operation : parsedRow.getOperations()) {
                StitchNode node = new StitchNode(createStitch(operation.getType()));
                row.addStitch(node);
                rowNodes.add(node);
            }

            for (int i = 0; i < rowNodes.size() - 1; i++) {
                rowNodes.get(i).connectNext(rowNodes.get(i + 1));
            }

            if (!builtRows.isEmpty()) {
                Row previousRow = builtRows.get(builtRows.size() - 1);
                List<StitchNode> previousNodes = previousRow.getStitches();

                // Connect the last stitch of the previous row to the first stitch of the current row
                previousNodes.get(previousNodes.size() - 1).connectNext(rowNodes.get(0));

                // Connect remaining stitches in a circular manner
                for (int i = 0; i < rowNodes.size(); i++) {

                    //i smaller than prev node size, add a child the current row's ith element
                    if (i < previousNodes.size()) {
                        StitchNode parent = previousNodes.get(i);
                        parent.addChild(rowNodes.get(i));
                    }
                }
            }

            builtRows.add(row);
            graph.addRow(row);
        }
    }

    private Stitch createStitch(OperationType type) {
        return switch (type) {
            case SC -> new SingleCrochet();
            case HDC -> new HalfDoubleCrochet();
            case DC -> new DoubleCrochet();
            case HTR -> new HalfTrebleCrochet();
            case TR -> new TrebleCrochet();
            case SLST -> new SlipStitch();
            default -> throw new IllegalArgumentException("Unsupported operation: " + type);
        };
    }
}