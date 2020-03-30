package cl.cbb.gdeh.controllers;

import cl.cbb.gdeh.config.ClientAppConfig;
import cl.cbb.gdeh.config.UserClient;
import cl.cbb.gdeh.entities.Rol;
import cl.cbb.gdeh.entities.Usuario;
import cl.cbb.gdeh.message.request.LoginForm;
import cl.cbb.gdeh.message.response.JwtResponse;
import cl.cbb.gdeh.repositories.RolRepository;
import cl.cbb.gdeh.repositories.UsuarioRepository;
import cl.cbb.gdeh.security.jwt.JwtProvider;
import cl.cbb.gdeh.user.client.APARSP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * Logger del controlador de Auth.
     */
    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Servicio que provee el JWT Secret Key y JWT LifeTime.
     */
    @Autowired
    JwtProvider jwtProvider;

    /**
     * Repositorio que provee el manejo de usuarios.
     */
    @Autowired
    UsuarioRepository usuarioRepository;

    /**
     * Obtener las credenciales de Usuario y el Token de acceso mediante el inicio de sesion de este
     * @param loginRequest
     * @return Credenciales de usuario, token de acceso y tipo de autenticacion (header).
     */
    @RequestMapping(value ="/signin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        String jwt = null,name=null;
        List<Rol> roles = new ArrayList<>();
        try{

            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.register(ClientAppConfig.class);
            ctx.refresh();
            UserClient usc = ctx.getBean(UserClient.class);

            APARSP response = usc.getResponse(loginRequest.getEmail(),loginRequest.getPassword());

            if(response.getROW().size() != 0) {
                jwt = jwtProvider.generateJwtToken(loginRequest.getEmail());
                name = response.getROW().get(0).getUSRDISPLAYNAME();
                for(int i=0;i<response.getROW().size();i++)
                    roles.add(new Rol(response.getROW().get(i).getROLCODIGO(),response.getROW().get(i).getROLNAME(),
                            response.getROW().get(i).getMODCODIGO(),response.getROW().get(i).getMODNOMBRE(),
                            response.getROW().get(i).getOPCCODIGO(),response.getROW().get(i).getOPCNOMBRE(),
                            response.getROW().get(i).getVALORDOM()));
                logger.info("El usuario Test ha iniciado sesión con los datos email: " + loginRequest.getEmail()
                        +", password: "+loginRequest.getPassword()+ " con exito");
            }
            else {
                logger.info("El usuario Test ha intentado realizar inicio de sesión con los datos email: " + loginRequest.getEmail()
                        +", password: "+loginRequest.getPassword()+ " sin exito");
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

        } catch(Exception e){
            logger.error("Error en Inicio Sesión: ",e);
        }

        /*Emisor prueba = Emisor.getInstance();
        List<SseEmitter> sseEmitters = prueba.getSseEmitters();
        synchronized (sseEmitters) {
            for (SseEmitter sseEmitter : sseEmitters) {
                try {
                    sseEmitter.send("Hola", MediaType.TEXT_PLAIN);
                    sseEmitter.complete();
                } catch (Exception e) {
                    //???
                }
            }
        }*/

        if(!usuarioRepository.existsByEmail(loginRequest.getEmail())){
            Usuario sesion = new Usuario(name,loginRequest.getEmail());
            usuarioRepository.save(sesion);
        }


        return ResponseEntity.ok(new JwtResponse(jwt, loginRequest.getEmail(), name, roles));
    }
}
