package com.example.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
//import org.hibernate.mapping.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.minhasfinancas.exception.RegraNegocioException;
import com.example.minhasfinancas.model.entity.Lancamento;
import com.example.minhasfinancas.model.entity.Usuario;
import com.example.minhasfinancas.model.enums.StatusLancamento;
import com.example.minhasfinancas.model.repository.LancamentoRepository;
import com.example.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.example.minhasfinancas.service.impl.LancamentoServiceImpl;

//import antlr.collections.List;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {
	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execução
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		//verificação
		Assertions.assertThat( lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat( lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	

@Test
public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
	
	Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
	Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
	Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvar), RegraNegocioException.class);
	Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}

@Test
public void deveAtualizarUmLancamento() {
	//cenario
	//Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
	Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
	lancamentoSalvo.setId(1l);
	lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
	
	Mockito.doNothing().when(service).validar(lancamentoSalvo);
	Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
	
	//execução
	service.atualizar(lancamentoSalvo);
	
	//verificação
	Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	
	//Assertions.assertThat( lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
	//Assertions.assertThat( lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
}

@Test
public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
	//Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
	
	//cenario
	Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
	
	//execução e verificação
	Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoASalvar), NullPointerException.class);
	Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	}

@Test
public void deveDeletarUmLancamento() {
	//cenario
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		//execução
		service.deletar(lancamento);
		
		//verificação
		Mockito.verify( repository ).delete(lancamento);
	
}

@Test
public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
	//cenario
			Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
			
			
			//execução
			Assertions.catchThrowableOfType( () -> service.deletar(lancamento), NullPointerException.class);
			
			//verificação
			Mockito.verify( repository, Mockito.never()  ).delete(lancamento);
	
	}

@Test
public void deveFiltrarLancamento() {
	//cenario
	Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
	lancamento.setId(1l);
	
	
	List<Object> lista = Arrays.asList(lancamento);
	
	Mockito.when(repository.findAll(Mockito.any(Example.class)) ).thenReturn(lista);
	
	//execução
	List<Lancamento> resultado = service.buscar(lancamento);
	
	//verificaçoes
	Assertions
	.assertThat(resultado)
	.isNotEmpty()
	.hasSize(1)
	.contains(lancamento);
}

@Test
public void deveAtualizarOsStatusDeUmLancamento() {
	//cenario
	Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
	lancamento.setStatus(StatusLancamento.PENDENTE);
	
	StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
	Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
	
	//execucao
	service.atualizarStatus(lancamento, novoStatus);
	
	//verificacoes
	Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
	Mockito.verify(service).atualizar(lancamento);
	
	
}

@Test
public void deveObterUmLancamentoPorID() {
	//CENARIO
	Long id = 1l;
	
	
	Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
	lancamento.setId(id);
	
	Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
	
	//execucao
	
	Optional<Lancamento> resultado = service.obterPorId(id);
	
	//verificacao
	
	Assertions.assertThat(resultado.isPresent()).isTrue();
}


@Test
public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
	//CENARIO
	Long id = 1l;
	
	
	Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
	lancamento.setId(id);
	
	Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
	
	//execucao
	
	Optional<Lancamento> resultado = service.obterPorId(id);
	
	//verificacao
	
	Assertions.assertThat(resultado.isPresent()).isFalse();
}

@Test
public void deveLancarErrosAoValidarUmLancamento() {
	Lancamento lancamento = new Lancamento();
	
	Throwable erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");
	
	lancamento.setDescricao("");
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");
	
	lancamento.setDescricao("Salario");
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês valido.");
	
	lancamento.setAno(0);
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês valido.");
	
	lancamento.setAno(13);
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês valido.");
	
	lancamento.setMes(1);
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano valido.");
	
	lancamento.setAno(202);
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano valido.");
	
	lancamento.setAno(2022);
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuario.");
	
	lancamento.setUsuario(new Usuario());
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuario.");
	
	lancamento.getUsuario().setId(1l);
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor valido.");
	
	lancamento.setValor(BigDecimal.ZERO);
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor valido.");
	
	erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
	Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo de lancamento.");
	
}

}


