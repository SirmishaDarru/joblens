package com.joblens.matching;

import com.joblens.job.Job;

public record JobRecommendation(Job job, MatchResult match) {}
