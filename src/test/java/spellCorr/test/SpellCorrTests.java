package spellCorr.test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import spellCorr.candidateModel.Candidate;
import spellCorr.candidateModel.CandidateComparator;
import spellCorr.candidateModel.CandidateInfo;
import spellCorr.candidateModel.LevenshteinDistance;
import spellCorr.candidateModel.MaxCandidate;
import spellCorr.candidateModel.SoundexSimilarity;
import spellCorr.errorModel.DataOrganizer;
import spellCorr.errorModel.ErrorMatrix;
import spellCorr.lexicon.BigramTokenizer;
import spellCorr.lexicon.CharCounter;
import spellCorr.lexicon.LexiconGenerator;

public class SpellCorrTests {

	private LexiconGenerator lexiconGenerator; 
	private LevenshteinDistance levenDistance;
	private MaxCandidate maxCandidate;
	private SoundexSimilarity soundexSim;
	private String lexiconData;
	private String bigLexiconData;
	private String holbrookData;
	private String wikipediaData;
	private String birkbeckData;
	private String delimiter = "\\n";
	
	private Map<String, BigDecimal> testMap = new HashMap<String, BigDecimal>();
	
	//Das Lexikon einrichten 
	
	@Before
	public void setUp() {
		String fileName = "lexiconCorpora/count_big.txt";
		String fileName2 = "lexiconCorpora/big.txt";
		String fileName3 = "misspellingCorpora/holbrook-missp.dat";
		String fileName4 = "misspellingCorpora/wikipedia.dat";
		String fileName5 = "misspellingCorpora/birkbeck.dat";
		
		lexiconGenerator = new LexiconGenerator();
	    lexiconData = lexiconGenerator.getLoadedFileData(fileName);
	    testMap = lexiconGenerator.getWordCountMap(lexiconData, delimiter);
	   
	    bigLexiconData = lexiconGenerator.getLoadedFileData(fileName2);

	    holbrookData = lexiconGenerator.getLoadedFileData(fileName3);
	    wikipediaData = lexiconGenerator.getLoadedFileData(fileName4);
	    birkbeckData = lexiconGenerator.getLoadedFileData(fileName5);
	}
	
	//Testen ob lexiconData nicht null ist
	
	@Test
	public void testLexiconData() {
		Assert.assertNotNull(lexiconData);
		Assert.assertNotNull(bigLexiconData);
//		System.out.println(lexiconGenerator.getBigLexiconSize(bigLexiconData));
//		System.out.println(lexiconData.length()-1);
	}
	
	@Test
	public void testMap() {
		Assert.assertNotNull(lexiconData);
		Assert.assertTrue(testMap.containsKey("abandoned"));
//		System.out.println(testMap.size());
		
		System.out.println(testMap.get("the"));
		System.out.println(testMap.get("abandoned"));
		System.out.println(testMap.get("retreating"));

	}
	
	@Test
	public void testLevenDistance() {
		String testWord = "waether";
		levenDistance = new LevenshteinDistance();
		Map<String, BigDecimal> testCorrectWords = levenDistance.getCandidates(testWord, testMap, 2);
		Assert.assertNotNull(testCorrectWords);
		Assert.assertFalse(testCorrectWords.isEmpty());
		for (String key : testCorrectWords.keySet()) {
		   System.out.println(key);
		}
		
	}
	
	@Test
	public void testSoundex() {
		String testWord = "wether";
		soundexSim = new SoundexSimilarity();
		Map<String, BigDecimal> testCorrectWords = soundexSim.getCandidates(testWord, testMap, 4);
		Assert.assertNotNull(testCorrectWords);
		Assert.assertFalse(testCorrectWords.isEmpty());
//		for (String key : testCorrectWords.keySet()) {
//		   System.out.println(key);
//		}
		
	}
	
	@Test
	public void testMaxCandidate() {
		String testWord = "papir";
//		System.out.println(testWord.length());
		levenDistance = new LevenshteinDistance();
		Map<String, BigDecimal> testCorrectWords = levenDistance.getCandidates(testWord, testMap, 1);
		Assert.assertNotNull(testCorrectWords);
		Assert.assertFalse(testCorrectWords.isEmpty());
		maxCandidate = new MaxCandidate();
		String finalCandidate = maxCandidate.getMaxCandidate(testCorrectWords);
		Assert.assertNotNull(finalCandidate);
//		if(finalCandidate == null) {
//			System.out.println("Nichts zum korrigieren");
//		} else {
//			System.out.println(finalCandidate);
//		}
		
	}
	
	@Test
	public void testBigramtokenizer() {
		
		BigramTokenizer bigramTok = new BigramTokenizer();
		int [][] testMatrix= bigramTok.getBigramMatrix(bigLexiconData);
		Assert.assertNotNull(testMatrix);
		int frequency1 = bigramTok.getFrequency(19, 4); // te
		int frequency3 = bigramTok.getFrequency(8, 13); // in
		int frequency4 = bigramTok.getFrequency(22, 23); // wx
		Assert.assertEquals(42592, frequency1);
		Assert.assertEquals(95330, frequency3);
		Assert.assertEquals(0, frequency4);
//		System.out.println(frequency1);
//		System.out.println(frequency2);
//		System.out.println(frequency3);
//		System.out.println(frequency4);
		
	}
	
