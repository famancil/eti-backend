package cl.cbb.gdeh.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "guia_historial")
public class GuiaHistorial implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDateTime fecha;

    private String usuario;

    private String observacion;

    private Guia guia;

    //private TickEstado tickEstado;

    private GuiaEstado guiaEstado;

    public GuiaHistorial(){
        super();
    }

    public GuiaHistorial(LocalDateTime fecha, String usuario, String observacion) {
        super();
        this.fecha = fecha;
        this.usuario = usuario;
        this.observacion = observacion;
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

    @Column(name = "fecha")
    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Column(name = "usuario")
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Column(name = "observacion")
    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @JoinColumn(name = "guia_id", referencedColumnName = "id")
    @ManyToOne
    @JsonBackReference
    public Guia getGuia() {
        return guia;
    }

    public void setGuia(Guia guia) {
        this.guia = guia;
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
}
