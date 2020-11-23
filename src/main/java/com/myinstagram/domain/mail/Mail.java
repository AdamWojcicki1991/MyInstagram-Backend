package com.myinstagram.domain.mail;

public final class Mail {
    private final String mailTo;
    private final String subject;
    private final String text;

    public static class MailBuilder {
        private String mailTo;
        private String subject;
        private String text;

        public MailBuilder mailTo(String mailTo) {
            this.mailTo = mailTo;
            return this;
        }

        public MailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailBuilder text(String text) {
            this.text = text;
            return this;
        }

        public Mail build() {
            return new Mail(mailTo, subject, text);
        }
    }

    private Mail(final String mailTo, final String subject, final String text) {
        this.mailTo = mailTo;
        this.subject = subject;
        this.text = text;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "mailTo='" + mailTo + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
