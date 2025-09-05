package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.model.RefreshToken;
import com.example.demo.model.Role;
import com.example.demo.model.Users;
import com.example.demo.payload.JwtResponse;
import com.example.demo.payload.LoginRequest;
import com.example.demo.payload.LogoutRequest;
import com.example.demo.payload.MessageResponse;
import com.example.demo.payload.RegisterRequest;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.service.UsersService;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    public final UsersService usersService;

    @Autowired
    public final AuthenticationManager authenticationManager;

    @Autowired
    public final JwtTokenProvider tokenProvider;

    @Autowired
    public final RefreshTokenService refreshTokenService;


    public AuthController(UsersService usersService, AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider, RefreshTokenService refreshTokenService) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String acces_jwt = tokenProvider.generateAccessToken(authentication);
            String refresh_jwt = null;
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String username = userDetails.getUsername();
            String role = userDetails.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .findFirst()
                    .orElse(null);

            Users user = usersService.getUserRepository().findByUsername(userDetails.getUsername())
                    .orElse(null);

            if (user != null) {
                RefreshToken refreshToken = tokenProvider.generateAndSaveRefreshToken(user);
                refresh_jwt = refreshToken.getToken();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("User not found after authentication."));
            }

            return ResponseEntity.ok(new JwtResponse(acces_jwt, refresh_jwt, username, role));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid username or password."));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshTokenUser(@RequestBody LogoutRequest refreshRequest) {
        String refreshToken = refreshRequest.getRefreshToken();
        String newAccess_jwt = null;
        String newRefresh_jwt = null;
        String username = null;
        String role = null;
        try {
            if (tokenProvider.validateToken(refreshToken)) {
                System.out.println(username);

                if (refreshTokenService.verifyExpiration(refreshToken)) {
                    // Users user =
                    // usersService.getUserRepository().findByUsername(username).orElse(null);
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    role = userDetails.getAuthorities().stream()
                            .map(authority -> authority.getAuthority())
                            .findFirst()
                            .orElse(null);

                    Users user = usersService.getUserRepository().findByUsername(userDetails.getUsername())
                            .orElse(null);

                    newAccess_jwt = tokenProvider.generateAccessToken(authentication);

                    if (user != null) {
                        RefreshToken newRefreshToken = tokenProvider.generateAndSaveRefreshToken(user);
                        newRefresh_jwt = newRefreshToken.getToken();
                    } else {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new MessageResponse("User not found after authentication."));
                    }
                } else {
                    refreshTokenService.deleteToken(refreshToken);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new MessageResponse("Refresh token expired"));
                }

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MessageResponse("Invalid refresh token!"));
            }
            refreshTokenService.deleteToken(refreshToken);
            return ResponseEntity.ok(new JwtResponse(newAccess_jwt, newRefresh_jwt, username, role));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Refreshing error!"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody LogoutRequest logoutRequest) {
        // Find and delete the refresh token from the database
        refreshTokenService.deleteToken(logoutRequest.getRefreshToken());

        // Return a successful response
        return ResponseEntity.ok(new MessageResponse("Logout successful."));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            usersService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(),registerRequest.getIdentification());
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            // Handle the case where the username already exists
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        }
    }

}
