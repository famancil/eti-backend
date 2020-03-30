package cl.cbb.gdeh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class DeliveryObraAppConfig {

    @Bean
    public Jaxb2Marshaller marshaller2() {
        Jaxb2Marshaller marshaller2 = new Jaxb2Marshaller();
        marshaller2.setContextPath("cl.cbb.gdeh.zesales.delivery.obra.v3.client");
        return marshaller2;
    }

    @Bean
    public DeliveryObraClient deliveryObraClient(Jaxb2Marshaller marshaller) {
        DeliveryObraClient doc = new DeliveryObraClient();
        doc.setDefaultUri("http://172.33.0.163:8181/cxf/ZESALES_DELIVERY_OBRA_V3");
        //doc.setDefaultUri("http://10.249.88.3:8182/cxf/ZESALES_DELIVERY_OBRA_V3?wsdl");
        doc.setMarshaller(marshaller);
        doc.setUnmarshaller(marshaller);
        return doc;
    }
}
