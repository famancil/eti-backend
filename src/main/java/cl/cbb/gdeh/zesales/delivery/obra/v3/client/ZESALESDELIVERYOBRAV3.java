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
 *         &lt;element name="PKUNNR" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
 *         &lt;element name="PVBELN" type="{http://www.w3.org/2001/XMLSchema}integer"/&gt;
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
    "pkunnr",
    "pvbeln"
})
@XmlRootElement(name = "ZESALES_DELIVERY_OBRA_V3", namespace = "http://www.grupocbb.cl/sap/rfc/zesales_delivery_obra_v3")
public class ZESALESDELIVERYOBRAV3 {

    @XmlElement(name = "PKUNNR", required = true)
    protected String pkunnr;
    @XmlElement(name = "PVBELN", required = true)
    protected String pvbeln;

    /**
     * Obtiene el valor de la propiedad pkunnr.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public String getPKUNNR() {
        return pkunnr;
    }

    /**
     * Define el valor de la propiedad pkunnr.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPKUNNR(String value) {
        this.pkunnr = value;
    }

    /**
     * Obtiene el valor de la propiedad pvbeln.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public String getPVBELN() {
        return pvbeln;
    }

    /**
     * Define el valor de la propiedad pvbeln.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPVBELN(String value) {
        this.pvbeln = value;
    }

}
