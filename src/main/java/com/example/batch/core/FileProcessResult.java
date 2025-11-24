package com.example.batch.core;

/**
 * 파일 하나를 처리한 결과를 담는 클래스.
 * 성공/실패 여부, 메시지 등을 보관한다.
 */
public class FileProcessResult {

    private final FileTask task;  // 어떤 파일을 처리했는지
    private final boolean success; // 처리 성공 여부
    private final String message;  // 처리 결과 메시지 (예: "라인 수: 10줄")

    public FileProcessResult(FileTask task, boolean success, String message) {
        this.task = task;
        this.success = success;
        this.message = message;
    }

    public FileTask getTask() {
        return task;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "FileProcessResult{" +
                "task=" + task +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
