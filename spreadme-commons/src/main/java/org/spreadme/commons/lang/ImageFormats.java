/*
 * Copyright [2019] [shuwei.wang (c) wswill@foxmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.spreadme.commons.lang;

/**
 * Image Formats
 * @author shuwei.wang
 */
public enum ImageFormats {

	UNKNOWN,
	BMP,
	DCX,
	GIF,
	ICNS,
	ICO,
	JBIG2,
	JPEG,
	PAM,
	PSD,
	PBM,
	PGM,
	PNM,
	PPM,
	PCX,
	PNG,
	RGBE,
	TGA,
	TIFF,
	WBMP,
	XBM,
	XPM;

	public String getName() {
		return name().toLowerCase();
	}

	public String getExtension() {
		return name().toLowerCase();
	}
}
