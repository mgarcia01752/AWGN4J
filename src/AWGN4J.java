import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.MersenneTwister;

/**

	 Copyright [2017] [Maurice Garcia]
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	
 * @author Maurice Garcia
 *
 */

public class AWGN4J {

	private static GaussianRandomGenerator GRG = new GaussianRandomGenerator(new MersenneTwister());
	
	/**
	 * 
	 * @param complex Complex Number in linear
	 * @param dSNR Signal To Noise in dB
	 * @return
	 */
	public static Complex getAWGN(Complex complex, double dSNR) {
	
		/* (I^2 + Q^2)/2*SNR_Lin = Noise Sigma */
		double dNoiseSigma = Math.sqrt((Math.pow(complex.getReal(),2)+Math.pow(complex.getImaginary(),2))/(2*antiLog(dSNR)));
		
		/*Calculate Noise */
		Complex awgnComplex = new Complex((GRG.nextNormalizedDouble()*dNoiseSigma),(GRG.nextNormalizedDouble()*dNoiseSigma));
		
		/*Apply Noise to Complex*/		
		return new Complex(complex.getReal()+awgnComplex.getReal(),complex.getImaginary()+awgnComplex.getImaginary());
		
	}
	
	/**
	 * 
	 * @param dSignal in linear
	 * @param dSNR Signal To Noise in dB
	 * @return  Signal + [(E_s/SNR_Lin)^(1/2) * Random(0>x<1)];
	 */
	public static double getAWGN(double dSignal, double dSNR) {
		return dSignal + Math.sqrt(Math.pow(dSignal,2)/antiLog(dSNR))*GRG.nextNormalizedDouble();
	}
	
	/**
	 * 
	 * @param ldSignal List of Signal (Real) in linear
	 * @param dSNR  Signal To Noise in dB
	 * @return
	 */
	public static List<Double> getAWGN(List<Double> ldSignal, double dSNR) {

		List<Double> ldAWGN = new ArrayList<Double>();
		
		double dNoise = Math.sqrt(getE_s(ldSignal)/antiLog(dSNR))*GRG.nextNormalizedDouble();
		
		for (Double dSignal : ldSignal) {
			ldAWGN.add(dSignal + dNoise);
		}
		
		return ldAWGN;		
	}
	
	/**
	 * 
	 * @param dLog
	 * @return Antilog of 10
	 */
	public static double antiLog(double dLog) {
		return Math.pow(10,(dLog/10.0));
	}
	
	/**
	 * 
	 * @param ldSignal in linear
	 * @return Mean of List of |signal|^2
	 */
	private static double getE_s(List<Double> ldSignal) {
		
		double dSigTotal = 0;
		
		for (double dSignal: ldSignal) {
			dSigTotal += Math.pow(Math.abs(dSignal),2);
		}
		
		return dSigTotal/ldSignal.size();	
	}
	
	
}
