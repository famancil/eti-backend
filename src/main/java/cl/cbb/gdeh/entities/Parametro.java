package cl.cbb.gdeh.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "parametro")
public class Parametro implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String cod_centro;

    private String cod_planta;

    private String categoria;

    private String parametro;

    private String valor;

    private Boolean activo;

    private int excepcionable;

    private String idParametroRef;

    public Parametro(){
        super();
    }

    public Parametro(String categoria, String parametro, String valor,String idParametroRef) {
        super();
        this.cod_centro = "*";
        this.cod_planta = "*";
        this.categoria = categoria;
        this.parametro = parametro;
        this.valor = valor;
        this.activo = true;
        this.idParametroRef = idParametroRef;
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

    @Column(name = "cod_centro")
    public String getCod_centro() {
        return cod_centro;
    }

    public void setCod_centro(String cod_centro) {
        this.cod_centro = cod_centro;
    }

    @Column(name = "cod_planta")
    public String getCod_planta() {
        return cod_planta;
    }

    public void setCod_planta(String cod_planta) {
        this.cod_planta = cod_planta;
    }

    @Column(name = "categoria")
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Column(name = "parametro")
    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    @Column(name = "valor")
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Column(name = "activo")
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Column(name = "idParametroRef")
    public String getIdParametroRef() {
        return idParametroRef;
    }

    public void setIdParametroRef(String idParametroRef) {
        this.idParametroRef = idParametroRef;
    }

    @Column(name = "excepcionable")
    public int getExcepcionable() {
        return excepcionable;
    }

    public void setExcepcionable(int excepcionable) {
        this.excepcionable = excepcionable;
    }
}
