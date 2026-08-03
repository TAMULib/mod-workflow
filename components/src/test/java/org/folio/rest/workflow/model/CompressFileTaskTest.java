package org.folio.rest.workflow.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.folio.rest.workflow.enums.CompressFileContainer;
import org.folio.rest.workflow.enums.CompressFileFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompressFileTaskTest {

    private CompressFileTask compressFileTask;

    @BeforeEach
    void setUp() {
        compressFileTask = new CompressFileTask();
    }

    @Test
    void testInputVariablesGetterSetter() {
        Set<EmbeddedVariable> inputVariables = new HashSet<>();
        inputVariables.add(new EmbeddedVariable());
        inputVariables.add(new EmbeddedVariable());
        compressFileTask.setInputVariables(inputVariables);
        assertEquals(inputVariables, compressFileTask.getInputVariables());
    }

    @Test
    void testOutputVariableGetterSetter() {
        EmbeddedVariable outputVariable = new EmbeddedVariable();
        compressFileTask.setOutputVariable(outputVariable);
        assertEquals(outputVariable, compressFileTask.getOutputVariable());
    }

    @Test
    void testAsyncBeforeGetterSetter() {
        compressFileTask.setAsyncBefore(true);
        assertTrue(compressFileTask.getAsyncBefore());
    }

    @Test
    void testAsyncAfterGetterSetter() {
        compressFileTask.setAsyncAfter(true);
        assertTrue(compressFileTask.getAsyncAfter());
    }

    @Test
    void testSourceGetterSetter() {
        compressFileTask.setSource("/path/to/source");
        assertEquals("/path/to/source", compressFileTask.getSource());
    }

    @Test
    void testDestinationGetterSetter() {
        compressFileTask.setDestination("/path/to/destination");
        assertEquals("/path/to/destination", compressFileTask.getDestination());
    }

    @Test
    void testFormatGetterSetter() {
        compressFileTask.setFormat(CompressFileFormat.ZIP);
        assertEquals(CompressFileFormat.ZIP, compressFileTask.getFormat());
    }

    @Test
    void testContainerGetterSetter() {
        compressFileTask.setContainer(CompressFileContainer.TAR);
        assertEquals(CompressFileContainer.TAR, compressFileTask.getContainer());
    }
}
