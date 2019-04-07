/*
 * Copyright (c) 2007-2019 Holger de Carne and contributors, All Rights Reserved.
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

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.carne.boot.Exceptions;
import de.carne.boot.check.Check;
import de.carne.boot.logging.Log;
import de.carne.boot.logging.LogLevel;
import de.carne.filescanner.ModuleManifestInfos;
import de.carne.filescanner.engine.FileScannerProgress;
import de.carne.filescanner.engine.FileScannerResult;
import de.carne.filescanner.engine.transfer.FileScannerResultExportHandler;
import de.carne.filescanner.swt.export.ExportDialog;
import de.carne.filescanner.swt.export.ExportOptions;
import de.carne.filescanner.swt.preferences.Config;
import de.carne.filescanner.swt.preferences.PreferencesDialog;
import de.carne.filescanner.swt.preferences.UserPreferences;
import de.carne.filescanner.swt.resources.Images;
import de.carne.filescanner.swt.widgets.Hex;
import de.carne.swt.dnd.DropTargetBuilder;
import de.carne.swt.graphics.ResourceException;
import de.carne.swt.graphics.ResourceTracker;
import de.carne.swt.layout.FillLayoutBuilder;
import de.carne.swt.layout.GridLayoutBuilder;
import de.carne.swt.platform.PlatformIntegration;
import de.carne.swt.util.Property;
import de.carne.swt.util.UICommandSet;
import de.carne.swt.widgets.CompositeBuilder;
import de.carne.swt.widgets.ControlBuilder;
import de.carne.swt.widgets.CoolBarBuilder;
import de.carne.swt.widgets.FileDialogBuilder;
import de.carne.swt.widgets.LabelBuilder;
import de.carne.swt.widgets.MenuBuilder;
import de.carne.swt.widgets.ShellBuilder;
import de.carne.swt.widgets.ShellUserInterface;
import de.carne.swt.widgets.ToolBarBuilder;
import de.carne.swt.widgets.aboutinfo.AboutInfoDialog;
import de.carne.swt.widgets.heapinfo.HeapInfo;
import de.carne.swt.widgets.logview.LogViewDialog;
import de.carne.swt.widgets.notification.Notification;
import de.carne.text.MemoryUnitFormat;
import de.carne.util.Debug;
import de.carne.util.Late;
import de.carne.util.Strings;

/**
 * Main window UI.
 */
public class MainUI extends ShellUserInterface {

	private static final Log LOG = new Log();

	private final ResourceTracker resources;
	private final Late<HtmlRenderServer> resultRenderServerHolder = new Late<>();
	private final Late<MainController> controllerHolder = new Late<>();
	private final Late<Text> searchQueryHolder = new Late<>();
	private final Late<Tree> resultTreeHolder = new Late<>();
	private final Late<Browser> resultViewHolder = new Late<>();
	private final Late<Hex> inputViewHolder = new Late<>();
	private final Late<ProgressBar> sessionProgressHolder = new Late<>();
	private final Late<Label> sessionStatusHolder = new Late<>();
	private final Late<HeapInfo> runtimeHeapHolder = new Late<>();
	private final Late<Menu> copyObjectMenuHolder = new Late<>();
	private final Late<Menu> copyObjectToolHolder = new Late<>();
	private final Late<Menu> contextMenuCopyObjectMenuHolder = new Late<>();
	private final Consumer<Config> configConsumer = this::applyConfig;
	private final UICommandSet sessionCommands = new UICommandSet();
	private final UICommandSet resultSelectionCommands = new UICommandSet();
	private final Property<FileScannerResult> resultSelection = new Property<>();
	private final ExecutorService executor = Executors.newSingleThreadExecutor();

	private enum SearchState {

		DEFAULT,

		WRAP_NEXT,

		WRAP_PREVIOUS

	}

	private SearchState searchState = SearchState.DEFAULT;

	/**
	 * Constructs a new {@linkplain MainUI} instance.
	 *
	 * @param shell the user interface {@linkplain Shell}.
	 */
	public MainUI(Shell shell) {
		super(shell);
		this.resources = ResourceTracker.forDevice(shell.getDisplay());
	}

	/**
	 * Opens the given command line file for scanning.
	 *
	 * @param file the file to scan.
	 */
	public void openCommandLineFile(String file) {
		if (this.resultSelection.get() == null) {
			openFile(file);
		} else {
			Notification.information(root()).withText(MainI18N.i18nTextIgnoringExtraFile())
					.withMessage(MainI18N.i18nMessageIgnoringCommandLineFile()).open();
		}
	}

	/**
	 * Opens the given dropped file for scanning.
	 *
	 * @param file the file to scan.
	 */
	public void openDroppedFile(String[] file) {
		openFile(file[0]);
		if (file.length > 1) {
			Notification.information(root()).withText(MainI18N.i18nTextIgnoringExtraFile())
					.withMessage(MainI18N.i18nMessageIgnoringDroppedFile()).open();
		}
	}

