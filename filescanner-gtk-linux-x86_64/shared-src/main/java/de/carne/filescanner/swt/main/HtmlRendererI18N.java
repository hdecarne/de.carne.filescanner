/*
 * I18N resource strings (automatically generated - do not edit)
 */
package de.carne.filescanner.swt.main;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Resource bundle: de/carne/filescanner/swt/main/HtmlRendererI18N.properties
 */
public final class HtmlRendererI18N {

	/**
	 * The name of the {@linkplain ResourceBundle} wrapped by this class.
	 */
	public static final String BUNDLE_NAME = HtmlRendererI18N.class.getName();

	/**
	 * The {@linkplain ResourceBundle} wrapped by this class.
	 */
	public static final ResourceBundle BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private HtmlRendererI18N() {
		// Prevent instantiation
	}

	/**
	 * Format a resource string.
	 * @param key The resource key.
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String format(String key, Object... arguments) {
		String pattern = BUNDLE.getString(key);

		return MessageFormat.format(pattern, arguments);
	}

	/**
	 * Resource key {@code I18N_EPILOGUE}
	 * <p>
	 * &lt;&frasl;body&gt;&lt;&frasl;html&gt;
	 */
	public static final String I18N_EPILOGUE = "I18N_EPILOGUE";

	/**
	 * Resource string {@code I18N_EPILOGUE}
	 * <p>
	 * &lt;&frasl;body&gt;&lt;&frasl;html&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nEpilogue(Object... arguments) {
		return format(I18N_EPILOGUE, arguments);
	}

	/**
	 * Resource key {@code I18N_HREF_IMAGE_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;a href=&quot;{3}&quot;&gt;&lt;img src=&quot;{1}&quot; alt=&quot;{2}&quot;&gt;&lt;&frasl;a&gt;&lt;&frasl;span&gt;
	 */
	public static final String I18N_HREF_IMAGE_WITHOUT_BREAK = "I18N_HREF_IMAGE_WITHOUT_BREAK";

	/**
	 * Resource string {@code I18N_HREF_IMAGE_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;a href=&quot;{3}&quot;&gt;&lt;img src=&quot;{1}&quot; alt=&quot;{2}&quot;&gt;&lt;&frasl;a&gt;&lt;&frasl;span&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nHrefImageWithoutBreak(Object... arguments) {
		return format(I18N_HREF_IMAGE_WITHOUT_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_HREF_IMAGE_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;a href=&quot;{3}&quot;&gt;&lt;img src=&quot;{1}&quot; alt=&quot;{2}&quot;&gt;&lt;&frasl;a&gt;&lt;&frasl;span&gt;&lt;br&gt;
	 */
	public static final String I18N_HREF_IMAGE_WITH_BREAK = "I18N_HREF_IMAGE_WITH_BREAK";

	/**
	 * Resource string {@code I18N_HREF_IMAGE_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;a href=&quot;{3}&quot;&gt;&lt;img src=&quot;{1}&quot; alt=&quot;{2}&quot;&gt;&lt;&frasl;a&gt;&lt;&frasl;span&gt;&lt;br&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nHrefImageWithBreak(Object... arguments) {
		return format(I18N_HREF_IMAGE_WITH_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_HREF_MEDIA_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;[&lt;a href=&quot;{1}&quot;&gt;{2}&lt;&frasl;a&gt;]&lt;&frasl;span&gt;
	 */
	public static final String I18N_HREF_MEDIA_WITHOUT_BREAK = "I18N_HREF_MEDIA_WITHOUT_BREAK";

	/**
	 * Resource string {@code I18N_HREF_MEDIA_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;[&lt;a href=&quot;{1}&quot;&gt;{2}&lt;&frasl;a&gt;]&lt;&frasl;span&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nHrefMediaWithoutBreak(Object... arguments) {
		return format(I18N_HREF_MEDIA_WITHOUT_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_HREF_MEDIA_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;[&lt;a href=&quot;{1}&quot;&gt;{2}&lt;&frasl;a&gt;]&lt;&frasl;span&gt;&lt;br&gt;
	 */
	public static final String I18N_HREF_MEDIA_WITH_BREAK = "I18N_HREF_MEDIA_WITH_BREAK";

	/**
	 * Resource string {@code I18N_HREF_MEDIA_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;[&lt;a href=&quot;{1}&quot;&gt;{2}&lt;&frasl;a&gt;]&lt;&frasl;span&gt;&lt;br&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nHrefMediaWithBreak(Object... arguments) {
		return format(I18N_HREF_MEDIA_WITH_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_HREF_TEXT_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;a href=&quot;{2}&quot;&gt;{1}&lt;&frasl;a&gt;&lt;&frasl;span&gt;
	 */
	public static final String I18N_HREF_TEXT_WITHOUT_BREAK = "I18N_HREF_TEXT_WITHOUT_BREAK";

