package spellCorr.lexicon;

import spellCorr.util.AlphabetChanger;
/**
 * @author Johanna M.
 *
 */

/**
 * Der Bigram Tokenizer unterteilt den lexikalischen Korpus "big.txt" in Paare von Zeichen. Die Häufigkeiten werden wiederum
 * in einer Matrix gespeichert. Das "erste" Zeichen wird in der Reihe gespeichert. Das "zweite" Zeichen in der Spalte.
 */
public class BigramTokenizer {

	private String[] words;
	private int[][] bigramMatrix = new int[27][27];
	private AlphabetChanger alphaChanger = new AlphabetChanger();
    
    public String[] getWords() {
    	return words;
    }
    
    /**
     * Diese Methode unterteilt big.txt in einzelne Wörter. Durch iterateWordArray() wird über
     * jedes einzelne Wort und daraufhin über jedes Zeichen iteriert. 
     * @param str der Korpus, big.txt 
     */
    public void setBigramMatrix(String str) {
    	this.words = str.split(" ");
    	iterateWordArray(words);
    }
    
    public int[][] getBigramMatrix(String str){
    	this.words = str.split(" ");
    	iterateWordArray(words);
		return bigramMatrix;
    }
    
    private void iterateWordArray(String[] words) {
    	for(int i = 0; i < words.length; i++) {
    		iterateWord(words[i]);
    	}
    }
    
    /**
     * Gibt die Häufigkeit eines bestimmten Bigrams zurück, wenn man die genaue Reihe und Spalte weiß. 
     */
    public int getFrequency(int n1, int n2) {
    	return bigramMatrix[n1][n2];
    }
    
    /**
	 *Gibt die Häufigkeit eines bestimmten Bigrams anhand zweier Buchstaben oder Zeichen die man eingibt, zurück. 
	 *Diese werden dann in Indizes umgewandelt und so die richtige Stelle in der Matrix auffindbar gemacht. 
	 */
    public int getFrequencybyChars(char c1, char c2) {
    	int n1 = alphaChanger.getNumber(c1);
    	int n2 = alphaChanger.getNumber(c2);
    	return bigramMatrix[n1][n2];
    }
    
    /**
	 *Hier kann die Häufigkeit eines Bigrams in der Matrix herausgefunden werden, die nicht in der jetzigen Klasse ist.
	 */
    public int getFrequencyOfMatrix(int n1, int n2, int[][] bigramMatrix) {
    	return bigramMatrix[n1][n2];
    }
    
    /**
	 *Hier wird ein einzelnes Wort in Bigramme unterteilt und in die Matrix einsortiert. 
	 *@param word Das einzelne Wort
	 */
    private void iterateWord(String word) {
    	for(int i = 0; i < word.length(); i++) {
    		if(i == 0) {
    			int number1 = alphaChanger.getNumber(' ');
    			int number2 = alphaChanger.getNumber(word.charAt(i));
    			int number3 = 0;
    			if(i+1 < word.length()) {
    			number3 = alphaChanger.getNumber(word.charAt(i+1));
    			}
    			
    			if(number1 != 100 && number2 != 100) {
    				
    			if(bigramMatrix[number1][number2] == 0) {
    				bigramMatrix[number1][number2] = 1;
    				
    			} else {
    				int frequency = getFrequency(number1, number2);
    				bigramMatrix[number1][number2] = frequency + 1;
    			}
    			}
    			
    			if(number2 != 100 && number3 != 100) {
    			if(bigramMatrix[number2][number3] == 0) {
    				bigramMatrix[number2][number3] = 1;
    			} else {
    				int frequency = getFrequency(number2, number3);
    				bigramMatrix[number2][number3] = frequency + 1;
    			}
    			}
    		} else if(i == word.length()-1) {
    			int number1 = alphaChanger.getNumber(word.charAt(i));
    			int number2 = alphaChanger.getNumber(' ');
    			if(number1 != 100 && number2 != 100) {
    			if(bigramMatrix[number1][number2] == 0) {
    				bigramMatrix[number1][number2] = 1;
    			}else {
    				int frequency = getFrequency(number1, number2);
    				bigramMatrix[number1][number2] = frequency + 1;
    			}
    			}
    		} else {
    			int number1 = alphaChanger.getNumber(word.charAt(i));
    			int number2 = alphaChanger.getNumber(word.charAt(i+1));
    			if(number1 != 100 && number2 != 100) {
    			if(bigramMatrix[number1][number2] == 0) {
    				bigramMatrix[number1][number2] = 1;
    			}else {
    				int frequency = getFrequency(number1, number2);
    				bigramMatrix[number1][number2] = frequency + 1;
    			}
    			}
    		}
    	}
    }

}
