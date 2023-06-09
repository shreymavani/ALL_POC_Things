/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudera.nifi.processors.tp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.io.OutputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Tags({"myappendtext"})
@CapabilityDescription("Custom processor to append text")
@SeeAlso({})
@ReadsAttributes({@ReadsAttribute(attribute="", description="")})
@WritesAttributes({@WritesAttribute(attribute="", description="")})
public class MyAppendText extends AbstractProcessor {

    private static final String FIELD_SUCCESS = "success";
    private static final String FIELD_FAILURE = "failure";

    private static final PropertyDescriptor APPEND_TEXT = new PropertyDescriptor
            .Builder().name("AppendText")
            .displayName("Append Text")
            .description("Text to append")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    private static final Relationship REL_SUCCESS = new Relationship.Builder()
            .name(FIELD_SUCCESS)
            .description("Success relationship")
            .build();

    private static final Relationship REL_FAILURE = new Relationship.Builder()
            .name(FIELD_FAILURE)
            .description("Failure relationship")
            .build();

    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
        descriptors.add(APPEND_TEXT);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
        relationships.add(REL_SUCCESS);
        relationships.add(REL_FAILURE);
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) {


    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {

        FlowFile flowFile = session.get();

        if ( flowFile == null ) {
//            return null;
        }

        try {



            final String textToAppend = context.getProperty(APPEND_TEXT).getValue();
            final AtomicReference<Boolean> wasError = new AtomicReference<>(false);
            session.write(flowFile, new OutputStreamCallback() {
                @Override public void process(OutputStream outputStream) throws IOException {
                    try (final OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8")) {
                        writer.append(textToAppend);
                    } catch (Exception exception) {
                        wasError.set(true);
                    }
                }
            });

            if (wasError.get()) {
                session.transfer(flowFile, REL_FAILURE);
//                return null;
            }

            session.transfer(flowFile, REL_SUCCESS);

        } catch(Exception exception) {
            session.transfer(flowFile, REL_FAILURE);
        }
//        return null;
    }
}
