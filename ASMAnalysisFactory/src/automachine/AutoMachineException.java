/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-10-13 ����12:29:11
 * @modifier: Liuxizhiyi
 * @time 2008-10-13 ����12:29:11
 * @reviewer: Liuxizhiyi
 * @time 2008-10-13 ����12:29:11
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package automachine;

/**
 * 
 * @author Liuxizhiyi
 */
public class AutoMachineException extends Exception {
	static final long serialVersionUID = -3387516996578429948L;

	/**
	 * Constructs a new exception with <code>null</code> as its detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 */
	public AutoMachineException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * {@link #initCause}.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public AutoMachineException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 */
	public AutoMachineException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message
	 */
	public AutoMachineException(Throwable cause) {
		super(cause);
	}
}

// end
