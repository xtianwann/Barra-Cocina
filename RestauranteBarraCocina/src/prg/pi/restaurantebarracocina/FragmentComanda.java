package prg.pi.restaurantebarracocina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import prg.pi.restaurantebarracocina.restaurante.Mesa;
import prg.pi.restaurantebarracocina.restaurante.MesaDestino;
import prg.pi.restaurantebarracocina.restaurante.Pedido;
import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import prg.pi.restaurantebarracocina.restaurante.Producto;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentComanda extends Fragment {
	private ListView lista;
	private AdaptadorComanda adaptador;
	private ArrayList<PedidosEntrantesCB> pedidosEntrantes;
	private int seleccionado = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_lista, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		prepararListeners();
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
				pedido = new PedidoTexto();
				pedido.cantidadTexto = (TextView) convertView
						.findViewById(R.id.unidad);
				pedido.productoTexto = (TextView) convertView
						.findViewById(R.id.producto);
				pedido.estadoTexto = (TextView) convertView
						.findViewById(R.id.estado);
				convertView.setTag(pedido);
			} else {
				pedido = (PedidoTexto) convertView.getTag();
			}
			PedidosEntrantesCB pedidoEntrante = pedidosEntrantes.get(position);
			Log.d("position",pedidoEntrante.getProducto().getNombreProducto()+"");
			pedido.cantidadTexto.setText(pedidoEntrante.getUnidades()+"");
			pedido.productoTexto.setText(pedidoEntrante.getProducto()
					.getCantidadPadre()
					+ " "
					+ pedidoEntrante.getProducto().getNombreProducto());
			pedido.estadoTexto.setText(pedidoEntrante.getListos()+"");
			if (seleccionado == position) {
				pedido.cantidadTexto.setBackgroundColor(Color
						.parseColor("#F6A421"));
				pedido.productoTexto.setBackgroundColor(Color
						.parseColor("#F6A421"));
				pedido.estadoTexto.setBackgroundColor(Color
						.parseColor("#F6A421"));

			} else {
				pedido.cantidadTexto.setBackgroundColor(Color.TRANSPARENT);
				pedido.productoTexto.setBackgroundColor(Color.TRANSPARENT);
				pedido.estadoTexto.setBackgroundColor(Color.TRANSPARENT);
			}
			return convertView;
		}

		class PedidoTexto {
			TextView cantidadTexto;
			TextView productoTexto;
			TextView estadoTexto;
		}
	}
	public void addPedidos(PedidosEntrantesCB[] pedidosE) {
		for(PedidosEntrantesCB pedido : pedidosE){
			pedidosEntrantes.add(pedido);
		}
		lista.invalidateViews();
		adaptador.notifyDataSetChanged();
	}
	
	private void prepararListeners() {
		pedidosEntrantes = new ArrayList<PedidosEntrantesCB>();
		lista = (ListView) getView().findViewById(R.id.lista);
		adaptador = new AdaptadorComanda(getView().getContext());
		lista.setAdapter(adaptador);
		lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id) {
				seleccionado = pos;
				adaptador.notifyDataSetChanged();
			}
		});
		
//		enviar = (Button) getView().findViewById(R.id.enviar);
//		enviar.setOnClickListener(new AdapterView.OnClickListener() {
//			public void onClick(View view) {
//				
//					new Thread(new Runnable() {
//						public void run() {
//							getActivity().runOnUiThread(new Runnable() {
//								@Override
//								public void run() {
//
//								}
//							});
//						}
//					}).start();
//				}
//			}
//
//		});
	}
	
}
