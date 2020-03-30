package cl.cbb.gdeh.controllers;

import cl.cbb.gdeh.config.ClientAppConfig;
import cl.cbb.gdeh.config.DeliveryObraAppConfig;
import cl.cbb.gdeh.config.DeliveryObraClient;
import cl.cbb.gdeh.config.JDBCCmd;
import cl.cbb.gdeh.entities.*;
import cl.cbb.gdeh.message.response.JwtResponse;
import cl.cbb.gdeh.repositories.GuiaDetalleRepository;
import cl.cbb.gdeh.repositories.GuiaEstadoOpcionRepository;
import cl.cbb.gdeh.services.*;
import cl.cbb.gdeh.zesales.delivery.obra.v3.client.ZESALESDELIVERYOBRAV3RSP;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.mail.internet.MimeMessage;
import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(value="/api")
public class GuiaController {

    /**
     * Logger del controlador de Guia.
     */
    private static final Logger logger = LogManager.getLogger(GuiaController.class);

    /**
     * Servicio utilizado para el manejo del guia
     */
    @Autowired
    private GuiaService guiaService;

    /**
     * Servicio utilizado para el manejo del TickEstado.
     */
    @Autowired
    private TickEstadoService tickEstadoService;

    /**
     * Servicio utilizado para el manejo del GuiaEstado.
     */
    @Autowired
    private GuiaEstadoService guiaEstadoService;

    /**
     * Servicio utilizado para el manejo del Parametro
     */
    @Autowired
    private ParametroService parametroService;

    /**
     * Lista de receptores de eventos para las notificaciones hechas por el servidor
     */
    private final List<SseEmitter> sseEmitters = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    ResourceLoader resourceLoader;

    /**
     * Servicio utilizado para el manejo del Guia Estado Opcion
     */

    @Autowired
    private GuiaEstadoOpcionRepository guiaEstadoOpcionRepository;

    /**
     * Servicio utilizado para el manejo del Guia Detalle
     */

    @Autowired
    private GuiaDetalleRepository guiaDetalleRepository;

    /**
     * Obtener un {@link Guia} en especifico de la base de datos.
     * @return {@link Guia}
     */
    @RequestMapping(value ="/guias/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Guia getGuia(@PathVariable Long id) {
        Guia guia = guiaService.getGuiaById(id);
        //System.out.println(guia.getFolio_sii());
        try {
            if (guia != null) {
                logger.info("El usuario Test ha accedido al recurso Guia con folio " + guia.getFolio_sii());
                //guia.getGuiaEstado().setGuiaEstadoOpcion(guiaEstadoOpcionRepository.findByGuiaEstado(guia.getGuiaEstado()));
                //guia.setGuiaHistorials(guiaService.getAllGuiasHistoricas(guia));
                System.out.println(guiaDetalleRepository.findByGuiaId(guia.getId()));
                guia.setGuiaDetalle(guiaDetalleRepository.findByGuiaId(guia.getId()));
                //System.out.println(guia.getGuiaDetalle().get(0).getProd_code());
            } else
                logger.info("El usuario Test ha tratado de acceder a un recurso Guia con id " + id + " sin exito");
        }catch (Exception e){
            logger.error("Error en acceder al recurso Guia: ",e);
        }

        Emisor prueba = Emisor.getInstance();
        List<SseEmitter> sseEmitters = prueba.getSseEmitters();
        synchronized (sseEmitters) {
            for (SseEmitter sseEmitter : sseEmitters) {
                try {
                    sseEmitter.send(guia.getId(), MediaType.APPLICATION_JSON);
                    sseEmitter.complete();
                } catch (Exception e) {
                    //???
                }
            }
        }

        return guia;
    }

    /**
     * Obtener todas las {@link Guia} de la base de datos.
     * @return Lista de {@link Guia}.
     */
    @RequestMapping(value ="/guias", method = RequestMethod.GET)
    @ResponseBody
    public List<Guia> getAllGuia(){

        List<Guia> guias = guiaService.getAllGuiaAndGuiaRepaso();
        try {
            for (Guia guia : guias) {
                guia.getGuiaEstado().setGuiaEstadoOpcion(guiaEstadoOpcionRepository.findByGuiaEstado(guia.getGuiaEstado()));
                guia.setGuiaDetalle(guiaDetalleRepository.findByGuiaId(guia.getId()));
            }
            if (guias != null)
                logger.info("El usuario Test ha accedido a todos los recursos de Guia con exito");
            else
                logger.info("El usuario Test ha tratado de acceder a todos los recursos de Guia sin exito");
        }
        catch (Exception e){
            logger.error("Error en acceder al recurso Guia: ",e);
        }

        return guias;
    }

    /**
     * Obtener un {@link GuiaEstado} a partir de su ID como parametro en la base de datos.
     * @param id
     * @return {@link GuiaEstado}
     */
    @RequestMapping(value ="/guiaEstados/{id}", method = RequestMethod.GET)
    @ResponseBody
    public GuiaEstado getGuiaEstado(@PathVariable Long id) {

        GuiaEstado guiaEstado = guiaEstadoService.getGuiaEstadoById(id);
        if(guiaEstado!=null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                logger.info("El usuario Test ha accedido al recurso Guia Estado con datos " + mapper.writeValueAsString(guiaEstado)
                        + " con exito");
            }catch(Exception ex) {
                logger.error("Error en obtener GuiaEstado: ",ex);
            }
        }
        else
            logger.info("El usuario Test ha tratado de acceder al recurso Guia Estado con id " + id + " sin exito");

