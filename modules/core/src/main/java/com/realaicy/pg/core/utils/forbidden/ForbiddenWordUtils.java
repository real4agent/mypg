/**
 * Copyright (c) 2005-2012 https://github.com/zhangkaitao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.realaicy.pg.core.utils.forbidden;

import com.google.common.collect.Lists;
import com.realaicy.pg.core.Constants;
import com.realaicy.pg.core.utils.fetch.RemoteFileFetcher;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 屏蔽关键词 工具类
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public class ForbiddenWordUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForbiddenWordUtils.class);

    /**
     * 默认的遮罩文字
     */
    private static final String DEFAULT_MASK = "***";
    /**
     * 屏蔽关键词抓取的url
     */
    private static String forbiddenWordFetchURL;

    /**
     * 屏蔽关键词抓取时间间隔 毫秒
     */
    private static int reloadInterval = 60000; //10分钟

    /**
     * 屏蔽关键词
     */
    private static List<Pattern> forbiddenWords;

    public static void setForbiddenWordFetchURL(String forbiddenWordFetchURL) {
        ForbiddenWordUtils.forbiddenWordFetchURL = forbiddenWordFetchURL;
    }

    public static void setReloadInterval(int reloadInterval) {
        ForbiddenWordUtils.reloadInterval = reloadInterval;
    }

    /**
     * 替换input中的屏蔽关键词为默认的掩码
     *
     * @param input 输入字符串
     * @return 替换之后的字符串
     */
    public static String replace(String input) {
        return replace(input, DEFAULT_MASK);
    }

    /**
     * 将屏蔽关键词 替换为 mask
     *
     * @param input 输入字符串
     * @param mask 替换的掩码
     * @return 替换之后的字符串
     */
    public static String replace(String input, String mask) {
        for (Pattern forbiddenWordPattern : forbiddenWords) {
            input = forbiddenWordPattern.matcher(input).replaceAll(mask);
        }
        return input;
    }

    /**
     * 是否包含屏蔽关键词
     *
     * @param input 输入字符串
     * @return 如果含有关键词则返回真
     */
    public static boolean containsForbiddenWord(String input) {
        for (Pattern forbiddenWordPattern : forbiddenWords) {
            if (forbiddenWordPattern.matcher(input).find()) {
                return true;
            }
        }
        return false;
    }

    static {
        InputStream is = null;
        try {
            String fileName = "forbidden.txt";
            is = ForbiddenWordUtils.class.getResourceAsStream(fileName);
            byte[] fileCBytes;
            fileCBytes = IOUtils.toByteArray(is);
            ForbiddenWordUtils.loadForbiddenWords(fileCBytes);
        } catch (IOException e) {
            LOGGER.error("read forbidden file failed", e);
        } finally {
            IOUtils.closeQuietly(is);
        }

    }

    /**
     * 初始化远程抓取配置
     */
    public static void initRemoteFetch() {
        RemoteFileFetcher.createPeriodFetcher(
                forbiddenWordFetchURL,
                reloadInterval,
                new RemoteFileFetcher.FileChangeListener() {
                    public void fileReloaded(byte[] fileConent) throws IOException {
                        ForbiddenWordUtils.loadForbiddenWords(fileConent);
                    }
                });
    }

    private static void loadForbiddenWords(byte[] fileCBytes) throws IOException {
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileCBytes), Constants.ENCODING));
            List<String> forbiddenWordsStrList = IOUtils.readLines(reader);
            forbiddenWords = Lists.newArrayList();
            for (int i = forbiddenWordsStrList.size() - 1; i >= 0; i--) {
                String forbiddenWord = forbiddenWordsStrList.get(i).trim();
                if (forbiddenWord.length() != 0 && !forbiddenWord.startsWith("#")) {
                    forbiddenWords.add(Pattern.compile(forbiddenWord));
                }
            }
        } catch (Exception e) {
            LOGGER.error("load forbidden words failed", e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

}
