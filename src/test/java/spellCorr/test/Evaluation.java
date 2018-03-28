package spellCorr.test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import spellCorr.candidateModel.Candidate;
import spellCorr.errorModel.DataOrganizer;
import spellCorr.lexicon.LexiconGenerator;
import spellingCorrector.Preprocessor;

public class Evaluation {
	
	private String testData;
	public int totalWords = 0;
	public int correctWords = 0;

	
	Preprocessor prePro = new Preprocessor();
	LexiconGenerator lG = new LexiconGenerator();
	DataOrganizer dataOrga = new DataOrganizer();
	
	private void compareWithTest(String errorData, String testSet) {
		testData = lG.getLoadedFileData(testSet);
		Map<String, List<String>> wordsWithLists = dataOrga.getWordwithListMap(testData);
		for(Entry<String, List<String>> entry : wordsWithLists.entrySet()) {
			List<String> list = entry.getValue();
			for(String wrongWord : list) {
				int ld = StringUtils.getLevenshteinDistance(entry.getKey(), wrongWord);
				if(ld < 3) {
					if(ld == 2) {
						if(entry.getKey().length() == wrongWord.length()) {
							int index = StringUtils.indexOfDifference(entry.getKey(), wrongWord);
							int ld2 = StringUtils.getLevenshteinDistance(entry.getKey().substring(index+2), wrongWord.substring(index+2));
							if(entry.getKey().charAt(index+1) == wrongWord.charAt(index) && 
									entry.getKey().charAt(index) == wrongWord.charAt(index+1) && ld2 > 0) {
								totalWords = totalWords + 1;
								if(errorData != null) {
									List<Candidate> candidateList = prePro.getCandidates(errorData, wrongWord);
									if(candidateList.isEmpty() == false) {
										String bestCandidate= candidateList.get(0).getPossibleWord();
										String rightWord = entry.getKey();
										if(bestCandidate.equals(rightWord)) {
											correctWords = correctWords + 1;
										}
									} 
								} else {
									String bestCandidate= prePro.getCandidateOnlyLM(wrongWord);
									if(bestCandidate.equals("") == false) {
										String rightWord = entry.getKey();
										if(bestCandidate.equals(rightWord)) {
											correctWords = correctWords + 1;
										}
									}
								}
								
							}
						}
					} else {
						totalWords = totalWords + 1;
						if(errorData != null) {
							List<Candidate> candidateList = prePro.getCandidates(errorData, wrongWord);
							if(candidateList.isEmpty() == false) {
								String bestCandidate= candidateList.get(0).getPossibleWord();
								String rightWord = entry.getKey();
								if(bestCandidate.equals(rightWord)) {
									correctWords = correctWords + 1;
								}
							} 
						} else {
							String bestCandidate= prePro.getCandidateOnlyLM(wrongWord);
							if(bestCandidate.equals("") == false) {
								String rightWord = entry.getKey();
								if(bestCandidate.equals(rightWord)) {
									correctWords = correctWords + 1;
								}
							}
						}
					}
				} 
				
			}
			
		}
		
	}
	
	public BigDecimal getResult(String errorData, String testSet) {
		compareWithTest(errorData, testSet);
		BigDecimal result = new BigDecimal(correctWords).divide(new BigDecimal(totalWords), 5, BigDecimal.ROUND_HALF_UP);
		BigDecimal percentage = result.multiply(new BigDecimal(100), new MathContext(3));
		return percentage;
	}
	
	
}
