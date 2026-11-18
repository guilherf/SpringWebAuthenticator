package net.bifewagyu.authenticator.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController("admin")
public class AdminController {



         @GetMapping("/painelAdmin")
        public String painel() {
            return "ENTRADA AO PAINEL SUCESSO";
        }









}
