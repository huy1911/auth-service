package com.lpb.mid.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lpb.mid.entity.Users;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Data
public class UserPrincipal implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final String userId;

    private String username;

    private String whiteListIp;

    private String appId;
    private String secretKey;

    @JsonIgnore
    private String password;


    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String userId, String username,
                         String appId, String whiteListIp,
                         String password, Collection<? extends GrantedAuthority> authorities,String secretKey) {
        this.userId = userId;
        this.username = username;
        this.appId = appId;
        this.whiteListIp = whiteListIp;
        this.password = password;
        this.authorities = authorities;
        this.secretKey = secretKey;
    }


    public static UserPrincipal build(Users user, Set<GrantedAuthority> authorities) {

        return new UserPrincipal(
                user.getId(),
                user.getUserName(),
                user.getAppId(),
                user.getWhiteListIp(),
                user.getPassWord(),
                authorities,
                user.getSecretKey());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return userId;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserPrincipal user = (UserPrincipal) o;
        return Objects.equals(userId, user.userId);
    }
    public static UserPrincipal getAuthorizedUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
