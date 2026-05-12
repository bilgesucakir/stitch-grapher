package com.bilgesucakir.stitchgrapher.service;

import com.bilgesucakir.stitchgrapher.dto.PatternInputDto;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.PatternParser;
import com.bilgesucakir.stitchgrapher.topology.FlatTopologyBuilder;
import com.bilgesucakir.stitchgrapher.validation.FlatPatternValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for generating stitch graphs from flat (non-circular) patterns.
 */
@Service
public class FlatPatternService implements PatternService {

    private final PatternParser parser;
    private final FlatPatternValidator validator;
    private final FlatTopologyBuilder topologyBuilder;

    @Autowired
    public FlatPatternService(PatternParser parser, FlatPatternValidator validator,
                              FlatTopologyBuilder topologyBuilder) {
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
