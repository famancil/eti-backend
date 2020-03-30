package cl.cbb.gdeh.services;

import cl.cbb.gdeh.entities.GuiaEstado;
import cl.cbb.gdeh.repositories.GuiaEstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GuiaEstadoService {

    /**
     * Repositorio para el manejo CRUD de la entidad {@link GuiaEstado}
     */
    @Autowired
    private GuiaEstadoRepository guiaEstadoRepository;

    /**
     * Obtiene el {@link GuiaEstado} segun el id que se pase como parametro.
     */

    public GuiaEstado getGuiaEstadoById(Long idGuiaEstado){
        Optional<GuiaEstado> guiaEstado = guiaEstadoRepository.findById(idGuiaEstado);
        if(guiaEstado!=null){
            return guiaEstado.get();
        }
        else return null;

    }

    /**
     * Obtiene el {@link GuiaEstado} segun el id que se pase como parametro.
     */

    public List<GuiaEstado> getAllGuiaEstado(){
        List<GuiaEstado> guiaEstados = guiaEstadoRepository.findByOrderByIdAsc();
        if(guiaEstados!=null){
            return guiaEstados;
        }
        else return null;
    }

    /**
     * Obtiene el {@link GuiaEstado} segun el id que se pase como parametro.
     */
    public GuiaEstado getByEstado(String estado){
        return guiaEstadoRepository.findByEstado(estado).get();
    }

    /**
     * Guarda un {@link GuiaEstado} nuevo en la base de datos.
     */
    public void saveGuiaEstado(GuiaEstado guiaEstado){

        // si estado es nulo se setea como ""
        if ( guiaEstado.getEstado() == null ) {
            guiaEstado.setEstado("");
        }

        // si descripcion es nulo se setea como ""
        if ( guiaEstado.getDescripcion() == null ) {
            guiaEstado.setDescripcion("");
        }

        guiaEstadoRepository.save(guiaEstado);
    }
}
