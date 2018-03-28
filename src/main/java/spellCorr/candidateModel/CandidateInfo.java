package spellCorr.candidateModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import spellCorr.errorModel.ErrorMatrix;
import spellCorr.lexicon.BigramTokenizer;
import spellCorr.lexicon.CharCounter;
import spellCorr.util.AlphabetChanger;

/**
 * Die Klasse CandidateInfo erstellt eine Liste von Kandidaten. 
 * Zusätzlich werden alle nötigen Informationen für die Kandidaten rausgesucht und abgespeichert.
 */
public class CandidateInfo {
	
	private LevenshteinDistance levenDistance = new LevenshteinDistance();
	private ErrorMatrix errorMatrix = new ErrorMatrix();
	private AlphabetChanger alphaChanger = new AlphabetChanger();
	private CharCounter charCount = new CharCounter();
	private BigramTokenizer bigramTok = new BigramTokenizer();
	public CandidateComparator candComp;
	
	/**
	 * Erstellte Liste an Kandidaten 
	 * @see spellCorr.candidateModel.Candidate
	 */
	private List<Candidate> candidateList = new ArrayList<Candidate>();
	
	public List<Candidate> getCandidateList(){
		return candidateList;
	}
	
	/**
	 * Errechnet die Wahrscheinlichkeiten jedes Kandidats in Prozent und gibt sie aus
	 */
	public void printListwithPerc(List<Candidate> candidates) {
		BigDecimal sum = BigDecimal.ZERO;
		BigDecimal c2Sum = null;
		BigDecimal c2Perc;
		for(Candidate c : candidates) {
			sum = sum.add(c.getNoisyChannelProb());
		}
		for(Candidate c2 : candidates) {
			if(sum.compareTo(BigDecimal.ZERO) == 0) {
				c2Perc = new BigDecimal(0);
			} else {
			c2Sum = c2.getNoisyChannelProb().divide(sum, 3, BigDecimal.ROUND_HALF_UP);
			c2Perc = c2Sum.multiply(new BigDecimal(100), new MathContext(3));
			}
			System.out.println(c2.getPossibleWord() + ": " + c2Perc + "%");
		}
	}
	
	public void setCandidateList(String incorrectWord, Map<String, BigDecimal> wordProbMap, int[][] bigramMatrix, 
			int[] charArray, int[][] subMatrix, int[][] delMatrix, int[][] insMatrix, int[][] revMatrix) {
		setInfoCandidates(incorrectWord, wordProbMap, bigramMatrix, charArray, subMatrix, delMatrix, insMatrix, revMatrix);
	}
	
	
	/**
	 * Eine vollständige Ausgabe aller Informationen von allen Kandidaten, nicht nach Wahrscheinlichkeit sortiert.
	 */
	public void getCandidatesInfo(String incorrectWord, Map<String, BigDecimal> wordProbMap, 
			int[][] bigramMatrix, int[] charArray, int[][] subMatrix, int[][] delMatrix, int[][] insMatrix, int[][] revMatrix) {
		setInfoCandidates(incorrectWord, wordProbMap, bigramMatrix, charArray, subMatrix, delMatrix, insMatrix, revMatrix);
		for(Candidate c : candidateList) {
			System.out.println(c.toString());
		}
	}
	
