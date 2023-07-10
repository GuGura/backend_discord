package com.example.backend.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.backend.controller.exception.CustomException;
import com.example.backend.model.JwtToken;
import com.example.backend.model.User;
import com.example.backend.properties.JwtProperties;
import com.example.backend.security.PrincipalDetails;
import com.example.backend.service.JwtService;
import com.example.backend.util.ConvenienceUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean requestURI = isExceptRequest(request);
        try {
            if (!requestURI) {
                String tokenType = jwtService.findTokenType(request);
                System.out.println("1. 권한이나 인증이 필요한 요청이 전달됨: " + tokenType);
                String jwtToken = jwtService.getTokenFromHeader(request, tokenType);
                DecodedJWT jwt = jwtService.decodedJWT(jwtToken);
                String username = jwtService.getClaim(jwt, "username", String.class);
                User user = jwtService.findUserByUsername(username);
                if (tokenType.equals(JwtProperties.HEADER_REFRESH)) response = refreshJwtProcess(user, response);
                PrincipalDetails principalDetails = new PrincipalDetails(user);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("user_id", user.getId());
                System.out.println("Success");
            }
            super.doFilterInternal(request, response, chain);
        } catch (Exception e) {
            ConvenienceUtil.jwtExceptionHandler(e,request,response);
        }
    }

    public HttpServletResponse refreshJwtProcess(User user, HttpServletResponse response) throws CustomException {
        Map<String, Object> accessJwtMap = jwtService.createAccessToken(user.getId(), user.getUsername(), user.getRole());
        Map<String, Object> refreshJwtMap = jwtService.createRefreshToken(user.getUsername(), user.getRole());
        JwtToken jwt = jwtService.createTokenDto(user.getUsername(), accessJwtMap, refreshJwtMap);
        jwtService.updateToken(jwt);
        response.addHeader(JwtProperties.HEADER_ACCESS, JwtProperties.TOKEN_PREFIX + jwt.getAccessJwt());
        response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + jwt.getRefreshJwt());
        return response;
    }

    public boolean isExceptRequest(HttpServletRequest request) {
        String[] exceptRequest = {"/sign"};
        String requestURI = request.getRequestURI();
        System.out.println("요청 URI: " + requestURI);
        AtomicBoolean isExceptRequest = new AtomicBoolean(false);
        Arrays.asList(exceptRequest).forEach(URI -> {
            if (requestURI.contains(URI))
                isExceptRequest.set(true);
        });
        return isExceptRequest.get();
    }
}
