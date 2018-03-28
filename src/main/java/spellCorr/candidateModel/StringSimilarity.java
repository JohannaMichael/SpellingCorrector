package spellCorr.candidateModel;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Dieses Interface erlaubt verschiedene Arten von String-Vergleich-Implementationen. 
 */
public interface StringSimilarity {
	
	/**
	 * @param incorrectWord Falsch geschriebenes Wort
	 * @param wordProbMap Großes Lexikon mit Worthäufigkeiten
	 * @param level Entscheidet, wie strikt die Auswahl der Kandidaten sein soll 
	 */
	public Map<String, BigDecimal> getCandidates(String incorrectWord, Map<String, BigDecimal> wordProbMap, int level);
}
