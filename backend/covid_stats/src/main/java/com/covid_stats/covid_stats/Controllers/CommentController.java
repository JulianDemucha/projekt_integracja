package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.Models.Comment;
import com.covid_stats.covid_stats.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 1) Pobierz wszystkie główne komentarze (bez nadrzędnego komentarza), strona po 10 (domyślnie).
     *    Parametry:
     *      - page (opcjonalne, domyślnie 0)
     *      - size (opcjonalne, domyślnie 10)
     *
     *  Odpowiedź: Page<Comment> w JSON, zawiera:
     *    - content: lista Comment
     *    - totalPages, totalElements, size, number, itp.
     */
    @GetMapping("/roots")
    public ResponseEntity<Page<Comment>> getRootComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Comment> rootsPage = commentService.getRootComments(page, size);
        return ResponseEntity.ok(rootsPage);
    }

    /**
     * 2) Dodaj nowy komentarz główny (wymaga uwierzytelnienia).
     *    Przykład żądania JSON:
     *    {
     *      "userId": 5,
     *      "content": "To jest nowy komentarz."
     *    }
     */
    @PostMapping("/add")
    public ResponseEntity<Comment> addRootComment(@RequestBody AddCommentRequest request) {
        Comment created = commentService.addRootComment(request.getContent(), request.getUserId());
        return ResponseEntity.ok(created);
    }

    /**
     * 3) Dodaj odpowiedź do istniejącego komentarza (wymaga uwierzytelnienia).
     *    Endpoint: /api/comments/reply/{parentId}
     *    Przykład żądania JSON:
     *    {
     *      "userId": 7,
     *      "content": "To jest odpowiedź na komentarz o ID 3."
     *    }
     */
    @PostMapping("/reply/{parentId}")
    public ResponseEntity<Comment> addReply(
            @PathVariable Long parentId,
            @RequestBody AddCommentRequest request
    ) {
        Comment reply = commentService.addReply(parentId, request.getContent(), request.getUserId());
        return ResponseEntity.ok(reply);
    }

    /**
     * 4) Usuwanie komentarza – dostępne tylko dla ADMIN lub autora komentarza.
     */
    @PreAuthorize("hasRole('ADMIN') or @commentSecurity.isCommentAuthor(#id, authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 5) Pobierz paginowane odpowiedzi do komentarza o danym ID (publiczne GET).
     */
    @GetMapping("/{parentId}/replies")
    public ResponseEntity<Page<Comment>> getRepliesPaged(
            @PathVariable Long parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Comment> repliesPage = commentService.getReplies(parentId, page, size);
        return ResponseEntity.ok(repliesPage);
    }

    // (opcjonalne) jeśli gdzieś jeszcze chcemy dostać wszystkie odpowiedzi w jednej liście (publiczne GET)
    @GetMapping("/{parentId}/replies/all")
    public ResponseEntity<List<Comment>> getAllReplies(@PathVariable Long parentId) {
        List<Comment> replies = commentService.getRepliesNoPage(parentId);
        return ResponseEntity.ok(replies);
    }
}

/**
 * Prosty DTO do odbioru danych przy dodawaniu komentarza.
 */
class AddCommentRequest {
    private Long userId;
    private String content;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
