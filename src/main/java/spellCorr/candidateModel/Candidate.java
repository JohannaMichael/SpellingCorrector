package spellCorr.candidateModel;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author Johanna M.
 *
 */


/**
 * Diese Klasse repräsentiert einen Kandidaten (Wort), welcher für ein falsch geschriebenes Wort in Frage kommt. 
 */
public class Candidate {
	
	

	private String possibleWord; 
	
	/**
	 * Wahrscheinlichkeit, dass ein Wort/Kandidat in einem englischen Korpus vorkommt. 
	 */
	private BigDecimal lMProbability;
	
	/**
	 * Die Häufigkeit eines bestimmten Fehlers.
	 */
	private BigDecimal errorFreq;
	
	/**
	 * Die Häufigkeit eines bestimmten Buchstabens oder Sequenz von Buchstaben. 
	 */
	private BigDecimal charFreq;
	/**
	 * Die Art des Fehlers: Substitution, Insertion, Deletion, Transposition.
	 */
	public int errorClass;
	
	/**
	 * Die Wahrscheinlichkeit einen bestimmten Fehler zu begehen.
	 */
	private BigDecimal errorProbability;
	
	/**
	 * Die Wahrscheinlichkeit, dass ein Wort in einem Korpus vorkommt, multipliziert
	 * mit der Wahrscheinlichkeit, dass ein bestimmter Fehler passiert.
	 */
	private BigDecimal noisyChannelProb;
	
	public String getPossibleWord() {
		return possibleWord;
	}

	public void setPossibleWord(String incorrectWord) {
		this.possibleWord = incorrectWord;
	}

	public BigDecimal getlMProbability() {
		return lMProbability;
	}

	public void setlMProbability(BigDecimal lMProbability) {
		this.lMProbability = lMProbability;
	}
	
	public BigDecimal getErrorFreq() {
		return errorFreq;
	}
	public void setErrorFreq(BigDecimal errorFreq) {
		this.errorFreq = errorFreq;
	}
	public BigDecimal getCharFreq() {
		return charFreq;
	}
	public void setCharFreq(BigDecimal charFreq) {
		this.charFreq = charFreq;
	}
	
	/**
	 * Errechnet die Fehler-Wahrscheinlichkeit
	 * @param errorFreq Fehler-Häufigkeit
	 * @param charFreq Buchstaben-Häufigkeit
	 */
	public void setErrorProbability(BigDecimal errorFreq, BigDecimal charFreq) {
		
		if(errorFreq.compareTo(BigDecimal.ZERO) != 0 && charFreq.compareTo(BigDecimal.ZERO) != 0) {
		errorProbability = errorFreq.divide(charFreq,10,BigDecimal.ROUND_HALF_UP);
//		errorProbability = errorProbability.multiply(new BigDecimal(Math.pow(10, 9)), new MathContext(4));
		} else {
			errorProbability = new BigDecimal(0);
		}
	}
	
	public BigDecimal getErrorProbability() {
		return errorProbability;
	}
	
	/**
	 * Multipliziert die Language-Model-Wahrscheinlichkeit mit der Fehler-Wahrscheinlicheit.
	 * @param lmProbability Language-Model-Häufigkeit
	 * @param errorProbability Fehler-Wahrscheinlichkeit
	 */
    public void setNoisyChannelProb(BigDecimal lmProbability, BigDecimal errorProbability) {
    	
		if(lmProbability.compareTo(BigDecimal.ZERO) != 0 && errorProbability.compareTo(BigDecimal.ZERO) != 0) {
		noisyChannelProb = errorProbability.multiply(lmProbability);
		noisyChannelProb = noisyChannelProb.multiply(new BigDecimal(Math.pow(10, 11)), new MathContext(4));
		} else {
			noisyChannelProb = new BigDecimal(0);
		}
	}
    
    public BigDecimal getNoisyChannelProb() {
    	return noisyChannelProb;
    }
    
    public String toString() {
		return "--> " + getPossibleWord() + ": " + "LM Word Prob.: " + 
           getlMProbability() + ", Error Freq.: " + getErrorFreq() + ", Char Freq.: " + getCharFreq() + ", Error Prob.: " + 
	       getErrorProbability() + ", final NoisyChannel Prob.: " + getNoisyChannelProb();
    	
    }
    
	
}
