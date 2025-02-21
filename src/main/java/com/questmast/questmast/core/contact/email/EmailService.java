package com.questmast.questmast.core.contact.email;

import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import com.questmast.questmast.common.exception.type.FieldNotValidException;
import com.questmast.questmast.core.enums.PersonRole;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailService {

    // https://app.abstractapi.com/
    private static final String ABSTRACT_API_KEY = "5756e145c71f4bd68088f225e200df3d";
    private final JavaMailSender javaMailSender;

    public String getValidEmail(@NotBlank String email) throws IOException, FieldNotValidException {
        if(Boolean.TRUE.equals(isEmailValid(email))) {
            return email;
        } else {
            throw new FieldNotValidException("Email", email);
        }
    }

    public Boolean isEmailValid(String email) throws IOException {
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

    public void sendRegistrationVerificationEmail(PersonRole personRole, String dstEmail) {
        String role = "student";
        if(personRole.equals(PersonRole.ROLE_ADMIN)) role = "admin";
        if(personRole.equals(PersonRole.ROLE_CONTENT_MODERATOR)) role = "content-moderator";

        String apiEndpointUrl = "/api/authentication/verify-email/" + role + "/" + dstEmail;

        String link = "http://localhost:8080" + apiEndpointUrl;

        String emailBody = """
        <html>
            <body>
                <p>Olá,</p>
                <p>Clique no botão abaixo para verificar seu e-mail:</p>
                <form action="%s" method="POST">
                    <button type="submit" style="background-color: #4CAF50; color: white; border: none; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;">Verificar E-mail</button>
                </form>
                <p>Se você não solicitou este e-mail, pode ignorá-lo.</p>
            </body>
        </html>
        """.formatted(link);

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(dstEmail);
            helper.setSubject("Bem-vindo(a) ao QuestMast!");
            helper.setText(emailBody, true);

            javaMailSender.send(mimeMessage);
            log.info("Email sent!");
        } catch (Exception e) {
            log.info("Email not sent! {}", e.getMessage());
        }
    }
}
