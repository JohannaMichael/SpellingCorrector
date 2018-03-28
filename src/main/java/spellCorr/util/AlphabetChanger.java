package spellCorr.util;

/**
 * Wandelt einen Buchstaben oder ein Whitespace in eine bestimmte Zahl um. Wenn keins davon zutrifft, 
 * wird die Zahl 100 zurückgegeben. 
 */
public class AlphabetChanger {

	public int getNumber(char letter) {
		
		switch(letter) {
        case 'a': return 0;
        case 'b': return 1;
        case 'c': return 2;
        case 'd': return 3;
        case 'e': return 4;
        case 'f': return 5;
        case 'g': return 6;
        case 'h': return 7;
        case 'i': return 8;
        case 'j': return 9;
        case 'k': return 10;
        case 'l': return 11;
        case 'm': return 12;
        case 'n': return 13;
        case 'o': return 14;
        case 'p': return 15;
        case 'q': return 16;
        case 'r': return 17;
        case 's': return 18;
        case 't': return 19;
        case 'u': return 20;
        case 'v': return 21;
        case 'w': return 22;
        case 'x': return 23;
        case 'y': return 24;
        case 'z': return 25;
        case ' ': return 26;
        
        
        default: return 100;
	}
	}
}
