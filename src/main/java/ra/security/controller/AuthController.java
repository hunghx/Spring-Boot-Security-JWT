package ra.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.security.exception.LoginException;
import ra.security.model.dto.request.FormSignInDto;
import ra.security.model.dto.request.FormSignUpDto;
import ra.security.model.dto.response.JwtResponse;
import ra.security.security.jwt.JwtProvider;
import ra.security.security.user_principle.UserPrinciple;
import ra.security.service.IUserService;
import ra.security.service.impl.MailService;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v4/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private MailService mailService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("vào được rồi nè");
    }
    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> signin(@RequestBody FormSignInDto formSignInDto) throws LoginException{
        Authentication authentication =null;
        try {
             authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(formSignInDto.getUsername(), formSignInDto.getPassword())
            ); // tạo đối tương authentiction để xác thực thông qua username va password
            // tạo token và trả về cho người dùng
        }catch (AuthenticationException e){
            throw new LoginException("Username or password is incorrect!");
        }
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        String token = jwtProvider.generateToken(userPrinciple);
        // lấy ra user principle
        List<String> roles = userPrinciple.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(JwtResponse.builder().token(token)
                .name(userPrinciple.getName())
                .username(userPrinciple.getUsername())
                .roles(roles)
                        .type("Bearer")
                .status(userPrinciple.isStatus()).build());
    }
    @PostMapping("/sign-up")
    public ResponseEntity<String> signup(
//            @RequestBody FormSignUpDto formSignUpDto,
            @RequestParam List<MultipartFile> file){
//        userService.save(formSignUpDto); // save : status : pending
        mailService.sendEmail("phanhoanghieu1999@gmail.com","Hacker Viet Nam","<h1 style='color:red'>Chúc mừng bạn đã bị hack nick !</h1>",file);
        return new ResponseEntity("success",HttpStatus.CREATED);

    }
}
