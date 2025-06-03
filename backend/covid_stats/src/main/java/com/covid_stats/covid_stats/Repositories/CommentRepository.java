package com.covid_stats.covid_stats.Repositories;

import com.covid_stats.covid_stats.Models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // znajdź główne komentarze (bez parent) w kolejności od najnowszych, z paginacją
    Page<Comment> findByParentIsNullOrderByCreatedAtDesc(Pageable pageable);

    // znajdź wszystkie odpowiedzi do danego komentarza, w kolejności rosnącej, z paginacją
    Page<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId, Pageable pageable);

    // (opcjonalnie) jeżeli wciąż gdzieś będziemy chcieli pobrać listę bez paginacji:
    List<Comment> findByParentIsNullOrderByCreatedAtDesc();
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
}
