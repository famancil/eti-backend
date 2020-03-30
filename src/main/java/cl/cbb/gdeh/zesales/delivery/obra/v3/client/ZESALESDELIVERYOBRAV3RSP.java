//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.0 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2019.10.12 a las 10:35:40 PM CLST 
//


package cl.cbb.gdeh.zesales.delivery.obra.v3.client;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PDESCRIP_OBRA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PCLIENTE_FINAL" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="PDESCRIP_CLTEFIN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DIRECCION" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DIRECCION_OBRA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PREGION" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="FINIOBRA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="FFINOBRA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="NEJECVEN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TELF1" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="RETORNO1" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RETORNO2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "pdescripobra",
    "pclientefinal",
    "pdescripcltefin",
    "direccion",
    "direccionobra",
    "pregion",
    "finiobra",
    "ffinobra",
    "nejecven",
    "telf1",
    "retorno1",
    "retorno2"
})
@XmlRootElement(name = "ZESALES_DELIVERY_OBRA_V3_RSP")
public class ZESALESDELIVERYOBRAV3RSP {

    @XmlElement(name = "PDESCRIP_OBRA", required = true)
    protected String pdescripobra;
    @XmlElement(name = "PCLIENTE_FINAL", required = true)
    protected BigInteger pclientefinal;
    @XmlElement(name = "PDESCRIP_CLTEFIN", required = true)
    protected String pdescripcltefin;
    @XmlElement(name = "DIRECCION", required = true)
    protected String direccion;
    @XmlElement(name = "DIRECCION_OBRA", required = true)
    protected String direccionobra;
    @XmlElement(name = "PREGION", required = true)
    protected String pregion;
    @XmlElement(name = "FINIOBRA", required = true)
    protected String finiobra;
    @XmlElement(name = "FFINOBRA", required = true)
    protected String ffinobra;
    @XmlElement(name = "NEJECVEN", required = true)
    protected String nejecven;
    @XmlElement(name = "TELF1", required = true)
    protected BigInteger telf1;
    @XmlElement(name = "RETORNO1", required = true)
    protected String retorno1;
    @XmlElement(name = "RETORNO2", required = true)
    protected String retorno2;

    /**
     * Obtiene el valor de la propiedad pdescripobra.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDESCRIPOBRA() {
        return pdescripobra;
    }

    /**
     * Define el valor de la propiedad pdescripobra.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDESCRIPOBRA(String value) {
        this.pdescripobra = value;
    }

    /**
     * Obtiene el valor de la propiedad pclientefinal.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPCLIENTEFINAL() {
        return pclientefinal;
    }

    /**
     * Define el valor de la propiedad pclientefinal.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPCLIENTEFINAL(BigInteger value) {
        this.pclientefinal = value;
    }

    /**
     * Obtiene el valor de la propiedad pdescripcltefin.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDESCRIPCLTEFIN() {
        return pdescripcltefin;
    }

    /**
     * Define el valor de la propiedad pdescripcltefin.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDESCRIPCLTEFIN(String value) {
        this.pdescripcltefin = value;
    }

    /**
     * Obtiene el valor de la propiedad direccion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDIRECCION() {
        return direccion;
    }

    /**
     * Define el valor de la propiedad direccion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDIRECCION(String value) {
        this.direccion = value;
    }

    /**
     * Obtiene el valor de la propiedad direccionobra.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDIRECCIONOBRA() {
        return direccionobra;
    }

    /**
     * Define el valor de la propiedad direccionobra.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDIRECCIONOBRA(String value) {
        this.direccionobra = value;
    }

    /**
     * Obtiene el valor de la propiedad pregion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPREGION() {
        return pregion;
    }

    /**
     * Define el valor de la propiedad pregion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPREGION(String value) {
        this.pregion = value;
    }

    /**
     * Obtiene el valor de la propiedad finiobra.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFINIOBRA() {
        return finiobra;
    }

    /**
     * Define el valor de la propiedad finiobra.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFINIOBRA(String value) {
        this.finiobra = value;
    }

    /**
     * Obtiene el valor de la propiedad ffinobra.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFFINOBRA() {
        return ffinobra;
    }

    /**
     * Define el valor de la propiedad ffinobra.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFFINOBRA(String value) {
        this.ffinobra = value;
    }

    /**
     * Obtiene el valor de la propiedad nejecven.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNEJECVEN() {
        return nejecven;
    }

    /**
     * Define el valor de la propiedad nejecven.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNEJECVEN(String value) {
        this.nejecven = value;
    }

    /**
     * Obtiene el valor de la propiedad telf1.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTELF1() {
        return telf1;
    }

    /**
     * Define el valor de la propiedad telf1.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTELF1(BigInteger value) {
        this.telf1 = value;
    }

    /**
     * Obtiene el valor de la propiedad retorno1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRETORNO1() {
        return retorno1;
    }

    /**
     * Define el valor de la propiedad retorno1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRETORNO1(String value) {
        this.retorno1 = value;
    }

    /**
     * Obtiene el valor de la propiedad retorno2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRETORNO2() {
        return retorno2;
    }

    /**
     * Define el valor de la propiedad retorno2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRETORNO2(String value) {
        this.retorno2 = value;
    }

}
