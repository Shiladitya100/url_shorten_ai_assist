package com.schwab.urlshortener.controller;

import com.schwab.urlshortener.dto.CsrfTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/security")
@Tag(name = "Security", description = "Security helper endpoints")
public class SecurityController {

    @GetMapping("/csrf")
    @Operation(
            summary = "Get CSRF token",
            description = "Returns the CSRF token required by state-changing endpoints such as POST /api/v1/urls.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "CSRF token returned")
            }
    )
    public ResponseEntity<CsrfTokenResponse> getCsrfToken(CsrfToken csrfToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("XSRF-TOKEN", csrfToken.getToken());
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        return ResponseEntity.ok(new CsrfTokenResponse(
                "X-XSRF-TOKEN",
                csrfToken.getParameterName(),
                csrfToken.getToken()
        ));
    }
}