	/**
	 * Diese Methode bringt alle nötigen Informationen für die Kandidaten zusammen. Jeder Kandidat wird 
	 * mit dem falsch geschriebenen Wort nach der Levenshtein-Distanz verglichen. Es wird auch geschaut, welche Art von Fehler 
	 * begangen wurde, um dann in den jeweiligen Matrizen zu suchen. 
	 * @param incorrectWord
	 * @param wordProbMap Großes Lexikon mit Worthäufigkeiten
	 * @param bigramMatrix Häufigkeit von Bigrams
	 * @param charArray Häufigkeit jedes Buchstabens 
	 * @param subMatrix Häufigkeit eines Substitution Fehlers
	 * @param delMatrix Häufigkeit eines Deletion Fehlers
	 * @param insmatrix Häufigkeit eines Insertion Fehlers
	 * @param revMatrix Häufigkeit eines Transposition/Reverse Fehlers
	 */
	private void setInfoCandidates(String incorrectWord, Map<String, BigDecimal> wordProbMap, int[][] bigramMatrix, int[] charArray, 
		int[][] subMatrix, int[][] delMatrix, int[][] insMatrix, int[][] revMatrix) {
		Map<String, BigDecimal> possibleCand = levenDistance.getCandidates(incorrectWord, wordProbMap, 2);
		
		for(Map.Entry<String, BigDecimal> entry : possibleCand.entrySet()){
			int ld = StringUtils.getLevenshteinDistance(entry.getKey(), incorrectWord);
			if(ld == 1) {
			Candidate cand = new Candidate();
			cand.setPossibleWord(entry.getKey());
			cand.setlMProbability(entry.getValue());
			cand.errorClass = errorMatrix.getErrorClass(entry.getKey(), incorrectWord);
			if(cand.errorClass == 3) {
				int subFreq = getSubError(entry.getKey(), incorrectWord, subMatrix);
				cand.setErrorFreq(new BigDecimal(subFreq));
				int charFreq = getCharFreq(entry.getKey(), incorrectWord, cand.errorClass, charArray, bigramMatrix);
				cand.setCharFreq(new BigDecimal(charFreq));
				cand.setErrorProbability(cand.getErrorFreq(), cand.getCharFreq());
				cand.setNoisyChannelProb(cand.getlMProbability(), cand.getErrorProbability());
			} else if(cand.errorClass == 2) {
				int delFreq= getDelError(entry.getKey(), incorrectWord, delMatrix);
				cand.setErrorFreq(new BigDecimal(delFreq));
				int charFreq = getCharFreq(entry.getKey(), incorrectWord, cand.errorClass, charArray, bigramMatrix);
				cand.setCharFreq(new BigDecimal(charFreq));
				cand.setErrorProbability(cand.getErrorFreq(), cand.getCharFreq());
				cand.setNoisyChannelProb(cand.getlMProbability(), cand.getErrorProbability());
			} else if(cand.errorClass == 1) {
				int insFreq= getInsError(entry.getKey(), incorrectWord, insMatrix);
				cand.setErrorFreq(new BigDecimal(insFreq));
				int charFreq = getCharFreq(entry.getKey(), incorrectWord, cand.errorClass, charArray, bigramMatrix);
				cand.setCharFreq(new BigDecimal(charFreq));
				cand.setErrorProbability(cand.getErrorFreq(), cand.getCharFreq());
				cand.setNoisyChannelProb(cand.getlMProbability(), cand.getErrorProbability());
			}
			candidateList.add(cand);
			} else if(ld == 2) {
				Candidate cand = new Candidate();
				cand.setPossibleWord(entry.getKey());
				cand.setlMProbability(entry.getValue());
				
				int revFreq = getRevError(entry.getKey(), incorrectWord, revMatrix);
				cand.setErrorFreq(new BigDecimal(revFreq));
				int charFreq = getCharFreq(entry.getKey(), incorrectWord, 4, charArray, bigramMatrix);
				cand.setCharFreq(new BigDecimal(charFreq));
				cand.setErrorProbability(cand.getErrorFreq(), cand.getCharFreq());
				cand.setNoisyChannelProb(cand.getlMProbability(), cand.getErrorProbability());
				
				candidateList.add(cand);
			}
		}
		
	}
	
	/**
	 * Sucht in der Substitution Matrix nach der Häufigkeit dieses bestimmten Fehlers
	 */
	private int getSubError(String correctWord, String wrongWord, int[][] subMatrix) {
		int subFreq = 0;
		int index = StringUtils.indexOfDifference(correctWord, wrongWord);
		char rightLetter = correctWord.charAt(index);
		char wrongLetter = wrongWord.charAt(index);
		
		int numberRight = alphaChanger.getNumber(rightLetter);
		int numberWrong = alphaChanger.getNumber(wrongLetter);
		
		if(numberRight != 100 && numberWrong != 100) {
			subFreq = errorMatrix.getFrequencyOfMatrix(numberRight, numberWrong, subMatrix);
		}
		return subFreq;
	}
	
