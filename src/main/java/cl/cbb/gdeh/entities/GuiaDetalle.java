package cl.cbb.gdeh.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "guia_detalle")
public class GuiaDetalle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String intrnl_line_num;

    private String cstmry_delv_qty;

    private String metric_delv_qty;

    private String delv_qty;

    private String qty_um;

    private String rm_slump;

    private String prod_descrip;

    private String prod_code;

    private Guia guia;

    private int lock_flag;

    public GuiaDetalle(){
        super();
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

    @Column(name = "intrnl_line_num")
    public String getIntrnl_line_num() {
        return intrnl_line_num;
    }

    public void setIntrnl_line_num(String intrnl_line_num) {
        this.intrnl_line_num = intrnl_line_num;
    }

    @Column(name = "cstmry_delv_qty")
    public String getCstmry_delv_qty() {
        return cstmry_delv_qty;
    }

    public void setCstmry_delv_qty(String cstmry_delv_qty) {
        this.cstmry_delv_qty = cstmry_delv_qty;
    }

    @Column(name = "metric_delv_qty")
    public String getMetric_delv_qty() {
        return metric_delv_qty;
    }

    public void setMetric_delv_qty(String metric_delv_qty) {
        this.metric_delv_qty = metric_delv_qty;
    }

    @Column(name = "delv_qty")
    public String getDelv_qty() {
        return delv_qty;
    }

    public void setDelv_qty(String delv_qty) {
        this.delv_qty = delv_qty;
    }

    @Column(name = "qty_um")
    public String getQty_um() {
        return qty_um;
    }

    public void setQty_um(String qty_um) {
        this.qty_um = qty_um;
    }

    @Column(name = "rm_slump")
    public String getRm_slump() {
        return rm_slump;
    }

    public void setRm_slump(String rm_slump) {
        this.rm_slump = rm_slump;
    }

    @Column(name = "prod_descrip")
    public String getProd_descrip() {
        return prod_descrip;
    }

    public void setProd_descrip(String prod_descrip) {
        this.prod_descrip = prod_descrip;
    }

    @Column(name = "prod_code")
    public String getProd_code() {
        return prod_code;
    }

    public void setProd_code(String prod_code) {
        this.prod_code = prod_code;
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

    @Column(name = "lock_flag")
    public int getLock_flag() {
        return lock_flag;
    }

    public void setLock_flag(int lock_flag) {
        this.lock_flag = lock_flag;
    }
}
