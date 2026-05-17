package com.bilgesucakir.stitchgrapher.stitch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class SingleCrochetTest {

    @Test
    void shouldHaveCorrectProperties() {

        SingleCrochet sc = new SingleCrochet();

        assertThat(sc.getType()).isEqualTo(StitchType.SC);

        assertThat(sc.getBaseWidth()).isEqualTo(1.0f);
        assertThat(sc.getBaseHeight()).isEqualTo(1.0f);
        assertThat(sc.getBaseYarnUsage()).isEqualTo(1.2f);
        assertThat(sc.getPullThrough()).isEqualTo(1);
        assertThat(sc.getYarnOver()).isEqualTo(1);
    }
}