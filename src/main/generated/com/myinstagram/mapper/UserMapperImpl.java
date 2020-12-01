package com.myinstagram.mapper;

import com.myinstagram.domain.dto.UserDto;
import com.myinstagram.domain.dto.UserDto.UserDtoBuilder;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.User.UserBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-01T02:54:42+0100",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 11.0.7 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User mapToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.userName( userDto.getUserName() );
        user.login( userDto.getLogin() );
        user.password( userDto.getPassword() );
        user.email( userDto.getEmail() );
        user.description( userDto.getDescription() );
        user.createDate( userDto.getCreateDate() );
        user.userStatus( userDto.getUserStatus() );
        user.enabled( userDto.isEnabled() );

        return user.build();
    }

    @Override
    public UserDto mapToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.userName( user.getUserName() );
        userDto.login( user.getLogin() );
        userDto.password( user.getPassword() );
        userDto.email( user.getEmail() );
        userDto.description( user.getDescription() );
        userDto.createDate( user.getCreateDate() );
        userDto.userStatus( user.getUserStatus() );
        userDto.enabled( user.isEnabled() );

        return userDto.build();
    }

    @Override
    public List<UserDto> mapToUsersDto(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserDto> list = new ArrayList<UserDto>( users.size() );
        for ( User user : users ) {
            list.add( mapToUserDto( user ) );
        }

        return list;
    }
}
