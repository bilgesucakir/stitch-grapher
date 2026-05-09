package com.bilgesucakir.stitchgrapher.dto;

import java.util.List;

public record StitchGraphDto(List<GraphNodeDto> nodes, List<GraphEdgeDto> edges) {
}
