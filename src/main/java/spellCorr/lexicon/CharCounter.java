package spellCorr.lexicon;

import spellCorr.util.AlphabetChanger;

/**
 * Der CharCounter unterteilt den lexikalischen Korpus "big.txt" in einzelne Zeichen. Die Häufigkeiten werden wiederum
 * in einem Array abgespeichert. Jedes Zeichen hat einen bestimmten Index im Array. @see spellCorr.util.AlphabetChanger
 */
public class CharCounter {
	
	private int[] charArray = new int[27];
	AlphabetChanger alphaChanger = new AlphabetChanger();
	
	
	public void setCharArray(String bigText) {
		createCharArray(bigText);
	}
	
	public int[] getCharArray(String bigText) {
		createCharArray(bigText);
		return charArray;
	}
	
	public int getFrequency(int number) {
		return charArray[number];
	}
	
	public int getFrequencyOfArray(int number, int[] charArray) {
		return charArray[number];
	}
	
	private void createCharArray(String bigText) {
		
		for(int i = 0; i < bigText.length(); i++) {
			char c = bigText.charAt(i);
			int numberForChar = alphaChanger.getNumber(c);
			if(numberForChar != 100) {
				if(charArray[numberForChar] == 0) {
					charArray[numberForChar] = 1;
				} else {
					int frequency = getFrequency(numberForChar);
					charArray[numberForChar] = frequency + 1;
				}
			}
		}
	}

}