	/**
	 * Opens the given file for scanning.
	 *
	 * @param file the file to scan.
	 */
	public void openFile(String file) {
		LOG.info("Opening file ''{0}''...", file);

		this.resultSelection.set(null);
		try {
			FileScannerResult rootResult = this.controllerHolder.get().openAndScanFile(file);

			setRootResultTreeItem(rootResult);
			this.resultSelection.set(rootResult);
		} catch (Exception e) {
			unexpectedException(e);
		}
	}

	/**
	 * Sends close request to the UI.
	 */
	public void close() {
		root().close();
	}

	void resetSession(boolean session) {
		this.resultRenderServerHolder.get().clearSession();
		this.resultTreeHolder.get().removeAll();
		this.resultViewHolder.get().setText(getDefaultResultView());
		this.sessionProgressHolder.get().setSelection(0);
		this.sessionStatusHolder.get().setText("");
		this.sessionCommands.setEnabled(session);
		this.resultSelection.set(null, true);
	}

	void sessionRunning(boolean running) {
		this.sessionCommands.setEnabled(running);
		this.sessionProgressHolder.get().setEnabled(running);
	}

	void sessionProgress(FileScannerProgress progress) {
		this.sessionProgressHolder.get().setSelection(progress.scanProgress());

		MemoryUnitFormat memoryUnitFormat = MemoryUnitFormat.getMemoryUnitInstance();
		String statusScanned = memoryUnitFormat.format(progress.scannedBytes());
		long scanRate = progress.scanRate();
		String statusRate = (scanRate >= 0 ? memoryUnitFormat.format(progress.scanRate()) : "\u221e");
		int[] elapsedValues = elapsedValues(progress.scanTimeNanos());

		this.sessionStatusHolder.get().setText(MainI18N.i18nTextSessionStatus(statusScanned, statusRate,
				elapsedValues[0], elapsedValues[1], elapsedValues[2], elapsedValues[3]));
	}

	private int[] elapsedValues(long nanos) {
		long remaining = nanos / 1000000;
		int ms = (int) (remaining % 1000);

		remaining /= 1000;

		int s = (int) (remaining % 60);

		remaining /= 60;

		int m = (int) (remaining % 60);

		remaining /= 60;

		int h = (int) remaining;

		return new int[] { h, m, s, ms };
	}

	void sessionResult(FileScannerResult result) {
		TreeItem resultItem = result.getData(TreeItem.class);

		if (resultItem != null) {
			if (resultItem.getParentItem() == null && resultItem.getItemCount() == 0) {
				resultItem.setItemCount(result.childrenCount());
				resultItem.setExpanded(true);
			} else {
				resultItem.setItemCount(result.childrenCount());
			}
			for (FileScannerResult resultChild : result.children()) {
				sessionResult(resultChild);
			}
		}
	}

	void sessionException(Throwable exception) {
		Notification.error(root()).withText(MainI18N.i18nTextScanException())
				.withMessage(MainI18N.i18nMessageScanException(Exceptions.toString(exception))).open();
	}

	private void setRootResultTreeItem(FileScannerResult rootResult) {
		TreeItem rootResultItem = new TreeItem(this.resultTreeHolder.get(), SWT.NONE);

		decorateResultTreeItem(rootResultItem, rootResult, true);
		rootResultItem.setItemCount(rootResult.childrenCount());
		rootResultItem.setData(rootResult);
		rootResult.setData(rootResultItem);
	}

	private void onSetResultTreeItemData(Event event) {
		TreeItem resultItemParent = Check.isInstanceOf(event.item, TreeItem.class).getParentItem();
		FileScannerResult resultParent = Check.isInstanceOf(resultItemParent.getData(), FileScannerResult.class);
		FileScannerResult[] results = resultParent.children();
		int resultItemCount = Math.min(results.length, resultItemParent.getItemCount());

		for (int resultIndex = event.index; resultIndex < resultItemCount; resultIndex++) {
			TreeItem resultItem = resultItemParent.getItem(resultIndex);
			FileScannerResult result = results[resultIndex];

			initializeResultTreeItem(resultItem, result, false);
		}
	}

	private void initializeResultTreeItem(TreeItem item, FileScannerResult result, boolean root) {
		decorateResultTreeItem(item, result, root);
		item.setItemCount(result.childrenCount());
		item.setData(result);
		result.setData(item);
	}

	private void decorateResultTreeItem(TreeItem item, FileScannerResult result, boolean root) {
		String inputName;

		switch (result.type()) {
		case INPUT:
			inputName = root ? shortInputName(result.name()) : result.name();
			item.setText(inputName);
			item.setImage(inputImage(inputName));
			break;
		case FORMAT:
			item.setText(result.name());
			item.setImage(this.resources.getImage(Images.class, Images.IMAGE_RESULT_FORMAT16));
			break;
		case ENCODED_INPUT:
			item.setText(result.name());
			item.setImage(this.resources.getImage(Images.class, Images.IMAGE_RESULT_ENCODED_INPUT16));
			break;
		}
	}