        return guiaEstado;
    }

    /**
     * Obtener un {@link Guia} por el parametro del folio en la base de datos.
     * @param folio
     * @return {@link Guia}
     */
    @RequestMapping(value ="/guiasByFolio/{folio}", method = RequestMethod.GET)
    @ResponseBody
    public Guia getGuiaByFolio(@PathVariable String folio) {

        Guia guia = guiaService.getGuiaByFolio(folio);
        try {
            if (guia != null) {
                guia.getGuiaEstado().setGuiaEstadoOpcion(guiaEstadoOpcionRepository.findByGuiaEstado(guia.getGuiaEstado()));
                logger.info("El usuario Test ha accedido al recurso Guia con folio " + guia.getFolio_sii());
            } else
                logger.info("El usuario Test ha tratado de acceder a un recurso Guia con folio " + folio + " sin exito");

        }catch (Exception e) {
            logger.error("Error en acceder al recurso Guia: ",e);
        }
        return guia;
    }

    /**
     * Obtener la {@link Guia} electronica determinada por folio.
     * @param folio
     * @return URL de Guia Electrónica
     */
    @RequestMapping(value ="/guiasPDF/{tipodoc}/{fecha}/{folio}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getGuiaPDF(@PathVariable String tipodoc,
                                                          @PathVariable String fecha,
                                                          @PathVariable String folio) {

        String URLPDF = null;
        String URLPDFScan = parametroService.findDefaultValorByParametro("Ruta Guia Escaneada");
        String urlPDF = null;
        Resource pdfFile = null;
        fecha = Arrays.asList(fecha.split("T")).get(0);

        //System.out.println(Arrays.asList(dateParam.toString().split("-")));
        //ClassPathResource pdfFile = null;

        try {
            /*Guia guia = guiaService.getGuiaByFolio(folio);
            guia.getGuiaEstado().setGuiaEstadoOpcion(guiaEstadoOpcionRepository.findByGuiaEstado(guia.getGuiaEstado()));

            List<String> myList = new ArrayList<String>(Arrays.asList((guia.getTick_order_date().split("-"))));
            urlPDF = URLPDF
                    + myList.get(0) + "/" + myList.get(1) + "/" + myList.get(2) + "/" + guia.getFolio_sii() + ".pdf";*/

            List<String> myList = new ArrayList<String>(Arrays.asList(fecha.split("-")));

            //TO DO: Rescatar la URL segun el rango de fecha (Segun rango, ir a buscar al repositorio especifico)
            //Eso esta en el correo.

            //2019-10-08
            /*if(Integer.valueOf(myList.get(0)) < 2019 ) {
                System.out.println("Se va a la URL 1"); //10.ALGO
                URLPDF = parametroService.findDefaultValorByParametro("Ruta Guia Electronica 2");
            }*/

            //if(tipodoc.equals("cliente")){
            if(Integer.valueOf(myList.get(0)) == 2019 && Integer.valueOf(myList.get(1)) <= 8 &&
                    Integer.valueOf(myList.get(2)) <= 31) {
                System.out.println("Se va a la URL 2");
                URLPDF = parametroService.findDefaultValorByParametro("Ruta Guia Electronica 2");
            }

            else{
                System.out.println("Se va a la URL 1");
                URLPDF = parametroService.findDefaultValorByParametro("Ruta Guia Electronica 1");

            }
            //}

            if(tipodoc.equals("cedible")) {
                urlPDF = URLPDF + myList.get(0) + "/" + myList.get(1) + "/" + myList.get(2) + "/" +
                        folio + "-Cedible.pdf";
            }
            else {
                urlPDF = URLPDF + myList.get(0) + "/" + myList.get(1) + "/" + myList.get(2) + "/" +
                        folio + ".pdf";
            }
            //System.out.println(urlPDF);
            URL u = new URL(urlPDF);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();
            //System.out.println("codigo " +code);
            if (code == 404 || code == 503) {
                logger.info("Se ha tratado de acceder al recurso DTE con folio "
                        + folio + " y fecha "+fecha+" sin exito");
                //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                pdfFile = resourceLoader.getResource("classpath:"+"/static"+"/error.pdf");
                return ResponseEntity
                        .ok()
                        .contentLength(pdfFile.contentLength())
                        .contentType(
                                MediaType.parseMediaType("application/pdf"))
                        .body(new InputStreamResource(pdfFile.getInputStream()));
            }
            else{

                logger.info("Se ha accedido al recurso DTE con folio " + folio
                        +" y fecha "+fecha+", de manera exitosa");
                pdfFile = resourceLoader.getResource(urlPDF);
                return ResponseEntity
                        .ok()
                        .contentLength(pdfFile.contentLength())
                        .contentType(
                                MediaType.parseMediaType("application/pdf"))
                        .body(new InputStreamResource(pdfFile.getInputStream()));
            }


            /*if (code == 404 || code == 503) {

                //TO DO Dejar en la base de datos esta URL
                //URLPDF = parametroService.findDefaultValorByParametro("Ruta Guia Electronica 2");
                //URLPDF = "http://10.249.87.202:8090/pdfs/91755000/GuiaDespachoElectronica/";
                urlPDF = URLPDF
                        + myList.get(0) + "/" + myList.get(1) + "/" + myList.get(2) + "/" + folio + ".pdf";
                u = new URL(urlPDF);
                huc = (HttpURLConnection) u.openConnection();
                huc.setRequestMethod("GET");
                huc.connect();
                code = huc.getResponseCode();

                //TO DO: If anidados hay que mejorarlos.

                if (code == 404 || code == 503) {
                    logger.info("Se ha tratado de acceder al recurso DTE con folio "
                            + folio + " y fecha "+fecha+" sin exito");
                    //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    pdfFile = resourceLoader.getResource("classpath:"+"/static"+"/error.pdf");
                    return ResponseEntity
                            .ok()
                            .contentLength(pdfFile.contentLength())
                            .contentType(
                                    MediaType.parseMediaType("application/pdf"))
                            .body(new InputStreamResource(pdfFile.getInputStream()));
                }
                else{

                    logger.info("Se ha accedido al recurso DTE con folio " + folio
                            +" y fecha "+fecha+", de manera exitosa");
                    pdfFile = resourceLoader.getResource(urlPDF);
                    return ResponseEntity
                            .ok()
                            .contentLength(pdfFile.contentLength())
                            .contentType(
                                    MediaType.parseMediaType("application/pdf"))
                            .body(new InputStreamResource(pdfFile.getInputStream()));
                }
            }


            else {
                logger.info("Se ha accedido al recurso DTE con folio " + folio
                        + " y fecha " + fecha + ", de manera exitosa");


                pdfFile = resourceLoader.getResource(urlPDF);
                return ResponseEntity
                        .ok()
                        .contentLength(pdfFile.contentLength())
                        .contentType(
                                MediaType.parseMediaType("application/pdf"))
                        .body(new InputStreamResource(pdfFile.getInputStream()));
            }*/

        } catch (IOException | NullPointerException e) {
            logger.error("El usuario Test ha buscado la guia con folio "+ folio + " y fecha "
                    +fecha+" sin exito, error: ",e);
            try {
                pdfFile = resourceLoader.getResource("classpath:"+"/static"+"/error.pdf");
                return ResponseEntity
                        .ok()
                        .contentLength(pdfFile.contentLength())
                        .contentType(
                                MediaType.parseMediaType("application/pdf"))
                        .body(new InputStreamResource(pdfFile.getInputStream()));
                //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }catch (IOException ex){
                logger.error("El usuario Test ha buscado la guia escaneada con folio "+ folio + " y fecha " +
                        fecha+" sin exito, error: ",ex);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Obtener la {@link Guia} electronica determinada por folio.
     * @param folio
     * @return URL de Guia Electrónica
     */
    @RequestMapping(value ="/guiasEscaneadas/{fecha}/{folio}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getGuiaEscaneadaPDF(@PathVariable String fecha,
                                                                   @PathVariable String folio) {

        String URLPDF = parametroService.findDefaultValorByParametro("Ruta Guia Escaneada");
        //String urlPDF = null;
        //Integer directorio_num = (Integer.parseInt(folio)/1000)*1000;
        //Resource archivoEscaneado = resourceLoader.getResource(URLPDF+"/"
        // +directorio_num+"/"+folio+".pdf");

        try {

            Boolean carpeta = Boolean.valueOf(parametroService.findDefaultValorByParametro("Carpeta Folio"));

            String prefijo = parametroService.findDefaultValorByParametro("Prefijo Email Folio");
            String user = parametroService.findDefaultValorByParametro("userFileSamba");
            String pass = parametroService.findDefaultValorByParametro("passFileSamba");
            String URLScan = parametroService.findDefaultValorByParametro("Ruta escaneados");
            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", user, pass);

            String path = null;
            if(carpeta) {
                String folio_folder = String.valueOf((Integer.parseInt(folio)/1000)*1000);
                path = URLScan + "/"+folio_folder + "/" + prefijo + folio + ".pdf";
            }
            else
                path = URLScan + prefijo + folio + ".pdf";

            SmbFile smbFile = new SmbFile(path, auth);

            /*String user = parametroService.findDefaultValorByParametro("userFileSamba");
            String pass = parametroService.findDefaultValorByParametro("passFileSamba");
            String URLScan = parametroService.findDefaultValorByParametro("Ruta escaneados");
            String path = URLScan+"000-"+folio+".pdf";

            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",user, pass);
            SmbFile smbFile = new SmbFile(path,auth);*/

            if(smbFile.getContentLength()!=0) {
                logger.info("Se ha accedido al recurso Guia Escaneada con folio " + folio
                        + " y fecha " + fecha + ", de manera exitosa");
                return ResponseEntity
                        .ok()
                        .contentLength(smbFile.getContentLength())
                        .contentType(
                                MediaType.parseMediaType("application/pdf"))
                        .body(new InputStreamResource(smbFile.getInputStream()));
            }
            else {

                Resource archivoEscaneado = resourceLoader.getResource("classpath:"+URLPDF+"/error.pdf");
                logger.error("Se ha buscado la guia escaneada con folio "+folio+" sin exito.");
                return ResponseEntity
                        .ok()
                        .contentLength(archivoEscaneado.contentLength())
                        .contentType(
                                MediaType.parseMediaType("application/pdf"))
                        .body(new InputStreamResource(archivoEscaneado.getInputStream()));
            }

        } catch (Exception e) {
            logger.error("El usuario Test ha buscado la guia escaneada con folio "+folio+" sin exito, error: ",e);
            try {
                Resource archivoEscaneado = resourceLoader.getResource("classpath:"+URLPDF+"/error.pdf");
                return ResponseEntity
                        .ok()
                        .contentLength(archivoEscaneado.contentLength())
                        .contentType(
                                MediaType.parseMediaType("application/pdf"))
                        .body(new InputStreamResource(archivoEscaneado.getInputStream()));

            }catch (IOException ex){
                logger.error("El usuario Test ha buscado la guia escaneada con folio "+folio+" sin exito, error: ",ex);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Obtener la {@link Guia} electronica determinada por folio.
     * @return URL de Guia Electrónica
     */
    @RequestMapping(value ="/logs", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getLogs() {

        Resource archivoLog = null;

        /*try {
            //Guia guia = guiaService.getGuiaByFolio(folio);

            //dividir folio por 1000
            archivoLog = resourceLoader.getResource("classpath:"+"/"+folio+".pdf");


            return ResponseEntity
                    .ok()
                    .contentLength(archivoEscaneado.contentLength())
                    .contentType(
                            MediaType.parseMediaType("application/pdf"))
                    .body(new InputStreamResource(archivoEscaneado.getInputStream()));
        } catch (Exception e) {
            logger.error("El usuario Test ha buscado la guia escaneada con folio "+folio+" sin exito, error: ",e);
            try {
                archivoEscaneado = resourceLoader.getResource("classpath:"+URLPDF+"/error.pdf");
                return ResponseEntity
                        .ok()
                        .contentLength(archivoEscaneado.contentLength())
                        .contentType(
                                MediaType.parseMediaType("application/pdf"))
                        .body(new InputStreamResource(archivoEscaneado.getInputStream()));
                //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }catch (IOException ex){
                logger.error("El usuario Test ha buscado la guia escaneada con folio "+folio+" sin exito, error: ",ex);
            }
        }*/
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Obtener todas las {@link Guia} por un rango de horas de la base de datos.
     * @param params
     * @return Lista de {@link Guia}
     */
    @RequestMapping(value ="/guiasByHoras", method = RequestMethod.POST)
    @ResponseBody
    public List<Guia> getAllGuiaByHoras(@RequestBody Map<String,String> params){

        List<Guia> guiasFiltradas = new ArrayList<Guia>();
        try {
            int dia = Integer.parseInt(params.get("dia"));
            int hora_comienzo = Integer.valueOf(params.get("hora_comienzo"));
            int minutos_comienzo = Integer.valueOf(params.get("minutos_comienzo"));
            int hora_final = Integer.valueOf(params.get("hora_final"));
            int minutos_final = Integer.valueOf(params.get("minutos_final"));

            LocalDateTime t_final = LocalDateTime.now().withHour(hora_final).withMinute(minutos_final).withSecond(0);
            LocalDateTime t_inicio = LocalDateTime.now().minusDays(dia).withHour(hora_comienzo).withMinute(minutos_comienzo).withSecond(0);

            System.out.println(t_inicio);
            System.out.println(t_final);

            for (int i = 0; i < params.size() - 5; i++) {
                List<Guia> guias = guiaService.getAllGuiasByRangeDate(params.get("centro" + i), t_inicio, t_final);
                guiasFiltradas.addAll(guias);
            }

            if (guiasFiltradas.size() != 0)
                logger.info("El usuario Test ha accedido a todos los recursos filtrados de Guia " + guiasFiltradas +
                        "con parametros " + params + " con exito");
            else {
                logger.info("El usuario Test ha tratado de acceder a todos los recursos " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }
        }catch (Exception e){
            logger.error("Error en acceder al recurso Guia Filtrada: ",e);
        }
        //return null;
        return guiasFiltradas;
    }

    /**
     * Obtener todas las {@link Guia} por un rango de fecha de la base de datos.
     * @param params
     * @return Lista de {@link Guia}
     */
    @RequestMapping(value ="/guiasByFechas", method = RequestMethod.POST)
    @ResponseBody
    public List<Guia> getAllGuiaByFechas(@RequestBody Map<String,String> params){

        List<Guia> guiasFiltradas = new ArrayList<Guia>();
        try {
            List <String> fecha_i = Arrays.asList(params.get("fecha_inicial").split("-"));
            List <String> tiempo_i = Arrays.asList(params.get("t_inicio").split(":"));
            List <String> tiempo_f = Arrays.asList(params.get("t_final").split(":"));

            //System.out.println(tiempo_f);
            int año_i = Integer.parseInt(fecha_i.get(0));
            int mes_i = Integer.parseInt(fecha_i.get(1));
            int dia_i = Integer.parseInt(fecha_i.get(2));
            int hora_i = Integer.parseInt(tiempo_i.get(0));
            int minuto_i = Integer.parseInt(tiempo_i.get(1));


            int hora_f = Integer.parseInt(tiempo_f.get(0));
            int minuto_f = Integer.parseInt(tiempo_f.get(1));

            List <String> fecha_f = Arrays.asList(params.get("fecha_final").split("-"));
            int año_f = Integer.parseInt(fecha_f.get(0));
            int mes_f = Integer.parseInt(fecha_f.get(1));
            int dia_f = Integer.parseInt(fecha_f.get(2));
            /*if(params.get("fecha_final")!=null){
                fecha_f = Arrays.asList(params.get("fecha_final").split("-"));
                año_f = Integer.parseInt(fecha_f.get(0));
                mes_f = Integer.parseInt(fecha_f.get(1));
                dia_f = Integer.parseInt(fecha_f.get(2));
            }*/

            /*System.out.println("Fecha Inicial: "+año_i+"-"+mes_i+"-"+dia_i);
            System.out.println("Fecha Final: "+año_f+"-"+mes_f+"-"+dia_f);*/

            LocalDateTime t_inicio = LocalDateTime.now().withYear(año_i).withMonth(mes_i).withDayOfMonth(dia_i).
                    withHour(hora_i).withMinute(minuto_i).withSecond(0).withNano(0);

            LocalDateTime t_final  = LocalDateTime.now().withYear(año_f).withMonth(mes_f).withDayOfMonth(dia_f).
                    withHour(hora_f).withMinute(minuto_f).withSecond(59);;
            /*if(params.get("fecha_final")!=null) {
                //System.out.println("No es nulo");
                t_final = LocalDateTime.now().withYear(año_f).withMonth(mes_f).withDayOfMonth(dia_f).
                        withHour(hora_f).withMinute(minuto_f).withSecond(59);
            }*/
            /*else {
                t_final = LocalDateTime.now().withYear(año_i).withMonth(mes_i).withDayOfMonth(dia_i).
                        withHour(hora_f).withMinute(minuto_f).withSecond(59);
            }*/

            System.out.println("Fecha Inicio: " +t_inicio);
            System.out.println("Fecha Final: "+ t_final);

            for (int i = 0; i < params.size() - 4; i++) {
                List<Guia> guias = guiaService.getAllGuiasByRangeDate(params.get("centro" + i), t_inicio, t_final);
                guiasFiltradas.addAll(guias);
            }

            if (guiasFiltradas.size() != 0)
                logger.info("El usuario Test ha accedido a todos los recursos filtrados de Guia " + guiasFiltradas +
                        "con parametros " + params + " con exito");
            else {
                logger.info("El usuario Test ha tratado de acceder a todos los recursos " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }
        }catch (Exception e){
            logger.error("Error en acceder al recurso Guia Filtrada: ",e);
        }
        //return null;
        return guiasFiltradas;
    }

    /**
     * Obtener todas las {@link Guia} emitidas por un rango de fecha de la base de datos.
     * @param params
     * @return Lista de {@link Guia}
     */
    @RequestMapping(value ="/emitidas/guiasByFechas", method = RequestMethod.POST)
    @ResponseBody
    public List<Guia> getAllGuiaEmitidasByFechas(@RequestBody Map<String,String> params){

        List<Guia> guiasFiltradas = new ArrayList<Guia>();
        try {
            List <String> fecha_i = Arrays.asList(params.get("fecha_inicial").split("-"));
            List <String> tiempo_i = Arrays.asList(params.get("t_inicio").split(":"));
            List <String> tiempo_f = Arrays.asList(params.get("t_final").split(":"));

            int año_i = Integer.parseInt(fecha_i.get(0));
            int mes_i = Integer.parseInt(fecha_i.get(1));
            int dia_i = Integer.parseInt(fecha_i.get(2));
            int hora_i = Integer.parseInt(tiempo_i.get(0));
            int minuto_i = Integer.parseInt(tiempo_i.get(1));

            int hora_f = Integer.parseInt(tiempo_f.get(0));
            int minuto_f = Integer.parseInt(tiempo_f.get(1));

            List <String> fecha_f = Arrays.asList(params.get("fecha_final").split("-"));
            int año_f = Integer.parseInt(fecha_f.get(0));
            int mes_f = Integer.parseInt(fecha_f.get(1));
            int dia_f = Integer.parseInt(fecha_f.get(2));

            LocalDateTime t_inicio = LocalDateTime.now().withYear(año_i).withMonth(mes_i).withDayOfMonth(dia_i).
                    withHour(hora_i).withMinute(minuto_i).withSecond(0).withNano(0);

            LocalDateTime t_final  = LocalDateTime.now().withYear(año_f).withMonth(mes_f).withDayOfMonth(dia_f).
                    withHour(hora_f).withMinute(minuto_f).withSecond(59);;


            /*System.out.println("Fecha Inicio: " +t_inicio);
            System.out.println("Fecha Final: "+ t_final);*/

            for (int i = 0; i < params.size() - 4; i++) {
                List<Guia> guias = guiaService.getAllGuiaEmitidasByFechas(params.get("centro" + i), t_inicio, t_final);
                guiasFiltradas.addAll(guias);
            }

            if (guiasFiltradas.size() != 0)
                logger.info("El usuario Test ha accedido a todos los recursos filtrados de Guia " + guiasFiltradas +
                        "con parametros " + params + " con exito");
            else {
                logger.info("El usuario Test ha tratado de acceder a todos los recursos " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }
        }catch (Exception e){
            logger.error("Error en acceder al recurso Guia Filtrada: ",e);
        }
        //return null;
        return guiasFiltradas;
    }

    /**
     * Obtener todas las {@link Guia} con un estado especifico.
     * @param params
     * @return Lista de {@link Guia}
     */
    @RequestMapping(value ="/guiasByEstado", method = RequestMethod.POST)
    @ResponseBody
    public List<Guia> getAllGuiaByGuiaEstado(@RequestBody Map<String,String> params){
        System.out.println(params);
        List<Guia> guiasFiltradas = new ArrayList<Guia>();
        try {
            for (int i = 0; i < params.size() - 1; i++) {
                List<Guia> guias = guiaService.getAllGuiaByGuiaEstado(params.get("centro" + i), params.get("estado"));
                guiasFiltradas.addAll(guias);
            }

            if (guiasFiltradas.size() != 0)
                logger.info("El usuario Test ha accedido a todos los recursos filtrados de Guia " + guiasFiltradas +
                        "con parametros " + params + " con exito");
            else {
                logger.error("El usuario Test ha tratado de acceder a todos los recursos " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }
        }catch (Exception e){
            logger.error("Error en acceder al recurso Guia Filtrada por Estado: ",e);
        }
        System.out.println(guiasFiltradas.size());
        return guiasFiltradas;
    }

    /**
     * Obtener todas las {@link Guia} con un estado especifico.
     * @param params
     * @return Lista de {@link Guia}
     */
    @RequestMapping(value ="/guiasByEstadoHoras", method = RequestMethod.POST)
    @ResponseBody
    public List<Guia> getAllGuiaByEstadoAndHoras(@RequestBody Map<String,String> params){
        System.out.println(params);
        List<Guia> guiasFiltradas = new ArrayList<Guia>();
        try {
            int dia = Integer.parseInt(params.get("dia"));
            int hora_comienzo = Integer.valueOf(params.get("hora_comienzo"));
            int minutos_comienzo = Integer.valueOf(params.get("minutos_comienzo"));
            int hora_final = Integer.valueOf(params.get("hora_final"));
            int minutos_final = Integer.valueOf(params.get("minutos_final"));

            LocalDateTime t_final = LocalDateTime.now().withHour(hora_final).withMinute(minutos_final).withSecond(0);
            LocalDateTime t_inicio = LocalDateTime.now().minusDays(dia).withHour(hora_comienzo).withMinute(minutos_comienzo).withSecond(0);

            for (int i = 0; i < params.size() - 6; i++) {
                List<Guia> guias = guiaService.getAllGuiaByGuiaEstadoAndHoras(params.get("centro" + i),
                        params.get("estado"),t_inicio, t_final);
                guiasFiltradas.addAll(guias);
            }

            if (guiasFiltradas.size() != 0)
                logger.info("Se ha accedido a todos los recursos filtrados de Guia " + guiasFiltradas +
                        "con parametros " + params + " con exito");
            else {
                logger.info("Se ha tratado de acceder a todos los recursos " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }

            /*for (int i = 0; i < params.size() - 1; i++) {
                List<Guia> guias = guiaService.getAllGuiaByGuiaEstado(params.get("centro" + i), params.get("estado"));
                guiasFiltradas.addAll(guias);
            }

            if (guiasFiltradas.size() != 0)
                logger.info("El usuario Test ha accedido a todos los recursos filtrados de Guia " + guiasFiltradas +
                        "con parametros " + params + " con exito");
            else {
                logger.error("El usuario Test ha tratado de acceder a todos los recursos " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }*/
        }catch (Exception e){
            logger.error("Error en acceder a los recursos Guia Filtrada por Estado y Horas: ",e);
        }
        //System.out.println(guiasFiltradas.size());
        return guiasFiltradas;
    }

    /**
     * Obtener todas las {@link Guia} con un estado especifico.
     * @param params
     * @return Lista de {@link Guia}
     */
    @RequestMapping(value ="/guiasByEstadoAndHoras", method = RequestMethod.POST)
    @ResponseBody
    public List<Guia> getAllGuiaByGuiaEstadoAndHoras(@RequestBody Map<String,String> params){

        List<Guia> guiasFiltradas = new ArrayList<Guia>();

        try {

            LocalDateTime t_inicio = LocalDateTime.now().withYear(Integer.parseInt(params.get("fecha_comienzo_año")))
                    .withMonth(Integer.parseInt(params.get("fecha_comienzo_mes")))
                    .withDayOfMonth(Integer.parseInt(params.get("fecha_comienzo_dia")))
                    .withHour(0).withMinute(0).withSecond(0);
            LocalDateTime t_final = LocalDateTime.now().withYear(Integer.parseInt(params.get("fecha_final_año")))
                    .withMonth(Integer.parseInt(params.get("fecha_final_mes")))
                    .withDayOfMonth(Integer.parseInt(params.get("fecha_final_dia")))
                    .withHour(0).withMinute(0).withSecond(0);


            if (!t_inicio.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).
                    equals(t_final.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
                for (int i = 0; i < params.size() - 7; i++) {
                    List<Guia> guias = guiaService.getAllGuiasByEstadoAndRangeDate(
                            params.get("estado"), params.get("centro" + i), t_inicio, t_final);
                    guiasFiltradas.addAll(guias);
                }
            } else {
                for (int i = 0; i < params.size() - 7; i++) {
                    List<Guia> guias = guiaService.getAllGuiasByEstadoAndDate(
                            params.get("estado"), params.get("centro" + i), t_inicio);
                    guiasFiltradas.addAll(guias);
                }
            }


            if(guiasFiltradas.size()!=0)
                logger.info("El usuario Test ha accedido a todos los recursos filtrados de Guia " +guiasFiltradas+
                        "con parametros "+params+" con exito");
            else {
                logger.info("El usuario Test ha tratado de acceder a todos los recursos " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }

        }catch (Exception e){
            logger.error("Error en acceder al recurso Guia Filtrada por Estado y Rango de Fechas: ",e);
        }

        return guiasFiltradas;
    }

    /**
     * Mandar un correo que informe que la {@link Guia} escaneada fue guardada en la BD.
     * @param folio
     * @return
     */
    @RequestMapping(value ="/guiasSendEmail/{folio}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getGuiaFolioAndSendEmail(@PathVariable String folio){
    //public void getGuiaFolioAndSendEmail(@RequestBody Map<String,String> params){

        //System.out.println("Entra aqui");

        String campo1 = null;
        Guia guia = guiaService.getGuiaByFolio(folio);
        //System.out.println("Muestra esta guia " +guia);
        if(guia == null)
            return ResponseEntity.ok("Guia no encontrada");
        //Integer cust_code = Integer.valueOf(guia.getCust_code().trim());
        //int length = (int)(Math.log10(cust_code)+1);
        //System.out.println(length);
        //String codigo = new String(new char[10-length]).replace("\0", "0") + guia.getCust_code().trim();
        String codigo = guia.getCust_job_num().trim();
        //System.out.println(codigo);
        HttpResponse<String> response = null;
        try {
            String url_delivery_obra = parametroService.findDefaultValorByParametro("Ruta Sap Obra");
            //response = Unirest.post("http://172.33.0.163:8181/cxf/ZESALES_DELIVERY_OBRA_V3")
            response = Unirest.post(url_delivery_obra)
                    .body("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:zes=\"http://www.grupocbb.cl/sap/rfc/zesales_delivery_obra_v3\">\n" +
                            "   <soapenv:Header/>\n" +
                            "   <soapenv:Body>\n" +
                            "      <zes:ZESALES_DELIVERY_OBRA_V3>\n" +
                            "         <PKUNNR>"+codigo+"</PKUNNR>\n" +
                            "         <PVBELN>0</PVBELN>\n" +
                            "      </zes:ZESALES_DELIVERY_OBRA_V3>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>")
                    .asString();
            JSONObject jsonObject = XML.toJSONObject(response.getBody());
            campo1 = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").
                    getJSONObject("rsp:ZESALES_DELIVERY_OBRA_V3_RSP").get("CAMPO1").toString();
            //System.out.println(jsonObject);
            System.out.println(campo1);
        } catch (UnirestException e) {
            logger.error("Error al obtener cuenta de correo desde SAP: ",e);
        }


        //String emailSoap = response.getRETORNO1();
        String validEmail = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        //String emailSoap = params.get("email");
        //System.out.println(emailSoap);

        //Esto debe comentarse para mandar correo
        //campo1 = "hola.cl";
        String[] emails = campo1.split(";");
        List<String> correos = new ArrayList<String>();
        for (String email: emails) {
            if (email.trim() != null && !email.trim().equals("") && email.trim().matches(validEmail))
                correos.add(email.trim());
        }
        System.out.println(correos.size());

        try {
            JDBCCmd cmdDb = new JDBCCmd(parametroService.findDefaultValorByParametro("proWebSqlServer"),
                    parametroService.findDefaultValorByParametro("proWebSqlDb"),
                    parametroService.findDefaultValorByParametro("proWebSqlUser"),
                    parametroService.findDefaultValorByParametro("proWebSqlPass"));
            //System.out.println(Integer.parseInt(obra));
            campo1 = cmdDb.getEmailProgWeb(guia.getCust_job_num().trim());
            System.out.println(campo1);
        }catch (Exception e){
            logger.error("Error en mandar correo con guia adjunta: ",e);
        }

        if(campo1!=null) {
            emails = campo1.split(";");

            for (String email : emails) {
                if (email.trim() != null && !email.trim().equals("") && email.trim().matches(validEmail))
                    correos.add(email.trim());
            }
        }
        System.out.println(correos);

        Boolean smtp = Boolean.valueOf(parametroService.findDefaultValorByParametro("SMTP Local"));

        if(correos.size()!=0) {
            try {

                Boolean carpeta = Boolean.valueOf(parametroService.findDefaultValorByParametro("Carpeta Folio"));

                String prefijo = parametroService.findDefaultValorByParametro("Prefijo Email Folio");
                String user = parametroService.findDefaultValorByParametro("userFileSamba");
                String pass = parametroService.findDefaultValorByParametro("passFileSamba");
                String URLScan = parametroService.findDefaultValorByParametro("Ruta escaneados");
                NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", user, pass);

                String path = null;
                if(carpeta) {
                    String folio_folder = String.valueOf((Integer.parseInt(folio)/1000)*1000);
                    path = URLScan + "/"+folio_folder + "/" + prefijo + folio + ".pdf";
                }
                else
                    path = URLScan + prefijo + folio + ".pdf";

                SmbFile smbFile = new SmbFile(path, auth);

                if (smbFile.getContentLength() != 0) {

                    System.out.println("Sending Email...");

                    if(smtp)
                        guiaService.sendEmailWithAttachment(correos, folio, smbFile.getInputStream());
                    else
                        guiaService.sendEmailWithAttachmentQA(correos, folio, smbFile.getInputStream());

                    System.out.println("Done");
                    logger.info("Se ha mandado un correo con el recurso Guia Escaneada con folio " + folio
                            + " y numero de obra " + guia.getCust_job_num() + " de manera exitosa");
                } else {

                    System.out.println("No existe archivo");
                    System.out.println("Sending Email...");

                    if(smtp)
                        guiaService.sendEmailDefault(correos,folio,true);
                    else
                        guiaService.sendEmailDefaultQA(correos,folio,true);

                    System.out.println("Done");
                    logger.error("Error al mandar correo, la guia escaneada con folio " + folio + " y numero de obra " + guia.getCust_job_num());
                }
            } catch (Exception e) {
                logger.error("Error en mandar correo con guia adjunta: ", e);
            }
        }

        else {

            System.out.println("No existe correo");
            System.out.println("Sending Email...");

            if(smtp)
                guiaService.sendEmailDefault(correos,folio,false);
            else
                guiaService.sendEmailDefaultQA(correos,folio,false);

            System.out.println("Done");

        }
        return ResponseEntity.ok("Guia enviada");

    }

    /**
     * Mandar un correo que informe que la {@link Guia} escaneada fue guardada en la BD.
     * @param folio
     * @return
     */
    @RequestMapping(value ="/guiasGetEmail/{folio}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> GetEmail(@PathVariable String folio){

        String campo1 = null;
        Guia guia = guiaService.getGuiaByFolio(folio);

        /*Integer cust_code = Integer.valueOf(guia.getCust_job_num().trim());
        int length = (int)(Math.log10(cust_code)+1);

        String codigo = new String(new char[10-length]).replace("\0", "0") + guia.getCust_code().trim();
        System.out.println(codigo);*/

        String codigo = guia.getCust_job_num().trim();

        HttpResponse<String> response = null;
        try {
            response = Unirest.post("http://172.33.0.163:8181/cxf/ZESALES_DELIVERY_OBRA_V3")
                    .body("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:zes=\"http://www.grupocbb.cl/sap/rfc/zesales_delivery_obra_v3\">\n" +
                            "   <soapenv:Header/>\n" +
                            "   <soapenv:Body>\n" +
                            "      <zes:ZESALES_DELIVERY_OBRA_V3>\n" +
                            "         <PKUNNR>"+codigo+"</PKUNNR>\n" +
                            "         <PVBELN>0</PVBELN>\n" +
                            "      </zes:ZESALES_DELIVERY_OBRA_V3>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>")
                    .asString();
            JSONObject jsonObject = XML.toJSONObject(response.getBody());
            campo1 = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").
                    getJSONObject("rsp:ZESALES_DELIVERY_OBRA_V3_RSP").get("CAMPO1").toString();
            //System.out.println(jsonObject);
            System.out.println(campo1);
        } catch (UnirestException e) {
            logger.error("Error en mandar correo con guia adjunta: ",e);
        }

        String validEmail = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        if(campo1==null || campo1.equals("") || !campo1.matches(validEmail)) {
            System.out.println("Es nulo");
            try {
                JDBCCmd cmdDb = new JDBCCmd(parametroService.findDefaultValorByParametro("proWebSqlServer"),
                        parametroService.findDefaultValorByParametro("proWebSqlDb"),
                        parametroService.findDefaultValorByParametro("proWebSqlUser"),
                        parametroService.findDefaultValorByParametro("proWebSqlPass"));
                //System.out.println(Integer.parseInt(obra));
                campo1 = cmdDb.getEmailProgWeb(guia.getCust_job_num().trim());
                System.out.println(campo1);
            }catch (Exception e){
                logger.error("Error en mandar correo con guia adjunta: ",e);
            }
        }

        if(campo1 == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(campo1);
    }

    /**
     * Guardar un {@link Guia} en la base de datos.
     * @return {@link Guia}
     */
    @RequestMapping(value ="/guias", method = RequestMethod.POST)
    @ResponseBody
    public Guia saveGuia(){

        Guia guia = new Guia();
        guia.setTick_order_date("2019-10-15T03:00:00.000Z");
        guia.setTick_order_code("103");
        guia.setTick_tkt_code("6756664");
        guia.setTick_tkt_date("2019-10-15T03:00:00.000Z");
        guia.setTick_ship_plant_loc_code("13LR");
        guia.setFechaCreacion(LocalDateTime.now());
        guia.setFechaActualizacion(LocalDateTime.now());
        //guia.setTickEstado(tickEstadoService.getTickEstadoById((long) 1));
        guia.setGuiaEstado(guiaEstadoService.getGuiaEstadoById((long) 1));
        guiaService.save(guia);

        //guia.getGuiaEstado().setGuiaEstadoOpcion(guiaEstadoOpcionRepository.findByGuiaEstado(guia.getGuiaEstado()));

        /*Emisor prueba = Emisor.getInstance();
        List<SseEmitter> sseEmitters = prueba.getSseEmitters();
        synchronized (sseEmitters) {
            for (SseEmitter sseEmitter : sseEmitters) {
                try {
                    sseEmitter.send(guia.getId(), MediaType.APPLICATION_JSON);
                    sseEmitter.complete();
                } catch (Exception e) {
                    //???
                }
            }
        }*/

        return guia;
    }

    /**
     * Actualizar el numero de sello de una {@link Guia} en la base de datos.
     * @param params
     * @return {@link Guia}
     */
    @RequestMapping(value ="/guias/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Guia actualizarGuia(@RequestBody Map<String,String> params,@PathVariable Long id) {

        Guia nueva = guiaService.getGuiaById(id);
        String usuario = params.get("usuario");
        String nro_sello = params.get("nro_sello");
        if(nueva != null) {
            //nueva.setNumero_sello(nro_sello);
            //guiaService.save(nueva);

            //nueva.getGuiaEstado().setGuiaEstadoOpcion(guiaEstadoOpcionRepository.findByGuiaEstado(nueva.getGuiaEstado()));        }
        }
        else {
            logger.info("El usuario "+usuario+" ha tratado de actualizar un recurso Guia con id "+id+" y parametros" +
                    params+ " sin exito");
            return null;
        }

        try {
            System.out.println("Entro en guardar guia");
            String URLProxy = parametroService.findDefaultValorByParametro("Ruta Proxy DTE");
            HttpResponse<String> response = Unirest.get(URLProxy+"emitirGuia/"+id+"/"+nro_sello+"/false").asString();
            System.out.println(response.getBody());
        } catch (UnirestException e) {
            logger.info("El usuario "+usuario+" ha tratado de actualizar un recurso Guia con id "+id+" y parametros" +
                    params+ " sin exito, con error: ",e);
        }

        logger.info("El usuario "+usuario+" ha actualizado un recurso Guia con id "+id+" y con parametros " +
                params+" con exito");

        return nueva;
    }

    /**
     * Actualizar el numero de sello de una {@link Guia} en la base de datos.
     * @param params
     * @return {@link Guia}
     */
    @RequestMapping(value ="/guias/{id}/emitirGuia", method = RequestMethod.PUT)
    @ResponseBody
    public Guia emitirGuia(@RequestBody Map<String,String> params,@PathVariable Long id) {

        Guia nueva = guiaService.getGuiaById(id);
        String usuario = params.get("usuario");
        String nro_sello = params.get("nro_sello");
        if(nueva != null) {
            /*nueva.setNumero_sello(nro_sello);
            guiaService.save(nueva);*/

            //nueva.getGuiaEstado().setGuiaEstadoOpcion(guiaEstadoOpcionRepository.findByGuiaEstado(nueva.getGuiaEstado()));

        }
        else {
            logger.info("El usuario "+usuario+" ha tratado de actualizar un recurso Guia con id "+id+" y parametros" +
                    params+ " sin exito");
            return null;
        }

        try {
            System.out.println("Entro en emitir guia");
            String URLProxy = parametroService.findDefaultValorByParametro("Ruta Proxy DTE");
            HttpResponse<String> response = Unirest.get(URLProxy+"emitirGuia/"+id+"/"+nro_sello+"/true").asString();
            System.out.println(response.getBody());
        } catch (UnirestException e) {
            logger.info("El usuario "+usuario+" ha tratado de actualizar un recurso Guia con id "+id+" y parametros" +
                    params+ " sin exito, con error: ",e);
        }

        logger.info("El usuario "+usuario+" ha actualizado un recurso Guia con id "+id+" y con parametros " +
                params+" con exito");

        return nueva;
    }

    /**
     * Obtener todas las {@link Guia} filtradas por centro.
     * @param params
     * @return Lista de {@link Guia}
     */
    @RequestMapping(value ="/guiasCentros", method = RequestMethod.POST)
    @ResponseBody
    public List<Guia> findGuiasFiltradas(@RequestBody Map<String,String> params){

        List<Guia> guiasFiltradas = new ArrayList<>();

        for(int i=0;i<params.size();i++){
            List<Guia> guias = guiaService.getAllGuiasFiltradas(params.get("centro"+i));
            guiasFiltradas.addAll(guias);
        }

        //System.out.println("Entro por aca en guias centros");
        if(guiasFiltradas.size()!=0)
            logger.info("El usuario Test ha accedido a todos los recursos filtrados de Guia " +guiasFiltradas+
                    " con exito");
        else
            logger.info("El usuario Test ha tratado de acceder a todos los recursos " +
                    "filtrados de Guia con centros "+params+" sin exito");

        //System.out.println("Paso por el return");
        return guiasFiltradas;
    }

    /**
     * Obtener todas las {@link GuiaHistorial} de una guia mediante su clave
     * @param id
     * @return Lista {@link GuiaHistorial}
     */
    @RequestMapping(value ="/guiasHistorials/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<GuiaHistorial> findGuiaHistorials(@PathVariable Long id){

        Guia guia = guiaService.getGuiaById(id);
        if(guia != null)
            logger.info("El usuario Test ha accedido al recurso de Guia de id "+id+", con el historial "
                    +guia.getGuiaHistorials()+ " con exito");
        else {
            logger.info("El usuario Test tratado de acceder al recurso de Guia de id "+id+" sin exito");
            return null;
        }
        return guia.getGuiaHistorials();
    }

    /**
     * Obtener el total de {@link Guia} emitidas, escaneadas, repasada y escaneada-repasada
     * @return Lista de Lista {@link Integer}
     */
    @RequestMapping(value ="/guias/totalESR", method = RequestMethod.POST)
    @ResponseBody
    public List<List<Integer>> getTotalEmitidas(@RequestBody Map<String,String> params){

        List<List<Integer>> total = new ArrayList<>();
        List<Integer> totalESR  = new ArrayList<>();

        try {
            int dia = Integer.parseInt(params.get("dia"));
            int hora_comienzo = Integer.valueOf(params.get("hora_comienzo"));
            int minutos_comienzo = Integer.valueOf(params.get("minutos_comienzo"));
            int hora_final = Integer.valueOf(params.get("hora_final"));
            int minutos_final = Integer.valueOf(params.get("minutos_final"));

            LocalDateTime t_final = LocalDateTime.now().withHour(hora_final).withMinute(minutos_final).withSecond(0);
            LocalDateTime t_inicio = LocalDateTime.now().minusDays(dia).withHour(hora_comienzo).withMinute(minutos_comienzo).withSecond(0);

            for (int i = 0; i < params.size() - 5; i++) {
                Integer totalEmitida = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "EMITIDA_SII",t_inicio, t_final);
                totalESR.add(totalEmitida);
                Integer totalEscaneada = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "ESCANEADA",t_inicio, t_final);
                totalESR.add(totalEscaneada);
                Integer totalRepasada = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "REPASADA",t_inicio, t_final);
                totalESR.add(totalRepasada);
                Integer totalEscaneadaRepasada = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "ESCANEADA_REPASADA",t_inicio, t_final);
                totalESR.add(totalEscaneadaRepasada);
                total.add(new ArrayList<Integer>(totalESR));
                totalESR.clear();
            }

            /*for (int i = 0; i < params.size() - 5; i++) {
                Integer totalEscaneada = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "ESCANEADA",t_inicio, t_final);
                totalRep.add(totalEscaneada);
            }

            for (int i = 0; i < params.size() - 5; i++) {
                Integer totalRepasada = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "REPASADA",t_inicio, t_final);
                totalEsc.add(totalRepasada);
            }

            for (int i = 0; i < params.size() - 5; i++) {
                Integer totalEscaneadaRepasada = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "ESCANEADA_REPASADA",t_inicio, t_final);
                totalRE.add(totalEscaneadaRepasada);
            }

            total.add(totalEmi);
            total.add(totalEsc);
            total.add(totalRep);
            total.add(totalRE);*/

            if (total.size() != 0)
                logger.info("Se ha accedido a obtener el total de guias emitidas " +
                        "con los totales "+ total + "y con parametros " + params + " con exito");
            else {
                logger.info("Se ha tratado de obtener el total de guias emitidas " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }

        }catch (Exception e) {
            logger.error("Error en acceder al total de guias emitidas ", e);
        }
        return total;
    }

    /**
     * Obtener el total de {@link Guia} emitidas, escaneadas, repasada y escaneada-repasada
     * por un rango de fechas.
     * @param params
     * @return Lista de {@link Guia}
     */
    @RequestMapping(value ="/guiasByFechas/totalESR", method = RequestMethod.POST)
    @ResponseBody
    public List<List<Integer>> getTotalEmitidasByFechas(@RequestBody Map<String,String> params){

        List<List<Integer>> total = new ArrayList<>();
        List<Integer> totalESR  = new ArrayList<>();

        try {
            List <String> fecha_i = Arrays.asList(params.get("fecha_inicial").split("-"));
            List <String> tiempo_i = Arrays.asList(params.get("t_inicio").split(":"));
            List <String> tiempo_f = Arrays.asList(params.get("t_final").split(":"));

            int año_i = Integer.parseInt(fecha_i.get(0));
            int mes_i = Integer.parseInt(fecha_i.get(1));
            int dia_i = Integer.parseInt(fecha_i.get(2));
            int hora_i = Integer.parseInt(tiempo_i.get(0));
            int minuto_i = Integer.parseInt(tiempo_i.get(1));

            int hora_f = Integer.parseInt(tiempo_f.get(0));
            int minuto_f = Integer.parseInt(tiempo_f.get(1));

            List <String> fecha_f = Arrays.asList(params.get("fecha_final").split("-"));
            int año_f = Integer.parseInt(fecha_f.get(0));
            int mes_f = Integer.parseInt(fecha_f.get(1));
            int dia_f = Integer.parseInt(fecha_f.get(2));

            LocalDateTime t_inicio = LocalDateTime.now().withYear(año_i).withMonth(mes_i).withDayOfMonth(dia_i).
                    withHour(hora_i).withMinute(minuto_i).withSecond(0).withNano(0);

            LocalDateTime t_final  = LocalDateTime.now().withYear(año_f).withMonth(mes_f).withDayOfMonth(dia_f).
                    withHour(hora_f).withMinute(minuto_f).withSecond(59);


            /*System.out.println("Fecha Inicio: " +t_inicio);
            System.out.println("Fecha Final: "+ t_final);*/

            for (int i = 0; i < params.size() - 4; i++) {
                Integer totalEmitida = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "EMITIDA_SII",t_inicio, t_final);
                totalESR.add(totalEmitida);
                Integer totalEscaneada = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "ESCANEADA",t_inicio, t_final);
                totalESR.add(totalEscaneada);
                Integer totalRepasada = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "REPASADA",t_inicio, t_final);
                totalESR.add(totalRepasada);
                Integer totalEscaneadaRepasada = guiaService.findAllGuiaByEstadoAndHoras(params.get("centro" + i),
                        "ESCANEADA_REPASADA",t_inicio, t_final);
                totalESR.add(totalEscaneadaRepasada);
                total.add(new ArrayList<Integer>(totalESR));
                totalESR.clear();
            }

            if (total.size() != 0)
                logger.info("Se ha accedido a obtener el total de guias emitidas por rango de fecha " +
                        "con los totales "+ total + "y con parametros " + params + " con exito");
            else {
                logger.info("Se ha tratado de obtener el total de guias emitidas por rango de fecha " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }

        }catch (Exception e) {
            logger.error("Error en acceder al total de guias emitidas por rango de fecha ", e);
        }
        return total;
    }

    /**
     * Obtener el total de {@link GuiaRepaso} de folio_ok, firmada, obs,
     *   Sol. NC., Devuelto, Adiciones, Sobre estadía,
     *   Disposición, Muestra propia,  Muestra cliente, Camión devuelto
     * @return Lista {@link Integer}
     */
    @RequestMapping(value ="/guias/totalGR", method = RequestMethod.POST)
    @ResponseBody
    public List<List<Integer>> getTotalGuiaRepaso(@RequestBody Map<String,String> params){

        List<List<Integer>> totalGR = new ArrayList<>();
        List<Integer> total = new ArrayList<>();
        List<GuiaRepaso> guiaRepasos;
        int totalFolio = 0, totalFirma = 0, totalObs = 0, totalSol = 0, totalAdic = 0;
        int totalSobreEstadia = 0, totalDisp = 0, totalMuesPropia = 0, totalMuesCliente = 0, totalCamionDev=0;

        try {
            int dia = Integer.parseInt(params.get("dia"));
            int hora_comienzo = Integer.valueOf(params.get("hora_comienzo"));
            int minutos_comienzo = Integer.valueOf(params.get("minutos_comienzo"));
            int hora_final = Integer.valueOf(params.get("hora_final"));
            int minutos_final = Integer.valueOf(params.get("minutos_final"));

            LocalDateTime t_final = LocalDateTime.now().withHour(hora_final).withMinute(minutos_final).withSecond(0);
            LocalDateTime t_inicio = LocalDateTime.now().minusDays(dia).withHour(hora_comienzo).withMinute(minutos_comienzo).withSecond(0);



            for (int i = 0; i < params.size() - 5; i++) {
                guiaRepasos = guiaService.findAllGuiaRepasoByCentroAndRangoFecha(params.get("centro" + i),
                        t_inicio, t_final);
                for(GuiaRepaso gr:guiaRepasos){
                    if(gr.getFolios_ok()==1)
                        totalFolio++;
                    if(gr.getRc_firmado()==1)
                        totalFirma++;
                    if(gr.getObs()!=null && !gr.getObs().equals(""))
                        totalObs++;
                    if(gr.getSolicitud_nc()==1)
                        totalSol++;
                    if(gr.getAc_adit_cant()!=null || (gr.getAc_solicitante_adit()!=null
                    && gr.getAc_solicitante_adit().equals("")) || gr.getAc_firmado_adit()==1)
                        totalAdic++;
                    if(gr.getAc_adit_cant()!=null || (gr.getAc_solicitante_tiempo_camion()!=null
                            && gr.getAc_solicitante_tiempo_camion().equals("")) || gr.getAc_firmado_tiempo_camion()==1)
                        totalSobreEstadia++;
                    if(gr.getAc_hormigon()!=null || (gr.getAc_solicitante_hormigon()!=null
                            && gr.getAc_solicitante_hormigon().equals("")) || gr.getAc_firmado_hormigon()==1)
                        totalDisp++;
                    if(gr.getMue_propia()==1)
                        totalMuesPropia++;
                    if(gr.getMue_cliente()==1)
                        totalMuesCliente++;
                    if(gr.getCamion_devuelto()==1)
                        totalCamionDev++;
                }
                total.add(totalFolio);
                total.add(totalFirma);
                total.add(totalObs);
                total.add(totalSol);
                total.add(totalAdic);
                total.add(totalSobreEstadia);
                total.add(totalDisp);
                total.add(totalMuesPropia);
                total.add(totalMuesCliente);
                total.add(totalCamionDev);

                totalGR.add(new ArrayList<Integer>(total));
                total.clear();
                totalFolio = 0;
                totalFirma = 0;
                totalObs = 0;
                totalSol = 0;
                totalAdic = 0;
                totalSobreEstadia = 0;
                totalDisp = 0;
                totalMuesPropia = 0;
                totalMuesCliente = 0;
                totalCamionDev=0;
            }

            if (totalGR.size() != 0)
                logger.info("Se ha accedido a obtener el total de guias repasadas " +
                        "con los totales "+ totalGR + "y con parametros " + params + " con exito");
            else {
                logger.info("Se ha tratado de obtener el total de guias repasadas " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }

        }catch (Exception e) {
            logger.error("Error en acceder al total de guias repasadas ", e);
        }
        return totalGR;
    }

    /**
     * Obtener el total de {@link GuiaRepaso} de folio_ok, firmada, obs,
     *   Sol. NC., Devuelto, Adiciones, Sobre estadía,
     *   Disposición, Muestra propia,  Muestra cliente, Camión devuelto en un rango de fechas.
     * @return Lista {@link Integer}
     */
    @RequestMapping(value ="/guiasByFechas/totalGR", method = RequestMethod.POST)
    @ResponseBody
    public List<List<Integer>> getTotalGuiaRepasoByFechas(@RequestBody Map<String,String> params){

        List<List<Integer>> totalGR = new ArrayList<>();
        List<Integer> total = new ArrayList<>();
        List<GuiaRepaso> guiaRepasos;
        int totalFolio = 0, totalFirma = 0, totalObs = 0, totalSol = 0, totalAdic = 0;
        int totalSobreEstadia = 0, totalDisp = 0, totalMuesPropia = 0, totalMuesCliente = 0, totalCamionDev=0;

        try {
            List <String> fecha_i = Arrays.asList(params.get("fecha_inicial").split("-"));
            List <String> tiempo_i = Arrays.asList(params.get("t_inicio").split(":"));
            List <String> tiempo_f = Arrays.asList(params.get("t_final").split(":"));

            int año_i = Integer.parseInt(fecha_i.get(0));
            int mes_i = Integer.parseInt(fecha_i.get(1));
            int dia_i = Integer.parseInt(fecha_i.get(2));
            int hora_i = Integer.parseInt(tiempo_i.get(0));
            int minuto_i = Integer.parseInt(tiempo_i.get(1));

            int hora_f = Integer.parseInt(tiempo_f.get(0));
            int minuto_f = Integer.parseInt(tiempo_f.get(1));

            List <String> fecha_f = Arrays.asList(params.get("fecha_final").split("-"));
            int año_f = Integer.parseInt(fecha_f.get(0));
            int mes_f = Integer.parseInt(fecha_f.get(1));
            int dia_f = Integer.parseInt(fecha_f.get(2));

            LocalDateTime t_inicio = LocalDateTime.now().withYear(año_i).withMonth(mes_i).withDayOfMonth(dia_i).
                    withHour(hora_i).withMinute(minuto_i).withSecond(0).withNano(0);

            LocalDateTime t_final  = LocalDateTime.now().withYear(año_f).withMonth(mes_f).withDayOfMonth(dia_f).
                    withHour(hora_f).withMinute(minuto_f).withSecond(59);

            for (int i = 0; i < params.size() - 4; i++) {
                guiaRepasos = guiaService.findAllGuiaRepasoByCentroAndRangoFecha(params.get("centro" + i),
                        t_inicio, t_final);
                for(GuiaRepaso gr:guiaRepasos){
                    if(gr!=null) {
                        if (gr.getFolios_ok() == 1)
                            totalFolio++;
                        if (gr.getRc_firmado() == 1)
                            totalFirma++;
                        if (gr.getObs() != null && !gr.getObs().equals(""))
                            totalObs++;
                        if (gr.getSolicitud_nc() == 1)
                            totalSol++;
                        if (gr.getAc_adit_cant() != null || (gr.getAc_solicitante_adit() != null
                                && gr.getAc_solicitante_adit().equals("")) || gr.getAc_firmado_adit() == 1)
                            totalAdic++;
                        if (gr.getAc_adit_cant() != null || (gr.getAc_solicitante_tiempo_camion() != null
                                && gr.getAc_solicitante_tiempo_camion().equals("")) || gr.getAc_firmado_tiempo_camion() == 1)
                            totalSobreEstadia++;
                        if (gr.getAc_hormigon() != null || (gr.getAc_solicitante_hormigon() != null
                                && gr.getAc_solicitante_hormigon().equals("")) || gr.getAc_firmado_hormigon() == 1)
                            totalDisp++;
                        if (gr.getMue_propia() == 1)
                            totalMuesPropia++;
                        if (gr.getMue_cliente() == 1)
                            totalMuesCliente++;
                        if (gr.getCamion_devuelto() == 1)
                            totalCamionDev++;
                    }
                }
                total.add(totalFolio);
                total.add(totalFirma);
                total.add(totalObs);
                total.add(totalSol);
                total.add(totalAdic);
                total.add(totalSobreEstadia);
                total.add(totalDisp);
                total.add(totalMuesPropia);
                total.add(totalMuesCliente);
                total.add(totalCamionDev);

                totalGR.add(new ArrayList<Integer>(total));
                total.clear();
                totalFolio = 0;
                totalFirma = 0;
                totalObs = 0;
                totalSol = 0;
                totalAdic = 0;
                totalSobreEstadia = 0;
                totalDisp = 0;
                totalMuesPropia = 0;
                totalMuesCliente = 0;
                totalCamionDev=0;
            }

            if (totalGR.size() != 0)
                logger.info("Se ha accedido a obtener el total de guias repasadas " +
                        "con los totales "+ totalGR + "y con parametros " + params + " con exito");
            else {
                logger.info("Se ha tratado de obtener el total de guias repasadas " +
                        "filtrados de Guia con parametros " + params + " sin exito");
                return null;
            }

        }catch (Exception e) {
            logger.error("Error en acceder al total de guias repasadas ", e);
        }
        return totalGR;
    }

    /**
     * Actualizar el estado de una {@link Guia} en la base de datos
     * @param params,id
     * @return {@link Guia}
     */
    @RequestMapping(value ="guias/{id}/guiaEstado/", method = RequestMethod.POST)
    @ResponseBody
    public Guia actualizarGuiaEstado(@RequestBody Map<String,String> params,@PathVariable Long id){

        Guia nueva = guiaService.getGuiaById(id);
        String estado = params.get("estado");
        //if(estado.equals("SOL_EMISION")) System.out.println("Se solicita Guia Electronica");
        /*if(nueva != null) {
            GuiaEstado guiaEstado = guiaEstadoService.getByEstado(params.get("estado"));
            nueva.setGuiaEstado(guiaEstado);
            guiaService.save(nueva);

            GuiaHistorial guiaHistorial = new GuiaHistorial(null,params.get("usuario"),params.get("observacion"));
            guiaHistorial.setGuia(nueva);
            guiaHistorial.setGuiaEstado(nueva.getGuiaEstado());

            guiaService.saveHistorial(guiaHistorial);

            nueva.getGuiaEstado().setGuiaEstadoOpcion(guiaEstadoOpcionRepository.findByGuiaEstado(nueva.getGuiaEstado()));

        }
        else {
            logger.info("El usuario Test ha tratado de actualizar un recurso Guia con id "+id+" y parametros" +
                   params+ " sin exito");
            return null;
        }*/
        try {
            //System.out.println("Pasa por el anular de Leo");
            String URLProxy = parametroService.findDefaultValorByParametro(
                    "Ruta Proxy DTE");
            String encodedObs = Base64.getEncoder().encodeToString(params.get("observacion").getBytes());
            /*System.out.println(encodedObs);
            byte[] decodedBytes = Base64.getDecoder().decode(encodedObs);
            String decodedObs = new String(decodedBytes);
            System.out.println(decodedObs);*/
            Unirest.get(URLProxy+"anularGuia/"+ nueva.getId()+"/"+params.get("usuario")+"/"+encodedObs).asString();
        } catch (Exception e) {
            logger.error("Error en actualizar estado de guia: ", e);
        }

        logger.info("Se ha actualizado un recurso Guia con id "+id+" y con parametros " +
                params+" con exito");

        return null;
    }

    /**
     * Chequear si timeout de una {@link Guia} esta expirado.
     * @param id
     * @return {@link Guia}
     */
    @RequestMapping(value ="guias/{id}/timeoutExpirado", method = RequestMethod.POST)
    @ResponseBody
    public Boolean checkTimeoutExpirado(@PathVariable Long id){

        String timeout = null;
        Guia guia = guiaService.getGuiaById(id);
        timeout = parametroService.findDefaultValorByParametroAndPlanta(
                "Minutos de espera", guia.getTick_ship_plant_code());
        /*if (timeout == null) {
            timeout = parametroRepository.findDefaultValorByParametro("Minutos de espera");
        }*/
        LocalDateTime fecha_inicio = guia.getFechaCreacion().plusMinutes(Long.parseLong(timeout) + 1);
        LocalDateTime fecha_a_comparar = LocalDateTime.now();

        if (fecha_inicio.compareTo(fecha_a_comparar) < 0) {
            //System.out.println("Hay que cambiar estado a Timeout");
            try {
                String URLProxy = parametroService.findDefaultValorByParametro(
                        "Ruta Proxy DTE");
                Unirest.get(URLProxy+"notificarCe/" + guia.getId()).asString();
            } catch (UnirestException e) {
                logger.error("Error en actualizar estado de guia a timeout expirado: ", e);
            }
            //System.out.println("Se cambio el estado");
        }

        return true;
    }

    /**
     * Creación de la conexión Websockets con las vistas.
     * @return SseEmitter
     */
    @RequestMapping(value ="subscribe", method = RequestMethod.GET)
    public SseEmitter subscribe() {
        //System.out.println("Entro un Receptor");
        SseEmitter sseEmitter = new SseEmitter();
        Emisor prueba = Emisor.getInstance();
        List<SseEmitter> sseEmitters = prueba.getSseEmitters();
        synchronized (sseEmitters) {
            sseEmitters.add(sseEmitter);
            sseEmitter.onCompletion(() -> {
                synchronized (sseEmitters) {
                    sseEmitters.remove(sseEmitter);
                }
            });
            sseEmitter.onTimeout(sseEmitter::complete);
        }
        return sseEmitter;
    }
}
