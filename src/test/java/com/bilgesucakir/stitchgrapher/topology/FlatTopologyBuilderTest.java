package com.bilgesucakir.stitchgrapher.topology;

import com.bilgesucakir.stitchgrapher.graph.RowDirection;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.graph.StitchNode;
import com.bilgesucakir.stitchgrapher.parser.OperationType;
import com.bilgesucakir.stitchgrapher.parser.ParsedOperation;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.ParsedRow;
import com.bilgesucakir.stitchgrapher.stitch.StitchFactory;
import com.bilgesucakir.stitchgrapher.stitch.StitchType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class FlatTopologyBuilderTest {

    private final StitchFactory stitchFactory = new StitchFactory();
    private final FlatTopologyBuilder builder = new FlatTopologyBuilder(stitchFactory);

    @Test
    void build_oneRow_buildsCorrectTopology() {
        ParsedPattern pattern = new ParsedPattern(List.of(
                new ParsedRow(0, List.of(
                        new ParsedOperation(OperationType.SC),
                        new ParsedOperation(OperationType.DC),
                        new ParsedOperation(OperationType.TR)
                ))
        ));

        StitchGraph graph = assertDoesNotThrow(() -> builder.build(pattern));

        assertNotNull(graph);
        assertEquals(1, graph.getRows().size());
        assertEquals(3, graph.getRows().get(0).getStitches().size());

        assertThat(graph.getRows().get(0).getDirection()).isEqualTo(RowDirection.LEFT_TO_RIGHT);

        StitchNode nodeFirst = graph.getRows().get(0).getStitches().get(0);
        StitchNode nodeSecond = graph.getRows().get(0).getStitches().get(1);
        StitchNode nodeThird = graph.getRows().get(0).getStitches().get(2);

        assertEquals(StitchType.SC, nodeFirst.getStitch().getType());
        assertEquals(StitchType.DC, nodeSecond.getStitch().getType());
        assertEquals(StitchType.TR, nodeThird.getStitch().getType());

        assertThat(nodeFirst.getChildren()).isEmpty();
        assertThat(nodeSecond.getChildren()).isEmpty();
        assertThat(nodeThird.getChildren()).isEmpty();

        assertThat(nodeFirst.getParents()).isEmpty();
        assertThat(nodeSecond.getParents()).isEmpty();
        assertThat(nodeThird.getParents()).isEmpty();

        assertThat(nodeFirst.getNext().equals(nodeSecond)).isTrue();
        assertThat(nodeSecond.getNext().equals(nodeThird)).isTrue();
        assertThat(nodeThird.getNext()).isNull();

        assertThat(nodeFirst.getPrevious()).isNull();
        assertThat(nodeSecond.getPrevious().equals(nodeFirst)).isTrue();
        assertThat(nodeThird.getPrevious().equals(nodeSecond)).isTrue();
    }

    @Test
    void build_twoRowsAllConsuming_buildsCorrectTopology() {
        ParsedPattern pattern = new ParsedPattern(List.of(
                new ParsedRow(0, List.of(
                        new ParsedOperation(OperationType.SC),
                        new ParsedOperation(OperationType.DC),
                        new ParsedOperation(OperationType.TR)
                )),
                new ParsedRow(1, List.of(
                        new ParsedOperation(OperationType.SC),
                        new ParsedOperation(OperationType.DC),
                        new ParsedOperation(OperationType.TR)
                ))
        ));

        StitchGraph graph = assertDoesNotThrow(() -> builder.build(pattern));

        // Verify the structure of the graph
        assertNotNull(graph);
        assertEquals(2, graph.getRows().size());
        assertEquals(3, graph.getRows().get(0).getStitches().size());
        assertEquals(3, graph.getRows().get(1).getStitches().size());

        assertThat(graph.getRows().get(0).getDirection()).isEqualTo(RowDirection.LEFT_TO_RIGHT);
        assertThat(graph.getRows().get(1).getDirection()).isEqualTo(RowDirection.RIGHT_TO_LEFT);

        //Object references for easier assertions
        StitchNode nodeFirst = graph.getRows().get(0).getStitches().get(0);
        StitchNode nodeSecond = graph.getRows().get(0).getStitches().get(1);
        StitchNode nodeThird = graph.getRows().get(0).getStitches().get(2);

        StitchNode nodeFirstRow1 = graph.getRows().get(1).getStitches().get(0);
        StitchNode nodeSecondRow1 = graph.getRows().get(1).getStitches().get(1);
        StitchNode nodeThirdRow1 = graph.getRows().get(1).getStitches().get(2);

        //Verify first row
        assertEquals(StitchType.SC, nodeFirst.getStitch().getType());
        assertEquals(StitchType.DC, nodeSecond.getStitch().getType());
        assertEquals(StitchType.TR, nodeThird.getStitch().getType());

        assertThat(nodeFirst.getChildren().size()).isEqualTo(1);
        assertThat(nodeSecond.getChildren().size()).isEqualTo(1);
        assertThat(nodeThird.getChildren().size()).isEqualTo(1);

        assertThat(nodeFirst.getChildren().get(0).equals(nodeThirdRow1)).isTrue();
        assertThat(nodeSecond.getChildren().get(0).equals(nodeSecondRow1)).isTrue();
        assertThat(nodeThird.getChildren().get(0).equals(nodeFirstRow1)).isTrue();

        assertThat(nodeFirst.getParents()).isEmpty();
        assertThat(nodeSecond.getParents()).isEmpty();
        assertThat(nodeThird.getParents()).isEmpty();

        assertThat(nodeFirst.getNext().equals(nodeSecond)).isTrue();
        assertThat(nodeSecond.getNext().equals(nodeThird)).isTrue();
        assertThat(nodeThird.getNext()).isNull();

        assertThat(nodeFirst.getPrevious()).isNull();
        assertThat(nodeSecond.getPrevious().equals(nodeFirst)).isTrue();
        assertThat(nodeThird.getPrevious().equals(nodeSecond)).isTrue();

        //Verify second row
        assertEquals(StitchType.SC, nodeFirstRow1.getStitch().getType());
        assertEquals(StitchType.DC, nodeSecondRow1.getStitch().getType());
        assertEquals(StitchType.TR, nodeThirdRow1.getStitch().getType());

        assertThat(nodeFirstRow1.getChildren()).isEmpty();
        assertThat(nodeSecondRow1.getChildren()).isEmpty();
        assertThat(nodeThirdRow1.getChildren()).isEmpty();

        assertThat(nodeFirstRow1.getParents().size()).isEqualTo(1);
        assertThat(nodeSecondRow1.getParents().size()).isEqualTo(1);
        assertThat(nodeThirdRow1.getParents().size()).isEqualTo(1);

        assertThat(nodeFirstRow1.getParents().get(0).equals(nodeThird)).isTrue();
        assertThat(nodeSecondRow1.getParents().get(0).equals(nodeSecond)).isTrue();
        assertThat(nodeThirdRow1.getParents().get(0).equals(nodeFirst)).isTrue();

        assertThat(nodeFirstRow1.getNext().equals(nodeSecondRow1)).isTrue();
        assertThat(nodeSecondRow1.getNext().equals(nodeThirdRow1)).isTrue();
        assertThat(nodeThirdRow1.getNext()).isNull();

        assertThat(nodeFirstRow1.getPrevious()).isNull();
        assertThat(nodeSecondRow1.getPrevious().equals(nodeFirstRow1)).isTrue();
        assertThat(nodeThirdRow1.getPrevious().equals(nodeSecondRow1)).isTrue();
    }

    @Test
    void build_twoRowsWithDangling_buildsCorrectTopology() {
        ParsedPattern pattern = new ParsedPattern(List.of(
                new ParsedRow(0, List.of(
                        new ParsedOperation(OperationType.SC),
                        new ParsedOperation(OperationType.DC),
                        new ParsedOperation(OperationType.TR)
                )),
                new ParsedRow(1, List.of(
                        new ParsedOperation(OperationType.SC),
                        new ParsedOperation(OperationType.DC)
                ))
        ));

        StitchGraph graph = assertDoesNotThrow(() -> builder.build(pattern));

        // Verify the structure of the graph
        assertNotNull(graph);
        assertEquals(2, graph.getRows().size());
        assertEquals(3, graph.getRows().get(0).getStitches().size());
        assertEquals(2, graph.getRows().get(1).getStitches().size());

        assertThat(graph.getRows().get(0).getDirection()).isEqualTo(RowDirection.LEFT_TO_RIGHT);
        assertThat(graph.getRows().get(1).getDirection()).isEqualTo(RowDirection.RIGHT_TO_LEFT);

        //Object references for easier assertions
        StitchNode nodeFirst = graph.getRows().get(0).getStitches().get(0);
        StitchNode nodeSecond = graph.getRows().get(0).getStitches().get(1);
        StitchNode nodeThird = graph.getRows().get(0).getStitches().get(2);

        StitchNode nodeFirstRow1 = graph.getRows().get(1).getStitches().get(0);
        StitchNode nodeSecondRow1 = graph.getRows().get(1).getStitches().get(1);

        //Verify first row
        assertEquals(StitchType.SC, nodeFirst.getStitch().getType());
        assertEquals(StitchType.DC, nodeSecond.getStitch().getType());
        assertEquals(StitchType.TR, nodeThird.getStitch().getType());

        assertThat(nodeFirst.getChildren()).isEmpty();
        assertThat(nodeSecond.getChildren().size()).isEqualTo(1);
        assertThat(nodeThird.getChildren().size()).isEqualTo(1);

        assertThat(nodeSecond.getChildren().get(0).equals(nodeSecondRow1)).isTrue();
        assertThat(nodeThird.getChildren().get(0).equals(nodeFirstRow1)).isTrue();

        assertThat(nodeFirst.getParents()).isEmpty();
        assertThat(nodeSecond.getParents()).isEmpty();
        assertThat(nodeThird.getParents()).isEmpty();

        assertThat(nodeFirst.getNext().equals(nodeSecond)).isTrue();
        assertThat(nodeSecond.getNext().equals(nodeThird)).isTrue();
        assertThat(nodeThird.getNext()).isNull();

        assertThat(nodeFirst.getPrevious()).isNull();
        assertThat(nodeSecond.getPrevious().equals(nodeFirst)).isTrue();
        assertThat(nodeThird.getPrevious().equals(nodeSecond)).isTrue();

        //Verify second row
        assertEquals(StitchType.SC, nodeFirstRow1.getStitch().getType());
        assertEquals(StitchType.DC, nodeSecondRow1.getStitch().getType());

        assertThat(nodeFirstRow1.getChildren()).isEmpty();
        assertThat(nodeSecondRow1.getChildren()).isEmpty();

        assertThat(nodeFirstRow1.getParents().size()).isEqualTo(1);
        assertThat(nodeSecondRow1.getParents().size()).isEqualTo(1);

        assertThat(nodeFirstRow1.getParents().get(0).equals(nodeThird)).isTrue();
        assertThat(nodeSecondRow1.getParents().get(0).equals(nodeSecond)).isTrue();

        assertThat(nodeFirstRow1.getNext().equals(nodeSecondRow1)).isTrue();
        assertThat(nodeSecondRow1.getNext()).isNull();

        assertThat(nodeFirstRow1.getPrevious()).isNull();
        assertThat(nodeSecondRow1.getPrevious().equals(nodeFirstRow1)).isTrue();
    }

    @Test
    void build_emptyPattern_buildsEmptyGraph() {
        ParsedPattern pattern = new ParsedPattern(List.of());

        StitchGraph graph = assertDoesNotThrow(() -> builder.build(pattern));

        assertNotNull(graph);
        assertThat(graph.getRows()).isEmpty();
        assertThat(graph.getNodes()).isEmpty();
    }

    @Test
    void build_singleRowWithOneStitch_buildsCorrectGraph() {
        ParsedPattern pattern = new ParsedPattern(List.of(
                new ParsedRow(0, List.of(
                        new ParsedOperation(OperationType.HDC)
                ))
        ));

        StitchGraph graph = assertDoesNotThrow(() -> builder.build(pattern));

        assertNotNull(graph);
        assertEquals(1, graph.getRows().size());
        assertEquals(1, graph.getRows().get(0).getStitches().size());

        StitchNode node = graph.getRows().get(0).getStitches().get(0);
        assertEquals(StitchType.HDC, node.getStitch().getType());
        assertThat(node.getNext()).isNull();
        assertThat(node.getPrevious()).isNull();
        assertThat(node.getChildren()).isEmpty();
        assertThat(node.getParents()).isEmpty();
    }

    @Test
    void build_multipleRowsWithOneStichEach_buildsCorrectGraph() {
        ParsedPattern pattern = new ParsedPattern(List.of(
                new ParsedRow(0, List.of(
                        new ParsedOperation(OperationType.SC)
                )),
                new ParsedRow(1, List.of(
                        new ParsedOperation(OperationType.DC)
                )),
                new ParsedRow(2, List.of(
                        new ParsedOperation(OperationType.TR)
                ))
        ));

        StitchGraph graph = assertDoesNotThrow(() -> builder.build(pattern));

        assertNotNull(graph);
        assertEquals(3, graph.getRows().size());

        // Verify first row
        StitchNode nodeRow0 = graph.getRows().get(0).getStitches().get(0);
        assertEquals(StitchType.SC, nodeRow0.getStitch().getType());
        assertThat(nodeRow0.getNext()).isNull();
        assertThat(nodeRow0.getPrevious()).isNull();
        assertThat(nodeRow0.getChildren().size()).isEqualTo(1);
        assertThat(nodeRow0.getChildren().get(0).equals(graph.getRows().get(1).getStitches().get(0))).isTrue();
        assertThat(nodeRow0.getParents()).isEmpty();

        // Verify second row
        StitchNode nodeRow1 = graph.getRows().get(1).getStitches().get(0);
        assertEquals(StitchType.DC, nodeRow1.getStitch().getType());
        assertThat(nodeRow1.getNext()).isNull();
        assertThat(nodeRow1.getPrevious()).isNull();
        assertThat(nodeRow1.getChildren().size()).isEqualTo(1);
        assertThat(nodeRow1.getChildren().get(0).equals(graph.getRows().get(2).getStitches().get(0))).isTrue();
        assertThat(nodeRow1.getParents().size()).isEqualTo(1);
        assertThat(nodeRow1.getParents().get(0).equals(nodeRow0)).isTrue();

        // Verify third row
        StitchNode nodeRow2 = graph.getRows().get(2).getStitches().get(0);
        assertEquals(StitchType.TR, nodeRow2.getStitch().getType());
        assertThat(nodeRow2.getNext()).isNull();
        assertThat(nodeRow2.getPrevious()).isNull();
        assertThat(nodeRow2.getChildren()).isEmpty();
        assertThat(nodeRow2.getParents().size()).isEqualTo(1);
        assertThat(nodeRow2.getParents().get(0).equals(nodeRow1)).isTrue();
    }
}