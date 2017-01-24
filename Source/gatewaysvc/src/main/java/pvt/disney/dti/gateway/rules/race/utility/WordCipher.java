/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.utility;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

/**
 * The Class Cipher. Provides methods for decoding of a file and getting a collection of
 * decoded words from that file. 
 *
 * @author moons012
 */
public class WordCipher {
  
  /** The Constant KEY. */
  private static final String KEY = "keep-our-guests-safe";
  
  /**  The Constant ENCODED_FILE_NAME - used to. */
  public static final String ENCODED_FILE_NAME = "encodedWords.txt";

  /** The Constant COMMENT_DELIMITER. */
  public static final String COMMENT_DELIMITER = "#";

  
  /**
   * Encrypt.
   *
   * @param text the text
   * @return the string
   */
  public String encode(final String text) {
    return Base64.encodeBase64String(this.xor(text.getBytes()));
  }
  
  /**
   * Decode.
   *
   * @param hash the hash
   * @return the string
   */
  public String decode(final String hash) {
    try {
      return new String(this.xor(Base64.decodeBase64(hash.getBytes())), "UTF-8");
    } catch (java.io.UnsupportedEncodingException ex) {
      throw new IllegalStateException(ex);
    }
  }
  
  /**
   * Private helper method Xor.
   *
   * @param input the input
   * @return the byte[]
   */
  private byte[] xor(final byte[] input) {
    final byte[] output = new byte[input.length];
    final byte[] secret = WordCipher.KEY.getBytes();
    int spos = 0;
    for (int pos = 0; pos < input.length; ++pos) {
      output[pos] = (byte) (input[pos] ^ secret[spos]);
      spos += 1;
      if (spos >= secret.length) {
        spos = 0;
      }
    }
    return output;
  }
  
  
  /**
   * Gets the word collection - uses the default encoded file name ENCODED_FILE_NAMe
   * if one is not specified.
   * Gets the encoded word collection from  the specified filename that is somewhere on 
   * the classpath. Decodes the list of words 
   * and returns as a collection of clear text strings, one per word. 
   * Excludes comments (lines that start with #)
   *
   * @return the word collection
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws URISyntaxException the URI syntax exception
   */
  public Collection<String> getWordCollection() throws IOException, URISyntaxException {
	  return this.getWordCollection(this.ENCODED_FILE_NAME);
  }
  
	/**
	 * Gets the encoded word collection from  the specified filename that is somewhere on 
	 * the classpath. Decodes the list of words 
	 * and returns as a collection of clear text strings, one per word. 
	 * Excludes comments (lines that start with #)
	 * This is protected so we can call from a test specifying something other than
	 * the default filename, but still protect from use except when called by the
	 * public method above.
	 *
	 * @param fileName the file name
	 * @return the word collection
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws URISyntaxException the URI syntax exception
	 */
	protected Collection<String> getWordCollection(String fileName) throws IOException, URISyntaxException {
		List<String> encodedWordCollection = null;
		List<String> decodedWordCollection = new ArrayList<String>();
		
		//get the encoded file off the classpath and create a collection of encoded words
		File encodedfile = new File(getClass().getClassLoader().getResource(fileName).getFile());
		encodedWordCollection = FileUtils.readLines( encodedfile);
		
		//decoded the encoded words and add them to the decoded election, unless it is a comment
		for (String encodedWord : encodedWordCollection) {
			String decodedWord = this.decode(encodedWord);
			if (!decodedWord.startsWith(COMMENT_DELIMITER)) {
				decodedWordCollection.add(decodedWord);
			}
		}
		
		//return our nifty decoded list to keep our guests happy
		return decodedWordCollection;	
	}
	
}