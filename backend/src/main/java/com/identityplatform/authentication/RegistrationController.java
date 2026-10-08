package com.identityplatform.authentication;

import com.identityplatform.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse register(@RequestBody RegistrationRequest request) {
        User user = registrationService.register(request.email(),request.password());
        return new RegistrationResponse(user.getId(),user.getEmail(),user.getCreatedAt());
    }
}