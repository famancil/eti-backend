package cl.cbb.gdeh.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "plantas")
public class Plantas implements Serializable {

    private static final long serialVersionUID = 1L;

    private int codPlanta;

    private String codCentro;

    private String nomPlanta;

    private int vigencia;


    public Plantas() {
        super();
    }

    @Id
    @Column(name = "cod_planta")
    public int getCodPlanta() {
        return codPlanta;
    }

    public void setCodPlanta(int aCodPlanta) {
        codPlanta = aCodPlanta;
    }

    @Column(name = "cod_centro")
    public String getCodCentro() {
        return codCentro;
    }

    public void setCodCentro(String aCodCentro) {
        codCentro = aCodCentro;
    }

    @Column(name = "nom_planta")
    public String getNomPlanta() {
        return nomPlanta;
    }

    public void setNomPlanta(String aNomPlanta) {
        nomPlanta = aNomPlanta;
    }

    @Column(name = "vigencia")
    public int getVigencia() {
        return vigencia;
    }

    public void setVigencia(int aVigencia) {
        vigencia = aVigencia;
    }

}

