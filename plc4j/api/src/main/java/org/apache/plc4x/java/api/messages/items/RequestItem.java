/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
*/
package org.apache.plc4x.java.api.messages.items;

import org.apache.plc4x.java.api.model.Address;

import java.util.Objects;

public abstract class RequestItem<DATA_TYPE> {

    private final Class<DATA_TYPE> datatype;

    private final Address address;

    public RequestItem(Class<DATA_TYPE> datatype, Address address) {
        Objects.requireNonNull(datatype, "Data type must not be null");
        Objects.requireNonNull(address, "Address type must not be null");
        this.datatype = datatype;
        this.address = address;
    }

    public Class<DATA_TYPE> getDatatype() {
        return datatype;
    }

    public Address getAddress() {
        return address;
    }

}
