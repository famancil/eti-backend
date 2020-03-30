package cl.cbb.gdeh.services;

import cl.cbb.gdeh.controllers.GuiaRepasoController;
import cl.cbb.gdeh.entities.Guia;
import cl.cbb.gdeh.entities.GuiaRepaso;
import cl.cbb.gdeh.repositories.GuiaRepasoRepository;
import cl.cbb.gdeh.repositories.GuiaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class GuiaRepasoService {

    /**
     * Logger del servicio de GuiaRepaso.
     */
    private static final Logger logger = LogManager.getLogger(GuiaRepasoService.class);

    /**
     * Repositorio para el manejo CRUD de la entidad {@link GuiaRepaso}
     */
    @Autowired
    private GuiaRepasoRepository guiaRepasoRepository;

    /**
     * Repositorio para el manejo CRUD de la entidad {@link Guia}
     */
    @Autowired
    private GuiaRepository guiaRepository;

    /**
     * Obtiene el {@link GuiaRepaso} segun el id de la guia que se pase como parametro.
     */

    public GuiaRepaso getGuiaRepasoByGuiaId(Long idGuia){
        return guiaRepasoRepository.findByGuiaId(idGuia);
    }

    /**
     * Obtiene el {@link GuiaRepaso} segun el id que se pase como parametro.
     */

    public GuiaRepaso getGuiaRepasoById(Long idGuiaRepaso){
        Optional<GuiaRepaso> guiaRepaso = guiaRepasoRepository.findById(idGuiaRepaso);
        if(guiaRepaso!=null){
            return guiaRepaso.get();
        }
        else return null;

    }

    /**
     * Obtiene el {@link GuiaRepaso} segun el id que se pase como parametro.
     */

    public List<GuiaRepaso> getAllGuiaRepaso(){
        List<GuiaRepaso> guiaRepasos = guiaRepasoRepository.findByOrderByIdAsc();
        if(guiaRepasos!=null){
            return guiaRepasos;
        }
        else return null;
    }

    /**
     * Guarda un {@link GuiaRepaso} nuevo en la base de datos.
     */
    public void saveGuiaRepaso(GuiaRepaso guiaRepaso, Long idGuia){

        //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //Date fechaActual = new java.util.Date();
        /*System.out.println("Pasa por aqui al empezar ");

        System.out.println("Pasa por aqui: "+idGuia);*/
        //System.out.println((guiaRepository.findById(idGuia).get().getId()));

        //ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            //System.out.println(guiaRepository.getById(idGuia).getGuiaRepaso() != null);
            GuiaRepaso nueva = null;
            if(guiaRepository.getById(idGuia).getGuiaRepaso() != null)
                nueva = guiaRepository.getById(idGuia).getGuiaRepaso();
            else
                nueva = new GuiaRepaso();

            nueva.setObs(guiaRepaso.getObs());
            nueva.setUsuario(guiaRepaso.getUsuario());

            nueva.setHora_carga(guiaRepaso.getHora_carga());
            nueva.setSalida_planta(guiaRepaso.getSalida_planta());
            nueva.setLlegada_obra(guiaRepaso.getLlegada_obra());
            nueva.setInicio_descarga(guiaRepaso.getInicio_descarga());
            nueva.setSalida_obra(guiaRepaso.getSalida_obra());
            nueva.setIngreso_planta(guiaRepaso.getIngreso_planta());

            nueva.setSolicitud_nc(guiaRepaso.getSolicitud_nc());
            nueva.setRc_nombre(guiaRepaso.getRc_nombre());
            nueva.setRc_rut(guiaRepaso.getRc_rut());
            nueva.setRc_firmado(guiaRepaso.getRc_firmado());

            nueva.setFolios_ok(guiaRepaso.getFolios_ok());

            nueva.setAc_agua(guiaRepaso.getAc_agua());
            nueva.setAc_solicitante_agua(guiaRepaso.getAc_solicitante_agua());
            nueva.setAc_firmado_agua(guiaRepaso.getAc_firmado_agua());

            nueva.setAc_adit_cant(guiaRepaso.getAc_adit_cant());
            nueva.setAc_solicitante_adit(guiaRepaso.getAc_solicitante_adit());
            nueva.setAc_firmado_adit(guiaRepaso.getAc_firmado_adit());

            nueva.setAc_tipo_adit(guiaRepaso.getAc_tipo_adit());
            nueva.setAc_solicitante_tipo_adit(guiaRepaso.getAc_solicitante_tipo_adit());
            nueva.setAc_firmado_tipo_adit(guiaRepaso.getAc_firmado_tipo_adit());

            nueva.setAc_tiempo_camion(guiaRepaso.getAc_tiempo_camion());
            nueva.setAc_solicitante_tiempo_camion(guiaRepaso.getAc_solicitante_tiempo_camion());
            nueva.setAc_firmado_tiempo_camion(guiaRepaso.getAc_firmado_tiempo_camion());

            nueva.setAc_hormigon(guiaRepaso.getAc_hormigon());
            nueva.setAc_solicitante_hormigon(guiaRepaso.getAc_solicitante_hormigon());
            nueva.setAc_firmado_hormigon(guiaRepaso.getAc_firmado_hormigon());

            nueva.setMue_propia(guiaRepaso.getMue_propia());
            nueva.setMue_cliente(guiaRepaso.getMue_cliente());

            nueva.setMue_laboratorio(guiaRepaso.getMue_laboratorio());
            nueva.setMue_numero(guiaRepaso.getMue_numero());
            nueva.setMue_asentamiento(guiaRepaso.getMue_asentamiento());

            nueva.setCamion_devuelto(guiaRepaso.getCamion_devuelto());

            nueva.setGuia(guiaRepository.getById(idGuia));

            nueva.setFechaActualizacion(LocalDateTime.now());

            /*String json = ow.writeValueAsString(nueva);

            System.out.println("Nueva :"+json);*/

            guiaRepasoRepository.save(nueva);
        } catch (Exception e) {
            logger.error("Error en guardar Guia Repaso: ", e);
        }
        //guiaRepasoRepository.save(nueva);*/

    }
}
