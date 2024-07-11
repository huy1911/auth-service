package com.lpb.mid.service;

import com.lpb.mid.dto.ResponseDto;
import com.lpb.mid.dto.UserDto;
import com.lpb.mid.entity.Permision;
import com.lpb.mid.entity.Users;
import com.lpb.mid.exception.ErrorMessage;
import com.lpb.mid.exception.ExceptionHandler;
import com.lpb.mid.model.request.LoginRequest;
import com.lpb.mid.repo.RoleRepository;
import com.lpb.mid.repo.UserRepository;
import com.lpb.mid.utils.Constants;
import com.lpb.mid.utils.Convert;
import com.lpb.mid.utils.HmacUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUserNameAndStatus(username, Constants.ACTIVE_USER);
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (ObjectUtils.isEmpty(user)) {
            throw new ExceptionHandler(ErrorMessage.ERR_29);
        }
        List<Permision> roles = roleRepository.listRoleByUser(user.getId());
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));

        return UserPrincipal.build(user, authorities);
    }

    public void checkRequest(HttpServletRequest request, LoginRequest loginRequest, UserPrincipal userDetails) {
        List<String> strings = new ArrayList<>();
        strings.add(request.getHeader(Constants.X_SOURCE_ENV));
        strings.add(request.getHeader(Constants.X_REFERENCE_ID));
        strings.add(loginRequest.getUsername());
        strings.add(loginRequest.getPassword());
        strings.add(loginRequest.getCustomerNo());
        strings.add(loginRequest.getBranchCode());
        String encodeReq = HmacUtil.genHmac(Convert.getReq(strings), userDetails.getSecretKey());
        log.info("loadUserByUsername : encode req Hmac ----->{} by userName ---->{}", encodeReq, loginRequest.getUsername());
        if (!encodeReq.equals(loginRequest.getMac())) {
            log.error("checkRequest : mac fail user ---->{}", loginRequest.getUsername());
            throw new ExceptionHandler(ErrorMessage.ERR_21);
        }
    }

    public ResponseDto<?> getInfo() {
        UserPrincipal userDetails = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(StringUtils.isEmpty(userDetails.getUsername())){
            log.error("getInfo : userName not fount!!!!");
            throw new ExceptionHandler(ErrorMessage.ERR_20);
        }
        Users userInfo = userRepository.findByUserNameAndStatus(userDetails.getUsername(),Constants.ACTIVE_USER);
        if (ObjectUtils.isEmpty(userInfo)) {
            log.error("getInfo : userInfo not fount!!!!");
            throw new ExceptionHandler(ErrorMessage.ERR_29);
        }
        UserDto userDto = UserDto.builder()
                .userName(userInfo.getUserName())
                .appName(userInfo.getAppName())
                .channel(userInfo.getChannel())
                .inputter(userInfo.getInputter())
                .secretKey(userInfo.getSecretKey())
                .status(userInfo.getStatus())
                .whiteListIp(userInfo.getWhiteListIp()).build();
        return ResponseDto.builder()
                .type(ErrorMessage.ERR_000.type)
                .statusCode(ErrorMessage.ERR_000.code)
                .description(ErrorMessage.ERR_000.message)
                .data(userDto)
                .build();
    }

}
