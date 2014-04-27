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
import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
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
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	public FragmentComanda fragmentComanda;
	private Servidor servidor;
	private Button limpiar,cambiar,mas,menos,x;
	private Calculadora calculadora;

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
		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		fragmentComanda = (FragmentComanda) getSupportFragmentManager()
				.findFragmentById(R.id.fragmentlista);
		servidor = new Servidor(MainActivity.this);
		iniciarCalculadora();
	}
	private void iniciarCalculadora() {
		calculadora = new Calculadora(
				new int[] { R.id.c0, R.id.c1, R.id.c2, R.id.c3, R.id.c4,
						R.id.c5, R.id.c6, R.id.c7, R.id.c8, R.id.c9 }, R.id.ce,
				R.id.total);
		cambiar = (Button) findViewById(R.id.cambiar);
		cambiar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {


			}

		});
		mas = (Button) findViewById(R.id.mas);
		mas.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {

			}

		});
		menos = (Button) findViewById(R.id.menos);
		menos.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {

			}

		});
		x = (Button) findViewById(R.id.x);
		x.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {

			}

		});
		
	}
	public FragmentComanda getFragmentComanda() {
		return fragmentComanda;
	}
	public class Calculadora {
		Button cero, uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve,
				ce;
		public Button botones[] = { cero, uno, dos, tres, cuatro, cinco, seis,
				siete, ocho, nueve };
		public TextView total;

		public Calculadora(int botonesR[], int ceR, int totalR) {
			for (int contador = 0; contador < botones.length; contador++) {
				botones[contador] = (Button) findViewById(
						botonesR[contador]);
				botones[contador]
						.setOnClickListener(new AdapterView.OnClickListener() {
							public void onClick(View view) {
								if (total.getText().length() < 3) {
									Button botonPulsado = (Button) view;
									int sumando = Integer.parseInt(botonPulsado
											.getText() + "");
									sumar(sumando);
								}
							}
						});
			}
			ce = (Button) findViewById(ceR);
			ce.setOnClickListener(new AdapterView.OnClickListener() {
				public void onClick(View view) {
					total.setText(0 + "");
				}
			});
			total = (TextView) findViewById(totalR);
		}

		public void sumar(int sumando) {
			String totalSuma = total.getText() + "";
			int suma = Integer.parseInt(totalSuma);
			if (suma == 0) {
				totalSuma = sumando + "";
			} else {
				totalSuma = suma + "" + sumando + "";
			}
			total.setText(totalSuma);
		}
	}

}
