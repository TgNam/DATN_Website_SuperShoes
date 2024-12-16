package org.example.datn_website_supershoes.controller.REST;

import jakarta.validation.Valid;
import org.example.datn_website_supershoes.dto.request.LoginRequest;
import org.example.datn_website_supershoes.dto.response.Response;
import org.example.datn_website_supershoes.dto.token.TokenResponse;
import org.example.datn_website_supershoes.model.Account;
import org.example.datn_website_supershoes.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountLockedException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestAPI {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> loginHandle(@RequestBody @Valid LoginRequest loginRequest, BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getAllErrors().stream()
                        .map(error -> error.getDefaultMessage())
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errors);
            }
            TokenResponse tokenResponse = authenticationService.authenticate(loginRequest);
            return ResponseEntity.ok(tokenResponse);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Response.builder()
                            .status(HttpStatus.UNAUTHORIZED.toString())
                            .mess("Email hoặc mật khẩu không hợp lệ.")
                            .build()
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Response.builder()
                            .status(HttpStatus.UNAUTHORIZED.toString())
                            .mess("Thông tin đăng nhập không hợp lệ. Vui lòng kiểm tra mật khẩu của bạn.")
                            .build()
            );
        } catch (AccountLockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(
                    Response.builder()
                            .status(HttpStatus.LOCKED.toString())
                            .mess("Tài khoản đã bị khóa. Vui lòng liên hệ bộ phận hỗ trợ.")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Response.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .mess("Đã xảy ra lỗi không mong muốn: " + e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        try {
            return ResponseEntity.ok(authenticationService.register(account));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Param("email") String email) {
        try {
            authenticationService.resetPassword(email);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.builder()
                            .status(HttpStatus.OK.toString())
                            .mess("Succ")
                            .build()
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }

    @GetMapping("/verify-reset-password/{token}/{id}")
    public ResponseEntity<?> verifyResetPassword(@PathVariable("token") String token,
                                                 @PathVariable("id") Long id) {
        try {
            authenticationService.verify(token, id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Response.builder()
                            .status(HttpStatus.OK.toString())
                            .mess("Succ")
                            .build()
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Response.builder()
                            .status(HttpStatus.NOT_FOUND.toString())
                            .mess(e.getMessage())
                            .build()
                    );
        }
    }
}
