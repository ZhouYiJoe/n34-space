package com.n34.demo.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 持久化微博内容到磁盘上
 */
@Repository
public class FileRepository {
    private static final int BUFFER_SIZE = 10240;

    @Value("${n34.resource.resource-dir-path}")
    private String resourceDirPath;
    @Value("${n34.resource.post-dir-name}")
    private String postDirName;

    /**
     * 创建本应用的资源文件夹
     * @return 指向本应用的资源文件夹的File对象
     */
    private File getResourceDir() {
        File resourceDir = new File(resourceDirPath);
        if (!resourceDir.exists()) {
            resourceDir.mkdir();
        }
        return resourceDir;
    }

    /**
     * 创建存放博文文件的文件夹
     * @return 指向存放博文文件的文件夹的File对象
     */
    private File getPostDir() {
        File resourceDir = getResourceDir();
        File postDir = new File(resourceDir, postDirName);
        if (!postDir.exists()) {
            postDir.mkdir();
        }
        return postDir;
    }

    /**
     * 为新添加的博文创建用来存放博文内容的txt文件
     */
    public void createPostFile(String filename, String postBody) throws Exception {
        File dir = getPostDir();
        File post = new File(dir, filename + ".txt");
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(post));
        byte[] bytes = postBody.getBytes(StandardCharsets.UTF_8);
        out.write(bytes, 0, bytes.length);
        out.flush();
        out.close();
    }

    public String getPostFileContent(String filename) throws Exception {
        StringBuilder content = new StringBuilder();
        File post = new File(getPostDir(), filename + ".txt");
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(post));
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            content.append(new String(buffer, 0, len));
        }
        in.close();
        return content.toString();
    }

    public void deletePostFile(String filename) {
        File post = new File(getPostDir(), filename + ".txt");
        post.delete();
    }
}
