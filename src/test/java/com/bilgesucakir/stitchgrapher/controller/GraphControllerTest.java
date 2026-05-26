package com.bilgesucakir.stitchgrapher.controller;

import com.bilgesucakir.stitchgrapher.dto.PatternInputDto;
import com.bilgesucakir.stitchgrapher.dto.StitchGraphDto;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.mapper.StitchGraphMapper;
import com.bilgesucakir.stitchgrapher.parser.CrochetMode;
import com.bilgesucakir.stitchgrapher.service.CircularPatternService;
import com.bilgesucakir.stitchgrapher.service.FlatPatternService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GraphControllerTest {

    @Mock
    private CircularPatternService circularPatternService;

    @Mock
    private FlatPatternService flatPatternService;

    @Mock
    private StitchGraphMapper stitchGraphMapper;

    @InjectMocks
    private GraphController controller;

    @Test
    void generateGraph_circularMode_routesToCircularService_andMapsResult() {

        PatternInputDto input =
                new PatternInputDto(List.of("mr", "3sc"), CrochetMode.CIRCULAR);

        StitchGraph graph = mock(StitchGraph.class);
        StitchGraphDto expectedDto = new StitchGraphDto(List.of(), List.of());

        when(circularPatternService.generateGraph(input)).thenReturn(graph);
        when(stitchGraphMapper.toDto(graph)).thenReturn(expectedDto);

        StitchGraphDto result = controller.generateGraph(input);

        verify(circularPatternService).generateGraph(input);
        verify(flatPatternService, never()).generateGraph(any());
        verify(stitchGraphMapper).toDto(graph);

        assertSame(expectedDto, result);
    }

    @Test
    void generateGraph_flatMode_routesToFlatService_andMapsResult() {

        PatternInputDto input =
                new PatternInputDto(List.of("3sc"), CrochetMode.FLAT);

        StitchGraph graph = mock(StitchGraph.class);
        StitchGraphDto expectedDto = new StitchGraphDto(List.of(), List.of());

        when(flatPatternService.generateGraph(input)).thenReturn(graph);
        when(stitchGraphMapper.toDto(graph)).thenReturn(expectedDto);

        StitchGraphDto result = controller.generateGraph(input);

        verify(flatPatternService).generateGraph(input);
        verify(circularPatternService, never()).generateGraph(any());
        verify(stitchGraphMapper).toDto(graph);

        assertSame(expectedDto, result);
    }

    @Test
    void generateGraph_serviceThrows_exceptionPropagates_andMapperNotCalled() {

        PatternInputDto input =
                new PatternInputDto(List.of("invalid"), CrochetMode.FLAT);

        when(flatPatternService.generateGraph(input))
                .thenThrow(new RuntimeException("validation failed"));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> controller.generateGraph(input)
        );

        assertEquals("validation failed", ex.getMessage());

        verify(flatPatternService).generateGraph(input);
        verify(stitchGraphMapper, never()).toDto(any());
    }
}