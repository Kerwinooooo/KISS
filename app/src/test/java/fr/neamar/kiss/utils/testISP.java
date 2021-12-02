package fr.neamar.kiss.utils;

import org.testng.annotations.Test;
import static org.junit.Assert.*;

import fr.neamar.kiss.normalizer.StringNormalizer;


public class testISP {
    private static final int full_word_bonus = 1000000;
    private static final int adjacency_bonus  = 100000;
    private static final int separator_bonus  = 10000;
    private static final int camel_bonus      = 1000;
    private static final int leading_letter_penalty     = -100;
    private static final int max_leading_letter_penalty = -300;
    private static final int unmatched_letter_penalty   = -1;

    @Test
    public void testInterfaceBased1() {
        String stringA = "js";
        String stringB = "js";

        StringNormalizer.Result result = StringNormalizer.normalizeWithResult(stringA, false);
        FuzzyScore fuzzyScore = new FuzzyScore(result.codePoints, false);
        fuzzyScore.setFullWordBonus(full_word_bonus)
                .setAdjacencyBonus(adjacency_bonus)
                .setSeparatorBonus(separator_bonus)
                .setCamelBonus(camel_bonus)
                .setLeadingLetterPenalty(leading_letter_penalty)
                .setMaxLeadingLetterPenalty(max_leading_letter_penalty)
                .setUnmatchedLetterPenalty(unmatched_letter_penalty);

        assertEquals(full_word_bonus + adjacency_bonus + separator_bonus, fuzzyScore.match(stringB).score);
    }

    @Test
    public void testInterfaceBased2() {
        String stringA = "";
        String stringB = "";

        StringNormalizer.Result result = StringNormalizer.normalizeWithResult(stringA, false);
        FuzzyScore fuzzyScore = new FuzzyScore(result.codePoints, false);
        fuzzyScore.setFullWordBonus(full_word_bonus)
                .setAdjacencyBonus(adjacency_bonus)
                .setSeparatorBonus(separator_bonus)
                .setCamelBonus(camel_bonus)
                .setLeadingLetterPenalty(leading_letter_penalty)
                .setMaxLeadingLetterPenalty(max_leading_letter_penalty)
                .setUnmatchedLetterPenalty(unmatched_letter_penalty);

        assertEquals(0, fuzzyScore.match(stringB).score);
    }

    @Test
    public void testFunctionalityBased1() {
        String stringA = "js";
        String stringB = "js";

        StringNormalizer.Result result = StringNormalizer.normalizeWithResult(stringA, false);
        FuzzyScore fuzzyScore = new FuzzyScore(result.codePoints, false);
        fuzzyScore.setFullWordBonus(full_word_bonus)
                .setAdjacencyBonus(adjacency_bonus)
                .setSeparatorBonus(separator_bonus)
                .setCamelBonus(camel_bonus)
                .setLeadingLetterPenalty(leading_letter_penalty)
                .setMaxLeadingLetterPenalty(max_leading_letter_penalty)
                .setUnmatchedLetterPenalty(unmatched_letter_penalty);

        assertEquals(full_word_bonus + adjacency_bonus + separator_bonus, fuzzyScore.match(stringB).score);
    }

    @Test
    public void testFunctionalityBased2() {
        String stringA = "js";
        String stringB = "111";

        StringNormalizer.Result result = StringNormalizer.normalizeWithResult(stringA, false);
        FuzzyScore fuzzyScore = new FuzzyScore(result.codePoints, false);
        fuzzyScore.setFullWordBonus(full_word_bonus)
                .setAdjacencyBonus(adjacency_bonus)
                .setSeparatorBonus(separator_bonus)
                .setCamelBonus(camel_bonus)
                .setLeadingLetterPenalty(leading_letter_penalty)
                .setMaxLeadingLetterPenalty(max_leading_letter_penalty)
                .setUnmatchedLetterPenalty(unmatched_letter_penalty);

        assertEquals(0, fuzzyScore.match(stringB).score);
    }
}