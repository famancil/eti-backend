package cl.cbb.gdeh.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tick_estado")
public class TickEstado implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String estado;

    private String descripcion;

    public TickEstado(){
        super();
    }


    public TickEstado(String estado, String descripcion) {
        super();
        this.estado = estado;
        this.descripcion = descripcion;
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

    @Column(name = "estado")
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Column(name = "descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
