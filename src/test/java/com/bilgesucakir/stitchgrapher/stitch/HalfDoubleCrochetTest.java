package com.bilgesucakir.stitchgrapher.stitch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HalfDoubleCrochetTest {

    @Test
    void shouldHaveCorrectProperties() {

        HalfDoubleCrochet hdc = new HalfDoubleCrochet();

        assertThat(hdc.getType()).isEqualTo(StitchType.HDC);

        assertThat(hdc.getBaseWidth()).isEqualTo(1.0f);
        assertThat(hdc.getBaseHeight()).isEqualTo(1.5f);
        assertThat(hdc.getBaseYarnUsage()).isEqualTo(1.3f);
        assertThat(hdc.getPullThrough()).isEqualTo(1);
        assertThat(hdc.getYarnOver()).isEqualTo(2);
    }
}
