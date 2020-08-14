// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.digitaltwins.core;

import com.azure.core.http.HttpPipeline;
import com.azure.core.http.HttpPipelineBuilder;
import com.azure.core.http.policy.CookiePolicy;
import com.azure.core.http.policy.RetryPolicy;
import com.azure.core.http.policy.UserAgentPolicy;

/** Initializes a new instance of the AzureDigitalTwinsAPI type. */
public final class AzureDigitalTwinsAPI {
    /** server parameter. */
    private final String host;

    /**
     * Gets server parameter.
     *
     * @return the host value.
     */
    public String getHost() {
        return this.host;
    }

    /** Api Version. */
    private final String apiVersion;

    /**
     * Gets Api Version.
     *
     * @return the apiVersion value.
     */
    public String getApiVersion() {
        return this.apiVersion;
    }

    /** The HTTP pipeline to send requests through. */
    private final HttpPipeline httpPipeline;

    /**
     * Gets The HTTP pipeline to send requests through.
     *
     * @return the httpPipeline value.
     */
    public HttpPipeline getHttpPipeline() {
        return this.httpPipeline;
    }

    /** The DigitalTwinModels object to access its operations. */
    private final DigitalTwinModels digitalTwinModels;

    /**
     * Gets the DigitalTwinModels object to access its operations.
     *
     * @return the DigitalTwinModels object.
     */
    public DigitalTwinModels getDigitalTwinModels() {
        return this.digitalTwinModels;
    }

    /** The Querys object to access its operations. */
    private final Querys querys;

    /**
     * Gets the Querys object to access its operations.
     *
     * @return the Querys object.
     */
    public Querys getQuerys() {
        return this.querys;
    }

    /** The DigitalTwins object to access its operations. */
    private final DigitalTwins digitalTwins;

    /**
     * Gets the DigitalTwins object to access its operations.
     *
     * @return the DigitalTwins object.
     */
    public DigitalTwins getDigitalTwins() {
        return this.digitalTwins;
    }

    /** The EventRoutes object to access its operations. */
    private final EventRoutes eventRoutes;

    /**
     * Gets the EventRoutes object to access its operations.
     *
     * @return the EventRoutes object.
     */
    public EventRoutes getEventRoutes() {
        return this.eventRoutes;
    }

    /** Initializes an instance of AzureDigitalTwinsAPI client. */
    AzureDigitalTwinsAPI(String host) {
        this(
                new HttpPipelineBuilder()
                        .policies(new UserAgentPolicy(), new RetryPolicy(), new CookiePolicy())
                        .build(),
                host);
    }

    /**
     * Initializes an instance of AzureDigitalTwinsAPI client.
     *
     * @param httpPipeline The HTTP pipeline to send requests through.
     */
    AzureDigitalTwinsAPI(HttpPipeline httpPipeline, String host) {
        this.httpPipeline = httpPipeline;
        this.host = host;
        this.apiVersion = "2020-05-31-preview";
        this.digitalTwinModels = new DigitalTwinModels(this);
        this.querys = new Querys(this);
        this.digitalTwins = new DigitalTwins(this);
        this.eventRoutes = new EventRoutes(this);
    }
}
