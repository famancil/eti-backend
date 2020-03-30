package cl.cbb.gdeh.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ClientAppConfig {
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("cl.cbb.gdeh.user.client");
        return marshaller;
    }

    @Bean
    public UserClient userClient(Jaxb2Marshaller marshaller) {
        UserClient uc = new UserClient();
        uc.setDefaultUri("http://10.249.88.3:8182/cxf/apa?wsdl");
        uc.setMarshaller(marshaller);
        uc.setUnmarshaller(marshaller);
        return uc;
    }
}