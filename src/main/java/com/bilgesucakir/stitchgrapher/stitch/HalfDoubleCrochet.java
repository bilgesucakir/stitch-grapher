package com.bilgesucakir.stitchgrapher.stitch;

/**
 * Represents a half-double crochet stitch with template properties.
 * Currently uses hardcoded example values for sizing and yarn usage.
 */
public class HalfDoubleCrochet extends AbstractStitch{

    //Temporary hardcoded values for half double crochet stitch properties
    public HalfDoubleCrochet() {
        super(
                StitchType.HDC,
                1.5f,
                1.0f,
                1.3f,
                1,
                2
        );
    }
}
