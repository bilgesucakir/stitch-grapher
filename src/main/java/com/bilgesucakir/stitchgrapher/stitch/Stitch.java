package com.bilgesucakir.stitchgrapher.stitch;

/**
 * Represents a crochet stitch with its properties.
 */
public interface Stitch {

    StitchType getType();

    float getBaseHeight();

    float getBaseWidth();

    float getBaseYarnUsage();

    int getPullThrough();

    int getYarnOver();
}
