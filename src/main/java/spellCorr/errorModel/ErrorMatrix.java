package spellCorr.errorModel;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import spellCorr.util.AlphabetChanger;

/**
 *@author Johanna M. 
 *
 */

/**
 *Diese Klasse soll 4 Matrizen erzeugen. Sie repräsentieren 4 Arten an
 *Fehlern. Eine Matrix aber repräsentiert alle Kombinationen von Fehlern mit ihren jeweiligen Häufigkeiten. 
 *Die Häufigkeiten werden aus den Daten der Fehler-Korpora errechnet. Der Ablauf ist ähnlich dem der Auswahl
 *an Kandidaten. 
 */
public class ErrorMatrix {
	
	private AlphabetChanger alphaChanger = new AlphabetChanger();
	
	private int [][] insertionMatrix = new int[27][26]; //1
	private int [][] deletionMatrix = new int[27][26];  //2
	private int [][] subMatrix = new int[26][26];       //3
	private int [][] reverseMatrix = new int[26][26];   //4
	
	public void setErrorMatrices(Map<String, List<String>> wordsWithLists, boolean data) {
		compareWords(wordsWithLists, data);
	}
	
	/**
	 *Hier kann die jeweilige gewünschte Matrix ausgwählt und zurückgegeben werden. 
	 */
	public int[][] getErrorMatrix(int number){
		if(number == 1) {
			return insertionMatrix;
		} else if(number == 2) {
			return deletionMatrix;
		} else if(number == 3) {
			return subMatrix;
		} else if(number == 4) {
			return reverseMatrix;
		}
		return null;
	}
	 
	/**
	 *Die Worthäufigkeit extrahieren 
	 */
	private int parseWordFreq(String wrongWord) {
		int extraFrequency = 0;
		Pattern frequencyP = Pattern.compile("(\\d+)");
		Matcher frequencyM = frequencyP.matcher(wrongWord);
		if(frequencyM.find()) {
		     extraFrequency = Integer.parseInt(frequencyM.group());
		}
		return extraFrequency;
	}
	
	/**
	 *Der String/das Wort extrahieren 
	 */
	private String parseWord(String wrongWord) {
		String word = null;
		Pattern wordP = Pattern.compile("(\\w+)");
		Matcher wordM = wordP.matcher(wrongWord);
		if(wordM.find()) {
			word = wordM.group();
		}
		return word;
	}
	
	/**
	 *Aus den Daten der Fehler-Korpora, die vorher noch in eine Map unterteilt wurden, @see spellCorr.errorModel.DataOrganizer 
	 *werden jeweils die falschen mit dem richtigen Wort verglichen. 
	 */
	private void compareWords(Map<String, List<String>> wordsWithLists, boolean data) {
		
		for(Entry<String, List<String>> entry : wordsWithLists.entrySet()) {
			String correctWord = entry.getKey();
			List<String> wrongWordsList = entry.getValue();
			
			for(String wrongWord : wrongWordsList) {
				String parsedWrongWord = parseWord(wrongWord);
					
				int ld = StringUtils.getLevenshteinDistance(correctWord, parsedWrongWord);
				if(ld == 1) {
					int classifier = findErrorClass(correctWord, parsedWrongWord);
					if(classifier == 3) {
						if(data == true) {
						int extraFrequency= parseWordFreq(wrongWord);
						putSubError(correctWord, parsedWrongWord, extraFrequency);
						} else {
							putSubError(correctWord, parsedWrongWord, 1);
						}
					} else if(classifier == 2) {
						if(data == true) {
							int extraFrequency= parseWordFreq(wrongWord);
							putDelError(correctWord, parsedWrongWord, extraFrequency);
							} else {
								putDelError(correctWord, parsedWrongWord, 1);
							}
					} else if(classifier == 1) {
						if(data == true) {
							int extraFrequency= parseWordFreq(wrongWord);
							putInsError(correctWord, parsedWrongWord, extraFrequency);
							} else {
								putInsError(correctWord, parsedWrongWord, 1);
							}
					}
				} else if(ld == 2) {
					if(correctWord.length() == parsedWrongWord.length()) {
						int index = StringUtils.indexOfDifference(correctWord, parsedWrongWord);
						int ld2 = StringUtils.getLevenshteinDistance(correctWord.substring(index+2), parsedWrongWord.substring(index+2));
						if(correctWord.charAt(index+1) == parsedWrongWord.charAt(index) && 
								correctWord.charAt(index) == parsedWrongWord.charAt(index+1) && ld2 == 0) {
							if(data == true) {
								int extraFrequency= parseWordFreq(wrongWord);
								putRevError(correctWord, parsedWrongWord, extraFrequency);
								} else {
									putRevError(correctWord, parsedWrongWord, 1);
								}
						}
					}
				}
			}
		}
	}
	
	/**
	 *Findet die Art von Fehler, wenn die Levenshtein Distanz 1 ist. 
	 */
	public int getErrorClass(String correctWord, String wrongWord) {
		int i = findErrorClass(correctWord, wrongWord);
		return i;
	}
	
	private int findErrorClass(String correctWord, String wrongWord) {
		int classifier = 0;
		if(correctWord.length() == wrongWord.length()) {
			classifier = 3;
		} else if(correctWord.length() > wrongWord.length()) {
			classifier = 2;
		} else if(correctWord.length() < wrongWord.length()) {
			classifier = 1;
		} 
		
		
		return classifier;
	}
	
