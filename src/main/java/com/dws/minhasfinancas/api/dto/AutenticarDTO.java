package com.dws.minhasfinancas.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AutenticarDTO {

    private String email;
    private String senha;
}
