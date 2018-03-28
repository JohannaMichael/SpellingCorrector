package spellCorr.candidateModel;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Ohne ein Fehlermodell wird nur nach dem Language Model gegangen. Diese Klasse wählt das Wort mit 
 * der höchsten Häufigkeit im englischen Korpus big.txt aus.
 */
public class MaxCandidate {
	
	public String getMaxCandidate(Map<String, BigDecimal> candidatesMap) {
		String maxCandidate = "";
		if(candidatesMap.isEmpty()) {
			return maxCandidate;
		}else {
			maxCandidate = findingMaxCandidate(candidatesMap);
			return maxCandidate;
		}
	}
	
	private String findingMaxCandidate(Map<String, BigDecimal> candidatesMap) {
		String maxCandidate = null;
		BigDecimal maxValue= Collections.max(candidatesMap.values());
		for(Entry<String, BigDecimal> entry : candidatesMap.entrySet()) {
			if (entry.getValue() == maxValue) {
                maxCandidate = entry.getKey();     
            }
		}
		return maxCandidate;
	}
}
