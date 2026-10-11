package com.joblens.evaluation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/evaluation")
public class EvaluationController {
    record Request(@NotEmpty List<String> rankedJobIds, @NotNull Set<String> relevantJobIds, int k) {}
    record Response(double precisionAtK,double recallAtK,double ndcgAtK,int k) {}
    @PostMapping("/ranking") Response ranking(@Valid @RequestBody Request request){int k=Math.max(1,Math.min(request.k(),100));return new Response(RankingMetrics.precisionAtK(request.rankedJobIds(),request.relevantJobIds(),k),RankingMetrics.recallAtK(request.rankedJobIds(),request.relevantJobIds(),k),RankingMetrics.ndcgAtK(request.rankedJobIds(),request.relevantJobIds(),k),k);}
}
