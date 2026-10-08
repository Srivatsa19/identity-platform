package com.identityplatform.authentication;

import com.identityplatform.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/me")
public class CurrentUserController {

    @GetMapping
    public CurrentUserResponse me(@RequestAttribute(SecurityContext.ATTRIBUTE_NAME) SecurityContext securityContext) {
        if (!securityContext.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        User user = securityContext.getUser();
        return new CurrentUserResponse(user.getId(), user.getEmail(), user.getCreatedAt());
    }
}