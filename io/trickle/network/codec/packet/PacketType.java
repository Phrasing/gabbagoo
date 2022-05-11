package io.trickle.network.codec.packet;

public enum PacketType {
   FIXED_SIZE,
   INT_VARIABLE_SIZED;

   public static PacketType[] $VALUES = new PacketType[]{FIXED_SIZE, BYTE_VARIABLE_SIZED, SHORT_VARIABLE_SIZED, INT_VARIABLE_SIZED};
   BYTE_VARIABLE_SIZED,
   SHORT_VARIABLE_SIZED;
}
