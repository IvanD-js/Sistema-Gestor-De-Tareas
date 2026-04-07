package cursojava.tareas.presentacion;

import cursojava.tareas.TareasApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class SistemasTareasFx extends Application {

//    public static void main(String[] args) { launch(args); }


    //importamos la fabrica de spring con un atributo
    private ConfigurableApplicationContext applicationContext;

    //metodo init
    @Override
    public void init() throws Exception {
        //iniciamos la fabrica de spring y arrancamos spring en segundo plano
        this.applicationContext =
                new SpringApplicationBuilder(TareasApplication.class).run();

    }
    //escenario de la aplicacion que permite mas ramas
    @Override
    public void start(Stage stage) throws Exception { //metodo que nos permite iniciar la app de JavaFX

        //recuperamos el recurso donde se encuentra el archivo de la interfaz
        //cargamos el archivo FXML con la interface desde la carpeta de templates
        FXMLLoader loader = new FXMLLoader(TareasApplication.class.getResource("/templates/index.fxml"));

        //fabrica de objetos de tipo controlador
        loader.setControllerFactory(applicationContext::getBean); //intregramos las tecnologias de JFX con SPRING

        //objeto de tipo escena
        Scene escena = new Scene(loader.load());
        stage.setScene(escena); //establecemos la escena que queremos mostrar
        stage.show();

//        stage.setTitle("Sistemas de Tareas con JavaFX");
//        stage.show();
    }

    //metodo para detener la aplicacion y cerrar la fabrica de spring
    @Override
    public void stop(){
        applicationContext.close();
        //cerramos la app de JavaFX
        Platform.exit();

    }
}
