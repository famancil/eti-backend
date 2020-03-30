//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.3.0 
// Visite <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2019.10.24 a las 01:58:44 PM CLST 
//


package cl.cbb.gdeh.user.client;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="ROW" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="USR_LOGIN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="USR_DISPLAYNAME" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="USR_EMAIL" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ROL_CODIGO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="ROL_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="MOD_CODIGO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="MOD_NOMBRE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="OPC_CODIGO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="OPC_NOMBRE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="VALOR_DOM" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX1" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX3" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX4" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX5" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX6" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX7" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX8" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX9" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="AUX10" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
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
    "row"
})
@XmlRootElement(name = "APA_RSP")
public class APARSP {

    @XmlElement(name = "ROW")
    protected List<APARSP.ROW> row;

    /**
     * Gets the value of the row property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the row property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getROW().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link APARSP.ROW }
     * 
     * 
     */
    public List<APARSP.ROW> getROW() {
        if (row == null) {
            row = new ArrayList<APARSP.ROW>();
        }
        return this.row;
    }


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
     *         &lt;element name="USR_LOGIN" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="USR_DISPLAYNAME" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="USR_EMAIL" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ROL_CODIGO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="ROL_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="MOD_CODIGO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="MOD_NOMBRE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="OPC_CODIGO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="OPC_NOMBRE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="VALOR_DOM" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX1" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX2" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX3" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX4" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX5" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX6" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX7" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX8" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX9" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="AUX10" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
        "usrlogin",
        "usrdisplayname",
        "usremail",
        "rolcodigo",
        "rolname",
        "modcodigo",
        "modnombre",
        "opccodigo",
        "opcnombre",
        "valordom",
        "aux1",
        "aux2",
        "aux3",
        "aux4",
        "aux5",
        "aux6",
        "aux7",
        "aux8",
        "aux9",
        "aux10"
    })
    public static class ROW {

        @XmlElement(name = "USR_LOGIN", required = true)
        protected String usrlogin;
        @XmlElement(name = "USR_DISPLAYNAME", required = true)
        protected String usrdisplayname;
        @XmlElement(name = "USR_EMAIL", required = true)
        protected String usremail;
        @XmlElement(name = "ROL_CODIGO", required = true)
        protected String rolcodigo;
        @XmlElement(name = "ROL_NAME", required = true)
        protected String rolname;
        @XmlElement(name = "MOD_CODIGO", required = true)
        protected String modcodigo;
        @XmlElement(name = "MOD_NOMBRE", required = true)
        protected String modnombre;
        @XmlElement(name = "OPC_CODIGO", required = true)
        protected String opccodigo;
        @XmlElement(name = "OPC_NOMBRE", required = true)
        protected String opcnombre;
        @XmlElement(name = "VALOR_DOM", required = true)
        protected String valordom;
        @XmlElement(name = "AUX1", required = true)
        protected String aux1;
        @XmlElement(name = "AUX2", required = true)
        protected String aux2;
        @XmlElement(name = "AUX3", required = true)
        protected String aux3;
        @XmlElement(name = "AUX4", required = true)
        protected String aux4;
        @XmlElement(name = "AUX5", required = true)
        protected String aux5;
        @XmlElement(name = "AUX6", required = true)
        protected String aux6;
        @XmlElement(name = "AUX7", required = true)
        protected String aux7;
        @XmlElement(name = "AUX8", required = true)
        protected String aux8;
        @XmlElement(name = "AUX9", required = true)
        protected String aux9;
        @XmlElement(name = "AUX10", required = true)
        protected String aux10;

        /**
         * Obtiene el valor de la propiedad usrlogin.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUSRLOGIN() {
            return usrlogin;
        }

        /**
         * Define el valor de la propiedad usrlogin.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUSRLOGIN(String value) {
            this.usrlogin = value;
        }

        /**
         * Obtiene el valor de la propiedad usrdisplayname.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUSRDISPLAYNAME() {
            return usrdisplayname;
        }

        /**
         * Define el valor de la propiedad usrdisplayname.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUSRDISPLAYNAME(String value) {
            this.usrdisplayname = value;
        }

        /**
         * Obtiene el valor de la propiedad usremail.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getUSREMAIL() {
            return usremail;
        }

        /**
         * Define el valor de la propiedad usremail.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setUSREMAIL(String value) {
            this.usremail = value;
        }

        /**
         * Obtiene el valor de la propiedad rolcodigo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getROLCODIGO() {
            return rolcodigo;
        }

        /**
         * Define el valor de la propiedad rolcodigo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setROLCODIGO(String value) {
            this.rolcodigo = value;
        }

        /**
         * Obtiene el valor de la propiedad rolname.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getROLNAME() {
            return rolname;
        }

        /**
         * Define el valor de la propiedad rolname.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setROLNAME(String value) {
            this.rolname = value;
        }

        /**
         * Obtiene el valor de la propiedad modcodigo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMODCODIGO() {
            return modcodigo;
        }

        /**
         * Define el valor de la propiedad modcodigo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMODCODIGO(String value) {
            this.modcodigo = value;
        }

        /**
         * Obtiene el valor de la propiedad modnombre.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMODNOMBRE() {
            return modnombre;
        }

        /**
         * Define el valor de la propiedad modnombre.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMODNOMBRE(String value) {
            this.modnombre = value;
        }

        /**
         * Obtiene el valor de la propiedad opccodigo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOPCCODIGO() {
            return opccodigo;
        }

        /**
         * Define el valor de la propiedad opccodigo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOPCCODIGO(String value) {
            this.opccodigo = value;
        }

        /**
         * Obtiene el valor de la propiedad opcnombre.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOPCNOMBRE() {
            return opcnombre;
        }

        /**
         * Define el valor de la propiedad opcnombre.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOPCNOMBRE(String value) {
            this.opcnombre = value;
        }

        /**
         * Obtiene el valor de la propiedad valordom.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVALORDOM() {
            return valordom;
        }

        /**
         * Define el valor de la propiedad valordom.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVALORDOM(String value) {
            this.valordom = value;
        }

        /**
         * Obtiene el valor de la propiedad aux1.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX1() {
            return aux1;
        }

        /**
         * Define el valor de la propiedad aux1.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX1(String value) {
            this.aux1 = value;
        }

        /**
         * Obtiene el valor de la propiedad aux2.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX2() {
            return aux2;
        }

        /**
         * Define el valor de la propiedad aux2.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX2(String value) {
            this.aux2 = value;
        }

        /**
         * Obtiene el valor de la propiedad aux3.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX3() {
            return aux3;
        }

        /**
         * Define el valor de la propiedad aux3.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX3(String value) {
            this.aux3 = value;
        }

        /**
         * Obtiene el valor de la propiedad aux4.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX4() {
            return aux4;
        }

        /**
         * Define el valor de la propiedad aux4.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX4(String value) {
            this.aux4 = value;
        }

        /**
         * Obtiene el valor de la propiedad aux5.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX5() {
            return aux5;
        }

        /**
         * Define el valor de la propiedad aux5.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX5(String value) {
            this.aux5 = value;
        }

        /**
         * Obtiene el valor de la propiedad aux6.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX6() {
            return aux6;
        }

        /**
         * Define el valor de la propiedad aux6.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX6(String value) {
            this.aux6 = value;
        }

        /**
         * Obtiene el valor de la propiedad aux7.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX7() {
            return aux7;
        }

        /**
         * Define el valor de la propiedad aux7.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX7(String value) {
            this.aux7 = value;
        }

        /**
         * Obtiene el valor de la propiedad aux8.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX8() {
            return aux8;
        }

        /**
         * Define el valor de la propiedad aux8.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX8(String value) {
            this.aux8 = value;
        }

        /**
         * Obtiene el valor de la propiedad aux9.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX9() {
            return aux9;
        }

        /**
         * Define el valor de la propiedad aux9.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX9(String value) {
            this.aux9 = value;
        }

        /**
         * Obtiene el valor de la propiedad aux10.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAUX10() {
            return aux10;
        }

        /**
         * Define el valor de la propiedad aux10.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAUX10(String value) {
            this.aux10 = value;
        }

    }

}
