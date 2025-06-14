package com.covid_stats.covid_stats.Services;

import com.covid_stats.covid_stats.Models.AppUser;
import com.covid_stats.covid_stats.Models.Comment;
import com.covid_stats.covid_stats.Repositories.AppUserRepo;
import com.covid_stats.covid_stats.Repositories.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AppUserRepo userRepository;

    public CommentService(CommentRepository commentRepository,
                          AppUserRepo userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
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

    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getRootComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return commentRepository.findByParentIsNullOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getReplies(Long parentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        return commentRepository.findByParentIdOrderByCreatedAtAsc(parentId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Comment> getRepliesNoPage(Long parentId) {
        return commentRepository.findByParentIdOrderByCreatedAtAsc(parentId);
    }
}
