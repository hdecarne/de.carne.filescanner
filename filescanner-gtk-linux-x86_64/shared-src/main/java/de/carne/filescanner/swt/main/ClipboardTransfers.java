/*
 * Copyright (c) 2007-2018 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.filescanner.swt.main;

import java.util.HashSet;
import java.util.Set;

import de.carne.filescanner.engine.FileScannerResultExporter;

final class ClipboardTransfers {

	private static final Set<FileScannerResultExporter.Type> TRANSFERABLE_TYPES = new HashSet<>();

	static {

	}

	private ClipboardTransfers() {
		// prevent instantiation
	}

	public static boolean isTransferable(FileScannerResultExporter.Type type) {
		return TRANSFERABLE_TYPES.contains(type);
	}

}
