package com.bilgesucakir.stitchgrapher.stitch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SlipStitchTest {

    @Test
    void shouldHaveCorrectProperties() {

        SlipStitch slst = new SlipStitch();

        assertThat(slst.getType()).isEqualTo(StitchType.SLST);

        assertThat(slst.getBaseWidth()).isEqualTo(1.0f);
        assertThat(slst.getBaseHeight()).isEqualTo(0.8f);
        assertThat(slst.getBaseYarnUsage()).isEqualTo(1.0f);
        assertThat(slst.getPullThrough()).isEqualTo(0);
        assertThat(slst.getYarnOver()).isEqualTo(1);
    }
}
