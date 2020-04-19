/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.web.config;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.Jsp;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;

/**
 * {@link ConfigurationProperties} for a web server (e.g. port and path settings).
 *
 * @author Dave Syer
 * @author Stephane Nicoll
 * @author Andy Wilkinson
 * @author Ivan Sopov
 * @author Marcos Barbero
 * @author Eddú Meléndez
 * @author Quinten De Swaef
 * @author Venil Noronha
 * @author Aurélien Leboulanger
 * @author Brian Clozel
 * @author Olivier Lamy
 * @author Chentao Qu
 * @author Rafiullah Hamedy
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "server")
public class ServerProperties {

	/**
	 * Server HTTP port.
	 */
	private Integer port;

	/**
	 * Network address to which the server should bind.
	 */
	private InetAddress address;

	@NestedConfigurationProperty
	private final ErrorProperties error = new ErrorProperties();

	/**
	 * Whether X-Forwarded-* headers should be applied to the HttpRequest.
	 */
	private Boolean useForwardHeaders;

	/**
	 * Value to use for the Server response header (if empty, no header is sent).
	 */
	private String serverHeader;

	/**
	 * Maximum size of the HTTP message header.
	 */
	private DataSize maxHttpHeaderSize = DataSize.ofKilobytes(8);

	@NestedConfigurationProperty
	private Ssl ssl;

	@NestedConfigurationProperty
	private final Compression compression = new Compression();

	@NestedConfigurationProperty
	private final Http2 http2 = new Http2();

	private final Servlet servlet = new Servlet();

	private final Undertow undertow = new Undertow();

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public InetAddress getAddress() {
		return this.address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public Boolean isUseForwardHeaders() {
		return this.useForwardHeaders;
	}

	public void setUseForwardHeaders(Boolean useForwardHeaders) {
		this.useForwardHeaders = useForwardHeaders;
	}

	public String getServerHeader() {
		return this.serverHeader;
	}

	public void setServerHeader(String serverHeader) {
		this.serverHeader = serverHeader;
	}

	public DataSize getMaxHttpHeaderSize() {
		return this.maxHttpHeaderSize;
	}

	public void setMaxHttpHeaderSize(DataSize maxHttpHeaderSize) {
		this.maxHttpHeaderSize = maxHttpHeaderSize;
	}

	public ErrorProperties getError() {
		return this.error;
	}

	public Ssl getSsl() {
		return this.ssl;
	}

	public void setSsl(Ssl ssl) {
		this.ssl = ssl;
	}

	public Compression getCompression() {
		return this.compression;
	}

	public Http2 getHttp2() {
		return this.http2;
	}

	public Servlet getServlet() {
		return this.servlet;
	}

	public Undertow getUndertow() {
		return this.undertow;
	}

	/**
	 * Servlet properties.
	 */
	public static class Servlet {

		/**
		 * Servlet context init parameters.
		 */
		private final Map<String, String> contextParameters = new HashMap<>();

		/**
		 * Context path of the application.
		 */
		private String contextPath;

		/**
		 * Display name of the application.
		 */
		private String applicationDisplayName = "application";

		@NestedConfigurationProperty
		private final Jsp jsp = new Jsp();

		@NestedConfigurationProperty
		private final Session session = new Session();

		public String getContextPath() {
			return this.contextPath;
		}

		public void setContextPath(String contextPath) {
			this.contextPath = cleanContextPath(contextPath);
		}

		private String cleanContextPath(String contextPath) {
			if (StringUtils.hasText(contextPath) && contextPath.endsWith("/")) {
				return contextPath.substring(0, contextPath.length() - 1);
			}
			return contextPath;
		}

		public String getApplicationDisplayName() {
			return this.applicationDisplayName;
		}

		public void setApplicationDisplayName(String displayName) {
			this.applicationDisplayName = displayName;
		}

		public Map<String, String> getContextParameters() {
			return this.contextParameters;
		}

		public Jsp getJsp() {
			return this.jsp;
		}

		public Session getSession() {
			return this.session;
		}

	}

	/**
	 * Undertow properties.
	 */
	public static class Undertow {

		/**
		 * Maximum size of the HTTP post content. When the value is -1, the default, the
		 * size is unlimited.
		 */
		private DataSize maxHttpPostSize = DataSize.ofBytes(-1);

		/**
		 * Size of each buffer. The default is derived from the maximum amount of memory
		 * that is available to the JVM.
		 */
		private DataSize bufferSize;

		/**
		 * Number of I/O threads to create for the worker. The default is derived from the
		 * number of available processors.
		 */
		private Integer ioThreads;

		/**
		 * Number of worker threads. The default is 8 times the number of I/O threads.
		 */
		private Integer workerThreads;

		/**
		 * Whether to allocate buffers outside the Java heap. The default is derived from
		 * the maximum amount of memory that is available to the JVM.
		 */
		private Boolean directBuffers;

		/**
		 * Whether servlet filters should be initialized on startup.
		 */
		private boolean eagerFilterInit = true;

		private final Accesslog accesslog = new Accesslog();

		public DataSize getMaxHttpPostSize() {
			return this.maxHttpPostSize;
		}

		public void setMaxHttpPostSize(DataSize maxHttpPostSize) {
			this.maxHttpPostSize = maxHttpPostSize;
		}

		public DataSize getBufferSize() {
			return this.bufferSize;
		}

		public void setBufferSize(DataSize bufferSize) {
			this.bufferSize = bufferSize;
		}

		public Integer getIoThreads() {
			return this.ioThreads;
		}

		public void setIoThreads(Integer ioThreads) {
			this.ioThreads = ioThreads;
		}

		public Integer getWorkerThreads() {
			return this.workerThreads;
		}

		public void setWorkerThreads(Integer workerThreads) {
			this.workerThreads = workerThreads;
		}

		public Boolean getDirectBuffers() {
			return this.directBuffers;
		}

		public void setDirectBuffers(Boolean directBuffers) {
			this.directBuffers = directBuffers;
		}

		public boolean isEagerFilterInit() {
			return this.eagerFilterInit;
		}

		public void setEagerFilterInit(boolean eagerFilterInit) {
			this.eagerFilterInit = eagerFilterInit;
		}

		public Accesslog getAccesslog() {
			return this.accesslog;
		}

		/**
		 * Undertow access log properties.
		 */
		public static class Accesslog {

			/**
			 * Whether to enable the access log.
			 */
			private boolean enabled = false;

			/**
			 * Format pattern for access logs.
			 */
			private String pattern = "common";

			/**
			 * Log file name prefix.
			 */
			protected String prefix = "access_log.";

			/**
			 * Log file name suffix.
			 */
			private String suffix = "log";

			/**
			 * Undertow access log directory.
			 */
			private File dir = new File("logs");

			/**
			 * Whether to enable access log rotation.
			 */
			private boolean rotate = true;

			public boolean isEnabled() {
				return this.enabled;
			}

			public void setEnabled(boolean enabled) {
				this.enabled = enabled;
			}

			public String getPattern() {
				return this.pattern;
			}

			public void setPattern(String pattern) {
				this.pattern = pattern;
			}

			public String getPrefix() {
				return this.prefix;
			}

			public void setPrefix(String prefix) {
				this.prefix = prefix;
			}

			public String getSuffix() {
				return this.suffix;
			}

			public void setSuffix(String suffix) {
				this.suffix = suffix;
			}

			public File getDir() {
				return this.dir;
			}

			public void setDir(File dir) {
				this.dir = dir;
			}

			public boolean isRotate() {
				return this.rotate;
			}

			public void setRotate(boolean rotate) {
				this.rotate = rotate;
			}

		}

	}
}
