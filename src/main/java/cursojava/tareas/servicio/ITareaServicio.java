package cursojava.tareas.servicio;

import cursojava.tareas.modelo.Tarea;

import java.util.List;

public interface ITareaServicio {

    //metodo para listar las tareas que se encuentran registardas
    public List<Tarea> listarTareas();

    //metodo para buscar tarea por ID
    public Tarea buscarTareaPorId(Integer idTarea);

    //metodo para gaurdar una tarea
    public void guardarTarea(Tarea tarea); //si es diferente de nulo se agrega, si no lo es se actualiza

    //metodo para eliminar una tarea
    public void eliminarTarea(Tarea tarea);

}
