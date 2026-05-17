package com.bilgesucakir.stitchgrapher.topology;

import com.bilgesucakir.stitchgrapher.graph.Row;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.graph.StitchNode;
import com.bilgesucakir.stitchgrapher.parser.OperationType;
import com.bilgesucakir.stitchgrapher.parser.ParsedOperation;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.ParsedRow;
import com.bilgesucakir.stitchgrapher.stitch.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.bilgesucakir.stitchgrapher.graph.RowDirection.LEFT_TO_RIGHT;

/**
 * Builds a stitch graph for circular patterns where rows are arranged concentrically.
 * Connections are created such that each row links appropriately to the previous one in
 * a circular fashion.
 */
@Component
public class CircularTopologyBuilder implements  TopologyBuilder {


    @Override
    public StitchGraph build(ParsedPattern pattern) {
        StitchGraph graph = new StitchGraph();
        List<Row> builtRows = new ArrayList<>();

        for (ParsedRow parsedRow : pattern.rows()) {
            Row row = new Row(parsedRow.index(), LEFT_TO_RIGHT);

            List<StitchNode> rowNodes = new ArrayList<>();
            for (ParsedOperation operation : parsedRow.operations()) {
                StitchNode node = new StitchNode(createStitch(operation.type()));
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
