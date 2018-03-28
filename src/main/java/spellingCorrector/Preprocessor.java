package spellingCorrector;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spellCorr.candidateModel.Candidate;
import spellCorr.candidateModel.CandidateComparator;
import spellCorr.candidateModel.CandidateInfo;
import spellCorr.candidateModel.LevenshteinDistance;
import spellCorr.candidateModel.MaxCandidate;
import spellCorr.errorModel.DataOrganizer;
import spellCorr.errorModel.ErrorMatrix;
import spellCorr.lexicon.BigramTokenizer;
import spellCorr.lexicon.CharCounter;
import spellCorr.lexicon.LexiconGenerator;

public class Preprocessor {

	private LexiconGenerator lexiconGenerator; 
	private String lexiconData;
	private String bigLexiconData;
	private String holbrookData;
	private String wikipediaData;
	private String birkbeckData;
	private String delimiter = "\\n";
	private LevenshteinDistance levenDistance;
	private MaxCandidate maxCandidate;
	
	String fileName = "lexiconCorpora/count_big.txt";
	String fileName2 = "lexiconCorpora/big.txt";
	String fileName3 = "misspellingCorpora/holbrook-missp.dat";
	String fileName4 = "misspellingCorpora/wikipedia.dat";
	String fileName5 = "misspellingCorpora/birkbeck.dat";  
	private Map<String, BigDecimal> testMap = new HashMap<String, BigDecimal>();
	
	public List<Candidate> getCandidates(String errorData, String wrongWord){
		setUp();
		return process(errorData, wrongWord);
	}
	
	public String getCandidateOnlyLM(String wrongWord) {
		setUp();
		Map<String, BigDecimal> testCorrectWords = new HashMap<String, BigDecimal>();
		testCorrectWords = levenDistance.getCandidates(wrongWord, testMap, 2);
		maxCandidate = new MaxCandidate();
		String finalCandidate = maxCandidate.getMaxCandidate(testCorrectWords);
		return finalCandidate;
	}
	
	private void setUp() {
		lexiconGenerator = new LexiconGenerator();
		levenDistance = new LevenshteinDistance();
		 lexiconData = lexiconGenerator.getLoadedFileData(fileName);
		    testMap = lexiconGenerator.getWordCountMap(lexiconData, delimiter);
		   
		    bigLexiconData = lexiconGenerator.getLoadedFileData(fileName2);

		    holbrookData = lexiconGenerator.getLoadedFileData(fileName3);
		    wikipediaData = lexiconGenerator.getLoadedFileData(fileName4);
		    birkbeckData = lexiconGenerator.getLoadedFileData(fileName5);
	}
	
	private List<Candidate> process(String errorData, String wrongWord) {
		BigramTokenizer bigramTok = new BigramTokenizer();
		int[][] bigramMatrix = bigramTok.getBigramMatrix(bigLexiconData);
		CharCounter charCounter = new CharCounter();
		int[] charArray = charCounter.getCharArray(bigLexiconData);
		DataOrganizer dataOrga = new DataOrganizer();
		Map<String, List<String>> wordsWithLists = null;
		ErrorMatrix errorMatrix = new ErrorMatrix();
		if(errorData.equalsIgnoreCase("birkbeck")) {
			wordsWithLists = dataOrga.getWordwithListMap(birkbeckData);
			errorMatrix.setErrorMatrices(wordsWithLists, false);
		} else if(errorData.equalsIgnoreCase("holbrook")) {
			wordsWithLists = dataOrga.getWordwithListMap(holbrookData);
			errorMatrix.setErrorMatrices(wordsWithLists, true);
		} else if(errorData.equalsIgnoreCase("wikipedia")) {
			wordsWithLists = dataOrga.getWordwithListMap(wikipediaData);
			errorMatrix.setErrorMatrices(wordsWithLists, false);
		}
		int[][] subMatrix = errorMatrix.getErrorMatrix(3);
		int[][] delMatrix = errorMatrix.getErrorMatrix(2);
		int[][] insMatrix = errorMatrix.getErrorMatrix(1);
		int[][] revMatrix = errorMatrix.getErrorMatrix(4);
		CandidateInfo candInfo = new CandidateInfo();
		candInfo.setCandidateList(wrongWord, testMap, bigramMatrix, charArray, subMatrix, delMatrix, insMatrix, revMatrix);
		List<Candidate> candidateList = new ArrayList<Candidate>();
		candidateList = candInfo.getCandidateList();
		CandidateComparator candComp = new CandidateComparator();
		
		for(Candidate c : candidateList) {
			candComp.add(c);
		}
		candComp.sortByProbability(true);
		return candComp;
	}

}
