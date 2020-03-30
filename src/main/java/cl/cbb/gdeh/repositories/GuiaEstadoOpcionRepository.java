package cl.cbb.gdeh.repositories;

import cl.cbb.gdeh.entities.GuiaEstado;
import cl.cbb.gdeh.entities.GuiaEstadoOpcion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GuiaEstadoOpcionRepository extends JpaRepository<GuiaEstadoOpcion,Long> {

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.GuiaEstadoOpcion} que posean un guia estado igual al parametro.
     * @return
     */
    List<GuiaEstadoOpcion> findByGuiaEstado(GuiaEstado estado);
}
