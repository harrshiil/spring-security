package spring.security.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.security.springsecurityjwt.models.AuthenticationRequest;
import spring.security.springsecurityjwt.models.AuthenticationResponse;
import spring.security.springsecurityjwt.util.JwtUtil;

@RestController
public class HomeResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String home() {
        return ("<h1>Hello World</h1>");
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException badCredentialsException) {
            throw new Exception("Incorrect Username or Password", badCredentialsException);
        }

        UserDetails userDetails = myUserDetailService.loadUserByUsername(authenticationRequest.getUserName());

        return new AuthenticationResponse(jwtUtil.generateToken(userDetails));

    }
}