	/**
	 *Gibt die Häufigkeit eines bestimmten Fehlers zurück, wenn man die genaue Reihe und Spalte weiß. 
	 *@param n1 Reihe
	 *@param n2 Spalte
	 *@param matrix Welche Matrix wiedergegeben soll, jede Matrix hat eine Nummer, siehe oben.
	 */
	public int getFrequency(int n1, int n2, int matrix) {
		if(matrix == 3) {
			return subMatrix[n1][n2];
		} else if(matrix == 2) {
			return deletionMatrix[n1][n2];
		} else if(matrix == 1) {
			return insertionMatrix[n1][n2];
		} else if(matrix == 4) {
			return reverseMatrix[n1][n2];	
		} else {
			return -1;
		}
    }
	
	/**
	 *Gibt die Häufigkeit eines bestimmten Fehlers anhand zweier Buchstaben oder Zeichen die man eingibt, zurück. 
	 *Diese werden dann in Indizes umgewandelt und so die richtige Stelle in der Matrix auffindbar gemacht. 
	 *Man beachte hier, dass jede Fehler-Matrix seine eigenen Regel hat. Siehe schriftliche Dokumentation.
	 */
	public int getFrequencyByLetters(char c1, char c2, int matrix) {
		if(matrix == 3) {
			int n1 = alphaChanger.getNumber(c1);
			int n2 = alphaChanger.getNumber(c2);
			return subMatrix[n1][n2];
		} else if(matrix == 2) {
			int n1 = alphaChanger.getNumber(c1);
			int n2 = alphaChanger.getNumber(c2);
			return deletionMatrix[n1][n2];
		} else if(matrix == 1) {
			int n1 = alphaChanger.getNumber(c1);
			int n2 = alphaChanger.getNumber(c2);
			return insertionMatrix[n1][n2];
		} else if(matrix == 4) {
			int n1 = alphaChanger.getNumber(c1);
			int n2 = alphaChanger.getNumber(c2);
			return reverseMatrix[n1][n2];	
		} else {
			return -1;
		}
	}
	
	/**
	 *Hier kann die Häufigkeit eines Fehlers einer Matrix herausgefunden werden, die nicht in der jetzigen Klasse ist.
	 */
	public int getFrequencyOfMatrix(int n1, int n2, int[][] matrix) {
			return matrix[n1][n2];
    }
	
	private void putSubError(String correctWord, String wrongWord, int extraFreq) {
		
		int index = StringUtils.indexOfDifference(correctWord, wrongWord);
		char rightLetter = correctWord.charAt(index);
		char wrongLetter = wrongWord.charAt(index);
		
		int numberRight = alphaChanger.getNumber(rightLetter);
		int numberWrong = alphaChanger.getNumber(wrongLetter);
		
		if(numberRight != 100 && numberWrong != 100) {
			
			if(subMatrix[numberRight][numberWrong] == 0) {
				subMatrix[numberRight][numberWrong] = extraFreq;
				
			} else {
				int frequency = getFrequency(numberRight, numberWrong, 3);
				subMatrix[numberRight][numberWrong] = frequency + extraFreq;
			}
			}
	}

	
	private void putDelError(String correctWord, String wrongWord, int extraFreq) {
		char previousLetter;
		
		int index = StringUtils.indexOfDifference(correctWord, wrongWord);
		if(index == 0) {
			 previousLetter = ' ';
		} else {
			 previousLetter = correctWord.charAt(index-1);
		}
		char rightLetter = correctWord.charAt(index);
		
		int numberPrevious = alphaChanger.getNumber(previousLetter);
		int numberRight = alphaChanger.getNumber(rightLetter);
		
		if(numberRight != 100 && numberPrevious != 100) {
			
			if(deletionMatrix[numberPrevious][numberRight] == 0) {
				deletionMatrix[numberPrevious][numberRight] = extraFreq;
				
			} else {
				int frequency = getFrequency(numberPrevious, numberRight, 2);
				deletionMatrix[numberPrevious][numberRight] = frequency + extraFreq;
			}
			}
	}
	
	
	private void putInsError(String correctWord, String wrongWord, int extraFreq) {
		char previousLetter;
		
		int index = StringUtils.indexOfDifference(correctWord, wrongWord);
		if(index == 0) {
			 previousLetter = ' ';
		} else {
			 previousLetter = wrongWord.charAt(index-1);
		}
		char wrongLetter = wrongWord.charAt(index);
		
		int numberPrevious = alphaChanger.getNumber(previousLetter);
		int numberWrong = alphaChanger.getNumber(wrongLetter);
		
		if(numberWrong != 100 && numberPrevious != 100) {
			
			if(insertionMatrix[numberPrevious][numberWrong] == 0) {
				insertionMatrix[numberPrevious][numberWrong] = extraFreq;
				
			} else {
				int frequency = getFrequency(numberPrevious, numberWrong, 1);
				insertionMatrix[numberPrevious][numberWrong] = frequency + extraFreq;
			}
			}
	}
	
    private void putRevError(String correctWord, String wrongWord, int extraFreq) {
		
		int index = StringUtils.indexOfDifference(correctWord, wrongWord);
		char firstLetter = correctWord.charAt(index);
		char secondLetter = correctWord.charAt(index+1);
		
		int numberFirst = alphaChanger.getNumber(firstLetter);
		int numberSecond = alphaChanger.getNumber(secondLetter);
		
		if(numberFirst != 100 && numberSecond != 100) {
			
			if(reverseMatrix[numberFirst][numberSecond] == 0) {
				reverseMatrix[numberFirst][numberSecond] = extraFreq;
				
			} else {
				int frequency = getFrequency(numberFirst, numberSecond, 4);
				reverseMatrix[numberFirst][numberSecond] = frequency + extraFreq;
			}
			}
	}
}
