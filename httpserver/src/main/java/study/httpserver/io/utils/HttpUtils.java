package study.httpserver.io.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class HttpUtils {
	private static final String CONTENT_LENGTH = "content-length:";

	public static String normilizeHeaderName(String name) {
		StringBuilder headerName = new StringBuilder(name.trim());
		for (int i = 0; i < headerName.length(); i++) {
			char nameChar = name.charAt(i);
			if (i == 0) {
				toUpperCase(i, nameChar, headerName);
			} else if (nameChar == '-' && i < headerName.length() - 1) {
				toUpperCase(i + 1, headerName.charAt(i + 1), headerName);
				i++;
			} else {
				if (Character.isUpperCase(nameChar)) {
					headerName.setCharAt(i, Character.toLowerCase(nameChar));
				}
			}
		}
		return headerName.toString();
	}

	private static void toUpperCase(int index, char nameChar, StringBuilder headerName) {
		if (Character.isLowerCase(nameChar)) {
			headerName.setCharAt(index, Character.toUpperCase(nameChar));
		}

	}

	public static String readStartingLineAndHeaders(InputStream inputStream) throws IOException {
		ByteArray byteArray = new ByteArray();
		while (true) {
			int read = inputStream.read();
			if (read == -1) {
				throw new EOFException("InputStream closed");
			}
			byteArray.add((byte) read);
			if (byteArray.isEmptyLine()) {
				break;
			}
		}
		return new String(byteArray.toArray(), StandardCharsets.UTF_8);
	}

	public static String readMessageBody(InputStream inputStream, int contentLength) throws IOException {
		StringBuilder stringMessege = new StringBuilder();
		while (contentLength > 0) {
			byte[] messageBody = new byte[contentLength];
			int read = inputStream.read(messageBody);
			stringMessege.append(new String(messageBody, 0, read, StandardCharsets.UTF_8));
			contentLength -= read;
		}
		return stringMessege.toString();
	}

	public static int getContentLenght(String startingLineAndHeaders, int indexContentLength) {
		int startCutIndex = indexContentLength + CONTENT_LENGTH.length();
		int endCutIndex = startingLineAndHeaders.indexOf("\r\n", startCutIndex);
		if (endCutIndex == -1) {
			endCutIndex = startingLineAndHeaders.length();
		}
		return Integer.parseInt(startingLineAndHeaders.substring(startCutIndex, endCutIndex).trim());
	}

	public static int getIndexOfContentBody(String startingLineAndHeaders) {
		return startingLineAndHeaders.toLowerCase().indexOf(CONTENT_LENGTH);
	}

	private static class ByteArray {
		private byte[] array = new byte[1024];
		private int size;

		public void add(byte value) {
			if (size == array.length) {
				byte[] temp = array;
				array = new byte[array.length * 2];
				System.arraycopy(temp, 0, array, 0, size);
			}
			array[size++] = value;
		}

		private byte[] toArray() {
			if (size > 4) {
				return Arrays.copyOf(array, size - 4);
			} else {
				throw new IllegalStateException(
						"Byte array has invalid size: " + Arrays.toString(Arrays.copyOf(array, size)));
			}
		}

		public boolean isEmptyLine() {
			if (size >= 4) {
				return array[size - 1] == '\n' && array[size - 2] == '\r' && array[size - 3] == '\n'
						&& array[size - 4] == '\r';
			} else {
				return false;
			}
		}
	}

}
