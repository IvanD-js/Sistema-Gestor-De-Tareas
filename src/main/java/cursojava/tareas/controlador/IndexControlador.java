package cursojava.tareas.controlador;

import cursojava.tareas.modelo.Tarea;
import cursojava.tareas.servicio.TareaServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class IndexControlador implements Initializable {

    //atributos de al aplicacion

    //mandar mensajes en consola
    private static final Logger logger =
            LoggerFactory.getLogger(IndexControlador.class);

    //inyectamos el componente de bean de spring y nos da acceso al servicio de tareas
    @Autowired
    private TareaServicio tareaServicio;

    //atributos de la interfaz, tabla, id tarea, etc
    @FXML
    private TableView<Tarea> tareaTabla;

    @FXML
    private TableColumn<Tarea, Integer> idTareaColumna;

    @FXML
    private TableColumn<Tarea, String> nombreTareaColumna;

    @FXML
    private TableColumn<Tarea, String> responsableColumna;

    @FXML
    private TableColumn<Tarea, String> estatusColumna;

    //mostraremos una lista para mostar a los usuarios
    private final ObservableList<Tarea> tareaList =
            FXCollections.observableArrayList();

    //agregamos el mapeo de cada uno de los componentes de la interfaz

    @FXML
    private TextField nombreTareaTexto;

    @FXML
    private TextField responsableTexto;

    @FXML
    private TextField estatusTexto;

    //atributo interno para diferenciar el caso de agregar y modificar
    private Integer idTareaInterno;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //configuracion de la tabla para solo poder seleccionar UN REGISTRO cuando queramos eliminar o modificar
        tareaTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //metodo para configurar las columnas de la tabla
        configurarColumnas();

        //metodo para listar las tareas
        listarTareas();

    }

    //metodo para configurar las columnas, id tarea, tarea, responsable, estatus
    private void configurarColumnas() {

        //recuperamos los datos de la interfaz
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea")); //nombre del atributo de id tarea
        nombreTareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        responsableColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estatusColumna.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }

    private void listarTareas() {
        logger.info("Ejecutando listado de tareas");
        //limpiamos la lista de tareas
        tareaList.clear();
        tareaList.addAll(tareaServicio.listarTareas()); //mandamos a llamar al lista de las tareas
        tareaTabla.setItems(tareaList); //especificamos los elementos de la tabla de donde se encuentran los datos

    }

    //metodo para agregar tareas
    public void agregarTarea(){
        //validamos si la caja de texto, si es igual a nulo no podemos agregar la tarea
        if(nombreTareaTexto.getText().isEmpty()){
            mostrarMensaje("Error Validacion", "Debe proporcionar una tarea");
            nombreTareaTexto.requestFocus();
            return;
        }
        //si ya hay informacion procedemos a agregar una tarea
        else{
            var tarea = new Tarea();
            recolectarDatosFormulario(tarea);
            tarea.setIdTarea(null); //nos aseguramos de que una vez se agregue el valor del id pase a ser nulo
            //agregamos usando el servicio de spring
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("Informacion", "Tarea agregada");
            //metodo para limpiar el formualario una vez agregada la tarea
            limpiarFormulario();
            //listamos nuevamente las tareas para que se muestre en la tabla despues de agregar una tarea
            listarTareas();
        }
    }

    //metodo para seleccionar la tarea una vez se selecione el registro de la tabla
    public void cargarTareaFormulario(){

        //dentro de una variable veremos que registro se selecciono
        var tarea = tareaTabla.getSelectionModel().getSelectedItem(); //solo permite un registro con la seleccion simple

        //validamos si es diferente de nulo
        if(tarea != null){
           //atributo id tarea interno
            idTareaInterno = tarea.getIdTarea(); //obtenemos el id del objeto que haya seleccionado el usuario de la tabla
            //recuperamos los objetos de cada uno y el valor get para que se modifique el valor
            nombreTareaTexto.setText(tarea.getNombreTarea());
            responsableTexto.setText(tarea.getResponsable());
            estatusTexto.setText(tarea.getEstatus());
        }
    }

    //metodo para modificar la tarea
    public void modificarTarea(){
        //revisamos si es un caso de modificacion
        if (idTareaInterno == null){
            mostrarMensaje("Informacion", "Debes seleccionar una tarea");
            return;
        }
        //si el usuario ya proporciono un registro preguntamos SI ES VACIO regresamos un error
        if(nombreTareaTexto.getText().isEmpty()){
            mostrarMensaje("informacion", "Debe proporcionar una tarea");
            nombreTareaTexto.requestFocus(); //foco para que el usuario capture la informacion
            return;
        }

        //una vez pasamos esas validaciones creamos un objeto de tipo tarea para modificar los datos
        var tarea = new Tarea();
        recolectarDatosFormulario(tarea);
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Informacion", "Tarea Modificada");
        limpiarFormulario();
        listarTareas();
    }

    public void eliminarTarea(){
        //validamos que se haya seleccionado un registro para eliminar una tarea
        var tarea = tareaTabla.getSelectionModel().getSelectedItem(); //seleccionamos un registro

        //validamos si es diferente de nulo para hacer una accion, en este caso eliminar la tarea
        if(tarea != null){
            logger.info("Registro a eliminar: " + tarea.toString()); //especificamos que registro se va a eliminar
            //mandamos a llamar el metodo de eliminar la tarea
            tareaServicio.eliminarTarea(tarea);
            mostrarMensaje("Informacion", "ID de la tarea eliminada: " + tarea.getIdTarea());
            limpiarFormulario();
            listarTareas();
        }

        else{
            mostrarMensaje("Error", "No se ha seleccionado una tarea");
        }
    }


    //metodo para agregar la tarea despues de validarla
    private void recolectarDatosFormulario(Tarea tarea){

        //validamos si el id de tarea interno es diferente de nulo lo establecemos en el objeto de tarea
        if(idTareaInterno != null){
            tarea.setIdTarea(idTareaInterno); //si es diferente de nulo se hace una actualizacion y no una incersion
        }

        //recuperamos los datos que se van a agregar en el formulario
        tarea.setNombreTarea(nombreTareaTexto.getText());
        tarea.setResponsable(responsableTexto.getText());
        tarea.setEstatus(estatusTexto.getText());
    }


    public void limpiarFormulario(){
        //limpiamos los campos usando el metodo CLEAR en cada uno para que se limpien
        idTareaInterno = null;
        nombreTareaTexto.clear();
        responsableTexto.clear();
        estatusTexto.clear();
    }

    //metodo para mostrar mensajes
    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
