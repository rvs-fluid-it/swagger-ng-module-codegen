package be.fluid_it.tools.swagger.codegen;

import org.junit.Assert;
import org.junit.Test;

public class WrapperUtilTest {

    public static final String WHAT_EVER = "whatever";

    @Test
    public void aContainerTypeIsAWrapperWhenItStartsAndEndsWithACurlyBrace() {
        Assert.assertTrue(WrapperUtil.isWrapped("{" +
                WHAT_EVER +
                "}"));
        Assert.assertFalse(WrapperUtil.isWrapped("List"));
    }

    @Test
    public void stripCurlyBraces() {
        Assert.assertEquals(WHAT_EVER, WrapperUtil.stripCurlyBraces(" {" + WHAT_EVER + "}  " ));
    }

    @Test
    public void extractProperties() {
        WrapperUtil.NameTypeTuple[] expected = {new WrapperUtil.NameTypeTuple("data", "List<Sample>"),
                new WrapperUtil.NameTypeTuple("success", "boolean")};
        Assert.assertArrayEquals(expected, WrapperUtil.extractProperties("{ data: List<Sample>, success: boolean}" ));
    }

    @Test
    public void thePropertyIndicatingSuccessIsTheFirstEncounteredPropertyOfTypeBoolean() {
        Assert.assertTrue(WrapperUtil.hasSuccessProperty("{ data: List<Sample>, success: boolean}"));
        Assert.assertEquals("success", WrapperUtil.extractSuccessPropertyName("{ data: List<Sample>, success: boolean}"));
    }

    @Test
    public void theDataPropertyIsTheFirstEncounteredNoneBooleanTypeProperty() {
        Assert.assertEquals("data", WrapperUtil.extractDataPropertyName("{ data: List<Sample>, succes: boolean}"));
        Assert.assertEquals("Array<model.Sample>", WrapperUtil.extractPropertyType("{ data: List<Sample>, succes: boolean}", "model" , "data"));
        Assert.assertTrue(WrapperUtil.hasSuccessProperty("{ data: List<Sample>, success: boolean}"));
        Assert.assertFalse(WrapperUtil.hasMessagesProperty("{ data: List<Sample>, success: boolean}"));
    }

    @Test
    public void theMessagesPropertyIsTheSecondEncounteredNoneBooleanTypeProperty() {
        Assert.assertEquals("messages", WrapperUtil.extractMessagesPropertyName("{ data: List<Sample>, succes: boolean, messages: List<Message>}"));
        Assert.assertEquals("Array<model.Message>", WrapperUtil.extractPropertyType("{ data: List<Sample>, succes: boolean}, messages: List<Message>}", "model", "messages"));
        Assert.assertTrue(WrapperUtil.hasSuccessProperty("{ data: List<Sample>, success: boolean, messages: List<Message>}}"));
        Assert.assertTrue(WrapperUtil.hasMessagesProperty("{ data: List<Sample>, success: boolean, messages: List<Message>}}"));
    }
}
