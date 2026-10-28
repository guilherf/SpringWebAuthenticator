package net.bifewagyu.authenticator.controllers;


import jakarta.validation.Valid;
import net.bifewagyu.authenticator.dtos.request.UserRequest;
import net.bifewagyu.authenticator.dtos.response.UserResponse;
import net.bifewagyu.authenticator.models.EntityUser;
import net.bifewagyu.authenticator.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        System.out.println("CHEGOU" + userRequest.toString());

        UserResponse user = userService.salvarUsuario(userRequest);


        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }


    @GetMapping("/getUsers")
    public ResponseEntity<List<UserResponse>> getUsers() {


        return ResponseEntity.ok(userService.mostrarUsuario());


    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<UserResponse> removeUser(@PathVariable("id") Long id) {

        UserResponse user = userService.removerUsuario(id);

        return ResponseEntity.ok(user);


    }




}
