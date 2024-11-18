package com.rao.userservice.controller;

import com.rao.common.exception.ParameterErrorException;
import com.rao.common.util.Result;
import com.rao.userservice.anno.LogAnno;
import com.rao.userservice.service.UserService;
import com.rao.userservice.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-service")
public class AdminController {

    //TODO 查看用户管理日志

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/api/user/list")
    public Result<?> getPaginateUserList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "20") Integer size) {
        List<UserVo> userVoList = userService.getUserList(page, size);
        return Result.OK(userVoList);
    }

    @LogAnno
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/user/{userId}/reset/password")
    public Result<?> resetPassword(@PathVariable String userId) {
        userService.resetPassword(userId);
        return Result.OK(null);
    }

    @LogAnno
    @PreAuthorize("hasRole('ROLE_ROOT')")
    @PutMapping("/api/user/set-admin")
    public Result<?> setAdmin(@RequestParam String email, @RequestParam Integer role) {
        if (role != 1 && role != 2) throw new ParameterErrorException();
        userService.setAdmin(email, role);
        return Result.OK(null);
    }
}
