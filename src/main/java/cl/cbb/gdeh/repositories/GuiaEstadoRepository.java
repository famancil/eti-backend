package cl.cbb.gdeh.repositories;

import cl.cbb.gdeh.entities.GuiaEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuiaEstadoRepository extends JpaRepository<GuiaEstado,Long> {

    /**
     * Retorna todos los  {@link cl.cbb.gdeh.entities.GuiaEstado}.
     */

    List<GuiaEstado> findByOrderByIdAsc();

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.GuiaEstado} que posean un id igual al parametro.
     * @return
     */

    Optional<GuiaEstado> findById(Long id);

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.GuiaEstado} que posean un estado igual al parametro.
     * @return
     */
    Optional<GuiaEstado> findByEstado(String estado);
}
