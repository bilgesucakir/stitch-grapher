package com.bilgesucakir.stitchgrapher.stitch;

import com.bilgesucakir.stitchgrapher.parser.OperationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StitchFactoryTest {

    private final StitchFactory factory = new StitchFactory();

    @Test
    void createForOutput_sc_returnsSingleCrochet() {
        Stitch stitch = factory.createForOutput(OperationType.SC);

        assertThat(stitch).isInstanceOf(SingleCrochet.class);
        assertThat(stitch.getType()).isEqualTo(StitchType.SC);
    }

    @Test
    void createForOutput_inc_returnsSingleCrochet() {
        Stitch stitch = factory.createForOutput(OperationType.INC);

        // inc produces SC nodes
        assertThat(stitch).isInstanceOf(SingleCrochet.class);
        assertThat(stitch.getType()).isEqualTo(StitchType.SC);
    }

    @Test
    void createForOutput_dec_returnsSingleCrochet() {
        Stitch stitch = factory.createForOutput(OperationType.DEC);

        // dec produces SC nodes
        assertThat(stitch).isInstanceOf(SingleCrochet.class);
        assertThat(stitch.getType()).isEqualTo(StitchType.SC);
    }

    @Test
    void createForOutput_hdc_returnsHalfDoubleCrochet() {
        Stitch stitch = factory.createForOutput(OperationType.HDC);

        assertThat(stitch).isInstanceOf(HalfDoubleCrochet.class);
        assertThat(stitch.getType()).isEqualTo(StitchType.HDC);
    }

    @Test
    void createForOutput_dc_returnsDoubleCrochet() {
        Stitch stitch = factory.createForOutput(OperationType.DC);

        assertThat(stitch).isInstanceOf(DoubleCrochet.class);
        assertThat(stitch.getType()).isEqualTo(StitchType.DC);
    }

    @Test
    void createForOutput_tr_returnsTrebleCrochet() {
        Stitch stitch = factory.createForOutput(OperationType.TR);

        assertThat(stitch).isInstanceOf(TrebleCrochet.class);
        assertThat(stitch.getType()).isEqualTo(StitchType.TR);
    }

    @Test
    void createForOutput_slst_returnsSlipStitch() {
        Stitch stitch = factory.createForOutput(OperationType.SLST);

        assertThat(stitch).isInstanceOf(SlipStitch.class);
        assertThat(stitch.getType()).isEqualTo(StitchType.SLST);
    }

    @Test
    void createForOutput_unsupported_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                factory.createForOutput(null)
        );
    }
}