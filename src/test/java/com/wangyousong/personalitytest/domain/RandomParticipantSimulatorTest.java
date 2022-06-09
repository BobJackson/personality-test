package com.wangyousong.personalitytest.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class RandomParticipantSimulatorTest {

    @Test
    void should_simulate_result() {
        for (int i = 0; i < 100; i++) {
            log.debug("---------开始模拟--------");
            String result = RandomParticipantSimulator.simulate();
            log.debug("result = {}", result);
            assertThat(result).isNotEmpty();
        }
    }

    @Test
    void should_get_limit_number() {
        int sum = IntStream.rangeClosed(1, 5).limit(3).sum();

        assertThat(sum).isEqualTo(6);
    }
}