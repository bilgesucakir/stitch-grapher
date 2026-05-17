package com.bilgesucakir.stitchgrapher.service;

import com.bilgesucakir.stitchgrapher.dto.PatternInputDto;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;

/**
 * Service interface for generating a stitch graph from a given pattern input.
 * The implementation of this service will contain the logic to parse the pattern input and create a stitch graph representation.
 */
public interface PatternService {
    StitchGraph generateGraph(PatternInputDto input);
}