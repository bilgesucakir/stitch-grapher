package com.bilgesucakir.stitchgrapher.stitch;

import com.bilgesucakir.stitchgrapher.parser.OperationType;
import org.springframework.stereotype.Component;

@Component
public class StitchFactory {

    public Stitch createForOutput(OperationType type) {
        if (type == null) {
            throw new IllegalArgumentException("OperationType cannot be null");
        }
        return switch (type) {
            case SC, INC, DEC -> new    SingleCrochet();
            case HDC -> new HalfDoubleCrochet();
            case DC -> new DoubleCrochet();
            case HTR -> new HalfTrebleCrochet();
            case TR -> new TrebleCrochet();
            case SLST -> new SlipStitch();
            default -> throw new IllegalArgumentException("Unsupported operation: " + type);
        };
    }
}