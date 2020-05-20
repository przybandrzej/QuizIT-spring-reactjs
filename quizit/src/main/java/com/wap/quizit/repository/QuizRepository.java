package com.wap.quizit.repository;

import com.wap.quizit.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

  List<Quiz> findByTitleContainingIgnoreCase(String titleFragment);
  List<Quiz> findByCategoriesCategoryId(Long categoryId);
}
