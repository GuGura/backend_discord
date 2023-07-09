package com.example.backend.controller;

import com.example.backend.controller.exception.CustomException;
import com.example.backend.controller.exception.ErrorType;
import com.example.backend.controller.status.SuccessResponse;
import com.example.backend.controller.status.SuccessType;
import com.example.backend.model.UserDTO;
import com.example.backend.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignController {

    private final SignService signService;

    /**
     *  정규식 해야함
     */
    @PostMapping("/signUp")
    public ResponseEntity<?> singUp(@RequestBody Map<String,String> params){
        String[] str = {"username","emailAuthCode","nickname","password"};
        UserDTO userDTO = new UserDTO();
        Arrays.stream(str).forEach(index-> {
            if(params.get(index) == null)
                throw new CustomException(ErrorType.USER_EMPTY);
            userDTO.InputData(index,params.get(index));
        });
        signService.SignUp(userDTO);
        return SuccessResponse.toResponseEntity(SuccessType.SIGNUP_SUCCESS);
    }
}
