package com.lpb.mid.controller;


import com.lpb.mid.dto.JwtResponse;
import com.lpb.mid.dto.ResponseObject;
import com.lpb.mid.exception.ErrorMessage;
import com.lpb.mid.model.request.LoginRequest;
import com.lpb.mid.model.response.JwtDTOResponse;
import com.lpb.mid.sercurity.jwt.JwtTokenUtil;
import com.lpb.mid.service.RefreshTokenService;
import com.lpb.mid.service.UserPrincipal;
import com.lpb.mid.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;
    @Autowired
    RefreshTokenService refreshTokenService;


    @PostMapping("/login")

    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody @Valid LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            JwtDTOResponse jwtDTOResponse = jwtTokenUtil.generateJwtToken(authentication,loginRequest);

            UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userService.checkRequest(request, loginRequest,userDetails);
            refreshTokenService.createRefreshToken(userDetails.getId(), jwtDTOResponse.getJwt());

            return ResponseObject.successLogin(new JwtResponse(
                    jwtDTOResponse.getJwt(),
                    jwtDTOResponse.getExp()
            ));
        } catch (BadCredentialsException e) {
            return ResponseObject.unauthorized(ErrorMessage.INCORRECT_USERNAME_PASSWORD.getMessage());
        }
    }
    @GetMapping("/getInfo")
    public ResponseEntity<?> getInfo() {
        return ResponseEntity.ok().body(userService.getInfo());
    }

}
