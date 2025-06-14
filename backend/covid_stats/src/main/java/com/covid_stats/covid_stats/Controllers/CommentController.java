package com.covid_stats.covid_stats.Controllers;

import com.covid_stats.covid_stats.DTO.AddCommentRequest;
import com.covid_stats.covid_stats.Models.Comment;
import com.covid_stats.covid_stats.Services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/roots")
    public ResponseEntity<Page<Comment>> getRootComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                commentService.getRootComments(page, size)
        );
    }

    @PostMapping("/add")
    public ResponseEntity<Comment> addRootComment(@RequestBody AddCommentRequest request) {
        return ResponseEntity.ok(
                commentService.addRootComment(request.getContent(), request.getUserId())
        );
    }

    @PostMapping("/reply/{parentId}")
    public ResponseEntity<Comment> addReply(
            @PathVariable Long parentId,
            @RequestBody AddCommentRequest request
    ) {
        return ResponseEntity.ok(
                commentService.addReply(parentId, request.getContent(), request.getUserId())
        );
    }

    @PreAuthorize("hasRole('ADMIN') or @commentSecurity.isCommentAuthor(#id, authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{parentId}/replies")
    public ResponseEntity<Page<Comment>> getRepliesPaged(
            @PathVariable Long parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                commentService.getReplies(parentId, page, size)
        );
    }

    @GetMapping("/{parentId}/replies/all")
    public ResponseEntity<List<Comment>> getAllReplies(@PathVariable Long parentId) {
        return ResponseEntity.ok(
                commentService.getRepliesNoPage(parentId)
        );
    }
}
