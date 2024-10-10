package med.voll.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import med.voll.api.dto.endereco.DadosEndereco;
import med.voll.api.dto.medico.DadosCadastroMedico;
import med.voll.api.dto.medico.Especialidade;
import med.voll.api.dto.paciente.DadosCadastroPaciente;
import med.voll.api.model.Consulta;
import med.voll.api.model.Medico;
import med.voll.api.model.Paciente;

@SpringBootTest
@DataJpaTest //por padrão usa o bd em memória (h2)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //usa o banco de dados configurado Ex: mysql ou algum outro
@ActiveProfiles("test") //faz com que o spring leia as propriedades de application-test.properties para eu poder usar uma base de testes diferenciada
class MedicoRepositoryTest {

	@Autowired
	private MedicoRepository medicoRepository;
	
	@Autowired
	private TestEntityManager em;
	
	@Test
	@DisplayName("Deveria retornar null quando unico medico dadastrado não está disponível na data")
	void escolherMedicoAleatorioLivreNaData() {
		//given ou arrange		 
		var medico = cadastrarMedico("Medico", "medico@voll.med", "323232", Especialidade.CARDIOLOGIA);
		var paciente = cadastrarPaciente("Paciente", "paciente@email.com", "00000000000");
		var proximaSegundaAs10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10, 0); 
		cadastrarConsulta(medico, paciente, proximaSegundaAs10);
		
		//when ou act
		Medico medicoEscolhido = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);
		
		//then ou assert
		assertThat(medicoEscolhido).isNull();
		
	}
	
	@Test
	@DisplayName("Deveria devolver medico quando ele estiver disponivel na data")
	void escolherMedicoAleatorioLivreNaDataCenario2() {
	     var proximaSegundaAs10 = LocalDate.now()
	                    .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
	                    .atTime(10, 0);
	    var medico = cadastrarMedico("Medico", "medico@voll.med", "898989", Especialidade.CARDIOLOGIA);

	    var medicoLivre = medicoRepository.escolherMedicoAleatorioLivreNaData(Especialidade.CARDIOLOGIA, proximaSegundaAs10);
	    assertThat(medicoLivre).isEqualTo(medico);
	}

	
	
	private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data) {
	    em.persist(new Consulta(null, medico, paciente, data, null));
	}

	private Medico cadastrarMedico(String nome, String email, String crm, Especialidade especialidade) {
	    var medico = new Medico(dadosMedico(nome, email, crm, especialidade));
	    em.persist(medico);
	    return medico;
	}

	private Paciente cadastrarPaciente(String nome, String email, String cpf) {
	    var paciente = new Paciente(dadosPaciente(nome, email, cpf));
	    em.persist(paciente);
	    return paciente;
	}

	private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade) {
	    return new DadosCadastroMedico(
	            nome,
	            email,
	            "61999999999",
	            crm,
	            especialidade,
	            dadosEndereco()
	    );
	}

	private DadosCadastroPaciente dadosPaciente(String nome, String email, String cpf) {
	    return new DadosCadastroPaciente(
	            nome,
	            email,
	            "61999999999",
	            cpf,
	            dadosEndereco()
	    );
	}

	private DadosEndereco dadosEndereco() {
	    return new DadosEndereco(
	            "rua xpto",
	            "bairro",
	            "00000000",
	            "Brasilia",
	            "DF",
	            null,
	            null
	    );
	}
}
