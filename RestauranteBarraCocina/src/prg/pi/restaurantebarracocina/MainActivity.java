package prg.pi.restaurantebarracocina;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import prg.pi.restaurantebarracocina.restaurante.MesaDestino;
import prg.pi.restaurantebarracocina.restaurante.Pedido;
import prg.pi.restaurantebarracocina.restaurante.Mesa;
import prg.pi.restaurantebarracocina.restaurante.Producto;
import prg.pi.restaurantebarracocina.servidor.Servidor;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.support.v4.app.FragmentActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;

public class MainActivity extends FragmentActivity implements
		OnGesturePerformedListener {
	private Button listo;
	public FragmentComanda fragmentComanda1, fragmentComanda2,
			fragmentComanda3, fragmentComanda4;
	private FragmentComanda fragments[];
	private ArrayList<MesaDestino> mesasDestino;
	private int contador;
	private Servidor servidor;
	private GestureLibrary libreria;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mesasDestino = new ArrayList<MesaDestino>();
		contador = 0;
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		libreria = GestureLibraries.fromRawResource(this, R.raw.gestures);
		if (!libreria.load()) {
			finish();
		}
		GestureOverlayView gesturesView = (GestureOverlayView) findViewById(R.id.gestures);
		gesturesView.addOnGesturePerformedListener(this);
		gesturesView.setGestureVisible(false);
		fragmentComanda1 = (FragmentComanda) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentlista1);
		fragmentComanda2 = (FragmentComanda) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentlista2);
		fragmentComanda3 = (FragmentComanda) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentlista3);
		fragmentComanda4 = (FragmentComanda) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentlista4);

		listo = (Button) findViewById(R.id.listo);
		listo.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				for(FragmentComanda fragment : fragments)
					fragment.setSeleccionado(-1);
			}
		});
		servidor = new Servidor(MainActivity.this);
		fragments = new FragmentComanda[] { fragmentComanda1, fragmentComanda2,
				fragmentComanda3, fragmentComanda4 };
	}

	public MesaDestino[] getMesasDsestino() {
		return mesasDestino.toArray(new MesaDestino[0]);
	}

	public void addMesaDestino(MesaDestino mesa) {
		mesasDestino.add(mesa);
		reorganizarMesas();
	}

	private void reorganizarMesas() {
		int tope;
		if (mesasDestino.size() > contador + 4) {
			tope = 4;
		} else {
			tope = mesasDestino.size()-contador;
		}
		for (int contadorMesa = 0; contadorMesa < tope; contadorMesa++) {
			fragments[contadorMesa].iniciarMesa(mesasDestino.get(contadorMesa+contador));
		}
	}

	public void onGesturePerformed(GestureOverlayView ov, Gesture gesture) {
		ArrayList<Prediction> predictions = libreria.recognize(gesture);
		if (predictions.size() > 0) {
			String comando = predictions.get(0).name;
			Log.d("prediccion", comando);
			if (comando.equals("atras") && mesasDestino.size() > contador+4) {
				contador = contador+4;
				reiniciarMesas();
				reorganizarMesas();
			} else if (comando.equals("adelante") && contador > 0) {
				contador = contador-4;
				reiniciarMesas();
				reorganizarMesas();
			}

		}
	}
	public void reiniciarMesas() {
		for(FragmentComanda fragment : fragments)
			fragment.reiniciarMesa();
	}

}
