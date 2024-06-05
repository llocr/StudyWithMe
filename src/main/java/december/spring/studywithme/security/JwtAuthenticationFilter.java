package december.spring.studywithme.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import december.spring.studywithme.dto.LoginRequestDto;
import december.spring.studywithme.entity.User;
import december.spring.studywithme.jwt.JwtUtil;
import december.spring.studywithme.repository.UserRepository;
import december.spring.studywithme.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;


    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUserId(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        String accessToken = jwtUtil.createAccessToken(username);
        String refreshToken = jwtUtil.createRefreshToken(username);

        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        user.refreshTokenReset(refreshToken);
        userRepository.save(user);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        response.addHeader(JwtUtil.REFRESH_HEADER, refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(400);
        response.setContentType("application/json; charset=UTF-8");
        try {
            response.getWriter().write("{\"message\":\"회원을 찾을 수 없습니다.\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
