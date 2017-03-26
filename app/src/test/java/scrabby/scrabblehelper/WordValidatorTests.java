package scrabby.scrabblehelper;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordValidatorTests
{
    class TestDictionary implements IWordChecker
    {
        String _words[] = {
          "a", "test", "abcd", "abcd1", "123"
        };

        public boolean checkWordExists(String word)
        {
            for(String w : _words)
            {
                if(w.equals(word))
                    return true;
            }
            return false;
        }
    }

    WordValidityChecker _validator =
            new WordValidityChecker(new TestDictionary());

    @Test
    public void checkValidity_wordToShort() throws Exception
    {
        String word = "a";
        WordValidityChecker.ValidationResult result =_validator.checkWordIsValid(word);

        assertEquals(false, result.IsValid);
    }

    @Test
    public void checkValidity_wordWithBadSymbol() throws Exception
    {
        String word = "abcd1";
        WordValidityChecker.ValidationResult result =_validator.checkWordIsValid(word);

        assertEquals(false, result.IsValid);
    }

    @Test
    public void checkValidity_wordWithNoLetter() throws Exception
    {
        String word = "123";
        WordValidityChecker.ValidationResult result =_validator.checkWordIsValid(word);

        assertEquals(false, result.IsValid);
    }

    @Test
    public void checkValidity_correctWordInDict() throws Exception
    {
        String word = "test";
        WordValidityChecker.ValidationResult result =_validator.checkWordIsValid(word);

        assertEquals(true, result.IsValid);
    }

    @Test
    public void checkValidity_correctWordNotInDict() throws Exception
    {
        String word = "testaa";
        WordValidityChecker.ValidationResult result =_validator.checkWordIsValid(word);

        assertEquals(false, result.IsValid);
    }

    @Test
    public void checkValidity_correctWordWithBlanksIntDict() throws Exception
    {
        String word = "te??";
        WordValidityChecker.ValidationResult result =_validator.checkWordIsValid(word);

        assertEquals(true, result.IsValid);
    }
}