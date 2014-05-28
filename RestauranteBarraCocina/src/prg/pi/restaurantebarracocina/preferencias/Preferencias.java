package prg.pi.restaurantebarracocina.preferencias;

import prg.pi.restaurantebarracocina.R;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class Preferencias extends PreferenceFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferencias);
	}
}
