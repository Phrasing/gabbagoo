package io.trickle.network.codec.decoder;

import io.trickle.core.VertxSingleton;
import io.trickle.network.codec.packet.Packet;
import io.trickle.network.codec.packet.PacketType;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import java.util.concurrent.CompletableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketDecoder implements Handler {
   public static Logger logger = LogManager.getLogger("TRICKLE");
   public Handler packetHandler;
   public RecordParser parser;
   public int size = -1;
   public Reader reader = new Reader();
   public byte opcode = -1;

   public void handle(Object var1) {
      this.handle((Buffer)var1);
   }

   public static void lambda$serviceOpcodes$0() {
      System.exit(1);
   }

   public Handler getPacketHandler() {
      return this.packetHandler;
   }

   public boolean serviceOpcodes() {
      switch (this.opcode) {
         case -15:
            return true;
         case -10:
            CompletableFuture.runAsync(PacketDecoder::lambda$serviceOpcodes$0);
            return true;
         default:
            return false;
      }
   }

   public void reset() {
      this.opcode = -1;
      this.size = -1;
      this.parser.fixedSizeMode(1);
      this.parser.handler(this::readOpcode);
   }

   public void handleException(Throwable var1) {
      logger.error("Error occurred decoding packet: {}", var1.getMessage());
      this.reset();
   }

   public void readSize(Buffer var1) {
      if (this.size == -1) {
         this.size = var1.getInt(0);
         this.parser.fixedSizeMode(this.size);
         this.parser.handler(this::readPayload);
      }

   }

   public void readOpcode(Buffer var1) {
      if (this.opcode == -1) {
         this.opcode = var1.getByte(0);
         if (this.serviceOpcodes()) {
            this.reset();
         } else {
            this.parser.fixedSizeMode(4);
            this.parser.handler(this::readSize);
         }
      }

   }

   public void handle(Buffer var1) {
      this.parser.handle(var1);
   }

   public PacketDecoder(Handler var1) {
      this.packetHandler = var1;
      this.parser = RecordParser.newFixed(1).exceptionHandler(this::handleException).handler(this::readOpcode);
   }

   public void readPayload(Buffer var1) {
      String var2 = var1.toString();
      byte[] var3 = this.reader.read(var2);
      if (var3 != null && var3.length != 0) {
         Packet var4 = new Packet(this.opcode, PacketType.FIXED_SIZE, Buffer.buffer(var3));
         if (logger.isDebugEnabled()) {
            logger.debug("Received packet of opcode: {} and size {}", var4.getOpcode(), var4.getSize());
         }

         this.packetHandler.handle(var4);
      } else {
         System.out.println("Your system time is too out of sync for security reasons. Please re-sync/re-set your current date&time");
         VertxSingleton.INSTANCE.get().eventBus().send("login.loader", "time-sync");
      }

      this.reset();
   }
}
