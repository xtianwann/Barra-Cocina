package prg.pi.restaurantebarracocina.preferencias;

import android.app.Activity;
import android.os.Bundle;

/**
 * Actividad encargada de lanzar el fragment de preferencias de la aplicación.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class PreferenciasSet extends Activity {
	
 @Override
 protected void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferencias()).commit();
 }

}
