package cl.cbb.gdeh.repositories;

import cl.cbb.gdeh.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    /**
     * Retorna todos los {@link Usuario}.
     */

    List<Usuario> findByOrderByIdAsc();

    /**
     * Retorna un {@link Usuario} que posean un id igual al parametro.
     * id.
     * @return
     */

    Optional<Usuario> findById(Long id);

    /**
     * Retorna un {@link Usuario} que posean un username igual al parametro.
     * id.
     * @return
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Retorna un {@link Usuario} que posean un email igual al parametro.
     * id.
     * @return
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Retorna un {@link Usuario} que posean un name igual al parametro.
     * id.
     * @return
     * @param name
     */
    Optional<Usuario> findByName(String name);

    /**
     * Retorna un valor true o false si es que existe el username como parametro de un  {@link Usuario}.
     * id.
     * @return
     */
    Boolean existsByUsername(String username);

    /**
     * Retorna un valor true o false si es que existe el name como parametro de un  {@link Usuario}.
     * id.
     * @return
     */
    Boolean existsByName(String name);

    /**
     * Retorna un valor true o false si es que existe el email como parametro de un  {@link Usuario}.
     * id.
     * @return
     */
    Boolean existsByEmail(String email);

    /**
     * Retorna un {@link Usuario} que posean un key_id igual al parametro.
     * @return
     */
    @Query(value = "SELECT usuario FROM Usuario usuario WHERE usuario.key_id = :key_id")
    Usuario getByKeycloakID(@Param("key_id") String key_id);

    /**
     * Retorna un {@link Usuario} que posean un key_id igual al parametro.
     * @return
     */
    @Query(value = "SELECT DISTINCT valor_dom FROM Rol WHERE usuario_id = :id")
    List<String> getCodCentroUser(@Param("id") Long id);
}

