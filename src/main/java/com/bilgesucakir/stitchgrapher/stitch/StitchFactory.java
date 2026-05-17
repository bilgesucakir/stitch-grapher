package com.bilgesucakir.stitchgrapher.stitch;

import com.bilgesucakir.stitchgrapher.parser.OperationType;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating Stitch instances based on the OperationType.
 * This class abstracts the instantiation logic for different stitch types,
 * allowing for easy extension and maintenance.
 */
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
            case CH -> new Chain();
            default -> throw new IllegalArgumentException("Unsupported operation: " + type);
        };
    }
}