package com.example.batch.core;

import com.example.batch.config.BatchConfig;

/**
 * 배치 전체 흐름을 제어하는 클래스.
 * 지금은 그냥 설정값을 찍고, "나중에 여기서 실제 로직 돌릴 거다" 정도만.
 */
public class BatchRunner {

    private final BatchConfig config;

    public BatchRunner(BatchConfig config) {
        this.config = config;
    }

    public void run() {
        System.out.println("=== 미니 배치 시작 ===");

        // 나중에 여기서 파일 스캔 + 처리 서비스들을 호출할 거야.
        // 지금은 설정값만 한 번 찍어보자.
        System.out.println("[설정] inputDir     = " + config.getInputDir());
        System.out.println("[설정] processedDir = " + config.getProcessedDir());
        System.out.println("[설정] errorDir     = " + config.getErrorDir());
        System.out.println("[설정] minSizeBytes = " + config.getMinSizeBytes());
        System.out.println("[설정] threadPool   = " + config.getThreadPoolSize());

        System.out.println("=== 미니 배치 종료 ===");
    }
}
