package cl.cbb.gdeh.services;

import cl.cbb.gdeh.entities.Parametro;
import cl.cbb.gdeh.entities.TickEstado;
import cl.cbb.gdeh.repositories.ParametroRepository;
import cl.cbb.gdeh.repositories.TickEstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TickEstadoService {

    /**
     * Repositorio para el manejo CRUD de la entidad {@link TickEstado}
     */
    @Autowired
    private TickEstadoRepository tickEstadoRepository;

    /**
     * Obtiene el {@link TickEstado} segun el id que se pase como parametro.
     */

    public TickEstado getTickEstadoById(Long idTickEstado){
        Optional<TickEstado> tickEstado = tickEstadoRepository.findById(idTickEstado);
        if(tickEstado!=null){
            return tickEstado.get();
        }
        else return null;

    }

    /**
     * Obtiene el {@link TickEstado} segun el id que se pase como parametro.
     */

    public List<TickEstado> getAllTickEstado(){
        List<TickEstado> tickEstados = tickEstadoRepository.findByOrderByIdAsc();
        if(tickEstados!=null){
            return tickEstados;
        }
        else return null;
    }

    /**
     * Guarda un {@link TickEstado} nuevo en la base de datos.
     */
    public void saveTickEstado(TickEstado tickEstado){

        // si estado es nulo se setea como ""
        if ( tickEstado.getEstado() == null ) {
            tickEstado.setEstado("");
        }

        // si descripcion es nulo se setea como ""
        if ( tickEstado.getDescripcion() == null ) {
            tickEstado.setDescripcion("");
        }

        tickEstadoRepository.save(tickEstado);
    }
}
