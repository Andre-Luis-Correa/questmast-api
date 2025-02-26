package com.questmast.questmast.core.contact.email;

import com.questmast.questmast.common.exception.type.EmailException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendRegistrationVerificationEmail(String dstEmail, String verificationEmailCode) {
        String link = "http://localhost:5173/register-verification/" + dstEmail + ":" + verificationEmailCode;

        String emailBody = """
                <html>
                    <body>
                        <p>Olá,</p>
                        <p>Clique no botão abaixo para verificar seu e-mail:</p>
                        
                        <a href="%s"\s
                           style="background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center;\s
                                  text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px;\s
                                  cursor: pointer; border-radius: 4px;">
                            Verificar E-mail
                        </a>

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
            log.error("Email not sent! {}", e.getMessage());
            throw new EmailException(dstEmail);
        }
    }

    public void sendResetPasswordCodeEmail(String email, String resetPasswordCode) {
        String frontEndUrl = "http://localhost:5173/reset-password/" + email + ":" + resetPasswordCode;

        String emailBody = """
                <html>
                    <body>
                        <p>Olá,</p>
                        <p>Clique no botão abaixo para redefinir sua senha (o link expira em 20 minutos):</p>
                        
                        <a href="%s"
                           style="background-color: #4CAF50;
                                  color: white;
                                  padding: 10px 20px;
                                  text-align: center;
                                  text-decoration: none;
                                  display: inline-block;
                                  font-size: 16px;
                                  margin: 4px 2px;
                                  cursor: pointer;">
                            Redefinir Senha
                        </a>
                        
                        <p>Se você não solicitou uma redefinição de senha, ignore este e-mail.</p>
                        <p>Este link é válido por 20 minutos.</p>
                    </body>
                </html>
                """.formatted(frontEndUrl);


        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Redefinição de Senha - QuestMast");
            helper.setText(emailBody, true);

            javaMailSender.send(mimeMessage);
            log.info("Email sent!");
        } catch (Exception e) {
            log.error("Email not sent! {}", e.getMessage());
            throw new EmailException(email);
        }
    }

}
