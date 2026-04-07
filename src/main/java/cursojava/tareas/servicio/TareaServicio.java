package cursojava.tareas.servicio;

import cursojava.tareas.modelo.Tarea;
import cursojava.tareas.repositorio.TareaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //para que esta clase sea un componente de la fabrica de spring

//implementamos los metodos de la interface de Tarea Servicio, donde tenemos los metodos de las operaciones
//que queremos hacer como listar tareas, buscar, guardar y eliminar
public class TareaServicio implements ITareaServicio {

    @Autowired
    private TareaRepositorio tareaRepositorio; //cuando se ocupe el componente de repositorio se podra usar

    @Override
    public List<Tarea> listarTareas() {
        return tareaRepositorio.findAll(); //buscamos las tareas registradas en la BD
    }

    @Override
    public Tarea buscarTareaPorId(Integer idTarea) {
       Tarea tarea = tareaRepositorio.findById(idTarea).orElse(null); //en caso de que no se encuentre se agrega un valor de nulo
        return tarea;
    }

    @Override
    public void guardarTarea(Tarea tarea) {
        //guardamos el objeto tarea llamando al metodo SAVE que se implementa de HIBERNATE
        tareaRepositorio.save(tarea); //si  el valor de ID de la tarea es igual a nulo se inserta, si es diferente(ya existe) se actualizara

    }

    @Override
    public void eliminarTarea(Tarea tarea) {
        //metodo de eliminar tarea que se implementa de hibernate
        tareaRepositorio.delete(tarea);

    }
}
