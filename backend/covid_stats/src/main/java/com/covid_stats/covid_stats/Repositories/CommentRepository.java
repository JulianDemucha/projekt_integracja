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

    /* (x)
    dobra to tu tak samo repo jest jak i z tym userem ale pod tym sie cos dziwnego dzieje kurde blaszka co to
    za listy japierdole

    bez lypy to sa te Page i to ulatwia prace na stronach komentarzy bo tak se zrobilem i pzdr
    to page ci robi ze tam dajesz np ze w jednej stronie masz 10 komentarzy (jak w naszym przypadku)
    i kazda instancja tego page ma informacje o tym ile ogolnie jest stron wszystkich, jaki jest numer
    tej konkretnej strony i inne pierdoly. ogolnie niby rownie dobrze mozna by bylo se zrobic swoja klase
    page ktora by jakos to ogarniala ale po co jak JPA ma takie cacko

    a to jak dziala to np findByParentIsNullOrderByCreatedAtDesc i gdzie jest deklaracja bebechow tej metody
    to ostatnio ci wspominalem ze skurwysynstwo dziala tak ze se z nazwy kmini jak ma dzialac

    dobra pierdu pierdu to latwe to spierdalaj do CommentService

    -> ../Services/CommentService
     */
}
