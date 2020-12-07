package com.myinstagram.util;

import com.myinstagram.domain.auth.AuthenticationResponse;
import com.myinstagram.domain.auth.LoginRequest;
import com.myinstagram.domain.auth.RefreshTokenRequest;
import com.myinstagram.domain.auth.RegisterRequest;
import com.myinstagram.domain.dto.*;
import com.myinstagram.domain.entity.*;
import com.myinstagram.domain.enums.RoleType;
import com.myinstagram.domain.mail.Mail;
import com.myinstagram.mailboxlayer.dto.ValidateMailResponseDto;
import com.myinstagram.openweather.dto.OpenWeatherMainDto;
import com.myinstagram.openweather.dto.OpenWeatherResponse;
import com.myinstagram.openweather.dto.OpenWeatherResponseDto;
import com.myinstagram.openweather.dto.OpenWeatherWeatherDto;
import org.springframework.mail.SimpleMailMessage;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.myinstagram.domain.enums.UserStatus.ACTIVE;

public final class DataFixture {

    private DataFixture() {
    }

    public static User createUser(final String login, final String mail) {
        return User.builder()
                .userName("User")
                .login(login)
                .password("Password")
                .email(mail)
                .city("Poznan")
                .description("Description")
                .createDate(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .updateDate(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .userStatus(ACTIVE)
                .enabled(true)
                .posts(new ArrayList<>())
                .roles(new HashSet<>())
                .build();
    }

    public static Post createPost(final User user, final Instant postDate) {
        return Post.builder()
                .postName("Post")
                .caption("Sign")
                .url("URL")
                .imageSerialNumber(0L)
                .likesCount(0L)
                .postDate(postDate)
                .updateDate(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .user(user)
                .comments(new ArrayList<>())
                .build();
    }

    public static Comment createComment(final Post post) {
        return Comment.builder()
                .commentName("Comment")
                .content("Content")
                .commentDate(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .updateDate(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .post(post)
                .build();
    }

    public static Role createRole(final RoleType roleType, final User... users) {
        return Role.builder()
                .roleType(roleType)
                .users(new HashSet<>(Set.of(users)))
                .build();
    }

    public static RefreshToken createRefreshToken(final String token) {
        return RefreshToken.builder()
                .token(token)
                .createdDate(Instant.now())
                .build();
    }

    public static VerificationToken createVerificationToken(final User user, final String token) {
        return VerificationToken.builder()
                .token(token)
                .user(user)
                .expirationDate(Instant.now())
                .build();
    }

    public static User updateUser(final User user, final UserRequest userRequest) {
        return user.toBuilder()
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .description(userRequest.getDescription())
                .updateDate(Instant.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    }

    public static Mail createMail() {
        return new Mail.MailBuilder()
                .mailTo("email@gmail.com")
                .subject("Test Subject")
                .text("Test text")
                .build();
    }

    public static SimpleMailMessage getSimpleMailMessage(final Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail.getMailTo());
        mailMessage.setSubject(mail.getSubject());
        mailMessage.setText(mail.getText());
        return mailMessage;
    }

    public static OpenWeatherResponseDto createOpenWeatherResponseDto() {
        return OpenWeatherResponseDto.builder()
                .city("Paris")
                .temperature(16)
                .feltTemperature(12.2)
                .pressure(1000)
                .humidity(12)
                .mainWeather("Test Weather")
                .weatherDescription("Test Description")
                .build();
    }

    public static CommentRequest createCommentRequest() {
        return CommentRequest.builder()
                .login("Test Comment")
                .content("Test Content")
                .build();
    }

    public static UpdateCommentRequest createUpdateCommentRequest(final Comment comment) {
        return UpdateCommentRequest.builder()
                .commentId(comment.getId())
                .commentName("Test Comment")
                .content("Test Content")
                .build();
    }

    public static PostRequest createPostRequest(final String postName, final String caption) {
        return PostRequest.builder()
                .postName(postName)
                .caption(caption)
                .url("Test url")
                .build();
    }

    public static UpdatePostRequest createUpdatePostRequest() {
        return UpdatePostRequest.builder()
                .postName("Test Post")
                .caption("Test caption")
                .build();
    }

    public static UserRequest createUserRequest(final User user) {
        return UserRequest.builder()
                .userId(user.getId())
                .userName("Test User")
                .email("test@gmail.com")
                .description("Test Description")
                .build();
    }

    public static User updateUserPassword(final User user, final String encryptedPassword) {
        return user.toBuilder().password(encryptedPassword).build();
    }

    public static PasswordRequest createPasswordRequest(final String currentPassword, final String confirmPassword, final String newPassword) {
        return PasswordRequest.builder()
                .currentPassword(currentPassword)
                .confirmPassword(confirmPassword)
                .newPassword(newPassword)
                .build();
    }

    public static RegisterRequest createRegisterRequest(final String name, final String login,
                                                        final String city, final String email, final String password) {
        return RegisterRequest.builder()
                .name(name)
                .login(login)
                .city(city)
                .email(email)
                .password(password)
                .build();
    }

    public static RoleRequest createRoleRequest(final RoleType roleType) {
        return RoleRequest.builder()
                .login("login")
                .roleType(roleType)
                .build();
    }

    public static LoginRequest createLoginRequest() {
        return LoginRequest.builder()
                .login("login")
                .password("password")
                .build();
    }

    public static AuthenticationResponse createAuthenticationResponse() {
        return AuthenticationResponse.builder()
                .login("login")
                .authenticationToken("authenticationToken")
                .expiresAt(Instant.now())
                .refreshToken("refreshToken")
                .build();
    }

    public static RefreshTokenRequest createRefreshTokenRequest() {
        return RefreshTokenRequest.builder()
                .login("login")
                .refreshToken("refreshToken")
                .build();
    }

    public static OpenWeatherMainDto createOpenWeatherMainDto() {
        return OpenWeatherMainDto.builder()
                .temperature(12.1)
                .feltTemperature(12.3)
                .humidity(60)
                .pressure(999)
                .build();
    }

    public static OpenWeatherWeatherDto createOpenWeatherWeatherDto() {
        return OpenWeatherWeatherDto.builder()
                .mainWeather("Cloud")
                .weatherDescription("Cloudy weather")
                .build();
    }

    public static OpenWeatherResponse createOpenWeatherResponse(final OpenWeatherMainDto openWeatherMainDto, final OpenWeatherWeatherDto openWeatherWeatherDto) {
        OpenWeatherWeatherDto[] openWeatherWeathers = {openWeatherWeatherDto};
        return OpenWeatherResponse.builder()
                .city("Poznan")
                .openWeatherWeatherDto(openWeatherWeathers)
                .openWeatherMainDto(openWeatherMainDto)
                .build();
    }

    public static ValidateMailResponseDto createValidateResponseDto(final boolean emailFormatValid,
                                                                    final boolean mxRecordsFound,
                                                                    final boolean smtpValid) {
        return ValidateMailResponseDto.builder()
                .validatedEmail("test@gmail.com")
                .emailFormatValid(emailFormatValid)
                .mxRecordsFound(mxRecordsFound)
                .smtpValid(smtpValid)
                .build();
    }
}
