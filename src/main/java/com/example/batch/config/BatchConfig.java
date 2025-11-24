package com.example.batch.config;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 배치에서 사용할 기본 설정값들을 담는 클래스
 */
public class BatchConfig {

    private final Path inputDir;       // 입력 파일 폴더
    private final Path processedDir;   // 처리 완료 파일 폴더
    private final Path errorDir;       // 에러 파일 폴더
    private final long minSizeBytes;   // 처리할 최소 파일 크기
    private final int threadPoolSize;  // 사용할 스레드 개수

    public BatchConfig(Path inputDir,
                       Path processedDir,
                       Path errorDir,
                       long minSizeBytes,
                       int threadPoolSize) {

        this.inputDir = inputDir;
        this.processedDir = processedDir;
        this.errorDir = errorDir;
        this.minSizeBytes = minSizeBytes;
        this.threadPoolSize = threadPoolSize;
    }

    /**
     * 일단은 하드코딩된 기본값을 사용.
     * 나중에 환경변수 / 설정파일에서 읽어오도록 발전시킬 거야.
     */
    public static BatchConfig defaultConfig() {
        return new BatchConfig(
                Paths.get("input"),
                Paths.get("processed"),
                Paths.get("error"),
                1024L, // 1KB 이상만 처리
                4      // 스레드 4개
        );
    }

    public Path getInputDir() {
        return inputDir;
    }

    public Path getProcessedDir() {
        return processedDir;
    }

    public Path getErrorDir() {
        return errorDir;
    }

    public long getMinSizeBytes() {
        return minSizeBytes;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }
}
