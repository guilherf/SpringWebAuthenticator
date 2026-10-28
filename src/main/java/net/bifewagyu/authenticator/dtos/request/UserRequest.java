package net.bifewagyu.authenticator.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


//Envia para a aplicação
public record UserRequest(

        @NotBlank(message = "Insira o nome de usuario")
        String nome,

        @NotBlank(message = "Insira o email")
        @Email(message = "Email invalido")
        String email,

        @NotBlank(message = "Insira a senha")
        @Size(min = 8, max = 32, message = "")
        String password) {

    // DESCOBRIR COMO APARECER NO LOG, FAZER QUE APAREÇA A MENSAGEM FINALIZO POR HJ
}