	private String shortInputName(String name) {
		int shortNameIndex = name.lastIndexOf('/');

		if (shortNameIndex < 0) {
			shortNameIndex = name.lastIndexOf('\\');
		}
		return (shortNameIndex >= 0 && shortNameIndex + 1 < name.length() ? name.substring(shortNameIndex + 1) : name);
	}

	private Image inputImage(String shortInputName) {
		int extensionIndex = shortInputName.lastIndexOf('.');
		Image inputImage = null;

		if (0 < extensionIndex) {
			String extension = shortInputName.substring(extensionIndex);

			inputImage = extensionImage(extension);
		}
		return (inputImage != null ? inputImage : this.resources.getImage(Images.class, Images.IMAGE_RESULT_INPUT16));
	}

	private Image extensionImage(String extension) {
		Program program = Program.findProgram(extension);
		Image inputImage = null;

		if (program != null) {
			inputImage = this.resources.getImage(program, ProgramImageDataProvider::createImage);
		}
		return (inputImage != null ? inputImage : this.resources.getImage(Images.class, Images.IMAGE_RESULT_INPUT16));
	}

	private void expandAndSelectResultPath(FileScannerResult[] resultPath) {
		int resultPathIndex = 0;
		int resultPathTailIndex = resultPath.length - 1;

		while (resultPathIndex < resultPathTailIndex) {
			FileScannerResult result = resultPath[resultPathIndex];
			TreeItem resultItem = Objects.requireNonNull(result.getData(TreeItem.class));

			resultItem.setExpanded(true);

			FileScannerResult[] resultChildren = result.children();

			for (int resultChildrenIndex = 0; resultChildrenIndex < resultChildren.length; resultChildrenIndex++) {
				FileScannerResult resultChild = resultChildren[resultChildrenIndex];
				TreeItem resultChildItem = resultChild.getData(TreeItem.class);

				if (resultChildItem == null) {
					resultChildItem = resultItem.getItem(resultChildrenIndex);
					initializeResultTreeItem(resultChildItem, resultChild, false);
				}
			}
			resultPathIndex++;
		}
		this.resultSelection.set(resultPath[resultPathTailIndex]);
	}

	private void onResultTreeItemSelected(SelectionEvent event) {
		try {
			FileScannerResult result = null;

			if (event.item != null) {
				TreeItem resultItem = Check.isInstanceOf(event.item, TreeItem.class);

				result = Check.isInstanceOf(resultItem.getData(), FileScannerResult.class);
			}
			this.resultSelection.set(result);
		} catch (Exception e) {
			Exceptions.warn(e);
		}
	}

	private void onDisposed() {
		LOG.info("Disposing Main UI...");

		this.executor.shutdownNow();
		UserPreferences.get().removeConsumer(this.configConsumer);
		this.controllerHolder.get().close();
		this.resultRenderServerHolder.get().stop();
		this.resources.disposeAll();

		LOG.info("Main UI disposed");
	}

	private void onOpenSelected() {
		FileDialog openFileDialog = FileDialogBuilder.open(root()).withFilter(MainI18N.i18nTextFileOpenFilter()).get();
		String file = openFileDialog.open();

		if (file != null) {
			openFile(file);
		}
	}

	private void onPreferencesSelected() {
		try {
			PreferencesDialog preferencesDialog = new PreferencesDialog(root());

			preferencesDialog.open();
		} catch (Exception e) {
			unexpectedException(e);
		}
	}

	private void onPrintObjectSelected() {
		this.resultViewHolder.get().execute("javascript:window.print();");
	}

	private void onExportObjectSelected() {
		try {
			FileScannerResult result = this.resultSelection.get();

			if (result != null) {
				ExportDialog exportDialog = new ExportDialog(get());
				ExportOptions exportOptions = exportDialog.open(result);

				if (exportOptions != null) {
					ProgressUI progress = new ProgressUI(new Shell(root(), ProgressUI.STYLE));

					progress.open();
					progress.run(this.executor.submit(new ExportTask(progress, result, exportOptions))).get();
				}
			}
		} catch (CancellationException e) {
			Exceptions.ignore(e);
		} catch (ExecutionException e) {
			unexpectedException(e.getCause());
		} catch (Exception e) {
			unexpectedException(e);
		}
	}

	private void onCopyObjectToolSelected(SelectionEvent event) {
		if (event.detail == SWT.ARROW) {
			ToolItem toolItem = Check.isInstanceOf(event.widget, ToolItem.class);
			Rectangle toolItemBounds = toolItem.getBounds();
			Menu menu = this.copyObjectToolHolder.get();
			Point menuLocation = toolItem.getParent().toDisplay(toolItemBounds.x,
					toolItemBounds.y + toolItemBounds.height);

			menu.setLocation(menuLocation);
			menu.setVisible(true);
		} else {
			copyObject(ClipboardTransferHandler.defaultHandler(this.resultRenderServerHolder.get()));
		}
	}

