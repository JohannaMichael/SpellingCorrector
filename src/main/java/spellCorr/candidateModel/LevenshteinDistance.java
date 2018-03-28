package spellCorr.candidateModel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Diese Klasse stellt für das falsch geschriebene Wort eine Liste an Kandidaten zusammen. Stringvergleich mittels 
 * Levenshtein-Distanz, hier unter Verwendung der StringUtils (Apache Commons).
 * 
 * @see spellCorr.candidateModel.StringSimilarity
 */
public class LevenshteinDistance implements StringSimilarity {
	
	
	/**
	 * Die Levenshtein-Distanz wird hier etwas ummodifiziert, das heißt nicht nur die Fehlerarten Insertion, Deletion und Substitution 
	 * werden berücksichtigt, sondern auch Transposition. 
	 */
	public Map<String, BigDecimal> getCandidates(String incorrectWord, Map<String, BigDecimal> wordProbMap, int level) {
		Map<String, BigDecimal> correctedWords = new HashMap<String, BigDecimal>();
		 correctedWords = getLevenDistance(incorrectWord, wordProbMap, level);
		return correctedWords;
	}
	
	private Map<String, BigDecimal> getLevenDistance(String incorrectWord, Map<String, BigDecimal> wordProbMap, int level) {
		
		Map<String, BigDecimal> correctedWords = new HashMap<String, BigDecimal>();
		
		for (Map.Entry<String, BigDecimal> entry : wordProbMap.entrySet()){
			
			int ld = StringUtils.getLevenshteinDistance(entry.getKey(), incorrectWord);
			if (ld <= level && !incorrectWord.equals(entry.getKey())) {
				correctedWords.put(entry.getKey(),entry.getValue());
			} 
		}
		
		for(Iterator<Map.Entry<String, BigDecimal>> it = correctedWords.entrySet().iterator(); it.hasNext();) {
			 Map.Entry<String, BigDecimal> correctedWord = it.next();
			int ld = StringUtils.getLevenshteinDistance(correctedWord.getKey(), incorrectWord);
			if(ld == 2) {
				if(correctedWord.getKey().length() == incorrectWord.length()) {
					int index = StringUtils.indexOfDifference(correctedWord.getKey(), incorrectWord);
					int ld2 = StringUtils.getLevenshteinDistance(correctedWord.getKey().substring(index+2), incorrectWord.substring(index+2));
					if(correctedWord.getKey().charAt(index+1) != incorrectWord.charAt(index) || 
						correctedWord.getKey().charAt(index) != incorrectWord.charAt(index+1) || ld2 > 0) {
						it.remove();
					}
				}else {
					it.remove();
				}
			}
		}
		
		return correctedWords;
	}

}
