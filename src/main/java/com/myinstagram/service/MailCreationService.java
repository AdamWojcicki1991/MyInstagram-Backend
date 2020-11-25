package com.myinstagram.service;

import com.myinstagram.config.InstagramConfig;
import com.myinstagram.domain.entity.User;
import com.myinstagram.domain.mail.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailCreationService {
    private final Environment environment;
    private final TemplateEngine templateEngine;
    private final InstagramConfig instagramConfig;

    public String createNewUserEmail(final User user, final String password) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("password", password);
        context.setVariable("instagram_config", instagramConfig);
        return templateEngine.process("newUserEmailTemplate", context);
    }

    public String createResetPasswordEmail(final User user, final String password) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("password", password);
        context.setVariable("instagram_config", instagramConfig);
        return templateEngine.process("resetPasswordEmailTemplate", context);
    }

    public String createUpdateUserProfileEmail(final User user) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("instagram_config", instagramConfig);
        return templateEngine.process("updateUserProfileEmailTemplate", context);
    }

    public MimeMessagePreparator createMimeMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setPriority(1);
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setSubject(mail.getSubject());
            mimeMessageHelper.setText(mail.getText(), true);
            mimeMessageHelper.setFrom(new InternetAddress(Objects.requireNonNull(environment.getProperty("support.mail"))));
        };
    }
}
