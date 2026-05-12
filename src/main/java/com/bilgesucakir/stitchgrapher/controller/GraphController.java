package com.bilgesucakir.stitchgrapher.controller;

import com.bilgesucakir.stitchgrapher.dto.PatternInputDto;
import com.bilgesucakir.stitchgrapher.dto.StitchGraphDto;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.mapper.StitchGraphMapper;
import com.bilgesucakir.stitchgrapher.service.CircularPatternService;
import com.bilgesucakir.stitchgrapher.service.FlatPatternService;
import com.bilgesucakir.stitchgrapher.parser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling graph-related API endpoints.
 * Temporarily, this controller contains the logic for generating a stitch graph from a parsed pattern.
 * In the future, this logic should be moved to a dedicated service class to keep the controller focused on handling HTTP requests and responses.
 */
@RestController
public class GraphController {

    private final CircularPatternService circularPatternService;
    private final FlatPatternService flatPatternService;
    private final StitchGraphMapper stitchGraphMapper;

    @Autowired
    public GraphController(CircularPatternService circularPatternService, FlatPatternService flatPatternService,
                           StitchGraphMapper stitchGraphMapper) {
        this.circularPatternService = circularPatternService;
        this.flatPatternService = flatPatternService;
        this.stitchGraphMapper = stitchGraphMapper;
    }

    /**
     * Endpoint for generating a stitch graph from the input pattern.
     *
     * @param input the input pattern received as a {@link PatternInputDto}; contains a list of rows and the crochet mode
     * @return a {@link StitchGraphDto} containing the nodes and edges of the generated stitch graph
     */
    @PostMapping("/api/graph")
    public StitchGraphDto generateGraph(@RequestBody PatternInputDto input) {

        StitchGraph graph;
        if(input.mode() == CrochetMode.CIRCULAR) {
            graph = circularPatternService.generateGraph(input);
        } else {
            graph = flatPatternService.generateGraph(input);
        }

        return stitchGraphMapper.toDto(graph);
    }
}
