package com.bilgesucakir.stitchgrapher.dto;

import com.bilgesucakir.stitchgrapher.parser.CrochetMode;

import javax.xml.catalog.CatalogResolver;
import java.util.List;

/**
 * DTO for receiving pattern input from the client.
 * It contains a list of strings, where each string represents a row of stitches in the pattern.
 */
public record PatternInputDto(List<String> rows, CrochetMode mode){
}
