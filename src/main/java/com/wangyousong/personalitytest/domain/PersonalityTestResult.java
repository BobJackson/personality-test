package com.wangyousong.personalitytest.domain;

import java.util.List;
import java.util.stream.Collectors;

public class PersonalityTestResult {
    private final List<? extends Class<? extends TotalScore>> classes;

    public PersonalityTestResult(List<? extends TotalScore> totalScores) {
        classes = totalScores.stream().map(TotalScore::getClass).toList();
    }

    public String result() {
        if (classes.contains(Chameleon.class)) {
            return Chameleon.class.getSimpleName();
        }
        return classes.stream().map(Class::getSimpleName).collect(Collectors.joining(","));
    }
}
