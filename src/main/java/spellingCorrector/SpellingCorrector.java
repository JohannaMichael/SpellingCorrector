package spellingCorrector;

import java.util.List;

import spellCorr.candidateModel.Candidate;
import spellCorr.candidateModel.CandidateInfo;

public class SpellingCorrector {

	private ConsoleInput console;
	private String input;

	public SpellingCorrector() {
		console = new ConsoleInput();
	}

	public void run() {

		while (true) {
				System.out.println("Type any wrong word you like. If you want to exit, just write 'exit!' (we'll help you "
						+ "if you mistype it...).");
				    input = console.input();
				    if (input.equalsIgnoreCase("exit!")) {
						console.close();
						break;
					}
					Preprocessor prePro = new Preprocessor();
					String data = "birkbeck";
					String wrongWord = input;
					List<Candidate> candidateList= prePro.getCandidates(data, wrongWord);
					CandidateInfo candidateInfo = new CandidateInfo();
					if(candidateList == null || candidateList.isEmpty()) {
						System.out.println("Your word is either written correctly, or there are no right words found in the lexicon.");
					}else {
						candidateInfo.printListwithPerc(candidateList);
					}
					
		}
	}
}
