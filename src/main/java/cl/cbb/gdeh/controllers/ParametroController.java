package cl.cbb.gdeh.controllers;

import cl.cbb.gdeh.services.ParametroService;
import cl.cbb.gdeh.ApiApplication;
import cl.cbb.gdeh.entities.Parametro;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import com.mashape.unirest.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin
@Controller
@RequestMapping(value="/api")
public class ParametroController {

    /**
     * Servicio utilizado para el manejo del parametro
     */
    @Autowired
    private ParametroService parametroService;

    private static Logger logger = LoggerFactory.getLogger(ParametroController.class);


    /**
     * Obtener todos los {@link Parametro} en la base de datos.
     * @return Lista de {@link Parametro}
     */
    @RequestMapping(value ="/parametros", method = RequestMethod.GET)
    @ResponseBody
    public List<Parametro> getAllParametro() {

        List<Parametro> parametros = parametroService.getAllParametro();
        return parametros;
    }

    /**
     * Obtener todos los {@link Parametro} en la base de datos por categoria "Global".
     */
    @RequestMapping(value ="/parametrosByAllCategoriesExceptExcepcion", method = RequestMethod.GET)
    @ResponseBody
    public List<Parametro> getAllParametroByCategoryGlobal() {

        List<Parametro> parametros = parametroService.getAllParametrosByCategoryExceptExcepcion();
        return parametros;
    }

    /**
     * Obtener todos los {@link Parametro} en la base de datos por categoria "Excepcion".
     */
    @RequestMapping(value ="/parametrosByCategoryExcepcion/{parametro}", method = RequestMethod.GET)
    @ResponseBody
    public List<Parametro> getAllParametroByCategoryExcepcion(@PathVariable String parametro) {

        //List<Parametro> parametros = parametroService.getAllParametroByCategory("Excepcion");
        List<Parametro> parametros = parametroService.getAllParametroByCategoryAndParams("Excepción",parametro);
        return parametros;
    }

    /**
     * Obtener un {@link Parametro} en especifico de la base de datos.
     */
    @RequestMapping(value ="/parametros/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Parametro getParametro(@PathVariable Long id) {

        Parametro parametro = parametroService.getParametroById(id);
        return parametro;
    }

