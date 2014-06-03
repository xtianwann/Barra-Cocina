package prg.pi.restaurantebarracocina;

import java.util.ArrayList;

import prg.pi.restaurantebarracocina.FragmentHistorico.HistoricoListener;
import prg.pi.restaurantebarracocina.MainActivity.PendientesThread;
import prg.pi.restaurantebarracocina.conexion.Cliente;
import prg.pi.restaurantebarracocina.decodificador.DecodificadorCocinaOn;
import prg.pi.restaurantebarracocina.preferencias.PreferenciasSet;
import prg.pi.restaurantebarracocina.restaurante.PedidoFinalizado;
import prg.pi.restaurantebarracocina.restaurante.PedidoModificadoCamarero;
import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import prg.pi.restaurantebarracocina.servidor.Servidor;
import prg.pi.restaurantebarracocina.xml.XMLCocinaOn;
import prg.pi.restaurantebarracocina.xml.XMLLogout;
import prg.pi.restaurantebarracocina.xml.XMLPedidosListos;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 
 * 
 * Clase encargada de enviar los pedidos listos y de la comunicación con el fragment de pedidos históricos.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */
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
	private AlertDialog.Builder dialog;
	private FragmentHistorico fragmentHistorico;
	private static SharedPreferences preferencias;
	private DecodificadorCocinaOn decoCocinaOn;
	private PendientesThread hilo;
	private ProgressDialog pDialog;
	android.net.NetworkInfo wifi;
	ConnectivityManager connMgr;

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
		preferencias = PreferenceManager.getDefaultSharedPreferences(this);
		connMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (wifi.isAvailable()) {
			if (wifi.getDetailedState() != DetailedState.CONNECTED) {
				dialog = new AlertDialog.Builder(this);
				dialog.setMessage("No se detecta señal wifi");
				dialog.setCancelable(false);
				dialog.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				dialog.show();
			}
		} else {
			dialog = new AlertDialog.Builder(this);
			dialog.setMessage("El wifi no esta activado");
			dialog.setCancelable(false);
			dialog.setNeutralButton("Activar",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							activarWifi();
							try {
								Thread.sleep(15000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							dialog.cancel();
						}
					});
			dialog.show();
		}
		fragmentHistorico = (FragmentHistorico) getSupportFragmentManager()
				.findFragmentById(R.id.historicosF);
		fragmentHistorico.setHistoricoListener(this);
		servidor = new Servidor(MainActivity.this);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		iniciarCalculadora();
		prepararListeners();
		decoCocinaOn = null;
		hilo = new PendientesThread();
		hilo.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
		/** true -> el menú ya está visible */
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.configuracion:
			startActivity(new Intent(this, PreferenciasSet.class));
			break;
		case R.id.apagado:
			wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (comprobarSenalWifi(wifi)) {
				new LogoutAsincrono().execute();
				finish();
			} else {
				dialog = new AlertDialog.Builder(MainActivity.this);
				dialog.setMessage("No se detecta señal wifi, si continua su usuario no se deslogueará del sistema");
				dialog.setCancelable(true);
				dialog.setNeutralButton("Continuar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				dialog.show();
			}
			break;
		}

		return true;
		/** true -> consumimos el item, no se propaga */
	}
	/**
	 * 
	 * Clase encargada de pedir al servidor los pedidos pendientes de la cocina/barra
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */
	public class PendientesThread extends Thread {
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					XMLCocinaOn xmlPendientes = new XMLCocinaOn();
					String mensaje = xmlPendientes.xmlToString(xmlPendientes
							.getDOM());
					Log.e("ipServidor", getIpServidor());
					Cliente c = new Cliente(mensaje, getIpServidor());
					try {
						c.init();
						decoCocinaOn = c.getDecoCocinaOn();
						actualizarPedidos(decoCocinaOn.getPedidosPrincipal());
					} catch (NullPointerException e) {
						dialog = new AlertDialog.Builder(MainActivity.this);
						dialog.setMessage("No se ha podido conectar al servidor.Compruebe los parametros en la preferencias.");
						dialog.setCancelable(false);
						dialog.setPositiveButton("Reintentar",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										hilo = new PendientesThread();
										hilo.start();
										dialog.cancel();
									}
								});
						dialog.setNegativeButton("Preferencias",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										startActivity(new Intent(
												MainActivity.this,
												PreferenciasSet.class));
										mostrarDialogo();
									}
								});
						dialog.show();
					}
				}
			});
		}
	}
	/**
     * Muestra un dialogo.
     * 
     */
	public void mostrarDialogo() {
		dialog.show();
	}
	/**
	 * 
	 * Clase encargada de mostrar los pedidos enviados por los camareros.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */
	private class AdaptadorComanda extends BaseAdapter {
		private LayoutInflater mInflater;
		/**
		 * Constructor:
		 * 
		 * @param context
		 *            [Context] Contexto en el que se encuentra el adaptador.
		 */
		public AdaptadorComanda(Context context) {
			mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return pedidosEntrantes.size();
		}
		@Override
		public Object getItem(int position) {
			return position;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
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
	/**
	 * 
	 * Encargado de iniciar el listener de la lista de pedidos,su adaptador y todos los listener de los botones de la
	 * interfaz.
	 * 
	 */
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
	/**
	 * 
	 * Encargado de iniciar los listeners de los botones de la calculadora.
	 * 
	 */
	private void iniciarCalculadora() {
		calculadora = new Calculadora(
				new int[] { R.id.c0, R.id.c1, R.id.c2, R.id.c3, R.id.c4,
						R.id.c5, R.id.c6, R.id.c7, R.id.c8, R.id.c9 }, R.id.ce,
				R.id.total,this.findViewById(R.id.drawer_layout));
		cambiar = (Button) findViewById(R.id.cambiar);
		cambiar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				int cambio = Integer.parseInt(calculadora.total.getText() + "");
				if (seleccionado > -1) {
					if (fragmentHistorico.getHistoricos(pedidosEntrantes
							.get(seleccionado)) == null) {
						pedidosEntrantes.get(seleccionado).setListos(cambio);
						addListo();
						adaptador.notifyDataSetChanged();
					} else if (cambio <= pedidosEntrantes.get(seleccionado)
							.getUnidades()
							&& cambio >= fragmentHistorico.getHistoricos(
									pedidosEntrantes.get(seleccionado))
									.getListos()) {
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
	/**
	 * 
	 * Añade los pedidos a la lista de pedidos a enviar.
	 * 
	 */
	public void addListo() {
		if (!isListo(seleccionado)) {
			listos.add(pedidosEntrantes.get(seleccionado));
		} else {
			if (pedidosEntrantes.get(seleccionado).getListos() < 1) {
				listos.remove(pedidosEntrantes.get(seleccionado));
			}
		}
	}
	/**
	 * 
	 * Comprueba si un pedido ya esta en la lista de pedidos a enviar.
	 *
	 * @param pedido [int] Posicion del pedido en la lista.
	 * @return [boolean] true is esta ya en la lista a enviar, false en caso contrario.
	 */
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
	/**
	 * 
	 * Añade los pedidos enviados a la lista de pedidos históricos. En caso de que se hayan preparado todos los productos de un pedido, se elimina de la lista de pedidos entrantes.
	 * 
	 */
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
	/**
	 * 
	 * Comprueba si se han preparado todos los productos de un pedido.
	 * 
	 * @return [boolean] true si se han preparado todos los productos, en caso contrario false.
	 * 
	 */
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
	/**
	 * 
	 * Comunica a los camareros lo pedidos que estan listos para servir.
	 * 
	 * 	 
	 */
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
						Cliente c = new Cliente(mensaje, getIpServidor());
						c.init();
						terminarPedido();
						listos.clear();
						seleccionado = -1;
						adaptador.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}
	/**
	 * 
	 * Añade pedidos enviados por los camareros a la lista de pedidos entrantes.
	 * 
	 */
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
					Log.e("pedidoH", true + "");
					pedidoH.setUnidades(pedidoH.getUnidades()
							+ pedido.getUnidades());
					encontradoH = true;
					pedidoHEncontrado = new PedidosEntrantesCB(
							pedidoH.getNombreSeccion(),
							pedidoH.getNombreMesa(), pedidoH.getIdComanda(),
							pedidoH.getUnidades(), pedidoH.getProducto(),
							pedidoH.getListos());
					break;
				}
			}
			if (!encontradoH) {
				Log.e("encontradoH", false + "");
				encontradoE = false;
				for (PedidosEntrantesCB pedidoE : pedidosEntrantes) {
					if (pedido.getIdComanda() == pedidoE.getIdComanda()
							&& pedido.getProducto().getIdMenu() == pedidoE
									.getProducto().getIdMenu()) {
						pedidoE.setUnidades(pedidoE.getUnidades()
								+ pedido.getUnidades());
						Log.e("pedidoE", true + "");
						encontradoE = true;
						break;
					}
				}
				if (!encontradoE) {
					Log.e("encontradoE", false + "");
					for (PedidosEntrantesCB pedidoS : fragmentHistorico
							.getHistoricosServidos()) {
						if (pedidoS.getIdComanda() == pedido.getIdComanda()
								&& pedidoS.getProducto().getIdMenu() == pedido
										.getProducto().getIdMenu()) {
							pedido.setUnidades(pedido.getUnidades()
									+ pedidoS.getUnidades());
							pedido.setListos(pedidoS.getListos());
							fragmentHistorico.dameHistoricos().add(pedido);
							fragmentHistorico.getHistoricosServidos().remove(
									pedidoS);
							break;
						}
					}
					pedidosEntrantes.add(pedido);
				}
			} else {
				Log.e("encontradoH", true + "");
				encontradoE = false;
				for (PedidosEntrantesCB pedidoE : pedidosEntrantes) {
					if (pedido.getIdComanda() == pedidoE.getIdComanda()
							&& pedido.getProducto().getIdMenu() == pedidoE
									.getProducto().getIdMenu()) {
						encontradoE = true;
						pedidoE.setUnidades(pedidoHEncontrado.getUnidades()
								+ pedidoE.getUnidades());
						Log.e("pedidoE", true + "");
						break;
					}
				}
				if (!encontradoE) {
					Log.e("encontradoE", false + "");
					pedidosEntrantes.add(pedidoHEncontrado);
				}
			}
		}
		fragmentHistorico.avisaAdaptador();
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
	}
	/**
	 * 
	 * 
	 * Clase encargada las notificaciones mostradas en la aplicación.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */
	private class NotificacionBarra {
		private AlertDialog.Builder dialog;
		private MainActivity mainActivity;
		/**
		 * Constructor:
		 * 
		 * @param mainActivity
		 *            [MainActivity] Clase en la que mostrar las notificaciones.
		 */
		public NotificacionBarra(MainActivity mainActivity) {
			this.mainActivity = mainActivity;
		}
		/**
		 * Lanza una notificación cuando se intenta enviar pedidos sin modificar.
		 * 
		 */
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
	/**
	 * 
	 * 
	 * Clase encargada de almacenar los datos y colores en los textos de la lista pedidos entrantes.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */
	public class PedidoTexto {
		TextView cantidadTexto;
		TextView productoTexto;
		TextView estadoTexto;
		PedidosEntrantesCB pedidoEntrante;
		/**
	     * Constructor:
	     * 
	     * @param view [View] Vista a modificar.
	     */
		public PedidoTexto(View convertView) {
			cantidadTexto = (TextView) convertView.findViewById(R.id.unidad);
			productoTexto = (TextView) convertView.findViewById(R.id.producto);
			estadoTexto = (TextView) convertView.findViewById(R.id.estado);
		}
		/**
	     * Añade el contenido de los textos de la lista de pedidos entrantes.
	     * 
	     * @param pedidoEntrante [PedidosEntrantesCB] Pedido entrante.
	     */
		public void addPedido(PedidosEntrantesCB pedidoEntrante) {
			cantidadTexto.setText(pedidoEntrante.getUnidades() + "");
			productoTexto.setText(pedidoEntrante.getProducto()
					.getCantidadPadre()
					+ " "
					+ pedidoEntrante.getProducto().getNombreProducto());
			estadoTexto.setText(pedidoEntrante.getListos() + "");
		}
		/**
	     * Modifica el color de todos los textos de la lista de comandas pendientes
	     * 
	     * @param posicion [int] Posicion del pedido entrante en la lista.
	     */
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
		/**
		 * Permite modificar los pedidos entrantes
		 * 
		 * @param pedidoEntrante [PedidosEntrantesCB] Pedido entrante a modificar.
		 */
		public PedidoTexto(PedidosEntrantesCB pedidoEntrante) {
			this.pedidoEntrante = pedidoEntrante;
		}
	}
	/**
	 * Modifica las unidades de los pedidos enviados por el camarero.
	 * 
	 * @param pedidoM [PedidoModificadoCamarero] Pedido a modificar.
	 */
	public void modificarUnidades(PedidoModificadoCamarero pedidoM) {
		for (PedidosEntrantesCB pedidoH : fragmentHistorico.dameHistoricos()) {
			if (pedidoM.getIdComanda() == pedidoH.getIdComanda()
					&& pedidoM.getIdMenu() == pedidoH.getProducto().getIdMenu()) {
				pedidoH.setUnidades(pedidoM.getUnidades());
				if (pedidoH.getListos() > pedidoH.getUnidades()) {
					pedidoH.setListos(pedidoH.getUnidades());
				}
				if (pedidoH.getUnidades() == 0) {
					fragmentHistorico.dameHistoricos().remove(pedidoH);
					break;
				} else if (pedidoH.isServido()) {
					fragmentHistorico.getHistoricosServidos().add(pedidoH);
				}
				fragmentHistorico.avisaAdaptador();
			}
		}
		for (PedidosEntrantesCB pedidoE : pedidosEntrantes) {
			if (pedidoM.getIdComanda() == pedidoE.getIdComanda()
					&& pedidoM.getIdMenu() == pedidoE.getProducto().getIdMenu()) {
				pedidoE.setUnidades(pedidoM.getUnidades());
				if (pedidoE.isTerminado()) {
					pedidosEntrantes.remove(pedidoE);
					break;
				}
			}
		}
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
	}
	/**
	 * Modifica las unidades de los pedidos servidos devueltos por el camarero.
	 * 
	 * @param pedidoM [PedidoModificadoCamarero] Pedido a modificar.
	 */
	public void cancelarPedidos(PedidoModificadoCamarero pedidoM) {
		for (PedidosEntrantesCB pedidoH : fragmentHistorico.dameHistoricos()) {
			if (pedidoM.getIdComanda() == pedidoH.getIdComanda()
					&& pedidoM.getIdMenu() == pedidoH.getProducto().getIdMenu()) {
				pedidoH.setUnidades(pedidoH.getUnidades()
						- pedidoM.getUnidades());
				pedidoH.setListos(pedidoH.getListos() - pedidoM.getUnidades());
			}
		}
		fragmentHistorico.avisaAdaptador();
		for (PedidosEntrantesCB pedidoE : pedidosEntrantes) {
			if (pedidoM.getIdComanda() == pedidoE.getIdComanda()
					&& pedidoM.getIdMenu() == pedidoE.getProducto().getIdMenu()) {
				pedidoE.setUnidades(pedidoE.getUnidades()
						- pedidoM.getUnidades());
				pedidoE.setListos(pedidoE.getListos() - pedidoM.getUnidades());
			}
		}
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
	}
	/**
	 * Actualiza los pedidos pendientes de la cocina/barra cuando se enciende o pierde la conexión.
	 * 
	 * @param pedidoActualizados [ArrayList<PedidosEntrantesCB>] Lista de pedidos para actualizar.
	 */
	public void actualizarPedidos(
			ArrayList<PedidosEntrantesCB> pedidoActualizados) {
		pedidosEntrantes.clear();
		fragmentHistorico.dameHistoricos().clear();
		for (PedidosEntrantesCB pedido : pedidoActualizados) {
			if (pedido.isServido()){
				fragmentHistorico.getHistoricosServidos().add(pedido);
			}
			else{
				if(pedido.getListos() > 0 )
					fragmentHistorico.dameHistoricos().add(pedido);
			}
			if (!pedido.isTerminado())
				pedidosEntrantes.add(pedido);
		}
		Log.e("size", fragmentHistorico.dameHistoricos().size() + "");
		fragmentHistorico.avisaAdaptador();
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
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
	/**
	 * Activa la conexión wifi del dispositivo.
	 * 
	 **/
	private void activarWifi() {
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
	}
	/**
	 * Borra los pedidos servidos de la lista de pedidos históricos.
	 * 
	 * @param finalizados [PedidoFinalizado[]] Lista de pedidos servidos.
	 * 
	 **/
	public void todosServidos(PedidoFinalizado[] finalizados) {
		ArrayList<PedidosEntrantesCB> pedidosBorrar = new ArrayList<PedidosEntrantesCB>();
		boolean encontrado = false;
		for (PedidoFinalizado pedidoE : finalizados) {
			for (PedidosEntrantesCB pedidoH : fragmentHistorico
					.dameHistoricos()) {
				if (pedidoH.getIdComanda() == pedidoE.getIdComanda()
						&& pedidoH.getProducto().getIdMenu() == pedidoE
								.getIdMenu()) {
					encontrado = true;
					pedidosBorrar.add(pedidoH);
				}
			}
		}
		if (encontrado) {
			for (PedidosEntrantesCB p : pedidosBorrar) {
				fragmentHistorico.dameHistoricos().remove(p);
				fragmentHistorico.getHistoricosServidos().add(p);
			}
			fragmentHistorico.avisaAdaptador();
		}
	}
	/**
	 * Obtiene la ip del servidor de las preferencias.
	 * 
	 * @return [String] Ip del servidor.
	 **/
	public static String getIpServidor() {
		return preferencias.getString("ipServidor", null) + "";
	}
	/**
	 * Comprueba si el dispositivo capta señal wifi.
	 * 
	 * @return [boolean] true si hay señal wifi,false en caso contrario.
	 * 
	 **/
	public boolean comprobarSenalWifi(android.net.NetworkInfo wifi) {
		boolean resultado = false;
		if (wifi.getDetailedState() == DetailedState.CONNECTED)
			resultado = true;
		return resultado;
	}
	//Pendiente de que lo escriba Juan
	public class LogoutAsincrono extends AsyncTask<Void, Void, Boolean> {

		protected void onPreExecute() {
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Saliendo...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean resultado = true;

			XMLLogout xmlLogout = new XMLLogout();
			String mensaje = xmlLogout.xmlToString(xmlLogout.getDOM());
			Cliente cliente = new Cliente(mensaje, getIpServidor());
			try {
				cliente.init();
				SystemClock.sleep(2000);
			} catch (NullPointerException e) {
				resultado = false;
				return resultado;
			}
			return resultado;
		}

		protected void onPostExecute(Boolean resultado) {
			pDialog.dismiss();
			if (resultado) {
				finish();
			} else {
				Toast.makeText(MainActivity.this,
						"Se ha producido algún error, inténtalo de nuevo",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (pDialog != null && pDialog.isShowing()) {
			pDialog.cancel();
		}
	}
	/**
	 * Borra los pedidos de la comanda cerrada/cobrada de todas las listas
	 * 
	 * @param idCom [int] Id de la comanda a borrar.
	 * 
	 **/
	public void finalizarComanda(int idCom) {
		ArrayList <PedidosEntrantesCB> pedidosBorrar = new ArrayList<PedidosEntrantesCB>();
		
		for(PedidosEntrantesCB pedido : pedidosEntrantes){
			if(pedido.getIdComanda() == idCom)
				pedidosBorrar.add(pedido);
		}
		
		for(PedidosEntrantesCB pedido : pedidosBorrar)
			pedidosEntrantes.remove(pedido);
		
		pedidosBorrar.clear();
		
		for(PedidosEntrantesCB pedido : fragmentHistorico.dameHistoricos()){
			if(pedido.getIdComanda() == idCom)
				pedidosBorrar.add(pedido);
		}
		
		for(PedidosEntrantesCB pedido : pedidosBorrar)
			fragmentHistorico.dameHistoricos().remove(pedido);
		
		pedidosBorrar.clear();
		
		for(PedidosEntrantesCB pedido : fragmentHistorico.getHistoricosServidos()){
			if(pedido.getIdComanda() == idCom)
				pedidosBorrar.add(pedido);
		}
		
		for(PedidosEntrantesCB pedido : pedidosBorrar)
			fragmentHistorico.getHistoricosServidos().remove(pedido);
		
		pedidosBorrar = null;
		
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
		fragmentHistorico.avisaAdaptador();
	}
}
