package com.bilgesucakir.stitch_grapher.stitch;

/**
 * Represents a crochet stitch with its properties.
 */
public interface Stitch {

    StitchType getType();

    float getBaseHeight();

    float getBaseWidth();

    float getBaseYarnUsage();

    int getPullThroughCount();
}
