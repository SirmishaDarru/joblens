package com.joblens.evaluation;

import java.util.*;

public final class RankingMetrics {
    private RankingMetrics() {}
    public static double precisionAtK(List<String> ranked, Set<String> relevant, int k) { if(k<=0)return 0; return ranked.stream().limit(k).filter(relevant::contains).count()/(double)Math.min(k,ranked.size()); }
    public static double recallAtK(List<String> ranked, Set<String> relevant, int k) { if(relevant.isEmpty())return 0; return ranked.stream().limit(k).filter(relevant::contains).count()/(double)relevant.size(); }
    public static double ndcgAtK(List<String> ranked, Set<String> relevant, int k) { double dcg=0;int rank=0;for(String item:ranked.stream().limit(k).toList()){rank++;if(relevant.contains(item))dcg+=1/(Math.log(rank+1)/Math.log(2));}double ideal=0;for(int idealRank=1;idealRank<=Math.min(k,relevant.size());idealRank++)ideal+=1/(Math.log(idealRank+1)/Math.log(2));return ideal==0?0:dcg/ideal; }
}
