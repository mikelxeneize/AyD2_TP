package ejemplo.TPAyD2;
import src.main.resources.backEnd.Nucleo;
import src.main.resources.controlador.ControladorMenuPrincipal;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        ControladorMenuPrincipal contr= new ControladorMenuPrincipal();
        Nucleo.getInstance().cargarConfiguracion();
    }
}
