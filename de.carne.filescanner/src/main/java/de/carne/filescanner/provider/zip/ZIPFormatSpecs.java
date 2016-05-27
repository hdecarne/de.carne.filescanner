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
package de.carne.filescanner.provider.zip;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.carne.filescanner.core.format.spec.FixedStringAttribute;
import de.carne.filescanner.core.format.spec.EncodedFormatSpec;
import de.carne.filescanner.core.format.spec.StructFormatSpec;
import de.carne.filescanner.core.format.spec.U16Attribute;
import de.carne.filescanner.core.format.spec.U16Attributes;
import de.carne.filescanner.core.format.spec.U16FlagRenderer;
import de.carne.filescanner.core.format.spec.U16SymbolRenderer;
import de.carne.filescanner.core.format.spec.U32Attribute;
import de.carne.filescanner.core.format.spec.U32Attributes;
import de.carne.filescanner.core.format.spec.VarArrayFormatSpec;
import de.carne.filescanner.core.input.DecodeParams;

/**
 * ZIP format structures.
 */
class ZIPFormatSpecs {

	public static final String NAME_ZIP = "ZIP archive";

	public static final String NAME_ZIP_ENTRY = "ZIP entry [{0}]";

	public static final String NAME_ZIP_LFH = "Local file header";

	public static final ZIPVersionRenderer ZIP_VERSION_SYMBOLS = new ZIPVersionRenderer();

