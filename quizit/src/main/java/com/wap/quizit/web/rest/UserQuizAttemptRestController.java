package com.wap.quizit.web.rest;

import com.wap.quizit.model.UserQuizAttempt;
import com.wap.quizit.model.UserQuizAttemptAnswer;
import com.wap.quizit.service.UserQuizAttemptService;
import com.wap.quizit.service.dto.UserQuizAttemptAnswerDTO;
import com.wap.quizit.service.dto.UserQuizAttemptDTO;
import com.wap.quizit.service.dto.UserQuizSummary;
import com.wap.quizit.service.exception.EntityNotFoundException;
import com.wap.quizit.service.mapper.UserQuizAttemptMapper;
import com.wap.quizit.util.Constants;
import com.wap.quizit.util.DataValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/quizzes_attempts")
@AllArgsConstructor
public class UserQuizAttemptRestController {

  // TODO add creating Quiz Attempt from the Summary DTO

  private UserQuizAttemptService userQuizAttemptService;
  private UserQuizAttemptMapper userQuizAttemptMapper;

  @GetMapping
  public ResponseEntity<List<UserQuizAttemptDTO>> getAll() {
    List<UserQuizAttemptDTO> list = userQuizAttemptService.getAll()
        .stream().map(userQuizAttemptMapper::map).collect(Collectors.toList());
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/id/{id}")
  public ResponseEntity<UserQuizAttemptDTO> getById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(userQuizAttemptMapper.map(userQuizAttemptService.getById(id)), HttpStatus.OK);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<UserQuizAttemptDTO>> getByUser(@PathVariable("userId") Long userId) {
    List<UserQuizAttemptDTO> list = userQuizAttemptService.getByUser(userId)
        .stream().map(userQuizAttemptMapper::map).collect(Collectors.toList());
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/quiz/{quizId}")
  public ResponseEntity<List<UserQuizAttemptDTO>> getByQuiz(@PathVariable("quizId") Long quizId) {
    List<UserQuizAttemptDTO> list = userQuizAttemptService.getByQuiz(quizId)
        .stream().map(userQuizAttemptMapper::map).collect(Collectors.toList());
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/question/{questionId}")
  public ResponseEntity<List<UserQuizAttemptDTO>> getByQuestion(@PathVariable("questionId") Long questionId) {
    List<UserQuizAttemptDTO> list = userQuizAttemptService.getByQuestion(questionId)
        .stream().map(userQuizAttemptMapper::map).collect(Collectors.toList());
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @GetMapping("/{userId}/{quizId}")
  public ResponseEntity<List<UserQuizAttemptDTO>> getByUserAndSolvedQuiz(
      @PathVariable("userId") Long userId, @PathVariable("quizId") Long quizId) {
    List<UserQuizAttempt> list = userQuizAttemptService.getByUserAndQuiz(userId, quizId);
    List<UserQuizAttemptDTO> listDto = list.stream().map(userQuizAttemptMapper::map).collect(Collectors.toList());
    return new ResponseEntity<>(listDto, HttpStatus.OK);
  }

  @GetMapping("/id/{id}/summary")
  public ResponseEntity<UserQuizSummary> getSummaryByAttemptId(@PathVariable("id") Long id) {
    return new ResponseEntity<>(userQuizAttemptMapper.mapToSummary(userQuizAttemptService.getById(id)), HttpStatus.OK);
  }

  @GetMapping("/summary/{userId}")
  public ResponseEntity<List<UserQuizSummary>> getSummaryByUser(@PathVariable("userId") Long userId) {
    List<UserQuizAttempt> list = userQuizAttemptService.getByUser(userId);
    List<UserQuizSummary> listDto = list.stream().map(userQuizAttemptMapper::mapToSummary).collect(Collectors.toList());
    return new ResponseEntity<>(listDto, HttpStatus.OK);
  }

  @GetMapping("/summary/{userId}/{quizId}")
  public ResponseEntity<List<UserQuizSummary>> getSummaryByUserAndSolvedQuiz(
      @PathVariable("userId") Long userId, @PathVariable("quizId") Long quizId) {
    List<UserQuizAttempt> list = userQuizAttemptService.getByUserAndQuiz(userId, quizId);
    List<UserQuizSummary> listDto = list.stream().map(userQuizAttemptMapper::mapToSummary).collect(Collectors.toList());
    return new ResponseEntity<>(listDto, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<UserQuizAttemptDTO> createAttempt(@RequestBody UserQuizAttemptDTO dto) {
    UserQuizAttempt userQuizAttempt = userQuizAttemptMapper.map(dto);
    userQuizAttempt.setId(Constants.DEFAULT_ID);
    userQuizAttempt.setAttemptTime(LocalDateTime.now());
    DataValidator.validateUserQuizAttempt(userQuizAttempt);
    var saved = userQuizAttemptService.save(userQuizAttempt);
    return new ResponseEntity<>(userQuizAttemptMapper.map(saved), HttpStatus.OK);
  }

  @PostMapping("/answer")
  public ResponseEntity<UserQuizAttemptAnswerDTO> createQuestionAnswer(@RequestBody UserQuizAttemptAnswerDTO dto) {
    UserQuizAttemptAnswer userQuizAttempt = userQuizAttemptMapper.map(dto);
    userQuizAttempt.setId(Constants.DEFAULT_ID);
    DataValidator.validateUserQuizAttemptAnswer(userQuizAttempt);
    var saved = userQuizAttemptService.saveAnswer(userQuizAttempt);
    return new ResponseEntity<>(userQuizAttemptMapper.map(saved), HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<UserQuizAttemptDTO> update(@RequestBody UserQuizAttemptDTO dto) {
    if(userQuizAttemptService.getByIdNoError(dto.getId()).isEmpty()) {
      throw new EntityNotFoundException(UserQuizAttempt.class, dto.getId());
    }
    UserQuizAttempt userQuizAttempt = userQuizAttemptMapper.map(dto);
    DataValidator.validateUserQuizAttempt(userQuizAttempt);
    var saved = userQuizAttemptService.save(userQuizAttempt);
    return new ResponseEntity<>(userQuizAttemptMapper.map(saved), HttpStatus.OK);
  }

  @PutMapping("/answer")
  public ResponseEntity<UserQuizAttemptAnswerDTO> updateAnswer(@RequestBody UserQuizAttemptAnswerDTO dto) {
    if(userQuizAttemptService.getQuizAttemptAnswerByIdNoError(dto.getId()).isEmpty()) {
      throw new EntityNotFoundException(UserQuizAttemptAnswer.class, dto.getId());
    }
    UserQuizAttemptAnswer userQuizAttempt = userQuizAttemptMapper.map(dto);
    DataValidator.validateUserQuizAttemptAnswer(userQuizAttempt);
    var saved = userQuizAttemptService.saveAnswer(userQuizAttempt);
    return new ResponseEntity<>(userQuizAttemptMapper.map(saved), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAttempt(@PathVariable("id") Long id) {
    userQuizAttemptService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/answer/{id}")
  public ResponseEntity<Void> deleteAttemptAnswer(@PathVariable("id") Long id) {
    userQuizAttemptService.deleteAnswerById(id);
    return ResponseEntity.noContent().build();
  }
}
