package com.bilgesucakir.stitchgrapher.topology;

import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;

/** Builder abstraction for stitch graph topology. */
public interface TopologyBuilder {

    /**
     * Build a stitch graph for the provided parsed pattern.
     *
     * @param pattern the parsed pattern to convert.
     * @return the constructed stitch graph.
     */
    StitchGraph build(ParsedPattern pattern);
}
