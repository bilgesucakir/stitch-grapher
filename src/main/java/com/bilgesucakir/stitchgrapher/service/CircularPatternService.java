package com.bilgesucakir.stitchgrapher.service;

import com.bilgesucakir.stitchgrapher.dto.PatternInputDto;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.PatternParser;
import com.bilgesucakir.stitchgrapher.topology.CircularTopologyBuilder;
import com.bilgesucakir.stitchgrapher.validation.CircularPatternValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for generating stitch graphs from circular patterns.
 */
@Service
public class CircularPatternService implements PatternService{

    private final PatternParser parser;
    private final CircularPatternValidator validator;
    private final CircularTopologyBuilder topologyBuilder;

    @Autowired
    public CircularPatternService(PatternParser parser, CircularPatternValidator validator,
                              CircularTopologyBuilder topologyBuilder) {
        this.parser = parser;
        this.validator = validator;
        this.topologyBuilder = topologyBuilder;
    }

    @Override
    public StitchGraph generateGraph(PatternInputDto input) {

        ParsedPattern pattern = parser.parse(input.rows());
        validator.validate(pattern);
        return topologyBuilder.build(pattern);
    }
}
