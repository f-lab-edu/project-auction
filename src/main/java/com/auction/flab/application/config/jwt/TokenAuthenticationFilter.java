package com.auction.flab.application.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static Pattern SPACE_PATTERN = Pattern.compile("\\s");
    private static final String TOKEN_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);
        tokenProvider.getAuthentication(token).ifPresent(authentication ->
            SecurityContextHolder.getContext().setAuthentication(authentication));
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (isValidTokenFormat(authorizationHeader)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    private boolean isValidTokenFormat(String authorizationHeader) {
        return authorizationHeader != null
                && authorizationHeader.startsWith(TOKEN_PREFIX)
                && authorizationHeader.length() > TOKEN_PREFIX.length()
                && !SPACE_PATTERN.matcher(authorizationHeader.substring(TOKEN_PREFIX.length())).matches();
    }

}
