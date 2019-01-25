package validator;

import java.util.logging.Logger;

import validator.util.JsonStreamReader;
import validator.util.JsonSchemaValidator;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.util.Optional;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {

    private static final Logger logger = Logger.getLogger(JsonSchemaValidator.class.getName());
    private JsonStreamReader streamReader;

    public static void main(String[] args) throws Exception {
        //Reading configuration
        JsonStreamReader streamReader = new JsonStreamReader();
        streamReader.openStream(new File(ClassLoader.getSystemClassLoader().getResource("testData.json").getFile()));

        while (streamReader.isNextNodeAvailable()) {
            JsonSchemaValidator validator = new JsonSchemaValidator();
            Optional<JsonNode> node = streamReader.nextNode();
            if(node.isPresent()) {
                String endpoint = node.get().findValue("endpoint").asText();
                String schemaFile = node.get().findValue("schema-file").asText();

                //Validating Endpoint
                logger.info("Parsing in progress on endpoint: "+endpoint);
                validator.runValidation(endpoint, "/schemas/" + schemaFile);
            }
            validator.printResults();
        }

    }
}