    /**
     * Obtener todos los códigos de centro diferentes de {@link Parametro}.
     */
    @RequestMapping(value ="/parametrosAllCodCentro", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAllCodCentro() {

        List<String> centros = parametroService.getAllCodCentro();
        return centros;
    }

    /**
     * Obtener todos los códigos de centro diferentes de {@link Parametro}.
     */
    @RequestMapping(value ="/parametrosAllCodPlantaByCodCentro/{codCentro}", method = RequestMethod.GET)
    @ResponseBody
    public List<Object> getAllCodCentroAndColPlanta(@PathVariable String codCentro) {

        List<Object> centroPlantas = parametroService.getAllCodPlantaByCodCentro(codCentro);
        return centroPlantas;
    }

    /**
     * Obtener todos los laboratorios de {@link Parametro} en la base de datos.
     */
    @RequestMapping(value ="/parametros/laboratorios", method = RequestMethod.GET)
    @ResponseBody
    public List<Parametro> getLaboratorios() {

        return parametroService.getAllLaboratorios();
    }

    /**
     * Obtener todos los tipos de aditivos de {@link Parametro} en la base de datos.
     */
    @RequestMapping(value ="/parametros/aditivos", method = RequestMethod.GET)
    @ResponseBody
    public List<Parametro> getAditivos() {

        return parametroService.getAllAditivos();
    }

    /**
     * Guardar un {@link Parametro} en la base de datos.
     */
    @RequestMapping(value ="/parametros", method = RequestMethod.POST)
    @ResponseBody
    public Parametro saveParametro(@RequestBody Parametro parametro) {

        parametroService.saveParametro(parametro);
        return parametro;
    }

    /**
     * Modificar datos de un {@link Parametro} en especifico de la base de datos.
     */
    @RequestMapping(value ="/parametros/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Parametro updatePartialParametro(@PathVariable Long id,@RequestBody Parametro parametro) {

        Parametro antiguo =  parametroService.getParametroById(id);
        if(parametro.getCod_centro()!=null)
            antiguo.setCod_centro(parametro.getCod_centro());
        if(parametro.getCod_planta()!=null)
            antiguo.setCod_planta(parametro.getCod_planta());
        if(parametro.getCategoria()!=null)
            antiguo.setCategoria(parametro.getCategoria());
        if(parametro.getParametro()!=null)
            antiguo.setParametro(parametro.getParametro());
        if(parametro.getValor()!=null)
            antiguo.setValor(parametro.getValor());
        if(parametro.getActivo()!=null)
            antiguo.setActivo(parametro.getActivo());
        if(parametro.getIdParametroRef()!=null)
            antiguo.setIdParametroRef(parametro.getIdParametroRef());

        parametroService.saveParametro(antiguo);

        return antiguo;
    }

    /**
     * Obtener un {@link Parametro} en especifico de la base de datos.
     */
    @RequestMapping(value ="/parametrosByName", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> getParametroByName(@RequestBody Map<String,String> params) {
        String parametro = parametroService.findDefaultValorByParametroAndPlanta(params.get("name"),
                params.get("planta"));
        if(parametro != null){
            logger.info("El usuario Test ha accedido al recurso Parametro con nombre " +params.get("name")+
                    " y centro "+params.get("centro")+" con exito");
        }
        else {
            logger.info("El usuario Test ha tratado de acceder a un recurso Parametro con nombre " + params.get("name") +
                    "y centro " + params.get("centro") + " sin exito");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(parametro, HttpStatus.OK);
    }

    /**
     * Obtener la URL Listen del {@link Parametro} en la base de datos.
     */
    /*@RequestMapping(value ="/parametroURListen", method = RequestMethod.GET)
    @ResponseBody
    public Parametro getURListen() {
        System.out.println("Entro aqui");
        return parametroService.findURListen();
    }*/

    /**
     * Obtener la URL Listen del {@link Parametro} en la base de datos.
     */
    @RequestMapping(value ="/parametros/UF", method = RequestMethod.GET)
    @ResponseBody
    public String getUFActual() {

        Parametro parametro = parametroService.findByParametro("ValorUF");

        List<String> listValue = new ArrayList<String>(Arrays.asList(parametro.getValor().split("-")));

        String valor = null;
        String fecha = listValue.get(1)+"-"+listValue.get(2)+"-"+listValue.get(3);

        String fechaActual = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if(fecha.equals(fechaActual))
            valor = listValue.get(0);

        else{

            try {
                String api_key = parametroService.findDefaultValorByParametro("API KEY SBIF");

                HttpResponse<String> response = Unirest.get("https://api.sbif.cl/api-sbifv3/recursos_api/uf?apikey="+api_key+
                        "&formato=xml")
                        .asString();

                JSONObject jsonObject = XML.toJSONObject(response.getBody());
                fecha = jsonObject.getJSONObject("IndicadoresFinancieros").getJSONObject("UFs").getJSONObject("UF").get("Fecha").toString();
                valor = jsonObject.getJSONObject("IndicadoresFinancieros").getJSONObject("UFs").getJSONObject("UF").get("Valor").toString();

                parametro.setValor(valor+"-"+fecha);
                parametroService.saveParametro(parametro);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*System.out.println(fecha);
        System.out.println(fechaActual);
        System.out.println(fecha.equals(fechaActual));*/

        return valor;
    }

    /**
     * Obtener la valor de un {@link Parametro} en la base de datos.
     */
    @RequestMapping(value ="/parametrosByValor", method = RequestMethod.POST)
    @ResponseBody
    public String getValorParametro(@RequestBody Map<String,String> params) {
        return parametroService.findDefaultValorByParametro(params.get("valor"));
    }

    /**
     * Cambiar el ambiente de QA a PRD y viceversa de ciertos {@link Parametro} en la base de datos.
     */
    @RequestMapping(value ="/parametros/switch", method = RequestMethod.POST)
    @ResponseBody
    public void switchParametro(@RequestBody Map<String,String> params) {
        //System.out.println(params.get("env"));

        String env = params.get("env");
        try {

            if(env.equals("QA")){

                Parametro parametro = parametroService.findByParametro("URL IConstruye");
                String qa = parametroService.findDefaultValorByParametro("URL IConstruyeQA");
                parametro.setValor(qa);
                parametroService.saveParametro(parametro);

                Parametro cmdSqlServer = parametroService.findByParametro("cmdSqlServer");
                String cmdSqlServerQA = parametroService.findDefaultValorByParametro("cmdSqlServerQA");
                cmdSqlServer.setValor(cmdSqlServerQA);
                parametroService.saveParametro(cmdSqlServer);

                Parametro cmdSqlDb = parametroService.findByParametro("cmdSqlDb");
                String cmdSqlDbQA = parametroService.findDefaultValorByParametro("cmdSqlDbQA");
                cmdSqlDb.setValor(cmdSqlDbQA);
                parametroService.saveParametro(cmdSqlDb);

                Parametro cmdSqlUser = parametroService.findByParametro("cmdSqlUser");
                String cmdSqlUserQA = parametroService.findDefaultValorByParametro("cmdSqlUserQA");
                cmdSqlUser.setValor(cmdSqlUserQA);
                parametroService.saveParametro(cmdSqlUser);

                Parametro cmdSqlPass = parametroService.findByParametro("cmdSqlPass");
                String cmdSqlPassQA = parametroService.findDefaultValorByParametro("cmdSqlPassQA");
                cmdSqlPass.setValor(cmdSqlPassQA);
                parametroService.saveParametro(cmdSqlPass);

            }
            else if(env.equals("PRD")){

                Parametro parametro = parametroService.findByParametro("URL IConstruye");
                String prd = parametroService.findDefaultValorByParametro("URL IConstruyePRD");
                parametro.setValor(prd);

                Parametro cmdSqlServer = parametroService.findByParametro("cmdSqlServer");
                String cmdSqlServerPRD = parametroService.findDefaultValorByParametro("cmdSqlServerPRD");
                cmdSqlServer.setValor(cmdSqlServerPRD);
                parametroService.saveParametro(cmdSqlServer);

                Parametro cmdSqlDb = parametroService.findByParametro("cmdSqlDb");
                String cmdSqlDbPRD = parametroService.findDefaultValorByParametro("cmdSqlDbPRD");
                cmdSqlDb.setValor(cmdSqlDbPRD);
                parametroService.saveParametro(cmdSqlDb);

                Parametro cmdSqlUser = parametroService.findByParametro("cmdSqlUser");
                String cmdSqlUserPRD = parametroService.findDefaultValorByParametro("cmdSqlUserPRD");
                cmdSqlUser.setValor(cmdSqlUserPRD);
                parametroService.saveParametro(cmdSqlUser);

                Parametro cmdSqlPass = parametroService.findByParametro("cmdSqlPass");
                String cmdSqlPassPRD = parametroService.findDefaultValorByParametro("cmdSqlPassPRD");
                cmdSqlPass.setValor(cmdSqlPassPRD);
                parametroService.saveParametro(cmdSqlPass);

            }

            else{
                logger.error("Se ha tratado de realizar cambio de ambiente con el parametro "+env+" sin exito");
            }

        }catch (Exception e){
            logger.error("Se ha tratado de realizar cambio de ambiente sin exito, e: ",e);
        }

    }

    /**
     * Modificar datos de un {@link Parametro} en especifico de la base de datos.
     */
    @RequestMapping(value ="/parametros/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteLogicParametro(@PathVariable Long id) {
        parametroService.deleteLogicParametro(id);
    }
}
