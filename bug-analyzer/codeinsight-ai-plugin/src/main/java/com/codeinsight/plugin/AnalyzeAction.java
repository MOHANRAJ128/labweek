package com.codeinsight.plugin;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.*;

public class AnalyzeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getProject();

        if (project == null) {
            Messages.showErrorDialog("No project found", "Error");
            return;
        }

        String projectPath = project.getBasePath();

        try {
            // 🔹 Step 1: Ask logs
            FileChooserDescriptor descriptor = new FileChooserDescriptor(
                    true,  // choose files
                    false, // folders
                    false,
                    false,
                    false,
                    false
            );

            descriptor.setTitle("Select Error Log File");

            VirtualFile file = FileChooser.chooseFile(descriptor, project, null);

            if (file == null) {
                Messages.showErrorDialog("No file selected", "Error");
                return;
            }

            File logFile = new File(file.getPath());

            // 🔹 Step 2: Zip project
            File zipFile = zipProject(projectPath);

            // 🔹 Step 3: Call backend
            String response = callBackend(zipFile, logFile);

            Messages.showInfoMessage("AI Response:\n" + response, "CodeInsight AI");

        } catch (Exception ex) {
            Messages.showErrorDialog("Error: " + ex.getMessage(), "Failure");
        }
    }

    // 🔹 ZIP LOGIC
    private File zipProject(String projectPath) throws IOException {

        File sourceDir = new File(projectPath);
        File zipFile = new File(projectPath + File.separator + "project.zip");

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            Path sourcePath = sourceDir.toPath();

            Files.walk(sourcePath)
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> !path.toString().contains(".git"))
                    .filter(path -> !path.toString().contains("target"))
                    .filter(path -> !path.toString().contains("node_modules"))
                    .filter(path -> !path.toString().contains(".idea"))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                        try {
                            zos.putNextEntry(zipEntry);
                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return zipFile;
    }

    // 🔹 MULTIPART API CALL
    private String callBackend(File zipFile, File logFile) throws Exception {

        String boundary = "----CodeInsightBoundary";

        URL url = new URL("http://localhost:8080/ai/debug");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream os = conn.getOutputStream();
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(os), true)) {

            // 🔹 Project name
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"projectName\"\r\n\r\n");
            writer.append("IntelliJProject\r\n");

            // 🔹 Upload ZIP
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"projectFile\"; filename=\"project.zip\"\r\n");
            writer.append("Content-Type: application/zip\r\n\r\n");
            writer.flush();

            Files.copy(zipFile.toPath(), os);
            os.flush();
            writer.append("\r\n");

            // 🔹 Upload LOG FILE
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"logFile\"; filename=\"error.txt\"\r\n");
            writer.append("Content-Type: text/plain\r\n\r\n");
            writer.flush();

            Files.copy(logFile.toPath(), os);
            os.flush();
            writer.append("\r\n");

            // 🔹 End
            writer.append("--").append(boundary).append("--").append("\r\n");
            writer.flush();
        }

        return new String(conn.getInputStream().readAllBytes());
    }
}