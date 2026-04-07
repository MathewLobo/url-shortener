package com.mathew.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ShortenRequest {

    @NotBlank(message = "URL cannot be blank")
    @Pattern(
        regexp = "^(https?://)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
        message = "Please enter a valid URL starting with http:// or https://"
    )
    private String url;

    private int expiryDays = 0;
}