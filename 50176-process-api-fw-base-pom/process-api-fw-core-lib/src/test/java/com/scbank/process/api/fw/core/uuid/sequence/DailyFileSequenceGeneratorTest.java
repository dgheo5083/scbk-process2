package com.scbank.process.api.fw.core.uuid.sequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("DailyFileSequenceGenerator")
class DailyFileSequenceGeneratorTest {

    @TempDir
    Path tempDir;

    private final LocalDate day = LocalDate.of(2026, 1, 1);

    @Test
    @DisplayName("시퀀스는 1부터 순차 증가한다")
    void sequentialIncrement() {
        DailyFileSequenceGenerator gen = new DailyFileSequenceGenerator(tempDir);

        assertThat(gen.next(day)).isEqualTo(1L);
        assertThat(gen.next(day)).isEqualTo(2L);
        assertThat(gen.next(day)).isEqualTo(3L);
    }

    @Test
    @DisplayName("인자 없는 next()는 오늘 날짜로 시퀀스를 생성한다")
    void noArgNextUsesToday() {
        DailyFileSequenceGenerator gen = new DailyFileSequenceGenerator(tempDir);

        assertThat(gen.next()).isEqualTo(1L);
    }

    @Test
    @DisplayName("INIT 상태에서는 파일의 기존 값을 읽어 블록 단위로 이어간다")
    void readsExistingValueAfterInit() {
        DailyFileSequenceGenerator gen = new DailyFileSequenceGenerator(tempDir);
        gen.next(day); // 파일에 0 기록, curValue=1

        gen.setCurValue(AbstractSequenceGenerator.INIT_VALUE); // -1

        // 파일의 "0"을 읽어 0 + BLOCK_SIZE(10) = 10 으로 설정 후 증가 -> 11
        assertThat(gen.next(day)).isEqualTo(11L);
    }

    @Test
    @DisplayName("maxValue를 초과하면 시퀀스를 INIT으로 리셋한다")
    void resetsWhenExceedingMax() {
        DailyFileSequenceGenerator gen = new DailyFileSequenceGenerator(tempDir, 0L, 10);

        assertThat(gen.next(day)).isEqualTo(1L); // 0 -> 유효(0<=0) -> 1
        assertThat(gen.next(day)).isEqualTo(0L); // 1 > max(0) -> INIT(-1) -> 0
    }

    @Test
    @DisplayName("maxValue 2-인자 생성자도 동작한다")
    void twoArgConstructor() {
        DailyFileSequenceGenerator gen = new DailyFileSequenceGenerator(tempDir, 50L);

        assertThat(gen.next(day)).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 baseDir은 생성자에서 만든다")
    void createsBaseDirIfMissing() {
        Path missing = tempDir.resolve("not-exist");
        assertThat(Files.exists(missing)).isFalse();

        new DailyFileSequenceGenerator(missing);

        assertThat(Files.exists(missing)).isTrue();
    }

    @Test
    @DisplayName("시퀀스 파일 경로를 해석하지 못하면 IllegalStateException을 던진다")
    void resolveFileNull_throws() {
        DailyFileSequenceGenerator gen = new DailyFileSequenceGenerator(tempDir) {
            @Override
            protected Path resolveFile(LocalDate d) {
                return null;
            }
        };

        assertThatThrownBy(() -> gen.next(day)).isInstanceOf(IllegalStateException.class);
    }
}
