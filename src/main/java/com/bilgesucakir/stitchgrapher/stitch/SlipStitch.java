package com.bilgesucakir.stitchgrapher.stitch;

/**
 * Represents a slip stitch with template properties.
 * Currently uses hardcoded example values for sizing and yarn usage.
 */
public class SlipStitch extends AbstractStitch{

    //Temporary hardcoded values for slip stitch properties
    public SlipStitch() {
        super(
                StitchType.SLST,
                0.8f,
                1.0f,
                1.0f,
                0,
                1
        );
    }
}
