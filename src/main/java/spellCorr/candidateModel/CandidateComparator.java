package spellCorr.candidateModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sortiert die List von Kandidaten nach ihrer Wahrscheinlichkeit.
 */
public class CandidateComparator extends ArrayList<Candidate>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3675676838162351830L;
	
	public void sortByProbability(boolean descendingOrder) {
		sortProbability(descendingOrder);
	}
	
	
	private void sortProbability(boolean descendingOrder) {

		
		Comparator<Candidate> probabilityComp = new Comparator<Candidate>() {
			
			public int compare(Candidate c1, Candidate c2) {
				int prob = c1.getNoisyChannelProb().compareTo(c2.getNoisyChannelProb());
					return prob;
				
			}
		};
		if(descendingOrder == true){
		Collections.sort(this, probabilityComp.reversed());
		}else{
			Collections.sort(this, probabilityComp);
		}

}
	

}
