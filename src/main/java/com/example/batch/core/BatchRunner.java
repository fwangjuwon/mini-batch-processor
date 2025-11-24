package com.example.batch.core;

import com.example.batch.config.BatchConfig;
import com.example.batch.service.FileProcessService;
import com.example.batch.service.FileScanService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * 배치 전체 흐름을 제어하는 클래스.
 */
public class BatchRunner {

    private final BatchConfig config;

    public BatchRunner(BatchConfig config) {
        this.config = config;
    }

    public void run() {
        System.out.println("=== 미니 배치 시작 ===");

        // 1. 파일 스캔
        FileScanService scanService = new FileScanService(config);
        List<FileTask> tasks = scanService.scan();
        System.out.println("스캔된 파일 수 = " + tasks.size());

        if (tasks.isEmpty()) {
            System.out.println("처리할 파일이 없습니다.");
            System.out.println("=== 미니 배치 종료 ===");
            return;
        }

        // 2. FileProcessor를 "람다식"으로 구현
        //
        // ★ Java 8 람다식 예제
        //   - 원래라면 FileProcessor를 구현한 클래스를 만들고
        //     new MyFileProcessor() 이런 식으로 써야 하지만,
        //   - @FunctionalInterface + 람다 덕분에 아래처럼 간단히 쓸 수 있다.
        //
        //   (FileTask task) -> { ... } 부분이 람다식이다.
        //
        FileProcessor processor = task -> {
            try {
                // Files.lines(...) : 파일을 한 줄씩 Stream으로 읽는 Java 8 메서드
                long lineCount;
                try (java.util.stream.Stream<String> lines = Files.lines(task.getPath())) {
                    lineCount = lines.count();  // ★ Stream.count() (터미널 연산)
                }

                String msg = "라인 수: " + lineCount + "줄";
                return new FileProcessResult(task, true, msg);

            } catch (IOException e) {
                return new FileProcessResult(task, false,
                        "파일 읽기 실패: " + e.getMessage());
            }
        };

        // 3. FileProcessService로 비동기 처리
        FileProcessService processService = new FileProcessService(config, processor);
        List<FileProcessResult> results = processService.processAll(tasks);
        processService.shutdown();

        long successCount = results.stream()
                .filter(FileProcessResult::isSuccess)   // ★ 메서드 레퍼런스
                .count();

        long failCount = results.size() - successCount;

        System.out.println("=== 처리 결과 요약 ===");
        System.out.println("성공: " + successCount + ", 실패: " + failCount);

        // 상세 결과 로그
        results.forEach(result ->   // ★ forEach + 람다식
                System.out.println(result.getTask().getPath() + " -> "
                        + (result.isSuccess() ? "성공" : "실패")
                        + " / " + result.getMessage())
        );

        System.out.println("=== 미니 배치 종료 ===");
    }
}
