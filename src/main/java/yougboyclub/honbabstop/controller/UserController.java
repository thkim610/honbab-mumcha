package yougboyclub.honbabstop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import yougboyclub.honbabstop.dto.RequestEmailCodeVerificationDto;
import yougboyclub.honbabstop.dto.RequestUserDto;
import yougboyclub.honbabstop.dto.RequestUserEmailDto;
import yougboyclub.honbabstop.service.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    //이메일 인증코드 발송
    //BindingResult: 스프링 프레임워크에서 사용되는 유효성 검사(validation) 결과를 수신하고 오류 메시지를 처리하는 인터페이스
    @PostMapping("/emails/authenticationRequest")
    public ResponseEntity<String> sendMessage(@RequestBody @Validated RequestUserEmailDto toEmailDto, BindingResult bindingResult) {

        //입력받은 이메일이 빈칸이 있거나 이메일형식이 아닐 시, 에러 반환.
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 형식이 맞지 않습니다.");
        }

        String toEmail = toEmailDto.getEmail();
        userService.sendCodeToEmail(toEmail);

        return ResponseEntity.status(HttpStatus.OK).body("인증코드가 발송되었습니다.");
    }

    //인증코드 인증 확인
    @PostMapping("/emails/codeChecked")
    public ResponseEntity<String> verificationEmailCode(@RequestBody RequestEmailCodeVerificationDto verificationDto) {
        userService.verifiedCode(verificationDto.getEmail(), verificationDto.getAuthCode());

        return ResponseEntity.status(HttpStatus.OK).body("인증이 완료되었습니다."); //200
    }

    //회원가입
    @PostMapping("/new")
    public ResponseEntity<String> join(@RequestBody RequestUserDto dto){
        System.out.println("dto = " + dto);

        userService.save(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 성공하였습니다.");
    }
}