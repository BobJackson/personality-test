package com.wangyousong.personalitytest.domain;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

@Slf4j
public class RandomParticipantSimulator {

    private static final Random random = init();

    @SneakyThrows
    private static SecureRandom init() {
        return SecureRandom.getInstanceStrong();
    }

    record DataOption(int questionNumber, int score) implements ScoreOption {

    }

    public static String simulate() {
        List<? extends TotalScore> totalScores = calculate(
                mkTigerOption(),
                mkPeacockOption(),
                mkOwlOption(),
                mkKoalaOption(),
                mkChameleonOption()
        );
        return new PersonalityTestResult(totalScores).result();
    }

    private static List<? extends TotalScore> calculate(Tiger tiger,
                                                        Peacock peacock,
                                                        Owl owl,
                                                        Koala koala,
                                                        Chameleon chameleon) {

        long count = Stream.of(tiger,
                        peacock,
                        owl,
                        koala,
                        chameleon)
                .map(it -> {
                    int total = it.total();
                    log.debug("{} got {}", it.getClass().getSimpleName(), it.total());
                    return total;
                })
                .distinct()
                .count();

        log.debug("distinct score count = {}", count);

        // case 1: 5个不同分值，最高项得分的动物
        if (count == 5) {
            Optional<? extends TotalScore> scoreOptional = Stream.of(tiger,
                            peacock,
                            owl,
                            koala,
                            chameleon)
                    .max(comparing(TotalScore::total));
            return List.of(scoreOptional.orElseThrow(() -> new RuntimeException("can't calculate a max score !")));
        }

        // case 2: 4个分值相等，不管是否包含变色龙，都是变色龙
        if (count == 4) {
            return List.of(chameleon);
        }

        // case 3: 3个分值相等，包含变色龙,就是变色龙；不包含变色龙，就是这三项最高分的组合
        List<? extends TotalScore> totalScores = List.of(tiger, peacock, owl, koala, chameleon);
        Map<Integer, Integer> totalScoreContainer = createTotalScoreContainer(totalScores);
        if (count == 3) {
            return findSameNTotalScores(chameleon, totalScores, totalScoreContainer, t -> t != 3);
        }
        // case 4: 2个分值相等，包含变色龙，就是变色龙；不包含变色龙，就是这两项最高分的组合
        if (count == 2) {
            return findSameNTotalScores(chameleon, totalScores, totalScoreContainer, t -> t != 2);
        }

        // case 5: 5个分值均相等（count == 1），则是变色龙
        return List.of(chameleon);
    }

    private static List<? extends TotalScore> findSameNTotalScores(Chameleon chameleon,
                                                                   List<? extends TotalScore> totalScores,
                                                                   Map<Integer, Integer> totalScoreContainer,
                                                                   Predicate<Integer> notCount) {
        // 找到分值相同的N个选项，判断其中是否包含变色龙
        List<Integer> excludedTotalScores = totalScoreContainer.values().stream().filter(notCount).toList();
        List<? extends TotalScore> reservedTotalScores = totalScores.stream()
                .filter(t -> !excludedTotalScores.contains(t.total())).toList();
        boolean hasChameleon = reservedTotalScores.stream()
                .anyMatch(it -> it.getClass().isInstance(Chameleon.class));
        if (hasChameleon) {
            return List.of(chameleon);
        }
        return reservedTotalScores;
    }

    private static Map<Integer, Integer> createTotalScoreContainer(List<? extends TotalScore> totalScores) {
        Map<Integer, Integer> totalScoreContainer = new HashMap<>();
        for (TotalScore ts : totalScores) {
            Integer frequency = totalScoreContainer.get(ts.total());
            totalScoreContainer.put(ts.total(), Objects.isNull(frequency) ? 1 : frequency + 1);
        }
        return totalScoreContainer;
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
        return 1 + random.nextInt(5);
    }
}
