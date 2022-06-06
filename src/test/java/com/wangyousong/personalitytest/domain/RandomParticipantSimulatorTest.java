package com.wangyousong.personalitytest.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class RandomParticipantSimulatorTest {

    @Test
    void should_simulate_result() {
        TotalScore totalScore = RandomParticipantSimulator.simulate();

        log.debug("result = {}", totalScore.result());

        assertThat(totalScore.total()).isGreaterThanOrEqualTo(6);
        assertThat(totalScore.total()).isLessThanOrEqualTo(30);
    }
}