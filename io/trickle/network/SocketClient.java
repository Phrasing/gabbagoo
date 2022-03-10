/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  io.vertx.core.AbstractVerticle
 *  io.vertx.core.Future
 *  io.vertx.core.Handler
 *  io.vertx.core.Promise
 *  io.vertx.core.buffer.Buffer
 *  io.vertx.core.json.JsonObject
 *  io.vertx.core.net.NetClient
 *  io.vertx.core.net.NetClientOptions
 *  io.vertx.core.net.NetSocket
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.trickle.network;

import io.trickle.network.codec.decoder.PacketDecoder;
import io.trickle.network.codec.encoder.PacketEncoder;
import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketType;
import io.trickle.network.codec.packet.PacketWriter;
import io.trickle.network.codec.packet.handler.LoginHandler;
import io.trickle.network.codec.packet.handler.PacketHandler;
import io.trickle.util.CommandLineHandler;
import io.trickle.util.Storage;
import io.trickle.util.Utils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SocketClient
extends AbstractVerticle {
    public Buffer HEARTBEAT = Buffer.buffer((byte[])new byte[]{-15});
    public Promise<Void> loginPromise;
    public NetSocket connection;
    public static byte[] HANDSHAKE_RCV;
    public boolean handshakeDone = false;
    public PacketEncoder encoder;
    public PacketDecoder decoder = new PacketDecoder(this::handle);
    public static Logger logger;
    public NetClient client;
    public int reconnectAttempts = 0;
    public long heartbeatID = -1L;
    public boolean isClosing = false;

    public Future transmit(Buffer buffer) {
        Future future = this.connection.write((Object)buffer);
        if (!this.connection.writeQueueFull()) return future;
        this.connection.pause();
        return future;
    }

    public void lambda$reconnect$6(Long l) {
        Promise promise = Promise.promise();
        this.connect(promise);
        promise.future().onSuccess(this::lambda$reconnect$4).onFailure(this::lambda$reconnect$5);
    }

    public void register(NetSocket netSocket) {
        this.connection = netSocket;
        this.connection.pause();
        this.handshakeDone = false;
        this.connection.exceptionHandler(SocketClient::lambda$register$1).closeHandler(this::closeHandler).endHandler(SocketClient::lambda$register$2).drainHandler(this::lambda$register$3).handler(this::onHandshakeACK);
        this.connection.resume();
        this.sendHandshake();
    }

    static {
        logger = LogManager.getLogger((String)"TRICKLE");
        HANDSHAKE_RCV = new byte[]{0, 4, 55, 9, 3, 13};
    }

    public void lambda$register$3(Void void_) {
        this.connection.resume();
    }

    public void disconnect() {
        this.isClosing = true;
        this.client.close();
    }

    public void reconnect() {
        logger.warn("Trying to re-establish connection...");
        if (this.reconnectAttempts++ < 3) {
            this.vertx.setTimer(10000L, this::lambda$reconnect$6);
            return;
        }
        CommandLineHandler.stop();
    }

    public void closeHandler(Void void_) {
        if (this.isClosing) return;
        this.reconnect();
    }

    public void sendHandshake() {
        PacketWriter packetWriter = PacketWriter.create(-3, PacketType.INT_VARIABLE_SIZED);
        packetWriter.writeString("trickle_client_0x00");
        packetWriter.writeString("x4623346df#zig");
        packetWriter.writeShort(1);
        packetWriter.writeShort(0);
        packetWriter.writeShort(242);
        this.transmit(packetWriter.build());
    }

    public Future login() {
        if (this.loginPromise != null) {
            if (!this.loginPromise.future().isComplete()) return this.loginPromise.future().onSuccess(SocketClient::lambda$login$9).onFailure(SocketClient::lambda$login$10);
        }
        this.loginPromise = Promise.promise();
        return this.loginPromise.future().onSuccess(SocketClient::lambda$login$9).onFailure(SocketClient::lambda$login$10);
    }

    public void start(Promise promise) {
        this.client = this.vertx.createNetClient(this.getOptions());
        this.connect(promise);
    }

    public void socketLog(int n, Buffer buffer) {
        this.socketLog(n, buffer.toString());
    }

    public void transmit(Packet packet) {
        this.encoder.handle(packet);
    }

    public SocketClient() {
        this.encoder = new PacketEncoder(this::transmit);
    }

    public static void lambda$login$9(Void void_) {
        System.out.println("Logged in successfully!");
    }

    public void login0() {
        PacketWriter packetWriter = PacketWriter.create(1, PacketType.INT_VARIABLE_SIZED);
        packetWriter.writeString(Storage.ACCESS_KEY);
        packetWriter.writeString(Utils.getLocalAddress());
        packetWriter.writeString(Utils.getMacAddress());
        this.transmit(packetWriter.build());
    }

    public void handle(Packet packet) {
        try {
            PacketHandler packetHandler = PacketHandler.HANDLERS[packet.getOpcode()];
            if (packetHandler == null) {
                throw new Exception("Incoming packet[" + packet.getOpcode() + "] has no handler");
            }
            if (packetHandler instanceof LoginHandler) {
                packetHandler.handle(this.loginPromise, packet);
                return;
            }
            packetHandler.handle(packet, this::transmit);
            return;
        }
        catch (Throwable throwable) {
            logger.warn("Failed to handle packet: {}", (Object)throwable.getMessage());
        }
    }

    public void lambda$reconnect$5(Throwable throwable) {
        logger.warn("Failed to reconnect. Retrying...");
        this.reconnect();
    }

    public void connect(Promise promise) {
        this.client.connect(4450, "34.72.202.156").onSuccess(arg_0 -> this.lambda$connect$0(promise, arg_0)).onFailure(arg_0 -> ((Promise)promise).fail(arg_0));
    }

    public void registerDecoder() {
        this.connection.pause();
        this.connection.handler((Handler)this.decoder);
        this.connection.resume();
    }

    public void lambda$startHeartbeat$8(Long l) {
        if (this.connection == null) return;
        if (!this.handshakeDone) return;
        this.transmit(this.HEARTBEAT);
    }

    public void socketLog(int n, String string) {
        PacketWriter packetWriter = PacketWriter.create(3, PacketType.INT_VARIABLE_SIZED);
        packetWriter.writeInt(n);
        packetWriter.writeString(string);
        this.transmit(packetWriter.build());
    }

    public static void lambda$register$1(Throwable throwable) {
        logger.warn("Connection error: {}", (Object)throwable.getMessage());
    }

    public void onHandshakeACK(Buffer buffer) {
        if (this.handshakeDone) return;
        if (!Arrays.equals(HANDSHAKE_RCV, buffer.getBytes())) return;
        this.handshakeDone = true;
        this.registerDecoder();
        this.startHeartbeat();
        this.login0();
    }

    public static void lambda$stop$7(Promise promise, Void void_) {
        promise.complete();
    }

    public void startHeartbeat() {
        if (this.heartbeatID != -1L) {
            this.vertx.cancelTimer(this.heartbeatID);
        }
        this.heartbeatID = this.vertx.setPeriodic(8888L, this::lambda$startHeartbeat$8);
    }

    public NetClientOptions getOptions() {
        return new NetClientOptions().setConnectTimeout(30000).setTcpKeepAlive(true).setTcpFastOpen(true).setTcpQuickAck(true).setTcpNoDelay(true).setReconnectAttempts(5).setReconnectInterval(10000L).setIdleTimeout(15).setIdleTimeoutUnit(TimeUnit.SECONDS);
    }

    public static void lambda$register$2(Void void_) {
    }

    public static void lambda$login$10(Throwable throwable) {
        System.out.println("Failed to login: " + throwable.getMessage());
    }

    public void socketLog(int n, JsonObject jsonObject) {
        this.socketLog(n, jsonObject.encode());
    }

    public void lambda$connect$0(Promise promise, NetSocket netSocket) {
        this.register(netSocket);
        promise.complete();
    }

    public void stop(Promise promise) {
        this.isClosing = true;
        if (this.client != null) {
            this.client.close().onSuccess(arg_0 -> SocketClient.lambda$stop$7(promise, arg_0)).onFailure(arg_0 -> ((Promise)promise).fail(arg_0));
            return;
        }
        promise.complete();
    }

    public void lambda$reconnect$4(Void void_) {
        logger.info("Successfully reconnected!");
        this.reconnectAttempts = 0;
    }
}

