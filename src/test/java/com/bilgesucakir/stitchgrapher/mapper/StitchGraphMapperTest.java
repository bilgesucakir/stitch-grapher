package com.bilgesucakir.stitchgrapher.mapper;

import com.bilgesucakir.stitchgrapher.dto.StitchGraphDto;
import com.bilgesucakir.stitchgrapher.graph.Row;
import com.bilgesucakir.stitchgrapher.graph.RowDirection;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.graph.StitchNode;
import com.bilgesucakir.stitchgrapher.stitch.SingleCrochet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StitchGraphMapperTest {

    private final StitchGraphMapper mapper = new StitchGraphMapper();

    @Test
    void shouldMapNodesAndEdgesCorrectly() {

        // Create graph
        StitchGraph graph = new StitchGraph();

        Row row0 = new Row(0, RowDirection.LEFT_TO_RIGHT);
        Row row1 = new Row(1, RowDirection.RIGHT_TO_LEFT);

        StitchNode n1 = new StitchNode(new SingleCrochet());
        StitchNode n2 = new StitchNode(new SingleCrochet());
        StitchNode n3 = new StitchNode(new SingleCrochet());

        // row 0: n1 -> n2
        n1.connectNext(n2);
        row0.addStitch(n1);
        row0.addStitch(n2);

        // row 1: n3
        row1.addStitch(n3);

        // parent-child
        n2.addChild(n3);

        graph.addRow(row0);
        graph.addRow(row1);

        // map
        StitchGraphDto dto = mapper.toDto(graph);

        // assertions

        // nodes
        assertThat(dto.nodes()).hasSize(3);

        assertThat(dto.nodes())
                .extracting("label")
                .containsOnly("SC");

        // edges (n1->n2 and n2->n3)
        assertThat(dto.edges()).hasSize(2);

        assertThat(dto.edges())
                .anyMatch(e -> e.source().equals(n1.getId().toString())
                        && e.target().equals(n2.getId().toString()));

        assertThat(dto.edges())
                .anyMatch(e -> e.source().equals(n2.getId().toString())
                        && e.target().equals(n3.getId().toString()));
    }
}