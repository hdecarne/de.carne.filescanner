/*
 * Copyright (c) 2017 Holger de Carne and contributors, All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.carne.filescanner.swt.resources;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;

import de.carne.swt.ResourceException;
import de.carne.swt.graphics.ImageResourcePool;
import de.carne.util.Exceptions;
import de.carne.util.Late;
import de.carne.util.Lazy;

/**
 * Image resources.
 */
public final class Images {

	private Images() {
		// Prevent instantiation
	}

	private static final Late<ImageResourcePool> POOL_HOLDER = new Late<>();

	/**
	 * Initialize the underlying {@linkplain Image}.
	 *
	 * @param device The {@linkplain Device} device to create the {@linkplain Image}s for.
	 */
	public static void setup(Device device) {
		POOL_HOLDER.set(new ImageResourcePool(device));
	}

	private static synchronized Image getImage(String name) {
		Image image;

		try {
			image = POOL_HOLDER.get().get(Images.class, name);
		} catch (ResourceException e) {
			throw Exceptions.toRuntime(e);
		}
		return image;
	}

	/**
	 * fslogo16.png
	 */
	public static final Lazy<Image> IMAGE_FSLOGO16 = new Lazy<>(() -> getImage("fslogo16.png"));
	/**
	 * fslogo32.png
	 */
	public static final Lazy<Image> IMAGE_FSLOGO32 = new Lazy<>(() -> getImage("fslogo32.png"));
	/**
	 * fslogo48.png
	 */
	public static final Lazy<Image> IMAGE_FSLOGO48 = new Lazy<>(() -> getImage("fslogo48.png"));
	/**
	 * fslogo128.png
	 */
	public static final Lazy<Image> IMAGE_FSLOGO128 = new Lazy<>(() -> getImage("fslogo128.png"));
	/**
	 * fslogo{16,32,48,128}.png
	 */
	public static final Lazy<Image[]> IMAGES_FSLOGO = new Lazy<>(() -> new Image[] { IMAGE_FSLOGO16.get(),
			IMAGE_FSLOGO32.get(), IMAGE_FSLOGO48.get(), IMAGE_FSLOGO128.get() });

}