package com.pio.security.jwt.actuator;

import com.pio.security.jwt.constant.ResponseMessage;
import com.pio.security.jwt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtHealthIndicator implements HealthIndicator {

    @Value("${jwt.username}")
    private String username;
    @Value("${jwt.password}")
    private String password;
    @Value("${jwt.role}")
    private String role;
    private final JwtHelper jwtHelper;

    @Autowired
    public JwtHealthIndicator(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    public boolean isHealthGood() {
        UserDetails userDetails = User.builder().username(username).password(password).roles(role).build();
        try {
            String token = jwtHelper.generateToken(userDetails);
            return token != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Health health() {
        if (isHealthGood()) {
            return Health.up().withDetail("JWT Status", ResponseMessage.JWT_STATUS_SUCCESS).build();
        }
        return Health.down().withDetail("JWT Status", ResponseMessage.JWT_STATUS_FAILED).build();
    }
}