	/**
	 * Resource string {@code I18N_HREF_TEXT_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;a href=&quot;{2}&quot;&gt;{1}&lt;&frasl;a&gt;&lt;&frasl;span&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nHrefTextWithoutBreak(Object... arguments) {
		return format(I18N_HREF_TEXT_WITHOUT_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_HREF_TEXT_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;a href=&quot;{2}&quot;&gt;{1}&lt;&frasl;a&gt;&lt;&frasl;span&gt;&lt;br&gt;
	 */
	public static final String I18N_HREF_TEXT_WITH_BREAK = "I18N_HREF_TEXT_WITH_BREAK";

	/**
	 * Resource string {@code I18N_HREF_TEXT_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;a href=&quot;{2}&quot;&gt;{1}&lt;&frasl;a&gt;&lt;&frasl;span&gt;&lt;br&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nHrefTextWithBreak(Object... arguments) {
		return format(I18N_HREF_TEXT_WITH_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_INDENT_IN}
	 * <p>
	 * &lt;div class=&quot;indent&quot;&gt;
	 */
	public static final String I18N_INDENT_IN = "I18N_INDENT_IN";

	/**
	 * Resource string {@code I18N_INDENT_IN}
	 * <p>
	 * &lt;div class=&quot;indent&quot;&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nIndentIn(Object... arguments) {
		return format(I18N_INDENT_IN, arguments);
	}

	/**
	 * Resource key {@code I18N_INDENT_OUT}
	 * <p>
	 * &lt;&frasl;div&gt;
	 */
	public static final String I18N_INDENT_OUT = "I18N_INDENT_OUT";

	/**
	 * Resource string {@code I18N_INDENT_OUT}
	 * <p>
	 * &lt;&frasl;div&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nIndentOut(Object... arguments) {
		return format(I18N_INDENT_OUT, arguments);
	}

	/**
	 * Resource key {@code I18N_PROLOGUE_DEFAULT}
	 * <p>
	 * &lt;!DOCTYPE HTML&gt;&lt;html&gt;&lt;head&gt;&lt;meta charset=&quot;utf-8&quot;&gt;&lt;base href=&quot;{0}&quot;&gt;&lt;link rel=&quot;stylesheet&quot; type=&quot;text&frasl;css&quot; href=&quot;{1}&quot;&gt;&lt;&frasl;head&gt;&lt;body&gt;
	 */
	public static final String I18N_PROLOGUE_DEFAULT = "I18N_PROLOGUE_DEFAULT";

	/**
	 * Resource string {@code I18N_PROLOGUE_DEFAULT}
	 * <p>
	 * &lt;!DOCTYPE HTML&gt;&lt;html&gt;&lt;head&gt;&lt;meta charset=&quot;utf-8&quot;&gt;&lt;base href=&quot;{0}&quot;&gt;&lt;link rel=&quot;stylesheet&quot; type=&quot;text&frasl;css&quot; href=&quot;{1}&quot;&gt;&lt;&frasl;head&gt;&lt;body&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nPrologueDefault(Object... arguments) {
		return format(I18N_PROLOGUE_DEFAULT, arguments);
	}

	/**
	 * Resource key {@code I18N_PROLOGUE_EXTENDED}
	 * <p>
	 * &lt;!DOCTYPE HTML&gt;&lt;html&gt;&lt;head&gt;&lt;meta charset=&quot;utf-8&quot;&gt;&lt;base href=&quot;{0}&quot;&gt;&lt;link rel=&quot;stylesheet&quot; type=&quot;text&frasl;css&quot; href=&quot;{1}&quot;&gt;&lt;&frasl;head&gt;&lt;body class=&quot;{2}&quot;&gt;
	 */
	public static final String I18N_PROLOGUE_EXTENDED = "I18N_PROLOGUE_EXTENDED";

	/**
	 * Resource string {@code I18N_PROLOGUE_EXTENDED}
	 * <p>
	 * &lt;!DOCTYPE HTML&gt;&lt;html&gt;&lt;head&gt;&lt;meta charset=&quot;utf-8&quot;&gt;&lt;base href=&quot;{0}&quot;&gt;&lt;link rel=&quot;stylesheet&quot; type=&quot;text&frasl;css&quot; href=&quot;{1}&quot;&gt;&lt;&frasl;head&gt;&lt;body class=&quot;{2}&quot;&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nPrologueExtended(Object... arguments) {
		return format(I18N_PROLOGUE_EXTENDED, arguments);
	}

	/**
	 * Resource key {@code I18N_SIMPLE_IMAGE_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;img src=&quot;{1}&quot; alt=&quot;{2}&quot;&gt;&lt;&frasl;span&gt;
	 */
	public static final String I18N_SIMPLE_IMAGE_WITHOUT_BREAK = "I18N_SIMPLE_IMAGE_WITHOUT_BREAK";

