package com.bilgesucakir.stitchgrapher.stitch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TrebleCrochetTest {

    @Test
    void shouldHaveCorrectProperties() {

        TrebleCrochet tr = new TrebleCrochet();

        assertThat(tr.getType()).isEqualTo(StitchType.TR);

        assertThat(tr.getBaseWidth()).isEqualTo(1.0f);
        assertThat(tr.getBaseHeight()).isEqualTo(3.0f);
        assertThat(tr.getBaseYarnUsage()).isEqualTo(2.0f);
        assertThat(tr.getPullThrough()).isEqualTo(3);
        assertThat(tr.getYarnOver()).isEqualTo(3);
    }
}
