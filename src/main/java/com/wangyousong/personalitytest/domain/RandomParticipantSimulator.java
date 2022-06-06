package com.wangyousong.personalitytest.domain;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

@Slf4j
public class RandomParticipantSimulator {

    private static final Random random = init();

    @SneakyThrows
    private static SecureRandom init() {
        return SecureRandom.getInstanceStrong();
    }

    record DataOption(int questionNumber, int score) implements ScoreOption {

    }

    public static TotalScore simulate() {
        Tiger tiger = mkTigerOption();
        Peacock peacock = mkPeacockOption();
        Owl owl = mkOwlOption();
        Koala koala = mkKoalaOption();
        Chameleon chameleon = mkChameleonOption();
        return decide(tiger, peacock, owl, koala, chameleon);
    }

    private static TotalScore decide(Tiger tiger,
                                     Peacock peacock,
                                     Owl owl,
                                     Koala koala,
                                     Chameleon chameleon) {
        // 在没有分值重复的情况下，可以决定一种动物
        Optional<TotalScore> scoreOptional = Stream.of(tiger,
                        peacock,
                        owl,
                        koala,
                        chameleon)
                .peek(it -> log.debug("This option got {}.({})", it.total(), it.getClass().getSimpleName()))
                .max(Comparator.comparing(TotalScore::total));
        return scoreOptional.orElseThrow(() -> new RuntimeException("can't decide a max score !"));
    }

    private static Tiger mkTigerOption() {
        return new Tiger(List.of(
                new DataOption(5, mkRandomScore()),
                new DataOption(10, mkRandomScore()),
                new DataOption(14, mkRandomScore()),
                new DataOption(18, mkRandomScore()),
                new DataOption(24, mkRandomScore()),
                new DataOption(30, mkRandomScore())
        ));
    }

    private static Koala mkKoalaOption() {
        return new Koala(List.of(
                new DataOption(5, mkRandomScore()),
                new DataOption(10, mkRandomScore()),
                new DataOption(14, mkRandomScore()),
                new DataOption(18, mkRandomScore()),
                new DataOption(24, mkRandomScore()),
                new DataOption(30, mkRandomScore())
        ));
    }

    private static Peacock mkPeacockOption() {
        return new Peacock(List.of(
                new DataOption(3, mkRandomScore()),
                new DataOption(6, mkRandomScore()),
                new DataOption(13, mkRandomScore()),
                new DataOption(20, mkRandomScore()),
                new DataOption(22, mkRandomScore()),
                new DataOption(29, mkRandomScore())
        ));
    }

    private static Owl mkOwlOption() {
        return new Owl(List.of(
                new DataOption(2, mkRandomScore()),
                new DataOption(8, mkRandomScore()),
                new DataOption(15, mkRandomScore()),
                new DataOption(17, mkRandomScore()),
                new DataOption(25, mkRandomScore()),
                new DataOption(28, mkRandomScore())
        ));
    }

    private static Chameleon mkChameleonOption() {
        return new Chameleon(List.of(
                new DataOption(5, mkRandomScore()),
                new DataOption(10, mkRandomScore()),
                new DataOption(14, mkRandomScore()),
                new DataOption(18, mkRandomScore()),
                new DataOption(24, mkRandomScore()),
                new DataOption(30, mkRandomScore())
        ));
    }

    private static int mkRandomScore() {
        int score = 1 + random.nextInt(5);
        log.debug("score = {}", score);
        return score;
    }
}
