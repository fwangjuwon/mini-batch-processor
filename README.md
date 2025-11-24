# Mini Batch Processor (Java 8)

Java 8 기능들을 활용하여 만든 간단한 파일 배치 처리 프로그램이다.  
정해진 디렉토리에서 파일을 스캔하고, 각 파일을 비동기적으로 처리한 뒤 결과를 출력하는 구조로 되어 있다.  
이 프로젝트의 목표는 **Java 8 주요 기능들을 실제 코드에서 직접 사용해보는 것**이다.

---

##  주요 기능

- `input` 디렉토리에서 파일 스캔
- 최소 크기 이상의 파일만 필터링
- 각 파일을 **CompletableFuture** 기반으로 병렬 처리
- 파일 라인 수 세기(Line Count)
- 처리 성공/실패 여부 요약 출력
- FileProcessor 교체를 통해 처리 로직 확장 가능

---

#  Java 8 핵심 기능 정리

이 프로젝트는 Java 8 주요 기능들을 자연스럽게 사용하면서 동작한다.  
아래는 실제 코드에서 사용한 Java 8 기능들이다.

---

## 1. 람다식 (Lambda Expressions)

```java
FileProcessor processor = task -> { ... };
```

- 함수형 인터페이스(FileProcessor)를 간결하게 구현할 수 있다.
- Stream API, CompletableFuture 등에서 적극적으로 사용된다.

 ## 2. 함수형 인터페이스 (@FunctionalInterface)

```java
@FunctionalInterface
public interface FileProcessor {
    FileProcessResult process(FileTask task) throws Exception;
}
```

## 3. Stream API
파일 필터링 예제

```java
Files.list(inputDir)
     .filter(Files::isRegularFile)
     .filter(path -> Files.size(path) >= config.getMinSizeBytes())
     .collect(Collectors.toList());
```
처리 결과 집계 예제
```java
long successCount = results.stream()
                           .filter(FileProcessResult::isSuccess)
                           .count();
```
- map / filter / collect / count 등 다양한 Stream 기능을 활용했다.

## 4. 메서드 레퍼런스 (Method References)
```java
futures.stream()
       .map(CompletableFuture::join)
       .collect(Collectors.toList());
```
- ClassName::methodName 형태로 메서드를 간결하게 참조하는 문법이다.
- Stream과 CompletableFuture에서 자주 사용된다.

## 5. CompletableFuture
비동기처리
```java
CompletableFuture.supplyAsync(() -> processor.process(task), executor);
```
여러 처리의 완료를 기다리는 코드
```java
CompletableFuture.allOf(futuresArray).join();
```
- Java 8에서 새롭게 도입된 강력한 비동기 API다.
- Future보다 더 풍부하고 체이닝/조합 기능이 뛰어나다.

## 6.Files.lines() – 파일을 Stream으로 읽기
```java
try (Stream<String> lines = Files.lines(task.getPath())) {
    long lineCount = lines.count();
}
```
- 파일을 한 줄씩 Stream 형태로 읽을 수 있다.
- Stream API 기반의 라인 카운팅이 가능하다.

## 7. Optional(향후 적용 에정)
이 프로젝트는 향후 Optional을 설정값 처리 및 null-safe 로직에 적용할 계획이다.


# 프로젝트 구조
```less
src/main/java/com.example.batch
 ├─ App.java                     (프로그램 시작점)
 ├─ config
 │    └─ BatchConfig.java        (배치 설정값)
 ├─ core
 │    ├─ BatchRunner.java        (배치 전체 실행 흐름)
 │    ├─ FileTask.java           (파일 하나 표현)
 │    ├─ FileProcessor.java      (@FunctionalInterface)
 │    └─ FileProcessResult.java  (처리 결과)
 └─ service
      ├─ FileScanService.java    (input 폴더에서 파일 스캔)
      └─ FileProcessService.java (CompletableFuture로 비동기 처리)
```

# 실행 방법
 1. 프로젝트 clone 또는 다운로드
 2. input/ 폴더 생성 후 1KB 이상 텍스트 파일 넣기
 3. 실행
```java
Run → Java Application (App.java)
```

# 출력 예시
```diff
=== 미니 배치 시작 ===
스캔된 파일 수 = 3

=== 처리 결과 요약 ===
성공: 3, 실패: 0
input/file1.txt -> 성공 / 라인 수: 10줄
input/file2.txt -> 성공 / 라인 수: 25줄
=== 미니 배치 종료 ===
```
