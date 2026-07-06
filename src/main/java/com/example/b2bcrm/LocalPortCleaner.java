package com.example.b2bcrm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.LinkedHashSet;
import java.util.Set;

final class LocalPortCleaner {

    private static final int DEFAULT_PORT = 8080;

    private LocalPortCleaner() {
    }

    static void killProcessOnServerPort(String[] args) {
        if (!isWindows() || isDisabled()) {
            return;
        }

        int port = resolvePort(args);
        Set<String> processIds = findListeningProcessIds(port);

        for (String processId : processIds) {
            if (processId.equals(currentProcessId())) {
                continue;
            }

            taskkill(processId, port);
        }
    }

    private static int resolvePort(String[] args) {
        String fromArgs = findServerPortArgument(args);
        if (fromArgs != null) {
            return parsePort(fromArgs);
        }

        String fromSystemProperty = System.getProperty("server.port");
        if (fromSystemProperty != null && !fromSystemProperty.trim().isEmpty()) {
            return parsePort(fromSystemProperty);
        }

        String fromEnv = System.getenv("SERVER_PORT");
        if (fromEnv != null && !fromEnv.trim().isEmpty()) {
            return parsePort(fromEnv);
        }

        return DEFAULT_PORT;
    }

    private static String findServerPortArgument(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--server.port=")) {
                return arg.substring("--server.port=".length());
            }
        }
        return null;
    }

    private static int parsePort(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ignored) {
            return DEFAULT_PORT;
        }
    }

    private static Set<String> findListeningProcessIds(int port) {
        Set<String> processIds = new LinkedHashSet<>();
        ProcessBuilder processBuilder = new ProcessBuilder(
            "cmd.exe",
            "/c",
            "netstat -ano | findstr LISTENING | findstr :" + port
        );

        try {
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String processId = extractProcessId(line, port);
                    if (processId != null) {
                        processIds.add(processId);
                    }
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.out.println("Could not inspect local port " + port + ": " + ex.getMessage());
        }

        return processIds;
    }

    private static String extractProcessId(String line, int port) {
        String trimmed = line.trim();
        if (!trimmed.contains(":" + port) || !trimmed.contains("LISTENING")) {
            return null;
        }

        String[] parts = trimmed.split("\\s+");
        return parts.length == 0 ? null : parts[parts.length - 1];
    }

    private static void taskkill(String processId, int port) {
        try {
            Process process = new ProcessBuilder("taskkill", "/PID", processId, "/F").start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Stopped existing local process " + processId + " on port " + port + ".");
            }
        } catch (IOException | InterruptedException ex) {
            Thread.currentThread().interrupt();
            System.out.println("Could not stop process " + processId + " on port " + port + ": " + ex.getMessage());
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }

    private static boolean isDisabled() {
        return "false".equalsIgnoreCase(System.getProperty("local.port.cleaner.enabled"))
            || "false".equalsIgnoreCase(System.getenv("LOCAL_PORT_CLEANER_ENABLED"));
    }

    private static String currentProcessId() {
        String runtimeName = ManagementFactory.getRuntimeMXBean().getName();
        int separatorIndex = runtimeName.indexOf('@');
        return separatorIndex > 0 ? runtimeName.substring(0, separatorIndex) : runtimeName;
    }
}