	private void onCopyObjectSelected(SelectionEvent event) {
		MenuItem menuItem = Check.isInstanceOf(event.widget, MenuItem.class);
		Object menuItemData = menuItem.getData();

		if (menuItemData != null) {
			FileScannerResultExportHandler exportHandler = Check.isInstanceOf(menuItemData,
					FileScannerResultExportHandler.class);

			copyObject(ClipboardTransferHandler.exportHandler(exportHandler));
		} else {
			copyObject(ClipboardTransferHandler.defaultHandler(this.resultRenderServerHolder.get()));
		}
	}

	private void copyObject(ClipboardTransfer transfer) {
		Clipboard clipboard = null;

		try {
			FileScannerResult result = this.resultSelection.get();

			if (result != null) {
				ProgressUI progress = new ProgressUI(new Shell(root(), ProgressUI.STYLE));
				ClipboardTransferHandler handler = transfer.getInstance(progress);

				progress.open();
				progress.run(this.executor.submit(new ClipboardTransferTask(progress, result, handler))).get();
				clipboard = new Clipboard(root().getDisplay());
				handler.transfer(clipboard);
			}
		} catch (CancellationException e) {
			Exceptions.ignore(e);
		} catch (ExecutionException e) {
			unexpectedException(e.getCause());
		} catch (Exception e) {
			unexpectedException(e);
		} finally {
			if (clipboard != null) {
				clipboard.dispose();
			}
		}
	}

	private void onGotoNextSelected() {
		try {
			FileScannerResult from = null;

			if (this.searchState != SearchState.WRAP_NEXT) {
				from = this.resultSelection.get();
			}

			String query = getSearchQuery();
			FileScannerResult[] searchResult = this.controllerHolder.get().searchNext(from, query);

			if (searchResult != null) {
				expandAndSelectResultPath(searchResult);
				this.searchState = SearchState.DEFAULT;
			} else {
				this.searchState = SearchState.WRAP_NEXT;
				Notification.information(root()).withText(MainI18N.i18nTextNoSearchResult())
						.withMessage(MainI18N.i18nMessageNoSearchResult()).open();
			}
		} catch (Exception e) {
			unexpectedException(e);
		}
	}

	private void onGotoPreviousSelected() {
		try {
			FileScannerResult from = null;

			if (this.searchState != SearchState.WRAP_PREVIOUS) {
				from = this.resultSelection.get();
			}

			String query = getSearchQuery();
			FileScannerResult[] searchResult = this.controllerHolder.get().searchPrevious(from, query);

			if (searchResult != null) {
				expandAndSelectResultPath(searchResult);
				this.searchState = SearchState.DEFAULT;
			} else {
				this.searchState = SearchState.WRAP_PREVIOUS;
				Notification.information(root()).withText(MainI18N.i18nTextNoSearchResult())
						.withMessage(MainI18N.i18nMessageNoSearchResult()).open();
			}
		} catch (Exception e) {
			unexpectedException(e);
		}
	}

	private String getSearchQuery() {
		String query = Strings.safeTrim(this.searchQueryHolder.get().getText());

		return (Strings.notEmpty(query) ? query : "*");
	}

	private void onGotoEndSelected() {
		try {
			Hex inputView = this.inputViewHolder.get();
			FileScannerResult result = this.resultSelection.get();

			if (result != null) {
				inputView.scrollTo(result.end());
			}
		} catch (Exception e) {
			unexpectedException(e);
		}
	}

	private void onGotoStartSelected() {
		try {
			Hex inputView = this.inputViewHolder.get();
			FileScannerResult result = this.resultSelection.get();

			if (result != null) {
				inputView.scrollTo(result.start());
			}
		} catch (Exception e) {
			unexpectedException(e);
		}
	}

	private void onLogSelected() {
		try {
			LogViewDialog log = LogViewDialog.build(root(), Log.root());

			log.withLogo(LogLevel.LEVEL_NOTICE,
					Objects.requireNonNull(Images.class.getResource(Images.IMAGE_LOG_NOTICE16)));
			log.withLogo(LogLevel.LEVEL_ERROR,
					Objects.requireNonNull(Images.class.getResource(Images.IMAGE_LOG_ERROR16)));
			log.withLogo(LogLevel.LEVEL_WARNING,
					Objects.requireNonNull(Images.class.getResource(Images.IMAGE_LOG_WARNING16)));
			log.withLogo(LogLevel.LEVEL_INFO,
					Objects.requireNonNull(Images.class.getResource(Images.IMAGE_LOG_INFO16)));
			log.withLogo(LogLevel.LEVEL_DEBUG,
					Objects.requireNonNull(Images.class.getResource(Images.IMAGE_LOG_DEBUG16)));
			log.withLogo(LogLevel.LEVEL_TRACE,
					Objects.requireNonNull(Images.class.getResource(Images.IMAGE_LOG_TRACE16)));
			log.open();
		} catch (Exception e) {
			unexpectedException(e);
		}
	}

	private static final String[] RESOURCES_COPYRIGHT = { "Copyright1.txt", "Copyright2.txt" };

