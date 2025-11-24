package com.example.batch.core;

import java.util.List;

import com.example.batch.config.BatchConfig;
import com.example.batch.service.FileScanService;

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

        FileScanService scanService = new FileScanService(config);
        List<FileTask> tasks = scanService.scan();
        System.out.println("스캔된 파일 수 = " + tasks.size());

        for (FileTask task : tasks) {
            System.out.println(" - " + task);
        }

        System.out.println("=== 미니 배치 종료 ===");
    }
}
