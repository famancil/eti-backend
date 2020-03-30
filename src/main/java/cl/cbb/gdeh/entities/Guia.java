package cl.cbb.gdeh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "guia")
public class Guia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String folio_sii;

    @NotNull
    private String tick_order_date;

    @NotNull
    private String tick_order_code;

    private String tick_tkt_code;

    private String tick_tkt_date;

    private String cliente;

    private String obra;

    private String tick_driv_empl_code;

    private String driver;

    private String tick_ship_plant_code;

    private String tick_ship_plant_loc_code;

    private String tick_truck_code;

    private String lic_num;

    private String tick_typed_time;

    private String tick_proj_code;

    private String tick_delv_addr;

    private String numero_sello;

    private String cust_code;

    private String cust_rut;

    private String cust_addr;

    private String cust_comuna;

    private String cust_ciudad;

    private String po;

    private String load_time;

    private String cust_giro;

    private String cust_job_num;

    //@NotNull
    //private TickEstado tickEstado;

    @NotNull
    private GuiaEstado guiaEstado;

    private List<GuiaHistorial> guiaHistorials;

    @DateTimeFormat(pattern="dd/MM/YYYY")
    private LocalDateTime fechaCreacion;

    @DateTimeFormat(pattern="dd/MM/YYYY")
    private LocalDateTime fechaActualizacion;

    private GuiaRepaso guiaRepaso;

    private List<GuiaDetalle> guiaDetalle;

    public Guia(){
        super();
    }

    public Guia(String folio_sii, String tick_order_date, String tick_order_code, String tick_tkt_code, String tick_tkt_date,
                String cliente, String obra, String tick_driv_empl_code, String tick_ship_plant_code,
                String tick_ship_plant_loc_code, String tick_truck_code, String tick_typed_time, String tick_proj_code,
                String tick_delv_addr, GuiaEstado guiaEstado) {
        super();
        this.folio_sii = folio_sii;
        this.tick_order_date = tick_order_date;
        this.tick_order_code = tick_order_code;
        this.tick_tkt_code = tick_tkt_code;
        this.tick_tkt_date = tick_tkt_date;
        this.cliente = cliente;
        this.obra = obra;
        this.tick_driv_empl_code = tick_driv_empl_code;
        this.tick_ship_plant_code = tick_ship_plant_code;
        this.tick_ship_plant_loc_code = tick_ship_plant_loc_code;
        this.tick_truck_code = tick_truck_code;
        this.tick_typed_time = tick_typed_time;
        this.tick_proj_code = tick_proj_code;
        this.tick_delv_addr = tick_delv_addr;
        //this.tickEstado = tickEstado;
        this.guiaEstado = guiaEstado;
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

    @Column(name = "folio_sii")
    public String getFolio_sii() {
        return folio_sii;
    }

    public void setFolio_sii(String folio_sii) {
        this.folio_sii = folio_sii;
    }

    @Column(name = "tick_order_date")
    public String getTick_order_date() {
        return tick_order_date;
    }

    public void setTick_order_date(String tick_order_date) {
        this.tick_order_date = tick_order_date;
    }

    @Column(name = "tick_order_code")
    public String getTick_order_code() {
        return tick_order_code;
    }

    public void setTick_order_code(String tick_order_code) {
        this.tick_order_code = tick_order_code;
    }

    @Column(name = "tick_tkt_code")
    public String getTick_tkt_code() {
        return tick_tkt_code;
    }

    public void setTick_tkt_code(String tick_tkt_code) {
        this.tick_tkt_code = tick_tkt_code;
    }

    @Column(name = "tick_tkt_date")
    public String getTick_tkt_date() {
        return tick_tkt_date;
    }

    public void setTick_tkt_date(String tick_tkt_date) {
        this.tick_tkt_date = tick_tkt_date;
    }

    @Column(name = "cliente")
    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    @Column(name = "obra")
    public String getObra() {
        return obra;
    }

    public void setObra(String obra) {
        this.obra = obra;
    }

    @Column(name = "tick_driv_empl_code")
    public String getTick_driv_empl_code() {
        return tick_driv_empl_code;
    }

    public void setTick_driv_empl_code(String tick_driv_empl_code) {
        this.tick_driv_empl_code = tick_driv_empl_code;
    }

    @Column(name = "tick_ship_plant_code")
    public String getTick_ship_plant_code() {
        return tick_ship_plant_code;
    }

    public void setTick_ship_plant_code(String tick_ship_plant_code) {
        this.tick_ship_plant_code = tick_ship_plant_code;
    }

    @Column(name = "tick_ship_plant_loc_code")
    public String getTick_ship_plant_loc_code() {
        return tick_ship_plant_loc_code;
    }

    public void setTick_ship_plant_loc_code(String tick_ship_plant_loc_code) {
        this.tick_ship_plant_loc_code = tick_ship_plant_loc_code;
    }

    @Column(name = "tick_truck_code")
    public String getTick_truck_code() {
        return tick_truck_code;
    }

    public void setTick_truck_code(String tick_truck_code) {
        this.tick_truck_code = tick_truck_code;
    }

    @Column(name = "tick_typed_time")
    public String getTick_typed_time() {
        return tick_typed_time;
    }

    public void setTick_typed_time(String tick_typed_time) {
        this.tick_typed_time = tick_typed_time;
    }

    @Column(name = "tick_proj_code")
    public String getTick_proj_code() {
        return tick_proj_code;
    }

    public void setTick_proj_code(String tick_proj_code) {
        this.tick_proj_code = tick_proj_code;
    }

    @Column(name = "tick_delv_addr")
    public String getTick_delv_addr() {
        return tick_delv_addr;
    }

    public void setTick_delv_addr(String tick_delv_addr) {
        this.tick_delv_addr = tick_delv_addr;
    }

    /*@JoinColumn(name = "tick_estado_id", referencedColumnName = "id")
    @ManyToOne
    public TickEstado getTickEstado() {
        return tickEstado;
    }

    public void setTickEstado(TickEstado tickEstado) {
        this.tickEstado = tickEstado;
    }*/

    @JoinColumn(name = "guia_estado_id", referencedColumnName = "id")
    @ManyToOne
    public GuiaEstado getGuiaEstado() {
        return guiaEstado;
    }

    public void setGuiaEstado(GuiaEstado guiaEstado) {
        this.guiaEstado = guiaEstado;
    }

    @Column(name = "fecha_creacion")
    //@Temporal(TemporalType.DATE)
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Column(name = "fecha_actualizacion")
    //@Temporal(TemporalType.DATE)
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @OneToOne(optional = false, mappedBy = "guia")
    public GuiaRepaso getGuiaRepaso() {
        return guiaRepaso;
    }

    public void setGuiaRepaso(GuiaRepaso guiaRepaso) {
        this.guiaRepaso = guiaRepaso;
    }

    @Column(name = "driver")
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @Column(name = "lic_num")
    public String getLic_num() {
        return lic_num;
    }

    public void setLic_num(String lic_num) {
        this.lic_num = lic_num;
    }

    @Column(name = "numero_sello")
    public String getNumero_sello() {
        return numero_sello;
    }

    public void setNumero_sello(String numero_sello) {
        this.numero_sello = numero_sello;
    }

    @OneToMany
    @JoinColumn(name = "guia_id")
    public List<GuiaHistorial> getGuiaHistorials() {
        return guiaHistorials;
    }

    public void setGuiaHistorials(List<GuiaHistorial> guiaHistorials) {
        this.guiaHistorials = guiaHistorials;
    }

    @Column(name = "cust_code")
    public String getCust_code() {
        return cust_code;
    }

    public void setCust_code(String cust_code) {
        this.cust_code = cust_code;
    }

    @Column(name = "cust_rut")
    public String getCust_rut() {
        return cust_rut;
    }

    public void setCust_rut(String cust_rut) {
        this.cust_rut = cust_rut;
    }

    @Column(name = "cust_addr")
    public String getCust_addr() {
        return cust_addr;
    }

    public void setCust_addr(String cust_addr) {
        this.cust_addr = cust_addr;
    }

    @Column(name = "cust_comuna")
    public String getCust_comuna() {
        return cust_comuna;
    }

    public void setCust_comuna(String cust_comuna) {
        this.cust_comuna = cust_comuna;
    }

    @Column(name = "cust_ciudad")
    public String getCust_ciudad() {
        return cust_ciudad;
    }

    public void setCust_ciudad(String cust_ciudad) {
        this.cust_ciudad = cust_ciudad;
    }

    @Column(name = "po")
    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    @Column(name = "load_time")
    public String getLoad_time() {
        return load_time;
    }

    public void setLoad_time(String load_time) {
        this.load_time = load_time;
    }

    @Column(name = "cust_giro")
    public String getCust_giro() {
        return cust_giro;
    }

    public void setCust_giro(String cust_giro) {
        this.cust_giro = cust_giro;
    }

    @Column(name = "cust_job_num")
    public String getCust_job_num() {
        return cust_job_num;
    }

    public void setCust_job_num(String cust_job_num) {
        this.cust_job_num = cust_job_num;
    }

    @OneToMany
    @JoinColumn(name = "guia_id")
    public List<GuiaDetalle> getGuiaDetalle() {
        return guiaDetalle;
    }

    public void setGuiaDetalle(List<GuiaDetalle> guiaDetalle) {
        this.guiaDetalle = guiaDetalle;
    }
}
