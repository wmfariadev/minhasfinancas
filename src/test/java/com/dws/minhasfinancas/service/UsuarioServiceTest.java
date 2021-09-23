package com.dws.minhasfinancas.service;

import com.dws.minhasfinancas.exception.ErroAutenticacao;
import com.dws.minhasfinancas.exception.RegraNegocioException;
import com.dws.minhasfinancas.model.entity.Usuario;
import com.dws.minhasfinancas.model.repository.UsuarioRepository;
import com.dws.minhasfinancas.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;

    Usuario usuarioMock;

    @BeforeAll
    public void setUp() {
        usuarioMock = Usuario.builder().email("email@email.com").nome("nome").senha("123").id(1L).build();
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        // cenario
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

        // acao
        Throwable exception = Assertions.catchThrowable(() -> service.validarEmail("email@email.com"));

        // verificacao
        Assertions.assertThat(exception).isInstanceOf(RegraNegocioException.class).hasMessage("Já existe um usuário com este e-mail");
    }
    
    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
        // cenario
        Mockito.when(repository.findByEmail("email@email.com")).thenReturn(Optional.of(usuarioMock));
        
        // acao
        Usuario result = service.autenticar("email@email.com", "123");
        
        // verificacao
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUmUsuarioComEmailInformado() {
        // cenario
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        // acao
        Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123"));

        // verificacao
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuario não encontrado");
    }

    @Test
    public void deveLancarErroQuandoSenhaForDiferenteDoEsperado() {
        // cenario
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuarioMock));

        // acao
        Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha"));

        // verificacao
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida");
    }

    @Test
    public void deveSalvarUmNovoUsuario() {
        // cenario
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuarioMock);

        // acao
        service.salvarUsuario(new Usuario());

        // verificacao
        Assertions.assertThat(usuarioMock.getId()).isEqualTo(1L);
        Assertions.assertThat(usuarioMock.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioMock.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioMock.getSenha()).isEqualTo("123");
    }
}
