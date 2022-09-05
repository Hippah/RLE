package rip.hippo.lwjeb.extensions.rle.message.impl;

import com.google.gson.Gson;
import rip.hippo.lwjeb.extensions.rle.message.MessageAdapter;

import java.io.Serializable;

/**
 * @author Hippo
 */
public final class GsonMessageAdapter implements MessageAdapter {

  private final Gson gson;

  public GsonMessageAdapter(Gson gson) {
    this.gson = gson;
  }

  public GsonMessageAdapter() {
    this(new Gson());
  }

  @Override
  public String toMessage(Serializable serializable) {
    String name = serializable.getClass().getName();
    return name.length() + ":" + name + gson.toJson(serializable);
  }

  @Override
  public Serializable toSerializable(String message) throws ClassNotFoundException {
    int length = Integer.parseInt(message.substring(0, message.indexOf(':')));
    int extra = String.valueOf(length).length() + 1;
    String className = message.substring(message.indexOf(':') + 1, length + extra);
    return (Serializable) gson.fromJson(message.substring(className.length() + extra),
        getClassLoader().loadClass(className));
  }

  public ClassLoader getClassLoader() {
    return getClass().getClassLoader();
  }
}