	/**
	 * Sucht in der Deletion Matrix nach der Häufigkeit dieses bestimmten Fehlers
	 */
	private int getDelError(String correctWord, String wrongWord, int[][] delMatrix) {
		int delFreq = 0;
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
			delFreq = errorMatrix.getFrequencyOfMatrix(numberPrevious, numberRight, delMatrix);
		}
		
		return delFreq;
	}
	
	/**
	 * Sucht in der Insertion Matrix nach der Häufigkeit dieses bestimmten Fehlers
	 */
	private int getInsError(String correctWord, String wrongWord, int[][] insMatrix) {
		int insFreq = 0;
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
			insFreq = errorMatrix.getFrequencyOfMatrix(numberPrevious, numberWrong, insMatrix);
		}
		return insFreq;
	}
	
	/**
	 * Sucht in der Transposition Matrix nach der Häufigkeit dieses bestimmten Fehlers
	 */
	private int getRevError(String correctWord, String wrongWord, int[][] revMatrix) {
		int revFreq = 0;
		int index = StringUtils.indexOfDifference(correctWord, wrongWord);
		char firstLetter = correctWord.charAt(index);
		char secondLetter = correctWord.charAt(index+1);
		
		int numberFirst = alphaChanger.getNumber(firstLetter);
		int numberSecond = alphaChanger.getNumber(secondLetter);
		
		if(numberFirst != 100 && numberSecond != 100) {
			revFreq = errorMatrix.getFrequencyOfMatrix(numberFirst, numberSecond, revMatrix);
		}
		return revFreq;
	}
	
	/**
	 * Sucht nach der Häufigkeit eines bestimmten Buchstabens und Buchstaben Sequenz (Bigram)
	 */
	private int getCharFreq(String correctWord, String wrongWord, int errorClass, int[] charArray, int[][] bigramMatrix) {
		int charFreq = 0;
		int index = StringUtils.indexOfDifference(correctWord, wrongWord);
		if(errorClass == 3) {
			char rightLetter = correctWord.charAt(index);
			int numberRight = alphaChanger.getNumber(rightLetter);
			if(numberRight != 100) {
			charFreq = charCount.getFrequencyOfArray(numberRight, charArray);
			}
		} else if(errorClass == 2) {
			char previousLetter;
			if(index == 0) {
				 previousLetter = ' ';
			} else {
				 previousLetter = correctWord.charAt(index-1);
			}
			char rightLetter = correctWord.charAt(index);
			
			int numberPrevious = alphaChanger.getNumber(previousLetter);
			int numberRight = alphaChanger.getNumber(rightLetter);
			
			if(numberRight != 100 && numberPrevious != 100) {
				charFreq = bigramTok.getFrequencyOfMatrix(numberPrevious, numberRight, bigramMatrix);
			}
		} else if(errorClass == 1) {
			char previousLetter;
			if(index == 0) {
				 previousLetter = ' ';
			} else {
				 previousLetter = correctWord.charAt(index-1);
			}
			int numberPrevious = alphaChanger.getNumber(previousLetter);
			
			if(numberPrevious != 100) {
				charFreq = charCount.getFrequencyOfArray(numberPrevious, charArray);
			}
		} else if(errorClass == 4) {
			char firstLetter = correctWord.charAt(index);
			char secondLetter = correctWord.charAt(index+1);
			int numberFirst = alphaChanger.getNumber(firstLetter);
			int numberSecond = alphaChanger.getNumber(secondLetter);
			if(numberFirst != 100 && numberSecond != 100) {
			charFreq = bigramTok.getFrequencyOfMatrix(numberFirst, numberSecond, bigramMatrix);
			}
		}
		return charFreq;
		
	}
	
	
}
