package cl.cbb.gdeh.services;

import cl.cbb.gdeh.controllers.GuiaController;
import cl.cbb.gdeh.entities.*;
import cl.cbb.gdeh.repositories.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sendgrid.*;
import jcifs.smb.SmbFile;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class GuiaService {

    /**
     * Logger del Servicio de Guia.
     */
    private static final Logger logger = LogManager.getLogger(GuiaService.class);

    /**
     * Repositorio para el manejo CRUD de la entidad {@link Guia}
     */
    @Autowired
    private GuiaRepository guiaRepository;

    /**
     * Repositorio para el manejo CRUD de la entidad {@link TickEstado}
     */
    @Autowired
    private TickEstadoRepository tickEstadoRepository;

    /**
     * Repositorio para el manejo CRUD de la entidad {@link GuiaEstado}
     */
    @Autowired
    private GuiaEstadoRepository guiaEstadoRepository;

    /**
     * Repositorio para el manejo CRUD de la entidad {@link GuiaRepaso}
     */
    @Autowired
    private GuiaRepasoRepository guiaRepasoRepository;

    /**
     * Repositorio para el manejo CRUD de la entidad {@link GuiaHistorial}
     */
    @Autowired
    private GuiaHistorialRepository guiaHistorialRepository;

    /**
     * Repositorio para el manejo CRUD de la entidad {@link Parametro}
     */
    @Autowired
    private ParametroRepository parametroRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    ResourceLoader resourceLoader;

    /**
     * Servicio utilizado para el manejo del parametro
     */
    @Autowired
    private ParametroService parametroService;

    /**
     * Servicio utilizado para el manejo del GuiaEstado.
     */
    @Autowired
    private GuiaEstadoService guiaEstadoService;

    /**
     * Servicio utilizado para el manejo del Guia Detalle
     */

    @Autowired
    private GuiaDetalleRepository guiaDetalleRepository;

    /**
     * Obtiene una {@link Guia} segun el id que se pase como parametro.
     */

    public Guia getGuiaById(Long idGuia){
        Guia guia = guiaRepository.getById(idGuia);
        if(guia!=null){
            return guia;
        }
        else return null;

    }

    /**
     * Obtiene una {@link Guia} segun la clave del ticket que se pasa como parametro.
     */
    public Guia getGuiaByClaveTicket(String claveTicket){

        String[] claveSeparada = claveTicket.split("-");
        String order_date = claveSeparada[0]+"-"+claveSeparada[1]+"-"+claveSeparada[2];
        String order_code = claveSeparada[3];
        String tkt_code = claveSeparada[4];

        System.out.println(order_date);
        System.out.println(order_code);
        System.out.println(tkt_code);

        Guia guia = guiaRepository.findGuiaByOrderDateAndOrderCodeAndTktCode(order_date, order_code,tkt_code);
        if(guia!=null){
            return guia;
        }
        else return null;

    }

    /**
     * Obtiene el {@link Guia} segun el folio que se pase como parametro.
     */

    public Guia getGuiaByFolio(String folio){
        Guia guia = guiaRepository.getByFolio(folio);
        if(guia!=null){
            return guia;
        }
        else return null;

    }

    /**
     * Obtiene el {@link Guia} segun el id que se pase como parametro.
     */

    public List<Guia> getAllGuia(){
        List<Guia> guias = guiaRepository.findByOrderByIdAsc();
        if(guias!=null){
            return guias;
        }
        else return null;
    }

    /**
     * Obtiene todos los {@link Guia} con los atributos de {@link GuiaRepaso).
     */

    public List<Guia> getAllGuiaAndGuiaRepaso(){
        List<Guia> guias = guiaRepository.findAllGuiaAndOrderDesc();
        if(guias!=null){
            /*for(int i=0;i<guias.size();i++){
                GuiaRepaso guiaRepaso = guiaRepasoRepository.findByGuiaId(guias.get(i).getId());
                System.out.println(guiaRepaso.getGuia().getFolio_sii());
                if(guiaRepaso != null) guias.get(i).setGuiaRepaso(guiaRepaso);
            }*/
            return guias;
        }
        else return null;
    }

    /**
     * Obtiene todos los {@link Guia} con un estado especifico.
     */
    public List<Guia> getAllGuiaByGuiaEstado(String centro,String estado){
        List<Guia> guias = null;
       /* System.out.println("Centro: "+centro);
        System.out.println("Estado: "+estado);*/
        if(estado.equals("EMITIDA_SII")){
           guias = guiaRepository.findAllGuiaEmitScanRep(centro);
        } else {
           guias = guiaRepository.findAllGuiaByGuiaEstadoAndOrderDesc(estado, centro);
        }
        //System.out.println("Guias: "+guias);
        if(guias!=null){
            return guias;
        }
        else return null;
    }

    /**
     * Obtiene todos los {@link Guia} con un estado especifico, en las ultimas X horas.
     */
    public List<Guia> getAllGuiaByGuiaEstadoAndHoras(String centro,String estado,
                                                     LocalDateTime fecha_comienzo, LocalDateTime fecha_final){
        List<Guia> guias = null;
       /* System.out.println("Centro: "+centro);
        System.out.println("Estado: "+estado);*/
        if(estado.equals("EMITIDA_SII")){
            guias = guiaRepository.getAllGuiasEmitScanRepAndRangoFecha(centro,fecha_comienzo,fecha_final);
        } else {
            guias = guiaRepository.getAllGuiasByEstadoAndRangeDate(estado, centro,fecha_comienzo,fecha_final);
        }
        //System.out.println("Guias: "+guias);
        if(guias!=null){
            return guias;
        }
        else return null;
    }

    /**
     * Obtiene todos los {@link Guia} con un estado especifico, en las ultimas X horas.
     */
    public Integer findAllGuiaByEstadoAndHoras(String centro,String estado,
                                                     LocalDateTime fecha_comienzo, LocalDateTime fecha_final){

        return guiaRepository.findTotalbyEstadoAndRangeDate(estado, centro,fecha_comienzo,fecha_final);
    }

    /**
     * Obtiene todos los {@link GuiaRepaso} con un centro especifico y rango de fecha.
     */
    public List<GuiaRepaso> findAllGuiaRepasoByCentroAndRangoFecha(String centro,
                                               LocalDateTime fecha_comienzo, LocalDateTime fecha_final){

        return guiaRepository.getAllGuiasRepasoByRangeDate(centro,fecha_comienzo,fecha_final);
    }

    /**
     * Obtiene todos una lista de {@link Guia} que posean un centro igual al parametro
     */

    public List<Guia> getAllGuiasFiltradas(String cod_centro){
        //HashMap<Long, Guia> map = new HashMap<>();
        PageRequest pageRequest = PageRequest.of(0, 100);
        List<Guia> guias = guiaRepository.getByCodCentro(cod_centro,pageRequest);
        /*for(Guia guia:guias){
            map.put(guia.getId(),guia);
        }*/
        if(guias!=null){
            return guias;
        }
        else return null;
    }

    /**
     * Obtiene todos una lista de {@link GuiaHistorial} que posean una guia igual al parametro
     */

    public List<GuiaHistorial> getAllGuiasHistoricas(Guia guia){
        List<GuiaHistorial> guiaHistorials = guiaHistorialRepository.findByGuia(guia);
        if(guiaHistorials!=null){
            return guiaHistorials;
        }
        else return null;
    }

    /**
     * Obtiene todos una lista de {@link Guia} que esten en el rango de fecha especificado
     */

    public List<Guia> getAllGuiasByRangeDate(String cod_centro,LocalDateTime fecha_comienzo, LocalDateTime fecha_final){

        List<Guia> guias = guiaRepository.getAllGuiasByRangeDate(cod_centro,fecha_comienzo,fecha_final);

        for (Guia guia : guias) {
            guia.setGuiaDetalle(guiaDetalleRepository.findByGuiaId(guia.getId()));
        }
        /*for (Guia guia:guias){
            if(guia.getGuiaEstado().getId() == 1 || guia.getGuiaEstado().getId() == 11 ||
                    guia.getGuiaEstado().getId() == 12) {
                String timeout = null;
                timeout = parametroRepository.findDefaultValorByParametroAndCodPlanta(
                        "Minutos de espera", guia.getTick_ship_plant_code());
                if (timeout == null) {
                    timeout = parametroRepository.findDefaultValorByParametro("Minutos de espera");
                }
                LocalDateTime fecha_inicio = guia.getFechaCreacion().plusMinutes(Long.parseLong(timeout) + 1);
                LocalDateTime fecha_a_comparar = LocalDateTime.now();

                if (fecha_inicio.compareTo(fecha_a_comparar) < 0) {
                    HttpResponse<String> response = null;
                    System.out.println("Hay que cambiar estado a Timeout");
                    try {
                        response = Unirest.get("http://172.16.1.2:8080/api/notificarCe/" + guia.getId()).asString();
                    } catch (UnirestException e) {
                        logger.error("Error en actualizar estado de guia a timeout expirado: ", e);
                    }
                    System.out.println("Se cambio el estado");
                }
            }
        }*/

        //System.out.println(guias);
        return guias;
    }

    /**
     * Obtiene todos los {@link Guia} emitidas con un centro.
     */
    public List<Guia> getAllGuiaEmitidasByFechas(String cod_centro,LocalDateTime fecha_comienzo, LocalDateTime fecha_final){

        List<Guia> guias = guiaRepository.findAllGuiaEmitScanRepByFechas(cod_centro,fecha_comienzo,fecha_final);

        return guias;
    }

    /**
     * Obtiene todos una lista de {@link Guia} que esten en el rango de fecha y estado especificado
     */
    public List<Guia> getAllGuiasByEstadoAndRangeDate(String estado, String cod_centro,
                                                      LocalDateTime fecha_comienzo,
                                                      LocalDateTime fecha_final){
        //System.out.println(cod_centro);
        List<Guia> guias = guiaRepository.getAllGuiasByEstadoAndRangeDate(estado,cod_centro,
                fecha_comienzo,fecha_final);
        //System.out.println(guias);
        return guias;
    }

    /**
     * Obtiene todos una lista de {@link Guia} que esten en la fecha y estado especificado
     */
    public List<Guia> getAllGuiasByEstadoAndDate(String estado, String cod_centro,
                                                      LocalDateTime fecha_comienzo){
        //System.out.println(cod_centro);
        List<Guia> guias = guiaRepository.getAllGuiasByEstadoAndDate(estado,cod_centro,
                fecha_comienzo);
        //System.out.println(guias);
        return guias;
    }

    /**
     * Guarda un {@link Guia} nuevo en la base de datos.
     */
    public void saveGuia(Guia guia, Long idTickEstado, Long idGuiaEstado){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date fechaActual = new java.util.Date();

        Optional<TickEstado> tickEstado = tickEstadoRepository.findById(idTickEstado);
        /*if ( guia.getTickEstado() == null ) {
            guia.setTickEstado(tickEstado.get());
        }*/

        Optional<GuiaEstado> guiaEstado = guiaEstadoRepository.findById(idGuiaEstado);
        if ( guia.getGuiaEstado() == null ) {
            guia.setGuiaEstado(guiaEstado.get());
        }

        if ( guia.getFechaCreacion() == null ) {
            guia.setFechaCreacion(LocalDateTime.now());
        }

        guia.setFechaActualizacion(LocalDateTime.now());

        guiaRepository.save(guia);
    }

    /**
     * Guarda un {@link Guia} nuevo en la base de datos.
     */
    public void save(Guia guia){

        guia.setFechaActualizacion(LocalDateTime.now());

        guiaRepository.save(guia);
    }

    /**
     * Guarda un {@link Guia} nuevo en la base de datos.
     */
    public void saveHistorial(GuiaHistorial guiaHistorial){

        GuiaHistorial actualizar = new GuiaHistorial();

        actualizar.setFecha(LocalDateTime.now());
        actualizar.setGuia(guiaHistorial.getGuia());
        //actualizar.setTickEstado(guiaHistorial.getTickEstado());
        actualizar.setGuiaEstado(guiaHistorial.getGuiaEstado());

        if(guiaHistorial.getObservacion() != null) {
            actualizar.setObservacion(guiaHistorial.getObservacion());
        }

        //actualizar.setTickEstado(guiaHistorial.getTickEstado());
        actualizar.setUsuario(guiaHistorial.getUsuario());

        guiaHistorialRepository.save(actualizar);
    }

    public void sendEmailDefaultQA(List emails,String folio, Boolean isEmailExist){

        System.out.println("Entra en solo QA");

        Guia guia = guiaRepository.getByFolio(folio);

        String formato = Arrays.asList(guia.getTick_order_date().split("T")).get(0);
        List<String> formatoList = Arrays.asList(formato.split("-"));
        String fecha = formatoList.get(2)+"-"+formatoList.get(1)+"-"+formatoList.get(0);

        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper1 = null;
        try {
            String emailAdmin = parametroService.findDefaultValorByParametro("Email Administrador");
            String cco = parametroService.findDefaultValorByParametro("CCO");

            //emailAdmin = "sergio.carvallo@";
            helper1 = new MimeMessageHelper(msg, true);
            helper1.setFrom(new InternetAddress("no-reply@grupocbb.cl"));
            //String email = "famancil.inf@gmail.com";
            //helper1.setTo(email);
            helper1.setTo(emailAdmin);
            //String cco = parametroService.findDefaultValorByParametro("CCO");
            helper1.setBcc(cco);
            helper1.setSubject("Guia N° "+guia.getFolio_sii()+" Fecha "+fecha);

            if(isEmailExist) {
                helper1.setText("Estimado Administrador \n\nSe ha intentado enviar copia escaneada de la guia folio " +
                        guia.getFolio_sii() + " a los correos " + emails + " sin exito dado que el archivo a adjuntar (000-" + guia.getFolio_sii() + ".pdf) no esta disponible." +
                        "\n\nAtentamente \n\nCbb Ready Mix");
            }
            else {
                helper1.setText("Estimado Administrador \n\nSe ha intentado enviar copia escaneada de la guia folio " +
                        guia.getFolio_sii() + " pero no existe registro de correos en SAP y Programación Web." +
                        "\n\nAtentamente \n\nCbb Ready Mix");
            }


            /*helper1.setText("Estimado Administrador \n\nSe ha intentado enviar copia escaneada de la guia folio " +
                    guia.getFolio_sii()+" sin exito dado que el archivo a adjuntar (000-"+guia.getFolio_sii()+".pdf) no esta disponible."+
                    "\n\nAtentamente \n\nGDEH");*/
            javaMailSender.send(msg);

            /*Email from = new Email("cbbreadymix@cbb.cl");
            String subject = "Guia N° "+guia.getFolio_sii()+" Fecha "+fecha;

            Content content = null;
            if(isEmailExist) {
                content = new Content("text/plain", "Estimado Administrador \n\nSe ha intentado enviar copia escaneada de la guia folio " +
                        guia.getFolio_sii() + " a los correos " + emails + " sin exito dado que el archivo a adjuntar (000-" + guia.getFolio_sii() + ".pdf) no esta disponible." +
                        "\n\nAtentamente \n\nCbb Ready Mix");
            }
            else {
                content = new Content("text/plain", "Estimado Administrador \n\nSe ha intentado enviar copia escaneada de la guia folio " +
                        guia.getFolio_sii() + " pero no existe registro de correos en SAP y Programación Web." +
                        "\n\nAtentamente \n\nCbb Ready Mix");
            }
            //Mail mail = new Mail(from, subject, to, content);
            Mail mail = new Mail();
            Personalization p1 = new Personalization();
            //p1.addTo(new Email("felipe.mancilla@estrategia-ti.cl"));
            p1.addTo(new Email(emailAdmin));
            if(!emailAdmin.equals(cco)) p1.addBcc(new Email(cco));
            //p1.addBcc(new Email("famancil.inf@gmail.com"));

            mail.setFrom(from);
            mail.setSubject(subject);
            mail.addContent(content);
            mail.addPersonalization(p1);


            SendGrid sg = new SendGrid("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");
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendEmailDefault(List emails,String folio, Boolean isEmailExist){

        System.out.println("Entra en solo");

        Guia guia = guiaRepository.getByFolio(folio);

        String formato = Arrays.asList(guia.getTick_order_date().split("T")).get(0);
        List<String> formatoList = Arrays.asList(formato.split("-"));
        String fecha = formatoList.get(2)+"-"+formatoList.get(1)+"-"+formatoList.get(0);

        /*MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper1 = null;*/
        try {
            String emailAdmin = parametroService.findDefaultValorByParametro("Email Administrador");
            String cco = parametroService.findDefaultValorByParametro("CCO");

            //emailAdmin = "sergio.carvallo@";
            /*helper1 = new MimeMessageHelper(msg, true);
            helper1.setFrom(new InternetAddress("no-reply@grupocbb.cl"));
            String email = "famancil.inf@gmail.com";
            helper1.setTo(email);
            //helper1.setTo(emailAdmin);
            String cco = parametroService.findDefaultValorByParametro("CCO");
            helper1.setBcc(cco);
            helper1.setSubject("Guia N° "+guia.getFolio_sii()+" Fecha "+fecha);
            helper1.setText("Estimado Administrador \n\nSe ha intentado enviar copia escaneada de la guia folio " +
                    guia.getFolio_sii()+" sin exito dado que el archivo a adjuntar (000-"+guia.getFolio_sii()+".pdf) no esta disponible."+
                    "\n\nAtentamente \n\nGDEH");
            javaMailSender.send(msg);*/

            Email from = new Email("cbbreadymix@cbb.cl");
            String subject = "Guia N° "+guia.getFolio_sii()+" Fecha "+fecha;

            Content content = null;
            if(isEmailExist) {
                content = new Content("text/plain", "Estimado Administrador \n\nSe ha intentado enviar copia escaneada de la guia folio " +
                        guia.getFolio_sii() + " a los correos " + emails + " sin exito dado que el archivo a adjuntar (000-" + guia.getFolio_sii() + ".pdf) no esta disponible." +
                        "\n\nAtentamente \n\nCbb Ready Mix");
            }
            else {
                content = new Content("text/plain", "Estimado Administrador \n\nSe ha intentado enviar copia escaneada de la guia folio " +
                        guia.getFolio_sii() + " pero no existe registro de correos en SAP y Programación Web." +
                        "\n\nAtentamente \n\nCbb Ready Mix");
            }
            //Mail mail = new Mail(from, subject, to, content);
            Mail mail = new Mail();
            Personalization p1 = new Personalization();
            //p1.addTo(new Email("felipe.mancilla@estrategia-ti.cl"));
            p1.addTo(new Email(emailAdmin));
            if(!emailAdmin.equals(cco)) p1.addBcc(new Email(cco));
            //p1.addBcc(new Email("famancil.inf@gmail.com"));
            /*System.out.println(p1.getBccs());
            System.out.println(p1.getTos());*/
            mail.setFrom(from);
            mail.setSubject(subject);
            mail.addContent(content);
            mail.addPersonalization(p1);


            SendGrid sg = new SendGrid("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");
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            //System.out.println(response.getBody());
            System.out.println(response.getHeaders());

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void sendEmailWithAttachment(List emails,String folio,
                                        InputStream PDF) throws MessagingException, IOException {

        System.out.println("Entra en attachment");

        //MimeMessage msg = javaMailSender.createMimeMessage();
        //Esto debe comentarse para mandar correo
        //email = "felipe.mancilla@alumnos.usm.cl;famancil.inf@gmail.com";
        //MimeMessageHelper helper1 = new MimeMessageHelper(msg, true);
        //new InternetAddress("no-reply@grupocbb.cl", "no-reply");

        /*helper1.setFrom(new InternetAddress("no-reply <srvc_aplicaciones.net@grupocbb.cl>"));

        String[] emails = email.split(";");
        helper1.setTo(emails);

        String cco = parametroService.findDefaultValorByParametro("CCO");
        helper1.setBcc(cco);

        //helper1.setSubject("Guia N° "+folio+" Guardada");

        //List<String> folioSII = new ArrayList<String>(Arrays.asList(folio.split("-")));
        Guia guia = guiaRepository.getByFolio(folio);

        //Hay que revisar que si el folio que se mando existe en la BD.
        // Puede tirar problema null.

        // true = text/html
        try {

            String formato = Arrays.asList(guia.getTick_order_date().split("T")).get(0);
            List<String> formatoList = Arrays.asList(formato.split("-"));
            String fecha = formatoList.get(2)+"-"+formatoList.get(1)+"-"+formatoList.get(0);

            helper1.setSubject("Guia N° "+guia.getFolio_sii()+" Fecha "+fecha);

            helper1.setText("Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N°" +guia.getFolio_sii() +
                    " correspondiente al día "+fecha+" para la obra "+guia.getObra()+
                    "\n\nAtentamente \n\nCbb Ready Mix");
        }catch (NullPointerException e){
            helper1.setSubject("Guia N° XXXX Fecha XXXX");
            helper1.setText("Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N° (Número No encontrado)" +
                    " correspondiente al día (Fecha no encontrada) para la obra (Nombre no encontrado)"+
                    "\n\nAtentamente \n\nCbb Ready Mix");
        }


        ByteArrayDataSource attachment = new ByteArrayDataSource(PDF, "application/pdf");
        helper1.addAttachment("Guia Folio N°"+folio+".pdf",attachment);

        javaMailSender.send(msg);*/
        Guia guia = guiaRepository.getByFolio(folio);

        try {

            String formato = Arrays.asList(guia.getTick_order_date().split("T")).get(0);
            List<String> formatoList = Arrays.asList(formato.split("-"));
            String fecha = formatoList.get(2)+"-"+formatoList.get(1)+"-"+formatoList.get(0);

            Email from = new Email("cbbreadymix@cbb.cl");
            String subject = "Guia N° "+guia.getFolio_sii()+" Fecha "+fecha;
            //Email to = new Email("leonardo.velasquez@estrategia-ti.cl");

            //Mail mail = new Mail(from, subject, to, content);
            Mail mail = new Mail();


            String emailAdmin = parametroService.findDefaultValorByParametro("Email Administrador");
            String cco = parametroService.findDefaultValorByParametro("CCO");

            Boolean enviarCliente = Boolean.valueOf(parametroService.findDefaultValorByParametro("Enviar Clientes"));
            System.out.println(enviarCliente);
            Personalization p1 = new Personalization();

            //Esto debe descomentar cuando este listo

            Content content = null;
            if(enviarCliente) {
                //String[] emails = email.split(";");
                for (Object correo: emails){
                    p1.addTo(new Email(correo.toString()));
                }
                content = new Content("text/plain", "Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+
                        "\n\nAtentamente \n\nCbb Ready Mix");
            }
            else {
                p1.addTo(new Email(emailAdmin));
                //p1.addTo(new Email("felipe.mancilla@estrategia-ti.cl"));
                content = new Content("text/plain", "Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+
                        "\n\nAtentamente \n\nCbb Ready Mix \n\n\n"+
                        "PD: Estimado Administrador \n\n Se ha tratado de mandar copia adjuntada de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+ " a los correos "+emails+", pero por configuración se omitió."+
                        "\n\nAtentamente \n\nCbb Ready Mix");
                /*content = new Content("text/plain", "Estimado Administrador \n\n Se ha tratado de mandar copia adjuntada de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+ " a los correos "+emails+", pero por configuración se omitió."+
                        "\n\nAtentamente \n\nCbb Ready Mix");*/
            }

            /*else p1.addTo(new Email(emailAdmin));
            p1.addTo(new Email("s7carvallo@gmail.com"));*/
            //p1.addBcc(new Email("famancil.inf@gmail.com"));
            if(!emailAdmin.equals(cco)) p1.addBcc(new Email(cco));
            /*System.out.println(Arrays.asList(p1.getTos().get(0).getEmail()));
            System.out.println(Arrays.asList(p1.getTos().get(1).getEmail()));
            System.out.println(Arrays.asList(p1.getBccs().get(0).getEmail()));*/
            mail.setFrom(from);
            mail.setSubject(subject);
            mail.addContent(content);
            mail.addPersonalization(p1);

            //System.out.println("Pasa por aqui");


            //System.out.println(archivoEscaneado.getFile().exists());

            byte[] bFile = null;
            try {
                bFile = IOUtils.toByteArray(PDF);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Attachments attachments3 = new Attachments();
            org.apache.commons.codec.binary.Base64 x = new Base64();
            String imageDataString = x.encodeAsString(bFile);
            attachments3.setContent(imageDataString);
            attachments3.setFilename("Guia Folio N°"+folio+".pdf");
            attachments3.setType("application/pdf");
            attachments3.setDisposition("attachment");
            attachments3.setContentId("Banner");
            mail.addAttachments(attachments3);

            String api_key_sendgrid = parametroService.findDefaultValorByParametro("API KEY SendGrid");
            SendGrid sg = new SendGrid(api_key_sendgrid);
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response2 = sg.api(request);
                System.out.println(response2.getStatusCode());
                System.out.println(response2.getBody());
                System.out.println(response2.getHeaders());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }catch (NullPointerException e){

            Email from = new Email("noreply@cbb.cl");
            String subject = "Guia N° XXXX Fecha XXXX";
            Email to = new Email("leonardo.velasquez@estrategia-ti.cl");
            Content content = new Content("text/plain", "Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N° (Número No encontrado)" +
                    " correspondiente al día (Fecha no encontrada) para la obra (Nombre no encontrado)"+
                    "\n\nAtentamente \n\nCbb Ready Mix");
            //Mail mail = new Mail(from, subject, to, content);
            Mail mail = new Mail();
            Personalization p1 = new Personalization();
            p1.addTo(new Email("famancil.inf@gmail.com"));
            p1.addBcc(new Email("felipe.mancilla@estrategia-ti.cl"));
            mail.setFrom(from);
            mail.setSubject(subject);
            mail.addContent(content);
            mail.addPersonalization(p1);
        }

        if(guia.getGuiaEstado().getId() != 6 && guia.getGuiaEstado().getId() != 8) {

            GuiaEstado guiaEstado = null;

            if (guia.getGuiaEstado().getId() != 7)
                guiaEstado = guiaEstadoService.getGuiaEstadoById((long) 6);
            else
                guiaEstado = guiaEstadoService.getGuiaEstadoById((long) 8);

            guia.setGuiaEstado(guiaEstado);
            save(guia);

            GuiaHistorial guiaHistorial = new GuiaHistorial(null, null, null);
            guiaHistorial.setGuia(guia);
            //TickEstado tickEstado = tickEstadoRepository.findById((long) 1).get();
            //System.out.println(tickEstado.getId());
            //guiaHistorial.setTickEstado(tickEstado);
            guiaHistorial.setFecha(LocalDateTime.now());
            guiaHistorial.setGuiaEstado(guia.getGuiaEstado());

            guiaHistorialRepository.save(guiaHistorial);
        }

    }

    public void sendEmailWithAttachmentQA(List<String> emails,String folio,
                                        InputStream PDF) throws MessagingException, IOException {

        System.out.println("Entra en attachment QA");

        MimeMessage msg = javaMailSender.createMimeMessage();

        Guia guia = guiaRepository.getByFolio(folio);

        MimeMessageHelper helper1 = new MimeMessageHelper(msg, true);

        try {

            String emailAdmin = parametroService.findDefaultValorByParametro("Email Administrador");
            Boolean enviarCliente = Boolean.valueOf(parametroService.findDefaultValorByParametro("Enviar Clientes"));


            String formato = Arrays.asList(guia.getTick_order_date().split("T")).get(0);
            List<String> formatoList = Arrays.asList(formato.split("-"));
            String fecha = formatoList.get(2)+"-"+formatoList.get(1)+"-"+formatoList.get(0);
            //Esto debe comentarse para mandar correo
            //email = "felipe.mancilla@alumnos.usm.cl;famancil.inf@gmail.com";

            //new InternetAddress("no-reply@grupocbb.cl", "no-reply");

            helper1.setFrom(new InternetAddress("no-reply <no-reply@grupocbb.cl>"));

            //String[] emails = email.split(";");

            helper1.setSubject("Guia N° "+guia.getFolio_sii()+" Fecha "+fecha);

            if(enviarCliente) {
                //String[] emails = email.split(";");
                String[] tos = emails.toArray(new String[0]);
                helper1.setTo(tos);
                helper1.setText("Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+
                        "\n\nAtentamente \n\nCbb Ready Mix");
            }
            else {
                helper1.setTo(emailAdmin);
                //p1.addTo(new Email("felipe.mancilla@estrategia-ti.cl"));
                helper1.setText("Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+
                        "\n\nAtentamente \n\nCbb Ready Mix \n\n\n"+
                        "PD: Estimado Administrador \n\n Se ha tratado de mandar copia adjuntada de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+ " a los correos "+emails+", pero por configuración se omitió."+
                        "\n\nAtentamente \n\nCbb Ready Mix");

            }


            //

            String cco = parametroService.findDefaultValorByParametro("CCO");
            helper1.setBcc(cco);

            //helper1.setSubject("Guia N° "+folio+" Guardada");

            //List<String> folioSII = new ArrayList<String>(Arrays.asList(folio.split("-")));

            //Hay que revisar que si el folio que se mando existe en la BD.
            // Puede tirar problema null.

            // true = text/html

            /*helper1.setText("Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N°" +guia.getFolio_sii() +
                    " correspondiente al día "+fecha+" para la obra "+guia.getObra()+
                    "\n\nAtentamente \n\nCbb Ready Mix");*/
        }catch (NullPointerException e){
            helper1.setSubject("Guia N° XXXX Fecha XXXX");
            helper1.setText("Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N° (Número No encontrado)" +
                    " correspondiente al día (Fecha no encontrada) para la obra (Nombre no encontrado)"+
                    "\n\nAtentamente \n\nCbb Ready Mix");
        }


        ByteArrayDataSource attachment = new ByteArrayDataSource(PDF, "application/pdf");
        helper1.addAttachment("Guia Folio N°"+folio+".pdf",attachment);

        javaMailSender.send(msg);
        //Guia guia = guiaRepository.getByFolio(folio);

        /*try {

            String formato = Arrays.asList(guia.getTick_order_date().split("T")).get(0);
            List<String> formatoList = Arrays.asList(formato.split("-"));
            String fecha = formatoList.get(2)+"-"+formatoList.get(1)+"-"+formatoList.get(0);

            Email from = new Email("cbbreadymix@cbb.cl");
            String subject = "Guia N° "+guia.getFolio_sii()+" Fecha "+fecha;
            //Email to = new Email("leonardo.velasquez@estrategia-ti.cl");

            //Mail mail = new Mail(from, subject, to, content);
            Mail mail = new Mail();


            String emailAdmin = parametroService.findDefaultValorByParametro("Email Administrador");
            String cco = parametroService.findDefaultValorByParametro("CCO");

            Boolean enviarCliente = Boolean.valueOf(parametroService.findDefaultValorByParametro("Enviar Clientes"));
            System.out.println(enviarCliente);
            Personalization p1 = new Personalization();

            //Esto debe descomentar cuando este listo

            Content content = null;
            if(enviarCliente) {
                //String[] emails = email.split(";");
                for (Object correo: emails){
                    p1.addTo(new Email(correo.toString()));
                }
                content = new Content("text/plain", "Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+
                        "\n\nAtentamente \n\nCbb Ready Mix");
            }
            else {
                p1.addTo(new Email(emailAdmin));
                //p1.addTo(new Email("felipe.mancilla@estrategia-ti.cl"));
                content = new Content("text/plain", "Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+
                        "\n\nAtentamente \n\nCbb Ready Mix \n\n\n"+
                        "PD: Estimado Administrador \n\n Se ha tratado de mandar copia adjuntada de la guía de despacho N°" +guia.getFolio_sii() +
                        " correspondiente al día "+fecha+" para la obra "+guia.getObra()+ " a los correos "+emails+", pero por configuración se omitió."+
                        "\n\nAtentamente \n\nCbb Ready Mix");

            }*/

            /*else p1.addTo(new Email(emailAdmin));
            p1.addTo(new Email("s7carvallo@gmail.com"));*/
            //p1.addBcc(new Email("famancil.inf@gmail.com"));
            //if(!emailAdmin.equals(cco)) p1.addBcc(new Email(cco));
            /*System.out.println(Arrays.asList(p1.getTos().get(0).getEmail()));
            System.out.println(Arrays.asList(p1.getTos().get(1).getEmail()));
            System.out.println(Arrays.asList(p1.getBccs().get(0).getEmail()));*/

            /*mail.setFrom(from);
            mail.setSubject(subject);
            mail.addContent(content);
            mail.addPersonalization(p1);

            //System.out.println("Pasa por aqui");


            //System.out.println(archivoEscaneado.getFile().exists());

            byte[] bFile = null;
            try {
                bFile = IOUtils.toByteArray(PDF);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Attachments attachments3 = new Attachments();
            org.apache.commons.codec.binary.Base64 x = new Base64();
            String imageDataString = x.encodeAsString(bFile);
            attachments3.setContent(imageDataString);
            attachments3.setFilename("Guia Folio N°"+folio+".pdf");
            attachments3.setType("application/pdf");
            attachments3.setDisposition("attachment");
            attachments3.setContentId("Banner");
            mail.addAttachments(attachments3);

            SendGrid sg = new SendGrid("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");
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response2 = sg.api(request);
                System.out.println(response2.getStatusCode());
                System.out.println(response2.getBody());
                System.out.println(response2.getHeaders());
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }catch (NullPointerException e){

            Email from = new Email("noreply@cbb.cl");
            String subject = "Guia N° XXXX Fecha XXXX";
            Email to = new Email("leonardo.velasquez@estrategia-ti.cl");
            Content content = new Content("text/plain", "Estimado Cliente \n\nAdjuntamos copia de la guía de despacho N° (Número No encontrado)" +
                    " correspondiente al día (Fecha no encontrada) para la obra (Nombre no encontrado)"+
                    "\n\nAtentamente \n\nCbb Ready Mix");
            //Mail mail = new Mail(from, subject, to, content);
            Mail mail = new Mail();
            Personalization p1 = new Personalization();
            p1.addTo(new Email("famancil.inf@gmail.com"));
            p1.addBcc(new Email("felipe.mancilla@estrategia-ti.cl"));
            mail.setFrom(from);
            mail.setSubject(subject);
            mail.addContent(content);
            mail.addPersonalization(p1);
        }*/

        if(guia.getGuiaEstado().getId() != 6 && guia.getGuiaEstado().getId() != 8) {

            GuiaEstado guiaEstado = null;

            if (guia.getGuiaEstado().getId() != 7)
                guiaEstado = guiaEstadoService.getGuiaEstadoById((long) 6);
            else
                guiaEstado = guiaEstadoService.getGuiaEstadoById((long) 8);

            guia.setGuiaEstado(guiaEstado);
            save(guia);

            GuiaHistorial guiaHistorial = new GuiaHistorial(null, null, null);
            guiaHistorial.setGuia(guia);
            //TickEstado tickEstado = tickEstadoRepository.findById((long) 1).get();
            //System.out.println(tickEstado.getId());
            //guiaHistorial.setTickEstado(tickEstado);
            guiaHistorial.setFecha(LocalDateTime.now());
            guiaHistorial.setGuiaEstado(guia.getGuiaEstado());

            guiaHistorialRepository.save(guiaHistorial);
        }

    }
}
