package be.fluid_it.tools.swagger.codegen;

import io.swagger.codegen.*;
import io.swagger.codegen.languages.AbstractTypeScriptClientCodegen;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NgModuleCodegen extends AbstractTypeScriptClientCodegen {
    private static final SimpleDateFormat SNAPSHOT_SUFFIX_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");

    public static final String SERVICE_NAME = "serviceName";
    public static final String PRETTY_SERVICE_NAME = "prettyServiceName";
    public static final String NPM_SCOPE = "npmScope";
    public static final String NPM_NAME = "npmName";
    public static final String NPM_VERSION = "npmVersion";
    public static final String NPM_REPOSITORY = "npmRepository";
    public static final String NPM_AUTHOR = "npmAuthor";
    public static final String NPM_LICENSE = "npmLicense";
    public static final String SNAPSHOT = "snapshot";

    protected String serviceName = null;
    protected String npmScope = null;
    protected String npmName = null;
    protected String npmVersion = "1.0.0";
    protected String npmAuthor = null;
    protected String npmLicense = null;
    protected String npmRepository = null;

    public NgModuleCodegen() {
        super();
        this.outputFolder = "generated-code/ng-module-rest-services";

        embeddedTemplateDir = templateDir = "templates";

        modelTemplateFiles.put("model.mustache", ".ts");

        apiTemplateFiles.put("api.mustache", ".ts");
        typeMapping.put("Date", "Date");
        apiPackage = "api";
        modelPackage = "model";

        this.cliOptions.add(new CliOption(SERVICE_NAME, "The short name of your service"));
        this.cliOptions.add(new CliOption(NPM_SCOPE, "The scope under which you want to publish generated npm package"));
        this.cliOptions.add(new CliOption(NPM_NAME, "The name under which you want to publish generated npm package"));
        this.cliOptions.add(new CliOption(NPM_VERSION, "The version of your npm package"));
        this.cliOptions.add(new CliOption(NPM_REPOSITORY, "Use this property to set an url your private npmRepo in the package.json"));
        this.cliOptions.add(new CliOption(NPM_AUTHOR, "The author of the npm package which you want to publish"));
        this.cliOptions.add(new CliOption(NPM_LICENSE, "The license of the npm package which you want to publish"));
        this.cliOptions.add(new CliOption(SNAPSHOT, "When setting this property to true the version will be suffixed with -SNAPSHOT.yyyyMMddHHmm", BooleanProperty.TYPE).defaultValue(Boolean.FALSE.toString()));
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + "/src/" + apiPackage().replace('.', File.separatorChar);
    }

    @Override
    public String modelFileFolder() {
        return outputFolder + "/src/" + modelPackage().replace('.', File.separatorChar);
    }

    @Override
    protected void addAdditionPropertiesToCodeGenModel(CodegenModel codegenModel, ModelImpl swaggerModel) {
        codegenModel.additionalPropertiesType = getSwaggerType(swaggerModel.getAdditionalProperties());
        addImport(codegenModel, codegenModel.additionalPropertiesType);
    }

    public String getName() {
        return "typescript-ng-module";
    }

    public String getHelp() {
        return "Generates a TypeScript Angular2 @NgModule client library.";
    }

    @Override
    public void processOpts() {
        supportingFiles.add(new SupportingFile("models.mustache", "src" + File.separator + modelPackage().replace('.', File.separatorChar), "models.ts"));
        supportingFiles.add(new SupportingFile("apis.mustache", "src" + File.separator + apiPackage().replace('.', File.separatorChar), "api.ts"));
        if (additionalProperties.containsKey(SERVICE_NAME)) {
            this.setServiceName(additionalProperties.get(SERVICE_NAME).toString());
        }
        if (additionalProperties.containsKey(NPM_NAME)) {
            addNpmPackageGeneration();
        }
        supportingFiles.add(new SupportingFile("configuration.mustache", "src", "configuration.ts"));
        supportingFiles.add(new SupportingFile("variables.mustache", "src", "variables.ts"));
        supportingFiles.add(new SupportingFile("classType.mustache", "src", "classType.ts"));
    }

    private void addNpmPackageGeneration() {
        if (additionalProperties.containsKey(NPM_SCOPE)) {
            this.setNpmScope(additionalProperties.get(NPM_SCOPE).toString());
        }
        if (additionalProperties.containsKey(NPM_NAME)) {
            this.setNpmName(additionalProperties.get(NPM_NAME).toString());
        }
        if (additionalProperties.containsKey(NPM_VERSION)) {
            this.setNpmVersion(additionalProperties.get(NPM_VERSION).toString());
        }
        if (additionalProperties.containsKey(SNAPSHOT) && Boolean.valueOf(additionalProperties.get(SNAPSHOT).toString())) {
            this.setNpmVersion(npmVersion + "-SNAPSHOT." + SNAPSHOT_SUFFIX_FORMAT.format(new Date()));
        }
        additionalProperties.put(NPM_VERSION, npmVersion);

        if (additionalProperties.containsKey(NPM_REPOSITORY)) {
            this.setNpmRepository(additionalProperties.get(NPM_REPOSITORY).toString());
        }
        if (additionalProperties.containsKey(NPM_AUTHOR)) {
            this.setNpmAuthor(additionalProperties.get(NPM_AUTHOR).toString());
        }
        if (additionalProperties.containsKey(NPM_LICENSE)) {
            this.setNpmLicense(additionalProperties.get(NPM_LICENSE).toString());
        }

        supportingFiles.add(new SupportingFile("package.mustache", getIndexDirectory(), "package.json"));
        supportingFiles.add(new SupportingFile("dist.package.mustache", getIndexDirectory() + File.separator + "dist", "package.json"));
        supportingFiles.add(new SupportingFile("rollup.config.mustache", getIndexDirectory(), "rollup.config.js"));
        supportingFiles.add(new SupportingFile("tsconfig.mustache", getIndexDirectory(), "tsconfig.json"));
        supportingFiles.add(new SupportingFile("root.index.mustache", getIndexDirectory(), "index.ts"));
        supportingFiles.add(new SupportingFile("src.index.mustache", getIndexDirectory() + File.separator + "src", "index.ts"));
    }

    private String getIndexDirectory() {
        String indexPackage = modelPackage.substring(0, Math.max(0, modelPackage.lastIndexOf('.')));
        return indexPackage.replace('.', File.separatorChar);
    }

    @Override
    public String getTypeDeclaration(Property p) {
        Property inner;
        if (p instanceof ArrayProperty) {
            ArrayProperty mp1 = (ArrayProperty) p;
            inner = mp1.getItems();
            return this.getSwaggerType(p) + "<" + this.getTypeDeclaration(inner) + ">";
        } else if (p instanceof MapProperty) {
            MapProperty mp = (MapProperty) p;
            inner = mp.getAdditionalProperties();
            return "{ [key: string]: " + this.getTypeDeclaration(inner) + "; }";
        } else if (p instanceof FileProperty || p instanceof ObjectProperty) {
            return "any";
        } else {
            return super.getTypeDeclaration(p);
        }
    }

    @Override
    public String getSwaggerType(Property p) {
        String swaggerType = super.getSwaggerType(p);
        if (languageSpecificPrimitives.contains(swaggerType)) {
            return swaggerType;
        }
        return addModelPrefix(swaggerType);
    }

    private String addModelPrefix(String swaggerType) {
        String type = null;
        if (typeMapping.containsKey(swaggerType)) {
            type = typeMapping.get(swaggerType);
        } else {
            type = swaggerType;
        }

        if (!startsWithLanguageSpecificPrimitiv(type)) {
            type = "models." + swaggerType;
        }
        return type;
    }

    private boolean startsWithLanguageSpecificPrimitiv(String type) {
        for (String langPrimitive : languageSpecificPrimitives) {
            if (type.startsWith(langPrimitive)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void postProcessParameter(CodegenParameter parameter) {
        super.postProcessParameter(parameter);
        parameter.dataType = addModelPrefix(parameter.dataType);
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
        this.additionalProperties.put(PRETTY_SERVICE_NAME, changefirstLetterToCapital(serviceName));
    }

    public String getPrettyServiceName() {
        return changefirstLetterToCapital(getServiceName());
    }

    private String changefirstLetterToCapital(String input) {
        String output = input;
        if (input != null && input.length() > 0) {
            output = input.substring(0, 1).toUpperCase();
            if (input.length() > 1) {
                output = output + input.substring(1);
            }
        }
        return output;
    }

    public String getNpmScope() {
        return npmScope;
    }

    public void setNpmScope(String npmScope) {
        this.npmName = npmScope;
    }

    public String getNpmName() {
        return npmName;
    }

    public void setNpmName(String npmName) {
        this.npmName = npmName;
    }

    public String getNpmVersion() {
        return npmVersion;
    }

    public void setNpmVersion(String npmVersion) {
        this.npmVersion = npmVersion;
    }

    public String getNpmRepository() {
        return npmRepository;
    }

    public void setNpmRepository(String npmRepository) {
        this.npmRepository = npmRepository;
    }

    public String getNpmAuthor() {
        return npmAuthor;
    }

    public void setNpmAuthor(String npmAuthor) {
        this.npmName = npmAuthor;
    }

    public String getNpmLicense() {
        return npmLicense;
    }

    public void setNpmLicense(String npmLicense) {
        this.npmLicense = npmLicense;
    }

    @Override
    public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
        super.postProcessOperations(objs);
        if (objs.containsKey("operations")) {
            Object operations = objs.get("operations");
            if (operations instanceof Map && ((Map) operations).containsKey("operation")) {
                if (((Map) operations).containsKey("operation")) {
                    Object operation = ((Map) operations).get("operation");
                    if (operation instanceof List) {

                        for (Object untypedCodegenOperation : (List) operation) {
                            if (untypedCodegenOperation instanceof CodegenOperation) {
                                CodegenOperation codegenOperation = (CodegenOperation) untypedCodegenOperation;
                                codegenOperation.path = translatePath(codegenOperation.path, !codegenOperation.allParams.isEmpty() ? codegenOperation.allParams.get(0) : null);
                            }
                        }
                        List httpMethodAwareOperation = new ArrayList();
                        for (Object untypedCodegenOperation : (List) operation) {
                            if (untypedCodegenOperation instanceof CodegenOperation) {
                                httpMethodAwareOperation.add(new HttpMethodAwareCodegenOperation((CodegenOperation) untypedCodegenOperation));
                            } else {
                                httpMethodAwareOperation.add(untypedCodegenOperation);
                            }
                        }
                        ((Map) operations).put("operation", httpMethodAwareOperation);
                    }
                }
            }
        }
        return objs;
    }

    static String translatePath(String path, CodegenParameter codegenParameter) {
        if (codegenParameter == null) return path;
        String parameterName = codegenParameter.paramName;
        String translatedPath = path.replace("{", "${");
        // Only one pathParameter in the path is supported
        // Could be made more robust if the need arise
        if (codegenParameter.dataType.contains(".")) {
            parameterName = parameterName + ".id";
        }
        if (translatedPath.contains("${")) {
            String[] parts = translatedPath.split("\\$\\{");
            if (parts.length > 1 && !"".equals(parts[1])) {
                String placeholder = parts[1];
                if (placeholder.contains("}")) {
                    placeholder = placeholder.split("\\}")[0];
                    if (placeholder.endsWith("Id")) {
                        translatedPath = parts[0] + "${" + parameterName + "}";
                    }
                }
            }
        }
        return translatedPath;
    }
}
