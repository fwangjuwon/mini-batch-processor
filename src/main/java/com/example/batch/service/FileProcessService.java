package com.example.batch.service;

import com.example.batch.config.BatchConfig;
import com.example.batch.core.FileProcessResult;
import com.example.batch.core.FileProcessor;
import com.example.batch.core.FileTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 파일들을 비동기(병렬)로 처리하는 서비스.
 *
 * 여기서 Java 8의 중요한 기능들을 사용한다:
 *  - CompletableFuture
 *  - 람다식
 *  - Stream API
 */
public class FileProcessService {

    private final BatchConfig config;
    private final FileProcessor processor;

    // Java 8 이전에도 있던 ExecutorService지만,
    // 'newFixedThreadPool'과 함께 CompletableFuture에서 많이 같이 쓴다.
    private final ExecutorService executor;

    public FileProcessService(BatchConfig config, FileProcessor processor) {
        this.config = config;
        this.processor = processor;
        this.executor = Executors.newFixedThreadPool(config.getThreadPoolSize());
    }

    /**
     * 파일 리스트를 받아서 비동기적으로 모두 처리한다.
     * - CompletableFuture.supplyAsync(...) 를 사용해서
     *   각 파일을 병렬로 처리한다. (Java 8의 핵심 기능)
     */
    public List<FileProcessResult> processAll(List<FileTask> tasks) {

        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }

        // 1) 각 파일에 대한 CompletableFuture 생성
        List<CompletableFuture<FileProcessResult>> futures = tasks.stream()
                // ★ Java 8 Stream + 람다식
                .map(task ->
                        // ★ Java 8 CompletableFuture.supplyAsync
                        CompletableFuture.supplyAsync(() -> {
                            try {
                                // processor.process(task)는 FileProcessor의 추상 메서드.
                                // 나중에 람다로 구현할 예정.
                                return processor.process(task);
                            } catch (Exception e) {
                                // 예외가 난 경우에도 Result 객체로 감싸서 리턴
                                return new FileProcessResult(task, false,
                                        "처리 중 예외 발생: " + e.getMessage());
                            }
                        }, executor)
                )
                .collect(Collectors.toList()); // ★ Java 8 Collectors.toList()

        // 2) CompletableFuture.allOf 로 "다 끝날 때까지 기다리기"
        CompletableFuture<Void> allDoneFuture =
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // ★ join() : Future.get()과 비슷하지만 체크 예외를 던지지 않는 Java 8 메서드
        allDoneFuture.join();

        // 3) 모든 Future에서 결과 꺼내서 List<FileProcessResult> 로 변환
        List<FileProcessResult> results = futures.stream()
                .map(CompletableFuture::join) // ★ 메서드 레퍼런스 (Java 8)
                .collect(Collectors.toList());

        return results;
    }

    public void shutdown() {
        executor.shutdown();
    }
}
