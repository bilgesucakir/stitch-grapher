package com.bilgesucakir.stitchgrapher.parser;

import com.bilgesucakir.stitchgrapher.exception.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test scaffold for OperationType enum.
 */
public class OperationTypeTest {

    @Test
    void from_validTokens_mapsToCorrectEnum() {
        assertEquals(OperationType.SC, OperationType.from("sc"));
        assertEquals(OperationType.INC, OperationType.from("inc"));
        assertEquals(OperationType.DEC, OperationType.from("dec"));
        assertEquals(OperationType.HDC, OperationType.from("hdc"));
        assertEquals(OperationType.DC, OperationType.from("dc"));
        assertEquals(OperationType.HTR, OperationType.from("htr"));
        assertEquals(OperationType.TR, OperationType.from("tr"));
        assertEquals(OperationType.SLST, OperationType.from("slst"));
        assertEquals(OperationType.CH, OperationType.from("ch"));
    }

    @Test
    void from_caseInsensitive_acceptsUpperAndMixedCase() {
        assertEquals(OperationType.SC, OperationType.from("Sc"));
        assertEquals(OperationType.DC, OperationType.from("DC"));
        assertEquals(OperationType.INC, OperationType.from("InC"));
    }

    @Test
    void from_unknownToken_throwsParseException() {
        ParseException ex = assertThrows(ParseException.class, () -> OperationType.from("unknown"));
        assertTrue(ex.getMessage().toLowerCase().contains("unknown token"));
    }

    @Test
    void getters_returnConfiguredValues() {
        // sanity check for requiredInput/producedOutput values
        assertEquals(1, OperationType.SC.getRequiredInput());
        assertEquals(2, OperationType.INC.getProducedOutput());
        assertEquals(0, OperationType.CH.getRequiredInput());
    }
}
