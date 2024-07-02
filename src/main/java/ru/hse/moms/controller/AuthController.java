package ru.hse.moms.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.moms.request.SignInRequest;
import ru.hse.moms.request.SignUpRequest;
import ru.hse.moms.response.JwtResponse;
import ru.hse.moms.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        JwtResponse jwt = userService.signUp(signUpRequest);
        return ResponseEntity.ok(jwt);
    }
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
        JwtResponse jwt = userService.signIn(signInRequest);
        return ResponseEntity.ok(jwt);
    }
}
