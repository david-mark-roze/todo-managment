package net.javaguides.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {

    private static final String TOKEN_TYPE = "Bearer";

    private String accessToken;
    private String role;

    public String getTokenType(){
        return TOKEN_TYPE;
    }
}
