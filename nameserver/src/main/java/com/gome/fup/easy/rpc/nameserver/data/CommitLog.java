package com.gome.fup.easy.rpc.nameserver.data;

import com.gome.fup.easy.rpc.nameserver.config.NameServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 记录操作日志
 * Created by fupeng-ds on 2018/7/11.
 */
public class CommitLog {

    private static final Logger log = LoggerFactory.getLogger(CommitLog.class);

    protected FileChannel channel;

    protected AtomicLong writePosition;

    public CommitLog() {
        try {
            File f = new File(NameServerConfig.LOG_PATh);
            if (!f.exists()) {
                f.mkdir();
                log.info("make dir {}", NameServerConfig.LOG_PATh);
            }
            RandomAccessFile file = new RandomAccessFile(new File(NameServerConfig.COMMIT_LOG), "rw");
            channel = file.getChannel();
            log.info("create sanplog file channel success, file size is {}", channel.size());
            writePosition = new AtomicLong(channel.size());
            log.info("snaplog file write position is {}", writePosition.get());
        } catch (Exception e) {
            log.error("create snaplog fail", e);
            throw new RuntimeException(e);
        }
    }

    public boolean write(byte[] bytes) {
        boolean flag = false;
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        try {
            long pos = writePosition.get();
            int length = buffer.limit();
            channel.position(pos);
            channel.write(buffer);
            buffer.compact();
            writePosition.addAndGet(length);
            flag = true;
        } catch (Exception e) {
            log.error("append message fail", e);
        }
        return flag;
    }

    public byte[] read(long pos, int size) {
        byte[] bytes = null;
        ByteBuffer buffer = ByteBuffer.allocate(size);
        try {
            channel.read(buffer, pos);
            buffer.flip();
            int limit = buffer.limit();
            bytes = new byte[limit];
            while (buffer.hasRemaining()) {
                buffer.get(bytes);
            }
        } catch (IOException e) {
            log.error("read message fail", e);
        }
        return bytes;
    }

    public void shutdown() {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                log.error("channel shutdown fail!");
                throw new RuntimeException(e);
            }
        }
    }
}
