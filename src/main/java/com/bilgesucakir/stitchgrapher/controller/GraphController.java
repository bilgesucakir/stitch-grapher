package com.bilgesucakir.stitchgrapher.controller;

import com.bilgesucakir.stitchgrapher.dto.GraphEdgeDto;
import com.bilgesucakir.stitchgrapher.dto.GraphNodeDto;
import com.bilgesucakir.stitchgrapher.dto.PatternInputDto;
import com.bilgesucakir.stitchgrapher.dto.StitchGraphDto;
import com.bilgesucakir.stitchgrapher.graph.*;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.topology.TopologyBuilder;
import com.bilgesucakir.stitchgrapher.parser.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling graph-related API endpoints.
 * Temporarily, this controller contains the logic for generating a stitch graph from a parsed pattern.
 * In the future, this logic should be moved to a dedicated service class to keep the controller focused on handling HTTP requests and responses.
 */
@RestController
public class GraphController {

    /**
     * Endpoint for generating a stitch graph from the input pattern.
     *
     * @param input the input pattern received as a {@link PatternInputDto}; contains a list of rows and the crochet mode
     * @return a {@link StitchGraphDto} containing the nodes and edges of the generated stitch graph
     */
    @PostMapping("/api/graph")
    public StitchGraphDto generateGraph(@RequestBody PatternInputDto input) {

        // Parse the input pattern
        PatternParser parser = new PatternParser();
        ParsedPattern pattern = parser.parse(input.rows());
        CrochetMode mode = input.mode();

        // Build the stitch graph from the parsed pattern
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        StitchGraph graph = topologyBuilder.build(pattern, mode);

        List<GraphNodeDto> nodes = new ArrayList<>();
        List<GraphEdgeDto> edges = new ArrayList<>();

        // Convert the stitch graph to DTOs for the response
        for (Row row : graph.getRows()) {
            for (int i = 0; i < row.getStitches().size(); i++) {
                StitchNode node = row.getStitches().get(i);

                nodes.add(new GraphNodeDto(
                        node.getId().toString(),
                        node.getStitch().getType().name(),
                        row.getIndex(),
                        i,
                        row.getDirection().name())
                );
            }
        }

        // Create edges based on the connections between nodes
        for (StitchNode node : graph.getNodes()) {
            if (node.getNext() != null) {
                String source = node.getId().toString();
                String target = node.getNext().getId().toString();

                Row row = findRowOfNode(graph, node);
                edges.add(new GraphEdgeDto(source, target));
            }

            for (StitchNode child : node.getChildren()) {
                edges.add(new GraphEdgeDto(
                        node.getId().toString(),
                        child.getId().toString())
                );
            }
        }

        return new StitchGraphDto(nodes, edges);
    }

    private Row findRowOfNode(StitchGraph graph, StitchNode node) {
        for (Row row : graph.getRows()) {
            if (row.getStitches().contains(node)) {
                return row;
            }
        }
        throw new IllegalArgumentException("Node not found in any row");
    }
}
