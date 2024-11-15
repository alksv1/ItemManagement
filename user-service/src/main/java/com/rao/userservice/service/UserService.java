package com.rao.userservice.service;


import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.rao.common.exception.AccountOrPasswordErrorException;
import com.rao.common.exception.InternalErrorException;
import com.rao.common.exception.ParameterErrorException;
import com.rao.common.exception.ResourceNotFoundException;
import com.rao.common.util.Captcha;
import com.rao.common.util.JwtUtil;
import com.rao.userservice.dto.ChangePasswordDto;
import com.rao.userservice.dto.PasswordLoginDto;
import com.rao.userservice.mapper.UserMapper;
import com.rao.userservice.po.UserPo;
import com.rao.userservice.util.AESUtil;
import com.rao.userservice.util.EmailUtil;
import com.rao.userservice.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;

    private final EmailUtil emailUtil;

    private final AESUtil aesUtil;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserMapper userMapper,
                       EmailUtil emailUtil,
                       AESUtil aesUtil,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       ModelMapper modelMapper) {
        this.userMapper = userMapper;
        this.emailUtil = emailUtil;
        this.aesUtil = aesUtil;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    public String sendEmailCaptcha(String email) {
        String captcha = Captcha.generateSixDigit();
        String s;
        try {
            s = aesUtil.generateEncryptString(email, captcha);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalErrorException();
        }
        emailUtil.sendMail(email, captcha);
        return s;
    }

    public String loginByEmail(String email, String captcha, String encodeCode) {
        String json;
        try {
            json = aesUtil.decrypt(encodeCode);
        } catch (Exception ex) {
            throw new ParameterErrorException();
        }
        ParsedData parsed = JSON.parseObject(json, ParsedData.class);
        if (!email.equals(parsed.getEmail()) ||
                !captcha.equals(parsed.getCaptcha()))
            throw new AccountOrPasswordErrorException();
        if (LocalDateTime.now().isAfter(parsed.getExpireTime()))
            return null;
        UserPo userPo = userMapper.getUserByEmail(email);
        if (userPo == null) {
            String id = UUID.randomUUID().toString();
            userMapper.insertUser(id, email);
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", id);
            claims.put("version", 0);
            return jwtUtil.generateToken(claims);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userPo.getId());
        claims.put("version", userPo.getVersion());
        return jwtUtil.generateToken(claims);
    }

    public void changeUserPassword(ChangePasswordDto changePasswordDto) {
        UserPo userPo = userMapper.getUserByEmail(changePasswordDto.getEmail());
        if (userPo == null || (userPo.getPassword() != null && !passwordEncoder.matches(changePasswordDto.getOriginalPassword(), userPo.getPassword())))
            throw new AccountOrPasswordErrorException();
        UserPo userPo1 = new UserPo();
        userPo1.setId(userPo.getId());
        userPo1.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userMapper.updateUserInfo(userPo1);
    }

    public UserVo getUserInfoById(String userId) {
        UserPo userPo = userMapper.getUserById(userId);
        return modelMapper.map(userPo, UserVo.class);
    }

    public String loginByPassword(PasswordLoginDto passwordLoginDto) {
        UserPo userPo = userMapper.getUserByEmail(passwordLoginDto.getAccount());
        if (userPo == null) throw new ResourceNotFoundException();
        if (!passwordEncoder.matches(passwordLoginDto.getPassword(), userPo.getPassword()))
            throw new AccountOrPasswordErrorException();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userPo.getId());
        claims.put("version", userPo.getVersion());
        return jwtUtil.generateToken(claims);
    }

    public List<UserVo> getUserList(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<UserPo> userPoList = userMapper.getUserList();
        return userPoList.stream()
                .map(userPo -> modelMapper.map(userPo, UserVo.class))
                .toList();
    }

    public void resetPassword(String userId) {
        UserPo userPo = new UserPo();
        userPo.setId(userId);
        userPo.setPassword(passwordEncoder.encode("123456"));
        userMapper.updateUserInfo(userPo);
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ParsedData {
    private String email;
    private String captcha;
    private LocalDateTime issueTime;
    private LocalDateTime expireTime;
}
