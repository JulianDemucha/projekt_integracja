package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Models.Comment;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import com.covid_stats.covid_stats.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    //(x) alo dzien dobry te service to sa po to co widac nizej czyli faktyczne uzywanie repozytoriow


    private final CommentRepository commentRepository;

    private final AppUserRepo userRepository;

    //(x) tu bez autowired i tak by skurwysyn springowski wstrzyknal te instancje repo ale lepiej se oznaczyc tak o chyba
    @Autowired
    public CommentService(CommentRepository commentRepository,
                          AppUserRepo userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    // tworzy nowy komentarz (główny, parent = null)
    @Transactional /* (x) kurwa tu to ciezka sprawa wytlumaczyc bez zrozumienia entitymanagera,
    ktorego mielismy na zajeciach ale teoretycznie rozumienie go nie jest potrzebne do ogarniecia springboota
    na zajeciach mielismy czystego springa nie springoota i tam wszystkie te operacje na bazach byly robione
    za pomoca niego a tu to wszystko sie dzieje w tle bo springboot jest sigmastyczny.
    w kazdym badz razie tam se cos kminisz kminisz a na koncu zeby scommitowac te zmiany to wlasnie na koncu jebiesz
    commita. no to to transactional to robi ze masz jedna transakcje na cala metode czyli normalnie pod sam koniec ci
    commita robi. ogolnie to tu masz save na koncu wiec w sumie totalnie nic nie zmienia ale clean code wiec w razie co
    gdyby cos zmieniac w tej metodzie to warto miec XDD to sie bardziej przydaje jak operujesz na kilku repo w jednej
    metodzie albo cos kminisz ze masz wiele zapisow, ogolnie sam jakos bardzo tego jeszcze nie ogaritka

    ! a ta metoda sluzy do tworzenia wylacznie glownych komentarzy !
    */
    public Comment addRootComment(String content, Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(user);
        comment.setParent(null);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }


    // tworzy odpowiedź na istniejący komentarz
    @Transactional
    public Comment addReply(Long parentId, String content, Long userId) {
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono komentarza nadrzędnego"));
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));
        Comment reply = new Comment();
        reply.setContent(content);
        reply.setAuthor(user);
        reply.setParent(parent);
        parent.getReplies().add(reply);
        return commentRepository.save(reply);
    }

    // pobiera wszystkie „glowne, root” komentarze (tych bez parent), stronicowanie po 10
    @Transactional(readOnly = true)
    public Page<Comment> getRootComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return commentRepository.findByParentIsNullOrderByCreatedAtDesc(pageable);
    }

    // pobiera wszystkie odpowiedzi do komentarza o danym ID, stronicowanie po 10
    @Transactional(readOnly = true)
    public Page<Comment> getReplies(Long parentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        return commentRepository.findByParentIdOrderByCreatedAtAsc(parentId, pageable);
    }

    // zwraca wszystkie podkomentarze bez paginacji
    @Transactional(readOnly = true)
    public List<Comment> getRepliesNoPage(Long parentId) {
        return commentRepository.findByParentIdOrderByCreatedAtAsc(parentId);
    }
}

// (x) -> DatabaseUserDetailsService
