/*
 * This Java source file was generated by the Gradle 'init' task.
 */
import org.junit.Before;
import org.junit.Test;
import validator.util.JsonSchemaValidator;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class AppTest {

    JsonSchemaValidator validator;


    @Before public  void setUp() {
        validator = new JsonSchemaValidator();
    }

    private File loadFile(String filePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        File fileToValidate = new File(classLoader.getResource(filePath).getFile());
        System.out.println(fileToValidate.getAbsolutePath());
        return fileToValidate;
    }

    @Test
    public void testJsonValidationWithSchemaSuccess() throws Exception {
        File fileToValidate = loadFile("files/testSuccess.json");

        validator.runValidation(fileToValidate,"/schemas/college-schema.json");
        assertThat("Number of null values during parsing is different than expected", validator.getCountOfNullValues() , is(8));
        assertThat("Number of parsed objects is different than expected", validator.getCountOfParsedObjects() , is(10));
        assertThat("Number of different parsing errors during parsing is different than expected", validator.getErrorsMap().size() , is(0));
    }

    @Test
    public void testJsonValidationWithSchemaError() throws Exception {
        File fileToValidate = loadFile("files/testError.json");

        validator.runValidation(fileToValidate,"/schemas/college-schema.json");
        assertThat("Number of null values during parsing is different than expected", validator.getCountOfNullValues() , is(8));
        assertThat("Number of parsed objects is different than expected", validator.getCountOfParsedObjects() , is(7));
        assertThat("Number of different parsing errors during parsing is different than expected", validator.getErrorsMap().size() , is(2));
    }

    @Test
    public void testJsonValidationWithEmptyInput() throws Exception {
        File fileToValidate = loadFile("files/testEmpty.json");

        validator.runValidation(fileToValidate,"/schemas/college-schema.json");
        assertThat("Number of null values during parsing is different than expected", validator.getCountOfNullValues() , is(0));
        assertThat("Number of parsed objects is different than expected", validator.getCountOfParsedObjects() , is(0));
        assertThat("Number of different parsing errors during parsing is different than expected", validator.getErrorsMap().size() , is(1));
    }

    @Test
    public void testJsonValidationWithSchemaErrorMissingProperty() throws Exception {
        File fileToValidate = loadFile("files/testErrorMissingProperty.json");

        validator.runValidation(fileToValidate,"/schemas/college-schema.json");
        assertThat("Number of null values during parsing is different than expected", validator.getCountOfNullValues() , is(8));
        assertThat("Number of parsed objects is different than expected", validator.getCountOfParsedObjects() , is(8));
        assertThat("Number of different parsing errors during parsing is different than expected", validator.getErrorsMap().size() , is(2));
    }

    @Test
    public void testJsonValidationWithSchemaErrorEmptyOrObjectValue() throws Exception {
        File fileToValidate = loadFile("files/testErrorEmptyValue.json");

        validator.runValidation(fileToValidate,"/schemas/college-schema.json");
        validator.printResults();
        assertThat("Number of null values during parsing is different than expected", validator.getCountOfNullValues() , is(2));
        assertThat("Number of parsed objects is different than expected", validator.getCountOfParsedObjects() , is(0));
        assertThat("Number of different parsing errors during parsing is different than expected", validator.getErrorsMap().size() , is(1));
    }
}
