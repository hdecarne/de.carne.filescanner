/*
 * Copyright (c) 2007-2016 Holger de Carne and contributors, All Rights Reserved.
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
package de.carne.filescanner.util;

/**
 * Utility class used for hex formatting any kind of data.
 */
public final class Hex {

	private static final char[] LOWER_NIBBLE_MAP = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
			'b', 'c', 'd', 'e', 'f' };

	private static final char[] UPPER_NIBBLE_MAP = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
			'B', 'C', 'D', 'E', 'F' };

	/**
	 * Format a {@code byte} value using lower case characters.
	 *
	 * @param buffer The buffer to format into.
	 * @param b The value to format.
	 * @return The updated buffer.
	 */
	public static StringBuilder formatL(StringBuilder buffer, byte b) {
		buffer.append(LOWER_NIBBLE_MAP[(b >> 4) & 0xf]);
		buffer.append(LOWER_NIBBLE_MAP[b & 0xf]);
		return buffer;
	}

	/**
	 * Format a {@code byte} value using upper case characters.
	 *
	 * @param buffer The buffer to format into.
	 * @param b The value to format.
	 * @return The updated buffer.
	 */
	public static StringBuilder formatU(StringBuilder buffer, byte b) {
		buffer.append(UPPER_NIBBLE_MAP[(b >> 4) & 0xf]);
		buffer.append(UPPER_NIBBLE_MAP[b & 0xf]);
		return buffer;
	}

	/**
	 * Format a {@code long} value using lower case characters.
	 *
	 * @param buffer The buffer to format into.
	 * @param l The value to format.
	 * @return The updated buffer.
	 */
	public static StringBuilder formatL(StringBuilder buffer, long l) {
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 60) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 56) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 52) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 48) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 44) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 40) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 36) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 32) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 28) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 24) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 20) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 16) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 12) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 8) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) ((l >>> 4) & 0xfl)]);
		buffer.append(LOWER_NIBBLE_MAP[(int) (l & 0xfl)]);
		return buffer;
	}

	/**
	 * Format a {@code long} value using upper case characters.
	 *
	 * @param buffer The buffer to format into.
	 * @param l The value to format.
	 * @return The updated buffer.
	 */
	public static StringBuilder formatU(StringBuilder buffer, long l) {
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 60) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 56) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 52) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 48) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 44) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 40) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 36) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 32) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 28) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 24) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 20) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 16) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 12) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 8) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) ((l >>> 4) & 0xfl)]);
		buffer.append(UPPER_NIBBLE_MAP[(int) (l & 0xfl)]);
		return buffer;
	}

}
