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
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SocketClient extends AbstractVerticle {
   public Buffer HEARTBEAT = Buffer.buffer(new byte[]{-15});
   public long heartbeatID = -1L;
   public int reconnectAttempts = 0;
   public PacketEncoder encoder = new PacketEncoder(this::transmit);
   public Promise loginPromise;
   public boolean handshakeDone = false;
   public PacketDecoder decoder = new PacketDecoder(this::handle);
   public NetSocket connection;
   public static byte[] HANDSHAKE_RCV = new byte[]{0, 4, 55, 9, 3, 13};
   public boolean isClosing = false;
   public NetClient client;
   public static Logger logger = LogManager.getLogger("TRICKLE");

   public void lambda$startHeartbeat$8(Long var1) {
      if (this.connection != null && this.handshakeDone) {
         this.transmit(this.HEARTBEAT);
      }

   }

   public static void lambda$stop$7(Promise var0, Void var1) {
      var0.complete();
   }

   public static void lambda$login$10(Throwable var0) {
      System.out.println("Failed to login: " + var0.getMessage());
   }

   public void closeHandler(Void var1) {
      if (!this.isClosing) {
         this.reconnect();
      }

   }

   public Future transmit(Buffer var1) {
      Future var2 = this.connection.write(var1);
      if (this.connection.writeQueueFull()) {
         this.connection.pause();
      }

      return var2;
   }

   public void handle(Packet var1) {
      try {
         PacketHandler var2 = PacketHandler.HANDLERS[var1.getOpcode()];
         if (var2 == null) {
            throw new Exception("Incoming packet[" + var1.getOpcode() + "] has no handler");
         }

         if (var2 instanceof LoginHandler) {
            var2.handle(this.loginPromise, var1);
         } else {
            var2.handle(var1, this::transmit);
         }
      } catch (Throwable var3) {
         logger.warn("Failed to handle packet: {}", var3.getMessage());
      }

   }

   public void register(NetSocket var1) {
      this.connection = var1;
      this.connection.pause();
      this.handshakeDone = false;
      this.connection.exceptionHandler(SocketClient::lambda$register$1).closeHandler(this::closeHandler).endHandler(SocketClient::lambda$register$2).drainHandler(this::lambda$register$3).handler(this::onHandshakeACK);
      this.connection.resume();
      this.sendHandshake();
   }

   public static void lambda$register$2(Void var0) {
   }

   public void registerDecoder() {
      this.connection.pause();
      this.connection.handler(this.decoder);
      this.connection.resume();
   }

   public void onHandshakeACK(Buffer var1) {
      if (!this.handshakeDone && Arrays.equals(HANDSHAKE_RCV, var1.getBytes())) {
         this.handshakeDone = true;
         this.registerDecoder();
         this.startHeartbeat();
         this.login0();
      }

   }

   public static void lambda$register$1(Throwable var0) {
      logger.warn("Connection error: {}", var0.getMessage());
   }

   public void startHeartbeat() {
      if (this.heartbeatID != -1L) {
         super.vertx.cancelTimer(this.heartbeatID);
      }

      this.heartbeatID = super.vertx.setPeriodic(8888L, this::lambda$startHeartbeat$8);
   }

   public Future login() {
      if (this.loginPromise == null || this.loginPromise.future().isComplete()) {
         this.loginPromise = Promise.promise();
      }

      return this.loginPromise.future().onSuccess(SocketClient::lambda$login$9).onFailure(SocketClient::lambda$login$10);
   }

   public void start(Promise var1) {
      this.client = super.vertx.createNetClient(this.getOptions());
      this.connect(var1);
   }

   public void socketLog(int var1, JsonObject var2) {
      this.socketLog(var1, var2.encode());
   }

   public void reconnect() {
      logger.warn("Trying to re-establish connection...");
      if (this.reconnectAttempts++ < 3) {
         super.vertx.setTimer(10000L, this::lambda$reconnect$6);
      } else {
         CommandLineHandler.stop();
      }

   }

   public void transmit(Packet var1) {
      this.encoder.handle(var1);
   }

   public void stop(Promise var1) {
      this.isClosing = true;
      if (this.client != null) {
         Future var10000 = this.client.close().onSuccess(SocketClient::lambda$stop$7);
         Objects.requireNonNull(var1);
         var10000.onFailure(var1::fail);
      } else {
         var1.complete();
      }

   }

   public void login0() {
      PacketWriter var1 = PacketWriter.create((int)1, PacketType.INT_VARIABLE_SIZED);
      var1.writeString(Storage.ACCESS_KEY);
      var1.writeString(Utils.getLocalAddress());
      var1.writeString(Utils.getMacAddress());
      this.transmit(var1.build());
   }

   public void disconnect() {
      this.isClosing = true;
      this.client.close();
   }

   public void lambda$register$3(Void var1) {
      this.connection.resume();
   }

   public void lambda$reconnect$6(Long var1) {
      Promise var2 = Promise.promise();
      this.connect(var2);
      var2.future().onSuccess(this::lambda$reconnect$4).onFailure(this::lambda$reconnect$5);
   }

   public void connect(Promise var1) {
      Future var10000 = this.client.connect(4450, "34.136.103.202").onSuccess(this::lambda$connect$0);
      Objects.requireNonNull(var1);
      var10000.onFailure(var1::fail);
   }

   public void lambda$reconnect$5(Throwable var1) {
      logger.warn("Failed to reconnect. Retrying...");
      this.reconnect();
   }

   public void lambda$connect$0(Promise var1, NetSocket var2) {
      this.register(var2);
      var1.complete();
   }

   public static void lambda$login$9(Void var0) {
      System.out.println("Logged in successfully!");
   }

   public void socketLog(int var1, Buffer var2) {
      this.socketLog(var1, var2.toString());
   }

   public void lambda$reconnect$4(Void var1) {
      logger.info("Successfully reconnected!");
      this.reconnectAttempts = 0;
   }

   public void socketLog(int var1, String var2) {
      PacketWriter var3 = PacketWriter.create((int)3, PacketType.INT_VARIABLE_SIZED);
      var3.writeInt(var1);
      var3.writeString(var2);
      this.transmit(var3.build());
   }

   public NetClientOptions getOptions() {
      return (new NetClientOptions()).setConnectTimeout(30000).setTcpKeepAlive(true).setTcpFastOpen(true).setTcpQuickAck(true).setTcpNoDelay(true).setReconnectAttempts(5).setReconnectInterval(10000L).setIdleTimeout(15).setIdleTimeoutUnit(TimeUnit.SECONDS);
   }

   public void sendHandshake() {
      PacketWriter var1 = PacketWriter.create((int)-3, PacketType.INT_VARIABLE_SIZED);
      var1.writeString("trickle_client_0x00");
      var1.writeString("x4623346df#zig");
      var1.writeShort((int)1);
      var1.writeShort((int)0);
      var1.writeShort((int)278);
      this.transmit(var1.build());
   }
}
