package com.score.test.scorecard.service;

import com.score.test.scorecard.domain.Score;
import com.score.test.scorecard.repository.ScoreBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ScoresServiceTest {

    @MockBean
    private ScoreBoardRepository scoreBoardRepository;

    private ScoresService scoresService;

    private Score score;

    @BeforeEach
    public void setup() {
        scoresService = new ScoresService(scoreBoardRepository);
        score = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();

    }

    @Test
    public void whenGivenId_shouldReturnScore_ifFound() {
        when(scoreBoardRepository.findById(122L)).thenReturn(java.util.Optional.ofNullable(score));

        Optional<Score> actualScore = scoresService.findById(122L);
        assertEquals(actualScore.get().getId(), score.getId());
        assertEquals(actualScore.get().getTeam1(), score.getTeam1());
        assertEquals(actualScore.get().getTeam2(), score.getTeam2());
        assertEquals(actualScore.get().getGoals1(), score.getGoals1());
        assertEquals(actualScore.get().getGoals2(), score.getGoals2());
    }

    @Test
    public void test_saveScore() {
        when(scoreBoardRepository.save(score)).thenReturn(score);

        Score actualScore = scoresService.saveScore(score);
        assertEquals(actualScore.getId(), score.getId());
        assertEquals(actualScore.getTeam1(), score.getTeam1());
        assertEquals(actualScore.getTeam2(), score.getTeam2());
        assertEquals(actualScore.getGoals1(), score.getGoals1());
        assertEquals(actualScore.getGoals2(), score.getGoals2());
        verify(scoreBoardRepository).save(score);
    }

    @Test
    public void whenGivenId_shouldDeleteScore_ifFound() {
        Score score1 = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();

        when(scoreBoardRepository.findById(122L)).thenReturn(java.util.Optional.ofNullable(score1));

        scoresService.deleteScore(score1.getId());
        verify(scoreBoardRepository).deleteById(score1.getId());
    }


    @Test
    public void whenDerivedExceptionThrown_thenAssertionSucceds() {
        Score score2 = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();

        when(scoreBoardRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(null));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            scoresService.deleteScore(score2.getId());
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains("Could not find user with id"));
    }

    @Test
    public void whenGivenId_shouldUpdateScore_ifFound() {
        Score score1 = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();

        Score score2 = Score.builder()
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 31)
                .goals2((byte) 41)
                .build();

        given(scoreBoardRepository.findById(score1.getId())).willReturn(Optional.of(score1));
        scoresService.updateScore(score1.getId(), score2);

        verify(scoreBoardRepository).save(score2);
        verify(scoreBoardRepository).findById(score1.getId());
    }

    @Test
    public void should_throw_exception_when_user_doesnt_exist() {
        Score score1 = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();

        given(scoreBoardRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            scoresService.updateScore(111L, score1);
        });

        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains("Could not find user with id"));
    }
}
