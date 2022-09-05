package rip.hippo.lwjeb.extensions.rle.message;

import java.io.Serializable;

/**
 * @author Hippo
 */
public interface MessageAdapter {
  String toMessage(Serializable serializable);
  Serializable toSerializable(String message) throws ClassNotFoundException;
}