	private void onAboutSelected() {
		try {
			URL logoUrl = Images.class.getResource(Images.IMAGE_FSLOGO48);
			AboutInfoDialog aboutInfo = AboutInfoDialog.build(root(), new ModuleManifestInfos()).withLogo(logoUrl);

			for (String copyrightResource : RESOURCES_COPYRIGHT) {
				URL copyrightUrl = MainUI.class.getResource(copyrightResource);

				aboutInfo.withCopyright(copyrightUrl);
			}
			aboutInfo.open();
		} catch (Exception e) {
			unexpectedException(e);
		}
	}

	private void onResultSelectionChanged(@Nullable FileScannerResult newResult,
			@SuppressWarnings({ "unused", "squid:S1172" }) @Nullable FileScannerResult oldResult) {
		if (newResult != null) {
			TreeItem resultItem = newResult.getData(TreeItem.class);

			this.resultTreeHolder.get().select(resultItem);
			this.resultTreeHolder.get().showItem(resultItem);
			this.inputViewHolder.get().setResult(newResult);

			HtmlResultDocument resultDocument = this.resultRenderServerHolder.get().createResultDocument(newResult,
					false);

			this.resultViewHolder.get().setUrl(resultDocument.documentUrl());
			this.resultSelectionCommands.setEnabled(true);
			resetCopyObjectMenus(newResult);
		} else {
			this.resultViewHolder.get().setText(getDefaultResultView());
			this.resultSelectionCommands.setEnabled(false);
			clearCopyObjectMenus();
		}
		this.searchState = SearchState.DEFAULT;
	}

	private String getDefaultResultView() {
		de.carne.filescanner.engine.ModuleManifestInfos engineInfos = new de.carne.filescanner.engine.ModuleManifestInfos();

		return MainI18N.i18nTextDefaultResultViewHtml(Strings.encodeHtml(engineInfos.name()),
				Strings.encodeHtml(engineInfos.version()), Strings.encodeHtml(engineInfos.build()));
	}

	private void clearCopyObjectMenus() {
		clearCopyObjectMenu(this.copyObjectMenuHolder);
		clearCopyObjectMenu(this.copyObjectToolHolder);
		clearCopyObjectMenu(this.contextMenuCopyObjectMenuHolder);
	}

	private void clearCopyObjectMenu(Late<Menu> menuHolder) {
		MenuBuilder copyObject = new MenuBuilder(menuHolder);

		copyObject.removeItems();
	}

	private void resetCopyObjectMenus(FileScannerResult result) {
		FileScannerResultExportHandler[] exportHandlers = result.exportHandlers();

		resetCopyObjectMenu(this.copyObjectMenuHolder, exportHandlers);
		resetCopyObjectMenu(this.copyObjectToolHolder, exportHandlers);
		resetCopyObjectMenu(this.contextMenuCopyObjectMenuHolder, exportHandlers);
	}

	private void resetCopyObjectMenu(Late<Menu> menuHolder, FileScannerResultExportHandler[] exportHandlers) {
		MenuBuilder copyObject = new MenuBuilder(menuHolder);

		copyObject.removeItems();
		copyObject.addItem(SWT.PUSH);
		copyObject.withText(MainI18N.i18nMenuEditCopyDefault());
		copyObject.withImage(this.resources.getImage(Images.class, Images.IMAGE_COPY_DEFAULT16));
		copyObject.onSelected(this::onCopyObjectSelected);

		boolean firstExportHandler = true;

		for (FileScannerResultExportHandler exportHandler : exportHandlers) {
			if (ClipboardTransferHandler.isTransferable(exportHandler.transferType())) {
				if (firstExportHandler) {
					copyObject.addItem(SWT.SEPARATOR);
					firstExportHandler = false;
				}
				copyObject.addItem(SWT.PUSH);
				copyObject.withText(
						String.format("%1$s (%2$s)", exportHandler.name(), exportHandler.transferType().mimeType()));
				copyObject.withImage(extensionImage(exportHandler.defaultFileExtension()));
				copyObject.onSelected(this::onCopyObjectSelected);
				copyObject.currentItem().setData(exportHandler);
			}
		}
	}

	@Override
	public void open() throws ResourceException {
		LOG.info("Opening Main UI...");

		this.resultRenderServerHolder.set(new HtmlRenderServer(UserPreferences.get()));

		MainController controller = this.controllerHolder.set(new MainController(this));
		Shell root = buildRoot(controller);

		UserPreferences preferences = UserPreferences.get();

		preferences.addConsumer(this.configConsumer);
		this.configConsumer.accept(preferences);
		this.resultTreeHolder.get().setFocus();
		root.layout(true);
		resetSession(false);
		root.open();
	}

