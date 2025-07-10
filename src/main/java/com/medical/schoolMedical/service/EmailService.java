package com.medical.schoolMedical.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Async
    public void sendOtpEmail(String to, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject("Your OTP Code");
//        from thì tự động lấy trong câú hình

//        Chuyển đổi OTP qua bên thymeleaf(HTML)
        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("appName", "School Medical Management System");

//        Render HTML template
        String htmlContent = templateEngine.process("common/otp-email", context);
        helper.setText(htmlContent, true); // true để gửi dưới dạng HTML

        mailSender.send(mimeMessage);
    }

}
