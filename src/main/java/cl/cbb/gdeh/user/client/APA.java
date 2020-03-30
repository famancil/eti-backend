//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.0 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2019.10.24 a las 01:58:44 PM CLST 
//


package cl.cbb.gdeh.user.client;

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
 *         &lt;element name="p_user" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="p_password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="p_aplica" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="p_dominio" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
    "pUser",
    "pPassword",
    "pAplica",
    "pDominio"
})
@XmlRootElement(name = "APA", namespace = "http://www.grupocbb.cl/ws/apa")
public class APA {

    @XmlElement(name = "p_user", required = true)
    protected String pUser;
    @XmlElement(name = "p_password")
    protected String pPassword;
    @XmlElement(name = "p_aplica", required = true)
    protected String pAplica;
    @XmlElement(name = "p_dominio", required = true)
    protected String pDominio;

    /**
     * Obtiene el valor de la propiedad pUser.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPUser() {
        return pUser;
    }

    /**
     * Define el valor de la propiedad pUser.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPUser(String value) {
        this.pUser = value;
    }

    /**
     * Obtiene el valor de la propiedad pPassword.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPPassword() {
        return pPassword;
    }

    /**
     * Define el valor de la propiedad pPassword.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPPassword(String value) {
        this.pPassword = value;
    }

    /**
     * Obtiene el valor de la propiedad pAplica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPAplica() {
        return pAplica;
    }

    /**
     * Define el valor de la propiedad pAplica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPAplica(String value) {
        this.pAplica = value;
    }

    /**
     * Obtiene el valor de la propiedad pDominio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPDominio() {
        return pDominio;
    }

    /**
     * Define el valor de la propiedad pDominio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPDominio(String value) {
        this.pDominio = value;
    }

}
