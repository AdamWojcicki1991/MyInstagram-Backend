package com.myinstagram.mapper;

import com.myinstagram.domain.dto.RoleDto;
import com.myinstagram.domain.entity.Role;
import com.myinstagram.domain.enums.RoleType;
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
public class RoleMapperImpl implements RoleMapper {

    @Override
    public Role mapToRole(RoleDto roleDto) {
        if ( roleDto == null ) {
            return null;
        }

        Role role = new Role();

        return role;
    }

    @Override
    public RoleDto mapToRoleDto(Role role) {
        if ( role == null ) {
            return null;
        }

        Long id = null;
        RoleType roleType = null;

        id = role.getId();
        roleType = role.getRoleType();

        RoleDto roleDto = new RoleDto( id, roleType );

        return roleDto;
    }

    @Override
    public List<RoleDto> mapToRolesDto(List<Role> roles) {
        if ( roles == null ) {
            return null;
        }

        List<RoleDto> list = new ArrayList<RoleDto>( roles.size() );
        for ( Role role : roles ) {
            list.add( mapToRoleDto( role ) );
        }

        return list;
    }
}
