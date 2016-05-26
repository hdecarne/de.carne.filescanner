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
package de.carne.filescanner.core.format.spec;

import java.text.DateFormat;

import de.carne.filescanner.util.Dos;
import de.carne.filescanner.util.Hexadecimal;
import de.carne.filescanner.util.Units;

/**
 * Helper functions and the like for {@linkplain NumberAttributeType#U16}
 * attributes.
 */
public final class U16Attributes {

	/**
	 * Decimal format.
	 */
	public static final NumberFormat<Short> DECIMAL_FORMAT = new NumberFormat<Short>() {

		@Override
		public String apply(Short t) {
			return java.text.NumberFormat.getNumberInstance().format(t);
		}

	};

	/**
	 * Hexadecimal format.
	 */
	public static final NumberFormat<Short> HEXADECIMAL_FORMAT = new NumberFormat<Short>() {

		@Override
		public String apply(Short t) {
			return Hexadecimal.formatL(new StringBuilder("0x"), t.shortValue()).toString();
		}

	};

	/**
	 * Comment renderer for a byte count value.
	 */
	public static final CommentRenderer<Short> BYTE_COUNT_COMMENT = new CommentRenderer<>(
			v -> Units.formatByteValue(v.longValue()));

	/**
	 * Comment renderer for DOS date values.
	 */
	public static final CommentRenderer<Short> DOS_DATE_COMMENT = new CommentRenderer<>(
			v -> DateFormat.getDateInstance().format(Dos.dosDateToDate(v.shortValue())));

	/**
	 * Comment renderer for DOS time values.
	 */
	public static final CommentRenderer<Short> DOS_TIME_COMMENT = new CommentRenderer<>(
			v -> DateFormat.getTimeInstance().format(Dos.dosTimeToDate(v.shortValue())));

}
