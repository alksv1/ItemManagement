package com.rao.userservice.controller;

import com.rao.common.util.JwtUtil;
import com.rao.common.util.RequestHeaderUtil;
import com.rao.common.util.Result;
import com.rao.userservice.dto.ChangePasswordDto;
import com.rao.userservice.dto.PasswordLoginDto;
import com.rao.userservice.service.UserService;
import com.rao.userservice.vo.UserVo;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-service")
public class UserController {

    private final UserService userService;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @GetMapping("/api/captcha/email")
    public Result<?> getEmailCaptcha(@Param("email") String email) {
        String key = userService.sendEmailCaptcha(email);
        return Result.OK(key);
    }

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @PostMapping("/api/login/email")
    public Result<?> loginByEmail(@Param("email") String email, @Param("captcha") String captcha, @RequestBody String encodeCode) {
        String token = userService.loginByEmail(email, captcha, encodeCode);
        if (token == null)
            return Result.captchaExpireError();
        return Result.OK(token);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/api/user/password")
    public Result<?> changeUserPassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changeUserPassword(changePasswordDto);
        return Result.OK(null);
    }

    @PreAuthorize("hasRole('ROLE_GUEST')")
    @PostMapping("/api/login/password")
    public Result<?> loginByPassword(@Valid @RequestBody PasswordLoginDto passwordLoginDto) {
        String token = userService.loginByPassword(passwordLoginDto);
        return Result.OK(token);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/user/me")
    public Result<?> getCurrentUserInfo() {
        String userId = jwtUtil.getUserIdFromToken(RequestHeaderUtil.getToken());
        UserVo userVo = userService.getUserInfoById(userId);
        return Result.OK(userVo);
    }
}
