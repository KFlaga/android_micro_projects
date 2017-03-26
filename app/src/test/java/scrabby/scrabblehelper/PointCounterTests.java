package scrabby.scrabblehelper;

import org.junit.Test;

import static org.junit.Assert.*;

public class PointCounterTests
{
    PointCounter _pointCounter = new PointCounter();

    @Test
    public void checkPointsForWord_ab_d() throws Exception
    {
        LetterSequence word = new LetterSequence();
        word.add(new Letter('a'));
        word.add(new Letter('b'));
        word.add(new Letter('?'));
        word.add(new Letter('d'));

        int points = _pointCounter.getPointsForWord(word);
        assertEquals(1 + 3 + 2, points);
    }

    @Test
    public void checkPointsForWord_ab_d_withBonus() throws Exception
    {
        LetterSequence word = new LetterSequence();
        word.add(new Letter('a'));
        word.add(new Letter('b'));
        word.add(new Letter('?'));
        word.add(new Letter('d'));

        word.get(0).Bonus = Letter.BonusType.Bonus_Word_x2;
        word.get(1).Bonus = Letter.BonusType.Bonus_Letter_x2;
        word.get(2).Bonus = Letter.BonusType.Bonus_Letter_x3;

        int points = _pointCounter.getPointsForWord(word);
        assertEquals((1 + 3 * 2 + 2)*2, points);
    }
}