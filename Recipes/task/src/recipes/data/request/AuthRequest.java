package recipes.data.request;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class AuthRequest {
    @Email
    @Pattern(regexp = "\\w+@\\w+\\.\\w+")
    private final String email;

    @NotBlank
    @Size(min = 8)
    private final String password;
}
