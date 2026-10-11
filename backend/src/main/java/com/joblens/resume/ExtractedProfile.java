package com.joblens.resume;

import java.util.List;

public record ExtractedProfile(
        List<String> skills,
        List<String> education,
        List<String> experience,
        List<String> projects,
        List<String> warnings
) {}
