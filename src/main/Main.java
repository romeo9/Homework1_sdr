package main;

import SignalProcessor.SignalProcessor;

/**
 * @author Claudia Romeo
 * @author Dalila Rosati
 * @author Ludovica Forastieri
 */

public class Main {

	public static void main(String[] arg) throws Exception {
		int numSequenze = 3;
		for (int i = 1; i <= numSequenze; i++) {
			SignalProcessor.testDetection(i, 1); // i valori di SNR verranno poi diminuiti di 3dB
			SignalProcessor.testDetection(i, 2);
			SignalProcessor.testDetection(i, 3);
			SignalProcessor.testDetection(i, 4);
			System.out.println();
		}

	}
}
