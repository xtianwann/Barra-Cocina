package prg.pi.restaurantebarracocina;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * 
 * Clase en la que se recogen los botones de la calculadora.
 * 
 * @author Juan G. Pérez Leo
 * @author Cristian Marín Honor
 */

public class Calculadora {
	Button cero, uno, dos, tres, cuatro, cinco, seis, siete, ocho, nueve,
			ce;
	public Button botones[] = { cero, uno, dos, tres, cuatro, cinco, seis,
			siete, ocho, nueve };
	public TextView total;
	/**
     * Constructor:
     * 
     * @param botonesR [int[]] Referencia a los botones de los números de la calculadora.
     * @param ceR [int] Referencia al botón CE de la calculadora.
     * @param totalR [int] Referencia a la pantalla de la calculadora.
     */
	public Calculadora(int botonesR[], int ceR, int totalR,View view) {
		for (int contador = 0; contador < botones.length; contador++) {
			botones[contador] = (Button) view.findViewById(
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
		ce = (Button) view.findViewById(ceR);
		ce.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				total.setText(0 + "");
			}
		});
		total = (TextView) view.findViewById(totalR);
	}
	/**
     * Muestra los números pulsados de la calculadora
     * 
     * @param sumando [int] Numero pulsado.
     */

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
