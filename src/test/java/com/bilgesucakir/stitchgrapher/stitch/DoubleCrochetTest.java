package com.bilgesucakir.stitchgrapher.stitch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test scaffold for DoubleCrochet stitch properties.
 * TODO: implement unit tests.
 */
public class DoubleCrochetTest {

    @Test
    void shouldHaveCorrectProperties() {

        DoubleCrochet dc = new DoubleCrochet();

        assertThat(dc.getType()).isEqualTo(StitchType.DC);

        assertThat(dc.getBaseWidth()).isEqualTo(1.0f);
        assertThat(dc.getBaseHeight()).isEqualTo(2.0f);
        assertThat(dc.getBaseYarnUsage()).isEqualTo(1.5f);
        assertThat(dc.getPullThrough()).isEqualTo(2);
        assertThat(dc.getYarnOver()).isEqualTo(2);
    }

}

