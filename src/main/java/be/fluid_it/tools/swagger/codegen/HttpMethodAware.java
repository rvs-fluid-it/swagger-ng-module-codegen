package be.fluid_it.tools.swagger.codegen;

public interface HttpMethodAware {
    boolean isPOST();
    boolean isGET();
    boolean isPUT();
    boolean isDELETE();
    boolean isPATCH();
    boolean isHEAD();
    boolean isOtherHttpMethod();
}
