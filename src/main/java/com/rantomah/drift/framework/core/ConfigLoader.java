package com.rantomah.drift.framework.core;

import com.rantomah.drift.framework.core.exception.DriftException;
import com.rantomah.drift.framework.core.impl.PrefixedEnvironment;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.Yaml;

public class ConfigLoader {

    private static final String BASE_FILENAME = "application";
    private static final String EXTENTION = ".yml";
    private static final String PROFILE_ENV_VARNAME = "DRIFT_PROFILE";
    private static final String PROFILE_PROPERTY_NAME = "drift.profile";

    private ConfigLoader() {}

    public static PrefixedEnvironment load() {
        String profile = detectProfile();
        Map<String, Object> defaultConfig = loadYaml(BASE_FILENAME + EXTENTION);
        Map<String, Object> profileConfig = Collections.emptyMap();
        if (profile != null) {
            profileConfig = loadYaml(BASE_FILENAME + "-" + profile + EXTENTION);
        }
        Map<String, Object> merged = merge(defaultConfig, profileConfig);
        return new PrefixedEnvironment(profile, merged);
    }

    private static String detectProfile() {
        String cliProfile = System.getProperty(PROFILE_PROPERTY_NAME);
        if (cliProfile != null && !cliProfile.isBlank()) {
            return cliProfile.trim().toLowerCase();
        }
        String envProfile = System.getenv(PROFILE_ENV_VARNAME);
        if (envProfile != null && !envProfile.isBlank()) {
            return envProfile.trim().toLowerCase();
        }
        return "default";
    }

    private static String normalizePlaceholders(String yamlContent) {
        Pattern p = Pattern.compile("\\$\\{\\s*([A-Za-z0-9_]+)\\s*}");
        Matcher m = p.matcher(yamlContent);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String key = m.group(1).trim();
            m.appendReplacement(sb, "\\${" + key + "}");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String expandWithEnvLoop(String yaml, Map<String, String> env) {
        String expanded = yaml;
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String key = entry.getKey().trim();
            String value = entry.getValue();
            String placeholder = "${" + key + "}";
            expanded = expanded.replace(placeholder, value);
        }
        return expanded;
    }

    public static Map<String, String> loadEnvFile(String fileName) {
        Dotenv dotenv =
                Dotenv.configure().filename(fileName).ignoreIfMalformed().ignoreIfMissing().load();
        Map<String, String> map = new HashMap<>();
        for (DotenvEntry entry : dotenv.entries()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isBlank()) {
                map.put(key.trim(), value.trim());
            }
        }
        return map;
    }

    private static Map<String, Object> loadYaml(String fileName) {
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                return Collections.emptyMap();
            }
            String rawYaml = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            String normalized = normalizePlaceholders(rawYaml);
            String expanded = expandWithEnvLoop(normalized, loadEnvFile(".env"));
            Yaml yaml = new Yaml();
            return yaml.loadAs(expanded, Map.class);
        } catch (Exception e) {
            throw new DriftException("Failed to load YAML: " + fileName, e);
        }
    }

    private static Map<String, Object> merge(
            Map<String, Object> base, Map<String, Object> override) {
        Map<String, Object> result = new LinkedHashMap<>(base);
        for (Map.Entry<String, Object> entry : override.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map && result.get(key) instanceof Map) {
                Map<String, Object> merged =
                        merge((Map<String, Object>) result.get(key), (Map<String, Object>) value);
                result.put(key, merged);
            } else {
                result.put(key, value);
            }
        }
        return result;
    }
}
