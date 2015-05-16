package signalProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import utility.Utility;

/**
 * @author Claudia Romeo
 * @author Dalila Rosati
 * @author Ludovica Forastieri
 */

public class SignalProcessor {
	private static int NUM_PROVE=1000;

	/**Metodo che calcola la soglia
	 * @param pFa è la probabilità di falso allarme
	 * @param snr è il rapporto segnale rumore
	 * @return il valore della soglia
	 * @throws Exception
	 */
	public static double calcolaSoglia(double pFa, double snr) throws Exception {
		double[] energie = new double[NUM_PROVE];	//nuovo array di energie
		for (int i = 0; i < NUM_PROVE; i++) {
			Noise  n = new Noise(snr, 1000, 1);		//genero un rumore di lunghezza uguale ai blocchi in cui ho diviso il segnale
			energie[i] = n.energia();				//per ogni prova calcolo l'energia del rumore
		}
		double th = Utility.media(energie) + (2 * Math.sqrt(Utility.varianza(energie)) * 
				Utility.InvErf(1 - (2 * pFa)));		//calcolo la soglia
		System.out.print("la soglia è:");			//se la soglia è NotaNumber la considero come -infinito
		if(Double.isNaN(th))						
			System.out.print("-infinity\n");
		else
			System.out.print(th +"\n");
		return th;
	}


	/** metodo che calcola la probabilità di detection di un segnale 
	 * @param signal segnale ricevuto
	 * @param soglia
	 * @return la probabilità di detection di quel segnale con una data soglia
	 */
	public static double probabilitàDetection(Signal signal, double soglia) {
		double energiaBlocco = 0;
		double count = 0;
		for (int i = 0; i < signal.getLength(); i += 1000) {
			Signal blocco = dividiSegnale(signal, i, i + 1000);	// divido il segnale
			energiaBlocco = blocco.energia();					// calcolo l'energia del blocco da 1000 campioni
			if (energiaBlocco > soglia) {						// se l'energia del blocco è maggiore della soglia incremento il contatore
				count ++;
			}
		}
		return (double)count/(double)NUM_PROVE;
	}

	/** Metodo di supporto per dividere un segnale in più blocchi 
	 * @param signOriginale è il segnale originale letto da input
	 * @param lowerBound è il limite inferiore di divisione del segnale
	 * @param upperBound è il limite superiore 
	 * @return il segnale diviso in tale blocco
	 */
	public static Signal dividiSegnale(Signal signOriginale, int lowerBound, int upperBound) {
		Signal blocco = new Signal(upperBound - lowerBound);
		for (int i = 0; i < upperBound - lowerBound; i++) {
			blocco.getParteReale()[i] = signOriginale.getParteReale()[i+ lowerBound];
			blocco.getParteImmaginaria()[i] = signOriginale.getParteImmaginaria()[i+lowerBound];
		}
		return blocco;
	}

	/** Legge il segnale da file
	 * @param pathIn il path del file da leggere da input
	 * @param numeroCampioni di cui è composto il segnale
	 * @return il segnale letto
	 */
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


	/** Metodo che testa la presenza dell'utente primario
	 * @param sequenza il numero della sequenza da leggere
	 * @param output il numero dell'output in cui è divisa la sequenza
	 */
	public static void testDetection(int sequenza, int output) throws Exception {
		Signal segnale = leggiSegnale("/Users/claudiaromeo/Documents/SDR/Sequenze_SDR_2015/Sequenza_"
				+ sequenza + "/output_" + output + ".dat",1000000);
		double snr = snr(segnale.energia());	
		double pFa = 0.001; // probabilità falso allarme (10^-3)
		double soglia = calcolaSoglia(pFa, snr);
		double pd = probabilitàDetection(segnale, soglia);
		if (pd >= 0.8) // Se la probabilità di detection è maggiore di 0,8 si suppone la presenza dell'utente primario
			System.out.println("Sequenza "+sequenza+"\t Output "+output+"\t SNR = "+snr+"\t Probabilità Detection = "+pd*100+"% \t Presenza utente primario");
		else  			// altrimenti si suppone l'assenza dell'utente primario
			if(Double.isNaN(snr))
				System.out.println("Sequenza "+sequenza+"\t Output "+output+"\t SNR = "+"-infinity"+"\t Probabilità Detection = "+pd*100+"% \t Assenza utente primario" );
			else
				System.out.println("Sequenza "+sequenza+"\t Output "+output+"\t SNR = "+snr+"\t Probabilità Detection = "+pd*100+"% \t Assenza utente primario" );

	}
	/**Metodo che calcola l'SNR
	 * @param energia è l'energia del segnale letto da input
	 * @return rapporto segnale rumore
	 */
	public static double snr(double energia){
		double pSu = 1;
		double pN = energia - pSu;
		double snr = pSu/pN;
		snr = 10*Math.log10(snr); //linearizzazione

		return snr;
	}
}