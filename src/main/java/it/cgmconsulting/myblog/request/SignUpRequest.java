package it.cgmconsulting.myblog.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpRequest {

    @NotBlank @Size(max = 20, min = 6)
    private String username;

   // @NotBlank @Size(max = 10, min = 6)
    @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$",
    message = "password must of 6 to 10 lenght whit no special characters")
    private String password;

    @NotBlank @Email
    private String email;
}