	@Test
	public void testCharCounter() {
		
		CharCounter charCounter = new CharCounter();
//		charCounter.setCharArray(bigLexiconData);
		int [] testArray = charCounter.getCharArray(bigLexiconData);
		Assert.assertNotNull(testArray);
		Assert.assertEquals(3651, charCounter.getFrequency(25));
		
//		for(int i = 0; i < testArray.length; i++) {
//			System.out.println(testArray[i]);
//		}	
	}
	
	@Test
	public void testDataOrganizer() {
		
		DataOrganizer dataOrga = new DataOrganizer();
		String[] testArray = dataOrga.dataSplitter(holbrookData);
		Assert.assertNotNull(testArray);
		Assert.assertTrue(testArray.length > 2);
//		System.out.println(testArray[2]);
		
		Map<String, List<String>> wordsWithLists = dataOrga.getWordwithListMap(holbrookData);
		Assert.assertNotNull(wordsWithLists);
		Assert.assertFalse(wordsWithLists.isEmpty());
		for(Entry<String, List<String>> entry : wordsWithLists.entrySet()) {
			System.out.println("-----");
			System.out.println(entry.getKey());
			System.out.println("List:");
			List<String> list = entry.getValue();
			Assert.assertNotNull(list);
			for(String s : list) {
				System.out.println(s);
			}
			
		}
	}
	
	@Test
	public void testErrorMatrix() {
		DataOrganizer dataOrga = new DataOrganizer();
		//Map<String, List<String>> wordsWithLists = dataOrga.getWordwithListMap(holbrookData);
		Map<String, List<String>> wordsWithLists2 = dataOrga.getWordwithListMap(wikipediaData);
		//Assert.assertFalse(wordsWithLists.isEmpty());
		Assert.assertFalse(wordsWithLists2.isEmpty());
		ErrorMatrix errorMatrix = new ErrorMatrix();
		//errorMatrix.setErrorMatrices(wordsWithLists, true);
		errorMatrix.setErrorMatrices(wordsWithLists2, false);
//		int[][] subMatrix = errorMatrix.getErrorMatrix(3);
		int[][] revMatrix = errorMatrix.getErrorMatrix(4);
//		int[][] insMatrix = errorMatrix.getErrorMatrix(1);
//		Assert.assertNotNull(subMatrix);
//		Assert.assertNotNull(delMatrix);
		Assert.assertNotNull(revMatrix);
		
		for(int i = 0; i < revMatrix.length; i++) {
			for(int j = 0; j < revMatrix[i].length; j++) {
				if(revMatrix[i][j] < 10) {
				System.out.print(revMatrix[i][j] + " |");
				} else if(revMatrix[i][j] >= 10) {
					System.out.print(revMatrix[i][j] + "|");
				}
			}
			System.out.println();
		}
	}
	
	@Test
	public void testCandidateInfo() {
		BigramTokenizer bigramTok = new BigramTokenizer();
		int[][] bigramMatrix = bigramTok.getBigramMatrix(bigLexiconData);
		CharCounter charCounter = new CharCounter();
		int[] charArray = charCounter.getCharArray(bigLexiconData);
		DataOrganizer dataOrga = new DataOrganizer();
		Map<String, List<String>> wordsWithLists = dataOrga.getWordwithListMap(birkbeckData);
		ErrorMatrix errorMatrix = new ErrorMatrix();
		errorMatrix.setErrorMatrices(wordsWithLists, false);
		int[][] subMatrix = errorMatrix.getErrorMatrix(3);
		int[][] delMatrix = errorMatrix.getErrorMatrix(2);
		int[][] insMatrix = errorMatrix.getErrorMatrix(1);
		int[][] revMatrix = errorMatrix.getErrorMatrix(4);
		Assert.assertNotNull(subMatrix);
		CandidateInfo candInfo = new CandidateInfo();
//		candInfo.getCandidatesInfo("weither", testMap, bigramMatrix, charArray, subMatrix, delMatrix, insMatrix);
//		candInfo.getCandidatesInfo("finaly", testMap, bigramMatrix, charArray, subMatrix, delMatrix, insMatrix);
//		candInfo.getCandidatesInfo("acress", testMap, bigramMatrix, charArray, subMatrix, delMatrix, insMatrix);
		candInfo.setCandidateList("hnadle", testMap, bigramMatrix, charArray, subMatrix, delMatrix, insMatrix, revMatrix);
		List<Candidate> candidateList = new ArrayList<Candidate>();
		candidateList = candInfo.getCandidateList();
		Assert.assertNotNull(candidateList);
		CandidateComparator candComp = new CandidateComparator();
		
		for(Candidate c : candidateList) {
			candComp.add(c);
		}
		candComp.sortByProbability(true);
		
		for(Candidate c : candComp) {
			System.out.println(c.getPossibleWord() + ": " + c.getNoisyChannelProb());
		}
		System.out.println("----");
		
		candInfo.printListwithPerc(candComp);
	}
	
	//Test dauert sehr lange
	@Test
	public void testEvaluation() {
		Evaluation evaluation = new Evaluation();
		BigDecimal result = evaluation.getResult(null, "misspellingCorpora/aspell.dat");
		Assert.assertNotNull(result);
		Assert.assertTrue(result.compareTo(new BigDecimal(101)) == -1);
		Assert.assertTrue(result.compareTo(BigDecimal.ZERO) == 1);
		System.out.println(result); 
		//birkbeck: 37%, 65.9% wikipedia: 36%, 63.3%, holbrook: 34%, 62.1%
		//ohne errorModel: 61.0% 
	}
	
}
