package cl.cbb.gdeh.config;

import cl.cbb.gdeh.user.client.APA;
import cl.cbb.gdeh.user.client.APARSP;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class UserClient extends WebServiceGatewaySupport {

    public APARSP getResponse(String email,String password) {
        APA usuario = new APA();
        usuario.setPUser(email);
        usuario.setPPassword(password);
        usuario.setPAplica("25");
        usuario.setPDominio("11");
        return (APARSP) getWebServiceTemplate().marshalSendAndReceive(usuario);
    }
}