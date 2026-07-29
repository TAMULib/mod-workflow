import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FolioRequestTaskTest {

    @Test
    void testConstructor() {
        FolioRequestTask task = new FolioRequestTask();
        assertNotNull(task);
    }

    @Test
    void testAsyncBeforeDefault() {
        FolioRequestTask task = new FolioRequestTask();
        assertFalse(task.isAsyncBefore());
    }

    @Test
    void testAsyncAfterDefault() {
        FolioRequestTask task = new FolioRequestTask();
        assertFalse(task.isAsyncAfter());
    }

    @Test
    void testInputVariablesNotNull() {
        FolioRequestTask task = new FolioRequestTask();
        assertNotNull(task.getInputVariables());
    }

    @Test
    void testHeaderOutputVariablesNotNull() {
        FolioRequestTask task = new FolioRequestTask();
        assertNotNull(task.getHeaderOutputVariables());
    }

    @Test
    void testOutputVariableNotNull() {
        FolioRequestTask task = new FolioRequestTask();
        assertNotNull(task.getOutputVariable());
    }

    @Test
    void testRequestNotNull() {
        FolioRequestTask task = new FolioRequestTask();
        assertNotNull(task.getRequest());
    }
}
