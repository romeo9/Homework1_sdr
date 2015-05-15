package SignalProcessor;

public class Signal {

	private int length;
	private double[] parteReale;
	private double[] parteImmaginaria;

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

	public double[] getParteImmaginaria() {
		return this.parteImmaginaria;
	}

	public double energia() {
		double energia=0;
		for(int i=0; i<this.length; i++){
			energia+=Math.pow(Math.abs(this.parteReale[i]), 2) + Math.pow(Math.abs(this.parteImmaginaria[i]), 2);
		}
		return energia/this.length;
	}

	public int getLength() {
		return this.length;
	}

}