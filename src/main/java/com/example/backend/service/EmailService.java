package com.example.backend.service;


import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.example.backend.mapper.EmailMapper;
import com.example.backend.model.MailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static com.example.backend.controller.exception.ErrorType.MAIL_AUTHENTICATE_CODE_FAIL;

@Service
@RequiredArgsConstructor
@ComponentScan(basePackages = "org.springframework.boot.autoconfigure.thymeleaf")
public class EmailService {

    private final EmailMapper emailMapper;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine; //인텔리제이 버그터진거 정상적으로 잘나오면 진행하면된다.

    public void save(String username, String authCode) {
        boolean result = emailMapper.findAuthenticationCode(username).isPresent();
        if (result) emailMapper.update(username,authCode);
        else emailMapper.save(username,authCode);
    }

    @Async("signUpEmailSender")
    public void sendMail(MailForm form) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(new InternetAddress(form.getFrom(),"meatTeam"));
            message.addRecipients(MimeMessage.RecipientType.TO,form.getTo()); //보낼 사람
            message.setSubject(form.getSubject()); //타이틀
            message.setText(setContext(form.getText()),"utf-8","html"); //내용
            mailSender.send(message);
        }catch (Exception ignored){
            throw new CustomException(ErrorType.MAIL_SEND_FAIL);
        }
    }
    public String setContext(String authCode){
        Context context = new Context();
        context.setVariable("authCode",authCode); //데이터
        return templateEngine.process("email/signup_templates",context);
    }

    public void isExist(String username,String emailAuthCode){
        emailMapper.findAuth(username,emailAuthCode).orElseThrow(()->new CustomException(MAIL_AUTHENTICATE_CODE_FAIL));
    }

}
