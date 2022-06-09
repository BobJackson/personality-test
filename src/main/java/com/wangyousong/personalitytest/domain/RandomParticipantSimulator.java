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
        // 根据总分排序
        List<? extends TotalScore> sortedTotalScore = Stream.of(tiger,
                        peacock,
                        owl,
                        koala,
                        chameleon)
                .sorted(comparing(TotalScore::total).reversed())
                .peek(t -> log.debug("{} got {}", t.getClass().getSimpleName(), t.total()))
                .toList();
        // case 1: 如果第一个大于第二个，则只返回第一个（这种情况会包含原来的case 1: 5个不同分值，最高项得分的动物）
        if (sortedTotalScore.get(0).total() > sortedTotalScore.get(1).total()) {
            log.debug("满足case 1: 第一项得分大于第二项。 [{} 得分 {}]  > [{} 得分 {}]",
                    sortedTotalScore.get(0).getClass().getSimpleName(),
                    sortedTotalScore.get(0).total(),
                    sortedTotalScore.get(1).getClass().getSimpleName(),
                    sortedTotalScore.get(1).total()
                    );
            return sortedTotalScore.stream().limit(1).toList();
        }

        // case1为后面定下了基调：第一项等于第二项

        // 有分值重复的情况：再根据不同分值的数量判断
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

        // case 2: 4个或者3个分值相等，则是第一项和第二项组合为待选项，此时判断如果包含变色龙，就是变色龙，否则就是这两项组合；
        if (count == 4 || count == 3) {
            boolean hasChameleon = sortedTotalScore.stream()
                    .limit(2)
                    .anyMatch(RandomParticipantSimulator::isAChameleon);
            if (hasChameleon) {
                log.debug("满足case 2, 包含变色龙");
                return List.of(chameleon);
            }
            return sortedTotalScore.stream()
                    .limit(2)
                    .peek(t -> log.debug("满足case 2, 不包含变色龙，分别是： {} got {}", t.getClass().getSimpleName(), t.total()))
                    .toList();
        }

        // case 4: 4项相同 ｜｜5项相同，就是变色龙；
        log.debug("满足case 3： 4项或者5项都相同，直接变色龙");
        return List.of(chameleon);
    }

    private static boolean isAChameleon(TotalScore it) {
        return it.getClass().isInstance(Chameleon.class);
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
