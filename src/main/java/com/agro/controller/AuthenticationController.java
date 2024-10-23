package com.agro.controller;

import com.agro.exception.AuthenticationException;
import com.agro.model.request.SignIn;
import com.agro.model.request.SignUp;
import com.agro.model.response.Authentication;
import com.agro.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUp signUp, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new AuthenticationException("All fields must be correct and not empty");
        }
        return ResponseEntity.ok(authenticationService.signUp(signUp));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Authentication> signIn(@RequestBody SignIn signIn) {
        Authentication authResponse = authenticationService.signIn(signIn);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authResponse.token());

        return ResponseEntity.ok()
                .headers(headers)
                .body(authResponse);
    }
}
