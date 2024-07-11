package com.lpb.mid.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lpb.mid.entity.Permision;
import com.lpb.mid.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @JsonProperty("user")
    private Users user;
    @JsonProperty("roles")
    private List<Permision> roles;
}
