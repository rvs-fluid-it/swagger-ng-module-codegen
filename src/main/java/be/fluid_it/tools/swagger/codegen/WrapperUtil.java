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
                // TODO Refactor to get rid of duplication and add detect more built in typescript types
                if (classType.startsWith("List")) {
                    StringBuilder returnTypeBuilder = new StringBuilder("Array<");
                    String itemClassType = classType.substring(4).trim();
                    switch (itemClassType) {
                        case "BigInteger":
                            returnTypeBuilder.append("number");
                            break;
                        case "String":
                            returnTypeBuilder.append("string");
                            break;
                        default:
                            returnTypeBuilder.append(packagePrefix).append(itemClassType);
                    }
                    return returnTypeBuilder.append(">").toString();
                } else {
                    switch (classType) {
                        case "BigInteger":
                            return "number";
                        case "String":
                            return "string";
                        default:
                            return packagePrefix + classType;
                    }
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
