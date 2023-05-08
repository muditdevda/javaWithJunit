package com.qat.exception;

public class DFSException extends Exception {
	private static final long serialVersionUID = -6053472156162047662L;
	protected String name = "DFSException";

	public DFSException(String msg) {
		super(msg);
	}

	@Override
	public String toString() {

		StringBuilder result = new StringBuilder(name +": ");
		for (StackTraceElement element : getStackTrace()) {
			result.append(element);
			result.append("\n");
		}
		return result.toString();
	}

	public String toStringShort() {
		return name + ": " + getMessage();
	}
}
