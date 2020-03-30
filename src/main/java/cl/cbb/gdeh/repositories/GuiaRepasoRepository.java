package cl.cbb.gdeh.repositories;

import cl.cbb.gdeh.entities.GuiaRepaso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuiaRepasoRepository extends JpaRepository<GuiaRepaso,Long> {

    /**
     * Retorna todos los  {@link cl.cbb.gdeh.entities.GuiaEstado}.
     */

    List<GuiaRepaso> findByOrderByIdAsc();

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.GuiaEstado} que posean un id igual al parametro.
     * id.
     * @return
     */

    Optional<GuiaRepaso> findById(Long id);

    /**
     * Obtiene todos los {@link GuiaRepaso} por guia_id.
     *
     * @return {@link java.util.List} con todas las entidades {@link GuiaRepaso}.
     */
    @Query(value = "SELECT guia FROM GuiaRepaso guia WHERE guia_id = :guia_id ")
    GuiaRepaso findByGuiaId(@Param("guia_id") Long guia_id);
}
