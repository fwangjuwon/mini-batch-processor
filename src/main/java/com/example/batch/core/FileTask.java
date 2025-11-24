package com.example.batch.core;

import java.nio.file.Path;

public class FileTask {

    private final Path path;
    private final long size;

    public FileTask(Path path, long size) {
        this.path = path;
        this.size = size;
    }

    public Path getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "FileTask{" +
                "path=" + path +
                ", size=" + size +
                '}';
    }
}
