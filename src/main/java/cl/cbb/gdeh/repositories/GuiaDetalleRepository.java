package cl.cbb.gdeh.repositories;

import cl.cbb.gdeh.entities.Guia;
import cl.cbb.gdeh.entities.GuiaDetalle;
import cl.cbb.gdeh.entities.GuiaHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuiaDetalleRepository extends JpaRepository<GuiaDetalle,Long> {

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.GuiaDetalle} que posean un guia igual al parametro.
     * @return
     */
    List<GuiaDetalle> findByGuia(Guia guia);

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.GuiaDetalle} que posean un guia_id igual al parametro.
     * @return
     */
    @Query(value = "SELECT gd FROM GuiaDetalle gd WHERE gd.guia.id = :guia_id AND " +
            "(gd.prod_code LIKE '35%' OR gd.prod_code LIKE '30%')")
    List<GuiaDetalle> findByGuiaId(@Param("guia_id") Long guia_id);
}
