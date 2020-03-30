package cl.cbb.gdeh.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String rol_codigo;

    private String rol_name;

    private String mod_codigo;

    private String mod_nombre;

    private String opc_codigo;

    private String opc_nombre;

    private String valor_dom;

    private Usuario usuario;

    public Rol(){
        super();
    }

    public Rol(String rol_codigo, String rol_name, String mod_codigo, String mod_nombre, String opc_codigo,
               String opc_nombre, String valor_dom) {
        super();
        this.rol_codigo = rol_codigo;
        this.rol_name = rol_name;
        this.mod_codigo = mod_codigo;
        this.mod_nombre = mod_nombre;
        this.opc_codigo = opc_codigo;
        this.opc_nombre = opc_nombre;
        this.valor_dom = valor_dom;
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

    @Column(name = "rol_codigo")
    public String getRol_codigo() {
        return rol_codigo;
    }

    public void setRol_codigo(String rol_codigo) {
        this.rol_codigo = rol_codigo;
    }

    @Column(name = "rol_name")
    public String getRol_name() {
        return rol_name;
    }

    public void setRol_name(String rol_name) {
        this.rol_name = rol_name;
    }

    @Column(name = "mod_codigo")
    public String getMod_codigo() {
        return mod_codigo;
    }

    public void setMod_codigo(String mod_codigo) {
        this.mod_codigo = mod_codigo;
    }

    @Column(name = "mod_nombre")
    public String getMod_nombre() {
        return mod_nombre;
    }

    public void setMod_nombre(String mod_nombre) {
        this.mod_nombre = mod_nombre;
    }

    @Column(name = "opc_codigo")
    public String getOpc_codigo() {
        return opc_codigo;
    }

    public void setOpc_codigo(String opc_codigo) {
        this.opc_codigo = opc_codigo;
    }

    @Column(name = "opc_nombre")
    public String getOpc_nombre() {
        return opc_nombre;
    }

    public void setOpc_nombre(String opc_nombre) {
        this.opc_nombre = opc_nombre;
    }

    @Column(name = "valor_dom")
    public String getValor_dom() {
        return valor_dom;
    }

    public void setValor_dom(String valor_dom) {
        this.valor_dom = valor_dom;
    }

    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
