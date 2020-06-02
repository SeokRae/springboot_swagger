package com.user.swagger.controller;

import com.user.swagger.advice.exception.CUserNotFoundException;
import com.user.swagger.config.response.CommonResult;
import com.user.swagger.config.response.SingleResult;
import com.user.swagger.domain.user.User;
import com.user.swagger.domain.user.UserRepository;
import com.user.swagger.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 해당 클래스가 Swagger 리소스라는 것을 명시
@Api(value = "UserController v1", tags = {"1. User"}, description = "사용자 정보 수정 API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/")
public class UserController {

    private final UserRepository userRepository;
    private final ResponseService responseService; // 결과를 처리할 Service

    @ApiOperation(value = "모든 회원 조회", notes = "모든 회원을 조회한다")
    @GetMapping(value = "/user")
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @ApiOperation(value = "회원 조회",notes = "userId로 회원을 조회한다.")
    @GetMapping(value = "/user/{userId}")
    public SingleResult<User> findUserById(@ApiParam(value = "회원ID", required = true) @PathVariable long userId) {
        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userRepository.findById(userId).orElseThrow(CUserNotFoundException::new));
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public User save(@ApiParam(value = "회원아이디", required = true, example = "1") @RequestParam String userId,
                     @ApiParam(value = "회원이름", required = true, example = "이름") @RequestParam String name) {
        User user = User.builder()
                .userId(userId)
                .name(name)
                .build();
        return userRepository.save(user);
    }

    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<User> update(
            @ApiParam(value = "회원번호", required = true) @RequestParam long msrl,
            @ApiParam(value = "회원아이디", required = true) @RequestParam String userId,
            @ApiParam(value = "회원이름", required = true) @RequestParam String name) {

        User user = User.builder()
                .msrl(msrl)
                .userId(userId)
                .name(name)
                .build();
        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiOperation(value = "회원 삭제", notes = "userId로 회원정보를 삭제한다")
    @DeleteMapping(value = "/user/{userId}")
    public CommonResult delete(
            @ApiParam(value = "회원번호", required = true) @PathVariable long userId) {
        userRepository.deleteById(userId);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }
}