	static {
		ZIP_VERSION_SYMBOLS.addSymbol((short) (0 << 8), "MS-DOS and OS/2 (FAT / VFAT / FAT32 file systems)");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (1 << 8), "Amiga");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (2 << 8), "OpenVMS");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (3 << 8), "UNIX");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (4 << 8), "VM/CMS");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (5 << 8), "Atari ST");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (6 << 8), "OS/2 H.P.F.S.");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (7 << 8), "Macintosh");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (8 << 8), "Z-System");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (9 << 8), "CP/M");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (10 << 8), "Windows NTFS");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (11 << 8), "MVS (OS/390 - Z/OS)");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (12 << 8), "VSE");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (13 << 8), "Acorn Risc");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (14 << 8), "VFAT");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (15 << 8), "alternate MVS");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (16 << 8), "BeOS");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (17 << 8), "Tandem");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (18 << 8), "OS/400");
		ZIP_VERSION_SYMBOLS.addSymbol((short) (19 << 8), "OS/X (Darwin)");
	}

	public static final U16FlagRenderer ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS = new U16FlagRenderer();

	static {
		ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS.addFlagSymbol((short) 0x2000, "Central directory encrypted");
		ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS.addFlagSymbol((short) 0x0800, "UTF-8 strings");
		ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS.addFlagSymbol((short) 0x0040, "Strong encryption");
		ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS.addFlagSymbol((short) 0x0020, "Compressed patched data");
		ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS.addFlagSymbol((short) 0x0010, "Enhanced deflating");
		ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS.addFlagSymbol((short) 0x0008,
				"crc-32, compressed size and uncompressed size are in data descriptor");
		ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS.addFlagSymbol((short) 0x0006, "Decoder flag");
		ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS.addFlagSymbol((short) 0x0001, "Encryption");
	}

	public static final U16SymbolRenderer ZIP_COMPRESSION_METHOD_SYMBOLS = new U16SymbolRenderer();

	static {
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 0, "Stored (no compression)");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 1, "Shrunk");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 2, "Reduced with compression factor 1");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 3, "Reduced with compression factor 2");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 4, "Reduced with compression factor 3");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 5, "Reduced with compression factor 4");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 6, "Imploded");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 7, "Tokenizing compression algorithm");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 8, "Deflated");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 9, "Enhanced Deflating using Deflate64(tm)");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 10,
				"PKWARE Data Compression Library Imploding (old IBM TERSE)");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 12, "BZIP2 algorithm");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 14, "LZMA (EFS)");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 18, "IBM TERSE (new)");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 19, "IBM LZ77 z Architecture (PFS)");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 97, "WavPack compressed data");
		ZIP_COMPRESSION_METHOD_SYMBOLS.addSymbol((short) 98, "PPMd version I, Rev 1");
	}

	public static final U16Attribute LFH_COMPRESSION_METHOD = new U16Attribute("compression method");

	public static final U32Attribute LFH_COMPRESSED_SIZE = new U32Attribute("compressed size");

	public static final U16Attribute LFH_FILE_NAME_LENGTH = new U16Attribute("file name length");

	public static final U16Attribute LFH_EXTRA_FIELD_LENGTH = new U16Attribute("extra field length");

	public static final FixedStringAttribute LFH_FILE_NAME = new FixedStringAttribute("file name", StandardCharsets.UTF_8,
			LFH_FILE_NAME_LENGTH);

	public static final StructFormatSpec ZIP_LFH;

	static {
		StructFormatSpec lfh = new StructFormatSpec();

		lfh.append(new U32Attribute("local file header signature").setFinalValue(0x04034b50));
		lfh.append(new U16Attribute("version needed to extract").addExtraRenderer(ZIP_VERSION_SYMBOLS));
		lfh.append(new U16Attribute("general purpose bit flag").addExtraRenderer(ZIP_GENERAL_PURPOSE_FLAG_SYMBOLS));
		lfh.append(LFH_COMPRESSION_METHOD.bind().addExtraRenderer(ZIP_COMPRESSION_METHOD_SYMBOLS));
		lfh.append(new U16Attribute("last mod file time").addExtraRenderer(U16Attributes.DOS_TIME_COMMENT));
		lfh.append(new U16Attribute("last mod file date").addExtraRenderer(U16Attributes.DOS_DATE_COMMENT));
		lfh.append(new U32Attribute("crc-32"));
		lfh.append(LFH_COMPRESSED_SIZE.bind().addExtraRenderer(U32Attributes.BYTE_COUNT_COMMENT));
		lfh.append(new U32Attribute("uncompressed size").addExtraRenderer(U32Attributes.BYTE_COUNT_COMMENT));
		lfh.append(LFH_FILE_NAME_LENGTH.bind().addExtraRenderer(U16Attributes.BYTE_COUNT_COMMENT));
		lfh.append(LFH_EXTRA_FIELD_LENGTH.bind().addExtraRenderer(U16Attributes.BYTE_COUNT_COMMENT));
		lfh.append(LFH_FILE_NAME.bind());
		lfh.setResult(NAME_ZIP_LFH);
		ZIP_LFH = lfh;
	}

	public static final StructFormatSpec ZIP_ENTRY;

	static {
		StructFormatSpec zipEntry = new StructFormatSpec();

		zipEntry.append(ZIP_LFH);
		zipEntry.append(new EncodedFormatSpec(() -> getInputDecodeParams()));
		zipEntry.declareAttribute(LFH_COMPRESSION_METHOD).declareAttribute(LFH_COMPRESSED_SIZE)
				.declareAttribute(LFH_FILE_NAME);
		zipEntry.setResult(NAME_ZIP_ENTRY, LFH_FILE_NAME);
		ZIP_ENTRY = zipEntry;
	}

	public static final StructFormatSpec ZIP;

	static {
		StructFormatSpec zip = new StructFormatSpec();

		zip.append(new VarArrayFormatSpec(ZIP_ENTRY, true));
		zip.setResult(NAME_ZIP);
		ZIP = zip;
	}

	private static DecodeParams getInputDecodeParams() {
		Short compressionMethod = LFH_COMPRESSION_METHOD.get();
		Integer compressedSize = LFH_COMPRESSED_SIZE.get();
		String fileName = LFH_FILE_NAME.get();
		DecodeParams decodeParams = null;

		if (compressionMethod != null && compressedSize != null && fileName != null) {
			Path decodedPath = Paths.get(fileName);

			switch (compressionMethod.shortValue()) {
			case 0:
				decodeParams = DecodeParams.newNullDecoderFactory(compressedSize.longValue(), decodedPath);
			case 8:
				decodeParams = DecodeParams.newDeflateDecoderFactory(compressedSize.longValue(), decodedPath);
				break;
			}
		}
		return decodeParams;
	}

}
