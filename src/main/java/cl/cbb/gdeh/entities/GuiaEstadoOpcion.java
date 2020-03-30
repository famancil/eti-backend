package cl.cbb.gdeh.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "guia_estado_opcion")
public class GuiaEstadoOpcion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private GuiaEstado guiaEstado;

    private String opcion;

    public GuiaEstadoOpcion(){
        super();
    }

    public GuiaEstadoOpcion(GuiaEstado guiaEstado, String opcion) {
        super();
        this.guiaEstado = guiaEstado;
        this.opcion = opcion;
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

    @JoinColumn(name = "guia_estado_id", referencedColumnName = "id")
    @OneToOne
    @JsonIgnore
    public GuiaEstado getGuiaEstado() {
        return guiaEstado;
    }

    public void setGuiaEstado(GuiaEstado guiaEstado) {
        this.guiaEstado = guiaEstado;
    }

    @Column(name = "opcion")
    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }
}
