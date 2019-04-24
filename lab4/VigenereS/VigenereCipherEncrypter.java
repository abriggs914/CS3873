/**
 * Using a VigenereSquare and a CipherKey each row starts with a key letter. The
 * remainder of the row holds the letters A to Z (in shifted order). Although
 * there are 26 key rows shown, you will only use as many keys (different
 * alphabets) as there are unique letters in the key string, here just 5 keys,
 * {L, E, M, O, N}. For successive letters of the message, we are going to take
 * successive letters of the key string, and encipher each message letter
 * using its corresponding key row. Choose the next letter of the key, go along
 * that row to find the column heading that matches the message character; the
 * letter at the intersection of [key-row, msg-col] is the enciphered letter.
 *
 * For example, the first letter of the plaintext, A, is paired with L, the
 * first letter of the key. So use row L and column A of the Vigenere square,
 * namely L. Similarly, for the second letter of the plaintext, the second
 * letter of the key is used; the letter at row E and column T is X. The rest
 * of the plaintext is enciphered in a similar fashion.
 *
 * Plaintext: ATTACKATDAWN
 * Key: LEMONLEMONLE
 * Ciphertext: LXFOPVEFRNHR
 */
public class VigenereCipherEncrypter {

    public VigenereCipherEncrypter() {}

    /**
     * Encrypt the message using the provided CipherKey and VigenereSquare.
     * @param message to be encrypted
     * @param key used to encrypt message
     * @return encrypted message string
     */
    public static String encrypt(final String message, final CipherKey key) {
        StringBuilder cipher = new StringBuilder();
        String k = key.KEY;
        char[][] square = VigenereSquare.SQUARE;
        for (int i = 0; i < k.length(); i++) {
            //Use the integer values of the key and message char at postion i
            //to determine which character to use from the VigenereSquare and
            //append it to the cipher text.
            cipher.append(square[k.charAt(i)][message.charAt(i)]);
        }
        return cipher.toString();
    }
}
