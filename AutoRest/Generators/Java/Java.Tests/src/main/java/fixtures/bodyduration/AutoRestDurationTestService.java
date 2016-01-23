/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator 0.14.0.0
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package fixtures.bodyduration;

import java.util.List;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.logging.HttpLoggingInterceptor.Level;
import com.microsoft.rest.serializer.JacksonMapperAdapter;

/**
 * The interface for AutoRestDurationTestService class.
 */
public interface AutoRestDurationTestService {
    /**
     * Gets the URI used as the base for all cloud service requests.
     *
     * @return the BaseUri value.
     */
    String getBaseUri();

    /**
     * Gets the list of interceptors the OkHttp client will execute.
     *
     * @return the list of interceptors.
     */
    List<Interceptor> getClientInterceptors();

    /**
     * Sets the logging level for OkHttp client.
     *
     * @param logLevel the logging level enum.
     */
    void setLogLevel(Level logLevel);

    /**
     * Gets the adapter for {@link com.fasterxml.jackson.databind.ObjectMapper} for serialization
     * and deserialization operations..
     *
     * @return the adapter.
     */
    JacksonMapperAdapter getMapperAdapter();

    /**
     * Gets the DurationOperations object to access its operations.
     * @return the DurationOperations object.
     */
    DurationOperations getDurationOperations();

}
