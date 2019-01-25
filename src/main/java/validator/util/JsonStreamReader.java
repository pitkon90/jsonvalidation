package validator.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

/**
 * @author Balázs Ludrik
 */
public class JsonStreamReader {

    private static final JsonFactory factory = new JsonFactory();
    private String endpoint;
    private InputStream input;
    private JsonParser parser;
    private JsonToken actualToken;


    private Integer nullValuesCount;

    public JsonStreamReader() {
        this.endpoint = endpoint;
    }

    public Integer getNullValuesCount() {
        return nullValuesCount;
    }

    /**
     * Opens the endpoint in a stream, creates the parser object and sets the token location for the beginning of the first node
     * @throws Exception
     */
    public void openStream(String endpoint) throws Exception {
        this.input = new URL(endpoint).openStream();
        init();
    }

    /**
     * Opens the endpoint in a stream, creates the parser object and sets the token location for the beginning of the first node
     * @param jsonFile
     * @throws Exception
     */
    public void openStream(File jsonFile) throws Exception {
        this.input = new FileInputStream(jsonFile);
        init();
    }

    private void init() throws IOException {
        this.parser = factory.createParser(input);
        this.nullValuesCount = 0;
        setPositionToFirstNode();
    }

    /**
     * Sets token location to the first node object
     * @throws IOException
     */
    private void setPositionToFirstNode() throws IOException {
        do {
            actualToken = parser.nextToken();
        }while(actualToken!=null && !actualToken.equals(JsonToken.START_OBJECT));
    }

    /**
     * Verifies that we have next node in the stream.
     * @return  true - we have next node, false - we are at the end of the stream
     */
    public Boolean isNextNodeAvailable() throws IOException {
        while(actualToken!= null && isClosingTail()) {
            actualToken = parser.nextToken();
        }
        return (actualToken != null && !actualToken.equals(JsonToken.END_ARRAY)) ? true : false;
    }

    /**
     * Helper to remove unneccessary comma tail.
     * @param builder
     * @return
     */
    private StringBuilder removeTailIfNeeded(StringBuilder builder) {
        int ObjectLength = builder.length();
        if( builder.substring(ObjectLength-1).equals(",")) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder;
    }

    /**
     * Is the actual token is end of an array or end of an object
     * @return
     */
    private Boolean isClosingTail() {
        return (actualToken.equals(JsonToken.END_ARRAY) || actualToken.equals(JsonToken.END_OBJECT)) ? true : false;
    }

    /**
     * Creates JsonNode object from Json string
     * @param jsonString
     * @return
     * @throws IOException
     */
    private JsonNode creatNode(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }
    /**
     * Retrieves the next node element
     * @return Optional<JsonNode> element
     * @throws IOException
     */
    public Optional<JsonNode> nextNode() throws IOException {
        if (isNextNodeAvailable()) {
            if (actualToken.equals(JsonToken.START_OBJECT)) {
                //We start the object here
                StringBuilder nodeBuilder = new StringBuilder();
                nodeBuilder.append("{");
                actualToken = parser.nextToken();
                while (!(actualToken == null)) {
                    //One full node
                    switch (actualToken) {
                        case START_OBJECT:
                            Optional<JsonNode>nestedNode = this.nextNode();
                            if(nestedNode.isPresent()) {
                                nodeBuilder.append(nestedNode.get().toString()+",");
                            } else {
                                nodeBuilder.append("{},");
                            }
                            break;
                        case END_OBJECT:
                            nodeBuilder = removeTailIfNeeded(nodeBuilder);
                            nodeBuilder.append("}");
                            return Optional.ofNullable(creatNode(nodeBuilder.toString()));
                        case START_ARRAY:
                            nodeBuilder.append(" [");       //opening to array
                            break;
                        case END_ARRAY:
                            nodeBuilder = removeTailIfNeeded(nodeBuilder);
                            nodeBuilder.append("]").append(",");                    //appending closure to array
                            break;
                        case FIELD_NAME:
                            nodeBuilder.append("\"").append(parser.getCurrentName()).append("\"").append(":");
                            break;
                        case VALUE_STRING:
                            String value = parser.getValueAsString().replace("\"", "\\\"");  //escaping double quote(") characters
                            nodeBuilder.append(" \"").append(value).append("\"").append(",");
                            break;
                        case VALUE_NUMBER_INT:
                            nodeBuilder.append(parser.getValueAsString()).append(",");
                            break;
                        case VALUE_NUMBER_FLOAT:
                            nodeBuilder.append(parser.getValueAsString()).append(",");
                            break;
                        case VALUE_TRUE:
                            break;
                        case VALUE_FALSE:
                            break;
                        case VALUE_NULL:
                            nodeBuilder.append("null").append(",");
                            nullValuesCount = getNullValuesCount()+1;      //COUNTING the null values / Task 3
                            break;
                        default: break;
                    }
                    actualToken = parser.nextToken();

                }
            } else {

            }
        }
        return Optional.ofNullable(null);
    }
}
