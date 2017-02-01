package scrabby.scrabblehelper;

// Prosta implementacja do testow
public class TestChecker implements IWordChecker
{
    private String _testWords[];

    public TestChecker()
    {
        _testWords = new String[]{ "test", "aaa", "bbb" };
    }

    public boolean checkWordExists(String word)
    {
        for(String w : _testWords)
        {
            if(word.equals(w))
            {
                return true;
            }
        }
        return false;
    }
}
