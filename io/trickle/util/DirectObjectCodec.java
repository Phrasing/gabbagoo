/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.eventbus.MessageCodec
 */
package io.trickle.util;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class DirectObjectCodec
implements MessageCodec {
    public String name;

    public DirectObjectCodec(Class clazz) {
        this.name = clazz.getName();
    }

    public String name() {
        return this.name + "Codec";
    }

    public void encodeToWire(Buffer buffer, Object object) {
    }

    public byte systemCodecID() {
        return -1;
    }

    public Object transform(Object object) {
        return object;
    }

    public Object decodeFromWire(int n, Buffer buffer) {
        return null;
    }
}

