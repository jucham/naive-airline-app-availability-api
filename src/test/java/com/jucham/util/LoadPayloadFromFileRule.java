package com.jucham.util;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoadPayloadFromFileRule implements MethodRule {
    private static final String DEFAULT_PAYLOAD_FILE_CONTENT = "{\"expected_content_to_fill\": true}";
    private String payload;

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if (testRequestsToLoadPayloadFromFile(method)) {
                    final String payloadPath = target.getClass().getSimpleName() + "/" + method.getName() + ".json";
                    try {
                        loadPayloadFromFile(payloadPath);
                    } catch (FileNotFoundException ex) {
                        createNewJsonExpectedContentFile(payloadPath);
                    }
                }
                base.evaluate();
            }
        };
    }

    private boolean testRequestsToLoadPayloadFromFile(FrameworkMethod method) {
        final Annotation[] annotations = method.getAnnotations();
        if (annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof LoadPayloadFromFile)
                    return true;
            }
        }
        return false;
    }

    private void loadPayloadFromFile(String pathOfJsonExpectedResponse) throws IOException {
        String path = "classpath:" + pathOfJsonExpectedResponse;
        File file = ResourceUtils.getFile(path);
        payload = new String(Files.readAllBytes(file.toPath()));
    }

    private void createNewJsonExpectedContentFile(String pathOfJsonExpectedResponse) throws IOException {
        String path = "src/test/resources/" + pathOfJsonExpectedResponse;
        Files.createDirectories(Paths.get(path).getParent());
        Path file = Files.createFile(Paths.get(path));
        Files.writeString(file, DEFAULT_PAYLOAD_FILE_CONTENT);
        payload = DEFAULT_PAYLOAD_FILE_CONTENT;
    }

    public String getPayload() {
        return payload;
    }
}
