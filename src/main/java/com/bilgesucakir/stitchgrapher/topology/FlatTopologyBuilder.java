package com.bilgesucakir.stitchgrapher.topology;

import com.bilgesucakir.stitchgrapher.graph.Row;
import com.bilgesucakir.stitchgrapher.graph.RowDirection;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.graph.StitchNode;
import com.bilgesucakir.stitchgrapher.parser.ParsedOperation;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.ParsedRow;
import com.bilgesucakir.stitchgrapher.stitch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.bilgesucakir.stitchgrapher.graph.RowDirection.LEFT_TO_RIGHT;
import static com.bilgesucakir.stitchgrapher.graph.RowDirection.RIGHT_TO_LEFT;

/**
 * Builds a stitch graph for circular patterns where rows are arranged concentrically.
 * Connections are created such that each row links appropriately to the previous one in
 * a circular fashion.
 */
@Component
public class FlatTopologyBuilder implements TopologyBuilder {

    private final StitchFactory stitchFactory;

    @Autowired
    public FlatTopologyBuilder(StitchFactory stitchFactory) {
        this.stitchFactory = stitchFactory;
    }

    @Override
    public StitchGraph build(ParsedPattern pattern) {

        StitchGraph graph = new StitchGraph();
        List<Row> builtRows = new ArrayList<>();

        for (ParsedRow parsedRow : pattern.rows()) {

            RowDirection direction = parsedRow.index() % 2 == 0 ? LEFT_TO_RIGHT : RIGHT_TO_LEFT;

            Row row = new Row(parsedRow.index(), direction);
            List<StitchNode> rowNodes = new ArrayList<>();

            // STEP 1: create nodes (respect RO)
            for (ParsedOperation operation : parsedRow.operations()) {
                int produced = operation.type().getProducedOutput();

                for (int i = 0; i < produced; i++) {
                    StitchNode node = new StitchNode(stitchFactory.createForOutput(operation.type()));
                    row.addStitch(node);
                    rowNodes.add(node);
                }
            }

            // STEP 2: connect next within row
            for (int i = 0; i < rowNodes.size() - 1; i++) {
                rowNodes.get(i).connectNext(rowNodes.get(i + 1));
            }

            // STEP 3: connect parents (REVERSE cursor logic)
            if (!builtRows.isEmpty()) {

                Row previousRow = builtRows.get(builtRows.size() - 1);
                List<StitchNode> previousNodes = previousRow.getStitches();

                int cursor = previousNodes.size();
                int currentIndex = 0;

                for (ParsedOperation operation : parsedRow.operations()) {

                    int required = operation.type().getRequiredInput();
                    int produced = operation.type().getProducedOutput();

                    int start = cursor - required;
                    int end = cursor;

                    List<StitchNode> parents = previousNodes.subList(start, end);

                    for (int j = 0; j < produced; j++) {
                        StitchNode child = rowNodes.get(currentIndex++);

                        for (StitchNode parent : parents) {
                            parent.addChild(child);
                        }
                    }

                    cursor -= required;
                }
            }

            builtRows.add(row);
            graph.addRow(row);
        }
        return graph;
    }
}