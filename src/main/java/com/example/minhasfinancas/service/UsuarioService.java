package com.example.minhasfinancas.service;

import com.example.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

	Usuario autenticar(String email, String senha);
	
	Usuario salvaUsuario(Usuario usuario);
	
	void validarEmail(String email);
}
