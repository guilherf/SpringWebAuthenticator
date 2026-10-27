package net.bifewagyu.authenticator.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


//Envia para a aplicação
public record UserRequest(@NotBlank(message = "Insira o nome de usuario") String nome, @Email(message = "Email invalido") String email,
                         @Size(min = 8, max = 32, message = "") String password) {
}
