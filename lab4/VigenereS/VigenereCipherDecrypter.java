/**
 * Using ciphered text, a CipherKey and a VigenereSquare the
 * VigenereCipherDecrypter achieves decryption by going to the row in the table
 * corresponding to the key, finding the position of the ciphertext letter in
 * this row, and then using the column's label as the plaintext. For example,
 * in row L (from LEMON), the ciphertext L appears in column A, which is the
 * first plaintext letter. Next we go to row E (from LEMON), locate the
 * ciphertext X which is found in column T, this T is the second plaintext
 * letter.
 */
public class VigenereCipherDecrypter {

    public  VigenereCipherDecrypter() {}

    /**
     * Decrypt the cipher text using the provided CipherKey and
     * VigenereSquare.
     * @param cipher text.
     * @param key used to decrypt the cipher text.
     * @return decrypted message.
     */
    public static String decrypt(final String cipher, final CipherKey key) {
        StringBuilder message = new StringBuilder();
        String k = key.KEY;
        char[][] square = VigenereSquare.SQUARE;
        for (int i = 0; i < k.length(); i++) {
            int rowIndex = k.charAt(i);
            char[] row = square[rowIndex];
            int colIndex = new String(row).indexOf(cipher.charAt(i));
            message.append((char) colIndex);
        }
        return message.toString();
    }
}
