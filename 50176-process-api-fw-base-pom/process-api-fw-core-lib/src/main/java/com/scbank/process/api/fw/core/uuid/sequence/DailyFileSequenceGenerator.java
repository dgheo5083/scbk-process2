package com.scbank.process.api.fw.core.uuid.sequence;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.scbank.process.api.fw.core.error.FrameworkErrorCode;
import com.scbank.process.api.fw.core.exception.FrameworkRuntimeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 일자 파일 기반 시퀀스 생성자 추상 클래스
 * yyyyMMdd 기준으로 매일
 */
@Slf4j
public class DailyFileSequenceGenerator extends AbstractSequenceGenerator implements IDailySequenceGenerator {

    protected final Path baseDir;
    protected final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyyMMdd");
    protected static final int BLOCK_SIZE = 10;

    private Object lock = new Object();
    
    /**
     * 생성자
     * 
     * @param baseDir 기본 디렉토리
     */
    public DailyFileSequenceGenerator(Path baseDir) {
        this(baseDir, DEFAULT_MAX_VALUE, 10);
    }

    /**
     * 생성자
     * 
     * @param baseDir
     * @param maxValue
     */
    public DailyFileSequenceGenerator(Path baseDir, long maxValue) {
        this(baseDir, DEFAULT_MAX_VALUE, 10);
    }

    /**
     * 생성자
     * 
     * @param baseDir
     * @param maxValue
     */
    public DailyFileSequenceGenerator(Path baseDir, long maxValue, int blockSize) {
        super(maxValue);
        this.baseDir = baseDir;
        try {
            // base directory 가 없는 경우 생성
            if (!Files.exists(baseDir)) {
                Files.createDirectories(baseDir);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public long next() {
    	synchronized (lock) {
    		return this.next(LocalDate.now());
		}
    }

    @Override
    public long next(LocalDate day) {
        try {
            Path file = this.resolveFile(day);
            if (file == null) {
                throw new FrameworkRuntimeException(FrameworkErrorCode.INTERNAL_ERROR.getCode(),
                        "Failed to resolve sequence file path");
            }

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "rw")) {
                FileChannel ch = raf.getChannel();
                FileLock lock = ch.lock();
                try {
                    if (this.curValue.get() == INIT_VALUE && file.toFile().exists()) {
                        long val = this.readLong(ch);
                        this.setCurValue(val + BLOCK_SIZE);
                    }

                    if (this.isWritable(this.curValue.get())) {
                        this.writeLong(ch, this.curValue.get());
                    }

                    if (!this.isValid(this.curValue.get())) {
                        this.curValue.set(INIT_VALUE);
                    }

                    return this.curValue.incrementAndGet();
                } finally {
                    if (lock.isValid()) {
                        lock.release();
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected boolean isWritable(long next) {
        return next % BLOCK_SIZE == 0;
    }

    protected Path dayDir(LocalDate day) {
        return baseDir.resolve(DAY.format(day));
    }

    protected Path resolveFile(LocalDate day) {
        return dayDir(day).resolve("sequence.txt");
    }

    /**
     * 
     * @param ch
     * @return
     * @throws IOException
     */
    private long readLong(FileChannel ch) throws IOException {
        if (ch.size() <= 0) {
            return 0L;
        }

        ByteBuffer buf = ByteBuffer.allocate((int) ch.size());
        ch.read(buf, 0);
        buf.flip();

        String s = StandardCharsets.UTF_8.decode(buf).toString().trim();
        return s.isEmpty() ? 0L : Long.parseLong(s);
    }

    /**
     * 
     * @param ch
     * @param value
     * @throws IOException
     */
    private void writeLong(FileChannel ch, long value) throws IOException {
        byte[] out = Long.toString(value).getBytes(StandardCharsets.UTF_8);
        ch.truncate(0);
        ch.write(ByteBuffer.wrap(out), 0);
    }
}