	private void applyConfig(Config config) {
		Font inputViewFont = this.resources.getFont(config.getInputViewFont());

		this.inputViewHolder.get().setFont(inputViewFont);
		this.resultRenderServerHolder.get().applyConfig(config);

		TreeItem[] resultTreeSelection = this.resultTreeHolder.get().getSelection();

		if (resultTreeSelection.length > 0) {
			FileScannerResult result = Check.isInstanceOf(resultTreeSelection[0].getData(), FileScannerResult.class);

			this.inputViewHolder.get().setResult(result);

			HtmlResultDocument resultDocument = this.resultRenderServerHolder.get().createResultDocument(result, false);

			this.resultViewHolder.get().setUrl(resultDocument.documentUrl());
		}
	}

	@SuppressWarnings("squid:S1215")
	private void runGc() {
		if (LOG.isDebugLoggable()) {
			String before = Debug.formatUsedMemory();

			Runtime.getRuntime().gc();

			String after = Debug.formatUsedMemory();

			LOG.debug("GC: {0} -> {1}", before, after);
		} else {
			Runtime.getRuntime().gc();
		}
		this.runtimeHeapHolder.get().redraw();
	}

	private Shell buildRoot(MainController controller) {
		ShellBuilder rootBuilder = new ShellBuilder(root());
		CoolBarBuilder commands = buildCommandBar(rootBuilder);
		CompositeBuilder<SashForm> resultBuilder = rootBuilder.addCompositeChild(SashForm.class, SWT.HORIZONTAL);
		ControlBuilder<Tree> resultTree = resultBuilder.addControlChild(Tree.class,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.VIRTUAL);
		CompositeBuilder<SashForm> resultViewBuilder = resultBuilder.addCompositeChild(SashForm.class, SWT.VERTICAL);
		CompositeBuilder<Composite> resultViewBorder = resultViewBuilder.addCompositeChild(SWT.BORDER);
		ControlBuilder<Browser> resultView = resultViewBorder.addControlChild(Browser.class, SWT.NONE);
		ControlBuilder<Hex> inputView = resultViewBuilder.addControlChild(Hex.class, SWT.BORDER);
		CoolBarBuilder status = buildStatusBar(rootBuilder, controller);

		rootBuilder.withText(MainI18N.i18nTitle())
				.withImages(this.resources.getImages(Images.class, Images.IMAGES_FSLOGO)).onDisposed(this::onDisposed);
		buildMenuBar(rootBuilder);
		buildContextMenu(resultTree.get());
		resultTree.onEvent(SWT.SetData, this::onSetResultTreeItemData);
		resultTree.onSelected(this::onResultTreeItemSelected);
		resultView.onEvent(SWT.MenuDetect, event -> event.doit = false);

		FillLayoutBuilder.layout().apply(resultViewBorder);
		GridLayoutBuilder.layout().apply(rootBuilder);
		GridLayoutBuilder.data(GridData.FILL_HORIZONTAL).apply(commands);
		GridLayoutBuilder.data(GridData.FILL_BOTH).apply(resultBuilder);
		GridLayoutBuilder.data(GridData.FILL_HORIZONTAL).apply(status);

		resultBuilder.get().setWeights(new int[] { 40, 60 });
		this.resultTreeHolder.set(resultTree.get());
		this.resultViewHolder.set(resultView.get());
		this.inputViewHolder.set(inputView.get());

		DropTargetBuilder.fileTransfer(rootBuilder.get(), DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK)
				.onFileDrop(this::openDroppedFile);

		this.resultSelection.addChangedListener(this::onResultSelectionChanged);

		return rootBuilder.get();
	}

