package com.wap.quizit.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateAnswerDTO {

  @NotNull @NotBlank String contents;
  @NotNull Boolean isCorrect;
  @NotNull Integer pointsCount;
}
