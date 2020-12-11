package com.myinstagram.controller;

import com.google.gson.Gson;
import com.myinstagram.domain.dto.RoleDto;
import com.myinstagram.domain.dto.RoleRequest;
import com.myinstagram.facade.role.RoleFacade;
import com.myinstagram.security.jwt.JwtProvider;
import com.myinstagram.security.service.AuthenticationService;
import com.myinstagram.security.service.CustomUserDetailsService;
import com.myinstagram.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.myinstagram.domain.enums.RoleType.*;
import static com.myinstagram.util.DtoDataFixture.createRoleDto;
import static com.myinstagram.util.RequestDataFixture.createRoleRequest;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "user", password = "password")
@WebMvcTest(RoleController.class)
public class RoleControllerTestSuite {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;
    @MockBean
    private RoleFacade roleFacade;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private AuthenticationService authenticationService;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("/roles | GET")
    public void shouldGetRoles() throws Exception {
        RoleDto firstRoleDto = createRoleDto(MODERATOR).toBuilder().id(1L).build();
        RoleDto secondRoleDto = createRoleDto(ADMIN).toBuilder().id(2L).build();
        RoleDto thirdRoleDto = createRoleDto(USER).toBuilder().id(3L).build();
        ResponseEntity<List<RoleDto>> responseEntities = new ResponseEntity<>(
                List.of(firstRoleDto, secondRoleDto, thirdRoleDto), OK);
        //GIVEN
        given(roleFacade.getRoles()).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/roles"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].roleType", is(MODERATOR.toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].roleType", is(ADMIN.toString())))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].roleType", is(USER.toString())));
    }

    @Test
    @DisplayName("/roles | GET")
    public void shouldGetEmptyRoles() throws Exception {
        ResponseEntity<List<RoleDto>> responseEntities = new ResponseEntity<>(List.of(), OK);
        //GIVEN
        given(roleFacade.getRoles()).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/roles"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("/roles/{login} | GET")
    public void shouldGetRolesByLogin() throws Exception {
        RoleDto firstRoleDto = createRoleDto(MODERATOR).toBuilder().id(1L).build();
        RoleDto secondRoleDto = createRoleDto(ADMIN).toBuilder().id(2L).build();
        RoleDto thirdRoleDto = createRoleDto(USER).toBuilder().id(3L).build();
        ResponseEntity<List<RoleDto>> responseEntities = new ResponseEntity<>(
                List.of(firstRoleDto, secondRoleDto, thirdRoleDto), OK);
        //GIVEN
        given(roleFacade.getRolesByLogin("login")).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/roles/login"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].roleType", is(MODERATOR.toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].roleType", is(ADMIN.toString())))
                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].roleType", is(USER.toString())));
    }

    @Test
    @DisplayName("/roles/{login} | GET")
    public void shouldGetEmptyRolesByLogin() throws Exception {
        ResponseEntity<List<RoleDto>> responseEntities = new ResponseEntity<>(List.of(), OK);
        //GIVEN
        given(roleFacade.getRolesByLogin("login")).willReturn(responseEntities);
        //WHEN & THEN
        mockMvc.perform(get("/roles/login"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("/roles/id/{id} | GET")
    public void shouldGetRoleById() throws Exception {
        //GIVEN
        RoleDto roleDto = createRoleDto(MODERATOR).toBuilder().id(1L).build();
        ResponseEntity<RoleDto> responseEntity = new ResponseEntity<>(roleDto, OK);
        given(roleFacade.getRoleById(1L)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(get("/roles/id/1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.roleType", is(MODERATOR.toString())));
    }

    @Test
    @DisplayName("/roles/assign | POST")
    public void shouldAssignRoleToUser() throws Exception {
        //GIVEN
        RoleRequest roleRequest = createRoleRequest(MODERATOR);
        RoleDto roleDto = createRoleDto(MODERATOR).toBuilder().id(1L).build();
        ResponseEntity<RoleDto> responseEntity = new ResponseEntity<>(roleDto, OK);
        String jsonContent = gson.toJson(roleRequest);
        given(roleFacade.assignRoleToUser(roleRequest)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(post("/roles/assign")
                                .contentType(APPLICATION_JSON_VALUE)
                                .characterEncoding("UTF-8")
                                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.roleType", is(MODERATOR.toString())));
    }

    @Test
    @DisplayName("/roles/{id} | DELETE")
    public void shouldDeleteRoleById() throws Exception {
        //GIVEN
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Role Deleted Successfully!", OK);
        given(roleFacade.deleteRoleById(1L)).willReturn(responseEntity);
        //WHEN & THEN
        mockMvc.perform(delete("/roles/1"))
                .andExpect(status().is(200))
                .andExpect(content().string("Role Deleted Successfully!"));
    }
}
