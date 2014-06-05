package prg.pi.restaurantebarracocina.preferencias;

import prg.pi.restaurantebarracocina.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;
/**
 * 
 * 
 * Fragment encargado de mostrar las preferencias de la aplicación.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
public class Preferencias extends PreferenceFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferencias);
	}
}
