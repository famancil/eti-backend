package cl.cbb.gdeh.controllers;

import cl.cbb.gdeh.config.ClientAppConfig;
import cl.cbb.gdeh.config.JDBCCmd;
import cl.cbb.gdeh.config.UserClient;
import cl.cbb.gdeh.security.jwt.JwtProvider;
import cl.cbb.gdeh.services.ParametroService;
import cl.cbb.gdeh.user.client.APARSP;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TestController {

    /**
     * Logger del controlador de Test.
     */
    private static final Logger logger = LogManager.getLogger(TestController.class);

    /**
     * Servicio que provee el JWT Secret Key y JWT LifeTime.
     */
    @Autowired
    JwtProvider jwtProvider;

    /**
     * Servicio utilizado para el manejo del parametro
     */
    @Autowired
    private ParametroService parametroService;

    @Autowired
    ResourceLoader resourceLoader;

    /**
     * Probar Servicio APA para el inicio de sesion
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/signin", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testAuthenticateUser() {

        try{

            //String email = "maximiliano.rojas@grupocsbb.cl";
            //String password = "02,inicio";
            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.register(ClientAppConfig.class);
            ctx.refresh();
            UserClient usc = ctx.getBean(UserClient.class);
            String email = "maximiliano.rojas@gruposcbb.cl";
            String password = "02,inicio";

            APARSP response = usc.getResponse(email,password);

            if(response.getROW().size() != 0) {
                //jwtProvider.generateJwtToken(email);
                logger.info("El usuario Test ha iniciado sesión con los datos email: " + email
                        +", password: "+password+ " con exito");
            }
            else {
                logger.info("Se ha intentado probar inicio de sesión con los datos email: " + email
                        +", password: "+password+ " sin exito");
                return ResponseEntity.ok(true);
            }
           /*HttpResponse<String> response = Unirest.post("http://10.249.88.3:8182/cxf/apa?wsdl")
                    .body("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:apa=\"http://www.grupocbb.cl/ws/apa\">\n" +
                            "   <soapenv:Header/>\n" +
                            "   <soapenv:Body>\n" +
                            "      <apa:APA>\n" +
                            "         <p_user>"+email+"</p_user>\n" +
                            "         <!--Optional:-->\n" +
                            "         <p_password>"+password+"</p_password>\n" +
                            "         <p_aplica>25</p_aplica>\n" +
                            "         <p_dominio>11</p_dominio>\n" +
                            "      </apa:APA>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>")
                    .asString();

            JSONObject jsonObject = XML.toJSONObject(response.getBody());
            System.out.println(jsonObject);*/

        } catch(Exception e){
            logger.error("Error en Inicio Sesión: ",e);
            return ResponseEntity.ok(false);
        }


        return ResponseEntity.ok(true);
    }

    /**
     * Probar Servicio UF
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/UF", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testUF() {

        String api_key = parametroService.findDefaultValorByParametro("API KEY SBIF");

        try {
            String URL = "https://api.sbif.cl/api-sbifv3/recursos_api/uf?apikey="+api_key+
                    "&formato=xml";
            URL u = new URL(URL);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();
            if(code != 200){
                logger.info("Se ha tratado de acceder al valor UF con api_key "+api_key+" sin exito");
                return ResponseEntity.ok(false);
            }
            /*HttpResponse<String> response = Unirest.get("https://api.sbif.cl/api-sbifv3/recursos_api/uf?apikey="+api_key+
                    "&formato=xml")
                    .asString();*/

            //JSONObject jsonObject = XML.toJSONObject(response.getBody());
            //fecha = jsonObject.getJSONObject("IndicadoresFinancieros").getJSONObject("UFs").getJSONObject("UF").get("Fecha").toString();
            //valor = jsonObject.getJSONObject("IndicadoresFinancieros").getJSONObject("UFs").getJSONObject("UF").get("Valor").toString();
            //String codigo =
            //parametro.setValor(valor+"-"+fecha);
            //parametroService.saveParametro(parametro);


        } catch (Exception e) {
            logger.info("Se ha tratado de acceder al valor UF con api_key "+api_key+" sin exito");
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }

    /**
     * Probar URL 1 de DTE.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/guiasPDFURL2", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testGuiaPDFURL2() {

        String urlPDF = null;

        try {

            String URLPDF = parametroService.findDefaultValorByParametro("Ruta Guia Electronica 2");

            urlPDF = URLPDF + "2019/08/08/1877015.pdf";

            URL u = new URL(urlPDF);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();

            if (code == 200 || code ==404) {
                logger.info("Se ha accedido al recurso DTE por URL "+urlPDF+" de manera exitosa");
            }
            else{
                logger.info("Se ha accedido al recurso DTE por URL "+urlPDF+" sin exito");
                return ResponseEntity.ok(false);
            }

        } catch (IOException | NullPointerException e) {
            logger.info("Se ha accedido al recurso DTE por URL "+urlPDF+" sin exito");
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }

    /**
     * Probar URL 2 de DTE.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/guiasPDFURL1", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testGuiaPDFURL1() {

        String urlPDF = null;

        try {

            String URLPDF = parametroService.findDefaultValorByParametro("Ruta Guia Electronica 1");

            urlPDF = URLPDF + "2019/08/08/1877015.pdf";

            URL u = new URL(urlPDF);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();

            if (code == 200 || code == 404) {
                logger.info("Se ha accedido al recurso DTE por URL "+urlPDF+" de manera exitosa");
            }
            else{
                logger.info("Se ha accedido al recurso DTE por URL "+urlPDF+" sin exito");
                return ResponseEntity.ok(false);
            }

        } catch (IOException | NullPointerException e) {
            logger.info("Se ha accedido al recurso DTE por URL "+urlPDF+" sin exito");
            return ResponseEntity.ok(false);
        }

        return ResponseEntity.ok(true);
    }

    /**
     * Probar URL de Escaneada.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/guiasEscaneadas", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testGuiaEscaneadaURL() {

        try {

            String user = parametroService.findDefaultValorByParametro("userFileSamba");
            String pass = parametroService.findDefaultValorByParametro("passFileSamba");
            String URLScan = parametroService.findDefaultValorByParametro("Ruta escaneados");
            String path = URLScan+"000-2119.pdf";

            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",user, pass);
            SmbFile smbFile = new SmbFile(path,auth);

            if(smbFile.getContentLength()!=0) {
                logger.info("Se ha accedido al recurso Guia Escaneada con credenciales "+user+
                        ","+pass+" de manera exitosa");
            }
            else {
                logger.error("Se ha buscado la guia escaneada con credenciales "+user
                        +","+pass+" sin exito.");
                return ResponseEntity.ok(false);
            }


        } catch (Exception e) {
            logger.error("Se ha buscado la guia escaneada sin exito");
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);

    }

    /**
     * Probar URL de Escaneada.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/zesales", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testZesales() {

        try {
            HttpResponse<String> response = Unirest.post("http://172.33.0.163:8181/cxf/ZESALES_DELIVERY_OBRA_V3")
                    .body("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:zes=\"http://www.grupocbb.cl/sap/rfc/zesales_delivery_obra_v3\">\n" +
                            "   <soapenv:Header/>\n" +
                            "   <soapenv:Body>\n" +
                            "      <zes:ZESALES_DELIVERY_OBRA_V3>\n" +
                            "         <PKUNNR>0000</PKUNNR>\n" +
                            "         <PVBELN>0</PVBELN>\n" +
                            "      </zes:ZESALES_DELIVERY_OBRA_V3>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>")
                    .asString();
            JSONObject jsonObject = XML.toJSONObject(response.getBody());
            String campo1 = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").
                    getJSONObject("rsp:ZESALES_DELIVERY_OBRA_V3_RSP").get("CAMPO1").toString();
            //System.out.println(jsonObject);
            //System.out.println(campo1);
        } catch (UnirestException e) {
            logger.error("Se ha buscado datos del cliente sin exito");
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);

    }

    /**
     * Probar Conexión a Programación Web.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/proweb", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testProWeb() {

        try {
            JDBCCmd cmdDb = new JDBCCmd(parametroService.findDefaultValorByParametro("proWebSqlServer"),
                    parametroService.findDefaultValorByParametro("proWebSqlDb"),
                    parametroService.findDefaultValorByParametro("proWebSqlUser"),
                    parametroService.findDefaultValorByParametro("proWebSqlPass"));
            //System.out.println(Integer.parseInt(obra));
            String campo1 = cmdDb.getEmailProgWeb("264699");
            //System.out.println(campo1);
        }catch (Exception e){
            logger.error("Se ha tratado de acceder a Programación Web sin exito");
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);

    }

    /**
     * Probar Conexión a Command.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/cmd", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testCmd() {

        try {
            JDBCCmd cmdDb = new JDBCCmd(parametroService.findDefaultValorByParametro("cmdSqlServer"),
                    parametroService.findDefaultValorByParametro("cmdSqlDb"),
                    parametroService.findDefaultValorByParametro("cmdSqlUser"),
                    parametroService.findDefaultValorByParametro("cmdSqlPass"));
            Boolean test = cmdDb.testCmd();
            System.out.println(test);
            if (!test){
                logger.error("Se ha tratado de acceder a Command sin exito");
                return ResponseEntity.ok(false);
            }
            //System.out.println(Integer.parseInt(obra));
            //campo1 = cmdDb.getEmailProgWeb(Integer.parseInt(guia.getCust_code().trim()));
            //System.out.println(campo1);
        }catch (Exception e){
            logger.error("Se ha tratado de acceder a Command sin exito");
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);

    }

    /**
     * Probar escribir Logs.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/logs/{lineas}", method = RequestMethod.GET)
    @ResponseBody
    public void writeLog(@PathVariable int lineas, HttpServletResponse response) {
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {

            BufferedReader reader = new BufferedReader(new FileReader("/logs/app.log"));
            int maxlineas = 0;
            while (reader.readLine() != null) maxlineas++;
            reader.close();

            br = new BufferedReader(new FileReader("/logs/app.log"));
            bw = new BufferedWriter(new FileWriter("/logs/app2.log"));

            //String line = br.readLine();

            /*for( int i = 1; i <= 10 && line != null; i++)
            {
                bw.write(line);
                bw.write("\n");
                line = br.readLine();
            }*/

            File file = new File("/logs/app.log");
            int counter = 0;
            if(lineas > maxlineas) lineas = maxlineas;
            ReversedLinesFileReader object = new ReversedLinesFileReader(file, Charset.defaultCharset());
            while(counter < lineas) {
                String line = object.readLine();
                bw.write(line);
                bw.write("\n");
                //line = br.readLine();
                //System.out.println(object.readLine());
                counter++;
            }

            //System.out.println("Lines are Successfully copied!");

            br.close();
            bw.close();

            File fileToDownload = new File("/logs/app2.log");

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+fileToDownload.getName());
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();

        }catch (Exception e){
            logger.error("Se ha tratado de escribir logs sin exito: ",e);
            //System.out.println(e.getCause());
            //return ResponseEntity.ok(false);
        }
        //return ResponseEntity.ok(true);
    }

    /**
     * Probar obtener Logs.
     * @return True si el servicio funciona o False en caso contrario.
     */
    /*@RequestMapping(value ="/test/logs", method = RequestMethod.GET)
    @ResponseBody
    public void getLog(HttpServletResponse response) {

        try {

            File fileToDownload = new File("/logs/app2.log");

            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+fileToDownload.getName());
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();

            //File archivo = new File("/home/famancil/Documentos/modelo-controlador/logs/app.log");
            //return new FileSystemResource(archivo);
            //directoryDownloader(new File("/home/famancil/Documentos/modelo-controlador/logs/app.log"));

            //File[] listOfFiles = folder.listFiles();


        }catch (Exception e){
            logger.error("Se ha tratado de acceder a logs sin exito: ",e);
            //return ResponseEntity.ok(false);
        }
        //return ResponseEntity.ok(true);
    }*/

    /**
     * Probar Conexión a BD.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/bd", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testBd() {

        try {
            JDBCCmd cmdDb = new JDBCCmd(parametroService.findDefaultValorByParametro("cmdSqlServer"),
                    parametroService.findDefaultValorByParametro("cmdSqlDb"),
                    parametroService.findDefaultValorByParametro("cmdSqlUser"),
                    parametroService.findDefaultValorByParametro("cmdSqlPass"));
            //System.out.println(parametroService.findDefaultValorByParametro("cmdSqlServer"));
            //System.out.println(Integer.parseInt(obra));
            //campo1 = cmdDb.getEmailProgWeb(Integer.parseInt(guia.getCust_code().trim()));
            //System.out.println(campo1);
        }catch (Exception e){
            logger.error("Se ha tratado de acceder a Command sin exito");
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);

    }

    /**
     * Probar Conexión a Command.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/IConstruye", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testIConstruye() {

        try {
            String urlIcons = parametroService.findDefaultValorByParametro("URL IConstruye");
            URL u = new URL(urlIcons);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();

            if (code != 200) {
                logger.error("Se ha tratado de acceder a IConstruye sin exito");
                return ResponseEntity.ok(false);
            }

        }catch (Exception e){
            logger.error("Se ha tratado de acceder a IConstruye sin exito");
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);

    }

    /**
     * Probar Conexión a Command.
     * @return True si el servicio funciona o False en caso contrario.
     */
    @RequestMapping(value ="/test/listen", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity testListen() {

        try {
            String UrlProxy = parametroService.findDefaultValorByParametro("Ruta Proxy DTE");
            /*String urlIcons = parametroService.findDefaultValorByParametro("URL IConstruye");
            URL u = new URL(urlIcons);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();

            if (code != 200) {
                logger.error("Se ha tratado de acceder a IConstruye sin exito");
                return ResponseEntity.ok(false);
            }*/

            HttpResponse<String> response = Unirest.post(UrlProxy+"emitirGuia/0")
                    .asString();
            System.out.println(response.getBody());

        }catch (Exception e){
            logger.error("Se ha tratado de acceder a Proxy DTE sin exito");
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);

    }


}
