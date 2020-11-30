package com.myinstagram.mapper;

import com.myinstagram.domain.dto.CommentDto;
import com.myinstagram.domain.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    Comment mapToComment(CommentDto commentDto);

    @Mapping(target = "userId", expression = "java(comment.getPost().getUser().getId())")
    @Mapping(target = "postId", expression = "java(comment.getPost().getId())")
    @Mapping(target = "postName", expression = "java(comment.getPost().getPostName())")
    CommentDto mapToCommentDto(Comment comment);

    @Mapping(target = "userId", expression = "java(comment.getPost().getUser().getId())")
    @Mapping(target = "postId", expression = "java(comment.getPost().getId())")
    @Mapping(target = "postName", expression = "java(comment.getPost().getPostName())")
    List<CommentDto> mapToCommentsDto(List<Comment> comments);
}
