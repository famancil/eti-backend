package cl.cbb.gdeh.config;

import cl.cbb.gdeh.controllers.GuiaRepasoController;
import cl.cbb.gdeh.entities.Guia;
import cl.cbb.gdeh.entities.GuiaRepaso;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDBCCmd {

    /**
     * Logger del JDBCCmd.
     */
    private static final Logger logger = LogManager.getLogger(JDBCCmd.class);

    private Connection connObj;

    public JDBCCmd() {
    }

    public JDBCCmd(String server, String db, String user, String pass) throws SQLException {
        String connectionUrl = String.format("jdbc:sqlserver://%s:1433;databaseName=%s;user=%s;password=%s", server, db,
                user, pass);

        connObj = DriverManager.getConnection(connectionUrl);

    }

    public Boolean testCmd() throws SQLException {
        Statement stmtObj = null;
        ResultSet resObj = null;
        try {
            stmtObj = connObj.createStatement();
            String strSql = "SELECT TOP(1) * FROM [dbo].repaso_guia_hormigon";
            resObj = stmtObj.executeQuery(strSql);
            if (resObj.next())
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try
            {
                if (stmtObj != null) stmtObj.close();
                if (resObj != null) resObj.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public String getEmailProgWeb(String obra) throws SQLException {
        Statement stmtObj = null;
        ResultSet resObj = null;
        try {
            stmtObj = connObj.createStatement();
            String strSql = "EXEC [dbo].[PW_SEL_CorreosUltimaSolicitud] @Obra_Codigo = N'" + obra + "'";

            resObj = stmtObj.executeQuery(strSql);
            if (resObj.next()) {
                return resObj.getString(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try
            {
                if (stmtObj != null) stmtObj.close();
                if (resObj != null) resObj.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        return null;
        /*System.out.println(resObj);
        System.out.println(resObj.next());
        System.out.println(resObj.getString(1));
        System.out.println(resObj.next());*/
    }

    public String getTiemposRepaso(GuiaRepaso guiaRepaso,Guia guia) throws SQLException {

        Statement stmtObj = null;
        ResultSet resObj = null;
        try {
            stmtObj = connObj.createStatement();
            String strSql = "SELECT load_time,to_job_time,on_job_time,begin_unld_time," +
                    "to_plant_time,at_plant_time FROM dbo.tick (NOLOCK) AS ti " +
                    "WHERE LTRIM(ti.order_code) LIKE " + guia.getTick_order_code().trim() +
                    " AND LTRIM(ti.tkt_code) LIKE " + guia.getTick_tkt_code().trim() + ";";

            //System.out.println(strSql);
            resObj = stmtObj.executeQuery(strSql);
        /*if(resObj.next()){
            return resObj.getString(1);
        }
        else return null;
         */
            resObj.next();
            guiaRepaso.setHora_carga(resObj.getString(1));
            guiaRepaso.setSalida_planta(resObj.getString(2));
            guiaRepaso.setLlegada_obra(resObj.getString(3));
            guiaRepaso.setInicio_descarga(resObj.getString(4));
            guiaRepaso.setSalida_obra(resObj.getString(5));
            guiaRepaso.setIngreso_planta(resObj.getString(6));
        }catch (Exception e){
            logger.error("Error en obtener tiempos de CmdSeriesQA: ", e);
        }finally {
            try
            {
                if (stmtObj != null) stmtObj.close();
                if (resObj != null) resObj.close();
            }
            catch(Exception ex)
            {
                logger.error("Error en obtener tiempos de CmdSeriesQA: ", ex);
            }
        }

        /*System.out.println(resObj.getString(1));
        //System.out.println(resObj.getString(51));
        System.out.println(resObj.getString(2));
        System.out.println(resObj.getString(3));
        System.out.println(resObj.getString(4));
        //System.out.println(resObj.getString(55));
        //System.out.println(resObj.getString(56));
        //System.out.println(resObj.getString(57));
        System.out.println(resObj.getString(5));
        System.out.println(resObj.getString(6));*/

        /*System.out.println(resObj);
        System.out.println(resObj.next());
        System.out.println(resObj.getString(1));
        System.out.println(resObj.next());*/
        return null;
    }

    public void saveGuiaRepasoCmd(GuiaRepaso guiaRepaso, Guia guia) throws SQLException {

        Statement stmtObj = null;
        try {
        stmtObj = connObj.createStatement();
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String fecha1 = guia.getTick_order_date().trim();
        ZonedDateTime zdt = ZonedDateTime.parse(fecha1);
        LocalDateTime ldt = zdt.toLocalDateTime();
        List<String> tiempo = null;
        tiempo = Arrays.asList(guiaRepaso.getSalida_planta().split(":"));
        String salida_planta = ldt.withHour(Integer.parseInt(tiempo.get(0))).
                withMinute(Integer.parseInt(tiempo.get(1))).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        tiempo = Arrays.asList(guiaRepaso.getLlegada_obra().split(":"));
        String llegada_obra = ldt.withHour(Integer.parseInt(tiempo.get(0))).
                withMinute(Integer.parseInt(tiempo.get(1))).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        tiempo = Arrays.asList(guiaRepaso.getInicio_descarga().split(":"));
        String inicio_descarga = ldt.withHour(Integer.parseInt(tiempo.get(0))).
                withMinute(Integer.parseInt(tiempo.get(1))).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        tiempo = Arrays.asList(guiaRepaso.getSalida_obra().split(":"));
        String salida_obra = ldt.withHour(Integer.parseInt(tiempo.get(0))).
                withMinute(Integer.parseInt(tiempo.get(1))).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        tiempo = Arrays.asList(guiaRepaso.getIngreso_planta().split(":"));
        String ingreso_planta = ldt.withHour(Integer.parseInt(tiempo.get(0))).
                withMinute(Integer.parseInt(tiempo.get(1))).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        /*System.out.println(salida_planta);
        System.out.println(llegada_obra);
        System.out.println(inicio_descarga);
        System.out.println(salida_obra);
        System.out.println(ingreso_planta);*/
        /*System.out.println(tiempo);
        System.out.println(fecha);
        System.out.println(ldt.withHour(Integer.parseInt(tiempo.get(0))).withMinute(Integer.parseInt(tiempo.get(1))));
        */
        String strSql = "EXEC p_actualiza_repaso_guias_hormigon @nro_guia='" + guia.getTick_tkt_code().trim() + "', @fecha_pedido='" +
                guia.getTick_order_date().trim() + "', @numero_pedido ='" + guia.getTick_order_code().trim() + "', @to_job_time='" +
                salida_planta + "', @on_job_time='" + llegada_obra + "', @begin_unld_time='" + inicio_descarga + "', @to_plant_time=" +
                "'" + salida_obra + "', @at_plant_time='" + ingreso_planta + "', @observacion_cliente='" + guiaRepaso.getObs() + "'" +
                ",@comentario=null, @adit_superplastif=" + guiaRepaso.getAc_adit_cant() + ", @cono =null, " +
                " @cantidad_horm_retorno =" + guiaRepaso.getAc_hormigon() + ", @camion_retornado=" + guiaRepaso.getCamion_devuelto() + ", @motivo_retorno=null, " +
                "@destino_final=null, @muestreo=null, @cod_laboratorio=0, " +
                "@numero_muestra=" + guiaRepaso.getMue_numero() + ", @cono_muestra=" + guiaRepaso.getMue_asentamiento() + ", @hora_muestra=null," +
                "@odoini=1, @odofin=2, @horoini=3, @horofin=4, @sol_nc='" + guiaRepaso.getSolicitud_nc() + "', @observaciones=null, @cantidad_bombeo=null, " +
                "@cod_proveedor_servicio=null, @numero_bomba=null, @codigo_servicio=null";

        /*String strSql = "EXEC p_actualiza_repaso_guias_hormigon '7297443', '2019-10-27'," +
                "'103', '2019-10-26', '2019-10-26', '2019-10-26', '2019-10-26'," +
                "'2019-10-26', null, null, null, null, null, 0, null, null, null, 0, null, null, null," +
                "1, 2, 3, 4, null, 'REPASO DE GUIAS 2.0', null, null, null, null";*/

        //System.out.println(strSql);
        int resObj = stmtObj.executeUpdate(strSql);
        System.out.println(resObj);
        }catch (Exception e){
        e.printStackTrace();
        }finally {
            try
            {
                if (stmtObj != null) stmtObj.close();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        /*System.out.println(resObj.next());
        System.out.println(resObj.getString(1));*/
    }
}
