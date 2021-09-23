package com.dws.minhasfinancas.model.repository;

import com.dws.minhasfinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    final Usuario usuarioMock = Usuario.builder().nome("usuario").email("usuario@email.com").senha("123").build();

    @Test
    public void deveVerificarExistenciaDeUmEmail() {
        // cenario
        entityManager.persist(usuarioMock);

        // acao
        boolean existe = repository.existsByEmail(usuarioMock.getEmail());

        // verificacao
        Assertions.assertThat(existe).isTrue();
    }

    @Test
    public void naoDeveExistirEmailCadastrado() {
        // acao
        boolean existe = repository.existsByEmail("usuario@email.com");

        // verificacao
        Assertions.assertThat(existe).isFalse();
    }

    @Test
    public void devePersistirUmUsuario() {
        // cenario

        // acao
        repository.save(usuarioMock);

        // verificacao
        Assertions.assertThat(usuarioMock.getId()).isNotNull();
    }

    @Test
    public void deveBuscarUsuarioPorEmail() {
        // cenario
        entityManager.persist(usuarioMock);

        // acao
        Optional<Usuario> usuario = repository.findByEmail("usuario@email.com");

        // verificacao
        Assertions.assertThat(usuario.isPresent()).isTrue();
    }

    @Test
    public void deveRetornarVazioQuandoUsuarioNaoExistirNaBase() {
        // cenario

        // acao
        Optional<Usuario> usuario = repository.findByEmail("usuario@email.com");

        // verificacao
        Assertions.assertThat(usuario.isPresent()).isFalse();
    }
}
