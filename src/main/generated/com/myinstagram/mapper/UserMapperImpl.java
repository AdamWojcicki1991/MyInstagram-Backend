package com.myinstagram.mapper;

import com.myinstagram.domain.dto.UserDto;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.entity.User.UserBuilder;
import com.myinstagram.domain.enums.UserStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-30T17:28:40+0100",
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

        Long id = null;
        String userName = null;
        String login = null;
        String password = null;
        String email = null;
        String description = null;
        LocalDate createDate = null;
        UserStatus userStatus = null;
        boolean enabled = false;

        id = user.getId();
        userName = user.getUserName();
        login = user.getLogin();
        password = user.getPassword();
        email = user.getEmail();
        description = user.getDescription();
        createDate = user.getCreateDate();
        userStatus = user.getUserStatus();
        enabled = user.isEnabled();

        UserDto userDto = new UserDto( id, userName, login, password, email, description, createDate, userStatus, enabled );

        return userDto;
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
