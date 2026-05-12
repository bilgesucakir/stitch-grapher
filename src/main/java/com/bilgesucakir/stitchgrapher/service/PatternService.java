package com.bilgesucakir.stitchgrapher.service;

import com.bilgesucakir.stitchgrapher.dto.PatternInputDto;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;

public interface PatternService {
    StitchGraph generateGraph(PatternInputDto input);
}