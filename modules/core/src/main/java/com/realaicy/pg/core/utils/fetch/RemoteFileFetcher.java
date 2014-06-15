package com.realaicy.pg.core.utils.fetch;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 定时抓取远程文件
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class RemoteFileFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteFileFetcher.class);
    private static final ScheduledExecutorService scheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor(new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    return new Thread(r, "RemoteFileFetcher_Schedule_Thread");
                }
            });

    private byte[] fileConent;
    private String url;
    private long lastModified;
    private FileChangeListener listener;

    /**
     * @param url 文件的url
     * @param reloadInterval 重新下载的时间间隔
     * @param listener 监听器
     */
    private RemoteFileFetcher(String url, int reloadInterval,
                              FileChangeListener listener) {
        //int connectTimeout = 1000;
        //int readTimeout = 1000;

        this.url = url;
        this.listener = listener;
        if (reloadInterval > 0) {
            scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                public void run() {
                    RemoteFileFetcher.this.doFetch();
                }
            }, reloadInterval, reloadInterval, TimeUnit.MILLISECONDS);
        }
        doFetch();
    }

    public static RemoteFileFetcher createPeriodFetcher(String url,
                                                        int reloadInterval, FileChangeListener listener) {
        return new RemoteFileFetcher(url, reloadInterval, listener);
    }

    public long getLastModified() {
        return this.lastModified;
    }

    public byte[] getFileByteArray() {
        return this.fileConent;
    }

    private void doFetch() {
        if (url == null) {
            return;
        }
        LOGGER.info("Begin fetch remote file... url = {}", this.url);
        try {
            URL target = new URL(this.url);
            this.fileConent = IOUtils.toByteArray(target);
            this.lastModified = System.currentTimeMillis();
            if (this.listener != null && this.fileConent != null) {
                this.listener.fileReloaded(this.fileConent);
            }
        } catch (Exception e) {
            LOGGER.error("read from url failed", e);
        }
    }

    public interface FileChangeListener {
        public abstract void fileReloaded(byte[] contentBytes)
                throws IOException;
    }

}
