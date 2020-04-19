package com.wap.quizit.mapper;

import com.wap.quizit.dto.RegisterUserDTO;
import com.wap.quizit.dto.UserDTO;
import com.wap.quizit.mapper.decorator.UserMapperDecorator;
import com.wap.quizit.model.*;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {

  @Mapping(target = "role", source = "entity.role.id")
  @Mapping(target = "quizzes", expression = "java(convertQuizzes(entity.getQuizzes()))")
  @Mapping(target = "comments", expression = "java(convertComments(entity.getComments()))")
  @Mapping(target = "reportsIssued", expression = "java(convertReports(entity.getReportsIssued()))")
  UserDTO map(User entity);

  @Mapping(target = "role", ignore = true)
  @Mapping(target = "comments", ignore = true)
  @Mapping(target = "quizzes", ignore = true)
  @Mapping(target = "reportsIssued", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "password", ignore = true)
  User map(UserDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "comments", ignore = true)
  @Mapping(target = "quizzes", ignore = true)
  @Mapping(target = "reportsIssued", ignore = true)
  User map(RegisterUserDTO registerForm);

  default List<Long> convertQuizzes(Set<Quiz> list) {
    return list.stream().map(Quiz::getId).collect(Collectors.toList());
  }
  default List<Long> convertComments(Set<Comment> list) {
    return list.stream().map(Comment::getId).collect(Collectors.toList());
  }
  default List<Long> convertReports(Set<Report> list) {
    return list.stream().map(Report::getId).collect(Collectors.toList());
  }
}
