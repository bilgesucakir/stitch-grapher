package com.bilgesucakir.stitchgrapher.stitch;

/**
 * Represents a single crochet stitch with template properties.
 * Currently uses hardcoded example values for sizing and yarn usage.
 */
public class SingleCrochet extends AbstractStitch{

    //Temporary hardcoded values for single crochet stitch properties
    public SingleCrochet() {
        super(
                StitchType.SC,
                1.0f,
                1.0f,
                1.2f,
                1,
                1
        );
    }
}
