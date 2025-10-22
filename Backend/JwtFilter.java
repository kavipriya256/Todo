package kavi.example.hello;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kavi.example.hello.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authheader= request.getHeader("Authorization");
        if(authheader != null  && authheader.startsWith("Bearer ")) {
            //skips Bearer_ and takes from next char
            String token = authheader.substring(7);
            if (jwtUtil.validateJwtToken(token))
            {
                String email = jwtUtil.extractEmail(token);
                //UsernamePasswordAuthenticationToken = predefined class in spring
                var auth=new UsernamePasswordAuthenticationToken(email,null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request,response);
    }
}
