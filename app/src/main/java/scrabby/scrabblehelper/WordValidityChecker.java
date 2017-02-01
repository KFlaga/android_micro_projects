package scrabby.scrabblehelper;

import java.util.Locale;

interface IWordChecker
{
    boolean checkWordExists(String word);
}

// Klasa sluzaca do sprawdzania poprawnosci wprowadzonych wyrazow.
// Zadady:
// - ma conajmniej 2 znaki
// - ma conajwyzej 15 znakow
// - ma conajmniej 1 litere
// - ma conajwyzej 4 blanki (wymog wynikajacy z czasu przegladu slow)
// - zawiera wylacznie litery lub blanki
// - jesli nie ma blankow to czy slowo jest w slowniku
// - jesli ma blanki to czy da sie ulozyc dzieki nim jakies slowo
public class WordValidityChecker
{
    public class ValidationResult
    {
        ValidationResult(boolean isValid, String invalidReson)
        {
            IsValid = isValid;
            InvalidReson = invalidReson;
        }

        public boolean IsValid;
        public String InvalidReson;
    }

    public static final char BLANK_CODE = '?';

    private final String _availableLetters = "aąbcćdeęfghijklłmnńoópqrsśtuwvxyzźż";

    private IWordChecker _wordChecker;

    public WordValidityChecker(IWordChecker dictChecker)
    {
        _wordChecker = dictChecker;
    }

    // Sprawdza czy podane slowo jest poprawne (przed sprawdzaniem zmieniamy na male litery)
    public ValidationResult checkWordIsValid(String word)
    {
        word = word.toLowerCase(new Locale("pl"));
        if(word.length() < 2)
            return new ValidationResult(false, "Słowo musi mieć conajmniej 2 znaki.");

        if(word.length() < 2)
            return new ValidationResult(false, "Słowo musi mieć conajwyżej 15 znaków.");

        ValidationResult result = checkWordHaveCorrectSymbols(word);
        if(!result.IsValid)
            return result;

        boolean wordExists;
        if(checkWordHaveBlank(word))
        {
            // Slowo ma blanki, tak wiec trzeba sprawdzic czy da sie ulozyc dzieki nim slowo
            wordExists = checkWordWithBlanks(word);
        }
        else
        {
            // Slowo ma wylacznie litery, tak wiec sprawdzamy czy jest w slowniku
            wordExists = checkWordExists(word);
        }

        return new ValidationResult(wordExists, "Podane słowo nie istnieje w słowniku.");
    }

    // Sprawdza czy slowo ma tylko polskie litery lub blanki i ma conajmniej 1 litere
    private ValidationResult checkWordHaveCorrectSymbols(String word)
    {
        int letters = 0, blanks = 0, others = 0;
        for(int i = 0; i < word.length(); ++i)
        {
            char c = word.charAt(i);
            // Sprawdzamy czy znak jest litera z polskiego alfabetu (tylko male)
            if(c >= 'a' && c <= 'z')
                ++letters; // Jest to zwykla litera
            else if("ęóąśłżźćń".indexOf(c) >= 0)
                ++letters; // Jest to polska litera
            else if(c == BLANK_CODE)
                ++blanks; // Jest to blank
            else
                ++others;
        }

        if(others > 0)
            return new ValidationResult(false, "Słowo może zawierać tylko litery i blanki.");
        if(letters == 0)
            return new ValidationResult(false, "Słowo musi zawierać conajmniej jedną literę.");
        if(blanks > 4)
            return new ValidationResult(false, "Słowo może zawierać conajwyżej 4 blanki.");

        return new ValidationResult(true, "");
    }

    // Sprawdza czy slowo ma jakiegos blanka
    private boolean checkWordHaveBlank(String word)
    {
        return word.indexOf(BLANK_CODE) >= 0;
    }

    // Sprawdza czy slowo istnieje w slowniku (powinno byc bez blankow i tylko z malymi literami)
    private boolean checkWordExists(String word)
    {
        return _wordChecker.checkWordExists(word);
    }

    private boolean checkWordWithBlanks(String orginalWord)
    {
        return checkWordWithBlanks(orginalWord, new StringBuilder(orginalWord), 0);
    }

    // Zastepuje kolejne blanki kolejnymi literami i sprawdza czy istnieje takie slowo
    private boolean checkWordWithBlanks(String orginalWord, StringBuilder replacedWord, int startIdx)
    {
        // Znajdz kolejne 2 blanki (jesli tu jestesmy istnieje conajmniej 1)
        int nextBlank = orginalWord.indexOf(BLANK_CODE, startIdx);
        int nextBlank2 = orginalWord.indexOf(BLANK_CODE, nextBlank + 1);
        if(nextBlank2 > 0)
        {
            // Istnieja jeszcze conajmniej 2 blanki
            // Zastapiamy pierwszy z nich litera, a nastepnie wywolujemy
            // ta funkcje rekurencyjnie by zastpic kolejne blanki
            for(int i = 0; i < _availableLetters.length(); ++i)
            {
                // zastap blank litera i sprawdz czy slowo istnieje
                replacedWord.setCharAt(nextBlank, _availableLetters.charAt(i));
                if(checkWordWithBlanks(orginalWord, replacedWord, nextBlank + 1))
                    return true;
            }
        }
        else
        {
            // Jest to ostatni blank
            for(int i = 0; i < _availableLetters.length(); ++i)
            {
                // zastap blank litera i sprawdz czy slowo istnieje
                replacedWord.setCharAt(nextBlank, _availableLetters.charAt(i));
                if(checkWordExists(replacedWord.toString()))
                    return true;
            }
        }

        return false;
    }
}
