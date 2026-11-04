package net.bifewagyu.authenticator.dtos.response;


import net.bifewagyu.authenticator.models.RoleModels;

import java.util.List;

//Resposta da aplicação
public record UserResponse(Long id, String nome, String email, List<RoleModels> roles) {




}
