package validator.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Balázs Ludrik
 */
public class JsonSchemaManager {
    private final JsonValidator jsonValidator = JsonSchemaFactory.byDefault().getValidator();
    private Map<Class<?>, JsonNode> jsonNodeMap = new HashMap<>();

    public void load(Class<?> className, String schema) throws IOException {
        JsonNode jsonSchema = JsonLoader.fromResource(schema);
        jsonNodeMap.put(className, jsonSchema);
    }

    public Boolean check(Class<?> className, JsonNode toBeValidated) throws Exception {
        ProcessingReport report = null;

        report = jsonValidator.validate(jsonNodeMap.get(className), toBeValidated);
        if (!report.isSuccess()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<ProcessingMessage> messages = Lists.newArrayList(report);
            for (int i = 0; i < messages.size(); i++) {
                stringBuilder.append("- ");
                stringBuilder.append(messages.get(i).toString());
                stringBuilder.append((i == (messages.size()) - 1) ? "" : "\r");
            }
            throw new RuntimeException(stringBuilder.toString());
        } else {
            return true;
        }
    }

}
