package com.myinstagram.mapper;

import com.myinstagram.domain.dto.CommentDto;
import com.myinstagram.domain.entity.Comment;
import com.myinstagram.domain.entity.Comment.CommentBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-30T17:31:57+0100",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.7 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment mapToComment(CommentDto commentDto) {
        if ( commentDto == null ) {
            return null;
        }

        CommentBuilder comment = Comment.builder();

        comment.commentName( commentDto.getCommentName() );
        comment.content( commentDto.getContent() );
        comment.commentDate( commentDto.getCommentDate() );

        return comment.build();
    }

    @Override
    public CommentDto mapToCommentDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        Long id = null;
        String commentName = null;
        String content = null;
        LocalDate commentDate = null;

        id = comment.getId();
        commentName = comment.getCommentName();
        content = comment.getContent();
        commentDate = comment.getCommentDate();

        Long userId = comment.getPost().getUser().getId();
        Long postId = comment.getPost().getId();
        String postName = comment.getPost().getPostName();

        CommentDto commentDto = new CommentDto( id, commentName, content, commentDate, postId, postName, userId );

        return commentDto;
    }

    @Override
    public List<CommentDto> mapToCommentsDto(List<Comment> comments) {
        if ( comments == null ) {
            return null;
        }

        List<CommentDto> list = new ArrayList<CommentDto>( comments.size() );
        for ( Comment comment : comments ) {
            list.add( mapToCommentDto( comment ) );
        }

        return list;
    }
}
