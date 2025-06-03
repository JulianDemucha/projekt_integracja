package com.covid_stats.covid_stats.Security;

import com.covid_stats.covid_stats.Models.Comment;
import com.covid_stats.covid_stats.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("commentSecurity")
public class CommentSecurity {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentSecurity(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Sprawdza, czy użytkownik o podanej nazwie użytkownika jest autorem komentarza o danym ID.
     * Zwraca true jeśli komentarz istnieje i comment.author.username == username, w przeciwnym razie false.
     */
    public boolean isCommentAuthor(Long commentId, String username) {
        return commentRepository.findById(commentId)
                .map(Comment::getAuthor)
                .map(user -> user.getUsername().equals(username))
                .orElse(false);
    }
}