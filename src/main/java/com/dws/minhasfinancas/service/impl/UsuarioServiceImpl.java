package com.dws.minhasfinancas.service.impl;

import com.dws.minhasfinancas.exception.ErroAutenticacao;
import com.dws.minhasfinancas.exception.RegraNegocioException;
import com.dws.minhasfinancas.model.entity.Usuario;
import com.dws.minhasfinancas.model.repository.UsuarioRepository;
import com.dws.minhasfinancas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);

        if (!usuario.isPresent()) throw new ErroAutenticacao("Usuario não encontrado");

        if (!usuario.get().getSenha().equals(senha)) throw new ErroAutenticacao("Senha inválida");

        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = repository.existsByEmail(email);
        if (existe) {
            throw new RegraNegocioException("Já existe um usuário com este e-mail");
        }
    }
}
