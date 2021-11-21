package com.score.test.scorecard.service;

import com.score.test.scorecard.domain.Score;
import com.score.test.scorecard.exception.ScoreNotFoundException;
import com.score.test.scorecard.repository.ScoreBoardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScoresService {

    private final ScoreBoardRepository scoreBoardRepository;

    public ScoresService(ScoreBoardRepository scoreBoardRepository) {
        this.scoreBoardRepository = scoreBoardRepository;
    }

    public Optional<Score> findById(Long id) {
        return scoreBoardRepository.findById(id);
    }

    public Score saveScore(Score score) {
        return scoreBoardRepository.save(score);
    }

    public void deleteScore(Long id) {
        scoreBoardRepository.findById(id)
                .orElseThrow(() -> new ScoreNotFoundException(id));
        scoreBoardRepository.deleteById(id);
    }

    public Score updateScore(Long id, Score score) {

        scoreBoardRepository.findById(id)
                .orElseThrow(() -> new ScoreNotFoundException(id));

        score.setId(id);
        return scoreBoardRepository.save(score);
    }
}
