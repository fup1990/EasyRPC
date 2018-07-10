package com.gome.fup.easy.rpc.remoting;

import com.gome.fup.easy.rpc.common.thread.EasyThreadFactory;
import com.gome.fup.easy.rpc.remoting.config.RemotingConfig;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingRequest;
import com.gome.fup.easy.rpc.remoting.protocol.RemotingResponse;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by fupeng-ds on 2018/7/10.
 */
public abstract class AbstractRemotingService {

    private static final Logger log = LoggerFactory.getLogger(AbstractRemotingService.class);

    protected ConcurrentMap<Integer, RequestProcessor> processorMap = new ConcurrentHashMap<Integer, RequestProcessor>();

    private ExecutorService executorService = Executors.newFixedThreadPool(RemotingConfig.THREAD_NUM, new EasyThreadFactory("RequestProcessorThread_"));

    public void registerProcessor(int requestCode, RequestProcessor processor) {
        processorMap.put(requestCode, processor);
    }

    public RemotingResponse processRequest(final ChannelHandlerContext ctx, final RemotingRequest request) throws Exception {
        final RequestProcessor processor = processorMap.get(request.getHeaderCode());
        if (processor == null) {
            Future<RemotingResponse> future = executorService.submit(new Callable<RemotingResponse>() {
                public RemotingResponse call() throws Exception {
                    return processor.processRequest(ctx, request);
                }
            });
            return future.get();
        }
        log.warn("process request is null, request header code is {]", request.getHeaderCode());
        return null;
    }
}
