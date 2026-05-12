package com.bilgesucakir.stitchgrapher.stitch;

/**
 * Represents a treble (triple) crochet stitch with template properties.
 * Currently uses hardcoded example values for sizing and yarn usage.
 */
public class TrebleCrochet extends AbstractStitch{

    //Temporary hardcoded values for treble crochet stitch properties
    public TrebleCrochet() {
        super(
                StitchType.TR,
                3.0f,
                1.0f,
                2.0f,
                3,
                3
        );
    }
}
