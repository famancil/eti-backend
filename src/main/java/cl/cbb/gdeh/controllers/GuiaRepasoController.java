package cl.cbb.gdeh.controllers;

import cl.cbb.gdeh.config.JDBCCmd;
import cl.cbb.gdeh.entities.*;
import cl.cbb.gdeh.services.GuiaEstadoService;
import cl.cbb.gdeh.services.GuiaRepasoService;
import cl.cbb.gdeh.services.GuiaService;
import cl.cbb.gdeh.services.ParametroService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.options.Option;
import com.mashape.unirest.http.options.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping(value="/api")
public class GuiaRepasoController {

    /**
     * Logger del controlador de GuiaRepaso.
     */
    private static final Logger logger = LogManager.getLogger(GuiaRepasoController.class);

    /**
     * Servicio utilizado para el manejo del guia estado.
     */
    @Autowired
    private GuiaEstadoService guiaEstadoService;

    /**
     * Servicio utilizado para el manejo del guia repaso.
     */
    @Autowired
    private GuiaRepasoService guiaRepasoService;

    /**
     * Servicio utilizado para el manejo del guia
     */
    @Autowired
    private GuiaService guiaService;

    /**
     * Servicio utilizado para el manejo del parametro
     */
    @Autowired
    private ParametroService parametroService;

    /**
     * Obtener un {@link GuiaRepaso} de la base de datos.
     * @param id
     * @return
     */
    @RequestMapping(value ="/guiaRepasos/{id}", method = RequestMethod.GET)
    @ResponseBody
    public GuiaRepaso getGuiaRepaso(@PathVariable Long id ) {
        return guiaRepasoService.getGuiaRepasoByGuiaId(id);
    }

    /**
     * Guardar un {@link GuiaRepaso} en la base de datos.
     * @param id
     * @param guiaRepaso
     * @return
     */
    @RequestMapping(value ="/guiaRepasos/{id}", method = RequestMethod.POST)
    @ResponseBody
    public void saveGuiaRepaso(@RequestBody GuiaRepaso guiaRepaso, @PathVariable Long id ){

        System.out.println(guiaRepaso.getFolios_ok());

        /*HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> entity = new HttpEntity<String>(json ,headers);*/

        if(guiaRepaso.getFolios_ok() == 0){
            guiaRepasoService.saveGuiaRepaso(guiaRepaso, id);
            logger.info("Se ha creado un recurso GuiaRepaso con folio_coincidente falso con exito");
        }
        else{
            try {

                if (guiaRepaso.getHora_carga().matches("(.*)null(.*)"))
                    guiaRepaso.setHora_carga(null);
                if (guiaRepaso.getSalida_planta().matches("(.*)null(.*)"))
                    guiaRepaso.setSalida_planta(null);
                if (guiaRepaso.getLlegada_obra().matches("(.*)null(.*)"))
                    guiaRepaso.setLlegada_obra(null);
                if (guiaRepaso.getInicio_descarga().matches("(.*)null(.*)"))
                    guiaRepaso.setInicio_descarga(null);
                if (guiaRepaso.getSalida_obra().matches("(.*)null(.*)"))
                    guiaRepaso.setSalida_obra(null);
                if (guiaRepaso.getIngreso_planta().matches("(.*)null(.*)"))
                    guiaRepaso.setIngreso_planta(null);

                //TO DO descomentar esto para hacer el cambio a repasada.
                guiaRepasoService.saveGuiaRepaso(guiaRepaso, id);

                Guia guia = guiaService.getGuiaById(id);
                if (guia.getGuiaEstado().getId() != 7 && guia.getGuiaEstado().getId() != 8) {
                    GuiaEstado guiaEstado = null;
                    if (guia.getGuiaEstado().getId() == 6)
                        guiaEstado = guiaEstadoService.getGuiaEstadoById((long) 8);
                    else
                        guiaEstado = guiaEstadoService.getGuiaEstadoById((long) 7);

                    guia.setGuiaEstado(guiaEstado);
                    guiaService.save(guia);

                    GuiaHistorial guiaHistorial = new GuiaHistorial(null, guiaRepaso.getUsuario(), null);
                    guiaHistorial.setGuia(guia);
                    guiaHistorial.setGuiaEstado(guiaEstado);

                    guiaService.saveHistorial(guiaHistorial);
                }

                //Lo del Leo

                guiaRepaso.setGuia(guiaService.getGuiaById(id));
                guiaRepaso.setId(id);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(guiaRepaso);


                String URLProxy = parametroService.findDefaultValorByParametro(
                        "Ruta Proxy DTE");

                Unirest.post(URLProxy + "guiaRepasos")
                        .header("Content-Type", "application/json")
                        .body(json)
                        .asJson();

                logger.info("Se ha creado un recurso GuiaRepaso con id "+guiaRepaso.getId()+" y con parametros " +
                        id+" con exito");

            } catch (Exception e) {
                logger.error("Error en mandar repaso: ", e);
            }
        }
        /*try {
            JDBCCmd cmdDb = new JDBCCmd(parametroService.findDefaultValorByParametro("cmdSqlServer"),
                    parametroService.findDefaultValorByParametro("cmdSqlDb"),
                    parametroService.findDefaultValorByParametro("cmdSqlUser"),
                    parametroService.findDefaultValorByParametro("cmdSqlPass"));
            cmdDb.saveGuiaRepasoCmd(guiaRepaso,guiaService.getGuiaById(id));

        }catch (Exception e){
            e.printStackTrace();
            //logger.error("Error en mandar correo con guia adjunta: ",e);
        }*/


    }

    /**
     * Devolver los tiempos de {@link GuiaRepaso} de Command.
     * @param id
     * @return
     */
    @RequestMapping(value ="/guiaRepasos/{id}/tiempos", method = RequestMethod.GET)
    @ResponseBody
    public GuiaRepaso getTiempoGuiaRepaso(@PathVariable Long id ){
        GuiaRepaso guiaRepaso = new GuiaRepaso();
        try {
            JDBCCmd cmdDb = new JDBCCmd(parametroService.findDefaultValorByParametro("cmdSqlServer"),
                    parametroService.findDefaultValorByParametro("cmdSqlDb"),
                    parametroService.findDefaultValorByParametro("cmdSqlUser"),
                    parametroService.findDefaultValorByParametro("cmdSqlPass"));

            cmdDb.getTiemposRepaso(guiaRepaso,guiaService.getGuiaById(id));

        }catch (Exception e){
            logger.error("Error en obtener tiempos de CmdSeriesQA: ", e);

        }
        logger.info("Se ha obtenido con exito los tiempos del recurso GuiaRepaso con guia id "+
                id+" de forma exitosa");
        return guiaRepaso;
    }

}
