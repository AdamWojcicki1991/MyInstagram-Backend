package com.myinstagram.mapper;

import com.myinstagram.domain.dto.PostDto;
import com.myinstagram.domain.entity.Post;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-01T10:15:50+0100",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.7 (Oracle Corporation)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post mapToPost(PostDto postDto) {
        if ( postDto == null ) {
            return null;
        }

        Post post = new Post();

        return post;
    }

    @Override
    public PostDto mapToPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        Long id = null;
        String postName = null;
        String caption = null;
        String url = null;
        Long imageSerialNumber = null;
        Long likesCount = null;
        LocalDate postDate = null;

        id = post.getId();
        postName = post.getPostName();
        caption = post.getCaption();
        url = post.getUrl();
        imageSerialNumber = post.getImageSerialNumber();
        likesCount = post.getLikesCount();
        postDate = post.getPostDate();

        Long userId = post.getUser().getId();
        String login = post.getUser().getLogin();

        PostDto postDto = new PostDto( id, postName, caption, url, imageSerialNumber, likesCount, postDate, userId, login );

        return postDto;
    }

    @Override
    public List<PostDto> mapToPostsDto(List<Post> posts) {
        if ( posts == null ) {
            return null;
        }

        List<PostDto> list = new ArrayList<PostDto>( posts.size() );
        for ( Post post : posts ) {
            list.add( mapToPostDto( post ) );
        }

        return list;
    }
}
