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
package org.apache.plc4x.java.ads.api.generic;

import io.netty.buffer.ByteBuf;
import org.apache.plc4x.java.ads.api.generic.types.*;
import org.apache.plc4x.java.ads.api.util.ByteReadable;

/**
 * AMS Header	32 bytes	The AMS/TCP-Header contains the addresses of the transmitter and receiver. In addition the AMS error code , the ADS command Id and some other information.
 */
public class AMSHeader implements ByteReadable {
    /**
     * This is the AMSNetId of the station, for which the packet is intended. Remarks see below.
     */
    private final AMSNetId targetAmsNetId;
    /**
     * This is the AMSPort of the station, for which the packet is intended.
     */
    private final AMSPort targetAmsPort;
    /**
     * This contains the AMSNetId of the station, from which the packet was sent.
     */
    private final AMSNetId sourceAmsNetId;
    /**
     * This contains the AMSPort of the station, from which the packet was sent.
     */
    private final AMSPort sourceAmsPort;
    /**
     * 2 bytes.
     */
    private final Command commandId;

    private final State stateFlags;

    /**
     * 4 bytes	Size of the data range. The unit is byte.
     */
    private final DataLength dataLength;

    /**
     * 4 bytes	AMS error number. See ADS Return Codes.
     */
    private final AMSError code;

    /**
     * 4 bytes	Free usable 32 bit array. Usually this array serves to send an Id. This Id makes is possible to assign a received response to a request, which was sent before.
     */
    private final Invoke invokeId;

    /**
     * bytes	Data range. The data range contains the parameter of the considering ADS commands.
     */
    // TODO: check if this is indeed {@link ADSData} and the documentation is just confusing at this point
    private final Data nData;

    public AMSHeader(AMSNetId targetAmsNetId, AMSPort targetAmsPort, AMSNetId sourceAmsNetId, AMSPort sourceAmsPort, Command commandId, State stateFlags, DataLength dataLength, AMSError code, Invoke invokeId, Data nData) {
        this.targetAmsNetId = targetAmsNetId;
        this.targetAmsPort = targetAmsPort;
        this.sourceAmsNetId = sourceAmsNetId;
        this.sourceAmsPort = sourceAmsPort;
        this.commandId = commandId;
        this.stateFlags = stateFlags;
        this.dataLength = dataLength;
        this.code = code;
        this.invokeId = invokeId;
        this.nData = nData;
    }

    @Override
    public ByteBuf getByteBuf() {
        return AMSTCPPaket.buildByteBuff(
            targetAmsNetId,
            targetAmsPort,
            sourceAmsNetId,
            sourceAmsPort,
            commandId,
            stateFlags,
            dataLength,
            code,
            invokeId,
            nData);
    }
}