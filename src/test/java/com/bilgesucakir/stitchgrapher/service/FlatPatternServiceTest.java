package com.bilgesucakir.stitchgrapher.service;

import com.bilgesucakir.stitchgrapher.dto.PatternInputDto;
import com.bilgesucakir.stitchgrapher.exception.ValidationException;
import com.bilgesucakir.stitchgrapher.graph.StitchGraph;
import com.bilgesucakir.stitchgrapher.parser.ParsedPattern;
import com.bilgesucakir.stitchgrapher.parser.PatternParser;
import com.bilgesucakir.stitchgrapher.topology.FlatTopologyBuilder;
import com.bilgesucakir.stitchgrapher.validation.FlatPatternValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlatPatternServiceTest {

    @Mock
    private PatternParser parser;

    @Mock
    private FlatPatternValidator validator;

    @Mock
    private FlatTopologyBuilder topologyBuilder;

    @InjectMocks
    private FlatPatternService service;

    @Test
    void generateGraph_validFlow_callsAllDependencies() {

        PatternInputDto input = new PatternInputDto(List.of("3sc"), null);

        ParsedPattern parsedPattern = mock(ParsedPattern.class);
        StitchGraph graph = mock(StitchGraph.class);

        when(parser.parse(input.rows())).thenReturn(parsedPattern);
        when(topologyBuilder.build(parsedPattern)).thenReturn(graph);

        StitchGraph result = service.generateGraph(input);

        verify(parser).parse(input.rows());
        verify(validator).validate(parsedPattern);
        verify(topologyBuilder).build(parsedPattern);

        assertSame(graph, result);
    }

    @Test
    void generateGraph_validationFails_throwsValidationException_andStopsFlow() {

        PatternInputDto input = new PatternInputDto(List.of("invalid"), null);

        ParsedPattern parsedPattern = mock(ParsedPattern.class);

        when(parser.parse(input.rows())).thenReturn(parsedPattern);
        doThrow(new ValidationException("validation failed"))
                .when(validator).validate(parsedPattern);

        assertThrows(ValidationException.class,
                () -> service.generateGraph(input));

        verify(parser).parse(input.rows());
        verify(validator).validate(parsedPattern);
        verify(topologyBuilder, never()).build(any());
    }

    @Test
    void generateGraph_parserFails_stopsBeforeValidation() {

        PatternInputDto input = new PatternInputDto(List.of("bad input"), null);

        when(parser.parse(input.rows()))
                .thenThrow(new RuntimeException("parse failed"));

        assertThrows(RuntimeException.class,
                () -> service.generateGraph(input));

        verify(parser).parse(input.rows());
        verify(validator, never()).validate(any());
        verify(topologyBuilder, never()).build(any());
    }
}