	private void buildMenuBar(ShellBuilder rootBuilder) {
		Display display = rootBuilder.get().getDisplay();
		MenuBuilder menu = MenuBuilder.menuBar(rootBuilder);

		menu.addItem(SWT.CASCADE).withText(MainI18N.i18nMenuFile());
		menu.beginMenu();
		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuFileOpen());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_OPEN_FILE16));
		menu.onSelected(this::onOpenSelected);
		menu.addItem(SWT.SEPARATOR);
		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuFilePrint());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_PRINT_OBJECT16));
		menu.onSelected(this::onPrintObjectSelected);
		this.resultSelectionCommands.add(menu.currentItem());
		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuFileExport());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_EXPORT_OBJECT16));
		menu.onSelected(this::onExportObjectSelected);
		this.resultSelectionCommands.add(menu.currentItem());
		if (PlatformIntegration.isCocoa()) {
			PlatformIntegration.cocoaAddPreferencesSelectionAction(display, this::onPreferencesSelected);
			PlatformIntegration.cocoaAddQuitSelectionAction(display, this::close);
		} else {
			menu.addItem(SWT.SEPARATOR);
			menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuFilePreferences());
			menu.onSelected(this::onPreferencesSelected);
			menu.addItem(SWT.SEPARATOR);
			menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuFileQuit());
			menu.onSelected(this::close);
		}
		menu.endMenu();
		menu.addItem(SWT.CASCADE).withText(MainI18N.i18nMenuEdit());
		menu.beginMenu();
		menu.addItem(SWT.CASCADE).withText(MainI18N.i18nMenuEditCopy());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_COPY_OBJECT16));
		this.resultSelectionCommands.add(menu.currentItem());
		menu.beginMenu();
		this.copyObjectMenuHolder.set(menu.get());
		menu.endMenu();
		menu.endMenu();
		menu.addItem(SWT.CASCADE).withText(MainI18N.i18nMenuGoto());
		menu.beginMenu();
		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuGotoNext());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_GOTO_NEXT16));
		menu.onSelected(this::onGotoNextSelected);
		this.resultSelectionCommands.add(menu.currentItem());
		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuGotoPrevious());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_GOTO_PREVIOUS16));
		menu.onSelected(this::onGotoPreviousSelected);
		this.resultSelectionCommands.add(menu.currentItem());
		menu.addItem(SWT.SEPARATOR);
		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuGotoStart());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_GOTO_START16));
		menu.onSelected(this::onGotoStartSelected);
		this.resultSelectionCommands.add(menu.currentItem());
		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuGotoEnd());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_GOTO_END16));
		menu.onSelected(this::onGotoEndSelected);
		this.resultSelectionCommands.add(menu.currentItem());
		menu.endMenu();
		menu.addItem(SWT.CASCADE).withText(MainI18N.i18nMenuHelp());
		menu.beginMenu();
		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuHelpLog());
		menu.onSelected(this::onLogSelected);
		if (PlatformIntegration.isCocoa()) {
			PlatformIntegration.cocoaAddAboutSelectionAction(display, this::onAboutSelected);
		} else {
			menu.addItem(SWT.SEPARATOR);
			menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuHelpAbout());
			menu.onSelected(this::onAboutSelected);
		}
		menu.endMenu();
	}

	private void buildContextMenu(Tree resultTree) {
		MenuBuilder menu = MenuBuilder.popupMenu(resultTree);

		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuFilePrint());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_PRINT_OBJECT16));
		menu.onSelected(this::onPrintObjectSelected);
		this.resultSelectionCommands.add(menu.currentItem());
		menu.addItem(SWT.PUSH).withText(MainI18N.i18nMenuFileExport());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_EXPORT_OBJECT16));
		menu.onSelected(this::onExportObjectSelected);
		this.resultSelectionCommands.add(menu.currentItem());
		menu.addItem(SWT.CASCADE).withText(MainI18N.i18nMenuEditCopy());
		menu.withImage(this.resources.getImage(Images.class, Images.IMAGE_COPY_OBJECT16));
		this.resultSelectionCommands.add(menu.currentItem());
		menu.beginMenu();
		this.contextMenuCopyObjectMenuHolder.set(menu.get());
		menu.endMenu();
		resultTree.setMenu(menu.get());
	}

	private CoolBarBuilder buildCommandBar(ShellBuilder rootBuilder) {
		CoolBarBuilder commands = CoolBarBuilder.horizontal(rootBuilder, SWT.FLAT);
		ToolBarBuilder fileAndEditTools = ToolBarBuilder.horizontal(commands, SWT.FLAT);
		CompositeBuilder<Composite> queryInput = commands.addCompositeChild(SWT.NONE);
		ControlBuilder<Text> queryText = queryInput.addControlChild(Text.class, SWT.SEARCH | SWT.ICON_SEARCH);
		ToolBarBuilder gotoTools = ToolBarBuilder.horizontal(queryInput, SWT.FLAT);

		// File & edit tools
		fileAndEditTools.addItem(SWT.PUSH);
		fileAndEditTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_OPEN_FILE16))
				.withToolTipText(MainI18N.i18nTooltipFileOpen());
		fileAndEditTools.onSelected(this::onOpenSelected);
		fileAndEditTools.addItem(SWT.SEPARATOR);
		fileAndEditTools.addItem(SWT.PUSH);
		fileAndEditTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_PRINT_OBJECT16))
				.withDisabledImage(this.resources.getImage(Images.class, Images.IMAGE_PRINT_OBJECT_DISABLED16))
				.withToolTipText(MainI18N.i18nTooltipFilePrint());
		fileAndEditTools.onSelected(this::onPrintObjectSelected);
		this.resultSelectionCommands.add(fileAndEditTools.currentItem());
		fileAndEditTools.addItem(SWT.PUSH);
		fileAndEditTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_EXPORT_OBJECT16))
				.withDisabledImage(this.resources.getImage(Images.class, Images.IMAGE_EXPORT_OBJECT_DISABLED16))
				.withToolTipText(MainI18N.i18nTooltipFileExport());
		fileAndEditTools.onSelected(this::onExportObjectSelected);
		this.resultSelectionCommands.add(fileAndEditTools.currentItem());
		fileAndEditTools.addItem(SWT.SEPARATOR);
		fileAndEditTools.addItem(SWT.DROP_DOWN);
		fileAndEditTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_COPY_OBJECT16))
				.withDisabledImage(this.resources.getImage(Images.class, Images.IMAGE_COPY_OBJECT_DISABLED16))
				.withToolTipText(MainI18N.i18nTooltipEditCopy());
		fileAndEditTools.onSelected(this::onCopyObjectToolSelected);
		this.copyObjectToolHolder.set(new Menu(fileAndEditTools.get()));
		this.resultSelectionCommands.add(fileAndEditTools.currentItem());
		commands.addItem(SWT.NONE).withControl(fileAndEditTools);
		// Search tools
		queryText.get().setToolTipText(MainI18N.i18nTooltipQueryInput());
		queryText.onSelected(this::onGotoNextSelected);
		this.resultSelectionCommands.add(queryText.get());
		gotoTools.addItem(SWT.PUSH);
		gotoTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_GOTO_NEXT16))
				.withToolTipText(MainI18N.i18nTooltipGotoNext());
		gotoTools.onSelected(this::onGotoNextSelected);
		this.resultSelectionCommands.add(gotoTools.currentItem());
		gotoTools.addItem(SWT.PUSH);
		gotoTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_GOTO_PREVIOUS16))
				.withToolTipText(MainI18N.i18nTooltipGotoPrevious());
		gotoTools.onSelected(this::onGotoPreviousSelected);
		this.resultSelectionCommands.add(gotoTools.currentItem());
		gotoTools.addItem(SWT.SEPARATOR);
		gotoTools.addItem(SWT.PUSH);
		gotoTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_GOTO_END16))
				.withToolTipText(MainI18N.i18nTooltipGotoEnd());
		gotoTools.onSelected(this::onGotoEndSelected);
		this.resultSelectionCommands.add(gotoTools.currentItem());
		gotoTools.addItem(SWT.PUSH);
		gotoTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_GOTO_START16))
				.withToolTipText(MainI18N.i18nTooltipGotoStart());
		gotoTools.onSelected(this::onGotoStartSelected);
		this.resultSelectionCommands.add(gotoTools.currentItem());
		GridLayoutBuilder.layout(2).margin(2, 2).apply(queryInput);
		GridLayoutBuilder.data().align(SWT.FILL, SWT.CENTER).grab(true, false).apply(queryText);
		GridLayoutBuilder.data().apply(gotoTools);
		commands.addItem(SWT.NONE).withControl(queryInput);

		commands.lock(true).pack();
		this.searchQueryHolder.set(queryText.get());
		return commands;
	}

	private CoolBarBuilder buildStatusBar(ShellBuilder rootBuilder, MainController controller) {
		CoolBarBuilder status = CoolBarBuilder.horizontal(rootBuilder, SWT.FLAT);
		CompositeBuilder<Composite> session = status.addCompositeChild(SWT.NONE);
		ToolBarBuilder sessionTools = ToolBarBuilder.horizontal(session, SWT.FLAT);
		ControlBuilder<ProgressBar> sessionProgress = session.addControlChild(ProgressBar.class,
				SWT.HORIZONTAL | SWT.SMOOTH);
		LabelBuilder sessionStatus = session.addLabelChild(SWT.HORIZONTAL);
		CompositeBuilder<Composite> runtime = status.addCompositeChild(SWT.NONE);
		ControlBuilder<HeapInfo> runtimeHeap = runtime.addControlChild(HeapInfo.class, SWT.BORDER);
		ToolBarBuilder runtimeTools = ToolBarBuilder.horizontal(runtime, SWT.FLAT);

		sessionTools.addItem(SWT.PUSH);
		sessionTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_STOP16))
				.withDisabledImage(this.resources.getImage(Images.class, Images.IMAGE_STOP_DISABLED16))
				.withToolTipText(MainI18N.i18nTooltipStopScan());
		sessionTools.onSelected(controller::stopScan);

		runtimeHeap.get().setTimer(500);

		runtimeTools.addItem(SWT.PUSH);
		runtimeTools.withImage(this.resources.getImage(Images.class, Images.IMAGE_TRASH16))
				.withToolTipText(MainI18N.i18nTooltipRunGc());
		runtimeTools.onSelected(this::runGc);

		GridLayoutBuilder.layout(3).margin(0, 0).apply(session);
		GridLayoutBuilder.data().apply(sessionTools);
		GridLayoutBuilder.data().apply(sessionProgress);
		GridLayoutBuilder.data(GridData.FILL_HORIZONTAL).apply(sessionStatus);

		GridLayoutBuilder.layout(2).margin(0, 0).apply(runtime);
		GridLayoutBuilder.data().apply(runtimeHeap);
		GridLayoutBuilder.data().apply(runtimeTools);

		Rectangle displayBounds = rootBuilder.get().getDisplay().getBounds();

		status.addItem(SWT.NONE);
		status.withControl(session);
		status.currentItem().setPreferredSize(displayBounds.width, status.currentItem().getPreferredSize().y);
		status.addItem(SWT.NONE);
		status.withControl(runtime);

		this.sessionProgressHolder.set(sessionProgress.get());
		this.sessionCommands.add(sessionTools.currentItem());
		this.sessionStatusHolder.set(sessionStatus.get());
		this.runtimeHeapHolder.set(runtimeHeap.get());

		status.lock(true).pack();
		return status;
	}

}