	/**
	 * Resource string {@code I18N_SIMPLE_IMAGE_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;img src=&quot;{1}&quot; alt=&quot;{2}&quot;&gt;&lt;&frasl;span&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nSimpleImageWithoutBreak(Object... arguments) {
		return format(I18N_SIMPLE_IMAGE_WITHOUT_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_SIMPLE_IMAGE_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;img src=&quot;{1}&quot; alt=&quot;{2}&quot;&gt;&lt;&frasl;span&gt;&lt;br&gt;
	 */
	public static final String I18N_SIMPLE_IMAGE_WITH_BREAK = "I18N_SIMPLE_IMAGE_WITH_BREAK";

	/**
	 * Resource string {@code I18N_SIMPLE_IMAGE_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;&lt;img src=&quot;{1}&quot; alt=&quot;{2}&quot;&gt;&lt;&frasl;span&gt;&lt;br&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nSimpleImageWithBreak(Object... arguments) {
		return format(I18N_SIMPLE_IMAGE_WITH_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_SIMPLE_MEDIA_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;[&lt;a href=&quot;{1}&quot;&gt;{2}&lt;&frasl;a&gt;]&lt;&frasl;span&gt;
	 */
	public static final String I18N_SIMPLE_MEDIA_WITHOUT_BREAK = "I18N_SIMPLE_MEDIA_WITHOUT_BREAK";

	/**
	 * Resource string {@code I18N_SIMPLE_MEDIA_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;[&lt;a href=&quot;{1}&quot;&gt;{2}&lt;&frasl;a&gt;]&lt;&frasl;span&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nSimpleMediaWithoutBreak(Object... arguments) {
		return format(I18N_SIMPLE_MEDIA_WITHOUT_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_SIMPLE_MEDIA_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;[&lt;a href=&quot;{1}&quot;&gt;{2}&lt;&frasl;a&gt;]&lt;&frasl;span&gt;&lt;br&gt;
	 */
	public static final String I18N_SIMPLE_MEDIA_WITH_BREAK = "I18N_SIMPLE_MEDIA_WITH_BREAK";

	/**
	 * Resource string {@code I18N_SIMPLE_MEDIA_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;[&lt;a href=&quot;{1}&quot;&gt;{2}&lt;&frasl;a&gt;]&lt;&frasl;span&gt;&lt;br&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nSimpleMediaWithBreak(Object... arguments) {
		return format(I18N_SIMPLE_MEDIA_WITH_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_SIMPLE_TEXT_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;{1}&lt;&frasl;span&gt;
	 */
	public static final String I18N_SIMPLE_TEXT_WITHOUT_BREAK = "I18N_SIMPLE_TEXT_WITHOUT_BREAK";

	/**
	 * Resource string {@code I18N_SIMPLE_TEXT_WITHOUT_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;{1}&lt;&frasl;span&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nSimpleTextWithoutBreak(Object... arguments) {
		return format(I18N_SIMPLE_TEXT_WITHOUT_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_SIMPLE_TEXT_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;{1}&lt;&frasl;span&gt;&lt;br&gt;
	 */
	public static final String I18N_SIMPLE_TEXT_WITH_BREAK = "I18N_SIMPLE_TEXT_WITH_BREAK";

	/**
	 * Resource string {@code I18N_SIMPLE_TEXT_WITH_BREAK}
	 * <p>
	 * &lt;span class=&quot;{0}&quot;&gt;{1}&lt;&frasl;span&gt;&lt;br&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nSimpleTextWithBreak(Object... arguments) {
		return format(I18N_SIMPLE_TEXT_WITH_BREAK, arguments);
	}

	/**
	 * Resource key {@code I18N_TEXT_DEFAULT_RESULT_VIEW_HTML}
	 * <p>
	 * &lt;!DOCTYPE HTML&gt;&lt;html&gt;&lt;head&gt;&lt;meta charset=&quot;utf-8&quot;&gt;&lt;&frasl;head&gt;&lt;body&gt;&lt;code style=&quot;color:silver;&quot;&gt;{0} {1} (build: {2})&lt;&frasl;code&gt;&lt;&frasl;body&gt;&lt;&frasl;html&gt;
	 */
	public static final String I18N_TEXT_DEFAULT_RESULT_VIEW_HTML = "I18N_TEXT_DEFAULT_RESULT_VIEW_HTML";

	/**
	 * Resource string {@code I18N_TEXT_DEFAULT_RESULT_VIEW_HTML}
	 * <p>
	 * &lt;!DOCTYPE HTML&gt;&lt;html&gt;&lt;head&gt;&lt;meta charset=&quot;utf-8&quot;&gt;&lt;&frasl;head&gt;&lt;body&gt;&lt;code style=&quot;color:silver;&quot;&gt;{0} {1} (build: {2})&lt;&frasl;code&gt;&lt;&frasl;body&gt;&lt;&frasl;html&gt;
	 *
	 * @param arguments Format arguments.
	 * @return The formatted string.
	 */
	public static String i18nTextDefaultResultViewHtml(Object... arguments) {
		return format(I18N_TEXT_DEFAULT_RESULT_VIEW_HTML, arguments);
	}

}
