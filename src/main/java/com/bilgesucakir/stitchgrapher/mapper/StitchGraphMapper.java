package com.bilgesucakir.stitchgrapher.mapper;

import com.bilgesucakir.stitchgrapher.dto.GraphEdgeDto;
import com.bilgesucakir.stitchgrapher.dto.GraphNodeDto;
import com.bilgesucakir.stitchgrapher.dto.StitchGraphDto;
import com.bilgesucakir.stitchgrapher.graph.Row;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.graph.StitchNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps internal stitch graph model objects to DTOs used by the web API/UI.
 */
@Component
public class StitchGraphMapper {

    public StitchGraphDto toDto(StitchGraph graph) {
        List<GraphNodeDto> nodes = new ArrayList<>();
        List<GraphEdgeDto> edges = new ArrayList<>();

        // Convert graph nodes
        for (Row row : graph.getRows()) {
            List<StitchNode> stitches = row.getStitches();
            for (int i = 0; i < stitches.size(); i++) {
                StitchNode node = stitches.get(i);
                nodes.add(new GraphNodeDto(
                        node.getId().toString(),
                        node.getStitch().getType().name(),
                        row.getIndex(),
                        i,
                        row.getDirection().name()
                ));
            }
        }

        // Convert graph edges
        for (StitchNode node : graph.getNodes()) {
            if (node.getNext() != null) {
                edges.add(new GraphEdgeDto(
                        node.getId().toString(),
                        node.getNext().getId().toString()
                ));
            }
            for (StitchNode child : node.getChildren()) {
                edges.add(new GraphEdgeDto(
                        node.getId().toString(),
                        child.getId().toString()
                ));
            }
        }

        return new StitchGraphDto(nodes, edges);
    }
}
