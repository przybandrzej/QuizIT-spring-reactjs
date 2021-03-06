package com.wap.quizit.service.mapper.decorator;

import com.wap.quizit.service.dto.CommentDTO;
import com.wap.quizit.service.mapper.CommentMapper;
import com.wap.quizit.model.Comment;
import com.wap.quizit.service.QuestionService;
import com.wap.quizit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class CommentMapperDecorator implements CommentMapper {

  @Autowired
  @Qualifier("delegate")
  private CommentMapper delegate;
  @Autowired
  private UserService userService;
  @Autowired
  private QuestionService questionService;

  @Override
  public Comment map(CommentDTO dto) {
    var comment = delegate.map(dto);
    comment.setAuthor(userService.getById(dto.getAuthor()));
    comment.setQuestion(questionService.getById(dto.getQuestion()));
    return comment;
  }
}
