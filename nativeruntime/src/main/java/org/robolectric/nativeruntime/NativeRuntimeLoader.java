package org.robolectric.nativeruntime;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Locale;

/** Loads the Roboelctric native runtime. */
public final class NativeRuntimeLoader {
  private static final AtomicBoolean loaded = new AtomicBoolean(false);

  static {
    if (isSupported()) {
      ensureLoaded();
    } else {
      throw new AssertionError("The Robolectric native runtime is not supported in this OS/Architecture");
    }
  }

  private NativeRuntimeLoader() {}

  static void ensureLoaded() {
    if (loaded.compareAndSet(false, true)) {
      try {
        String libraryName = System.mapLibraryName("robolectric-nativeruntime");
        System.out.println("DSP Library name = " + libraryName);
        File tmpLibraryFile =
            java.nio.file.Files.createTempFile("", libraryName).toFile();
        tmpLibraryFile.deleteOnExit();
        URL resource = Resources.getResource(nativeLibraryPath());
        Resources.asByteSource(resource).copyTo(Files.asByteSink(tmpLibraryFile));
        System.load(tmpLibraryFile.getAbsolutePath());
      } catch (IOException e) {
        throw new AssertionError("Unable to load Robolectric nativeruntime library", e);
      }
    }
  }

  private static boolean isSupported() {
    return  ("mac".equals(osName()) && "aarch64".equals(arch()) || ("linux".equals(osName()) && "x86_64".equals(arch())));
  }

  private static String nativeLibraryPath() {
    return String.format("native/%s/%s/%s", osName(), arch(), System.mapLibraryName("robolectric-nativeruntime"));
  }

  private static String osName() {
    String osName = System.getProperty("os.name").toLowerCase(Locale.US);
    if (osName.contains("linux")) {
      return "linux";
    } else if (osName.contains("mac")) {
      return "mac";
    }
    return "unsupported";
  }

  private static String arch() {
    String arch = System.getProperty("os.arch").toLowerCase(Locale.US);
    if (arch.equals("x86_64") || arch.equals("amd64")) {
      return "x86_84";
    } else if (arch.equals("aarch64")) {
      return "aarch64";
    }
    return "unsupported";
  }
}
