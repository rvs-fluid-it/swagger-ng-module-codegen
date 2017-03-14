package be.fluid_it.tools.swagger.codegen;

public interface WrapperAware {
    boolean isWrapped();
    boolean hasSuccessProperty();
    String successPropertyName();
    String dataPropertyName();
    String dataPropertyType();
    boolean hasMessagesProperty();
    String messagesPropertyName();
    String messagesPropertyType();
}
