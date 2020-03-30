package cl.cbb.gdeh.repositories;

import cl.cbb.gdeh.entities.TickEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TickEstadoRepository extends JpaRepository<TickEstado,Long> {

    /**
     * Retorna todos los  {@link cl.cbb.gdeh.entities.TickEstado}.
     */

    List<TickEstado> findByOrderByIdAsc();

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.TickEstado} que posean un id igual al parametro.
     * id.
     * @return
     */

    Optional<TickEstado> findById(Long id);
}
