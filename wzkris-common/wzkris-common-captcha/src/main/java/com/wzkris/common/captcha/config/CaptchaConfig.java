package com.wzkris.common.captcha.config;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 验证码配置
 *
 * @author wzkris
 */
@Data
@RefreshScope
public class CaptchaConfig {

    @Autowired
    private ResourceStore resourceStore;

    private static final String resourceDir = "static";

    @PostConstruct
    public void init() throws URISyntaxException, IOException {
        // 加载目录下的所有文件
        List<String> filePaths = loadResourceFilesFromAllJars();
        for (String filePath : filePaths) {
            if (filePath.endsWith("jpg")) {
                resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", filePath, "default"));
                resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", filePath, "default"));
                resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", filePath, "default"));
                resourceStore.addResource(
                        CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", filePath, "default"));
            }
        }
    }

    /**
     * 从所有jar包中读取文件
     */
    private List<String> loadResourceFilesFromAllJars() throws URISyntaxException, IOException {
        List<String> filePaths = new ArrayList<>();

        // 获取当前线程的 ClassLoader（通常是 URLClassLoader）
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // 遍历 ClassLoader 中的所有 URL
        if (classLoader instanceof URLClassLoader urlClassLoader) {
            for (URL url : urlClassLoader.getURLs()) {
                if (url.getProtocol().equals("file")) {
                    // 如果是文件系统中的 JAR 文件
                    try (JarFile jarFile = new JarFile(url.getFile())) {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.startsWith(resourceDir + "/") && !entry.isDirectory()) {
                                filePaths.add(entryName);
                            }
                        }
                    }
                } else if (url.getProtocol().equals("jar")) {
                    // 如果是 JAR 文件中的资源
                    JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
                    try (JarFile jarFile = jarConnection.getJarFile()) {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.startsWith(resourceDir + "/") && !entry.isDirectory()) {
                                filePaths.add(entryName);
                            }
                        }
                    }
                }
            }
        } else {
            // 如果不是 URLClassLoader，尝试直接加载资源
            Enumeration<URL> resources = classLoader.getResources(resourceDir);
            while (resources.hasMoreElements()) {
                URL resourceUrl = resources.nextElement();
                if (resourceUrl.getProtocol().equals("jar")) {
                    // 处理 JAR 文件中的资源
                    JarURLConnection jarConnection = (JarURLConnection) resourceUrl.openConnection();
                    try (JarFile jarFile = jarConnection.getJarFile()) {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.startsWith(resourceDir + "/") && !entry.isDirectory()) {
                                filePaths.add(entryName);
                            }
                        }
                    }
                } else if (resourceUrl.getProtocol().equals("file")) {
                    // 处理文件系统中的资源
                    Path resourcePath = Paths.get(resourceUrl.toURI());
                    try (Stream<Path> paths = Files.walk(resourcePath)) {
                        paths.filter(Files::isRegularFile).forEach(path -> {
                            String filePath =
                                    resourceDir + "/" + path.getFileName().toString();
                            filePaths.add(filePath);
                        });
                    }
                }
            }
        }

        return filePaths;
    }
}
