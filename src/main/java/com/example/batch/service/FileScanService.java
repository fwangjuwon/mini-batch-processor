package com.example.batch.service;

import com.example.batch.config.BatchConfig;
import com.example.batch.core.FileTask;

import java.util.Collections;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileScanService {

    private final BatchConfig config;

    public FileScanService(BatchConfig config) {
        this.config = config;
    }

    public List<FileTask> scan() {
        Path inputDir = config.getInputDir();

        //Stream - input폴더에서 파일을 Stream으로 읽기
        try (Stream<Path> stream = Files.list(inputDir)) {

        	//파일인지 검사 
            return stream
                    .filter(Files::isRegularFile)                  // 파일만
                    .filter(path -> {
                        try {
                        	//파일크기 확인 
                            return Files.size(path) >= config.getMinSizeBytes(); // 최소 크기 조건
                        } catch (IOException e) {
                            return false;
                        }
                    })
                    .map(path -> {
                        try {
                        	//객체로 변환
                            return new FileTask(path, Files.size(path));
                        } catch (IOException e) {
                            return null;
                        }
                    })
                    .filter(task -> task != null)
                    //list로 변환 
                    .collect(Collectors.toList());

        } catch (IOException e) {
            System.out.println("[ERROR] 파일 스캔 중 오류 발생: " + e.getMessage());
            return Collections.emptyList();        }
    }
}
