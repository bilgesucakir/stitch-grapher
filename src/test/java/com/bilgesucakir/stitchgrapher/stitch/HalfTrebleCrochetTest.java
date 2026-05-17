package com.bilgesucakir.stitchgrapher.stitch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HalfTrebleCrochetTest {

    @Test
    void shouldHaveCorrectProperties() {

        HalfTrebleCrochet htr = new HalfTrebleCrochet();

        assertThat(htr.getType()).isEqualTo(StitchType.HTR);

        assertThat(htr.getBaseWidth()).isEqualTo(1.0f);
        assertThat(htr.getBaseHeight()).isEqualTo(2.5f);
        assertThat(htr.getBaseYarnUsage()).isEqualTo(1.8f);
        assertThat(htr.getPullThrough()).isEqualTo(3);
        assertThat(htr.getYarnOver()).isEqualTo(2);
    }

}
