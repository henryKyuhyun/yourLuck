package com.yourLuck.yourLuck.configuration.Filter;

import com.yourLuck.yourLuck.model.User;
import com.yourLuck.yourLuck.service.UserService;
import com.yourLuck.yourLuck.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;
    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/login", "/api/v1/users/join");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token;
        try {
            if (TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
                filterChain.doFilter(request, response);

                return;
            }else{
                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                log.info("AUTHRIZATION!!! : {}", header);
                if(header == null || !header.startsWith("Bearer ")){
                    throw new RuntimeException("INVALID / MISSING HEADER");
                }
                token = header.split(" ")[1].trim();
            }
            if(JwtTokenUtils.isExpired(token,key)){
                throw new RuntimeException("Expired Key");
            }
            String userName = JwtTokenUtils.getUserName(token, key);
            User user = userService.loadUserByUserName(userName);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(RuntimeException e) {
            log.error("Error while validating. {}", e.toString());
            filterChain.doFilter(request,response);
            return;
        }

        filterChain.doFilter(request, response);
    }



}
