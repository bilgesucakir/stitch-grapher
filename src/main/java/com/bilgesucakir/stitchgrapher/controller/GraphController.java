package com.bilgesucakir.stitchgrapher.controller;

import com.bilgesucakir.stitchgrapher.dto.GraphEdgeDto;
import com.bilgesucakir.stitchgrapher.dto.GraphNodeDto;
import com.bilgesucakir.stitchgrapher.dto.StitchGraphDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling graph-related API endpoints.
 */
@RestController
public class GraphController {

    @GetMapping("/api/graph")
    public StitchGraphDto getGraph() {

        List<GraphNodeDto> nodes = List.of(
            new GraphNodeDto("a", "a", 0, 0, "LEFT_TO_RIGHT"),
            new GraphNodeDto("b", "b", 0, 1, "LEFT_TO_RIGHT"),
            new GraphNodeDto("c", "c", 0, 2, "LEFT_TO_RIGHT"),
            new GraphNodeDto("d", "d", 1, 0, "RIGHT_TO_LEFT"),
            new GraphNodeDto("e", "e", 1,1, "RIGHT_TO_LEFT"),
            new GraphNodeDto("f", "f", 1,2, "RIGHT_TO_LEFT"),
            new GraphNodeDto("g", "g", 1,3, "RIGHT_TO_LEFT"),
            new GraphNodeDto("h", "h", 1,4, "RIGHT_TO_LEFT"),
            new GraphNodeDto("i", "i", 2, 0, "LEFT_TO_RIGHT"),
            new GraphNodeDto("j", "j", 2,1, "LEFT_TO_RIGHT"),
            new GraphNodeDto("k", "k", 2,2, "LEFT_TO_RIGHT")
        );

        List<GraphEdgeDto> edges = List.of(
            //row1
            new GraphEdgeDto("a", "b"),
            new GraphEdgeDto("b", "c"),

            //row2
            new GraphEdgeDto("d", "e"),
            new GraphEdgeDto("e", "f"),
            new GraphEdgeDto("f", "g"),
            new GraphEdgeDto("g", "h"),

            //row3
            new GraphEdgeDto("i", "j"),
            new GraphEdgeDto("j", "k"),

            //row1-row2
            new GraphEdgeDto("a","h"),
            new GraphEdgeDto("a", "g"),
            new GraphEdgeDto("b", "f"),
            new GraphEdgeDto("c", "e"),
            new GraphEdgeDto("c", "d"),

            //row2-row3
            new GraphEdgeDto("d", "k"),
            new GraphEdgeDto("e", "k"),
            new GraphEdgeDto("f", "j"),
            new GraphEdgeDto("g", "j"),
            new GraphEdgeDto("h", "i")
        );

        return new StitchGraphDto(
            nodes,
            edges
        );
    }
}
