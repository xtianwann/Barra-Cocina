package prg.pi.restaurantebarracocina;

import java.util.ArrayList;

import prg.pi.restaurantebarracocina.restaurante.PedidosEntrantesCB;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentHistorico extends Fragment{
	private ListView listaHistorico;
	private Button limpiar,cambiar,mas,menos,enviar,deshacer;
	private Calculadora calculadora;
	public ArrayList<PedidosEntrantesCB> historicos = new ArrayList<PedidosEntrantesCB>();
	private int seleccionado = -1;
	private AdaptadorHistorico adaptador;

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
	
	private class AdaptadorHistorico extends BaseAdapter {
		private LayoutInflater mInflater;
		
		public AdaptadorHistorico(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return historicos.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
//			HistoricoText pedidoText;
//			if (convertView == null) {
//				convertView = mInflater.inflate(R.layout.historico_list, null);
//				pedidoText = new HistoricoText(convertView);
//				convertView.setTag(pedidoText);
//			} else {
//				pedidoText = (HistoricoText) convertView.getTag();
//			}
//			PedidosEntrantesCB historico = historicos.get(position);
//			pedidoText.addTexto(historico.getNombreSeccion(), historico.getNombreMesa(), historico.getUnidades(),historico.getProducto().getCantidadPadre()+" "+historico.getProducto().getNombreProducto(), historico.getListos(), historico.getServidos());
//			if (seleccionado == position) {
//				pedidoText.cambiaColor(Color.parseColor("#F6A421"));
//			} else {
//				if(historico.existenListos()){
//					pedidoText.cambiaColor(Color.parseColor("#0EA7F4"));
//				}
//				else{
//					pedidoText.cambiaColor(Color.TRANSPARENT);
//				}
//			}
			
			return convertView;
		}
	}
	
	public class Calculadora {
		Button cero, uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve,
				ce;
		public Button botones[] = { cero, uno, dos, tres, cuatro, cinco, seis,
				siete, ocho, nueve };
		public TextView total;

		public Calculadora(int botonesR[], int ceR, int totalR) {
			for (int contador = 0; contador < botones.length; contador++) {
				botones[contador] = (Button) getView().findViewById(
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
			ce = (Button) getView().findViewById(ceR);
			ce.setOnClickListener(new AdapterView.OnClickListener() {
				public void onClick(View view) {
					total.setText(0+"");
				}
			});
			total = (TextView) getView().findViewById(totalR);
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

	public void limpiarPedidos() {
		historicos.clear();
		listaHistorico.invalidateViews();
	}
	
	private void prepararListeners() {
		listaHistorico = (ListView) getView().findViewById(R.id.historico);
		adaptador = new AdaptadorHistorico(getView().getContext());
		listaHistorico.setAdapter(adaptador);
		listaHistorico.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> list, View view, int pos,
					long id) {
				TextView seccion = (TextView)view.findViewById(R.id.seccionPendiente);
				ColorDrawable color = (ColorDrawable) seccion.getBackground();
				int codigoColor = color.getColor();
				if(codigoColor == (Color.parseColor("#0EA7F4"))){
					seleccionado = pos;
					adaptador.notifyDataSetChanged();
				}
			}
		});
		calculadora = new Calculadora(
				new int[] { R.id.c0, R.id.c1, R.id.c2, R.id.c3, R.id.c4,
						R.id.c5, R.id.c6, R.id.c7, R.id.c8, R.id.c9 }, R.id.ce,
				R.id.total);
		cambiar = (Button) getView().findViewById(R.id.cambiar);
		cambiar.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if(seleccionado > -1){
//					PedidosEntrantesCB pedido = historicos.get(seleccionado);
//					int numeroCalculadora = Integer.parseInt(calculadora.total.getText()+"");
//					if(numeroCalculadora <= pedido.getListos()){
//						pedido.setServidos(numeroCalculadora);
//						if(!pedido.existenListos()){
//							seleccionado = -1;
//							if(pedido.isServido())
//								historicos.remove(pedido);
//						}
//						adaptador.notifyDataSetChanged();
//					}					
//					calculadora.total.setText("0");
				}
			}

		});
		mas = (Button) getView().findViewById(R.id.mas);
		mas.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if(seleccionado > -1){
//					PedidosEntrantesCB pedido = historicos.get(seleccionado);
//					pedido.setServidos(pedido.getServidos()+1);
//					if(!pedido.existenListos()){
//						seleccionado = -1;
//						if(pedido.isServido())
//							historicos.remove(pedido);
//					}
//					adaptador.notifyDataSetChanged();
				}
			}

		});
		menos = (Button) getView().findViewById(R.id.menos);
		menos.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
