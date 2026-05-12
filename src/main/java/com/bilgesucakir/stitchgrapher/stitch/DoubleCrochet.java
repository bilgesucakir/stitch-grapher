package com.bilgesucakir.stitchgrapher.stitch;

/**
 * Represents a double crochet stitch with template properties.
 * Currently uses hardcoded example values for sizing and yarn usage.
 */
public class DoubleCrochet extends AbstractStitch{

    //Temporary hardcoded values for double crochet stitch properties
    public DoubleCrochet() {
        super(
                StitchType.DC,
                2.0f,
                1.0f,
                1.5f,
                2,
                2
        );
    }
}
