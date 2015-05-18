package signalProcessor;

import java.util.Arrays;

/**
 * @author Claudia Romeo
 * @author Dalila Rosati
 * @author Ludovica Forastieri
 */

public class Signal {

	private int length;
	private double[] parteReale;
	private double[] parteImmaginaria;

	/**
	 * @param length è la lunghezza del segnale in termini di campioni
	 */
	public Signal(int length) {

		this.length = length;
		this.parteReale = new double[length];
		this.parteImmaginaria = new double[length];

		for (int i = 0; i < this.length; i++) {
			double v = Math.random();
			if (v < 0.5)
				this.parteReale[i] = v / Math.sqrt(this.length);
			else
				this.parteReale[i] = -v / Math.sqrt(this.length);

			double p = Math.random();
			if (p < 0.5)
				this.parteImmaginaria[i] = p / Math.sqrt(this.length);
			else
				this.parteImmaginaria[i] = -p / Math.sqrt(this.length);
		}
	}

	public double[] getParteReale() {
		return this.parteReale;
	}

	public void setParteReale(double[] parteReale){
		this.parteReale=parteReale;
	}
	
	public double[] getParteImmaginaria() {
		return this.parteImmaginaria;
	}
	
	public void setParteImmaginaria(double[] parteImmaginaria){
		this.parteImmaginaria=parteImmaginaria;
	}
	
	public int getLength() {
		return this.length;
	}
	
	public void setLength(int length){
		this.length=length;
	}

	/**Metodo che calcola l'energia di un segnale
	 * @return l'energia del segnale
	 */
	public double energia() {
		double energia=0;
		for(int i=0; i<this.length; i++){
			energia+=Math.pow(Math.abs(this.parteReale[i]), 2) + Math.pow(Math.abs(this.parteImmaginaria[i]), 2);
		}
		return energia/this.length;
	}

	@Override
	public String toString() {
		return "Signal [length=" + length + ",\n parteReale="
				+ Arrays.toString(parteReale) + ",\n parteImmaginaria="
				+ Arrays.toString(parteImmaginaria) + "]";
	}

	

	
}