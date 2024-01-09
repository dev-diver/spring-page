package com.gang.mypage.controller;

import com.gang.mypage.dto.ResponseDTO;
import com.gang.mypage.dto.UserDTO;
import com.gang.mypage.model.UserAccount;
import com.gang.mypage.security.TokenProvider;
import com.gang.mypage.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            if(userDTO == null || userDTO.getPassword() == null){
                throw new RuntimeException("유효하지 않은 비밀번호");
            }

            log.debug("userDTO {}\n", userDTO);

            // 리퀘스트를 이용해 저장할 유저 만들기
            UserAccount user = UserAccount.builder()
                    .userId(userDTO.getUserId())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    //.authProvider("a")
                    //.role("b")
                    .build();
            // 서비스를 이용해 리파지토리에 유저 저장
            log.debug("user 생성 시작 {}\n", user.toString());

            UserAccount registeredUser = userService.create(user);
            final String token = tokenProvider.create(registeredUser);
            log.debug("user 생성 완료\n");
            UserDTO responseUserDTO = UserDTO.builder()
                    .token(token)
                    .id(registeredUser.id())
                    .userId(registeredUser.userId())
                    .build();
            // 유저 정보는 항상 하나이므로 그냥 리스트로 만들어야하는 ResponseDTO를 사용하지 않고 그냥 UserDTO 리턴.
            return ResponseEntity.ok(responseUserDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.trace(e.getMessage());
            // 예외가 나는 경우 bad 리스폰스 리턴.
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        UserAccount user = userService.getByCredentials(
                userDTO.getUserId(),
                userDTO.getPassword(),
                passwordEncoder);

        if(user != null) {
            // 토큰 생성
            final String token = tokenProvider.create(user);

            //user 생성
            final UserDTO responseUserDTO = UserDTO.builder()
                    .userId(user.userId())
                    .id(user.id())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login 실패.")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}
