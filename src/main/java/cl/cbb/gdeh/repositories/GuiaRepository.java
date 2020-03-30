package cl.cbb.gdeh.repositories;

import cl.cbb.gdeh.entities.Guia;
import cl.cbb.gdeh.entities.GuiaRepaso;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GuiaRepository extends JpaRepository<Guia,Long> {

    /**
     * Retorna todos las  {@link cl.cbb.gdeh.entities.Guia}.
     */

    List<Guia> findByOrderByIdAsc();

    /**
     * Retorna todos las  {@link cl.cbb.gdeh.entities.Guia} con los atributos de .
     * {@link cl.cbb.gdeh.entities.GuiaRepaso}
     */
    @Query(value = "SELECT guia FROM Guia guia ORDER BY guia.fechaCreacion DESC")
    List<Guia> findAllGuiaAndOrderDesc();

    /**
     * Retorna todos las  {@link cl.cbb.gdeh.entities.Guia} con un estado especifico
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE guia.guiaEstado.estado = :estado " +
            "AND guia.tick_ship_plant_loc_code = :centro ORDER BY guia.fechaCreacion DESC")
    List<Guia> findAllGuiaByGuiaEstadoAndOrderDesc(@Param("estado") String estado,
                                                   @Param("centro") String centro);

    /**
     * Retorna todos las  {@link cl.cbb.gdeh.entities.Guia} emitidas, escaneadas, repasadas y escaneada-repasada.
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE (guia.guiaEstado.id = 5 OR guia.guiaEstado.id = 6 OR " +
            "guia.guiaEstado.id = 7 OR guia.guiaEstado.id = 8) " +
            "AND guia.tick_ship_plant_loc_code = :centro ORDER BY guia.fechaCreacion DESC")
    List<Guia> findAllGuiaEmitScanRep(@Param("centro") String centro);

    /**
     * Retorna todos las  {@link cl.cbb.gdeh.entities.Guia} emitidas, escaneadas, repasadas y escaneada-repasada.
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE (guia.guiaEstado.id = 5 OR guia.guiaEstado.id = 6 OR " +
            "guia.guiaEstado.id = 7 OR guia.guiaEstado.id = 8) " +
            "AND guia.fechaCreacion BETWEEN :fecha_inicio AND :fecha_final "+
            "AND guia.tick_ship_plant_loc_code = :centro ORDER BY guia.fechaCreacion DESC")
    List<Guia> findAllGuiaEmitScanRepByFechas(@Param("centro") String centro,
                                              @Param("fecha_inicio") LocalDateTime fecha_inicio,
                                              @Param("fecha_final") LocalDateTime fecha_final);

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.Guia} que posean un id igual al parametro
     * id.
     * @return
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE guia.id = :id")
    Guia getById(@Param("id") Long id);

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.Guia} que posean un folio igual al parametro.
     * @return
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE guia.folio_sii = :folio")
    Guia getByFolio(@Param("folio") String folio);

    /**
     * Retorna un {@link cl.cbb.gdeh.entities.Guia} que posean un tkt_date y tkt_code igual a los
     * parametros.
     * @return
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE guia.tick_order_date = :order_date " +
            "AND guia.tick_order_code = :order_code AND guia.tick_tkt_code = :tkt_code")
    Guia findGuiaByOrderDateAndOrderCodeAndTktCode(@Param("order_date") String order_date, @Param("order_code") String order_code,
                                       @Param("tkt_code") String tkt_code);

    /**
     * Retorna una lista de {@link cl.cbb.gdeh.entities.Guia} que posean un centro igual al parametro.
     * @return
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE guia.tick_ship_plant_loc_code = :cod_centro ORDER BY guia.fechaCreacion DESC")
    List<Guia> getByCodCentro(@Param("cod_centro") String cod_centro, Pageable pageable);

    /**
     * Retorna una lista de {@link cl.cbb.gdeh.entities.Guia} que esten en el rango de fecha.
     * @return
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE guia.tick_ship_plant_loc_code = :cod_centro AND " +
            "guia.fechaCreacion BETWEEN :fecha_inicio AND :fecha_final " +
            "ORDER BY guia.fechaCreacion DESC")
    List<Guia> getAllGuiasByRangeDate(@Param("cod_centro") String cod_centro,
                                      @Param("fecha_inicio") LocalDateTime fecha_inicio,
                                      @Param("fecha_final") LocalDateTime fecha_final );

    /**
     * Retorna una lista de {@link cl.cbb.gdeh.entities.GuiaRepaso} que esten en el rango de fecha y centro.
     * @return
     */
    @Query(value = "SELECT gr FROM Guia guia LEFT JOIN guia.guiaRepaso gr " +
            "WHERE guia.tick_ship_plant_loc_code = :cod_centro AND " +
            "guia.fechaCreacion BETWEEN :fecha_inicio AND :fecha_final " +
            "ORDER BY guia.fechaCreacion DESC")
    List<GuiaRepaso> getAllGuiasRepasoByRangeDate(@Param("cod_centro") String cod_centro,
                                                  @Param("fecha_inicio") LocalDateTime fecha_inicio,
                                                  @Param("fecha_final") LocalDateTime fecha_final );

    /**
     * Retorna una lista de {@link cl.cbb.gdeh.entities.Guia} que esten en el rango de fecha y estado.
     * @return
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE (guia.guiaEstado.id = 5 OR guia.guiaEstado.id = 6 OR " +
            " guia.guiaEstado.id = 7 OR guia.guiaEstado.id = 8) AND guia.tick_ship_plant_loc_code = :cod_centro AND " +
            "guia.fechaCreacion BETWEEN :fecha_inicio AND :fecha_final " +
            "ORDER BY guia.fechaCreacion DESC")
    List<Guia> getAllGuiasEmitScanRepAndRangoFecha(@Param("cod_centro") String cod_centro,
                                      @Param("fecha_inicio") LocalDateTime fecha_inicio,
                                      @Param("fecha_final") LocalDateTime fecha_final );

    /**
     * Retorna una total de {@link cl.cbb.gdeh.entities.Guia} con el estado y que
     * esten en el rango de fecha.
     * @return
     */
    @Query(value = "SELECT count(guia) FROM Guia guia WHERE guia.guiaEstado.estado = :estado AND " +
            "guia.tick_ship_plant_loc_code = :cod_centro AND " +
            "guia.fechaCreacion BETWEEN :fecha_inicio AND :fecha_final " +
            "ORDER BY guia.fechaCreacion DESC")
    Integer findTotalbyEstadoAndRangeDate(@Param("estado") String estado,
                                               @Param("cod_centro") String cod_centro,
                                               @Param("fecha_inicio") LocalDateTime fecha_inicio,
                                               @Param("fecha_final") LocalDateTime fecha_final );

    /**
     * Retorna una lista de {@link cl.cbb.gdeh.entities.Guia} que tengan el estado por parametro y
     * esten en el rango de fecha.
     * @return
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE guia.guiaEstado.estado = :estado AND " +
            "guia.tick_ship_plant_loc_code = :cod_centro AND " +
            "guia.fechaCreacion BETWEEN :fecha_inicio AND :fecha_final " +
            "ORDER BY guia.fechaCreacion DESC")
    List<Guia> getAllGuiasByEstadoAndRangeDate(@Param("estado") String estado,
                                      @Param("cod_centro") String cod_centro,
                                      @Param("fecha_inicio") LocalDateTime fecha_inicio,
                                      @Param("fecha_final") LocalDateTime fecha_final );

    /**
     * Retorna una lista de {@link cl.cbb.gdeh.entities.Guia} que tengan el estado por parametro y
     * esten dentro de la fecha.
     * @return
     */
    @Query(value = "SELECT guia FROM Guia guia WHERE guia.guiaEstado.estado = :estado AND " +
            "guia.tick_ship_plant_loc_code = :cod_centro AND " +
            "guia.fechaCreacion >= :fecha_inicio " +
            "ORDER BY guia.fechaCreacion DESC")
    List<Guia> getAllGuiasByEstadoAndDate(@Param("estado") String estado,
                                               @Param("cod_centro") String cod_centro,
                                               @Param("fecha_inicio") LocalDateTime fecha_inicio);
}
