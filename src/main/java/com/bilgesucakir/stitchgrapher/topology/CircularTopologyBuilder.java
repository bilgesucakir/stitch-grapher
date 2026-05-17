package com.bilgesucakir.stitchgrapher.topology;

import com.bilgesucakir.stitchgrapher.graph.Row;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.graph.StitchNode;
import com.bilgesucakir.stitchgrapher.parser.ParsedOperation;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.ParsedRow;
import com.bilgesucakir.stitchgrapher.stitch.StitchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.bilgesucakir.stitchgrapher.graph.RowDirection.LEFT_TO_RIGHT;

/**
 * Builds a stitch graph for circular patterns where rows are arranged concentrically.
 * Supports increases, decreases, stitches like single, double, etc. and chain stitches.
 */
@Component
public class CircularTopologyBuilder implements TopologyBuilder {

    private final StitchFactory stitchFactory;

    @Autowired
    public CircularTopologyBuilder(StitchFactory stitchFactory) {
        this.stitchFactory = stitchFactory;
    }

    @Override
    public StitchGraph build(ParsedPattern pattern) {
        StitchGraph graph = new StitchGraph();
        List<Row> builtRows = new ArrayList<>();

        for (ParsedRow parsedRow : pattern.rows()) {
            Row row = new Row(parsedRow.index(), LEFT_TO_RIGHT);
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

            if (!builtRows.isEmpty()) {
                Row previousRow = builtRows.get(builtRows.size() - 1);
                List<StitchNode> previousNodes = previousRow.getStitches();

                // circular continuity (last → first)
                previousNodes.get(previousNodes.size() - 1).connectNext(rowNodes.get(0));

                int cursor = 0;
                int currentIndex = 0;

                for (ParsedOperation operation : parsedRow.operations()) {

                    int required = operation.type().getRequiredInput();
                    int produced = operation.type().getProducedOutput();

                    List<StitchNode> parents = List.of();

                    // Only assign parents if something is consumed
                    if (required > 0) {
                        parents = previousNodes.subList(cursor, cursor + required);
                    }

                    for (int j = 0; j < produced; j++) {
                        StitchNode child = rowNodes.get(currentIndex++);

                        for (StitchNode parent : parents) {
                            parent.addChild(child);
                        }
                    }

                    // Only move cursor if something was consumed
                    if (required > 0) {
                        cursor += required;
                    }
                }
            }
            builtRows.add(row);
            graph.addRow(row);
        }
        return graph;
    }
}