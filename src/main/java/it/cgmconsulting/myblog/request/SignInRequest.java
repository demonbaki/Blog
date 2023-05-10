package it.cgmconsulting.myblog.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignInRequest {

    @NotBlank
    @Size(min=4)
    private String usernameOrEmail;

    @NotBlank @Size(min=6, max=15)
    private String password;

}