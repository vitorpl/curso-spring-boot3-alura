package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import med.voll.api.dto.usuario.DadosAutenticacao;
import med.voll.api.infra.security.DadosTokenJwt;
import med.voll.api.infra.security.TokenService;
import med.voll.api.model.Usuario;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private TokenService tokenService;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@PostMapping
	public ResponseEntity<?> autenticar(@RequestBody @Valid DadosAutenticacao dados) {
		System.out.println(gerarHash(dados.senha()));
		
		var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
		
		var autentication = manager.authenticate(authenticationToken);

//		System.out.println("senha hash : " + dados.senha());
		

		String tokenJwt = tokenService.gerarToken((Usuario) autentication.getPrincipal());
		return ResponseEntity.ok(new DadosTokenJwt(tokenJwt));
	}

	// Método que recebe uma string e retorna o hash bcrypt
	public String gerarHash(String senha) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(senha);
	}
}
