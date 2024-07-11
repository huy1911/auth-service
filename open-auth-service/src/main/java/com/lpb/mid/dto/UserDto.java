package com.lpb.mid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userName;
    private String channel;
    private String secretKey;
    private String appName;
    private String whiteListIp;
    private String inputter;
    private String status;
}
