/**
 * 
 */
package pvt.disney.dti.gateway.rules.race.utility;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The Class WordCipherTest.
 *
 * @author moons012
 */
public class WordCipherTest {
	
	/** The Constant TEST_ENCODED_FILE_NAME. */
	public static final String TEST_ENCODED_FILE_NAME = "testEncodedWords.txt";
	
	/** The Constant SETUP_ENCODED_FILE_NAME_PATH. USED IN SETUP, NOT IN TESTS. */
	public static final String SETUP_ENCODED_FILE_NAME_PATH=".\\src\\test\\resources\\testEncodedWords.txt";
	
	/** The Constant TEST_CLEAR_FILE_NAME. */
	public static final String TEST_CLEAR_FILE_NAME = ".\\src\\test\\resources\\testWordsInClear.txt";
	
	/**
	 * Sets the up before class. Reads in the claer text file and writes and encoded word file (that should
	 * be in gitignore to exclude from repo even though we clean it up
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 WordCipher cipher = new WordCipher();
		  File clearFile = new File(TEST_CLEAR_FILE_NAME);
		  File encodedFile = new File(SETUP_ENCODED_FILE_NAME_PATH);
		  
		  List<String> clearWordList = null;
		  List<String> encodedWordList = new ArrayList<String>();
		  
		  //read in the clear list
		 clearWordList = FileUtils.readLines(clearFile);
			  
		 //create the encoded list
		 for (String clearWord : clearWordList) {
			 String encodedWord = cipher.encode(clearWord);
			 encodedWordList.add(encodedWord);
		 }
		  
		 //write the encode list file to test resource directory
		 FileUtils.writeLines(encodedFile, encodedWordList); 
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
		//FileUtils.deleteQuietly(new File(TEST_ENCODED_FILE_NAME));
	}


	/**
	 * Test method for {@link pvt.disney.dti.gateway.rules.race.utility.WordCipher#getWordCollection()}.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testGetWordCollection() throws Exception {
		WordCipher cipher = new WordCipher();
		Collection<String> wordCollection = cipher.getWordCollection(TEST_ENCODED_FILE_NAME);

		//check word list
		boolean containsComment = WordCipherTest.checkForComment(wordCollection);
		
		//confirm no comment lines
		assertFalse("FAIL: WORD COLLECTION CONTAINED A COMMENT",containsComment);
		
		//confirm contains aaaa and bad
		// we are hard coding the data here for now since we don't want bad word files checked in
		assertTrue("FAIL: zaaa not found in word collection", wordCollection.contains("zaaa"));
		assertTrue("FAIL: bad not found in word collection", wordCollection.contains("bad"));
		
	}
	
    /**
     * Test get word collection without a specified file name, which uses the default name,
     * specified in the WordCipher class.
     * @throws Exception the exception
     */
    @Test
    public void testGetWordCollectionForDefaultName() throws Exception {
    	WordCipher cipher = new WordCipher();
		Collection<String> wordCollection = cipher.getWordCollection();
		
		//check that it has no comments, but no other checking so we dont have bad words in here
		//confirm no comment lines
		boolean containsComment = WordCipherTest.checkForComment(wordCollection);
		assertFalse("FAIL: WORD COLLECTION CONTAINED A COMMENT",containsComment);
		
    }
    
    /**
     * Private helper method used by several tests to check for comment in a word collection.
     *
     * @param wordCollection the word collection
     * @return true, if successful
     */
    private static boolean checkForComment(Collection<String> wordCollection) {
    	boolean containsComment = false;
    	
    	for (String word: wordCollection) {
			if (word.startsWith(WordCipher.COMMENT_DELIMITER)) {
				containsComment = true;
				break;
			}
		}
    	
    	return containsComment;
    }
}
