package com.example.batch;

import com.example.batch.config.BatchConfig;
import com.example.batch.core.BatchRunner;

public class App {

    public static void main(String[] args) {
        // 1. 기본 설정 생성
        BatchConfig config = BatchConfig.defaultConfig();

        // 2. 배치 러너 생성
        BatchRunner runner = new BatchRunner(config);

        // 3. 배치 실행
        runner.run();
    }
}
