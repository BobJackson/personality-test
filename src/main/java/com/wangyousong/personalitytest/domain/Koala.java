package com.wangyousong.personalitytest.domain;

import java.util.List;

public class Koala implements TotalScore {

    private final List<ScoreOption> options;

    public Koala(List<ScoreOption> options) {
        this.options = options;
    }

    @Override
    public int total() {
        return options.stream().mapToInt(ScoreOption::score).sum();
    }

    @Override
    public String result() {
        return "You are a " + this.getClass().getSimpleName() + " , as you got " + total() + " score !";
    }
}
