package prg.pi.restaurantebarracocina;

import java.util.ArrayList;

import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import prg.pi.restaurantebarracocina.xml.XMLModificacionCB;
import prg.pi.restaurantebarracocina.conexion.Cliente;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentHistorico extends Fragment {
	private ListView listaHistorico;
	private Button limpiar, cambiar, mas, menos, enviar, deshacer;
	private Calculadora calculadora;
	private ArrayList<PedidosEntrantesCB> historicos = new ArrayList<PedidosEntrantesCB>();
	private int seleccionado = -1;
	private AdaptadorHistorico adaptador;
	private ArrayList<PedidosEntrantesCB> historicosServidos = new ArrayList<PedidosEntrantesCB>();
	public HistoricoListener historicoListener;
	private int listoAnterior;
	private AlertDialog.Builder dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_historico, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		prepararListeners();
	}
	/**
	 * 
	 * Clase encargada de mostrar los pedidos historicos
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */
	private class AdaptadorHistorico extends BaseAdapter {
		private LayoutInflater mInflater;
		/**
		 * Constructor:
		 * 
		 * @param context
		 *            [Context] Contexto en el que se encuentra el adaptador.
		 */
		public AdaptadorHistorico(Context context) {
			mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return historicos.size();
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
			HistoricoText pedidoText;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.historico_list, null);
				pedidoText = new HistoricoText(convertView);
				convertView.setTag(pedidoText);
			} else {
				pedidoText = (HistoricoText) convertView.getTag();
			}
			PedidosEntrantesCB historico = historicos.get(position);
			pedidoText.addTexto(historico.getNombreSeccion(),
					historico.getNombreMesa(), historico.getUnidades(),
					historico.getProducto().getCantidadPadre() + " "
							+ historico.getProducto().getNombreProducto(),
					historico.getListos());
			if (seleccionado == position) {
				pedidoText.cambiaColor(Color.parseColor("#FF0000"));
			} else {
				pedidoText.cambiaColor(Color.TRANSPARENT);
			}

			return convertView;
		}
	}
	/**
	 * 
	 * Elimina todos los pedidos históricos.
	 * 
	 */
	public void limpiarPedidos() {
		historicos.clear();
		listaHistorico.invalidateViews();
	}
	/**
	 * 
	 * Encargado de iniciar el listener de la lista de pedidos históricos,su adaptador y todos los listener de los botones de la
	 * interfaz.
	 * 
	 */
	private void prepararListeners() {
		listaHistorico = (ListView) getView().findViewById(R.id.historico);
		adaptador = new AdaptadorHistorico(getView().getContext());
		listaHistorico.setAdapter(adaptador);
		listaHistorico
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						if (seleccionado > -1) {
							notificacionDeshacer();
							historicos.get(seleccionado).setListos(
									listoAnterior);
							seleccionado = -1;
							adaptador.notifyDataSetChanged();
						} else {
							seleccionado = arg2;
							listoAnterior = historicos.get(arg2).getListos();
							adaptador.notifyDataSetChanged();
						}
						return false;
					}

				});
		calculadora = new Calculadora(
				new int[] { R.id.c0, R.id.c1, R.id.c2, R.id.c3, R.id.c4,
						R.id.c5, R.id.c6, R.id.c7, R.id.c8, R.id.c9 }, R.id.ce,
				R.id.total,getView());
		cambiar = (Button) getView().findViewById(R.id.cambiar);
		cambiar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					PedidosEntrantesCB pedido = historicos.get(seleccionado);
					int numeroCalculadora = Integer.parseInt(calculadora.total
							.getText() + "");
					if (numeroCalculadora <= pedido.getUnidades()
							&& numeroCalculadora <= listoAnterior) {
						pedido.setListos(numeroCalculadora);
						adaptador.notifyDataSetChanged();
					}
					calculadora.total.setText("0");
				}
			}

		});
		mas = (Button) getView().findViewById(R.id.mas);
		mas.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					PedidosEntrantesCB pedido = historicos.get(seleccionado);
					int suma = pedido.getListos() + 1;
					if (suma <= listoAnterior) {
						pedido.setListos(suma);
						adaptador.notifyDataSetChanged();
					}
				}
			}

		});
		menos = (Button) getView().findViewById(R.id.menos);
		menos.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (seleccionado > -1) {
					PedidosEntrantesCB pedido = historicos.get(seleccionado);
					if (pedido.getListos() > 0) {
						pedido.setListos(pedido.getListos() - 1);
						adaptador.notifyDataSetChanged();
					}
				}
			}

		});
		deshacer = (Button) getView().findViewById(R.id.deshacer);
		deshacer.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {

				if (seleccionado > -1) {
					final PedidosEntrantesCB pedido = historicos
							.get(seleccionado);
					if (pedido.getListos() != listoAnterior
							|| pedido.getUnidades() == pedido.getListos()) {
						new Thread(new Runnable() {
							public void run() {
								getActivity().runOnUiThread(new Runnable() {
									@Override
									public void run() {
										XMLModificacionCB xmlEnviarModificacion = new XMLModificacionCB(
												new PedidosEntrantesCB[] { pedido });
										String mensaje = xmlEnviarModificacion
												.xmlToString(xmlEnviarModificacion
														.getDOM());
										Cliente c = new Cliente(mensaje,
												MainActivity.getIpServidor());
										try {
											c.init();
											try {
												Thread.sleep(2000);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
											String respuesta[] = c
													.getDecoAcuse()
													.getRespuesta();
											if (respuesta[0].equals("NO")) {
												pedido.setListos(listoAnterior);
												dialog = new AlertDialog.Builder(
														getView().getContext());
												dialog.setMessage(respuesta[1]);
												dialog.setCancelable(false);
												dialog.setNeutralButton(
														"OK",
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																listaHistorico.invalidateViews();
																adaptador.notifyDataSetChanged();
																dialog.cancel();
															}
														});
												dialog.show();
											} else {
												if (pedido.getUnidades() == pedido
														.getListos()
														|| pedido.getListos() == 0) {
													pedido.setListos(0);
													historicos.remove(pedido);
												}
												historicoListener
														.onDeshacerPedido(pedido);
												seleccionado = -1;
												adaptador
														.notifyDataSetChanged();
											}
										} catch (NullPointerException e) {
											Log.e("size", historicos.size()
													+ "");
											Log.e("listoAnterior",
													listoAnterior + "");
											dialog = new AlertDialog.Builder(
													getView().getContext());
											dialog.setMessage("No se ha podido conectar al servidor");
											dialog.setCancelable(false);
											dialog.setNeutralButton(
													"OK",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.cancel();
														}
													});
											dialog.show();
										}
									}
								});
							}
						}).start();

					}
				}
			}

		});
	}
	/**
	 * 
	 * 
	 * Clase encargada de almacenar los datos y colores en los textos de la lista pedidos históricos.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */
	public class HistoricoText {
		private TextView seccionTexto;
		private TextView mesaTexto;
		private TextView cantidadTexto;
		private TextView productoTexto;
		private TextView listoTexto;
		/**
	     * Constructor:
	     * 
	     * @param view [View] Vista a modificar.
	     */
		public HistoricoText(View view) {
			cantidadTexto = (TextView) view
					.findViewById(R.id.cantidadPendiente);
			productoTexto = (TextView) view
					.findViewById(R.id.productoPendiente);
			seccionTexto = (TextView) view.findViewById(R.id.seccionPendiente);
			mesaTexto = (TextView) view.findViewById(R.id.mesaPendiente);
			listoTexto = (TextView) view.findViewById(R.id.listoPendiente);
		}
		/**
	     * Modifica el color de todos los textos de la lista de pedidos históricos.
	     * 
	     * @param color [int] Color a modificar.
	     */
		public void cambiaColor(int color) {
			cantidadTexto.setBackgroundColor(color);
			productoTexto.setBackgroundColor(color);
			seccionTexto.setBackgroundColor(color);
			mesaTexto.setBackgroundColor(color);
			listoTexto.setBackgroundColor(color);
		}
		/**
	     * Añade el contenido de los textos de la lista de pedidos históricos.
	     * 
	     * @param seccion [String] Nombre de la seccion.
	     * @param mesa [String] Nombre de la mesa.
	     * @param cantidad [int] Cantidad de productos.
	     * @param producto [String] Nombre del producto.
	     * @param listo [String] Cantidad de productos listos.
	     */
		public void addTexto(String seccion, String mesa, int cantidad,
				String producto, int listo) {
			cantidadTexto.setText(cantidad + "");
			productoTexto.setText(producto);
			seccionTexto.setText(seccion);
			mesaTexto.setText(mesa);
			listoTexto.setText(listo + "");
		}
	}
	/**
     * Añade los pedidos listos enviados por cocina/barra a la lista de pedidos históricos.
     * 
     * @param pedidosAdd [PedidosEntrantesCB[]]
     */
	public void addPedidosHistoricos(PedidosEntrantesCB[] pedidosAdd) {
		boolean encontrado;
		for (PedidosEntrantesCB pedido : pedidosAdd) {
			encontrado = false;
			for (PedidosEntrantesCB pedidoH : historicos) {
				if (pedido.getIdComanda() == pedidoH.getIdComanda()
						&& pedido.getProducto().getIdMenu() == pedidoH
								.getProducto().getIdMenu()) {
					if (pedido.getUnidades() != pedidoH.getUnidades())
						pedidoH.setListos(pedidoH.getListos()
								+ pedido.getListos());
					else {
						pedidoH.setListos(pedido.getListos());
					}
					encontrado = true;
					break;
				}
			}
			if (!encontrado)
				historicos.add(new PedidosEntrantesCB(
						pedido.getNombreSeccion(), pedido.getNombreMesa(),
						pedido.getIdComanda(), pedido.getUnidades(), pedido
								.getProducto(), pedido.getListos()));
		}
		listaHistorico.invalidateViews();
		adaptador.notifyDataSetChanged();
	}
	/**
	 * 
	 * 
	 * Interface para la comunicación con la clase principal.
	 * 
	 * @author Juan G. Pérez Leo
	 * @author Cristian Marín Honor
	 */
	public interface HistoricoListener {
		/**
	     * Comunica los pedidos que se han modificado
	     * 
	     * @param pedido [PedidosEntrantesCB] Pedidos modificados.
	     */
		public void onDeshacerPedido(PedidosEntrantesCB pedido);
	}
	/**
     * Permite modificar el listener. 
     * 
     * @param historicoListener [HistoricoListener] Listener asignado.
     */
	public void setHistoricoListener(HistoricoListener historicoListener) {
		this.historicoListener = historicoListener;
	}
	/**
     * Lanza una notificación cuando no se termina de deshacer un pedido correctamente. 
     * 
     */
	private void notificacionDeshacer() {
		dialog = new AlertDialog.Builder(getView().getContext());
		dialog.setMessage("No se ha terminado la acción de deshacer.");
		dialog.setCancelable(false);
		dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog.show();
	}
	/**
     * Devuelve el pedido entrante, en caso de encontrarlo, de la lista de pedidos históricos.
     * 
     * @param pedido [PedidosEntrantesCB] Pedido a buscar.
     * @return [PedidosEntrantesCB] Pedido encontrado.
     */
	public PedidosEntrantesCB getHistoricos(PedidosEntrantesCB pedido) {
		for (PedidosEntrantesCB pedidoE : historicos)
			if (pedido.getIdComanda() == pedidoE.getIdComanda()
					&& pedido.getProducto().getIdMenu() == pedidoE
							.getProducto().getIdMenu())
				return pedidoE;
		return null;
	}
	/**
     * Devuelve la lista de pedidos históricos.
     * 
     * @return [ArrayList<PedidosEntrantesCB>] Lista de pedidos históricos.
     */
	public ArrayList<PedidosEntrantesCB> dameHistoricos() {
		return historicos;
	}
	/**
     * Avisa al adaptador de la lista de pedidos históricos.
     * 
     */
	public void avisaAdaptador() {
		adaptador.notifyDataSetChanged();
	}
	/**
     * Devuelve la lista de pedidos históricos servidos.
     * 
     * @return [ArrayList<PedidosEntrantesCB>] Lista de pedidos históricos servidos.
     */
	public ArrayList<PedidosEntrantesCB> getHistoricosServidos() {
		return historicosServidos;
	}
	/**
     * Devuelve el adaptador de la lista de pedidos históricos.
     * 
     * @return [AdaptadorHistorico] Adaptador de la lista de pedidos históricos.
     */
	public AdaptadorHistorico getAdaptador() {
		return adaptador;
	}

}
