package prg.pi.restaurantebarracocina.preferencias;

import android.app.Activity;
import android.os.Bundle;

public class PreferenciasSet extends Activity {
	
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  // TODO Auto-generated method stub
  super.onCreate(savedInstanceState);
  
  getFragmentManager().beginTransaction().replace(android.R.id.content,new Preferencias()).commit();
 }

}
