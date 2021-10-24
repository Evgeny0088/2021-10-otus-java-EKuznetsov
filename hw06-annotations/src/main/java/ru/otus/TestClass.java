package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.annotations.TestDescription;
import static ru.otus.utils.TestCasesUtils.*;

import java.io.*;
import java.util.Arrays;

public class TestClass {

    // objects to be created in @Before methods
    private int[] values;
    private File file;

    public TestClass(){
        this.values = new int[0];
    }

    @Before
    public int[] values(){
        this.values = new int[]{1, 2, 3, 4, 5};
        return values;
    }

    @Before
    public File createFile() throws IOException {
        try{
            this.file = new File("hw06-annotations/src/main/resources/testFile.txt");
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("Test file data for chars count!    ");
            bufferedWriter.flush();
            bufferedWriter.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }
        return file;
    }

    @After
    public void teardown() {
        if (file.exists()){
            file.delete();
        }
        values = new int[0];
    }

    @TestDescription(description = "Checks summary value from array")
    @Test
    public void test1() throws NoSuchMethodException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int summary = Arrays.stream(values).reduce(0, Integer::sum);
        int expectedSummary = 20;
        if (!assertion(expectedSummary,summary,methodName)){
            throw new AssertionError();
        }
    }

    @TestDescription(description = "Verifies consistence of the text file")
    @Test
    public void test2(){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        int actualCharsCount = 0;
        StringBuilder stringBuffer = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()){
                stringBuffer.append(reader.readLine().trim());
            }
            actualCharsCount = stringBuffer.length();
        }catch (IOException exception){
            exception.printStackTrace();
        }
        int expectedCharsCount = 31;
        String expectedContent = "Test file data for chars count!";

        // verifying count of chars and content of the file (whitespaces ignored!)
        if (!assertion(expectedCharsCount,actualCharsCount,methodName) ||
                            !assertion(expectedContent,stringBuffer.toString(),methodName)){
            throw new AssertionError();
        }
    }

    @TestDescription(description = "test is empty")
    @Test
    public void test3(){
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();

        // Situation when both or one of expected/actual values are null, then it should throw AssertionError and no
        // information available about failure of the tests
        if (!assertion("some value", null, methodName)){
            throw new AssertionError();
        }
    }

    /*
    test without description by should be called anyway
     */
    @Test
    public void test4(){}

    /*
    Method without @Tets annotation, hence it should not be called on test runner
     */
    public void dummyMethod(){}
}
