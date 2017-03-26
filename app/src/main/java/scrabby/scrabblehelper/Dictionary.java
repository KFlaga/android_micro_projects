package scrabby.scrabblehelper;

import java.util.HashSet;
import java.util.ArrayList;


// Zawiera zestaw slow zaposanych w postaci mapy (dlugosc_slowa)->(zbior_slow)
// Do przechowywania zbioru slow uzyto HashSet, dzieki czemu mamy szybki
// dostep do wyrazow
// Wykorzystuje interfejs IDictionaryLoader do zaladowania zbioru slow, tak wiec latwo
// przystosowac go do roznych form zapisu slownika
class Dictionary implements IWordChecker
{
    interface IDictionaryLoader
    {
        ArrayList<HashSet<String>> loadDictionary();
    }

    private ArrayList<HashSet<String>> _wordsOfLength;

    Dictionary(IDictionaryLoader dictLoader)
    {
        _wordsOfLength = dictLoader.loadDictionary();
    }

    HashSet<String> getAllWordsOfLength(int len)
    {
        return _wordsOfLength.get(len);
    }

    public boolean checkWordExists(String word)
    {
        HashSet<String> set = getAllWordsOfLength(word.length());
        return set.contains(word);
    }
}
