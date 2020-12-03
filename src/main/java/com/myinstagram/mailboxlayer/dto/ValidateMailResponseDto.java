package com.myinstagram.mailboxlayer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ValidateMailResponseDto {
    @JsonProperty("email")
    private String validatedEmail;
    @JsonProperty("format_valid")
    private boolean emailFormatValid;
    @JsonProperty("mx_found")
    private boolean mxRecordsFound;
    @JsonProperty("smtp_check")
    private boolean smtpValid;
}
