package kavi.example.hello.controller;

import kavi.example.hello.models.User;
import kavi.example.hello.repository.UserRepository;
import kavi.example.hello.service.UserService;
import kavi.example.hello.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    //@Autowired - either this or RequiredArgsConstructor can be used, but this requires the field to be final
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ResposneEntity<?>  ?=any return type is accepted
    @PostMapping("/login")                    //email,password
    public ResponseEntity<?> loginUser(@RequestBody Map<String,String> body )
    {
        String email = body.get("email");
        String password = body.get("password");

        var userOptional = userRepository.findByEmail(email); //this returns Optional<User> - this is used in java to avoid null pointer exception
        if(userOptional.isEmpty())
        {
            return new ResponseEntity<>(Map.of("message", "User not registered"),HttpStatus.UNAUTHORIZED);
        }

        //else if user is present ; check if password is valid
        User user = userOptional.get(); // returns obj of User type
        //pass entered by user(raw data), pass in db (encrypted)
        if(!passwordEncoder.matches(password, user.getPassword()))
        {
            return new ResponseEntity<>(Map.of("message", "Invalid password"),HttpStatus.UNAUTHORIZED);
        }

        // if valid, then generate token
        String token = jwtUtil.generateToken(email);
        return ResponseEntity.ok(Map.of("token",token)); //200 http status response
        // json response of token is returend
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String,String> body )
    {
        String email = body.get("email");
        String rawpassword = body.get("password");
        String password=passwordEncoder.encode(rawpassword); //enocded pass is saved in db

        //check if email is valid = means if its present in DB (login)
        if(userRepository.findByEmail(email).isPresent())
        {
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        }
        //if email doesnt exists = create user
        userService.createUser(User.builder().email(email).password(password).build());
        return new ResponseEntity<>("Successfully registered",HttpStatus.CREATED);
    }
}
