package com.bilgesucakir.stitchgrapher.controller;

import com.bilgesucakir.stitchgrapher.dto.GraphEdgeDto;
import com.bilgesucakir.stitchgrapher.dto.GraphNodeDto;
import com.bilgesucakir.stitchgrapher.dto.PatternInputDto;
import com.bilgesucakir.stitchgrapher.dto.StitchGraphDto;
import com.bilgesucakir.stitchgrapher.graph.RowDirection;
import com.bilgesucakir.stitchgrapher.parser.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling graph-related API endpoints.
 */
@RestController
public class GraphController {

    @PostMapping("/api/graph")
    public StitchGraphDto generateGraph(@RequestBody PatternInputDto input) {

        PatternParser parser = new PatternParser();
        ParsedPattern pattern = parser.parse(input.rows());
        List<GraphNodeDto> nodes = new ArrayList<>();
        List<GraphEdgeDto> edges = new ArrayList<>();

        int nodeCounter = 0;
        for (ParsedRow row : pattern.getRows()) {

            RowDirection direction = row.getIndex() % 2 == 0
                    ? RowDirection.LEFT_TO_RIGHT
                    : RowDirection.RIGHT_TO_LEFT;

            List<String> rowNodeIds = new ArrayList<>();
            for (int i = 0; i < row.getOperations().size(); i++) {
                ParsedOperation operation = row.getOperations().get(i);
                String nodeId = "n" + nodeCounter++;
                rowNodeIds.add(nodeId);

                nodes.add(new GraphNodeDto(
                        nodeId,
                        operation.getType().name(),
                        row.getIndex(),
                        i,
                        direction.name()));
            }

            for (int i = 0; i < rowNodeIds.size() - 1; i++) {
                String source = rowNodeIds.get(i);
                String target = rowNodeIds.get(i + 1);

                edges.add(new GraphEdgeDto(source, target));
            }
        }

        return new StitchGraphDto(nodes, edges);
    }
}
