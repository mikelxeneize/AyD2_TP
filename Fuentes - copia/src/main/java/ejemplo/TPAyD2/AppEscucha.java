package ejemplo.TPAyD2;
import src.main.resources.backEnd.Nucleo;
import src.main.resources.controlador.ControladorMenuPrincipal;

/**
 * Hello world!
 *
 */
public class AppEscucha 
{
    public static void main( String[] args ) throws Exception {
		Nucleo.getInstance().iniciarNucleo();
        ControladorMenuPrincipal contr= new ControladorMenuPrincipal();
    }
}
