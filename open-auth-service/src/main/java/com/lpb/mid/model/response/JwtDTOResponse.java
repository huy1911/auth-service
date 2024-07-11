package com.lpb.mid.model.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
public class JwtDTOResponse {
    private String jwt;
    private String exp;
}
