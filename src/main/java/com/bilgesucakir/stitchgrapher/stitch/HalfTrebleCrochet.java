package com.bilgesucakir.stitchgrapher.stitch;

/**
 * Represents a half-treble crochet stitch with template properties.
 * Currently uses hardcoded example values for sizing and yarn usage.
 */
public class HalfTrebleCrochet extends AbstractStitch{

    //Temporary hardcoded values for half treble crochet stitch properties
    public HalfTrebleCrochet() {
        super(
                StitchType.HTR,
                2.5f,
                1.0f,
                1.8f,
                3,
                2
        );
    }
}
