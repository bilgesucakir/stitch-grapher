package com.bilgesucakir.stitch_grapher.stitch;

/**
 * Abstract base class for crochet stitches, providing common properties and methods.
 * Concrete stitch classes can extend this class to inherit these properties and implement specific behaviors.
 */
public abstract class AbstractStitch implements Stitch{

    private final StitchType type;
    private final float baseHeight;
    private final float baseWidth;
    private final float baseYarnUsage;
    private final int pullThroughCount;

    public AbstractStitch(StitchType type, float baseHeight, float baseWidth, float baseYarnUsage, int pullThroughCount) {
        this.type = type;
        this.baseHeight = baseHeight;
        this.baseWidth = baseWidth;
        this.baseYarnUsage = baseYarnUsage;
        this.pullThroughCount = pullThroughCount;
    }

    @Override
    public StitchType getType() {
        return type;
    }

    @Override
    public float getBaseHeight() {
        return baseHeight;
    }

    @Override
    public float getBaseWidth() {
        return baseWidth;
    }

    @Override
    public float getBaseYarnUsage() {
        return baseYarnUsage;
    }

    @Override
    public int getPullThroughCount() {
        return pullThroughCount;
    }

    @Override
    public String toString() {
        //Temporary toString implementation
        return type.name();
    }

}
