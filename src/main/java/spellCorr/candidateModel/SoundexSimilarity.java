package spellCorr.candidateModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;

/**
 *Zusätzliche Klasse um die Kandidaten nach phonetisch ähnlich klingenden Wörtern auszusuchen. 
 */
public class SoundexSimilarity implements StringSimilarity {

	public Map<String, BigDecimal> getCandidates(String incorrectWord, Map<String, BigDecimal> wordProbMap, int level) {

     Map<String, BigDecimal> correctedWords = new HashMap<String, BigDecimal>();
     Soundex soundex = new Soundex();
		for (Map.Entry<String, BigDecimal> entry : wordProbMap.entrySet()){
			
			try {
				 int dif = soundex.difference(entry.getKey(), incorrectWord);
				if (dif >= level && !incorrectWord.equals(entry.getKey())) {
					correctedWords.put(entry.getKey(),entry.getValue());
				}
			} catch (EncoderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return correctedWords;
	}

}
