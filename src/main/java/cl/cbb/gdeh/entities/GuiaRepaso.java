package cl.cbb.gdeh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "guia_repaso")
public class GuiaRepaso implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String obs;

    private String usuario;

    private String hora_carga;

    private String salida_planta;

    private String llegada_obra;

    private String inicio_descarga;

    private String salida_obra;

    private String ingreso_planta;

    private Integer solicitud_nc ;

    private String rc_nombre;

    private String rc_rut;

    private Integer rc_firmado;

    private Integer folios_ok;

    private Integer ac_agua;

    private String ac_solicitante_agua;

    private Integer ac_firmado_agua;

    private Integer ac_adit_cant;

    private String ac_solicitante_adit;

    private Integer ac_firmado_adit;

    private String ac_tipo_adit;

    private String ac_solicitante_tipo_adit;

    private Integer ac_firmado_tipo_adit;

    private Integer ac_tiempo_camion;

    private String ac_solicitante_tiempo_camion;

    private Integer ac_firmado_tiempo_camion;

    private Integer ac_hormigon;

    private String ac_solicitante_hormigon;

    private Integer ac_firmado_hormigon;

    private Integer mue_propia;

    private Integer mue_cliente;

    private String mue_laboratorio;

    private Integer mue_numero;

    private String mue_asentamiento;

    private Integer camion_devuelto;

    private Guia guia;

    //@DateTimeFormat(pattern="dd/MM/YYYY HH:mm:ss")
    private LocalDateTime fechaActualizacion;

    public GuiaRepaso(){
        super();
    }

    public GuiaRepaso(String obs, String usuario, String hora_carga, String salida_planta, String llegada_obra,
                      String inicio_descarga, String salida_obra, String ingreso_planta, Integer solicitud_nc,
                      String rc_nombre, String rc_rut, Integer rc_firmado, Integer folios_ok,
                      Integer ac_adit_cant, String mue_laboratorio, Integer mue_numero, String mue_asentamiento,
                      Integer camion_devuelto) {
        super();
        this.obs = obs;
        this.usuario = usuario;
        this.hora_carga = hora_carga;
        this.salida_planta = salida_planta;
        this.llegada_obra = llegada_obra;
        this.inicio_descarga = inicio_descarga;
        this.salida_obra = salida_obra;
        this.ingreso_planta = ingreso_planta;
        this.solicitud_nc = solicitud_nc;
        this.rc_nombre = rc_nombre;
        this.rc_rut = rc_rut;
        this.rc_firmado = rc_firmado;
        this.folios_ok = folios_ok;
        this.ac_adit_cant = ac_adit_cant;
        this.mue_laboratorio = mue_laboratorio;
        this.mue_numero = mue_numero;
        this.mue_asentamiento = mue_asentamiento;
        this.camion_devuelto = camion_devuelto;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "obs")
    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    @Column(name = "usuario")
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Column(name = "hora_carga")
    public String getHora_carga() {
        return hora_carga;
    }

    public void setHora_carga(String hora_carga) {
        this.hora_carga = hora_carga;
    }

    @Column(name = "salida_planta")
    public String getSalida_planta() {
        return salida_planta;
    }

    public void setSalida_planta(String salida_planta) {
        this.salida_planta = salida_planta;
    }

    @Column(name = "llegada_obra")
    public String getLlegada_obra() {
        return llegada_obra;
    }

    public void setLlegada_obra(String llegada_obra) {
        this.llegada_obra = llegada_obra;
    }

    @Column(name = "inicio_descarga")
    public String getInicio_descarga() {
        return inicio_descarga;
    }

    public void setInicio_descarga(String inicio_descarga) {
        this.inicio_descarga = inicio_descarga;
    }

    @Column(name = "salida_obra")
    public String getSalida_obra() {
        return salida_obra;
    }

    public void setSalida_obra(String salida_obra) {
        this.salida_obra = salida_obra;
    }

    @Column(name = "ingreso_planta")
    public String getIngreso_planta() {
        return ingreso_planta;
    }

    public void setIngreso_planta(String ingreso_planta) {
        this.ingreso_planta = ingreso_planta;
    }

    @Column(name = "solicitud_nc")
    public Integer getSolicitud_nc() {
        return solicitud_nc;
    }

    public void setSolicitud_nc(Integer solicitud_nc) {
        this.solicitud_nc = solicitud_nc;
    }

    @Column(name = "rc_nombre")
    public String getRc_nombre() {
        return rc_nombre;
    }

    public void setRc_nombre(String rc_nombre) {
        this.rc_nombre = rc_nombre;
    }

    @Column(name = "rc_rut")
    public String getRc_rut() {
        return rc_rut;
    }

    public void setRc_rut(String rc_rut) {
        this.rc_rut = rc_rut;
    }


    @Column(name = "rc_firmado")
    public Integer getRc_firmado() {
        return rc_firmado;
    }

    public void setRc_firmado(Integer rc_firmado) {
        this.rc_firmado = rc_firmado;
    }

    @Column(name = "folios_ok")
    public Integer getFolios_ok() {
        return folios_ok;
    }

    public void setFolios_ok(Integer folios_ok) {
        this.folios_ok = folios_ok;
    }

    @Column(name = "ac_agua")
    public Integer getAc_agua() {
        return ac_agua;
    }

    public void setAc_agua(Integer ac_agua) {
        this.ac_agua = ac_agua;
    }

    @Column(name = "ac_solicitante_agua")
    public String getAc_solicitante_agua() {
        return ac_solicitante_agua;
    }

    public void setAc_solicitante_agua(String ac_solicitante_agua) {
        this.ac_solicitante_agua = ac_solicitante_agua;
    }

    @Column(name = "ac_firmado_agua")
    public Integer getAc_firmado_agua() {
        return ac_firmado_agua;
    }

    public void setAc_firmado_agua(Integer ac_firmado_agua) {
        this.ac_firmado_agua = ac_firmado_agua;
    }

    @Column(name = "ac_adit_cant")
    public Integer getAc_adit_cant() {
        return ac_adit_cant;
    }

    public void setAc_adit_cant(Integer ac_adit_cant) {
        this.ac_adit_cant = ac_adit_cant;
    }

    @Column(name = "ac_solicitante_adit")
    public String getAc_solicitante_adit() {
        return ac_solicitante_adit;
    }

    public void setAc_solicitante_adit(String ac_solicitante_adit) {
        this.ac_solicitante_adit = ac_solicitante_adit;
    }

    @Column(name = "ac_firmado_adit")
    public Integer getAc_firmado_adit() {
        return ac_firmado_adit;
    }

    public void setAc_firmado_adit(Integer ac_firmado_adit) {
        this.ac_firmado_adit = ac_firmado_adit;
    }

    @Column(name = "ac_tipo_adit")
    public String getAc_tipo_adit() {
        return ac_tipo_adit;
    }

    public void setAc_tipo_adit(String ac_tipo_adit) {
        this.ac_tipo_adit = ac_tipo_adit;
    }

    @Column(name = "ac_solicitante_tipo_adit")
    public String getAc_solicitante_tipo_adit() {
        return ac_solicitante_tipo_adit;
    }

    public void setAc_solicitante_tipo_adit(String ac_solicitante_tipo_adit) {
        this.ac_solicitante_tipo_adit = ac_solicitante_tipo_adit;
    }

    @Column(name = "ac_firmado_tipo_adit")
    public Integer getAc_firmado_tipo_adit() {
        return ac_firmado_tipo_adit;
    }

    public void setAc_firmado_tipo_adit(Integer ac_firmado_tipo_adit) {
        this.ac_firmado_tipo_adit = ac_firmado_tipo_adit;
    }

    @Column(name = "ac_tiempo_camion")
    public Integer getAc_tiempo_camion() {
        return ac_tiempo_camion;
    }

    public void setAc_tiempo_camion(Integer ac_tiempo_camion) {
        this.ac_tiempo_camion = ac_tiempo_camion;
    }

    @Column(name = "ac_solicitante_tiempo_camion")
    public String getAc_solicitante_tiempo_camion() {
        return ac_solicitante_tiempo_camion;
    }

    public void setAc_solicitante_tiempo_camion(String ac_solicitante_tiempo_camion) {
        this.ac_solicitante_tiempo_camion = ac_solicitante_tiempo_camion;
    }

    @Column(name = "ac_firmado_tiempo_camion")
    public Integer getAc_firmado_tiempo_camion() {
        return ac_firmado_tiempo_camion;
    }

    public void setAc_firmado_tiempo_camion(Integer ac_firmado_tiempo_camion) {
        this.ac_firmado_tiempo_camion = ac_firmado_tiempo_camion;
    }

    @Column(name = "ac_hormigon")
    public Integer getAc_hormigon() {
        return ac_hormigon;
    }

    public void setAc_hormigon(Integer ac_hormigon) {
        this.ac_hormigon = ac_hormigon;
    }

    @Column(name = "ac_solicitante_hormigon")
    public String getAc_solicitante_hormigon() {
        return ac_solicitante_hormigon;
    }

    public void setAc_solicitante_hormigon(String ac_solicitante_hormigon) {
        this.ac_solicitante_hormigon = ac_solicitante_hormigon;
    }

    @Column(name = "ac_firmado_hormigon")
    public Integer getAc_firmado_hormigon() {
        return ac_firmado_hormigon;
    }

    public void setAc_firmado_hormigon(Integer ac_firmado_hormigon) {
        this.ac_firmado_hormigon = ac_firmado_hormigon;
    }

    @Column(name = "mue_propia")
    public Integer getMue_propia() {
        return mue_propia;
    }

    public void setMue_propia(Integer mue_propia) {
        this.mue_propia = mue_propia;
    }

    @Column(name = "mue_cliente")
    public Integer getMue_cliente() {
        return mue_cliente;
    }

    public void setMue_cliente(Integer mue_cliente) {
        this.mue_cliente = mue_cliente;
    }

    @Column(name = "mue_laboratorio")
    public String getMue_laboratorio() {
        return mue_laboratorio;
    }

    public void setMue_laboratorio(String mue_laboratorio) {
        this.mue_laboratorio = mue_laboratorio;
    }

    @Column(name = "mue_numero")
    public Integer getMue_numero() {
        return mue_numero;
    }

    public void setMue_numero(Integer mue_numero) {
        this.mue_numero = mue_numero;
    }

    @Column(name = "mue_asentamiento")
    public String getMue_asentamiento() {
        return mue_asentamiento;
    }

    public void setMue_asentamiento(String mue_asentamiento) {
        this.mue_asentamiento = mue_asentamiento;
    }

    @Column(name = "camion_devuelto")
    public Integer getCamion_devuelto() {
        return camion_devuelto;
    }

    public void setCamion_devuelto(Integer camion_devuelto) {
        this.camion_devuelto = camion_devuelto;
    }

    @JoinColumn(name = "guia_id", referencedColumnName = "id")
    @OneToOne
    @JsonIgnore
    public Guia getGuia() {
        return guia;
    }

    public void setGuia(Guia guia) {
        this.guia = guia;
    }

    @Column(name = "fecha_actualizacion")
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
