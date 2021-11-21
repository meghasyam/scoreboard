package com.score.test.scorecard.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    @Column(name = "team1")
    @NotEmpty(message = "com.score.test.scorecard.validation.empty_team1")
    String team1;
    @Column(name = "team2")
    @NotEmpty(message = "com.score.test.scorecard.validation.empty_team2")
    String team2;
    @NotNull(message = "com.score.test.scorecard.validation.empty_date")
    @Column(name = "date_updated")
    LocalDate date;
    @Column(name = "goals1")
    @Min(value = 0, message = "com.score.test.scorecard.validation.negative_goals1")
    byte goals1;
    @Column(name = "goals2")
    @Min(value = 0, message = "com.score.test.scorecard.validation.negative_goals2")
    byte goals2;
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Builder
    private Score(Long id, String team1, String team2,
                  LocalDate date, byte goals1, byte goals2) {
        this.team1 = team1;
        this.team2 = team2;
        this.date = date;
        this.goals1 = goals1;
        this.goals2 = goals2;
        this.id = id;

        validate(this);
    }

    private void validate(Score score) {
        Set<ConstraintViolation<Score>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(score);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
