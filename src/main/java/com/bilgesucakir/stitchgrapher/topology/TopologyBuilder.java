package com.bilgesucakir.stitchgrapher.topology;

import com.bilgesucakir.stitchgrapher.graph.Row;
import com.bilgesucakir.stitchgrapher.graph.RowDirection;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.graph.StitchNode;
import com.bilgesucakir.stitchgrapher.parser.OperationType;
import com.bilgesucakir.stitchgrapher.parser.ParsedOperation;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.ParsedRow;
import com.bilgesucakir.stitchgrapher.stitch.*;

import java.util.ArrayList;
import java.util.List;

import static com.bilgesucakir.stitchgrapher.graph.RowDirection.LEFT_TO_RIGHT;
import static com.bilgesucakir.stitchgrapher.graph.RowDirection.RIGHT_TO_LEFT;

//TODO: increase and decrease operations
public class TopologyBuilder {

    public StitchGraph build(ParsedPattern pattern) {
        StitchGraph graph = new StitchGraph();
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

        return graph;
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