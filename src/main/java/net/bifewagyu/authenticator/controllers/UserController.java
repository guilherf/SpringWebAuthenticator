package net.bifewagyu.authenticator.controllers;


import jakarta.validation.Valid;
import net.bifewagyu.authenticator.dtos.request.UserRequest;
import net.bifewagyu.authenticator.dtos.response.UserResponse;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity<UserResponse> getUser(@Valid @RequestBody UserRequest userRequest) {
        System.out.println("CHEGOU" + userRequest.toString());

        UserResponse user = userService.salvarUsuario(userRequest);


        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }


    @PostMapping("/remove")
    public ResponseEntity<UserResponse> removeUser(@Valid @RequestBody EntityUser entityUser) {

        UserResponse user = userService.removerUsuario(entityUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }




}
