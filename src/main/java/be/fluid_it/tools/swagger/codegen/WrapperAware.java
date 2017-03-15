package be.fluid_it.tools.swagger.codegen;

public interface WrapperAware {
    boolean isWrapped();
    String dataPropertyName();
    String dataPropertyType();
}
