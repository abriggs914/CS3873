/**
 * A Vigenere Square or Vigenere table consists of the alphabet written out 26
 * times in different rows, each alphabet shifted cyclically to the left
 * compared to the previous alphabet, corresponding to the 26 possible Caesar
 * ciphers, At different points in the encryption process, the cipher uses a
 * different alphabet from one of the rows. The alphabet used at each point
 * depends on a repeating keyword.
 */
public class VigenereSquare {

    /**
     * A 2D char array representing the shifted alphabets.
     */
    public static final char[][] SQUARE = fillSquare();

    private static final int LETTERS_IN_ALPHABET = 26;
    private static final int ASCII_RANGE = 256;

    public VigenereSquare() {}

    /**
     * Fill square with shifted alphabets in ASCII positions:
     *  'a' = 97 .. 'z' = 122
     * @return initialised char[][]
     */
    public static char[][] fillSquare() {
        char[][] square = new char[ASCII_RANGE][ASCII_RANGE];
        int start = 'a';
        int end = start + (LETTERS_IN_ALPHABET - 1);
        int index = start;
        for (int i = start; i <= end; i++) {
            for (int j = start; j <= end; j++) {
                //Check index position if beyond the range of the alphabet
                //reset index position to start.
                if (index > end) {
                    index = start;
                }
                square[i][j] = (char) index;
                index++;
            }
            index = i + 1;
        }
        return square;
    }
}
