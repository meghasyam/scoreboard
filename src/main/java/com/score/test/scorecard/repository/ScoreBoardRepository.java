package com.score.test.scorecard.repository;


import com.score.test.scorecard.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreBoardRepository extends JpaRepository<Score, Long> {
}
