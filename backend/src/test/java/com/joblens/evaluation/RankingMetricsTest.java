package com.joblens.evaluation;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class RankingMetricsTest { @Test void calculatesRankingMetrics(){var ranked=List.of("a","b","c","d");var relevant=Set.of("a","c");assertEquals(.5,RankingMetrics.precisionAtK(ranked,relevant,4));assertEquals(1,RankingMetrics.recallAtK(ranked,relevant,4));assertTrue(RankingMetrics.ndcgAtK(ranked,relevant,4)>0.8);} }
