package com.covid_stats.covid_stats.Repositories;

import com.covid_stats.covid_stats.Models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // znajdz glowne komentarze (bez parent) w kolejnosci od najnowszych, z paginacja
    // czyli znajduje komentarze glowne (parent ma tylko odpowiedz)
    Page<Comment> findByParentIsNullOrderByCreatedAtDesc(Pageable pageable);

    // znajdz wszystkie odpowiedzi do danego komentarza, w kolejnosci rosnacej, z paginacja
    Page<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId, Pageable pageable);

    //bez paginacji (w razie co)
    List<Comment> findByParentIsNullOrderByCreatedAtDesc();
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);


}
