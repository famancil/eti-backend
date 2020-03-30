package cl.cbb.gdeh.repositories;

import cl.cbb.gdeh.entities.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParametroRepository extends JpaRepository<Parametro,Long>{

    /**
     * Retorna todos los  {@link Parametro}.
     */

    List<Parametro> findByOrderByIdAsc();

    /**
     * Retorna un {@link Parametro} que posean un id igual al parametro.
     * id.
     * @return
     */

    Optional<Parametro> findById(Long id);

    /**
     * Retorna un {@link Parametro} que posean un parametro especifico.
     * id.
     * @return
     */

    Optional<Parametro> findByParametro(String parametro);

    /**
     * Obtiene todos los {@link Parametro} por categoria.
     *
     * @return {@link java.util.List} con todas las entidades {@link Parametro}.
     */
    @Query(value = "SELECT param FROM Parametro param WHERE param.categoria = :categoria AND param.activo=1")
    List<Parametro> findByCategoria(@Param("categoria") String categoria);

    /**
     * Obtiene todos los {@link Parametro} por categoria.
     *
     * @return {@link java.util.List} con todas las entidades {@link Parametro}.
     */
    @Query(value = "SELECT param FROM Parametro param WHERE param.parametro = :parametro AND param.activo=1")
    List<Parametro> getAllByValor(@Param("parametro") String parametro);

    /**
     * Obtiene todos los {@link Parametro} por categoria y parametro.
     *
     * @return {@link java.util.List} con todas las entidades {@link Parametro}.
     */
    @Query(value = "SELECT param FROM Parametro param WHERE param.categoria = :categoria " +
            "AND param.parametro = :parametro AND param.activo=1")
    List<Parametro> findByCategoriaAndParametro(@Param("categoria") String categoria,@Param("parametro") String parametro);

    /**
     * Obtiene todos los {@link Parametro} por categoria.
     *
     * @return {@link java.util.List} con todas las entidades {@link Parametro}.
     */
    @Query(value = "SELECT param FROM Parametro param WHERE param.categoria <> 'Excepción'")
    List<Parametro> findAllCategoriasExceptExcepcion();

    /**
     * Obtiene todos los codigos de centro diferentes de {@link Parametro}
     *
     * @return {@link java.util.List} con todos los codigos de centro diferentes de {@link Parametro}.
     */
    @Query(value = "SELECT DISTINCT p.codCentro FROM Plantas p")
    List<String> findAllCodCentro();

    /**
     * Obtiene todos los codigos de planta y de centro diferentes de {@link Parametro}
     *
     * @return {@link java.util.List} con todos los codigos de planta y de centro diferentes de {@link Parametro}.
     */
    @Query(value = "SELECT DISTINCT p.codCentro, p.codPlanta FROM Plantas p WHERE cod_centro = :cod_centro")
    List<Object> findAllCodPlantaByCodCentro(@Param("cod_centro") String cod_centro);

    /**
     * Obtiene un {@link Parametro} especifico por el parametro y centro.
     *
     * @return {@link Parametro}.
     */
    @Query(value = "SELECT param FROM Parametro param WHERE param.parametro = :parametro AND param.cod_planta = :cod_planta " +
            "AND param.activo=1 AND param.categoria = 'Excepción' ")
    Parametro findExcepcionByParametroAndCodPlanta(@Param("parametro") String parametro,
                                                 @Param("cod_planta") String cod_planta);

    /**
     * Obtiene una excepcion de un {@link Parametro} especifico por el parametro y centro.
     *
     * @return {@link Parametro}.
     */
    @Query(value = "SELECT param FROM Parametro param WHERE param.parametro = :parametro " +
            "AND param.activo=1 AND param.categoria = 'Global' ")
    Parametro findGlobalByParametro(@Param("parametro") String parametro);

    /**
     * Obtiene el parametro URL listen de {@link Parametro}
     *
     * @return {@link Parametro}.
     */
    @Query(value = "SELECT param FROM Parametro param WHERE param.parametro = 'URL Listen' " +
            "AND param.activo=1")
    Parametro findURListen();

    /**
     * Obtiene el parametro de un valor especifico.
     */
    @Query("SELECT p.valor FROM Parametro p where p.parametro = :parametro and cod_centro = '*' and cod_planta = '*'")
    String findDefaultValorByParametro(@Param("parametro") String parametro);

    /**
     * Obtiene el parametro de un valor especifico.
     */
    @Query("SELECT p.valor FROM Parametro p where p.parametro = :parametro and cod_planta = :cod_planta")
    String findDefaultValorByParametroAndCodPlanta(@Param("parametro") String parametro,@Param("cod_planta") String cod_planta);
}
