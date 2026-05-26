package com.bilgesucakir.stitchgrapher.parser;

import com.bilgesucakir.stitchgrapher.exception.ParseException;
import lombok.Getter;

/**
 * Enum representing crochet operation types and their input/output characteristics.
 * Use {@link OperationType#from(String)} to convert a token string into the enum.
 */
@Getter
public enum OperationType {

    SC(1,1),
    INC(1,2),
    DEC(2,1),
    HDC(1,1),
    DC(1,1),
    HTR(1,1),
    TR(1,1),
    SLST(1,1),
    CH(0,1),
    MR(0, 0);

    private final int requiredInput;
    private final int producedOutput;

    OperationType(int requiredInput,int producedOutput) {
        this.requiredInput = requiredInput;
        this.producedOutput = producedOutput;
    }

    public static OperationType from(String token) {

        return switch(token.toLowerCase()) {

            case "sc" -> SC;
            case "inc" -> INC;
            case "dec" -> DEC;
            case "hdc" -> HDC;
            case "dc" -> DC;
            case "htr" -> HTR;
            case "tr" -> TR;
            case "slst" -> SLST;
            case "ch" -> CH;
            case "mr" -> MR;

            default -> throw new ParseException(
                    "Unknown token: " + token
            );
        };
    }
}