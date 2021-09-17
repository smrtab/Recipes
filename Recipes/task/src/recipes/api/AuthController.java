package recipes.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.data.request.AuthRequest;
import recipes.data.request.RecipeRequest;
import recipes.data.service.AuthService;
import recipes.data.service.RecipeService;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(Map.of("id", authService.register(request)));
    }
}
