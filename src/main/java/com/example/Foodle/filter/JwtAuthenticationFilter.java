package com.example.Foodle.filter;

import com.example.Foodle.dao.UsersDao;
import com.example.Foodle.entity.UsersEntity;
import com.example.Foodle.provider.JwtProvider;
import com.example.Foodle.service.UsersService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtProvider jwtProvider;
    private UsersService usersService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
        throws ServletException, IOException {
            try{
                String token = parseBearerToken(request);
                if(token == null){
                    filterChain.doFilter(request, response);
                    return;
                }
                String uid = jwtProvider.validate(token);
                if(uid == null){
                    filterChain.doFilter(request, response);
                    return;
                }
                UsersDao usersDao = new UsersDao();
                UsersEntity user = usersDao.getUsersEntity(uid);
                String role = user.getRole(); // role = ROLE_USER or ROLE_ADMIN

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(role));

                SecurityContext securitycontext = SecurityContextHolder.createEmptyContext();
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uid, null, authorities);
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securitycontext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securitycontext);
                

            } catch (Exception e){
                e.printStackTrace();
            }
            filterChain.doFilter(request, response);
        }
    
    private String parseBearerToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        boolean hasAuthorizationHeader = StringUtils.hasText(header);
        if(!hasAuthorizationHeader){
            return null;
        }
        boolean isBearerToken = header.startsWith("Bearer ");
        if(!isBearerToken){
            return null;
        }
        String token = header.substring(7);
        return token;
    }
}
