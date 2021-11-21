package com.score.test.scorecard.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.score.test.scorecard.domain.Score;
import com.score.test.scorecard.exception.ScoreNotFoundException;
import com.score.test.scorecard.service.ScoresService;
import com.score.test.scorecard.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScoresResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoresService scoresService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createScore_whenPostMethod() throws Exception {

        Score score = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();

        doReturn(score).when(scoresService).saveScore(any());
        this.mockMvc.perform(post("/api/scores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJson(score)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.team1", is(score.getTeam1())));
    }

    @Test
    public void updateScore_whenPutScore() throws Exception {
        Score score1 = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();
        given(scoresService.updateScore(score1.getId(), score1)).willReturn(score1);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        this.mockMvc.perform(put("/api/scores/" + score1.getId())
                        .content(mapper.writeValueAsString(score1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void should_throw_exception_when_Score_doesnt_exist() throws Exception {
        Score score = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();
        given(scoresService.updateScore(score.getId(), score)).willThrow(new ScoreNotFoundException(score.getId()));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        this.mockMvc.perform(put("/api/scores/" + score.getId())
                        .content(mapper.writeValueAsString(score))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void listScore_whenGetMethod()
            throws Exception {

        Score score = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();

        given(scoresService.findById(122L)).willReturn(java.util.Optional.ofNullable(score));

        this.mockMvc.perform(get("/api/scores/" + score.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void removeScoreById_whenDeleteMethod() throws Exception {
        Score score = Score.builder()
                .id(122L)
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .goals1((byte) 3)
                .goals2((byte) 4)
                .build();

        doNothing().when(scoresService).deleteScore(score.getId());

        this.mockMvc.perform(delete("/api/scores/" + score.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
