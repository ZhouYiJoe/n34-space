package com.n34.demo.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    private static final int BUFFER_SIZE = 10240;
    private static final String CLASSPATH;

    static {
        String temp = null;
        try {
            temp = new ClassPathResource("").getFile().getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CLASSPATH = temp;
    }

    /**
     * 为新添加的博文创建用来存放博文内容的txt文件
     */
    public static void createPostFile(String filename, String postBody) throws Exception {
        File dir = new File(CLASSPATH, "post");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File post = new File(dir, filename + ".txt");
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(post));
        byte[] bytes = postBody.getBytes(StandardCharsets.UTF_8);
        out.write(bytes, 0, bytes.length);
        out.flush();
        out.close();
    }

    public static String getPostFileContent(String filename) throws Exception {
        StringBuilder content = new StringBuilder();
        File post = new File(CLASSPATH + File.separator + "post", filename + ".txt");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(post));
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            content.append(new String(buffer, 0, len));
        }
        in.close();
        return content.toString();
    }

    public static void deletePostFile(String filename) {
        File post = new File(CLASSPATH + File.separator + "post", filename + ".txt");
        post.delete();
    }
}
