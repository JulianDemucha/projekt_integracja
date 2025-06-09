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
     * pobiera wszystkie spaginowane komentarze (po 10 na strone) glowne
     */
    @GetMapping("/roots")
    public ResponseEntity<Page<Comment>> getRootComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Comment> rootsPage = commentService.getRootComments(page, size);
        return ResponseEntity.ok(rootsPage);
    }

    // dodanie nowego komentarza (authentication() w securiticonfig)
    @PostMapping("/add")
    public ResponseEntity<Comment> addRootComment(@RequestBody AddCommentRequest request) {
        Comment created = commentService.addRootComment(request.getContent(), request.getUserId());
        return ResponseEntity.ok(created);
    }

    // dodanie reply (authentication() w securityconfig)
    @PostMapping("/reply/{parentId}")
    public ResponseEntity<Comment> addReply(
            @PathVariable Long parentId,
            @RequestBody AddCommentRequest request
    ) {
        Comment reply = commentService.addReply(parentId, request.getContent(), request.getUserId());
        return ResponseEntity.ok(reply);
    }

    /**
     *     Usuwanie komentarza – dostępne tylko dla ADMIN lub autora komentarza.
     */
    @PreAuthorize("hasRole('ADMIN') or @commentSecurity.isCommentAuthor(#id, authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     *     pobranie paginowanych odpowiedzi do komentarza o danym ID
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

    // wszystkie odpowiedzi w jednej liście (publiczne GET)
    @GetMapping("/{parentId}/replies/all")
    public ResponseEntity<List<Comment>> getAllReplies(@PathVariable Long parentId) {
        List<Comment> replies = commentService.getRepliesNoPage(parentId);
        return ResponseEntity.ok(replies);
    }
}

// DTO do odbioru danych z komentarza
class AddCommentRequest {
    private Long userId;
    private String content;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
