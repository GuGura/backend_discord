package com.example.backend.controller;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.example.backend.controller.status.SuccessResponse;
import com.example.backend.controller.status.SuccessType;
import com.example.backend.model.MailForm;
import com.example.backend.service.EmailService;
import com.example.backend.service.SignService;
import com.example.backend.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class EmailController {

    private final EmailService emailService;
    private final SignService signService;

    @PostMapping("/sendMail")
    public ResponseEntity<?> sendMail(@RequestBody(required = false) Map<String, String> param) {
        String authCode = CodeGenerator.createCode();
        String username = param.get("username");
        if (username == null || username.equals(""))
            throw new CustomException(ErrorType.USER_EMPTY);
        if (signService.findUserByUsername(username).isPresent())
            throw new CustomException(ErrorType.USER_ALREADY_EXISTS);
        emailService.save(username, authCode);

        MailForm form = new MailForm();
        form.setFrom("meatTeam@gmail.com");
        form.setTo(username);
        form.setSubject("회원 가입 인증 이메일 입니다.");
        form.setText(authCode);
        emailService.sendMail(form);
        return SuccessResponse.toResponseEntity(SuccessType.MAIL_SEND_SUCCESS);
    }

    @PostMapping("/checkMail")
    public ResponseEntity<?> checkMail(@RequestBody Map<String,String> params)  {
        String username =  params.get("username");
        String emailAuthCode = params.get("emailAuthCode");
        emailService.isExist(username,emailAuthCode);
        return SuccessResponse.toResponseEntity(SuccessType.MAIL_AUTHENTICATE_CODE_SUCCESS);
    }
}