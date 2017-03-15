package be.fluid_it.tools.swagger.codegen;

import java.util.ArrayList;
import java.util.List;

public class WrapperUtil {
    public static boolean isWrapped(String returnType) {
        return returnType != null && returnType.trim().contains("WrappedAs");
    }

    public static String extractDataPropertyName(String returnType) {
        if (isWrapped(returnType)) {
            String[] parts = returnType.split("WrappedAs");
            return parts.length == 2 ? firstCharacterToLowercase(parts[1]) : "unknown";
        }
        return null;
    }

    private static String firstCharacterToLowercase(String string) {
        return string.length() > 1 ? Character.toLowerCase(string.charAt(0)) + string.substring(1) : string.toLowerCase();
    }

    public static String extractDataPropertyType(String returnType) {
        if (isWrapped(returnType)) {
            String strippedReturnType = returnType.split("WrappedAs")[0];
            String[] parts = strippedReturnType.split("\\.");
            String packagePrefix = null;
            if ( parts.length > 1) {
                StringBuffer packagePrefixBuffer = new StringBuffer();
                for (int i = 0; i < parts.length - 1; i++ ) {
                    packagePrefixBuffer.append(parts[i]).append(".");
                }
                packagePrefix = packagePrefixBuffer.toString();
                String classType = parts[parts.length - 1];
                if (classType.startsWith("List")) {
                    return "Array<" + packagePrefix + classType.substring(4) + ">";
                } else {
                    return packagePrefix + classType;
                }
            } else {
                if (strippedReturnType.startsWith("List")) {
                    return strippedReturnType.replaceAll("List" , "Array");
                }
                return strippedReturnType;
            }
        }
        return null;
    }
}
