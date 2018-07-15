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
 * Created by fupeng-ds on 2018/7/11.
 */
public class SnapLog {

    private static final Logger log = LoggerFactory.getLogger(SnapLog.class);

    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    private FileChannel channel;

    private RandomAccessFile file;

    private AtomicLong writePosition;

    public SnapLog() {
        try {
            File f = new File(NameServerConfig.LOG_PATh);
            if (!f.exists()) {
                f.mkdir();
                log.info("make dir {}", NameServerConfig.LOG_PATh);
            }
            this.file = new RandomAccessFile(new File(NameServerConfig.SNAPLOG_PATH), "rw");
            this.channel = file.getChannel();
            log.info("create sanplog file channel success, file size is {}", channel.size());
            writePosition = new AtomicLong(channel.size());
            log.info("snaplog file write position is {}", writePosition.get());
        } catch (Exception e) {
            log.error("create snaplog fail", e);
            throw new RuntimeException(e);
        }

    }

    public boolean appendMessage(byte[] bytes) {
        boolean flag = false;
        buffer.put(bytes);
        buffer.flip();
        try {
            commit();
            flag = true;
        } catch (Exception e) {
            log.error("append message fail", e);
        }
        return flag;
    }

    public void commit() throws IOException {
        long pos = writePosition.get();
        int length = buffer.limit();
        channel.position(pos);
        channel.write(buffer);
        buffer.compact();
        writePosition.addAndGet(length);
    }


}
