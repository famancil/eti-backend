package cl.cbb.gdeh.services;

import cl.cbb.gdeh.repositories.ParametroRepository;
import cl.cbb.gdeh.entities.Parametro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ParametroService {

    /**
     * Repositorio para el manejo CRUD de la entidad {@link Parametro}
     */
    @Autowired
    private ParametroRepository parametroRepository;

    /**
     * Obtiene el {@link Parametro} segun el id que se pase como parametro.
     */

    public Parametro getParametroById(Long idParametro){
        Optional<Parametro> parametro = parametroRepository.findById(idParametro);
        if(parametro!=null){
            return parametro.get();
        }
        else return null;

    }

    /**
     * Obtiene el {@link Parametro} segun el id que se pase como parametro.
     */

    public List<Parametro> getAllParametro(){
        List<Parametro> parametros = parametroRepository.findByOrderByIdAsc();
        if(parametros!=null){
            return parametros;
        }
        else return null;

    }

    /**
     * Obtiene una lista de los {@link Parametro} segun la categoria que se pase como parametro.
     */

    public List<Parametro> getAllParametroByCategory(String categoria){
        List<Parametro> parametros = parametroRepository.findByCategoria(categoria);
        //List<Parametro> parametros = null;
        if(parametros!=null){
            return parametros;
        }
        else return null;

    }

    /**
     * Obtiene una lista de los {@link Parametro} segun la categoria y parametro que se pase como parametro.
     */

    public List<Parametro> getAllParametroByCategoryAndParams(String categoria,String parametro){
        List<Parametro> parametros = parametroRepository.findByCategoriaAndParametro(categoria,parametro);
        //List<Parametro> parametros = null;
        if(parametros!=null){
            return parametros;
        }
        else return null;

    }

    /**
     * Obtiene una lista de los {@link Parametro} con todas las categorias excepcto de excepcion.
     */
    public List<Parametro> getAllParametrosByCategoryExceptExcepcion(){
        List<Parametro> parametros = parametroRepository.findAllCategoriasExceptExcepcion();
        //List<Parametro> parametros = null;
        if(parametros!=null){
            return parametros;
        }
        else return null;

    }

    /**
     * Obtiene una lista de los diferentes códigos de centro de {@link Parametro}.
     */
    public List<String> getAllCodCentro(){
        List<String> centros = parametroRepository.findAllCodCentro();
        if(centros!=null){
            return centros;
        }
        else return null;

    }

    /**
     * Obtiene una lista de los diferentes códigos de planta y de centro de {@link Parametro}.
     */
    public List<Object> getAllCodPlantaByCodCentro(String codCentro){
        List<Object> centros = parametroRepository.findAllCodPlantaByCodCentro(codCentro);
        if(centros!=null){
            return centros;
        }
        else return null;

    }

    /**
     * Obtiene todos los laboratorios de {@link Parametro}.
     */

    public List<Parametro> getAllLaboratorios(){
        List<Parametro> parametros = parametroRepository.getAllByValor("Laboratorio");
        if(parametros!=null){
            return parametros;
        }
        else return null;

    }

    /**
     * Obtiene todos los tipos de aditivos de {@link Parametro}.
     */

    public List<Parametro> getAllAditivos(){
        List<Parametro> parametros = parametroRepository.getAllByValor("Aditivo");
        if(parametros!=null){
            return parametros;
        }
        else return null;

    }

    /**
     * Obtiene un {@link Parametro} con un valor de parametro especifico.
     */
    public Parametro findByParametro(String parametro_valor){
        Parametro parametro = parametroRepository.findByParametro(parametro_valor).get();
        return parametro;

    }

    /**
     * Obtiene un {@link Parametro} con el parametro y cod_centro especifico.
     */
    public Parametro findParametroByParametroAndCodCentro(String parametro, String cod_planta){
        Parametro excepcion = parametroRepository.findExcepcionByParametroAndCodPlanta(parametro,cod_planta);
        if(excepcion != null)
            return excepcion;
        else {
            Parametro global = parametroRepository.findGlobalByParametro(parametro);
            return global;
        }

    }

    /**
     * Obtiene un {@link Parametro} con el parametro y cod_centro especifico.
     */
    public Parametro findURListen(){
        Parametro listen = parametroRepository.findURListen();
        if(listen != null)
            return listen;
        else return null;

    }

    /**
     * Obtiene un {@link Parametro} con el valor del parametro.
     */
    public String findDefaultValorByParametro(String parametroABuscar){
        return parametroRepository.findDefaultValorByParametro(parametroABuscar);
    }

    /**
     * Obtiene un {@link Parametro} con el valor del parametro y cod_planta especifico.
     */
    public String findDefaultValorByParametroAndPlanta(String parametroABuscar,
                                              String cod_planta){
        String valor = parametroRepository.findDefaultValorByParametroAndCodPlanta(parametroABuscar,cod_planta);
        if(valor==null)
            valor = parametroRepository.findDefaultValorByParametro(parametroABuscar);
        return valor;
    }

    /**
     * Guarda un {@link Parametro} nuevo en la base de datos.
     */
    public void saveParametro(Parametro parametro){

        // si cod_centro es nulo se setea como "*"
        if ( parametro.getCod_centro() == null ) {
            parametro.setCod_centro("*");
        }

        // si cod_planta es nulo se setea como "*"
        if ( parametro.getCod_planta() == null ) {
            parametro.setCod_planta("*");
        }

        // activo se setea por defecto con 1
        if ( parametro.getActivo() == null ) {
            parametro.setActivo(true);
        }

        // activo se setea por defecto con 1
        if (parametro.getCod_centro().equals("*") && parametro.getCod_planta().equals("*") ) {
            parametro.setIdParametroRef("*");
        }

        parametroRepository.save(parametro);
    }

    /**
     * Se hace un borrado logico de {@link Parametro} cambiando el atributo activo de true a false.
     */
    public void deleteLogicParametro(Long id){

        Parametro parametroFalse = parametroRepository.findById(id).get();

        parametroFalse.setActivo(false);
        parametroRepository.save(parametroFalse);

    }
}
