package cl.cbb.gdeh.controllers;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Emisor {

    private final List<SseEmitter> sseEmitters = Collections.synchronizedList(new ArrayList<>());
    private static Emisor instancia;
    public static Emisor getInstance() {
        if (instancia == null) {
            instancia = new Emisor();
        }
        return instancia;
    }
    public List<SseEmitter> getSseEmitters(){
        return this.sseEmitters;
    }
}
