package cl.cbb.gdeh.config;

import cl.cbb.gdeh.zesales.delivery.obra.v3.client.ZESALESDELIVERYOBRAV3;
import cl.cbb.gdeh.zesales.delivery.obra.v3.client.ZESALESDELIVERYOBRAV3RSP;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigInteger;

public class DeliveryObraClient extends WebServiceGatewaySupport {

    public ZESALESDELIVERYOBRAV3RSP getResponse(String PKUNNR, String PVBELN){

        ZESALESDELIVERYOBRAV3 obra = new ZESALESDELIVERYOBRAV3();
        obra.setPKUNNR(PKUNNR);
        obra.setPVBELN(PVBELN);
        return (ZESALESDELIVERYOBRAV3RSP) getWebServiceTemplate().marshalSendAndReceive(obra);
    }
}
