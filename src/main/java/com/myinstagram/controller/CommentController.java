package com.myinstagram.controller;

import com.myinstagram.domain.dto.CommentDto;
import com.myinstagram.domain.dto.CommentRequest;
import com.myinstagram.domain.dto.UpdateCommentRequest;
import com.myinstagram.facade.comment.CommentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/comments")
public final class CommentController {
    private final CommentFacade commentFacade;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments() {
        return commentFacade.getComments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable final Long id) {
        return commentFacade.getComment(id);
    }

    @PostMapping
    public ResponseEntity<CommentDto> publishComment(@RequestBody final CommentRequest commentRequest) {
        return commentFacade.publishComment(commentRequest);
    }

    @PutMapping
    public ResponseEntity<CommentDto> updateComment(@RequestBody final UpdateCommentRequest updateCommentRequest) {
        return commentFacade.updateComment(updateCommentRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCommentById(@PathVariable final Long id) {
        return commentFacade.deleteCommentById(id);
    }
}
