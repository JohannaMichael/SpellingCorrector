package spellCorr.errorModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Johanna M.
 *
 */

/**
 * Diese Klasse organisiert und unterteilt die Fehler-Korpora in eine Map mit einer Liste an richtig geschriebenen Wörtern. Jedes richtige 
 * Wort hat jeweils noch eine Liste mit seinen falsch geschriebenen Versionen(Wörtern). Bei den Daten von David Holbrook gibt es 
 * zu jedem falschen Wort auch noch seine Häufigkeit.
 */
public class DataOrganizer {
	
	/**
	 *Unterteilt die Daten in ein Array. Bei jedem "$" wird unterteilt. 
	 */
	public String[] dataSplitter(String data) {
		String[] wordsList= data.split("\\$");
		return wordsList;
	}
	
	public Map<String, List<String>> getWordwithListMap(String data){
		Map<String, List<String>> wordsWithLists = entrySplitter(data);
		return wordsWithLists;
	}
	
	/**
	 *Unterteilt die Daten weiter in eine Map. 
	 */
	private Map<String, List<String>> entrySplitter(String data){
		
		Map<String, List<String>> entryWord = new HashMap<String, List<String>>();
		List <String> wordsList = new ArrayList<String>();
		
		String[] wordsarray = dataSplitter(data);
		
		for(int i = 1; i < wordsarray.length; i++) {
			String s = wordsarray[i];
			wordsList.add(s);
		}
		
		for(String w: wordsList) {
			List<String> wordWithCountList = new ArrayList<String>();
			String[] oneWordArray = w.split("\\n");	
			for(int i = 0; i < oneWordArray.length; i++) {
				wordWithCountList.add(oneWordArray[i]);
			}
			wordWithCountList.remove(0);
			entryWord.put(oneWordArray[0], wordWithCountList);
		}
		
		
		return entryWord;
	}

}
