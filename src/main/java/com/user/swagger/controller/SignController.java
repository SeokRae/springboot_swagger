package com.user.swagger.controller;

import com.user.swagger.advice.exception.CEmailSignInFailedException;
import com.user.swagger.advice.exception.CUserDuplicatedUserIdException;
import com.user.swagger.config.response.CommonResult;
import com.user.swagger.config.response.SingleResult;
import com.user.swagger.config.security.JwtTokenProvider;
import com.user.swagger.domain.user.User;
import com.user.swagger.domain.user.UserRepository;
import com.user.swagger.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
@Api(value = "SignController v1", tags = {"3. Sign"}, description = "회원 접근관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(
            @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
        User user = userRepository.findByUid(id).orElseThrow(CEmailSignInFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CEmailSignInFailedException();

        return responseService.getSingleResult(
                jwtTokenProvider.createToken(
                        String.valueOf(user.getMsrl()),
                        user.getRoles())
        );
    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signin(
            @ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
            @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
            @ApiParam(value = "이름", required = true) @RequestParam String name) {
        // user 존재
        User user = userRepository.findByUid(id).orElseThrow(CUserDuplicatedUserIdException::new);
        userRepository.save(User.builder()
                .uid(id)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return responseService.getSuccessResult();
    }
}