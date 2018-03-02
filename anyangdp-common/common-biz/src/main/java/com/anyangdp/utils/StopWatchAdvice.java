package com.anyangdp.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.env.Environment;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author william
 */
@Slf4j
@Aspect
public class StopWatchAdvice {

    private static final StatsTrack serviceStatsTrack = new StatsTrack(5, 200);
    private static final StatsTrack daoStatsTrack = new StatsTrack(1, 500);

    private Environment env;

    public StopWatchAdvice(Environment env) {
        this.env = env;
    }

    @Pointcut("execution(* com.rr.common.service.AbstractJPAService.*(..))")
    public void servicePointcut() {
    }


    @Pointcut("execution(* com.rr.api.saas.dao.*.*(..))")
    public void daoPointcut() {
    }

    @Around("servicePointcut()")
    public Object serviceAroundAdvice(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            boolean stopWatch = env.getProperty("stop.watch", Boolean.class);

            if (stopWatch) {
                serviceStatsTrack.updateStats(joinPoint.getTarget().getClass().getCanonicalName(), joinPoint.getSignature()
                                .getName(),
                        (System
                                .currentTimeMillis() - start));
            }
        }
    }

    @Around("daoPointcut()")
    public Object daoAroundAdvice(ProceedingJoinPoint joinPoint)
            throws Throwable {
        long start = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            boolean stopWatch = env.getProperty("stop.watch", Boolean.class);

            if (stopWatch) {
                daoStatsTrack.updateStats(AopUtils.getTargetClass(joinPoint.getTarget()).getCanonicalName(),
                        joinPoint.getSignature()
                                .getName(),
                        (System
                                .currentTimeMillis() - start));
            }
        }

    }

    @Slf4j
    static class StatsTrack {
        private static ConcurrentHashMap<String, MethodStats> methodStats = new ConcurrentHashMap<>();

        private long statLogFrequency;
        private long methodWarningThreshold;

        StatsTrack(long statLogFrequency, long methodWarningThreshold) {
            this.statLogFrequency = statLogFrequency;
            this.methodWarningThreshold = methodWarningThreshold;
        }

        private void updateStats(String className, String methodName, long elapsedTime) {
            MethodStats stats = methodStats.computeIfAbsent(methodName, n -> new MethodStats(className, n));
            stats.count++;
            stats.totalTime += elapsedTime;
            if (elapsedTime > stats.maxTime) {
                stats.maxTime = elapsedTime;
            }

            if (elapsedTime > methodWarningThreshold) {
                log.warn("waring method: " + className + "." + methodName + "(), cnt = " + stats.count +
                        ", " +
                        "lastTime = " +
                        elapsedTime +
                        ", maxTime = " + stats.maxTime);
            }

            if (stats.count % statLogFrequency == 0) {
                long avgTime = stats.totalTime / stats.count;
                long runningAvg = (stats.totalTime - stats.lastTotalTime) / statLogFrequency;
                log.debug("debug method: " + className + "." + methodName + "(), cnt = " + stats.count + ", lastTime = " +
                        elapsedTime + ", " +
                        "avgTime = " + avgTime + ", runningAvg = " + runningAvg + ", maxTime = " + stats.maxTime);

                //reset the last total time
                stats.lastTotalTime = stats.totalTime;
            }
        }

    }

    static class MethodStats {
        String className;
        String methodName;
        long count;
        long totalTime;
        long lastTotalTime;
        long maxTime;

        MethodStats(String className, String methodName) {
            this.className = className;
            this.methodName = methodName;
        }
    }
}