//				if(seleccionado > -1){
//					PedidosEntrantesCB pedido = historicos.get(seleccionado);
//					if(pedido.getServidos() > 0){
//						pedido.setServidos(pedido.getServidos()-1);
//						adaptador.notifyDataSetChanged();
//					}
//				}
			}

		});
		//Trabajo
//		pedidosPendientes.add(new PedidosPendientesCamarero("Abajo", "Rincon", 1, 
//				new Producto(1, "Chocos", "Racion"),3,1,0));
//		pedidosPendientes.add(new PedidosPendientesCamarero("Arriba", "Centro", 2, 
//				new Producto(2, "Huevas", "Tapa"),5,2,0));
		//////////////////////////////////////////////////
		
	}
	
	public class HistoricoText {
		private TextView seccionTexto;
		private TextView mesaTexto;
		private TextView cantidadTexto;
		private TextView productoTexto;
		private TextView listoTexto;
		
		public HistoricoText (View view){
			cantidadTexto = (TextView) view
					.findViewById(R.id.cantidadPendiente);
			productoTexto = (TextView) view
					.findViewById(R.id.productoPendiente);
			seccionTexto = (TextView) view
					.findViewById(R.id.seccionPendiente);
			mesaTexto = (TextView) view
					.findViewById(R.id.mesaPendiente);
			listoTexto = (TextView) view
					.findViewById(R.id.listoPendiente);
		}
		public void cambiaColor(int color){
			cantidadTexto.setBackgroundColor(color);
			productoTexto.setBackgroundColor(color);
			seccionTexto.setBackgroundColor(color);
			mesaTexto.setBackgroundColor(color);
			listoTexto.setBackgroundColor(color);
		}
		public void addTexto(String seccion,String mesa,int cantidad,String producto,int listo){
			cantidadTexto.setText(cantidad+"");
			productoTexto.setText(producto);
			seccionTexto.setText(seccion);
			mesaTexto.setText(mesa);
			listoTexto.setText(listo+"");
		}
		
		public TextView getSeccionTexto() {
			return seccionTexto;
		}
		public void setSeccionTexto(TextView seccionTexto) {
			this.seccionTexto = seccionTexto;
		}
		public TextView getMesaTexto() {
			return mesaTexto;
		}
		public void setMesaTexto(TextView mesaTexto) {
			this.mesaTexto = mesaTexto;
		}
		public TextView getCantidadTexto() {
			return cantidadTexto;
		}
		public void setCantidadTexto(TextView cantidadTexto) {
			this.cantidadTexto = cantidadTexto;
		}
		public TextView getProductoTexto() {
			return productoTexto;
		}
		public void setProductoTexto(TextView productoTexto) {
			this.productoTexto = productoTexto;
		}
		public TextView getListoTexto() {
			return listoTexto;
		}
		public void setListoTexto(TextView listoTexto) {
			this.listoTexto = listoTexto;
		}
	}
//
//	public void addPedidosPendientes(
//			PedidosPendientesCamarero[] pedidosAdd) {
//		for(PedidosPendientesCamarero pedido : pedidosAdd)
//			pedidosPendientes.add(pedido);
//		pedidos.invalidateViews();
//		adaptador.notifyDataSetChanged();
//	}
//
//	public void addPedidosListos(PedidoListo[] pedidosListos) {
//		for(PedidoListo pedidoListo : pedidosListos){
//			for(PedidosPendientesCamarero pedido: pedidosPendientes){
//				if(pedido.getIdComanda() == pedidoListo.getIdComanda() && pedido.getProducto().getIdMenu() == pedidoListo.getIdMenu()){
//					pedido.setListos(pedidoListo.getListos());
//				}
//			}
//		}
//		pedidos.invalidateViews();
//		adaptador.notifyDataSetChanged();
//	}
}
