package com.score.test.scorecard.resource;

import com.score.test.scorecard.domain.Score;
import com.score.test.scorecard.service.ScoresService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/scores")
@Slf4j
public class ScoresResource {

    private ScoresService scoresService;

    public ScoresResource(ScoresService scoresService) {
        this.scoresService = scoresService;
    }

    @GetMapping("/{scoreId}")
    public Optional<Score> findById(@PathVariable @NotNull Long scoreId) {
        log.info("Find score by id " + scoreId);

        return scoresService
                .findById(scoreId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Execute POST method")
    public ResponseEntity<Score> createNewScore_whenPostScore(@RequestBody Score score) {

        Score createdScore = scoresService.saveScore(score);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdScore.getId())
                .toUri();

        return ResponseEntity.created(uri).body(createdScore);
    }


    @PutMapping("/{scoreId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Execute PUT method")
    public ResponseEntity<Score> updateScore_whenPutScore(@RequestBody Score score, @PathVariable Long scoreId) {
        return ResponseEntity.ok().body(scoresService.updateScore(scoreId, score));
    }

    @DeleteMapping("/{scoreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Execute DELETE method")
    public void deleteScore_whenDeleteScore(@PathVariable Long scoreId) {
        scoresService.deleteScore(scoreId);
    }
}

