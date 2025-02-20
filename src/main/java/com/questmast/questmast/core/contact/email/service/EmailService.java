package com.questmast.questmast.core.contact.email.service;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import com.questmast.questmast.common.exception.domain.EmailNotValidException;
import com.questmast.questmast.common.exception.domain.InvalidContactException;
import com.questmast.questmast.core.contact.email.domain.dto.EmailFormDTO;
import com.questmast.questmast.core.contact.email.domain.entity.Email;
import com.questmast.questmast.core.contact.email.repository.EmailRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private static final String ABSTRACT_API_KEY = "5756e145c71f4bd68088f225e200df3d";


    public Email getValidEmail(@NotBlank String email) throws Exception {
        if(isEmailValid(email)) {
            Email validEmail = new Email(email);
            return validEmail;
        } else {
            throw new EmailNotValidException(email);
        }
    }

    public Boolean isEmailValid(String email) throws Exception {
        String urlString = String.format("https://emailvalidation.abstractapi.com/v1/?api_key=%s&email=%s", ABSTRACT_API_KEY, email);
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();

            boolean isValidFormat = jsonResponse.get("is_valid_format").getAsJsonObject().get("value").getAsBoolean();
            boolean isMxFound = jsonResponse.get("is_mx_found").getAsJsonObject().get("value").getAsBoolean();
            boolean isSmtpValid = jsonResponse.get("is_smtp_valid").getAsJsonObject().get("value").getAsBoolean();

            return isValidFormat && isMxFound && isSmtpValid;
        }
    }
}
