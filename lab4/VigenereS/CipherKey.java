/**
 * The person sending the message to be encrypted (eg. attackatdawn) chooses a
 * keyword and repeats it until it matches the length of the plaintext, for
 * example, the keyword lemon, the cipher key will be lemonlemonle.
 */
public class CipherKey {

    /**
     * CipherKey String value.
     */
    public final String KEY;

    public CipherKey(String text, String keyword) {
        KEY = createKey(text, keyword);
    }

    /**
     * Creates a key string of the same length of the text based on
     * the keyword.
     * @param text to be encrypted
     * @param keyword the chosen keyword
     * @return the key string
     */
    public String createKey(final String text, final String keyword) {
        StringBuilder key = new StringBuilder();
        for (int i = 0, keywordIndex = 0; i < text.length(); i++,
                keywordIndex++) {
            if (keywordIndex >= keyword.length()) {
                keywordIndex = 0;
            }
            key.append(keyword.charAt(keywordIndex));
        }
        return key.toString();
    }
}
