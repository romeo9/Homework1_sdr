package SignalProcessor;

import java.util.Random;

public class Noise {

	private double varianza;
	private int length;
	private double[] parteReale;
	private double[] parteImmaginaria;

	public Noise(double snr, int length, double potenzaSegnale) {

		Random campione = null;
		double divisore = Math.pow(10, (snr / 10));
		this.varianza = (potenzaSegnale / divisore);
		this.length = length;

		this.parteReale = new double[length];
		for (int i = 0; i < this.length; i++) {
			campione = new Random();
			parteReale[i] = campione.nextGaussian() * Math.sqrt(varianza / 2);
		}

		this.parteImmaginaria = new double[length];
		for (int i = 0; i < this.length; i++) {
			campione = new Random();
			parteImmaginaria[i] = campione.nextGaussian() * Math.sqrt(varianza / 2);
		}
	}

	public double energia() {
		double energia=0;
		for(int i=0; i<this.length; i++){
			energia+=Math.pow(Math.abs(this.parteReale[i]), 2) + Math.pow(Math.abs(this.parteImmaginaria[i]), 2);
		}
		return energia/this.length;
	}

}

