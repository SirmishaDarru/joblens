package com.joblens.api;

import jakarta.servlet.FilterChain;import jakarta.servlet.ServletException;import jakarta.servlet.http.*;import org.springframework.stereotype.Component;import org.springframework.web.filter.OncePerRequestFilter;import java.io.IOException;import java.time.Instant;import java.util.concurrent.ConcurrentHashMap;import java.util.concurrent.atomic.AtomicInteger;

@Component public class RateLimitFilter extends OncePerRequestFilter {
    private static final int LIMIT=30;private static final long WINDOW_MS=60_000;private final ConcurrentHashMap<String,Window> windows=new ConcurrentHashMap<>();
    @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{String path=req.getRequestURI();if(path.startsWith("/api/auth/")||path.startsWith("/api/assistant/")){String key=req.getRemoteAddr()+":"+path;Window w=windows.compute(key,(k,current)->{long now=Instant.now().toEpochMilli();if(current==null||now-current.started>WINDOW_MS)return new Window(now, new AtomicInteger(1));current.count.incrementAndGet();return current;});if(w.count.get()>LIMIT){res.setStatus(429);res.setHeader("Retry-After","60");res.setContentType("application/json");res.getWriter().write("{\"error\":\"rate_limit_exceeded\",\"message\":\"Too many requests. Try again later.\"}");return;}}chain.doFilter(req,res);}
    private record Window(long started,AtomicInteger count){}
}
