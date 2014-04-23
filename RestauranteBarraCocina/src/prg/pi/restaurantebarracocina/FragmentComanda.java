package prg.pi.restaurantebarracocina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import prg.pi.restaurantebarracocina.restaurante.Mesa;
import prg.pi.restaurantebarracocina.restaurante.MesaDestino;
import prg.pi.restaurantebarracocina.restaurante.Pedido;
import prg.pi.restaurantebarracocina.restaurante.Producto;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentComanda extends Fragment{
	private ListView lista;
	private MesaDestino mesaDestino;
	private AdaptadorComanda adaptador;
	private int seleccionado = -1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_lista, container, false);
	}

	@Override
	public void onActivityCreated(Bundle state) {
		super.onActivityCreated(state);
		lista = (ListView) getView().findViewById(R.id.lista);
		reiniciarMesa();
	}
	private class AdaptadorComanda extends BaseAdapter {
		private LayoutInflater mInflater;

		public AdaptadorComanda(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return new ArrayList<Pedido>(mesaDestino.getPedidos().keySet()).size();
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
			Iterator iterador = mesaDestino.getPedidos().entrySet().iterator();
			if (iterador.hasNext()) {
				ArrayList<Pedido> pedidos = new ArrayList<Pedido>(mesaDestino.getPedidos().keySet());
				Pedido pedidoDestino = pedidos.get(position);
				String producto = pedidoDestino.getProducto().getCantidadPadre()+" "+pedidoDestino.getProducto().getNombreProducto();
				String unidades = pedidoDestino.getUnidades()+"";
				ArrayList<Integer> estados = new ArrayList<Integer>(mesaDestino.getPedidos().values());
				String estado = estados.get(position)+"";
				pedido.cantidadTexto.setText(unidades);
				pedido.productoTexto.setText(producto);
				pedido.estadoTexto.setText(estado);
			}
			return convertView;
		}

		class PedidoTexto {
			TextView cantidadTexto;
			TextView productoTexto;
			TextView estadoTexto;
		}
	}
	public int getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(int seleccionado) {
		this.seleccionado = seleccionado;
		adaptador.notifyDataSetChanged();
	}
	public void iniciarMesa(MesaDestino mesaDestino){
		this.mesaDestino = mesaDestino;
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
	}
	public void reiniciarMesa(){
		String vacio[] = {};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, vacio);
		lista.setAdapter(adapter);
	}
}
