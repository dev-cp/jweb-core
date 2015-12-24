package org.jweb.core.util;

import java.io.Closeable;

import javax.imageio.stream.ImageInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StreamUtil {
	protected static Log log = LogFactory.getLog(StreamUtil.class);

	public static void close(Closeable p) {
		if (p == null)
			return;
		try {
			p.close();
		} catch (Exception e) {
			log.error(e, e);
		}
	}
	
	public static void close(ImageInputStream p) {
		if (p == null)
			return;
		try {
			p.close();
		} catch (Exception e) {
			log.error(e, e);
		}
	}
}