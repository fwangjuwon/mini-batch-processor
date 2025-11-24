package com.example.batch.core;

/**
 * Java 8의 @FunctionalInterface 예제.
 *
 * - 메서드가 딱 하나만 있는 인터페이스에 @FunctionalInterface를 붙이면
 *   이 인터페이스를 "람다"로 구현할 수 있게 된다.
 * - 나중에 BatchRunner에서
 *
 *   FileProcessor processor = task -> { ... };
 *
 *   이런 식으로 구현할 예정.
 */
@FunctionalInterface // ★ Java 8에서 추가된 애너테이션
public interface FileProcessor {

    /**
     * 파일 하나를 처리하고, 결과를 반환한다.
     */
    FileProcessResult process(FileTask task) throws Exception;
}
