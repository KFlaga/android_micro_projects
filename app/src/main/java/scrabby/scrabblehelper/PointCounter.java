package scrabby.scrabblehelper;

import java.util.Map;
import java.util.TreeMap;

// Liczy punktacje za podane slowo
class PointCounter
{
    static final char BLANK_CODE = '?';

    private Map<Character, Integer> _pointsForLetterMap; // Mapa z punktami dla kazdego mozliwego kodu znaku (mapa kod->punkty)

    PointCounter()
    {
        _pointsForLetterMap = new TreeMap<>();

        // Dodajemy wszystkie obslugiwane kody znakow
        setPointsForLetter(BLANK_CODE, 0);
        setPointsForLetters("aeinorswz", 1);
        setPointsForLetters("cdklmpty", 2);
        setPointsForLetters("bghjłu", 3);
        setPointsForLetters("ąęfóśż", 5);
        setPointsForLetters("ć", 6);
        setPointsForLetters("ń", 7);
        setPointsForLetters("ż", 9);
    }

    // Ustawia wartosc punktowa danego znaku
    void setPointsForLetter(char code, int points)
    {
        _pointsForLetterMap.put(code, points);
    }

    // Ustawia wartosc punktowa dla wszystkich znakow w zadanym stringu
    void setPointsForLetters(String codes, int points)
    {
        for(int i = 0; i < codes.length(); ++i)
        {
            setPointsForLetter(codes.charAt(i), points);
        }
    }

    // Zwraca wartosc punktowa znaku
    // Jesli znak nie zostal dodany zwraca poprostu zero
    int getPointsForLetter(Letter letter)
    {
        Integer points = _pointsForLetterMap.get(letter.Char);
        if(points != null)
        {
            if(letter.Bonus == Letter.BonusType.Bonus_Letter_x2)
                return points * 2;
            else if(letter.Bonus == Letter.BonusType.Bonus_Letter_x3)
                return points * 3;
            else
                return points;
        }
        return 0;
    }

    // Zwraca wartosc punktowa znaku
    // Jesli slowo zawiera nie dodany znak, to nie ma za niego punktow
    int getPointsForWord(LetterSequence word)
    {
        int points = 0;
        int doubleWordBonusCount = 0;

        // Dla kazdego znaku w slowie dodaj jego wartosc punktowa
        for(int i = 0; i < word.size(); ++i)
        {
            Letter letter = word.get(i);
            points += getPointsForLetter(letter);

            // Sprawdz czy na literce jest bonus dla slowa
            if(letter.Bonus == Letter.BonusType.Bonus_Word_x2)
                doubleWordBonusCount += 1;
        }

        // Dodaj bonus
        while(doubleWordBonusCount > 0)
        {
            points = points * 2;
            doubleWordBonusCount -= 1;
        }

        return points;
    }
}
