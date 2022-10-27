package com.example.minhasfinancas.service.impl;

import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.minhasfinancas.exception.ErroAutenticacao;
import com.example.minhasfinancas.exception.RegraNegocioException;
import com.example.minhasfinancas.model.entity.Usuario;
import com.example.minhasfinancas.model.repository.UsuarioRepository;
//import com.example.minhasfinancas.model.repository.UsuarioRepositoryTest;
import com.example.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	
	private UsuarioRepository repository;
	
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		 Optional<Usuario> usuario = repository.findByEmail(email);
		 if(!usuario.isPresent()) {
			 throw new ErroAutenticacao("Usuario não encontrado para o email informado.");
		 }
		 
		 if(!usuario.get().getSenha().equals(senha)) {
			 throw new ErroAutenticacao("Senha Invalida.");
			 
			 
		 }
		
		return usuario.get();
	}

	@Override
	@Transactional
	public Usuario salvaUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		// TODO Auto-generated method stub
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Ja existe um usuario cadastrado com este email.");
		}
		
	}
	
	@Override
	public Optional<Usuario> obterPorId(Long id) {
		return repository.findById(id);
	}

}
