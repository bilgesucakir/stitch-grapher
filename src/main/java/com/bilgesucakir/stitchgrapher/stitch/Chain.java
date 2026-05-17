package com.bilgesucakir.stitchgrapher.stitch;

/**
 * Represents a chain stitch (ch) with template properties.
 * Currently uses hardcoded example values for sizing and yarn usage.
 */
public class Chain extends  AbstractStitch{
    //Temporary hardcoded values for single crochet stitch properties
    public Chain() {
        super(
                StitchType.CH,
                0.6f,
                1.0f,
                0.8f,
                0,
                0
        );
    }
}
