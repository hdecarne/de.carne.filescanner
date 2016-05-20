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
package de.carne.filescanner.core.format;

import java.util.function.Supplier;

/**
 * This class defines basic format spec attributes.
 * <p>
 * Basic attributes are of the form &lt;name&gt; = &lt;value&gt; where value is
 * a reasonable simple type. Basic attributes can be bound to the decode context
 * and hence evaluated by a format spec.
 * </p>
 *
 * @param <T> The attribute' data type.
 */
public abstract class Attribute<T> extends FormatSpec implements Supplier<T> {

	/**
	 * Possible bind scopes for an attribute.
	 */
	public static enum BindScope {

		/**
		 * Attribute not bound.
		 */
		NONE,

		/**
		 * Attribute bound with decode scope.
		 */
		DECODE,

		/**
		 * Attribute bound with result scope.
		 */
		RESULT

	}

	private final String name;

	private BindScope bindScope = BindScope.NONE;

	/**
	 * Construct {@code Attribute}.
	 *
	 * @param name The attribute's name.
	 */
	protected Attribute(String name) {
		assert name != null;

		this.name = name;
	}

	/**
	 * Get the attribute's name.
	 *
	 * @return The attribute's name.
	 */
	public final String name() {
		return this.name;
	}

	/**
	 * Get the attribute's value type.
	 *
	 * @return The attribute's value type.
	 */
	public abstract Class<T> getValueType();

	/**
	 * Mark this attribute as locally bound.
	 *
	 * @return The updated data attribute spec.
	 * @see #bind(boolean)
	 */
	public final Attribute<T> bind() {
		return bind(false);
	}

	/**
	 * Mark this attribute as bound.
	 * <p>
	 * The values of bound attributes are written to the decode context during
	 * the spec's evaluation. Decode bound attributes are only available via the
	 * eval phase. Result bound attributes are also available during the render
	 * phase.
	 * </p>
	 *
	 * @param result Whether this attribute is result bound ({@code true}) or
	 *        only decode bound ({@code false}).
	 * @return The updated data attribute spec.
	 */
	public final Attribute<T> bind(boolean result) {
		this.bindScope = (result ? BindScope.RESULT : BindScope.DECODE);
		return this;
	}

	/**
	 * Get the attribute's bind scope.
	 * 
	 * @return The attribute's bind scope.
	 */
	public final BindScope bindScope() {
		return this.bindScope;
	}

	/**
	 * Check whether this attribute is bound.
	 *
	 * @return [@code true} if the attribute is bound.
	 */
	public final boolean isBound() {
		return this.bindScope != BindScope.NONE;
	}

	/**
	 * Bind the attribute value.
	 *
	 * @param value The value to bind.
	 */
	protected final void bindValue(T value) {
		ResultContextHolder.get().setAttribute(this, value);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.function.Supplier#get()
	 */
	@Override
	public T get() {
		assert this.bindScope != BindScope.NONE;

		return ResultContextHolder.get().getAttribute(this);
	}

}
