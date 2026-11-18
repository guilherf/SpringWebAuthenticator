package net.bifewagyu.authenticator.dtos.response;

import net.bifewagyu.authenticator.models.RoleModels;

import java.util.List;

public record AdminResponse(Long id, String nome, String email, List<RoleModels> roles) {
}
