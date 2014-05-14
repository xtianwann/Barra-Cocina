package prg.pi.restaurantebarracocina;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import prg.pi.restaurantebarracocina.FragmentHistorico.HistoricoListener;
import prg.pi.restaurantebarracocina.conexion.Cliente;
import prg.pi.restaurantebarracocina.restaurante.MesaDestino;
import prg.pi.restaurantebarracocina.restaurante.Pedido;
import prg.pi.restaurantebarracocina.restaurante.Mesa;
import prg.pi.restaurantebarracocina.restaurante.PedidoModificadoCamarero;
import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import prg.pi.restaurantebarracocina.restaurante.Producto;
import prg.pi.restaurantebarracocina.servidor.Servidor;
import prg.pi.restaurantebarracocina.xml.XMLPedidosListos;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends FragmentActivity implements HistoricoListener {
	private Servidor servidor;
	private Button limpiar, cambiar, mas, menos, enviar, deshacer, todo;
	private Calculadora calculadora;
	private ListView lista;
	private AdaptadorComanda adaptador;
	private ArrayList<PedidosEntrantesCB> pedidosEntrantes;
	private int seleccionado = -1;
	private ArrayList<PedidosEntrantesCB> listos;
	private NotificacionBarra notificacion;
	private DrawerLayout drawerLayout;
	private FragmentHistorico fragmentHistorico;

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
		fragmentHistorico = (FragmentHistorico) getSupportFragmentManager()
				.findFragmentById(R.id.historicosF);
		fragmentHistorico.setHistoricoListener(this);
		servidor = new Servidor(MainActivity.this);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		iniciarCalculadora();
		prepararListeners();
		// Pruebas en el trabajo
		// pedidosEntrantes.add(new PedidosEntrantesCB("Abajo", "Rincon", 1, 3,
		// new Producto(1, "Chocos", "Racion"), 0));
		// pedidosEntrantes.add(new PedidosEntrantesCB("Arriba", "Centro", 2, 5,
		// new Producto(2, "Huevas", "Tapa"), 0));
		// ///////////////////////////////////////////////////////////
	}

	private class AdaptadorComanda extends BaseAdapter {
		private LayoutInflater mInflater;

		public AdaptadorComanda(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return pedidosEntrantes.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			PedidoTexto pedido;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.comanda_lista, null);
				pedido = new PedidoTexto(convertView);
				convertView.setTag(pedido);
			} else {
				pedido = (PedidoTexto) convertView.getTag();
			}
			PedidosEntrantesCB pedidoEntrante = pedidosEntrantes.get(position);
			pedido.addPedido(pedidoEntrante);
			pedido.cambiarColor(position);
			return convertView;
		}

	}

	private void prepararListeners() {
		pedidosEntrantes = new ArrayList<PedidosEntrantesCB>();
		lista = (ListView) findViewById(R.id.lista);
		adaptador = new AdaptadorComanda(this);
		lista.setAdapter(adaptador);
		lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id) {
				if (seleccionado > -1) {
					if (fragmentHistorico.getHistoricos(pedidosEntrantes
							.get(seleccionado)) != null) {
						if (pedidosEntrantes.get(seleccionado).getListos() == fragmentHistorico
								.getHistoricos(
										pedidosEntrantes.get(seleccionado))
								.getListos())
							listos.remove(pedidosEntrantes.get(seleccionado));
					}
				}
				seleccionado = pos;
				adaptador.notifyDataSetChanged();
			}
		});
		listos = new ArrayList<PedidosEntrantesCB>();
		notificacion = new NotificacionBarra(this);
		enviar = (Button) findViewById(R.id.enviar);
		enviar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (listos.size() > 0) {
					enviarComanda();
				} else {
					notificacion.lanzarNotificacionSinModificar();
				}
			}

		});
		todo = (Button) findViewById(R.id.todo);
		todo.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				int total = Integer.parseInt(calculadora.total.getText() + "");
				if (seleccionado > -1) {
					pedidosEntrantes.get(seleccionado).setListos(
							pedidosEntrantes.get(seleccionado).getUnidades());
					addListo();
					adaptador.notifyDataSetChanged();
				}
			}
		});
	}

	private void iniciarCalculadora() {
		calculadora = new Calculadora(
				new int[] { R.id.c0, R.id.c1, R.id.c2, R.id.c3, R.id.c4,
						R.id.c5, R.id.c6, R.id.c7, R.id.c8, R.id.c9 }, R.id.ce,
				R.id.total);
		cambiar = (Button) findViewById(R.id.cambiar);
		cambiar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				int cambio = Integer.parseInt(calculadora.total.getText() + "");
				if (seleccionado > -1) {
					if (cambio <= pedidosEntrantes.get(seleccionado)
							.getUnidades()) {
						pedidosEntrantes.get(seleccionado).setListos(cambio);
						addListo();
						adaptador.notifyDataSetChanged();
					}
				}
				calculadora.total.setText("0");
			}

		});
		mas = (Button) findViewById(R.id.mas);
		mas.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					int suma = pedidosEntrantes.get(seleccionado).getListos() + 1;
					if (suma <= pedidosEntrantes.get(seleccionado)
							.getUnidades()) {
						pedidosEntrantes.get(seleccionado).setListos(suma);
						addListo();
						adaptador.notifyDataSetChanged();
					}
				}
			}

		});
		menos = (Button) findViewById(R.id.menos);
		menos.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					int resta = pedidosEntrantes.get(seleccionado).getListos() - 1;
					if (resta > -1) {
						if (fragmentHistorico.getHistoricos(pedidosEntrantes
								.get(seleccionado)) != null) {
							Log.d("Listos",
									fragmentHistorico.getHistoricos(
											pedidosEntrantes.get(seleccionado))
											.getListos()
											+ "");
							if (resta >= fragmentHistorico.getHistoricos(
									pedidosEntrantes.get(seleccionado))
									.getListos()) {
								pedidosEntrantes.get(seleccionado).setListos(
										resta);
								addListo();
								adaptador.notifyDataSetChanged();
							}
						} else {
							pedidosEntrantes.get(seleccionado).setListos(resta);
							addListo();
							adaptador.notifyDataSetChanged();
						}

					}
				}
			}

		});
	}

	public void addListo() {
		if (!isListo(seleccionado)) {
			listos.add(pedidosEntrantes.get(seleccionado));
		} else {
			if (pedidosEntrantes.get(seleccionado).getListos() < 1) {
				listos.remove(pedidosEntrantes.get(seleccionado));
			}
		}
	}

	public boolean isListo(int pedido) {
		boolean listo = false;
		for (PedidosEntrantesCB pedidoListo : listos) {
			if (pedidoListo.equals(pedidosEntrantes.get(pedido))) {
				listo = true;
				break;
			}
		}
		return listo;
	}

	public void terminarPedido() {
		fragmentHistorico.addPedidosHistoricos(listos
				.toArray(new PedidosEntrantesCB[0]));
		for (PedidosEntrantesCB pedidoTerminado : listos) {
			if (pedidoTerminado.isTerminado()) {
				pedidosEntrantes.remove(pedidoTerminado);
			}
		}
		adaptador.notifyDataSetChanged();
	}

	public boolean existenTerminados() {
		boolean terminado = false;
		for (PedidosEntrantesCB pedidoListo : listos) {
			if (pedidoListo.isTerminado()) {
				terminado = true;
				break;
			}
		}
		return terminado;
	}

	public void enviarComanda() {
		new Thread(new Runnable() {
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						XMLPedidosListos xmlEnviarComandaLista = new XMLPedidosListos(
								listos.toArray(new PedidosEntrantesCB[0]));
						Log.e("size", listos.size() + "");
						String mensaje = xmlEnviarComandaLista
								.xmlToString(xmlEnviarComandaLista.getDOM());
						Cliente c = new Cliente(mensaje);
						try {
							c.iniciar();
						} catch (NullPointerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						terminarPedido();
						listos.clear();
						seleccionado = -1;
						adaptador.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}

	public void addPedidos(PedidosEntrantesCB[] pedidosE) {
		boolean encontradoH;
		boolean encontradoE;
		PedidosEntrantesCB pedidoHEncontrado = null;
		for (PedidosEntrantesCB pedido : pedidosE) {
			encontradoH = false;
			for (PedidosEntrantesCB pedidoH : fragmentHistorico
					.dameHistoricos()) {
				if (pedido.getIdComanda() == pedidoH.getIdComanda()
						&& pedido.getProducto().getIdMenu() == pedidoH
								.getProducto().getIdMenu()) {
					pedidoH.setUnidades(pedidoH.getUnidades()
							+ pedido.getUnidades());
					encontradoH = true;
					pedidoHEncontrado = new PedidosEntrantesCB(
							pedidoH.getNombreSeccion(),
							pedidoH.getNombreMesa(), pedidoH.getIdComanda(),
							pedidoH.getUnidades(), pedidoH.getProducto(),
							pedidoH.getListos());
					fragmentHistorico.avisaAdaptador();
					break;
				}
			}
			if (!encontradoH) {
				encontradoE = false;
				for (PedidosEntrantesCB pedidoE : pedidosEntrantes) {
					if (pedido.getIdComanda() == pedidoE.getIdComanda()
							&& pedido.getProducto().getIdMenu() == pedidoE
									.getProducto().getIdMenu()) {
						pedidoE.setUnidades(pedidoE.getUnidades()
								+ pedido.getUnidades());
						encontradoE = true;
						break;
					}
				}
				if (!encontradoE)
					pedidosEntrantes.add(pedido);
			} else {
				encontradoE = false;
				for (PedidosEntrantesCB pedidoE : pedidosEntrantes) {
					if (pedido.getIdComanda() == pedidoE.getIdComanda()
							&& pedido.getProducto().getIdMenu() == pedidoE
									.getProducto().getIdMenu()) {
						pedidoE.setUnidades(pedidoE.getUnidades()
								+ pedido.getUnidades());
						encontradoE = true;
						break;
					}
				}
				if (!encontradoE)
					pedidosEntrantes.add(pedidoHEncontrado);
			}
		}
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
	}

	public class Calculadora {
		Button cero, uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve,
				ce;
		public Button botones[] = { cero, uno, dos, tres, cuatro, cinco, seis,
				siete, ocho, nueve };
		public TextView total;

		public Calculadora(int botonesR[], int ceR, int totalR) {
			for (int contador = 0; contador < botones.length; contador++) {
				botones[contador] = (Button) findViewById(botonesR[contador]);
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

	private class NotificacionBarra {
		private AlertDialog.Builder dialog;
		private MainActivity mainActivity;

		public NotificacionBarra(MainActivity mainActivity) {
			this.mainActivity = mainActivity;
		}

		private void lanzarNotificacionSinModificar() {
			dialog = new AlertDialog.Builder(mainActivity);
			dialog.setMessage("No se ha modificado ningun pedido.");
			dialog.setCancelable(false);
			dialog.setNeutralButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			dialog.show();
		}
	}

	public class PedidoTexto {
		TextView cantidadTexto;
		TextView productoTexto;
		TextView estadoTexto;
		PedidosEntrantesCB pedidoEntrante;

		public PedidoTexto(PedidosEntrantesCB pedidoEntrante) {
			this.pedidoEntrante = pedidoEntrante;
		}

		public PedidoTexto(View convertView) {
			cantidadTexto = (TextView) convertView.findViewById(R.id.unidad);
			productoTexto = (TextView) convertView.findViewById(R.id.producto);
			estadoTexto = (TextView) convertView.findViewById(R.id.estado);
		}

		public void addPedido(PedidosEntrantesCB pedidoEntrante) {
			cantidadTexto.setText(pedidoEntrante.getUnidades() + "");
			productoTexto.setText(pedidoEntrante.getProducto()
					.getCantidadPadre()
					+ " "
					+ pedidoEntrante.getProducto().getNombreProducto());
			estadoTexto.setText(pedidoEntrante.getListos() + "");
		}

		private void cambiarColor(int posicion) {
			if (seleccionado == posicion) {
				cantidadTexto.setBackgroundColor(Color.parseColor("#F6A421"));
				productoTexto.setBackgroundColor(Color.parseColor("#F6A421"));
				estadoTexto.setBackgroundColor(Color.parseColor("#F6A421"));
			}

			else {
				if (isListo(posicion)) {
					if (pedidosEntrantes.get(posicion).isTerminado()) {
						cantidadTexto.setBackgroundColor(Color
								.parseColor("#FF0000"));
						productoTexto.setBackgroundColor(Color
								.parseColor("#FF0000"));
						estadoTexto.setBackgroundColor(Color
								.parseColor("#FF0000"));
					}

					else {
						cantidadTexto.setBackgroundColor(Color
								.parseColor("#2DFF20"));
						productoTexto.setBackgroundColor(Color
								.parseColor("#2DFF20"));
						estadoTexto.setBackgroundColor(Color
								.parseColor("#2DFF20"));
					}
				} else {
					cantidadTexto.setBackgroundColor(Color.TRANSPARENT);
					productoTexto.setBackgroundColor(Color.TRANSPARENT);
					estadoTexto.setBackgroundColor(Color.TRANSPARENT);
				}
			}
		}
	}

	@Override
	public void onDeshacerPedido(PedidosEntrantesCB pedido) {
		boolean encontrado = false;
		for (PedidosEntrantesCB pedidoE : pedidosEntrantes) {
			if (pedido.getIdComanda() == pedidoE.getIdComanda()
					&& pedido.getProducto().getIdMenu() == pedidoE
							.getProducto().getIdMenu()) {
				pedidoE.setListos(pedido.getListos());
				encontrado = true;
				break;
			}
		}
		if (!encontrado) {
			int listo;
			if (pedido.getUnidades() == pedido.getListos())
				listo = 0;
			else
				listo = pedido.getListos();
			pedidosEntrantes.add(new PedidosEntrantesCB(pedido
					.getNombreSeccion(), pedido.getNombreMesa(), pedido
					.getIdComanda(), pedido.getUnidades(),
					pedido.getProducto(), listo));
		}
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
	}

	public void modificarUnidades(PedidoModificadoCamarero pedidoM) {
		for(PedidosEntrantesCB pedidoH : fragmentHistorico.dameHistoricos()){
			if(pedidoM.getIdComanda() == pedidoH.getIdComanda()
					&& pedidoM.getIdMenu() == pedidoH
					.getProducto().getIdMenu()) {
				pedidoH.setUnidades(pedidoM.getUnidades());
				if(pedidoH.getListos() > pedidoH.getUnidades()){
					pedidoH.setListos(pedidoH.getUnidades());
				}
				fragmentHistorico.avisaAdaptador();
			}
		}
		for(PedidosEntrantesCB pedidoE : pedidosEntrantes){
			if(pedidoM.getIdComanda() == pedidoE.getIdComanda()
					&& pedidoM.getIdMenu() == pedidoE
					.getProducto().getIdMenu()) {
				pedidoE.setUnidades(pedidoM.getUnidades());
				if(pedidoE.isTerminado()){
					pedidosEntrantes.remove(pedidoE);
				}
			}
		}	
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
	}
	public void cancelarPedidos(PedidoModificadoCamarero pedidoM) {
		for(PedidosEntrantesCB pedidoH : fragmentHistorico.dameHistoricos()){
			if(pedidoM.getIdComanda() == pedidoH.getIdComanda()
					&& pedidoM.getIdMenu() == pedidoH
					.getProducto().getIdMenu()) {
				Log.e("unidades", pedidoH.getUnidades()-pedidoM.getUnidades()+"");
				Log.e("listos", pedidoH.getListos()-pedidoM.getUnidades()+"");
				pedidoH.setUnidades(pedidoH.getUnidades()-pedidoM.getUnidades());
				pedidoH.setListos(pedidoH.getListos()-pedidoM.getUnidades());
				fragmentHistorico.avisaAdaptador();
			}
		}
		for(PedidosEntrantesCB pedidoE : pedidosEntrantes){
			if(pedidoM.getIdComanda() == pedidoE.getIdComanda()
					&& pedidoM.getIdMenu() == pedidoE
					.getProducto().getIdMenu()) {
				pedidoE.setUnidades(pedidoE.getUnidades()-pedidoM.getUnidades());
				pedidoE.setListos(pedidoE.getListos()-pedidoM.getUnidades());
				if(pedidoE.isTerminado()){
					pedidosEntrantes.remove(pedidoE);
				}
			}
		}	
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
	}
	
	public void actualizarPedidos(ArrayList<PedidosEntrantesCB> pedidoActualizados){
		pedidosEntrantes = pedidoActualizados;
		fragmentHistorico.setHistoricos(pedidoActualizados);
		fragmentHistorico.avisaAdaptador();
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
	}
}
