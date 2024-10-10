package med.voll.api.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import med.voll.api.dto.medico.Especialidade;
import med.voll.api.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

	Page<Medico> findByAtivoTrue(Pageable paginacao);
	
	@Query(""" 
			SELECT med FROM Medico med
			WHERE med.ativo = true
			  AND med.especialidade  = :especialidade
			  AND med.id NOT IN (SELECT c.medico.id FROM Consulta c WHERE c.data = :data and c.motivoCancelamento is null)
			ORDER BY rand()
			LIMIT 1
			""")
	Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);
	
	@Query("""
			SELECT med.ativo
			FROM Medico med
			WHERE med.id = :idMedico
			""")
	Boolean findAtivoById(Long idMedico);

}
