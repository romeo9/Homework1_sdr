package SignalProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

import utility.Utility;

public class SignalProcessor {

	
	/**Metodo che calcola la soglia */
	public static double calcolaSoglia(int numeroProve, double pFa, double snr)	throws Exception {
		double[] energie = new double[numeroProve];
		Noise  n= new Noise(snr, 1000, 1);
		for (int i = 0; i < numeroProve; i++) {
			//n = new Noise(snr, 1000, 1);
			energie[i] = n.energia();
			//System.out.println(energie[i]);
		}
		double th = Utility.media(energie) + (2 * Math.sqrt(Utility.varianza(energie)) * Utility.InvErf(1 - (2 * pFa)));
		System.out.println("la soglia è:" +th);
		return th;
	}


	/** metodo che calcola la probabilità di detection di un segnale */
	public static double probabilitàDetection(Signal signal, int numeroProve, double soglia) {
		double energiaBlocco = 0;
		double count = 0;
		for (int i = 0; i < signal.getLength(); i += 1000) {
			Signal blocco = dividiSegnale(signal, i, i + 1000);	// divido il segnale
			energiaBlocco = blocco.energia();			// calcolo l'energia del blocco da 1000 campioni
			// System.out.println(energiaBlocco);
			if (energiaBlocco > soglia) {		// se l'energia del blocco è maggiore della soglia incremento il contatore
				count ++;
			}
		}
		return count/numeroProve;
	}

	/** Metodo di supporto per dividere un segnale in più blocchi */
	public static Signal dividiSegnale(Signal originale, int lowerBound, int upperBound) {
		Signal blocco = new Signal(upperBound - lowerBound);
		for (int i = 0; i < upperBound - lowerBound; i++) {
			blocco.getParteReale()[i] = originale.getParteReale()[i+ lowerBound];
			blocco.getParteImmaginaria()[i] = originale.getParteImmaginaria()[i+lowerBound];
		}
		return blocco;
	}

	/** Legge il segnale da file*/
	public static Signal leggiSegnale(String pathIn, int numeroCampioni)
			throws NumberFormatException, IOException {
		BufferedReader bReader = new BufferedReader(new FileReader(pathIn));
		String line;
		Signal signal = new Signal(numeroCampioni);
		int i = 0;
		while ((line = bReader.readLine()) != null) {
			String number[] = line.split("\t");
			signal.getParteReale()[i] = Double.parseDouble(number[0]);
			signal.getParteImmaginaria()[i] = Double.parseDouble(number[1]);
			i++;
		}
		bReader.close();
		return signal;
	}

		
	/** Metodo che testa la presenza dell'utente primario*/
	public static void testDetection(int sequenza, int output) throws Exception {
		Signal segnale = leggiSegnale("/Users/claudiaromeo/Documents/SDR/Sequenze_SDR_2015/Sequenza_"
				+ sequenza + "/output_" + output + ".dat",1000000);
		double snr = snr(segnale.energia());	
		double pFa = 0.001; // probabilità falso allarme (10^-3)
		double soglia = calcolaSoglia(1000, pFa, snr);
		double pd = probabilitàDetection(segnale, 1000, soglia);
		if (pd >= 0.8) // Se la probabilità di detection è maggiore di 0,8 si suppone la presenza dell'utente primario
			System.out.println("Sequenza "+sequenza+"\t SNR = "+snr+"\t Probabilità Detection = "+pd*100+"% \t Presenza utente primario");
		else  			// altrimenti si suppone l'assenza dell'utente primario
			System.out.println("Sequenza "+sequenza+"\t SNR = "+snr+"\t Probabilità Detection = "+pd*100+"% \t Assenza utente primario" );

	}
	public static double snr(double energia){
		double pSu = 1;
		double pN = energia - pSu;
		double snr = pSu/pN;
		snr = 10*Math.log10(snr); //linearizzazione
		return snr;
	}

	

}