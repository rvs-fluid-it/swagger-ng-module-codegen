package be.fluid_it.tools.swagger.codegen;

import org.junit.Assert;
import org.junit.Test;

public class WrapperUtilTest {

    public static final String WHAT_EVER = "whatever";

    @Test
    public void checkExtractDataPropertyName() {
        Assert.assertEquals("data", WrapperUtil.extractDataPropertyName("models.SampleWrappedAsData"));
    }

    @Test
    public void checkExtractDataPropertyType() {
        Assert.assertEquals("models.Sample", WrapperUtil.extractDataPropertyType("models.SampleWrappedAsData"));
        Assert.assertEquals("Array<models.Sample>", WrapperUtil.extractDataPropertyType("models.ListSampleWrappedAsData"));
        Assert.assertEquals("Array<number>", WrapperUtil.extractDataPropertyType("models.ListBigIntegerWrappedAsData"));
        Assert.assertEquals("string", WrapperUtil.extractDataPropertyType("models.StringWrappedAsData"));
    }
}
