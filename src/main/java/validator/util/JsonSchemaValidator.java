package validator.util;

import validator.model.College;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author Balázs Ludrik
 */
public class JsonSchemaValidator {

    private static final Logger logger = Logger.getLogger(JsonSchemaValidator.class.getName());

    private JsonSchemaManager jsonSchemaManager;
    private HashMap<String, Integer> errorsMap;
    private Integer countOfParsedObjects = 0;
    private JsonStreamReader streamReader;

    public int getCountOfNullValues() {
        return streamReader.getNullValuesCount();
    }

    public HashMap<String, Integer> getErrorsMap() {
        return errorsMap;
    }

    public Integer getCountOfParsedObjects() {
        return countOfParsedObjects;
    }

    /**
     * Constructor
     */
    public JsonSchemaValidator() {
        errorsMap = new HashMap<String, Integer>();
        streamReader = new JsonStreamReader();
    }

    //Answers for Task 1, 2, 3
    public void printResults() {
        logger.info("Successfully parsed objects count: " + countOfParsedObjects + "\n");
        if(errorsMap.isEmpty()) {
            logger.info("No error has been encountered during validation.\n");
        } else {
            logger.severe("Errors during parsing with quantity: \n");
            errorsMap.forEach((error,count)->logger.severe(error + " -> Occurence: " + count + "\n"));
        }
        logger.info("Null values in all nodes: " + streamReader.getNullValuesCount().toString());

    }

    /**
     * Runs validation on File
     * @param jsonFile      File: json File input
     * @param schemaPath    String: json schema input
     * @throws Exception
     */
    public void runValidation(File jsonFile, String schemaPath) throws Exception {
        streamReader.openStream(jsonFile);
        runValidation(schemaPath);
    }

    /**
     * Runs validation on Endpoint
     * @param endpoint      String: endpoint url
     * @param schemaPath    String: json schema input
     * @throws Exception
     */
    public void  runValidation(String endpoint, String schemaPath) throws Exception {
        streamReader.openStream(endpoint);
        runValidation(schemaPath);
    }

    /**
     * Helper method to run validation
     * @param schemaPath
     * @throws Exception
     */
    private void runValidation(String schemaPath) throws Exception {
        loadSchema(schemaPath);
        goThroughStreamAndValidate(streamReader);
    }

    /**
     * Loads the Json schema from file
     * @param schemaPath    String: resource json schema file path
     * @throws IOException
     */
    private void loadSchema(String schemaPath) throws IOException {
        jsonSchemaManager = new JsonSchemaManager();
        jsonSchemaManager.load(College.class, schemaPath);
    }

    /**
     * Helper method to iterate through all the nodes and validate them.
     * @param streamReader      JsonStreamReader: stream to read the node from
     * @throws IOException
     */
    private void goThroughStreamAndValidate(JsonStreamReader streamReader) throws IOException {
        while (streamReader.isNextNodeAvailable()) {
            validateNode(streamReader.nextNode());
        }
    }

    /**
     * Validates a json object
     * @param nodeToValidate    JsonNode: node to validate
     */
    public void validateNode(Optional<JsonNode> nodeToValidate){
        if(nodeToValidate.isPresent()) {
            try {
                jsonSchemaManager.check(College.class, nodeToValidate.get());
                countOfParsedObjects++;     //counting the parsed objects / Task 1
            } catch(Exception e) {
                // Showing the errors with occurences / Task 2
                String errorMessage = e.getMessage().split("\n")[0];
                if(errorsMap.containsKey(errorMessage)) {
                    //Error has already been encountered before
                    errorsMap.put(errorMessage, (errorsMap.get( errorMessage ).intValue())+1);
                } else {
                    errorsMap.put(errorMessage, 1);
                }
            }
        }

    }
}
