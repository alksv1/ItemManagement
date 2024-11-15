package com.rao.userservice.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPo {
    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createTime;
    private Integer role;
    private Integer version;
}
