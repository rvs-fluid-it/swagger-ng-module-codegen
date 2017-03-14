package be.fluid_it.tools.swagger.codegen;

import java.util.ArrayList;
import java.util.List;

public class WrapperUtil {
    public static boolean isWrapped(String containerType) {
        return containerType != null && containerType.trim().startsWith("{") && containerType.trim().endsWith("}");
    }

    public static boolean hasSuccessProperty(String containerType) {
        if (isWrapped(containerType)) {
            for (NameTypeTuple nameTypeTuple : extractProperties(containerType)) {
                if (nameTypeTuple.type().equals("boolean")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String extractSuccessPropertyName(String containerType) {
        if (isWrapped(containerType)) {
            for (NameTypeTuple nameTypeTuple : extractProperties(containerType)) {
                if (nameTypeTuple.type().equals("boolean")) {
                    return nameTypeTuple.name();
                }
            }
        }
        return null;
    }


    public static String extractDataPropertyName(String containerType) {
        if (isWrapped(containerType)) {
            for (NameTypeTuple nameTypeTuple : extractProperties(containerType)) {
                if (!nameTypeTuple.type().equals("boolean")) {
                    return nameTypeTuple.name();
                }
            }
        }
        return null;
    }

    public static String extractMessagesPropertyName(String containerType) {
        boolean firstFound = false;
        if (isWrapped(containerType)) {
            for (NameTypeTuple nameTypeTuple : extractProperties(containerType)) {
                if (!nameTypeTuple.type().equals("boolean")) {
                    if (!firstFound) {
                        firstFound = true;
                    } else {
                        return nameTypeTuple.name();
                    }
                }
            }
        }
        return null;
    }

    public static boolean hasMessagesProperty(String containerType) {
        boolean firstFound = false;
        if (isWrapped(containerType)) {
            for (NameTypeTuple nameTypeTuple : extractProperties(containerType)) {
                if (!nameTypeTuple.type().equals("boolean")) {
                    if (!firstFound) {
                        firstFound = true;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static NameTypeTuple[] extractProperties(String containerType) {
        if (isWrapped(containerType)) {
            String strippedContainerType = stripCurlyBraces(containerType);
            String[] keyValues  = strippedContainerType.split(",");
            List<NameTypeTuple> properties = new ArrayList<NameTypeTuple>();
            for (String keyValue : keyValues) {
                String[] parts = keyValue.split(":");
                if (parts.length >= 2) {
                    properties.add(new NameTypeTuple(parts[0].trim(), parts[1].trim()));
                }
            }
            return properties.toArray(new NameTypeTuple[properties.size()]);
        }
        return null;
    }

    public static String extractPropertyType(String containerType, String modelPackage, String propertyName) {
        if (isWrapped(containerType)) {
            for (NameTypeTuple nameTypeTuple : extractProperties(containerType)) {
                if (nameTypeTuple.name().equals(propertyName)) {
                    return prefixWithModelPackage(nameTypeTuple.type(), modelPackage);
                }
            }
        }
        return null;
    }

    private static String prefixWithModelPackage(String type, String modelPackage) {
        String prefixedType = null;
        if (type.contains("<")) {
            prefixedType = type.replaceAll("<", "<" +  modelPackage + ".");
            prefixedType = prefixedType.replaceAll("List", "Array");
        } else {
            prefixedType = modelPackage + "." + type;
        }
        return prefixedType;
    }

    static String stripCurlyBraces(String containerType) {
        String trimmedContainerType = containerType.trim();
        return trimmedContainerType.substring(1, trimmedContainerType.length() -  1);
    }

    static class NameTypeTuple  {
        private final String name;
        private final String type;

        public NameTypeTuple(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String name() {
            return name;
        }

        public String type() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NameTypeTuple)) return false;

            NameTypeTuple that = (NameTypeTuple) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            return type != null ? type.equals(that.type) : that.type == null;

        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (type != null ? type.hashCode() : 0);
            return result;
        }
    }

}
