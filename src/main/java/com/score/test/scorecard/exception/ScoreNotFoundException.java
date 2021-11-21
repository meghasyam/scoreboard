package com.score.test.scorecard.exception;

public class ScoreNotFoundException extends RuntimeException {
    public ScoreNotFoundException(Long id) {
        super("Could not find user with id " + id + ".");
    }
}
