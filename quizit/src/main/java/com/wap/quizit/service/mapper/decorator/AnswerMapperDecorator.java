package com.wap.quizit.service.mapper.decorator;

import com.wap.quizit.model.Answer;
import com.wap.quizit.model.Question;
import com.wap.quizit.service.AnswerService;
import com.wap.quizit.service.QuestionService;
import com.wap.quizit.service.dto.AnswerDTO;
import com.wap.quizit.service.dto.CreateAnswerDTO;
import com.wap.quizit.service.mapper.AnswerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashSet;

public abstract class AnswerMapperDecorator implements AnswerMapper {

  @Autowired
  @Qualifier("delegate")
  private AnswerMapper delegate;
  @Autowired
  private QuestionService questionService;
  @Autowired
  private AnswerService answerService;

  @Override
  public Answer map(AnswerDTO dto) {
    var answer = delegate.map(dto);
    Question question = questionService.getById(dto.getQuestion());
    answer.setQuestion(question);
    answer.setUserQuizAttemptAnswers(
        answerService.getByIdNoException(dto.getId())
            .map(Answer::getUserQuizAttemptAnswers)
            .orElse(new HashSet<>()));
    return answer;
  }

  @Override
  public Answer map(CreateAnswerDTO dto) {
    var answer = delegate.map(dto);
    answer.setQuestion(null);
    answer.setUserQuizAttemptAnswers(new HashSet<>());
    return answer;
  }
}
