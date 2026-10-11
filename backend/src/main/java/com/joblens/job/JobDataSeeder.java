package com.joblens.job;

import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import java.util.List;
@Configuration
public class JobDataSeeder {
    @Bean CommandLineRunner seedJobs(JobRepository repo) { return args -> { if(repo.count()>0)return; repo.saveAll(List.of(job("Backend Software Engineer","Northstar Labs","Bengaluru","Java,Spring Boot,REST APIs,SQL,PostgreSQL", "Build reliable backend services and APIs."), job("Full Stack Developer","Orbit Systems","Remote","React,TypeScript,Java,Spring Boot,MySQL", "Develop product features across frontend and backend."))); }; }
    private Job job(String title,String company,String location,String skills,String description){Job j=new Job();j.setTitle(title);j.setCompany(company);j.setLocation(location);j.setRequiredSkills(skills);j.setDescription(description);j.setSource("DEMO");return j;}
}
