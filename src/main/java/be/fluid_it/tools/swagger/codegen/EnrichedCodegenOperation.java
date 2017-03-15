package be.fluid_it.tools.swagger.codegen;

import io.swagger.codegen.*;
import io.swagger.models.properties.RefProperty;

public class EnrichedCodegenOperation extends CodegenOperation implements HttpMethodAware, WrapperAware{
    public String returnDataType;
    public String returnDataProperty;

    public EnrichedCodegenOperation(CodegenOperation codegenOperation) {
        super();
        for (CodegenProperty codegenProperty : codegenOperation.responseHeaders) {
            this.responseHeaders.add(codegenProperty);
        }
        this.hasAuthMethods = codegenOperation.hasAuthMethods;
        this.hasConsumes = codegenOperation.hasConsumes;
        this.hasProduces = codegenOperation.hasProduces;
        this.hasParams = codegenOperation.hasParams;
        this.hasOptionalParams = codegenOperation.hasOptionalParams;
        this.returnTypeIsPrimitive = codegenOperation.returnTypeIsPrimitive;
        this.returnSimpleType = codegenOperation.returnSimpleType;
        this.subresourceOperation = codegenOperation.subresourceOperation;
        this.isMapContainer = codegenOperation.isMapContainer;
        this.isListContainer = codegenOperation.isListContainer;
        this.isMultipart = codegenOperation.isMultipart;
        this.hasMore = codegenOperation.hasMore;
        this.isResponseBinary = codegenOperation.isResponseBinary;
        this.hasReference = codegenOperation.hasReference;
        this.isRestfulIndex = codegenOperation.isRestfulIndex;
        this.isRestfulShow = codegenOperation.isRestfulShow;
        this.isRestfulCreate = codegenOperation.isRestfulCreate;
        this.isRestfulUpdate = codegenOperation.isRestfulUpdate;
        this.isRestfulDestroy = codegenOperation.isRestfulDestroy;
        this.isRestful = codegenOperation.isRestful;

        this.path = codegenOperation.path;
        this.operationId = codegenOperation.operationId;
        this.returnType = codegenOperation.returnType;
        this.httpMethod = codegenOperation.httpMethod;
        this.returnBaseType = codegenOperation.returnBaseType;
        this.returnContainer = codegenOperation.returnContainer;
        this.summary = codegenOperation.summary;
        this.unescapedNotes = codegenOperation.unescapedNotes;
        this.notes = codegenOperation.notes;
        this.baseName = codegenOperation.baseName;
        this.defaultResponse = codegenOperation.defaultResponse;
        this.discriminator = codegenOperation.discriminator;

        this.consumes = codegenOperation.consumes;
        this.produces = codegenOperation.produces;

        this.bodyParam = codegenOperation.bodyParam;
        this.allParams = codegenOperation.allParams;
        this.bodyParams = codegenOperation.bodyParams;
        this.pathParams = codegenOperation.pathParams;
        this.queryParams = codegenOperation.queryParams;
        this.headerParams = codegenOperation.headerParams;
        this.formParams = codegenOperation.formParams;
        this.authMethods = codegenOperation.authMethods;
        this.tags = codegenOperation.tags;
        this.responses = codegenOperation.responses;
        this.imports = codegenOperation.imports;
        this. examples = codegenOperation.examples;
        this.externalDocs = codegenOperation.externalDocs;
        this.vendorExtensions = codegenOperation.vendorExtensions;
        this.nickname = codegenOperation.nickname; // legacy support
        this.operationIdLowerCase = codegenOperation.operationIdLowerCase; // for mardown documentation

        if (isWrapped()) {
            this.returnDataType = dataPropertyType();
            this.returnDataProperty = dataPropertyName();
        }

    }

    public boolean isPOST() {
        return httpMethod != null && httpMethod.equals("POST");
    }

    public boolean isGET() {
        return httpMethod != null && httpMethod.equals("GET");
    }

    public boolean isPUT() {
        return httpMethod != null && httpMethod.equals("PUT");
    }

    public boolean isDELETE() {
        return httpMethod != null && httpMethod.equals("DELETE");
    }

    public boolean isPATCH() {
        return httpMethod != null && httpMethod.equals("PATCH");
    }

    public boolean isHEAD() {
        return httpMethod != null && httpMethod.equals("HEAD");
    }

    public boolean isOtherHttpMethod() {
        return !isPOST() && !isGET() && !isPUT() && !isDELETE() && !isPATCH();
    }

    @Override
    public boolean isWrapped() {
        return WrapperUtil.isWrapped(this.returnType);
    }

    @Override
    public String dataPropertyName() {
        return WrapperUtil.extractDataPropertyName(this.returnType);
    }

    @Override
    public String dataPropertyType() {
        return WrapperUtil.extractDataPropertyType(this.returnType);
    }
}
