package cl.cbb.gdeh.repositories;

import cl.cbb.gdeh.entities.Guia;
import cl.cbb.gdeh.entities.GuiaHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuiaHistorialRepository extends JpaRepository<GuiaHistorial,Long> {

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.GuiaHistorial} que posean un guia igual al parametro.
     * @return
     */
    List<GuiaHistorial> findByGuia(Guia guia);
}
