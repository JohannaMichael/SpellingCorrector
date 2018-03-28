package spellCorr.lexicon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Der LexiconGenerator liest Text Dateien ein. Außerdem wird hier das Lexikon, mit Wahrscheinlichkeiten, erstellt.
 */
public class LexiconGenerator {
	
	BigDecimal bigLexiconSize;
	
	/**
	 * Das Lexikon wird in einer Map gespeichert. Jedes Wort hat eine Wahrscheinlichkeit, 
	 * dass es im Korpus, big.txt, vorkommt. Jedes Wort kriegt zudem immer +1 Häufigkeit, damit die Wahrscheinlichkeit 
	 * nicht 0 ist. Die Worthäufigkeit jedes Wortes wird immer durch 1036512 geteilt, da big.txt circa so viele Wörter hat.   
	 */
	public Map<String, BigDecimal> getWordCountMap(String lexiconData, String delimiter){
		Map<String, BigDecimal> wordCountMap = createWordCountMap(lexiconData, delimiter);
		return wordCountMap;
	}
	
	private Map<String, BigDecimal> createWordCountMap(String lexiconData, String delimiter){
		Pattern frequencyP = Pattern.compile("(\\d+)");
		Pattern wordP = Pattern.compile("(\\w+)");
		
		BigDecimal totalWords = new BigDecimal(1036512);
		
		Map<String, BigDecimal> wordCount = new HashMap<String, BigDecimal>();
		
		String[] splittedLexicon = splitLexicon(lexiconData, delimiter);
		
		for(int i = 0; i < splittedLexicon.length; i++) {
			
			Matcher wordMatcher = wordP.matcher(splittedLexicon[i]);
			Matcher frequencyMatcher = frequencyP.matcher(splittedLexicon[i]);
			
			while(wordMatcher.find() && frequencyMatcher.find()) {
				int frequency = Integer.parseInt(frequencyMatcher.group());
				BigDecimal probability = new BigDecimal(frequency+1);
				BigDecimal bd = probability.divide(totalWords,10,BigDecimal.ROUND_HALF_UP);
				wordCount.put(wordMatcher.group(), bd);
			}
		}
		
		
		return wordCount;
	}
	
	/**
	 * Gibt die Zahl der Wörter in big.txt wieder.   
	 */
	public int getBigLexiconSize(String bigLexiconData) {
		String[] dataArray = bigLexiconData.split(" ");
		this.bigLexiconSize = new BigDecimal(dataArray.length);
		return dataArray.length;
	}
	
	private String[] splitLexicon(String lexiconData, String delimiter) {
		
		String[] splittedLexicon = lexiconData.split(delimiter);
		return splittedLexicon;
	}
	
	/**
	 * Ließt die jeweilige Datei ein und gibt sie in einem String wieder zurück.   
	 */
	public String getLoadedFileData(String fileName){
		String text = loadFileData(fileName);
		return text;
	}
	

	private String loadFileData(String fileName) {
		FileInputStream in = null;
		File file = new File(fileName);
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(in, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedReader bufferedReader = new BufferedReader(isr);
		StringBuffer stringBuffer = new StringBuffer();

		String currentLine;
		try {
			while ((currentLine = bufferedReader.readLine()) != null) {
				stringBuffer.append(currentLine);
				stringBuffer.append("\n"); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		String text = stringBuffer.toString();
		return text;
}
}
