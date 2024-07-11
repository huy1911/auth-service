package com.lpb.mid.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotEmpty(message = "username không được để trống ")
    private String username;
    @NotEmpty(message = "password không được để trống ")
    private String password;
    private String customerNo;
    private String branchCode;
    @NotEmpty(message = "mac không được để trống ")
    private String mac;
}
