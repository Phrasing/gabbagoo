/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.Handler
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.parsetools.RecordParser
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.network.codec.decoder;

import io.trickle.core.VertxSingleton;
import io.trickle.network.codec.decoder.Reader;
import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import java.util.concurrent.CompletableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketDecoder
implements Handler {
    public Handler<Packet> packetHandler;
    public byte opcode = (byte)-1;
    public RecordParser parser;
    public static Logger logger = LogManager.getLogger((String)"TRICKLE");
    public int size = -1;
    public Reader reader = new Reader();

    public void handle(Buffer buffer) {
        this.parser.handle(buffer);
    }

    public void handle(Object object) {
        this.handle((Buffer)object);
    }

    public void handleException(Throwable throwable) {
        logger.error("Error occurred decoding packet: {}", (Object)throwable.getMessage());
        this.reset();
    }

    public void readPayload(Buffer buffer) {
        String string = buffer.toString();
        byte[] byArray = this.reader.read(string);
        if (byArray != null && byArray.length != 0) {
            Packet packet = new Packet(this.opcode, PacketType.FIXED_SIZE, Buffer.buffer((byte[])byArray));
            if (logger.isDebugEnabled()) {
                logger.debug("Received packet of opcode: {} and size {}", (Object)packet.getOpcode(), (Object)packet.getSize());
            }
            this.packetHandler.handle((Object)packet);
        } else {
            System.out.println("Your system time is too out of sync for security reasons. Please re-sync/re-set your current date&time");
            VertxSingleton.INSTANCE.get().eventBus().send("login.loader", (Object)"Invalid Key");
        }
        this.reset();
    }

    public PacketDecoder(Handler handler) {
        this.packetHandler = handler;
        this.parser = RecordParser.newFixed((int)1).exceptionHandler(this::handleException).handler(this::readOpcode);
    }

    public Handler getPacketHandler() {
        return this.packetHandler;
    }

    public void reset() {
        this.opcode = (byte)-1;
        this.size = -1;
        this.parser.fixedSizeMode(1);
        this.parser.handler(this::readOpcode);
    }

    public static void lambda$serviceOpcodes$0() {
        System.exit(1);
    }

    public void readSize(Buffer buffer) {
        if (this.size != -1) return;
        this.size = buffer.getInt(0);
        this.parser.fixedSizeMode(this.size);
        this.parser.handler(this::readPayload);
    }

    public boolean serviceOpcodes() {
        switch (this.opcode) {
            case -10: {
                CompletableFuture.runAsync(PacketDecoder::lambda$serviceOpcodes$0);
                return true;
            }
            case -15: {
                return true;
            }
        }
        return false;
    }

    public void readOpcode(Buffer buffer) {
        if (this.opcode != -1) return;
        this.opcode = buffer.getByte(0);
        if (this.serviceOpcodes()) {
            this.reset();
            return;
        }
        this.parser.fixedSizeMode(4);
        this.parser.handler(this::readSize);
    }
}

